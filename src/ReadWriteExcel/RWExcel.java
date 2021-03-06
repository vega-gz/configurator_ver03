package ReadWriteExcel;

import Tools.FileManager;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Node;

public class RWExcel {

    int startReadData = 0;
    private String path_file;

    public RWExcel(String s) { // коструктор сразу с определяющим имя именем
        path_file = s;
    }

    public RWExcel() { // по умолчанию(можно не использовать)

    }

    public void setPathF(String path_file) {
        this.path_file = path_file;
    }

    /**
     * --- Данные из ячейки по ссылке на строку и имени столбца---Lev---
     *
     */
    private static String getDataCell(Row row, String colName) {
        if (row == null) {
            return null;
        }
        int numberCol = CellReference.convertColStringToIndex(colName); // Переводим имя в индекс
        return getDataCell(row, numberCol);
    }

    // --- Данные из ячейки по ссылке на строку и номеру столбца---Lev---
    private static String getDataCell(Row row, int numberCol) {
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(numberCol); // адрес индекс
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING: {
                if (cell.getStringCellValue().contains("'")) {
                    return cell.getStringCellValue().replaceAll("'", "");
                }
                return cell.getStringCellValue();  // // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
            }
            case BLANK:
                return "";
            case NUMERIC:
                return "" + cell.getNumericCellValue(); // Double.toString(Double
            case FORMULA:
                // System.out.println("Formula");
                switch (cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        return "" + cell.getNumericCellValue();
                    case STRING:
                        return cell.getRichStringCellValue().toString().replaceAll("'", "");
                }
            default:
                return "";
        }
    }

    /**
     * Для Excel - Преобразование буквы столбца в номер столбца ---Lev---
     *
     * @param colName
     * @param colList
     * @return
     */
    private static int getColNumber(String colName, ArrayList<Node> colList) {
        for (int i = 0; i < colList.size(); i++) {
            if (colName.equals(globVar.sax.getDataAttr(colList.get(i), "nameColumnPos"))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Замена значений на словарные ---Lev---
     *
     * @param file
     * @param src
     * @return
     */
    private static String getFromDict(String file, String src) {
        XMLSAX dictSax = new XMLSAX();
        Node dict = dictSax.readDocument(file);
        ArrayList<Node> wordList = dictSax.getHeirNode(dict);
        for (Node wordNode : wordList) {
            String word = wordNode.getNodeName();
            if (word.length() <= src.length() && word.equals(src.substring(0, word.length()))) {
                String end = dictSax.getDataAttr(wordNode, "end");
                if (end == null || end != null && end.equals(src.substring(src.length() - end.length()))) {
                    return dictSax.getDataAttr(wordNode, "chng");
                }
            }
        }
        return "";
    }

    private static String getFromSwitch(Node col, String dataFromExcel) {
        if (dataFromExcel == null || col == null) {
            return null;
        }
        dataFromExcel.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
        String[] caseArr = {"case", "val", dataFromExcel};
        Node cse = globVar.sax.findNodeAtribute(col, caseArr);
        return globVar.sax.getDataAttr(cse, "def");
    }

    private static String getFromReplace(Node col, String data) {//, String dataFromExcel){
        if (data == null || col == null) {
            return null;
        }
        String s = data.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
        Node cse = globVar.sax.findNodeAtribute(col, new String[]{"case", "from", s});
        if (cse == null) {
            return data;
        }
        return globVar.sax.getDataAttr(cse, "to");
    }

    private static String getModbusDataBit(int i, int colCnt, String[][] dataFromExcel, String func) {
        if (i == 0) {
            return "0";
        }
        try {
            if ("2".equals(func) || "15".equals(func)) {
                int bit = (int) Double.parseDouble(dataFromExcel[i - 1][colCnt]);
                return "" + (bit + 1);
            }
            String prev = dataFromExcel[i - 1][colCnt];
            int dot = prev.indexOf(".");
            int bit = 0;
            int reg = 0;
            if (dot > 0) {
                reg = Integer.parseInt(prev.substring(0, dot));
                bit = Integer.parseInt(prev.substring(dot + 1)) + 1;
            } else {
                reg = Integer.parseInt(prev);
                bit = 1;
            }
            if (bit > 15) {
                bit = 0;
                reg++;
            }
            if (bit == 0) {
                return "" + reg;
            }
            return reg + "." + bit;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String getModbusDataReg(int i, int colCnt, int x, String[][] dataFromExcel) {
        if (i == 0) {
            return "0";
        }
        String format = dataFromExcel[i - 1][x];
        XMLSAX sax = new XMLSAX();
        Node root = sax.readDocument("ModbusFormat.xml");
        Node formNode = sax.returnFirstFinedNode(root, format);
        try {
            int step = Integer.parseInt(sax.getDataAttr(formNode, "step"));
            return "" + (Integer.parseInt(dataFromExcel[i - 1][colCnt]) + step);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double strToDoble(String op, Node f, ArrayList<Node> colList, String[][] dataFromExcel, int i) {
        try {
            return Double.parseDouble(op);
        } catch (NumberFormatException e) {
            Node f1 = globVar.sax.returnFirstFinedNode(f, op);
            String operation = globVar.sax.getDataAttr(f1, "operation");
            return calcFormula(operation, f1, f, colList, dataFromExcel, i);
        }
    }

    private static Double calcFormula(String operation, Node f1, Node f, ArrayList<Node> colList, String[][] dataFromExcel, int i) {
        if ("get".equals(operation)) {
            String colName = globVar.sax.getDataAttr(f1, "column");
            int x = getColNumber(colName, colList);
            if (x >= 0) {
                try {
                    return Double.parseDouble(dataFromExcel[i][x]);
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        Double op1 = strToDoble(globVar.sax.getDataAttr(f1, "operand1"), f, colList, dataFromExcel, i);
        Double op2 = strToDoble(globVar.sax.getDataAttr(f1, "operand2"), f, colList, dataFromExcel, i);
        if (op1 == null || op2 == null) {
            return null;
        }
        switch (operation) {
            case "+":
                return op1 + op2;
            case "-":
                return op1 - op2;
            case "*":
                return op1 * op2;
            case "/":
                if (op2 != 0) {
                    return op1 / op2;
                } else {
                    return null;
                }
        }
        return null;
    }

    private static Double getFormulaVal(String def, Node f, ArrayList<Node> colList, String[][] dataFromExcel, int i) {
        Node f1 = globVar.sax.returnFirstFinedNode(f, def);
        if (f1 == null) {
            return null;
        }
        String operation = globVar.sax.getDataAttr(f1, "operation");
        return calcFormula(operation, f1, f, colList, dataFromExcel, i);
    }

    // --- Получить название листов с файла ---
    public static ArrayList<String> getListSheetName(String pathExel) {
        ArrayList<String> listSheets = new ArrayList<>();
        Workbook wb = readDocument(pathExel);
            
        int qSheets = wb.getNumberOfSheets();
        for (int i = 0; i < qSheets; i++) {
            listSheets.add(wb.getSheetName(i));
        }
        return listSheets;
    }
    
    // --- прочитать файл ---
    private static Workbook readDocument(String pathExel){
        FileInputStream inputStream = null;
        ArrayList<String> listSheets = new ArrayList<>();
        Workbook wb = null;
        try {
            inputStream = new FileInputStream(new File(pathExel));
            if (inputStream == null) {
                FileManager.loggerConstructor("Не удалось открыть файл " + pathExel);
                return null;
            }
            String execut = pathExel.substring(pathExel.lastIndexOf(".") + 1); // получить расширение файла
            if (execut.equalsIgnoreCase("xlsx") | execut.equalsIgnoreCase("xlsm")) {
                wb = new XSSFWorkbook(inputStream);
            } else {
                wb = new HSSFWorkbook(inputStream);
            }   
            if (wb == null) {
                FileManager.loggerConstructor("Файл " + pathExel + " повреждён или это не XLS");
                return null;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RWExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RWExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(RWExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return wb;
    }

    /**
     * --- сформировать даные из конфигугации XML для чтения Exel---Lev---
     *
     * @param pathExel
     * @param jpb
     * @return
     */
    public static String ReadExelFromConfig(String pathExel, String nameSheet, String toTableInsert,  JProgressBar jpb) {  // pathExel Временно так как мозгов не хватило ночью.                
        Workbook wb = readDocument(pathExel);
        if (wb == null) {
            FileManager.loggerConstructor("Файл " + pathExel + " повреждён или это не XLS");
            return null;
        }

        int qSheets = 0;
        String[] listSheets = null;
        if(nameSheet == null){ // если нет конкретной страницы
            qSheets = wb.getNumberOfSheets();
            listSheets = new String[qSheets];
            for (int i = 0; i < qSheets; i++) {
                listSheets[i] = wb.getSheetName(i);
            }
        }else{
            listSheets = new String[]{nameSheet};
            qSheets = 1;
        }

        FileManager.loggerConstructor("Заливаем в таблицы абонента " + globVar.abonent + " данные из книги " + pathExel);
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);
        boolean isError = false;
        String tCnt = "";
        int maxCnt = nList.size() - 1;
        int nCnt = 0;
        for (Node n : nList) { // вот тут по нодам перебирает от этого не верно 
            if (jpb != null) {
                jpb.setValue((int) ((nCnt++) * 100.0 / maxCnt));//Прогресс загрузки данных из екселя в БД
            }            //String tableName = n.getNodeName();
            
            String exSheetName1 = null;
            if(toTableInsert != null & nameSheet != null) exSheetName1 = nameSheet; // костыль для загрузки одного сигнала
            else exSheetName1 = globVar.sax.getDataAttr(n, "excelSheetName");
            
            ArrayList<String> sheetList = new ArrayList<>();
            
            if ("mb".equals(exSheetName1.substring(0, 2))) {
                for (int i = 0; i < qSheets; i++) {
                    int sl = listSheets[i].length();
                    int tl = exSheetName1.length();
                    // это из за идиотизма какого то в новом файле( который дали)
                    //if (sl >= tl) {
                    //String newNameSheet = listSheets[i].substring(sl - tl).replace("#", "");
                    //newNameSheet = newNameSheet.replace("$", "");
                    if (sl >= tl && listSheets[i].substring(sl - tl).equals(exSheetName1)) {  //так было до изменения    
                        //if (newNameSheet.equals(exSheetName1)) {
                        sheetList.add(listSheets[i]);
                            //tableName = listSheets[i];
                        //break;
                    }
                    // }
                }
            } else {
                sheetList.add(exSheetName1);
            }

            for (String exSheetName : sheetList) {//тут начинает формирование по листу ,который мы выбрали
                //HSSFSheet sheet = wb.getSheet(exSheetName);
                Sheet sheet = wb.getSheet(exSheetName);
                int rowMax = 32767;
                int starExcelString = 1;
                String tableComment = globVar.sax.getDataAttr(n, "Comment");// !!!!!!!!в этом моменте делает неправильный коммент(беет коммент из AI)!!!!!!!
                if (sheet != null) {
                    String func = "";
                    Node excelNode = globVar.sax.returnFirstFinedNode(n, "EXEL");
                    if (excelNode != null) {
                        String info = globVar.sax.getDataAttr(excelNode, "info");
                        if (info != null) {
                            int x = info.indexOf("C");
                            int row = Integer.parseInt(info.substring(1, x));
                            int col = Integer.parseInt(info.substring(x + 1));
                            tableComment += getDataCell(sheet.getRow(row - 1), col - 1);
                            func = tableComment.substring(tableComment.lastIndexOf(";") + 1).trim();
                        }
                        String startStr = globVar.sax.getDataAttr(excelNode, "startStr");
                        if (startStr != null) {
                            try {
                                starExcelString = Integer.parseInt(startStr) - 1;
                            } catch (NumberFormatException e) {
                                starExcelString = -1;
                            }
                            if (starExcelString < 1 || starExcelString > 19) {
                                FileManager.loggerConstructor("В настройке \"startStr\" ноды \"EXEL\" типа данных "
                                        + n.getNodeName() + " неправильное значение \"" + startStr + "\". Должно быть целое от 2 до 20.");
                                isError = true;
                                starExcelString = 1;
                            }
                        }

                    }
                    ArrayList<Node> colList = globVar.sax.getHeirNode(excelNode);
                    //ArrayList<String[]> dataFromExcel = new ArrayList<>();
                    String[][] dataFromExcel = null;
                    String[] tabColNames = new String[colList.size()];
                    int colCnt = 0;
                    boolean first = true;
                    for (Node col : colList) {
                        String colExName = col.getNodeName();
                        tabColNames[colCnt] = globVar.sax.getDataAttr(col, "nameColumnPos");
                        if (first) {
                            for (int i = starExcelString; i < rowMax; i++) {
                                String strCell = getDataCell(sheet.getRow(i), colExName);
                                if (strCell == null || "".equals(strCell)) {
                                    first = false;
                                    rowMax = i - starExcelString;
                                }
                            }
                            dataFromExcel = new String[rowMax][colList.size()];
                        }
                        for (int i = 0; i < rowMax; i++) {//Пробегаем по строкам столбца 
                            String strCell = getDataCell(sheet.getRow(i + starExcelString), colExName);
                            if (strCell == null) {
                                strCell = "";
                            }
                            if ("".equals(strCell)) {
                                String def = globVar.sax.getDataAttr(col, "swt");
                                if (def != null) {
                                    int x = getColNumber(def, colList);
                                    if (x >= 0) {
                                        dataFromExcel[i][colCnt] = getFromSwitch(col, dataFromExcel[i][x]);
                                    }
                                    if (dataFromExcel[i][colCnt] == null) {
                                        def = null;
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "dictionary");
                                    if (def != null) {
                                        String colName = globVar.sax.getDataAttr(col, "sourceCol");
                                        int x = getColNumber(colName, colList);
                                        if (x >= 0) {
                                            dataFromExcel[i][colCnt] = getFromDict(def, dataFromExcel[i][x]);
                                        } else {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "asPrev");
                                    if (def != null) {
                                        if (i > 0) {
                                            dataFromExcel[i][colCnt] = dataFromExcel[i - 1][colCnt];
                                        } else {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "modbus");
                                    if (def != null) {
                                        if ("bit".equals(def)) {
                                            dataFromExcel[i][colCnt] = getModbusDataBit(i, colCnt, dataFromExcel, func);
                                        } else {
                                            int x = getColNumber(def, colList);
                                            if (x >= 0) {
                                                dataFromExcel[i][colCnt] = getModbusDataReg(i, colCnt, x, dataFromExcel);
                                            } else {
                                                def = null;
                                            }
                                        }
                                        if (dataFromExcel[i][colCnt] == null) {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "formula");
                                    if (def != null) {
                                        Node f = globVar.sax.returnFirstFinedNode(n, "Formulas");
                                        Double x = getFormulaVal(def, f, colList, dataFromExcel, i);
                                        if (x != null) {
                                            dataFromExcel[i][colCnt] = "" + (Math.round(x * 10000.0) / 10000.0);
                                        } else {
                                            dataFromExcel[i][colCnt] = "";
                                            FileManager.loggerConstructor("Для ячейки " + colExName + i + " листа " + exSheetName + " не удалось посчитать значение");
                                            isError = true;
                                        }
                                        //calcFormula(def,f1,f,colList, dataFromExcel, i);
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "default");
                                    if (def != null) {
                                        dataFromExcel[i][colCnt] = def;
                                    }
                                }
                                if (def == null) {
                                    FileManager.loggerConstructor("В ячейке " + colExName + i + " листа " + exSheetName + " должно быть значение");
                                    isError = true;
                                    dataFromExcel[i][colCnt] = "";
                                }
                            } else {
                                String registr = globVar.sax.getDataAttr(col, "registr");
                                if (registr != null) {
                                    if (registr.equalsIgnoreCase("UP")) {
                                        strCell = strCell.toUpperCase();
                                    } else {
                                        strCell = strCell.toLowerCase();
                                    }
                                }
                                String unical = globVar.sax.getDataAttr(col, "unical");
                                if (unical != null) {
                                    for (int j = 0; j < i; j++) {
                                        if (strCell.equals(dataFromExcel[j][colCnt])) {
                                            FileManager.loggerConstructor("Одинаковые значения \"" + strCell + "\" в ячейках " + colExName + (j + 1) + " и " + colExName + (i + 1) + " листа " + exSheetName);
                                            isError = true;
                                        }
                                    }
                                }
                                String replace = globVar.sax.getDataAttr(col, "replace");
                                if (replace != null) {
                                    strCell = getFromReplace(col, strCell);
                                }
                                String type = globVar.sax.getDataAttr(col, "type");
                                if (type != null) {
                                    if ("Int".equals(type)) {
                                        try {
                                            strCell = "" + ((int) Double.parseDouble(strCell));
                                        } catch (NumberFormatException e) {
                                            FileManager.loggerConstructor("Неправильное значение \"" + strCell + "\" в ячейке " + colExName + (i + 1) + " листа " + exSheetName);
                                        }
                                    } else if ("Number".equals(type)) {
                                        try {
                                            Double tmp = Double.parseDouble(strCell);
                                            strCell = "" + (Math.round(tmp * 10000.0) / 10000.0);
                                        } catch (NumberFormatException e) {
                                            FileManager.loggerConstructor("Неправильное значение \"" + strCell + "\" в ячейке " + colExName + (i + 1) + " листа " + exSheetName);
                                        }
                                    }
                                }
                                dataFromExcel[i][colCnt] = strCell;
                            }
                        }
                        colCnt++;
                    }
                    String nameT =  null;
                    if(toTableInsert != null & nameSheet != null) nameT = toTableInsert; // если есть есть конкретная таблица для внесения и если есть конкретный Лист из excell
                    else nameT = globVar.abonent + "_" + exSheetName;
                    
                    globVar.DB.createTable(nameT, tabColNames, dataFromExcel, tableComment);
                    tCnt += "\n - " + globVar.abonent + "_" + exSheetName;
                }
            }
            if(toTableInsert != null & nameSheet != null) break; // не носимвся по всем нодам XML а сразу выходим
        }
        if (isError) {
            return null;
        }
        return tCnt;
    }
    public static String ReadExcelSheet(String pathExel, String nameSheet, String toTableInsert){//totableInsert это полное имя таблицы в которую грузим
        XMLSAX sax=new XMLSAX();
         Workbook wb = readDocument(pathExel);
        if (wb == null) {
            FileManager.loggerConstructor("Файл " + pathExel + " повреждён или это не XLS");
            return null;
        }
        String []listSheets = new String[]{nameSheet};//массив из выбранных листов(вообще мы выбираем один лист из EXCEL ,так что надо будет поменять,пока по старинке делаю)
        int qSheets = 1;
        
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);
        boolean isError = false;
        String tCnt = "";
        String nodeList;//переменная ,значение которой ищем в ноде
        if(toTableInsert.contains("mb")){
            nodeList=toTableInsert.substring(toTableInsert.indexOf("mb"),toTableInsert.length());
        }else{
            nodeList=toTableInsert.substring(toTableInsert.lastIndexOf("_")+1,toTableInsert.length());
        }
            Node nSheet=sax.returnFirstFinedNode(globVar.cfgRoot,nodeList);//здесь мы ищем в ноде
            if(nSheet!=null){//если нашли ноду,начинаем формировать 
               Sheet sheet = wb.getSheet(nameSheet);//здесь мы ищем лист в EXCEL
                int rowMax = 32767;
                int starExcelString = 1;
                String tableComment = globVar.DB.getCommentTable(toTableInsert);//получили комментарий к таблице
                if(sheet!=null){
                    String func = "";
                    Node excelNode = globVar.sax.returnFirstFinedNode(nSheet, "EXEL");
                    if(excelNode!=null){
                           String info = globVar.sax.getDataAttr(excelNode, "info");
                        if (info != null) {
                            int x = info.indexOf("C");
                            int row = Integer.parseInt(info.substring(1, x));
                            int col = Integer.parseInt(info.substring(x + 1));
                            tableComment += getDataCell(sheet.getRow(row - 1), col - 1);
                            func = tableComment.substring(tableComment.lastIndexOf(";") + 1).trim();
                        }
                        
                        
                        //в этом блоке пока не разобрался,но это проверки в xml необходимые
                          String startStr = globVar.sax.getDataAttr(excelNode, "startStr");
                          if (startStr != null) {
                            try {
                                starExcelString = Integer.parseInt(startStr) - 1;
                            } catch (NumberFormatException e) {
                                starExcelString = -1;
                            }
                            if (starExcelString < 1 || starExcelString > 19) {
                                FileManager.loggerConstructor("В настройке \"startStr\" ноды \"EXEL\" типа данных "
                                        + nSheet.getNodeName() + " неправильное значение \"" + startStr + "\". Должно быть целое от 2 до 20.");
                                isError = true;
                                starExcelString = 1;
                            }
                        }
                    }
                    ArrayList<Node> colList = globVar.sax.getHeirNode(excelNode);//получили детей ноды EXCEL
                    String[][] dataFromExcel = null;
                    String[] tabColNames = new String[colList.size()];//сформировали из детей массив
                    int colCnt = 0;//порядковый номер в массиве
                    boolean first = true;
                    for(Node col : colList){//бежим по детям EXCEL строим таблицу
                        String colExName = col.getNodeName();
                        tabColNames[colCnt] = globVar.sax.getDataAttr(col, "nameColumnPos");//получили значение атрибута и записали его в массив
                        if(first){
                              for (int i = starExcelString; i < rowMax; i++) {
                                String strCell = getDataCell(sheet.getRow(i), colExName);//получили данные из ячейки в excel со столбца colExName
                                if (strCell == null || "".equals(strCell)) {//проверка пустые ли ячейки
                                    first = false;
                                    rowMax = i - starExcelString;
                                }
                            }
                               dataFromExcel = new String[rowMax][colList.size()];
                        }
                        
                        
                        for(int i=0;i<rowMax;i++){//Пробегаем по строкам столбца 
                            String strCell = getDataCell(sheet.getRow(i + starExcelString), colExName);
                            if (strCell == null) {
                                strCell = "";
                            }
                             if ("".equals(strCell)) {
                                String def = globVar.sax.getDataAttr(col, "swt");
                                if (def != null) {
                                    int x = getColNumber(def, colList);
                                    if (x >= 0) {
                                        dataFromExcel[i][colCnt] = getFromSwitch(col, dataFromExcel[i][x]);
                                    }
                                    if (dataFromExcel[i][colCnt] == null) {
                                        def = null;
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "dictionary");
                                    if (def != null) {
                                        String colName = globVar.sax.getDataAttr(col, "sourceCol");
                                        int x = getColNumber(colName, colList);
                                        if (x >= 0) {
                                            dataFromExcel[i][colCnt] = getFromDict(def, dataFromExcel[i][x]);
                                        } else {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "asPrev");
                                    if (def != null) {
                                        if (i > 0) {
                                            dataFromExcel[i][colCnt] = dataFromExcel[i - 1][colCnt];
                                        } else {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "modbus");
                                    if (def != null) {
                                        if ("bit".equals(def)) {
                                            dataFromExcel[i][colCnt] = getModbusDataBit(i, colCnt, dataFromExcel, func);
                                        } else {
                                            int x = getColNumber(def, colList);
                                            if (x >= 0) {
                                                dataFromExcel[i][colCnt] = getModbusDataReg(i, colCnt, x, dataFromExcel);
                                            } else {
                                                def = null;
                                            }
                                        }
                                        if (dataFromExcel[i][colCnt] == null) {
                                            def = null;
                                        }
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "formula");
                                    if (def != null) {
                                        Node f = globVar.sax.returnFirstFinedNode(nSheet, "Formulas");
                                        Double x = getFormulaVal(def, f, colList, dataFromExcel, i);
                                        if (x != null) {
                                            dataFromExcel[i][colCnt] = "" + (Math.round(x * 10000.0) / 10000.0);
                                        } else {
                                            dataFromExcel[i][colCnt] = "";
                                            FileManager.loggerConstructor("Для ячейки " + colExName + i + " листа " + nameSheet + " не удалось посчитать значение");
                                            isError = true;
                                        }
                                        //calcFormula(def,f1,f,colList, dataFromExcel, i);
                                    }
                                }
                                if (def == null) {
                                    def = globVar.sax.getDataAttr(col, "default");
                                    if (def != null) {
                                        dataFromExcel[i][colCnt] = def;
                                    }
                                }
                                if (def == null) {
                                    FileManager.loggerConstructor("В ячейке " + colExName + i + " листа " + nameSheet + " должно быть значение");
                                    isError = true;
                                    dataFromExcel[i][colCnt] = "";
                                }
                            } else {
                                String registr = globVar.sax.getDataAttr(col, "registr");
                                if (registr != null) {
                                    if (registr.equalsIgnoreCase("UP")) {
                                        strCell = strCell.toUpperCase();
                                    } else {
                                        strCell = strCell.toLowerCase();
                                    }
                                }
                                String unical = globVar.sax.getDataAttr(col, "unical");
                                if (unical != null) {
                                    for (int j = 0; j < i; j++) {
                                        if (strCell.equals(dataFromExcel[j][colCnt])) {
                                            FileManager.loggerConstructor("Одинаковые значения \"" + strCell + "\" в ячейках " + colExName + (j + 1) + " и " + colExName + (i + 1) + " листа " + nameSheet);
                                            isError = true;
                                        }
                                    }
                                }
                                String replace = globVar.sax.getDataAttr(col, "replace");
                                if (replace != null) {
                                    strCell = getFromReplace(col, strCell);
                                }
                                String type = globVar.sax.getDataAttr(col, "type");
                                if (type != null) {
                                    if ("Int".equals(type)) {
                                        try {
                                            strCell = "" + ((int) Double.parseDouble(strCell));
                                        } catch (NumberFormatException e) {
                                            FileManager.loggerConstructor("Неправильное значение \"" + strCell + "\" в ячейке " + colExName + (i + 1) + " листа " + nameSheet);
                                        }
                                    } else if ("Number".equals(type)) {
                                        try {
                                            Double tmp = Double.parseDouble(strCell);
                                            strCell = "" + (Math.round(tmp * 10000.0) / 10000.0);
                                        } catch (NumberFormatException e) {
                                            FileManager.loggerConstructor("Неправильное значение \"" + strCell + "\" в ячейке " + colExName + (i + 1) + " листа " + nameSheet);
                                        }
                                    }
                                }
                                dataFromExcel[i][colCnt] = strCell;
                            }
                        }
                        colCnt++;
                    }
                     String nameT =  null;
                    if(toTableInsert != null & nameSheet != null) nameT = toTableInsert; // если есть есть конкретная таблица для внесения и если есть конкретный Лист из excell
                    else nameT = globVar.abonent + "_" + nameSheet;
                    
                     globVar.DB.createTable(nameT, tabColNames, dataFromExcel, tableComment);
                    tCnt += "\n - " + globVar.abonent + "_" + nameSheet;
                }
                
            }
          //  if(toTableInsert != null & nameSheet != null) break; // не носимвся по всем нодам XML а сразу выходим
        
        
        
         if (isError) {
            return null;
        }
        
        return tCnt;
    }
       public static String[] getExcelChild(String typeTable,String[] tableCols,ArrayList<String> nColList ) {//возвращает детей EXCEL ноды
           XMLSAX xmlsax=new XMLSAX();
        ArrayList<Node> nList = globVar.sax.getHeirNode(globVar.cfgRoot);//получили детей ноды ConfigSignals
        ArrayList<Node> eList;
        for (int i = 0; i < nList.size(); i++) {
            if (nList.get(i).getNodeName().equals(typeTable)) {
                Node excel = xmlsax.returnFirstFinedNode(nList.get(i), "EXEL");//нашли ноду
                eList = xmlsax.getHeirNode(excel);
                for (int j = 0; j < eList.size(); j++) {//получили детей ноды excel ,в которых хранятся наименования столбцов
                    String nCol = xmlsax.getDataAttr(eList.get(j), "nameColumnPos");
                    nColList.add(nCol);
                }
            }
        }
        tableCols = nColList.toArray(new String[nColList.size()]);

        return tableCols;
    }

    

    public void fillTag_NAME_PLC() {

    }

}
