/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReadWriteExcel;

import java.io.File;
import org.apache.poi.ss.usermodel.Row;
import globalData.globVar;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.w3c.dom.Node;

public final class UnloadExcel {
    
    
    // --- Запуск обработчика ---
    public boolean runUnloadExcel(String abonent_name){
        boolean error = true;
        HSSFWorkbook workbook = new HSSFWorkbook();
        ArrayList<String> tableList = globVar.DB.getListTable();
        for (int i = 0; i < tableList.size(); i++) {
            String name_list = tableList.get(i);
            if (name_list.indexOf(abonent_name + "_") == 0) {
                if(createExcelSheet(name_list, workbook) == false){
                    System.out.println("not find Table" + name_list);
                    error = false;
                }
            }
        }
        return error;
    }
    
    boolean createExcelSheet(String nameTable, HSSFWorkbook workbook) {
        
        int x = nameTable.indexOf("_");
        String book_name = nameTable.substring(0, x);
        String sheetName = nameTable.substring(x + 1);
        int y = sheetName.indexOf("_mb_");
        String subAb = "";
        String nodeName = sheetName.substring(y + 1);
        if (y > 0) {
            nodeName = sheetName.substring(y + 1);
        }
        Node tableNode = globVar.sax.returnFirstFinedNode(nodeName);
        if (tableNode == null) {
            return false;
        }
        Node excelNode = globVar.sax.returnFirstFinedNode(tableNode, "EXEL");
        ArrayList<Node> childExcel = globVar.sax.getHeirNode(excelNode);//с одной бутылки на другую прилетел
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
            colNames.add(colName);
            int numberCol = CellReference.convertColStringToIndex(colExelName);//получили номер колонки F .A. B и тд
            row.createCell(numberCol).setCellValue(colName);//я бы скореевсеговысыпалсяно этонеточно
            row.getCell(numberCol).setCellStyle(cellStyle);//заполняем ячейки наименования цветом
        }
        rowNum++;
        ArrayList<String[]> data = globVar.DB.getData(nameTable, colNames);
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
