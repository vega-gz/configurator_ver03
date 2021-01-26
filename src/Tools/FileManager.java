/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Tools.StrTools;
import static Tools.StrTools.getAttributValue;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.w3c.dom.Node;

//import main.globVar;
public class FileManager {

    public static ArrayList<String> listAllPath = new ArrayList(); // отдельно вытащил из за рекурсии в pathAllFile

    public File wrFile = null;
    public File rdFile = null;
    public FileOutputStream fos = null;
    public FileInputStream fis = null;
    public Writer wrStream = null;
    public Reader rdStream = null;
    public boolean EOF;

    // --- копирование файла используя поток ---
    public static int copyFileWoReplace(String source, String dest, boolean notCopyFile) throws IOException {
        File copy = new File(dest);
        if (copy.isFile()) {//Проверяем, есть ли такой файл в целевом каталоге
            if (notCopyFile) {
                return 1; //Если есть и есть признак "не размножать копии" заканчиваем работу
            }
            for (int i = 1; copy.isFile(); i++) { //Если признака нет - создаём копию с номером версии
                dest = dest + "(" + i + ")";
                copy = new File(dest);
            }
        }
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
            if (is == null) {
                return 2;
            }
            if (os == null) {
                return 3;
            }
            is.close();
            os.close();
        }
        return 0;
    }

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
                pathAllFile(dir + File.separator + children[i]); // вызываем рекурсией
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
                pathAllDir(dir + File.separator + children[i]); // вызываем рекурсией
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
    public static void loggerConstructor(String s) {// throws FileNotFoundException {
        String nameF = globVar.logFile;
        File logF = new File(nameF);
        if (!logF.exists()) { // нет файла то создаем
            try {
                logF.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error create log file " + nameF);
            }
        }
        FileOutputStream logOS = null;
        try {
            logOS = new FileOutputStream(logF, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        OutputStreamWriter logStream = new OutputStreamWriter(logOS, StandardCharsets.UTF_8);

        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss_yyyy.MM.dd");
        String currentTime = formatForDateNow.format(new Date());
        s = currentTime + "\t" + s + "\n";
        try {
            logStream.write(s);
            logStream.close();
            logOS.close();
            //Files.write(Paths.get(nameF), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error write log file " + nameF);
        }

    }

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

    public void closeWrStream() {// throws IOException {
        if (wrStream != null) {
            try {
                wrStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void closeRdStream() throws IOException {
        if (rdStream != null) {
            rdStream.close();
        }
        if (fis != null) {
            fis.close();
        }
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

    public int createFile2write(String fileName) throws IOException {
        wrFile = new File(fileName);
        if (wrFile.isFile()) {
            wrFile.delete();
        }
        if (!wrFile.createNewFile()) {
            return 2; // не удалось создать файл
        }
        fos = new FileOutputStream(wrFile, false);
        wrStream = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        return 0;
    }

    public int openFile4read(String dirName, String fileName) throws IOException {
        return openFile4read(dirName, fileName, "UTF-8");
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
        EOF = false;
        return 0;
    }

    public int openFile4read(String fileName) throws IOException {
        rdFile = new File(fileName);
        if (!rdFile.isFile()) {
            return 3; // не удалось найти файл
        }
        fis = new FileInputStream(rdFile);
        rdStream = new InputStreamReader(fis, "UTF-8"); //cs);
        EOF = false;
        return 0;
    }

    public String findStringInFile2(String fileName, String f) throws IOException {
        int ret = openFile4read(fileName);
        if (ret != 0) {
            return null;
        }
        String s = rd();
        int n = 0;
        while (!EOF && !s.contains(f) && !s.contains("<Resource>")) {
            s = rd();
            System.out.println(n);//------------------------------------------ check print ----------------------------
            n++;
        }
        closeRdStream();
        if (s.contains(f)) {
            return s;
        } else {
            return null;
        }
    }

    public static String findStringInFile(String fileName, String f) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s;
        while ((s = in.readLine()) != null) {
            if (s.contains(f)) {
                break;
            }
        }
        in.close();
        if (s != null && s.contains(f)) {
            return s;
        }
        return null;
    }

    public void wr(String s) {
        try {
            // throws IOException {
            wrStream.write(s);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String rd() throws IOException {
        String s = "";
        int ch = 0;
        //while (((char)ch) != '\n' && ch >= 0 ) {
        while (true) {
            try {
                ch = rdStream.read();
                if (!(s.isEmpty() && (ch == 10))) {//ch == 13 || 
                    if (((char) ch) == '\n' || ch < 0 || ch == 13 || ch == 10 || ch == -1) {
                        break;
                    }
                    s += (char) ch;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                EOF = true;
                break;
            }

        }
        if (ch == -1) {
            EOF = true;
        }
        return s;
    }

    public int CopyFile(String file1Name, String file2Name, String srcDir, String dstDir, String charSet) throws IOException {
        if (openFile4read(srcDir, file1Name, charSet) != 0) {
            return 1;
        }
        if (createFile2write(dstDir, file2Name) != 0) {
            return 1;
        }
        while (!EOF) {
            String buf = globVar.fm.rd();
            wr(buf);
            //System.out.println("[" + buf +"]" + globVar.EOF);
        }
        closeWrStream();
        closeRdStream();
        return 0;
    }

    public static String FindFile(String dir, String nameType) throws IOException {
        return FindFile(dir, nameType, "Name=");
    }

    public static String FindFile(String dir, String nameType, String nameWords) {// throws IOException {
        if (dir == null || nameType == null) {
            return null;
        }
        File f = new File(dir + File.separator + nameType + ".type");
        if (f.isFile()) {
            return nameType + ".type";
        }
        //-----------------------------------------------------------------------------
        final File folder = new File(dir);
        String[] fileList = folder.list();
        //if (fileList != null) {
        for (String fn : fileList) {
            if (fn.length() > 5 && ".TYPE".equalsIgnoreCase(fn.substring(fn.length() - 5))) {
                String s = null;
                try {
                    s = findStringInFile(dir + File.separator + fn, nameWords);
                } catch (IOException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                String val = StrTools.getAttributValue(s, nameWords);
                if (val != null && val.equals(nameType)) {
                    return fn;
                }
            }
        }
        //} else JOptionPane.showMessageDialog(null, "Путь к генерации не найден" + dir);
        return null;
    }

    public static String getUUIDFromFile(String dir, String nameType) throws IOException {
        String nameWords = "Name=";
        String uuidWords = "UUID=";
        String ext = ".TYPE";
        //String firstName, secondName, fileName = null;
        FileManager fm = new FileManager();
        final File folder = new File(dir);
        String[] fileList = folder.list();
        if (fileList != null) {
            for (String fn : fileList) {
                if (fn.toUpperCase().contains(ext)) {
                    fm.openFile4read(dir, fn);
                    String s = fm.rd();
                    while (!s.contains(nameWords) && !fm.EOF) {
                        s = fm.rd();//
                    }
                    if (!fm.EOF) {
                        int start = s.indexOf(nameWords) + nameWords.length() + 1;
                        int end = s.indexOf("\"", start);
                        if (end > start) {
                            String foundType = s.substring(start, end);
                            if (foundType.equals(nameType)) {
                                start = s.indexOf(uuidWords) + uuidWords.length() + 1;
                                end = s.indexOf("\"", start);
                                if (end > start) {
                                    fm.rdStream.close();
                                    return s.substring(start, end);
                                }
                            }
                        }
                    }
                    fm.rdStream.close();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Путь " + dir + " не найден");
        }
        return null;
    }

    public static void createBackupDir() {
        String currentDat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime());
        globVar.backupDir = globVar.desDir + File.separator + "backUp" + currentDat;   //установили путь для бэкапа
        new File(globVar.backupDir).mkdir();                                       //создали папку для бэкапа
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

    public static int renameUUIDinFile(File fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName + "tmp"));
            String s;
            String uuid;
            while ((s = in.readLine()) != null) {
                int start = s.indexOf(" UUID=\"");
                if (start >= 0) {
                    start += 7;
                    int end = s.indexOf("\"", start);
                    uuid = s.substring(start, end).toUpperCase();
                    s = s.substring(0, start) + uuid + s.substring(end);
                }
                start = s.indexOf("TypeUUID=\"");
                if (start >= 0) {
                    start += 10;
                    int end = s.indexOf("\"", start);
                    uuid = s.substring(start, end).toUpperCase();
                    s = s.substring(0, start) + uuid + s.substring(end);
                }
                start = s.indexOf("Type=\"");
                if (start >= 0) {
                    start += 6;
                    int end = s.indexOf("\"", start);
                    if (end - start == 32) {
                        uuid = s.substring(start, end).toUpperCase();
                        s = s.substring(0, start) + uuid + s.substring(end);
                    }
                }
                out.write(s + "\n");
            }
            in.close();
            out.close();
            //File file = new File(fileName);                             //создаём ссылку на исходный файл
            fileName.delete();                                             //удаляем его
            new File(fileName + "tmp").renameTo(fileName);                          //создаём ссылку на сгенерированный файл и делаем его исходным
        } catch (IOException e) {
        }
        return 0;
    }

    public static int renameUUIDinDirectory(String parentDirectory) throws FileNotFoundException, IOException {
        File[] filesInDirectory = new File(parentDirectory).listFiles();//получаем список элементов по указанному адресу
        for (File f : filesInDirectory) {//пробегаем по списку элементов по указанному адресу
            renameUUIDinFile(f);
        }
        return 0;
    }
//--ПереименованиеTYPE

    public static int renameTypeFile(String dir) throws FileNotFoundException, IOException {
        File[] filesInDirectory = new File(dir).listFiles();//получаем список элементов по указанному адресу
        String expType = "type";
        String expFolder = "folder";
        int cnt = 0;//счетчик выполненных замен

        for (File findType : filesInDirectory) {//пробегаем по списку элементов по указанному адресу
            String nameTypeFile = findType.getName();
            String findexpType = nameTypeFile.substring(nameTypeFile.lastIndexOf(".") + 1);//ищу расширение type
            //System.out.println(findexpType);//находим расширение файла
            if (findType.isDirectory()) {//если это дирректория - ничего не делать
            } else if (findexpType.equals(expType)) {//если расширение type ,то читаем файл
                String findName = findStringInFile(dir + File.separator + nameTypeFile, " Name=");//ищем строку
                String typeAttr = StrTools.getAttributValue(findName, "Name=\"");//находим этот аттрибут
                String fileName = findType.getName().substring(0, nameTypeFile.indexOf('.'));//имя файла без расширения(ибо прога читает с расширением)
                if (fileName != null && typeAttr != null && !fileName.equals(typeAttr)) {//сравниваем имя файла и имя типа(если не равны ищем фолдер)
                    //Если имена типа и файла не совпали - надо менять имя файла
                    cnt++;
                    System.out.println(typeAttr);//оставляем вместо прогрессбара
                    boolean notRename = true;
                    for (File findFolder : filesInDirectory) {//пробегаемся по той же дирректории в поисках folder
                        String nameFolderFile = findFolder.getName();
                        String findexpFolder = nameFolderFile.substring(nameFolderFile.lastIndexOf(".") + 1);//ищу расширение folder
                        if (findexpFolder.equals(expFolder)) {//если нашли folder то начинаем его читать(нашли файл с расширение фолдер)
                            String pathFolderFile = dir + File.separator + nameFolderFile;
                            String myType = findStringInFile(pathFolderFile, fileName);//вроде как ищем строку содержащую имя файла(TYPE)
                            if (myType != null) {
                                notRename = false;
                                System.out.println(myType);
                                String fullNameAttr = StrTools.getAttributValue(myType, "Name=\"");//нашли значение аттрибута фолдера
                                //System.out.println("В файле " + findFolder.getName() + " нашлось соответствие.");
                                XMLSAX xmlsax = new XMLSAX();
                                Node nodeRead = xmlsax.readDocument(pathFolderFile);//прочитали ноду
                                String itemArray[] = {"Item", "Name", fullNameAttr};//массив чтобы найти ноду по имени и атрибутам
                                Node itemFolderNode = xmlsax.findNodeAtribute(nodeRead, itemArray);//нашли конкретный item в котором соответствия
                                xmlsax.editDataAttr(itemFolderNode, "Name", typeAttr + "." + expType);
                                File newName = new File(dir + File.separator + typeAttr + "." + expType);
                                if (!newName.isFile()) {
                                    boolean renameTo = findType.renameTo(newName);
                                    if (renameTo) //переименовали файл type на значение аттрибута
                                    {
                                        xmlsax.writeDocument();
                                    }
                                }

                                break;
                            }
                        }
                    }
                    if (notRename) {
                        File newName = new File(dir + File.separator + typeAttr + "." + expType);
                        if (!newName.isFile()) {
                            findType.renameTo(newName);
                        }
                    }
                }
            }
        }
        System.out.println("Выполнено замен " + cnt);
        return cnt;
    }
//--Переименование INT

    public static int renameIntFile(String dir) throws FileNotFoundException, IOException {
        File[] filesInDirectory = new File(dir).listFiles();//получаем список элементов по указанному адресу
        String expInt = "int";
        String expFolder = "folder";
        String attrIntValue;
        String fileName;//имя файла без расширения
        int cnt = 0;//счетчик выполненных замен
        String confName = null;

        FileManager fm = new FileManager();
        XMLSAX xmlsax = new XMLSAX();

        for (File findInt : filesInDirectory) {//запускаю цикл по файлам в поисках int
            String nameIntFile = findInt.getName();//имя файла с расширением
            String findexpInt = nameIntFile.substring(nameIntFile.lastIndexOf(".") + 1);//получили расширение

            if (findexpInt.equals(expInt)) {//если нашли совпадения по расширению
                String findTypeName = findStringInFile(dir + File.separator + nameIntFile, "Name=");//находит первое вхождение кода нашел соответствие
                attrIntValue = getAttributValue(findTypeName, "Name=" + '"');//находим этот аттрибут
                fileName = nameIntFile.substring(0, nameIntFile.indexOf('.'));

                if (!fileName.equals(attrIntValue)) {//сравниваем ,если не равны ищем в folder соответствия

                    for (File findFolder : filesInDirectory) {//перебегаем папку второй раз в поисках фолдер и совпадений по имени

                        String nameFolderFile = findFolder.getName();//имя с расширением
                        System.out.println("этот файл называется " + nameFolderFile + " " + cnt);
                        if (!findFolder.isDirectory()) {
                            confName = nameFolderFile.substring(0, nameFolderFile.indexOf('.'));//получили имя без расширения
                        }
                        // String confName = nameFolderFile.substring(0, nameFolderFile.indexOf('.'));//получили имя без расширения
                        String findexpFolder = nameFolderFile.substring(nameFolderFile.lastIndexOf(".") + 1);//ищу расширение folder
//
                        cnt++;

                        //----МЫ НАШЛИ КОНКРЕТНЫЙ ИНТ.ЕСЛИ ИМЯ И ТИП НЕ СОВПАДАЮТ ТО ПРОБЕГАЕМСЯ СНОВА И ИЩЕМ ФАЙЛЫ С ТЕМ ЖЕ НАЗВАНИЕМ И МЕНЯЕМ ЕГО НА ЗНАЧЕНИЕ АТТРИБУТА---\\     
                        if (confName.equals(fileName)) {//если нашли совпадения по имени(int не ходят по одному,толпой,собаки)
                            nameFolderFile = nameFolderFile.substring(nameFolderFile.indexOf('.') + 1);
                            findFolder.renameTo(new java.io.File(dir + File.separator + attrIntValue + "." + nameFolderFile));//меняем имена у всех файлов со схожими
                        }

                        //----ЕСЛИ НАШЛИ СОВПАДЕНИЯ ПО ФОЛДЕР.ИЩЕМ СОВПАДЕНИЯ ПО ИМЕНИ ФАЙЛА И МЕНЯЕМ НА АТТРИБУТ---\\
                        if (findexpFolder.equals(expFolder)) {//если нашли фолдер
                            String path = findFolder.getAbsolutePath();//получили путь до файла
                            String folder = findStringInFile(path, fileName);//вроде как ищем строку содержащую имя файла(INT)

                            if (folder != null) {
                                cnt++;
                                System.out.println("Соответствие найдено");
                                String fullNameAttr = getAttributValue(folder, "Name=" + '"');//нашли значение аттрибута фолдера

                                System.out.println("В файле " + findFolder.getName() + " нашлось соответствие.");
                                String itemArray[] = {"Item", "Name", fullNameAttr};//массив чтобы найти ноду по имени и атрибутам

                                Node nodeRead = xmlsax.readDocument(path);//прочитали ноду
                                Node folderNode = xmlsax.returnFirstFinedNode(nodeRead, "Folder");//находим folder
                                Node itemsNode = xmlsax.returnFirstFinedNode(folderNode, "Items");//находим items
                                Node itemFolderNode = xmlsax.findNodeAtribute(itemsNode, itemArray);//нашли конкретный item в котором соответствия

                                xmlsax.editDataAttr(itemFolderNode, "Name", attrIntValue + "." + expInt);//изменили атрибут

                                xmlsax.writeDocument();
                            }
                        }
                    }
                }
            }
        }
        return cnt;
    }

    //---МЕТОД ЗАМЕНЫ ИМЕН И АЛГОРИТМИЧЕСКИХ ИМЕН В TYPE ФАЙЛАХ-----Grigory
    @SuppressWarnings("empty-statement")
    public boolean ChangeIntTypeFile(String dir, ArrayList<String[]> name, String nameTable, JProgressBar jProgressBar1) {//newName---массив замен   Name---массив из БД
        File[] filesInDirectory = new File(dir + "\\" + "Design").listFiles();//получаем список элементов по указанному адресу
        FileManager fm = new FileManager();
        XMLSAX xmlsax = new XMLSAX();
        String nameFile = "";
        String comment, alg, newComment, newAlg;
        String[] nameLine;
        boolean isErr = false;
        //пробегаемся по файлам в поисках соответствия 
        for (File findType : filesInDirectory) {
            nameFile = findType.getName();//имя файла с расширением(в котором в данный момент ищем)
            String nT = "T_" + nameTable;
            int ntl = nT.length();
            int nfl = nameFile.length();
            if ((nfl >= ntl + 5) && (nameFile.substring(0, ntl).equalsIgnoreCase(nT)) && (nameFile.substring(nfl - 5).equalsIgnoreCase(".type"))) {
                Node typeNoode = xmlsax.readDocument(dir + "\\" + "Design" + "\\" + nameFile);//открываем файл
                for (int j = 0; j < name.size(); j++) {//ищем строку в файле необходимую
                    nameLine = name.get(j);//получили строку с элементами
                    //----выполняем присваивание из массива каждому элементу---
                    comment = nameLine[0];
                    alg = nameLine[1];
                    newComment = nameLine[2];
                    newAlg = nameLine[3];
                    //---находим ноду по атрибуту Name(алгоритмичесукое имя)
                    String argLine[] = {"Field", "Name", alg};
                    Node find = xmlsax.findNodeAtribute(argLine);
                    //---если нашли ноду совершаем замену
                    if (find != null) {
                        xmlsax.editDataAttr(find, "Name", newAlg);//заменяем алгоритмическое имя
                        xmlsax.editDataAttr(find, "Comment", newComment);//заменяем русское имя
                    }
//                    else {
//                        //  FileManager.loggerConstructor("Не найдена нода \"" + nodeTable + "\"");
//                        //вывод ошибки в лог и выставлене флага ошибки
//                    }

                    if (jProgressBar1 != null) {
                        jProgressBar1.setValue((int) ((j + 1) * 100.0 / name.size()));
                    }
                }

            }
            xmlsax.writeDocument(globVar.desDir + "\\" + "Design" + "\\" + nameFile);
        }
        return isErr;
    }

    public static void main(String[] args) throws IOException {//для тестирования
        FileManager fm = new FileManager();
        loggerConstructor("Яй криветко");
    }
}
