package Main;

import DataBaseConnect.DataBase;
import XMLTools.XMLSAX;
import globalData.globVar;
import javax.swing.JOptionPane;


public class Main {
    //static DataBase workbase =  DataBase.getInstance();

    public static void main(String[] args) {
        XMLSAX.getConnectBaseConfig("Config.xml");
        globVar.DB = DataBase.getInstance();
        if(globVar.DB == null){
            JOptionPane.showMessageDialog(null, "Не удалось устновить контакт с СУБД");
            return;
        }
        globVar.myDir = System.getProperty("user.dir");
        globVar.sax = new XMLSAX(); // Класс работы с XML  static что бы не парится
        globVar.cfgRoot = globVar.sax.readDocument("ConfigSignals.xml");
        if(globVar.cfgRoot == null){
            JOptionPane.showMessageDialog(null, "Не удалось прочитать "+globVar.myDir+"\\ConfigSignals.xml");
            return;
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_JPanel().setVisible(true);
            }
        });
    }

//    Main() {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //размеры экрана
//        int sizeWidth = 800;
//        int sizeHeight = 600;
//        int locationX = (screenSize.width - sizeWidth) / 2;
//        int locationY = (screenSize.height - sizeHeight) / 2;
//        Main_JPanel Windo_main = new Main_JPanel();
//        JFrame frame = new JFrame();
//        
//        frame.setBounds(locationX, locationY, sizeWidth, sizeHeight); // Размеры и позиция
//        frame.setContentPane(Windo_main); // Передаем нашу форму
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE); // Закрытие приложения но можно более хитро с диалоговыми
//    }

//    static void fillBaseAlldata(String patch_file)  {//загрузка непосредственно в базу
//            try {
//        rwexcel.setPatchF(patch_file); // изменяем путь файла
//        //workbase.connectionToBase();
//        Iterator<String> it_list_sheet = rwexcel.get_list_sheet().iterator(); //забираем список листов в файле и строим итератор из них
//        while (it_list_sheet.hasNext()) {
//            String name_table = it_list_sheet.next(); // название таблицы как имя листа из файла
//            int maxlencol;
//            if (name_table.equals("DO1")) {
//                maxlencol = rwexcel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
//            } else {
//                maxlencol = rwexcel.getMaxcColumn(name_table); //количество столбцов методом вычисления в фукции
//            }
//            ArrayList<String> name_collums = new ArrayList<>(rwexcel.getDataNameTable(name_table)); // получаем массив столбцов и формируем от куда начинать считывать данные
//            // так переопределим длину от куда тащим названия
//            //maxlencol = name_collums.size();
//            if(workbase!=null)workbase.createTable(name_table, name_collums); // передаем название таблицы и максимальное  - создание таблиц
//            else return;
//// берем данные с файла
//            ArrayList<String[]> sheet_fromsheet_from;
//            sheet_fromsheet_from = new ArrayList<>(rwexcel.getDataCell(name_table, maxlencol)); // maxlencol не верное вычисление похоже на 3 меньше в DO1 чем должно быть
//            Iterator<String[]> iter_sheet = sheet_fromsheet_from.iterator();
//            while (iter_sheet.hasNext()) {
//
//                String[] dataFromFile = iter_sheet.next();
//                workbase.insertRows(name_table, dataFromFile, name_collums); //Вносим данные в базу
//            }
//
//        }
//        } catch (IOException ex) {
//                System.out.println("Error read file or connect to base " +ex);
//        }
//    }
}
