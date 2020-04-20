/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author cherepanov
 */
public class RWExcel {

    public RWExcel(String s) { // коструктор сразу с определяющим имя именем
        setPatchF(s);
    }

    public RWExcel() { // по умолчанию(можно не использовать)
    }
    int startReadData = 0;
    private String path_file;

    public void setPatchF(String patch_file) {
        this.path_file = patch_file;
    }

    public void readAllfile() throws IOException {

        // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(path_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);

        ArrayList<String> list_sheet = new ArrayList<String>();

        // проганаяем список Листов в файле 
        Iterator<Sheet> it_sheet = wb.iterator();
        int tmp = 0;
        while (it_sheet.hasNext()) {
            Sheet sheet = it_sheet.next();
            System.out.println(sheet.getSheetName());
            tmp++;
            list_sheet.add(sheet.getSheetName());
        }

        Iterator<String> iter_list_sheet = list_sheet.iterator();
        while (iter_list_sheet.hasNext()) {
            String name_sheet = iter_list_sheet.next();
            System.out.println(name_sheet);
            Sheet sheet = wb.getSheet(name_sheet);

            //Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator(); // итератор Строк
            int len_row = 0;
            int max_len_row = 0;
            while (it.hasNext()) {
                Row row = it.next();
                len_row = row.getLastCellNum();
                if (len_row > max_len_row) {
                    max_len_row = len_row;
                }

                Iterator<Cell> cells = row.iterator(); // итератор Ячеек
                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    CellType cellType = cell.getCellType();

                    switch (cellType) {
                        case STRING:
                            System.out.print(cell.getStringCellValue());
                            break;
                        case BLANK:
                            System.out.print(cell.getColumnIndex());
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue());
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula());
                            break;
                        default:
                            System.out.print("|");
                    }

                    //System.out.print("[" + cell.getAddress()+ " " + cell.getCellType()+ "]");
                }

