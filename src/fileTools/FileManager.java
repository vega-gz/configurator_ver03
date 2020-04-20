/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileTools;

import globalData.globVar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.w3c.dom.Node;

//import main.globVar;
public class FileManager {

    public static ArrayList<String> listAllPath = new ArrayList(); // отдельно вытащил из за рекурсии в pathAllFile

    // --- копирование файла используя поток ---
    public void copyFile(String source, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source); // Можно и файл подставлять
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    // -- метод бекапа на основе времени ---
    public void backupFile(String source) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String currentDat = dateFormat.format(cal.getTime());
        copyFile(source, source + "_" + currentDat);
    }

    public void backupFile(String source, String destDir) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        String currentDat = dateFormat.format(cal.getTime());
        copyFile(source, source + "_" + currentDat);
    }

    // -- Поиск файла в конкретной папке ( можно по маске) Возращаем лист с путями ---
    public ArrayList<String> findFile(String path, String s) {
        ArrayList<String> listPathFile = new ArrayList();
        /*
         path - путь до директории
         s - название файла можно по маске "*.tx*"
         */
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(path), s)) {
            for (Path entry : dir) {
                System.out.println(entry);
                listPathFile.add(entry.toString());
            }
        } catch (IOException ex) {
            System.out.println("Prorgramm error java, please send this error to mail ");
        }
        return listPathFile;
    }

    // -- Рекурсивное удаление директории со всеми файлами ---
    public void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
                System.out.println("Удалено : " + f);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    // --- запоминает полные пути файлов  но как рекурсия дергаем его фукциями ниже---
    public void pathAllFile(String dir) {
        File f = new File(dir);
        if (f.isDirectory()) {
            String[] children = f.list();
            for (int i = 0; i < children.length; i++) {
                pathAllFile(dir + "\\" + children[i]); // вызываем рекурсией
            }
            //System.out.println("Директория " +  f.getAbsolutePath() ); // сообщение о директории прячем
        } else {
            //System.out.println("файл " + f.getAbsolutePath() + " symbols " + dir.length()); // просто вывод длину файла для статистики
            listAllPath.add(f.getAbsolutePath());
        }
    }

    // --- запоминает полные пути до папок  но как рекурсия дергаем его фукциями ниже---
    static void pathAllDir(String dir) {
        File f = new File(dir);
        if (f.isDirectory()) {
            String[] children = f.list(); // так смотрим что в директории 
            for (int i = 0; i < children.length; i++) { // пробегаемся по ней
                pathAllDir(dir + "\\" + children[i]); // вызываем рекурсией
            }
            listAllPath.add(f.getAbsolutePath());
        } else {
            //System.out.println("файл " + f.getAbsolutePath() + " symbols " + dir.length()); // просто вывод длину файла для статистики
        }
    }

    // --- возвращает все пути файлов ---
    public ArrayList<String> getPathListFile(String dir) {
        listAllPath.clear();
        pathAllFile(dir);
        return listAllPath;
    }

    // --- возвращает все пути папок ---
    public ArrayList<String> getPathListDir(String dir) {
        listAllPath.clear();
        pathAllDir(dir);
        return listAllPath;
    }

    // --- Логирование ошибок и другая информация ---
    public static void loggerConstructor(String s) {
        String nameF = globVar.logFile;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss_yyyy.MM.dd");
        String currentTime = formatForDateNow.format(new Date());
        File logF = new File(nameF);
        if (!logF.exists()) { // нет файла то создаем
            try {
                logF.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error create log file " + nameF);
            }
        }
        s = currentTime + "\t" + s + "\n";
        try {
            Files.write(Paths.get(nameF), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error write log file " + nameF);
        }
    }

    //public File dir;
    public File wrFile;
    public File rdFile;
    public FileOutputStream fos;
    public FileInputStream fis;
    public Writer wrStream;
    public Reader rdStream;
    //String buf;

    public int MoveFile(String fileName, String srcDir, String dstDir) {
        File originalFile = new File(srcDir, fileName);
        File newFile = new File(dstDir, fileName);

        if (originalFile.exists() && !newFile.exists()) {
            if (originalFile.renameTo(newFile)) {
            } else {
                return 1;
            }
        }
        return 0;
    }

    public int createFile2write(String dirName, String fileName) throws IOException {

        File dir = new File(dirName);
        // если объект представляет каталог
        if (dir.isFile()) {
            return 1; //неправильный путь
        }
        if (dir.isDirectory()) {
            wrFile = new File(dir, fileName);
            if (wrFile.isFile()) {
                wrFile.delete();
            }
            boolean created = wrFile.createNewFile();
            if (!created) {
                return 2; // не удалось создать файл
            }
        }
        fos = new FileOutputStream(wrFile, false);
        wrStream = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        return 0;
    }

    public int openFile4read(String dirName, String fileName, String charSer) throws IOException {
        File dir = new File(dirName);
        // если объект представляет каталог
        if (dir.isFile()) {
            return 1; //неправильный путь
        }
        if (dir.isDirectory()) {
            rdFile = new File(dir, fileName);
            if (!rdFile.isFile()) {
                return 3; // не удалось найти файл
            }
        } else {
            return 1; //неправильный путь 
        }
        fis = new FileInputStream(rdFile);
        rdStream = new InputStreamReader(fis, charSer); //cs);
        globVar.EOF = false;
        return 0;
    }

    public void wr(String s) throws IOException {
        wrStream.write(s);
    }

    public String rd() throws IOException {
        String s = "";
        int ch = 0;
        //while (((char)ch) != '\n' && ch >= 0 ) {
        while (true) {
            try {
                ch = rdStream.read();
                if (!(s.isEmpty() && (ch == 10))) {//ch == 13 || 
                    if (((char) ch) == '\n' || ch < 0 || ch == 13 || ch == 10) {
                        break;
                    }
                    s += (char) ch;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                globVar.EOF = true;
                break;
            }

        }
        if (ch == -1) {
            globVar.EOF = true;
        }
        return s;
    }

    public int CopyFile(String file1Name, String file2Name, String srcDir, String dstDir, String charSet) throws IOException {
        if (globVar.fm.openFile4read(srcDir, file1Name, charSet) != 0) {
            return 1;
        }
        if (globVar.fm.createFile2write(dstDir, file2Name) != 0) {
            return 1;
        }
        while (!globVar.EOF) {
            String buf = globVar.fm.rd();
            globVar.fm.wr(buf);
            //System.out.println("[" + buf +"]" + globVar.EOF);
        }
        globVar.fm.rdStream.close();
        globVar.fm.wrStream.close();
        return 0;
    }
//тот самый метод,еще подправлю

//    public static String FindFile(String dir, String nameType) {
//        boolean result;
//        String nameWords = "Name=";
//        String name, name1 = null;
//        int i = 0;
//        try {
//         //   создаем объект FileReader для объекта File
//            FileReader fr = new FileReader(dir);
//          //  создаем BufferedReader с существующего FileReader для построчного считывания
//            BufferedReader reader = new BufferedReader(fr);
//             считаем сначала первую строку
//            String line = reader.readLine();
//            while (line != null) {
//
//                line = reader.readLine();
//                if (line.contains(nameWords) == true) {
//                    int count = 0;
//                    for (int j = 0; j < nameWords.toCharArray().length; j++) {
//                        count++;
//                    }
//                    name = "." + line.substring(line.indexOf(nameWords) + count + 1);
//                    name1 = name.substring(name.indexOf('.') + 1, name.indexOf('"'));
//                    System.out.println(name1);
//
//                } else {
//                    System.out.println("В строке " + i + " не нашлось данного слова");
//                }
//                i++;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return name1;//должен возвращать имя файла со схожим типом
//
//    }
//
//    public String Find(String dir, String nameType) throws FileNotFoundException, IOException {
//        String nameWords = "Name=";
//        String name, name1 = null;
//        String trueName = null;
//        int i = 0;
//
//        получаем все файлы в папке
//        final File folder = new File(dir);
//
//        final List<File> fileList = Arrays.asList(folder.listFiles());//массив который хранит количество файлов в папке
//        for (int k = 0; k <= fileList.size(); k++) {
//            FileReader fr = new FileReader(fileList.get(k));
//            BufferedReader reader = new BufferedReader(fr);
//            String line = reader.readLine();
//            while (line != null) {
//
//                line = reader.readLine();
//                if (line.contains(nameWords) == true) {
//                    int count = 0;
//                    for (int j = 0; j < nameWords.toCharArray().length; j++) {
//                        count++;
//                    }
//                    name = "." + line.substring(line.indexOf(nameWords) + count + 1);
//                    name1 = name.substring(name.indexOf('.') + 1, name.indexOf('"'));
//                      System.out.println(name1);
//                    if (nameType.equals(name1)) {
//                        trueName = fileList.get(k).getName();
//
//                    }
//
//                }
//
//            }
//            break;
//        }
//        System.out.println(trueName);
//
//        return trueName;
//    }

    public static String FindFile(String dir, String nameType) {
        String nameWords = "Name=";
        String ext = ".TYPE";
        String firstName, secondName ,fileName= null;
        final File folder = new File(dir);
        String[] fileList = folder.list();
        int count = 0;
        for (int j = 0; j < nameWords.toCharArray().length; j++) {
            count++;
        }
        //создание fileList - списка всех файлов с расширением .type в каталоге dir
        for (String file : fileList) {
            try {
                String s = file.toString();
                //создаем объект FileReader для объекта File
                FileReader fr = new FileReader(dir + file);
                //создаем BufferedReader с существующего FileReader для построчного считывания
                BufferedReader reader = new BufferedReader(fr);
                // считаем сначала первую строку
                String line = reader.readLine();
                while (line != null) {
                    int start = line.indexOf(nameWords);
                    if (start >= 0) {
                        firstName = "." + line.substring(line.indexOf(nameWords) + count + 1);
                        secondName = firstName.substring(firstName.indexOf('.') + 1, firstName.indexOf('"'));
                        // start = line.indexOf("\"", start);
                        //  int end = line.indexOf("\"", start + 1);
                        if (nameType.equals(secondName)) {
                            System.out.println(file);    
                            return file;
                        }else{
                            break;
                        }
                    }
                    line = reader.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

       

    }

    public static class MyFileNameFilter implements FilenameFilter {

        private String ext;

        public MyFileNameFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }

//    public static void main(String[] args) throws IOException {//для тестирования
//        FileManager fm = new FileManager();
//        // fm.findWords("C:\\Users\\Григорий\\Desktop\\новый конфиг и excel\\ConfigSignals.xml");
//        //  FindFile("C:\\Users\\Григорий\\Desktop\\сиг\\T_GPA_DI_ToProcessing.type", "T_GPA_DI_ToProcessing");
//     //   fm.FindFile("C:\\Users\\Григорий\\Desktop\\сиг", "T_GPA_AI_FromProcessing");
//    }

}
