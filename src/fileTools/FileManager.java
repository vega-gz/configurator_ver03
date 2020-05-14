/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileTools;

import StringTools.StrTools;
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

//import main.globVar;
public class FileManager {

    public static ArrayList<String> listAllPath = new ArrayList(); // отдельно вытащил из за рекурсии в pathAllFile

    public File wrFile;
    public File rdFile;
    public FileOutputStream fos;
    public FileInputStream fis;
    public Writer wrStream;
    public Reader rdStream;
    public boolean EOF;

    // --- копирование файла используя поток ---
    public static int copyFileWoReplace(String source, String dest, boolean notCopyFile) throws IOException {
        File copy = new File(dest);
        if(copy.isFile()){//Проверяем, есть ли такой файл в целевом каталоге
            if(notCopyFile) return 1; //Если есть и есть признак "не размножать копии" заканчиваем работу
            for(int i = 1; copy.isFile(); i++){ //Если признака нет - создаём копию с номером версии
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
            if(is == null) return 2;
            if(os == null) return 3;
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
    public static void loggerConstructor(String s){// throws FileNotFoundException {
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
    public void closeWrStream() throws IOException{
        wrStream.close();
        fos.close();
    }
    public void closeRdStream() throws IOException{
        rdStream.close();
        fis.close();
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

    public String findStringInFile(String fileName, String f) throws IOException{
        int ret = openFile4read(fileName);
        if(ret!=0)return null;
        String s = rd();
        int n = 0;
        while(!EOF && !s.contains(f) && !s.contains("<Resource>")) {
            s = rd();
            System.out.println(n);//------------------------------------------ check print ----------------------------
            n++;
        }
        closeRdStream();
        if(s.contains(f))return s;
        else return null;
    }
    public static String findStringInFile1(String fileName, String f){
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String s;
            while ((s = in.readLine()) != null) if(s.contains(f))return s;
        }catch (IOException e) {}
        return null;
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
        String nameWords = "Name=";
        String ext = ".TYPE";
        String firstName, secondName, fileName = null;
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
                                fm.rdStream.close();
                                return fn;
                            }
                        }
                    }
                    fm.rdStream.close();
                }
            }
        } else JOptionPane.showMessageDialog(null, "Путь к генерации не найден" + dir);
        return null;
    }
    public static String getUUIDFromFile(String dir, String nameType) throws IOException {
        String nameWords = "Name=";
        String uuidWords = "UUID=";
        String ext = ".TYPE";
        String firstName, secondName, fileName = null;
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
        } else JOptionPane.showMessageDialog(null, "Путь " + dir + " не найден");
        return null;
    }

    public static void createBackupDir(){
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
    
    public static int renameUUIDinFile(File fileName){
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName+"tmp"));
            String s;
            String uuid;
            while ((s = in.readLine()) != null){
                int start = s.indexOf(" UUID=\"");
                if(start>=0){
                    start+= 7;
                    int end = s.indexOf("\"", start); 
                    uuid = s.substring(start, end).toUpperCase();
                    s = s.substring(0,start)+ uuid + s.substring(end);
                }
                start = s.indexOf("TypeUUID=\"");
                if(start>=0){
                    start+= 10;
                    int end = s.indexOf("\"", start); 
                    uuid = s.substring(start, end).toUpperCase();
                    s = s.substring(0,start)+ uuid + s.substring(end);
                }
                start = s.indexOf("Type=\"");
                if(start>=0){
                    start+= 6;
                    int end = s.indexOf("\"", start); 
                    if(end-start == 32){
                        uuid = s.substring(start, end).toUpperCase();
                        s = s.substring(0,start)+ uuid + s.substring(end);
                    }
                }
                out.write(s+"\n");
            }
            in.close();
            out.close();
            //File file = new File(fileName);                             //создаём ссылку на исходный файл
            fileName.delete();                                             //удаляем его
            new File(fileName+"tmp").renameTo(fileName);                          //создаём ссылку на сгенерированный файл и делаем его исходным
        }catch (IOException e) {}
        return 0;
    }
    
    public static int renameUUIDinDirectory(String parentDirectory) throws FileNotFoundException, IOException {
        File[] filesInDirectory = new File(parentDirectory).listFiles();//получаем список элементов по указанному адресу
        for (File f : filesInDirectory) {//пробегаем по списку элементов по указанному адресу
            renameUUIDinFile(f);
        }
        return 0;
    }

    public void renameTypeFile(String parentDirectory) throws FileNotFoundException, IOException {
        File[] filesInDirectory = new File(parentDirectory).listFiles();//получаем список элементов по указанному адресу

        String typeAttr,folderAttr=null;//переменная имени типа
        String expType = "type";
        String expFolder = "folder";
        String fileName;//имя самого файла type

        //FileManager fm = new FileManager();

        for (File findType : filesInDirectory) {//пробегаем по списку элементов по указанному адресу
            String Name = findType.getName();
            String findexp = Name.substring(Name.lastIndexOf(".") + 1);
            //System.out.println(findexp);//находим расширение файла

            if (findType.isDirectory()) {//если файл дирректория что то придумать

            } else if (findexp.equals(expType)) {//если расширение type ,то читаем файл
                String findName = findStringInFile1(Name, "Name=");
                typeAttr = StrTools.getAttributValue(findName, "Name=\"");//находим этот аттрибут
                fileName = findType.getName().substring(0, Name.indexOf('.'));//имя файла без расширения(ибо прога читает с расширением)
                if (!fileName.equals(typeAttr)) {//сравниваем имя файла и имя типа(если не равны ищем фолдер)
                    for (File findFolder : filesInDirectory) {//пробегаемся по той же дирректории в поисках folder
                        if (findexp.equals(expFolder)) {//если нашли folder то начинаем его читать(нашли файл с расширение фолдер)
                            String fold = findFolder.getName();
                            String foldexp = fold.substring(Name.lastIndexOf(".") + 1);
                            String path = findFolder.getAbsolutePath();//получили путь до файла
                            XMLSAX xmlsax = new XMLSAX();
                            xmlsax.readDocument(path);
                            String folder = findStringInFile1(findFolder.getName(), fileName);//вроде как ищем строку содержащую имя файла(TYPE)
                            if(folder!=null) folderAttr = StrTools.getAttributValue(folder,"Name=\"");//нашли значение аттрибута фолдера
                            if (folderAttr!=null && folderAttr.equals(fileName)) {//находим соответствие в folder если
                                fileName = findName;//меняем имя типа 
                                folder.indexOf(fileName.length());
                                findType.renameTo(new java.io.File(typeAttr+"."+expType));//переименовали файл type
                              //  Node folderNode=xmlsax.returnFirstFinedNode(globVar.cfgRoot, "Folder");
                                                     
                                
                            }

                        }
                    }

                    break;

                }

            }

        }
    }

//    public static void main(String[] args) throws IOException {//для тестирования
//        FileManager fm = new FileManager();
//        // fm.findWords("C:\\Users\\Григорий\\Desktop\\новый конфиг и excel\\ConfigSignals.xml");
//        //  FindFile("C:\\Users\\Григорий\\Desktop\\сиг\\T_GPA_DI_ToProcessing.type", "T_GPA_DI_ToProcessing");
//     //   fm.FindFile("C:\\Users\\Григорий\\Desktop\\сиг", "T_GPA_AI_FromProcessing");
//    }
}