                System.out.println();

            }
            String unicId = getUIID();
            System.out.println("Max_len_in_string_row " + Integer.toString(max_len_row) + " Unic ID " + getUIID());
        }

    }

    String getUIID() {
        java.util.UUID uniqueKey = java.util.UUID.randomUUID();
        Date dateNow = new Date();
        //  SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMddhhmmsss"); //формируем дату как нам вздумается

        //String uiid_str = uniqueKey.toString().replace("-", "") + formatForDateNow.format(dateNow).toString();
        String uiid_str = uniqueKey.toString().replace("-", "");
        return uiid_str;
    }

    public int getMaxcColumn(String name_sheet) throws FileNotFoundException, IOException {

        // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(path_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);

        System.out.println(name_sheet);
        Sheet sheet = wb.getSheet(name_sheet);
        Iterator<Row> it = sheet.iterator(); // итератор Строк
        int max_len_row = 0;
        while (it.hasNext()) {
            Row row = it.next();
            int len_row = row.getLastCellNum();
            if (len_row > max_len_row) {
                max_len_row = len_row;
            }
        }

        return max_len_row;
    }

    public ArrayList<String> get_list_sheet() throws FileNotFoundException, IOException {
        // Read XSL file
        FileInputStream inputStream = new FileInputStream(new File(path_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);

        ArrayList<String> list_sheet = new ArrayList<String>();

        // проганаяем список Листов в файле 
        Iterator<Sheet> it_sheet = wb.iterator();
        while (it_sheet.hasNext()) {
            Sheet sheet = it_sheet.next();
            list_sheet.add(sheet.getSheetName());
        }
        return list_sheet;
    }

    // --- Получить листы файла указав на него строкой  ---
    public ArrayList<String> get_list_sheet(String path) {
        ArrayList<String> list_sheet = new ArrayList<String>();
        try {
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(new File(path));
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            // проганаяем список Листов в файле
            Iterator<Sheet> it_sheet = wb.iterator();
            while (it_sheet.hasNext()) {
                Sheet sheet = it_sheet.next();
                list_sheet.add(sheet.getSheetName());
            }
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(RWExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list_sheet;
    }

    // --- Geting data from file Exel ----
    public ArrayList<String[]> getDataCell(String name_sheet, int lenmass) throws FileNotFoundException, IOException {
        String[] array_cell_len;
        ArrayList<String[]> array_cell = new ArrayList<>();

        FileInputStream inputStream = new FileInputStream(new File(path_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheet(name_sheet);

        // начальные значения наверное просто для инициализации
        int first_len = 0; // получили переменую строки с именами столбцов;
        int tmpFirstLenght = 0;
        int col_UUID = 0;
        int startm = 1;
        //array_cell_len = new String[lenmass + col_UUID]; // Почему +1

        switch (name_sheet) { // Высчитываем с какой строки заполнять таблицу и 4 UUID - 3 доп
            case "AI1":
            case "AO1":
            case "DI1": //case "DO1": // Что то у нас с этой Книгой в файле
            case "DO1": {
                //first_len = 3; // с 4 строки файла Это надо анализировать а не задавать 
                first_len = startReadData; // получили переменую строки с именами столбцов
                col_UUID = 4; // 4 UUID
                startm = 4; // это моя идиотия тут из за того что я решил тут вносить UUID в базу
                array_cell_len = new String[lenmass + col_UUID]; // Почему +1
            }
            break;
            default: { // если нечего не найдено из одходящего
                first_len = startReadData;
                startm = 1; // данные с первого так как один UUID 
                col_UUID = 1;
                array_cell_len = new String[lenmass + col_UUID]; // Почему +1 Так как одни данные под UUID
            }
        }

        Iterator<Row> it = sheet.iterator(); // итератор Строк
        int sum_sheet = 0;
        int len_row = 0;
        int max_len_row = 0;

        while (it.hasNext()) {
            while (tmpFirstLenght < first_len) { // а вот есть ли там данные
                it.next();
                ++tmpFirstLenght;
            }
            //++sum_sheet;
            if (it.hasNext()) { // проверка есть ли вообще данные после пропуска строк
                Row row = it.next();
                // System.out.println(row.getFirstCellNum() + " " + row.getLastCellNum()); //в строку что бы посмотреть что за нах
                int tmp = 0;
                //заносим Кол UUID
                int tmp_UUID = 1;
                do {
                    array_cell_len[tmp] = getUIID();
                    tmp_UUID++;
                    tmp++;
                } while (tmp_UUID <= col_UUID);

                //array_cell_len[tmp]= getUIID(); // так было до For
                // System.out.println(array_cell_len[tmp]);
                // tmp++;
                //System.out.println(row.getLastCellNum());
                Iterator<Cell> cells = row.cellIterator(); // итератор Ячеек вот не работает должным образом пропускает ячейки
                int i_tmp = 0;

                //  while (cells.hasNext()) {
                //      Cell cell = cells.next();
                while (i_tmp < array_cell_len.length - startm) {
                    Cell cell = row.getCell(i_tmp);

                    //System.out.println(cell.getAddress());
                    /* CellAddress cellReference = new CellAddress("Q110");
                     if (cell.getAddress().equals(cellReference)){
                     System.out.println(cell.getAddress());
                     System.out.println(cell.getCellType());
                     }*/
                    //System.out.println(cell.getAddress()); // Для проверки сдвига
                    /* System.out.println(cell.getAddress());
                     System.out.println(i_tmp);
                     System.out.println(row.getLastCellNum());*/
                    if (cell != null) {  // обходим таким дебильным способом
                        CellType cellType = cell.getCellType();
                        switch (cellType) {
                            case STRING: {
                                if (cell.getStringCellValue().contains("'")) {
                                    //System.out.print("Find ' ->  " + cell.getStringCellValue());
                                    array_cell_len[tmp] = cell.getStringCellValue().replaceAll("'", "");
                                }
                                array_cell_len[tmp] = cell.getStringCellValue();  // // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                            }
                            break;
                            case BLANK:
                                array_cell_len[tmp] = "NULL";
                                break;
                            case NUMERIC:
                                array_cell_len[tmp] = Double.toString(cell.getNumericCellValue()); // Double
                                break;
                            //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                            case FORMULA:
                                // System.out.println("Formula");
                                switch (cell.getCachedFormulaResultType()) {
                                    case NUMERIC:
                                        array_cell_len[tmp] = (Double.toString(cell.getNumericCellValue()));
                                        break;
                                    case STRING:
                                        array_cell_len[tmp] = cell.getRichStringCellValue().toString();
                                        array_cell_len[tmp] = array_cell_len[tmp].replaceAll("'", ""); // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                                        //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                                        break;
                                }
                                break;
                            default:
                                array_cell_len[tmp] = "|";
                                break;
                        }
                    } else {
                        array_cell_len[tmp] = "NULL"; // Вот тут чего такое то?
                    }

                    tmp++;
                    i_tmp++;
                }

                for (int i = 0; i < array_cell_len.length; i++) {
                    // System.out.print(array_cell_len[i] + " " );
                }
                //System.out.println();

                // ПРоверяемс считались какие то данные из ячеек строки (1 так как первый элемент занят ID) 
                //Желательно переписать
                int not_null_dat = 0;
                for (int i = 1; i < array_cell_len.length; i++) {
                    if (array_cell_len[i].isEmpty()) {
                        continue;
                    } else {
                        not_null_dat = 1;
                        break;
                    }
                }
                if (not_null_dat != 0) {
                    String[] tmp_array_cell_len = Arrays.copyOf(array_cell_len, array_cell_len.length);

                    // Проверяем пустой ли массив который мы заносим, так как Exel думает что есть данные
                    boolean empty = true;
                    //  if(tmp_array_cell_len.length != 0){    //массив может быть пустой
                    for (int i = startm; i < tmp_array_cell_len.length; i++) {
                        //if (!tmp_array_cell_len[i].equals("NULL") |  tmp_array_cell_len[i] != null) {
                        if (tmp_array_cell_len[i] == null || tmp_array_cell_len[i].equals("NULL") || tmp_array_cell_len[i].equals("")) {
                            empty = true;
                            //System.out.println("This find => " + tmp_array_cell_len[i]);
                        } else {
                            // System.out.println("This Else => " + tmp_array_cell_len[i]);
                            empty = false;
                            break;
                        }
                    }
                    //   }
                    if (!empty) {
                        array_cell.add(tmp_array_cell_len);
                    } // не пусто тогда заносим.
                    //array_cell.add(tmp_array_cell_len);
                    not_null_dat = 0;
                }
                //обнуляем массив для проверки выше если строки программа видит но они пустые.
                // array_cell_len = null;
                for (int i = 0; i < array_cell_len.length; i++) {
                    array_cell_len[i] = "";
                }
            }
        }

        //System.out.println( sum_sheet);
        System.out.println(array_cell.size() + " -number string in mass");
        return array_cell;
    }

// --- это второй метод Geting data from file Exel (Вытянуть данные из Exel  но по конкретным столбцам)----
    public ArrayList<String[]> getDataCell(String name_sheet, ArrayList<String> columns) {
        FileInputStream inputStream = null;
        String[] array_cell_len;
        ArrayList<String[]> array_cell = new ArrayList<>();
        try {
            inputStream = new FileInputStream(new File(path_file));
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheet(name_sheet);
            getRowReadData(sheet); // вызов фукции определения начала данных в на Листе
            // начальные значения наверное просто для инициализации
            int first_len = 0; // получили переменую строки с именами столбцов;
            int tmpFirstLenght = 0;
            int col_UUID = 0;
            int startm = 1;
            first_len = startReadData;
            startm = 1; // данные с первого так как один UUID
            col_UUID = 1;
            array_cell_len = new String[columns.size() + col_UUID]; // Почему +1 Так как одни данные под UUID
            Iterator<Row> rowIter = sheet.iterator(); // итератор Строк
            int sum_sheet = 0;
            int len_row = 0;
            int max_len_row = 0;
            while (rowIter.hasNext()) {
                while (tmpFirstLenght < first_len) { // а вот есть ли там данные
                    rowIter.next();
                    ++tmpFirstLenght;
                }
                if (rowIter.hasNext()) { // проверка есть ли вообще данные после пропуска строк
                    Row row = rowIter.next();
                    // System.out.println(row.getFirstCellNum() + " " + row.getLastCellNum()); //в строку что бы посмотреть что за нах
                    int tmp = 0;
                    //заносим Кол UUID
                    int tmp_UUID = 1;
                    do {
                        array_cell_len[tmp] = getUIID();
                        tmp_UUID++;
                        tmp++;
                    } while (tmp_UUID <= col_UUID);
                    //Iterator<Cell> cells = row.cellIterator(); // Итератор ячеек (Почему не применяю его ?)
                    //int i_tmp = 0;
                    for (int i_tmp = 0; i_tmp < columns.size(); ++i_tmp) { // пробегаемся по входному массиву
                        String nameCompareColumn = columns.get(i_tmp);
                            int numberCol = CellReference.convertColStringToIndex(nameCompareColumn); // Переводим имя в индекс
                            //System.out.println(CellReference.convertNumToColString(i_tmp)); // выявляем Имена стобцы в которых ячейка
                            Cell cell = row.getCell(numberCol); // адрес индекс
                            //cell.getRichStringCellValue().toString();
                            if (cell != null) {  // обходим таким дебильным способом
                                CellType cellType = cell.getCellType();
                                switch (cellType) {
                                    case STRING: {
                                        if (cell.getStringCellValue().contains("'")) {
                                            //System.out.print("Find ' ->  " + cell.getStringCellValue());
                                            array_cell_len[tmp] = cell.getStringCellValue().replaceAll("'", "");
                                        }
                                        array_cell_len[tmp] = cell.getStringCellValue();  // // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                                    }
                                    break;
                                    case BLANK:
                                        array_cell_len[tmp] = "";
                                        break;
                                    case NUMERIC:
                                        array_cell_len[tmp] = Double.toString(cell.getNumericCellValue()); // Double
                                        break;
                                    //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                                    case FORMULA:
                                        // System.out.println("Formula");
                                        switch (cell.getCachedFormulaResultType()) {
                                            case NUMERIC:
                                                array_cell_len[tmp] = (Double.toString(cell.getNumericCellValue()));
                                                break;
                                            case STRING:
                                                array_cell_len[tmp] = cell.getRichStringCellValue().toString();
                                                array_cell_len[tmp] = array_cell_len[tmp].replaceAll("'", ""); // убираю что бы не было трудностей с загрузкой в постгрес при этом ушли пустые строки
                                                //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                                                break;
                                        }
                                        break;
                                    default:
                                        array_cell_len[tmp] = "|";
                                        break;
                                }
                            } else {
                                array_cell_len[tmp] = ""; // Вот тут чего такое то?
                            }
                        tmp++;
                        //i_tmp++;
                    }
                    // ПРоверяемс считались какие то данные из ячеек строки (1 так как первый элемент занят ID)
                    //Желательно переписать
                    int not_null_dat = 0;
                    for (int i = 1; i < array_cell_len.length; i++) {
                        if (array_cell_len[i].isEmpty()) {
                            continue;
                        } else {
                            not_null_dat = 1;
                            break;
                        }
                    }
                    if (not_null_dat != 0) {
                        String[] tmp_array_cell_len = Arrays.copyOf(array_cell_len, array_cell_len.length);
                        // Проверяем пустой ли массив который мы заносим, так как Exel думает что есть данные
                        boolean empty = true;
                        //  if(tmp_array_cell_len.length != 0){    //массив может быть пустой
                        for (int i = startm; i < tmp_array_cell_len.length; i++) {
                            //if (!tmp_array_cell_len[i].equals("NULL") |  tmp_array_cell_len[i] != null) {
                            if (tmp_array_cell_len[i] == null || tmp_array_cell_len[i].equals("NULL") || tmp_array_cell_len[i].equals("")) {
                                empty = true;
                                //System.out.println("This find => " + tmp_array_cell_len[i]);
                            } else {
                                // System.out.println("This Else => " + tmp_array_cell_len[i]);
                                empty = false;
                                break;
                            }
                        }
                        //   }
                        if (!empty) {
                            array_cell.add(tmp_array_cell_len);
                        } // не пусто тогда заносим.
                        //array_cell.add(tmp_array_cell_len);
                        not_null_dat = 0;
                    }
                    //обнуляем массив для проверки выше если строки программа видит но они пустые.
                    // array_cell_len = null;
                    for (int i = 0; i < array_cell_len.length; i++) {
                        array_cell_len[i] = "";
                    }
                }
            }   //System.out.println( sum_sheet);

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
        System.out.println(array_cell.size() + " -number string in mass");
        return array_cell;
    }
    
    // --- Получение только данных столбца без UUid и структур. Только данные----
    public ArrayList<String> getDataColumn(String name_sheet, String column) {
        FileInputStream inputStream = null;
        String datCell = null;
        ArrayList<String> array_cell = new ArrayList<>();
        try {
            inputStream = new FileInputStream(new File(path_file));
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheet(name_sheet);
            getRowReadData(sheet); // вызов фукции определения начала данных в на Листе
            // начальные значения наверное просто для инициализации
            int first_len = 0; // получили переменую строки с именами столбцов;
            int tmpFirstLenght = 0;
            int col_UUID = 0;
            first_len = startReadData;
            col_UUID = 1;
            Iterator<Row> rowIter = sheet.iterator(); // итератор Строк
            int sum_sheet = 0;
            int len_row = 0;
            int max_len_row = 0;
            while (rowIter.hasNext()) {
                while (tmpFirstLenght < first_len) { // а вот есть ли там данные
                    rowIter.next();
                    ++tmpFirstLenght;
                }
                if (rowIter.hasNext()) { // проверка есть ли вообще данные после пропуска строк
                    Row row = rowIter.next();
                    // System.out.println(row.getFirstCellNum() + " " + row.getLastCellNum()); //в строку что бы посмотреть что за нах
                    int tmp = 0;
                            int numberCol = CellReference.convertColStringToIndex(column); // Переводим имя в индекс
                            //System.out.println(CellReference.convertNumToColString(i_tmp)); // выявляем Имена стобцы в которых ячейка
                            Cell cell = row.getCell(numberCol); // адрес индекс
                            //cell.getRichStringCellValue().toString();
                            if (cell != null) {  // обходим таким дебильным способом
                                CellType cellType = cell.getCellType();
                                switch (cellType) {
                                    case STRING: {
                                        if (cell.getStringCellValue().contains("'")) {
                                            datCell = cell.getStringCellValue().replaceAll("'", "");
                                        }else datCell = cell.getStringCellValue();
                                    }
                                    break;
                                    case BLANK:
                                        datCell = "";
                                        break;
                                    case NUMERIC:
                                        datCell = Double.toString(cell.getNumericCellValue()); // Double
                                        break;
                                    //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                                    case FORMULA:
                                        // System.out.println("Formula");
                                        switch (cell.getCachedFormulaResultType()) {
                                            case NUMERIC:
                                                datCell = (Double.toString(cell.getNumericCellValue()));
                                                break;
                                            case STRING:
                                                datCell = cell.getRichStringCellValue().toString().replaceAll("'", "");
                                                //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                                                break;
                                        }
                                        break;
                                    default:
                                        datCell = "|";
                                        break;
                                }
                            } else {
                                datCell = ""; // Вот тут чего такое то?
                            }
                    // ПРоверяемс считались какие то данные из ячеек строки (1 так как первый элемент занят ID)
                    //Желательно переписать
                    int not_null_dat = 0;
                    if(datCell != null) {
                      array_cell.add(datCell);
                    }
                    datCell = null;

                }
            }   //System.out.println( sum_sheet);

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
        System.out.println(array_cell.size() + " -number string in mass" + " List " + name_sheet + " column "+ column  );
        return array_cell;
    }

    public ArrayList<String> getDataNameTable(String name_sheet) throws FileNotFoundException, IOException {
        ArrayList<String> array_cell = new ArrayList<>();
        // Номера ячеек беру в ручную от куда брать названия для таблиц
        FileInputStream inputStream = new FileInputStream(new File(path_file));
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheet(name_sheet); // получаем по имени страницу из Exel
        //  --- запросы для формирования названия таблиц и строки начала данных---
        Iterator<Row> iterRow = sheet.iterator(); // Итератор строк
        List<String[]> massColumnName = new ArrayList(); // для данных  формирования имен столбцов
        int MaxLenNameMass = 0; // для длинны массива сформированных столбцов
        int maxLengtString = 0;   // просто максимальная длинна строки на странице
        int maxLnotNullString = 0;   // просто максимальная длинна строки не пустая в конце
        while (iterRow.hasNext()) {
            Row r = iterRow.next(); // строка
            System.out.print("endCell " + r.getLastCellNum() + " ");// + " DataCell ->" + r.getCell(r.getLastCellNum()));
            if (r.getLastCellNum() > maxLengtString) {
                maxLengtString = r.getLastCellNum(); // простовычисляем максимальную длинну строки
            }
        }// можно как то с делать в одном проходе но не до этого

        iterRow = sheet.iterator(); // Итератор строк(еще раз инициализация а иначяе не работает)
        boolean findColumnSig = false; // сигнал для остановки перебора
        int maxRowNum = 0; // Переменная для определения от куда данные начинаются

        while (iterRow.hasNext()) {
            Row r = iterRow.next(); // строка
            Iterator<Cell> icell = r.cellIterator();
            int tmpI = 0;
            String[] nameRow = {"Наименование сигнала", "Tag name", "Наименование"}; // нужно сделать с помощью файлов конфигов

            while (icell.hasNext()) {

                Cell c = icell.next();
                if (!findColumnSig) { // пока не нашли перебираем ячейки для ускорения
                    CellType cellType = c.getCellType();

                    switch (cellType) { // Вычисляем тип ячейки
                        case STRING: { // только если строка
                            String dataC = c.getStringCellValue(); // получим строку из ячейки
                            for (int i = 0; i < nameRow.length; ++i) { // прогоняем по списку  искомых  
                                if (dataC.equals(nameRow[i]))// ПРоверяем на совпадения обозначений столбоц если есть совпадения то со следующей строки данные
                                {
                                    String[] tmpMassN = new String[maxLengtString]; // размер массива = размер длины строки Exel
                                    findColumnSig = true; // выставили флаг в то что нашли
                                    //System.out.println("Addres f cell " + c.getAddress()); // адрес ячейки
                                    if (maxRowNum <= r.getRowNum()) {
                                        maxRowNum = r.getRowNum(); // адрес строки с найденным столбцом они мугут быть разные по этому максимальное берем
                                        startReadData = maxRowNum + 1; // +1 так как данные начинаются со следующей строки
                                        //System.out.println("Addres first data " + startReadData);
                                        Iterator<Cell> cellNameColumn = r.cellIterator();
                                        int j = 0;
                                        while (cellNameColumn.hasNext()) { // прогоняемся по строке для формирования массива
                                            Cell cName = cellNameColumn.next();
                                            CellType cellTypecName = cName.getCellType();
                                            j = cName.getColumnIndex(); // Индекс ячейки , так как не работает через ++, перескакивает
                                            switch (cellTypecName) { // Вычисляем тип ячейки
                                                case STRING: {
                                                    tmpMassN[j] = cName.getStringCellValue(); // получим строку из ячейки
                                                    //System.out.println("j= " + j + " Index Column " + cName.getColumnIndex() + " DataCell -  " + cName.getStringCellValue());
                                                }
                                                break;
                                                case BLANK:
                                                    tmpMassN[j] = null;
                                                    break;
                                                case NUMERIC:
                                                    tmpMassN[j] = Double.toString(cName.getNumericCellValue()); // Double
                                                    break;
                                                //case FORMULA : array_cell_len[tmp]=cell.getCellFormula(); // String
                                                case FORMULA:
                                                    switch (cName.getCachedFormulaResultType()) {
                                                        case NUMERIC:
                                                            tmpMassN[j] = Double.toString(cName.getNumericCellValue());
                                                            break;
                                                        case STRING:
                                                            tmpMassN[j] = cName.getRichStringCellValue().toString();
                                                            //System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                                                            break;
                                                    }
                                                    break;
                                                default:
                                                    array_cell.add("|");
                                            }

                                        }
                                        massColumnName.add(tmpMassN);
                                    }
                                    break; // прерываем массив поиска так как нашли исходное
                                }
                            }

                        }
                        break;

                    }
                    ++tmpI;
                }

            }
            findColumnSig = false;
        }

        // так мы перебираем и анализируем что мы насобирали и делаем один массив 
        // String[] tmpMassN = new String[MaxLenNameMass];
        String[] tmpMassN = new String[maxLengtString];// заменим на это пока по тупому длинну массива
        for (String[] mass : massColumnName) {
            for (int i = 0; i < mass.length; ++i) {
                if (tmpMassN[i] == null) { // как то надо на оборот к нижнему прикручивать верхнее
                    tmpMassN[i] = mass[i];
                } else {
                    if (mass[i] != null) { // если что добавляем тоже не пустое
                        tmpMassN[i] = tmpMassN[i] + "_" + mass[i];
                    }
                }
            }
        }
        // убераем пустое в сформированном массиве 
        for (int i = 0; i < tmpMassN.length; ++i) {
            System.out.println(tmpMassN[i]);
            if (tmpMassN[i] == null) {
                tmpMassN[i] = "Num_" + Integer.toString(i);
            }
            for (int j = i + 1; j < tmpMassN.length; ++j) { //пробегаем еще раз что бы проверить есть ли одинаковые значения 
                System.out.println(tmpMassN[i] + " == " + tmpMassN[j]);
                if (tmpMassN[i].equals(tmpMassN[j])) { // если точно такая же строка то меняем ее
                    tmpMassN[j] = tmpMassN[j] + "_" + Integer.toString(j);
                }
            }
            array_cell.add(tmpMassN[i]);
        }

        System.out.println("maxLengtString " + maxLengtString + " array_cell.size " + array_cell.size());
        return array_cell;
    }

    // --- Определяем от куда начинаются данные на Листе ---
    int getRowReadData(Sheet sheet) {
        String[] nameRow = {"Наименование сигнала", "Tag name", "Наименование"}; // нужно сделать с помощью файлов конфигов(определение данных) 
        Iterator<Row> iterRow = sheet.iterator(); // Итератор строк
        boolean findColumnSig = false; // сигнал для остановки перебора
        int maxRowNum = 0; // Переменная для определения от куда данные начинаются
        while (iterRow.hasNext()) {
            Row r = iterRow.next(); // строка
            Iterator<Cell> icell = r.cellIterator();
            int tmpI = 0;
            while (icell.hasNext()) {
                Cell c = icell.next();
                //System.out.println(c.getAddress());  // дебаг адрес ячейки
                if (!findColumnSig) { // пока не нашли перебираем ячейки для ускорения
                    CellType cellType = c.getCellType();
                    switch (cellType) { // Вычисляем тип ячейки
                        case STRING: { // только если строка
                            String dataC = c.getStringCellValue(); // получим строку из ячейки
                            for (int i = 0; i < nameRow.length; ++i) { // прогоняем по списку  искомых  
                                if (dataC.equals(nameRow[i])) { // ПРоверяем на совпадения обозначений столбоц если есть совпадения то со следующей строки данные
                                    findColumnSig = true; // выставили флаг в то что нашли
                                    //System.out.println("Addres f cell " + c.getAddress()); // адрес ячейки
                                    if (maxRowNum <= r.getRowNum()) {
                                        maxRowNum = r.getRowNum(); // адрес строки с найденным столбцом они мугут быть разные по этому максимальное берем
                                        startReadData = maxRowNum + 1; // +1 так как данные начинаются со следующей строки
                                    }
                                    break; // прерываем массив поиска так как нашли исходное
                                }
                            }
                        }
                        break;
                    }
                    ++tmpI;
                }
            }
            findColumnSig = false;
        }
        return maxRowNum;
    }
}
