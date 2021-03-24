/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import DataBaseTools.DataBase;
import Main.ProgressBar;
import Tools.FileManager;
import java.io.File;
import org.apache.poi.ss.usermodel.Row;
import globalData.globVar;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.w3c.dom.Node;

/**
 *
 * @author cherepanov
 */
public final class UnloadExcel {

    /**
     * Создает объект книги с названием равным имени абонента,выбранного
     * пользователем
     *
     * @param abonent_name имя абонента,для которого пользователь хочет
     * выгрузить данные
     * @param ProgressBar1
     * @return возвращает true если книга создалась,false если нет
     */
    public boolean runUnloadExcel(String abonent_name,ProgressBar ProgressBar1) {
        boolean error = true;
        HSSFWorkbook workbook = new HSSFWorkbook();
        ArrayList<String> tableList = globVar.DB.getListTable();
        for (int i = 0; i < tableList.size(); i++) {
            String name_list = tableList.get(i);
            if (name_list.indexOf(abonent_name + "_") == 0) {
                System.out.println(name_list);
                if (createExcelSheet(name_list, workbook) == false) {
                    System.out.println("not find Table" +" "+ name_list);
                    error = false;
                }
            }
            ProgressBar1.setVal((int) ( (i+1) * 100.0 / tableList.size()));
        }
        return error;
    }

    /**
     * Метод создает книгу с данными ,выгружаемыми из БД
     *
     * @param nameTable имя листа в БД ,которое делится на имя книги(book_name)
     * и имя листа(sheetName)
     * @param workbook книга ,которую мы создали в
     * методу{@link UnloadExcel#runUnloadExcel(java.lang.String)}
     * @return true если файл создался,false если нет
     */
    boolean createExcelSheet(String nameTable, HSSFWorkbook workbook) {
        DataBase db=new DataBase();
        int x = nameTable.indexOf("_");
        String book_name = nameTable.substring(0, x);
        String sheetName = nameTable.substring(x + 1);
        int y = sheetName.indexOf("_mb_");
        String subAb = "";
        String fullNameTable;
        String nodeName = sheetName.substring(y + 1);
        if (y > 0) {
            nodeName = sheetName.substring(y + 1);
        }
        Node tableNode = globVar.sax.returnFirstFinedNode(nodeName);
        if (tableNode == null) {
            return false;
        }
        Node excelNode = globVar.sax.returnFirstFinedNode(tableNode, "EXEL");//сама нода в которой находятся наши имена коолнок
        ArrayList<Node> childExcel = globVar.sax.getHeirNode(excelNode);//дети ноды excel
        // создание листа с названием 
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // счетчик для строк
        int rowNum = 0;
        // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
        Row row = sheet.createRow(rowNum);//
        ArrayList<String> colNames = new ArrayList<>();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//это стиль заполнения а не место
        //получаем данные из конфиг
        for (Node colExcel : childExcel) {
            String colExelName = colExcel.getNodeName();//получили имя ноды 
            String colName = globVar.sax.getDataAttr(colExcel, "nameColumnPos");//получили значение атрибута
            
            colNames.add(colName);//сделал для того чтобы для xml сохранилось НАИМЕНОВАНИЕ а  шапку для mb создать из комментария к таблице
            if(sheetName.contains("mb")&&colName.equals("Наименование")){
                fullNameTable=db.getCommentTable(nameTable);
                if(!fullNameTable.equals("null")||fullNameTable==null){
                int z=fullNameTable.indexOf("Modbus:");
               colName=fullNameTable.substring(z+"Modbus:".length());
                }
               // colName=db.getCommentTable(nameTable);
            }
            
            int numberCol = CellReference.convertColStringToIndex(colExelName);//получили номер колонки F .A. B и тд
            row.createCell(numberCol).setCellValue(colName);//создаем ячейку и заполняем ее значением colName
            row.getCell(numberCol).setCellStyle(cellStyle);//заполняем ячейки наименования цветом
        }
        //конечный итог,создали строку(шапку)
        rowNum++;
        ArrayList<String[]> data = globVar.DB.getData(nameTable, colNames);//вот эту строку нужно окружить try catch
        if(data.isEmpty()){
            FileManager.loggerConstructor("В базе отсутствует колонка,из которой мы пытаемся получить данные");
        }
        for (String[] sData : data) {
            int j = 0;
            row = sheet.createRow(rowNum);
            for (Node colExcel : childExcel) {
                String colExelName = colExcel.getNodeName();//получили имя адреса ячейки

                int numberCol = CellReference.convertColStringToIndex(colExelName);//получили номер колонки F .A. B и тд
                row.createCell(numberCol).setCellValue(sData[j]);
                j++;
            }
            ++rowNum;
        }
        // записываем созданный в памяти Excel документ в файл
        try {
            BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(new File(globVar.desDir + File.separator + book_name + ".xls")));
            workbook.write(buf);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
