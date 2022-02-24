/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

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
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;
import org.w3c.dom.Node;

public class FileManager {

    public static ArrayList<String> listAllPath = new ArrayList(); // отдельно вытащил из за рекурсии в pathAllFile

    public File wrFile = null;
    public File rdFile = null;
    public FileOutputStream fos = null;
    public FileInputStream fis = null;
    public Writer wrStream = null;
    public Reader rdStream = null;
    public boolean EOF;

    /**
     * Метод копирует файл используя поток ---
     *
     * @param source
     * @param dest
     * @param notCopyFile
     * @return
     * @throws IOException
     */
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

    /**
     * Метод поиска файла в конкретной папке.
     *
     * @param path путь до дирректории
     * @param s название файла,который мы ищем
     * @return лист с путями,где лежит файл
     */
    public static ArrayList<String> findFile(String path, String s) {
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

    /**
     * Метод рекурсивно удаляет дирректорию со всеми файлами
     *
     * @param dir путь до дирректории ,которую удаляем
     */
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

    /**
     * --- Логирование ошибок и другая информация ---
     *
     *
     */
    public static void writeLogToFile(String s) {// throws FileNotFoundException {
        String nameF = globVar.logFile;
        File logF = new File(nameF);
        if (!logF.exists()) { // нет файла то создаем
            try {
                logF.createNewFile();
                writeLogToFile("Лог файла не было, был создан");
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

    public int createFile2write(String fileName) {
        int eventReturn = 0;
        try {
            wrFile = new File(fileName);
            if (wrFile.isFile()) {
                wrFile.delete();
            }
            if (!wrFile.createNewFile()) {
                eventReturn = 2; // не удалось создать файл
            }
            fos = new FileOutputStream(wrFile, false);
            wrStream = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eventReturn;
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

    // --- Добавить строку в файл(почему Лев так назвал метод) ---
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

    /**
     * Метод проходит все файлы по указанному пути,читает файлы с расширением
     * file , переименовывает файл согласно значению его атрибута Name.
     *
     * @param dir путь до папки проекта
     * @exception IOException
     * @exception FileNotFoundException
     */
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

    /**
     * Метод проходит все файлы по указанному пути,читает файлы с расширением
     * int , переименовывает файл согласно значению его атрибута Name.
     *
     * @param dir путь до папки проекта
     * @exception FileNotFoundException
     * @exception IOException
     */
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

    public static int ChangeOPCNameFile(String dir) throws FileNotFoundException, IOException {
        File[] firstDirectory = new File(dir + File.separator + "Design").listFiles();
        XMLSAX xmlsax = new XMLSAX();
        int counter = 0;//счетчик количества изменений
        String nameFile_opcINT = "";//OPC файл с расширение int
        String nameFile_opcOPCCUASERVER = "";//OPC файл с расширение opccuaServer
        File newName;
        String expOPC;
        String opcServerName = null;
        ArrayList<String> oldNameOPC = new ArrayList<>();
        ArrayList<String> newNameOPC = new ArrayList<>();
        for (File findFile : firstDirectory) {
            if (findFile.isDirectory()) {//если это дирректория - ничего не делать
                continue;
            }
            nameFile_opcINT = findFile.getName();//имя файла с расширением(текущее)
            expOPC = nameFile_opcINT.substring(nameFile_opcINT.lastIndexOf(".") + 1);//получаем расширение
            System.out.println(nameFile_opcINT);
            Node typeNode = xmlsax.readDocument(dir + File.separator + "Design" + File.separator + nameFile_opcINT);//открываем файл
            Node findSubAppType = xmlsax.returnFirstFinedNode(typeNode, "SubAppType");//ищем ноду для файла OPC
            String kind = xmlsax.getDataAttr(findSubAppType, "Kind");//тип приложения,для того чтобы найти файлы OPC
            if (findSubAppType != null && kind != null && kind.contains("OPC")) {//если нода нашлась
                opcServerName = xmlsax.getDataAttr(findSubAppType, "Name");//получаем значение аттрибута
                newName = new File(dir + File.separator + "Design" + File.separator + opcServerName + "." + expOPC);
                if (findFile.renameTo(newName)) {
                    String fillNameOPC_int = nameFile_opcINT.substring(0, nameFile_opcINT.indexOf('.'));//имя файла без расширения
                    oldNameOPC.add(fillNameOPC_int);
                    newNameOPC.add(opcServerName);
                    System.out.println("файл " + findFile + " переименовался в " + newName);
                    counter++;
                }
            }
        }
        File[] secondDirectory = new File(dir + File.separator + "Design").listFiles();//обновленная директория
        for (int i = 0; i < oldNameOPC.size(); i++) {
            String oldOPC = oldNameOPC.get(i);
            String newOPC = newNameOPC.get(i);
            for (File findOPC : secondDirectory) {
                if (findOPC.isDirectory()) {//если это дирректория - ничего не делать
                    continue;
                }
                nameFile_opcOPCCUASERVER = findOPC.getName();
                expOPC = nameFile_opcOPCCUASERVER.substring(nameFile_opcOPCCUASERVER.lastIndexOf(".") + 1);//ищу расширение opcServer
                String fillNameOPC_cuaServer = null;
                try {
                    fillNameOPC_cuaServer = nameFile_opcOPCCUASERVER.substring(0, nameFile_opcOPCCUASERVER.indexOf('.'));//имя без расширения
                } catch (StringIndexOutOfBoundsException ex) {
                    System.out.println("Попался файл без расширения");
                }
                if (oldOPC.equals(fillNameOPC_cuaServer)) {
                    newName = new File(dir + File.separator + "Design" + File.separator + newOPC + "." + expOPC);
                    findOPC.renameTo(newName);
                    System.out.println("файл " + findOPC + " переименовался в " + newName);
                    counter++;
                }
            }
        }
        System.out.println("Произведено замен " + counter);
        return counter;
    }


    /**
     * Метод заменяет русское и алгоритмическое имена в базе данных и XML файлах
     * теми значениями ,которые ввел пользователь в соответствущих ячейка
     * таблицы.
     *
     * @param dir путь до папки проекта
     * @param name лист Русских имен и Алгоритмических имен,которые были
     * изменены
     * @param nameTable наименование таблицы,в которой была произведены замена
     *
     */
    @SuppressWarnings("empty-statement")
    public boolean ChangeIntTypeFile(String dir, ArrayList<String[]> name, String nameTable, JProgressBar jProgressBar1) {//newName---массив замен   Name---массив из БД
        File[] filesInDirectory = new File(dir + File.separator + "Design").listFiles();//получаем список элементов по указанному адресу
        FileManager fm = new FileManager();
        XMLSAX xmlsax = new XMLSAX();
        String nameFile = "";
        String comment, alg, newComment, newAlg;
        String[] nameLine;
        boolean isErr = false;
        for (File findType : filesInDirectory) {
            nameFile = findType.getName();//имя файла с расширением(в котором в данный момент ищем)
            String nT = "T_" + nameTable;
            int ntl = nT.length();
            int nfl = nameFile.length();
            if ((nfl >= ntl + 5) && (nameFile.substring(0, ntl).equalsIgnoreCase(nT)) && (nameFile.substring(nfl - 5).equalsIgnoreCase(".type"))) {
                Node typeNoode = xmlsax.readDocument(dir + File.separator + "Design" + File.separator + nameFile);//открываем файл
                for (int j = 0; j < name.size(); j++) {//ищем строку в файле необходимую
                    nameLine = name.get(j);//получили строку с элементами
                    //----выполняем присваивание из массива каждому элементу---
                    comment = nameLine[0];
                    alg = nameLine[1];
                    newComment = nameLine[2];
                    newAlg = nameLine[3];
                    //---находим ноду по атрибуту Name(алгоритмическое имя)
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
                xmlsax.writeDocument(globVar.desDir + File.separator + "Design" + File.separator + nameFile);
            }

        }
        return isErr;
    }
    public  boolean ChangefFileWBase(String dir, ArrayList<String[]> name,ArrayList<String[]>oldname, String nameTable, JProgressBar jProgressBar1) {//newName---массив замен   Name---массив из БД
        File[] filesInDirectory = new File(dir + File.separator + "Design").listFiles();//получаем список элементов по указанному адресу
        FileManager fm = new FileManager();
        XMLSAX xmlsax = new XMLSAX();
        String nameFile = "";
        String alg, newComment, newAlg;
        boolean isErr = false;
        /**
         * пробегаемся по файлам в поисках соответствия
         */
        for (File findType : filesInDirectory) {
            nameFile = findType.getName();//имя файла с расширением(в котором в данный момент ищем)
            String nT = "T_" + nameTable;
            int ntl = nT.length();
            int nfl = nameFile.length();
            if ((nfl >= ntl + 5) && (nameFile.substring(0, ntl).equalsIgnoreCase(nT)) && (nameFile.substring(nfl - 5).equalsIgnoreCase(".type"))) {
                Node typeNoode = xmlsax.readDocument(dir + File.separator + "Design" + File.separator + nameFile);//открываем файл
                for (int j = 0; j < name.size(); j++) {//ищем строку в файле необходимую
                    //----выполняем присваивание из массива каждому элементу---
                    alg = oldname.get(j)[2];
                    newComment = name.get(j)[1];
                    newAlg = name.get(j)[2];
                    //---находим ноду по атрибуту Name(алгоритмическое имя)
                    String argLine[] = {"Field", "Name", alg};
                    Node find = xmlsax.findNodeAtribute(argLine);
                    //---если нашли ноду совершаем замену
                    if (find != null) {
                        xmlsax.editDataAttr(find, "Name", newAlg);//заменяем алгоритмическое имя
                        xmlsax.editDataAttr(find, "Comment", newComment);//заменяем русское имя
                    }
                    if (jProgressBar1 != null) {
                        jProgressBar1.setValue((int) ((j + 1) * 100.0 / name.size()));
                    }
                }
                xmlsax.writeDocument(globVar.desDir + File.separator + "Design" + File.separator + nameFile);
                System.out.println("");
        
            }
        }
        System.out.println("");
        return isErr;
    }

    
    // --- Диалоговое окно с выбором Excel файлов ---
    public static File getChoiserExcelFile(){
    File excel = null;
    JFileChooser fileopen = new JFileChooser(System.getProperty("user.dir"));
        fileopen.setFileFilter(new FileFilter() { // фильтр файлов
            public String getDescription() {
                return "Excel (*.xls *.xlsx *.xlsm)";
            }
            //@Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".xls") | filename.endsWith(".xlsx") | filename.endsWith(".xlsm");
                }
            }
        });
        int ren = fileopen.showDialog(null, "Загрузка данных для " + globVar.abonent);
        if (ren == JFileChooser.APPROVE_OPTION) {
            excel = fileopen.getSelectedFile();// выбираем файл из каталога
        }
        return excel;
    }

        // --- запись файла строкой (полная перезапись файла данными  )---
    public static void writeStringInFile(String file, String Data) {
        FileWriter fooWriter = null;
        try {
            File myFoo = new File(file);
            fooWriter = new FileWriter(myFoo, false); // true to append
            fooWriter.write(Data);
            fooWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fooWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
        // --- запись файла строкой (полная перезапись файла данными  )---
    public static void writeAddStringToFile(String file, String Data) {
        FileWriter fooWriter = null;
        try {
            File myFoo = new File(file);
            fooWriter = new FileWriter(myFoo, true); // true to append
            fooWriter.write(Data + "\n");
            fooWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fooWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    // --- подсчет строк в файле ---
    static int sumStringInFile(String path) {
        File file = new File(path);
        int linesCount = 0;
        try {
            final LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            while (null != lnr.readLine()) {
                linesCount++;
            }
            //System.out.println("Koличecтвo cтpok в фaйлe: " + linesCount);
            lnr.close(); // обязательно закрыть hz почему
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }
        return linesCount;
    }
    
    
    // --- Удалить строку из файла ---
    static void delStringNumberFile(String path, int nunStr) {
        File file = new File(path);
        String str = "";
        try {
            File temp = File.createTempFile("file_", ".txt", file.getParentFile());          // временный файл
            InputStreamReader inputsream = new InputStreamReader(new FileInputStream(file), "UTF8"); // Правильное отображение русскаого языка чтение буыера такое себе
            BufferedReader bufferedReader = new BufferedReader(inputsream);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8")); // так еще можно на открытие
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF8"));

            String line = bufferedReader.readLine();
            int currentLine = 0;
            while (line != null) {
                
                if (currentLine != nunStr) {
                    writer.println(line); // просто пропуск нужной строки и все
                }
                ++currentLine;
                line = bufferedReader.readLine();
            }
            bufferedReader.close(); // Закрываем потоки
            writer.close();

            file.delete();      // удалить 
            temp.renameTo(file); // переименовать

        } catch (FileNotFoundException e) {
            // exception handling
        } catch (IOException e) {
            // exception handling
        }
    }
}
