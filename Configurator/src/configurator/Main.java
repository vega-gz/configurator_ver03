/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author cherepanov
 */
public class Main {

    static DataBase workbase = new DataBase();
    static RWExcel rwexcel = new RWExcel();

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_JPanel().setVisible(true);
            }
        });
    }

    static void fillDB(String patch_file) throws IOException {
        rwexcel.setPatchF(patch_file); // изменяем путь файла
        //  workbase.connectionToBase();
        // workbase.connectionToBase(url, pass, user);//по сути я уже подключен к базе данных
        Iterator<String> it_list_sheet = rwexcel.get_list_sheet().iterator(); //забираем список листов в файле и строим итератор из них
        while (it_list_sheet.hasNext()) {
            String name_table = it_list_sheet.next(); // название таблицы как имя листа из файла
            int maxlencol;
            if (name_table.equals("DO1")) {
                maxlencol = rwexcel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
            } else {
                maxlencol = rwexcel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
            }
            ArrayList<String> name_collums = new ArrayList<>(rwexcel.getDataNameTable(name_table)); // получаем массив столбцов и формируем от куда начинать считывать данные
            // так переопределим длину от куда тащим названия
            maxlencol = name_collums.size();
            workbase.createTable(name_table, maxlencol, name_collums); // передаем название таблицы и максимальное  - создание таблиц
            // берем данные с файла
            ArrayList<String[]> sheet_fromsheet_from;
            sheet_fromsheet_from = new ArrayList<>(rwexcel.getDataCell(name_table, maxlencol)); // maxlencol не верное вычисление похоже на 3 меньше в DO1 чем должно быть
            Iterator<String[]> iter_sheet = sheet_fromsheet_from.iterator();
            while (iter_sheet.hasNext()) {
                String[] dataFromFile = iter_sheet.next();

                workbase.insertRows(name_table, dataFromFile, name_collums);//вносим данные в базу

            }

        }
    }
}
