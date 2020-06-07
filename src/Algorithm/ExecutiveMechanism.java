/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Algorithm;

import DataBaseConnect.DataBase;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;

/**
 *
 * @author admin
 */
public class ExecutiveMechanism {
    
    DataBase workbase; // подключаемся к базе и берем список
    String nameTable; // название таблицы и заголовка
    String[] columns; // названия колонок для таблицы в формате масива
    ArrayList<String> columnT; // названия колонок для таблицы
    int identNodecase = 0; // по идентификатор выбора какой механизм использовать
    String[] arrNameExecute ; // Список имен нод механизмов для передачи выбора пользователю
    ArrayList<Node> listNodeRootN; // ноды рута AM_Classica, AM_NKU и тд
    XMLSAX readXML; // наш конфиг файл
    
    // Формируем первоначальные данные для пользователя, выбор механизма обработки
    public ExecutiveMechanism(){
        readXML = new XMLSAX();
        Node rootN = readXML.readDocument(globVar.сonfigAMs); // Читаем конфигурационный файл
        listNodeRootN = readXML.getHeirNode(rootN); 
        arrNameExecute = new String[listNodeRootN.size()]; // Список имен нод
        
        for (int iN = 0; iN < listNodeRootN.size(); ++iN) {
            Node n = listNodeRootN.get(iN);
            String nameN = n.getNodeName(); // имена первых нод названия иструментов
            arrNameExecute[iN] = nameN;
        }
    }
    
        // --- Установить по какому методу будет обработка механизма(выбор Ноды)
    public ArrayList<ArrayList> getDataCurrentNode(int i){
     identNodecase = i; // может быть ексэпшен при get из Листа
     if(identNodecase <= arrNameExecute.length){
        Node n = listNodeRootN.get(identNodecase);
        return getMecha(n);
     }
        return null;
    }
    
    // --- Получить ноды механизмов обработки--
    public String[] getNodeMechRun(){
        return arrNameExecute;
    
    }
    
        // --- Получить список столбцов данных--
    public String[] getColumns(){
        return columns;
    }
    
    // --- чтение xml и формирование  из него каких таблиц читаем и что сапоставлять ---
    public ArrayList<ArrayList> getMecha(Node n ) { // На вход документ и выбранная уже нода
        ArrayList<ArrayList> findingTagname = new ArrayList();//листы для хранения найденного Что передаем
        nameTable = globVar.abonent + "_AM";    //  название таблицы строится
        //this.setTitle(nameTable); // Установить заголовок
        columnT = new ArrayList<>(); // заготовка названия колонок
        columnT.add("TAG_NAME");
        columnT.add("Наименование");
        columnT.add("совпадения");
        columnT.add("true/false");
        String[] nameColumnList = new String[]{"TAG_NAME_PLC", "Наименование сигнала", "Наименование"}; // наименование колонок для выборки из базы
        ArrayList<String> listColumnSelect = new ArrayList(); // листы с колонками для запроса к базе
        ArrayList<ArrayList> listTagName = new ArrayList(); // 
        boolean firstStep = true; // Переменная первого прохода формирования списка
        int sumArraySize = 0; // Переменная для определения длинны формирования массива (не верно)
        int biforeSumArraySize = 0;
        ArrayList<String> notFindTables = new ArrayList(); // лист не найденных таблиц для запроса из фала

//        XMLSAX readXML = new XMLSAX();
//        Node rootN = readXML.readDocument(globVar.сonfigAMs); // Читаем конфигурационный файл
//        ArrayList<Node> listNodeRootN = readXML.getHeirNode(rootN); // ноды рута AM_Classica, AM_NKU и тд
//        
//        String[] arrNameExecute = new String[listNodeRootN.size()]; // Список имен нод
//        for (int iN = 0; iN < listNodeRootN.size(); ++iN) {
//            Node n = listNodeRootN.get(iN);
//            String nameN = n.getNodeName(); // имена первых нод названия иструментов
//            arrNameExecute[iN] = nameN;
//        }
        
        // вызываем метод выбора по нодам какю хотим обработать(пока две)
//        getJDialogChoiser(arrNameExecute).setVisible(true); // вызываем диалог с выбором а вот тут оно не должно быть
//        Node n = listNodeRootN.get(identNodecase);
        {
            ArrayList<Node> listNodeMethodExe = readXML.getHeirNode(n); // получить наследников с ними и будем работать
            ArrayList<String> nameDGODGI = new ArrayList<>();
            ArrayList<String> listNameOnOff = new ArrayList<>();
            ArrayList<String> listOnOffDGO = new ArrayList<>(); // окончания DGO(не нужно)
            ArrayList<String> listOnOffDGI = new ArrayList<>(); // окончания DGI
            // пробегаем по on off
            for (int i = 0; i < listNodeMethodExe.size(); ++i) { // старт основного алгоритма  по две ноды анализ
                Node nodeConEnd = listNodeMethodExe.get(i);
                String nameDGOorDGI = nodeConEnd.getNodeName();
                nameDGODGI.add(nodeConEnd.getNodeName());

                for (Node nodeOnOff : readXML.getHeirNode(nodeConEnd)) {
                    String nNanmeEnd = nodeOnOff.getNodeName();
                    listNameOnOff.add("_" + nNanmeEnd); // имена первых нод названия иструментов(уникальность названия стобцов для базы)
                    HashMap<String, String> dataOnOff = readXML.getDataNode(nodeOnOff); // только одно пока данное
                    String strEnd = dataOnOff.get("end"); // забираем окончания
                    if (i % 2 == 0) {// Распределение окончаний для поиска
                        listOnOffDGI.add(strEnd); // сформировали окончания DGI
                        columnT.add(nameDGOorDGI + nNanmeEnd);
                    } else {
                        listOnOffDGO.add(strEnd); // сформировали окончания DGO
                        columnT.add(nameDGOorDGI + nNanmeEnd);
                    }
                }
            }

            for (int sequence = 0; sequence < nameDGODGI.size(); ++sequence) { // Проходим по именам нод
                String nameCaseTable = nameDGODGI.get(sequence);
                listColumnSelect.clear(); // так как при следующих прогонах оно добавляет данные
                String nameTreq = globVar.abonent + "_" + nameCaseTable;
                List<String> columnsB = workbase.selectColumns(nameTreq); // возмем Названия колонок из таблицы

                // как то правильно нужно прикрутить столбец совпадений
                int columnEntry = 2;
                //  идентификатор столбца comboBoxs 
                int columnTueFalse = 3;
                boolean fNameColumn = false;

                for (int ib = 0; ib < nameColumnList.length; ++ib) { // Формируем список столбцов к базе (мин.2)
                    String s = nameColumnList[ib];
                    String nameAlgColumn = compareData(columnsB, s); // передаем лист и строку на анализ соответствия(есть ли подобные столбцы). первый попавшийся возвращаем.
                    if (nameAlgColumn != null) {
                        listColumnSelect.add(nameAlgColumn); // Добавляем колонки для передачи в базу для запроса SELECT 
                        if (sequence % 2 == 0) {// если второе значение Колонки DGI пропуск
                            //columnT.add(nameTreq +"_"+nameAlgColumn);
                            //columnT.add(0,s); // Первым значением
                            if (fNameColumn == false) { // так же и только имя русское
                                //columnT.add(0,s); // Первым значением
                                fNameColumn = true;
//                                columnT.add(columnEntry,"совпадения"); // Только при одном проходе(ноды только 2)
                            }
                        }
//                        columnT.add(0,s); // Первым значением
                    }
                }

                if (listColumnSelect.size() > 0) { //если есть что то для запроса к базе по столбцам
                    ArrayList<String[]> dataFromBase = workbase.getData(nameTreq, listColumnSelect); // получили массивы листов из базы 
                    listTagName.add(dataFromBase); // Кладем структуру в Лист

                } else {
                    notFindTables.add(nameTreq); // Нечего не нашли добавляем  название таблицы в массив для проблемных
                }
                if (listTagName != null && listTagName.size() > 1) {
                    int elMass = 0; // первый элемент Листа
                    int numElTagName = 0;// номер элемента с tag_name массива для сравнения (надо определять автоматом)
                    int numElRusName = 1;//   
                    //String[] egnoredList = new String[]{"NULL", "Res_", "Res"}; // Список окончания сигналов(в этой реализации может и не нужен)
                    String[] egnoredList = new String[]{}; // Список окончания сигналов(в этой реализации может и не нужен)
                    //String[] dataToParserDGO = listOnOffDGO.toArray(new String[listOnOffDGO.size()]); // это из за предыдущего алгоритма (пока для пробы)
                    //String[] dataToParserDGI = listOnOffDGI.toArray(new String[listOnOffDGI.size()]); // это из за предыдущего алгоритма (пока для пробы)
                    ArrayList<String[]> listDO = listTagName.get(numElTagName);
                    ArrayList<String[]> listDI = listTagName.get(numElRusName);
                    int numElTagname = 0;// номер элемента с tag_name массива для сравнения

                    while (listDO.size() > 0) {  // пробегаем по листу но тут только 1 так как Tag_name 
                        ArrayList<String> findTmp = new ArrayList(); // Временный 
                        String[] s = listDO.get(elMass); // Первый элемент DO 
                        if (checkSignal(s[numElTagname], egnoredList)) { // если сигнал из листа эгнорирования
                            listDO.remove(elMass);
                        } else {
                            if (s.length > 1) { // для подстраховки если не нашли имена столбцов
                                findTmp.add(s[numElRusName]);  // добавить русское название в столбец
                            }
                            int enterEndDO = 0; // триггер вхождения в поиск окончания DO

                            // сигнал DO но по всем окончаниям on Off
                            String commonSliceSig = null; // общая часть DO перед окончанием
                            for (String endDGO : listOnOffDGO) {
                                commonSliceSig = comparSignalEnd(s[numElTagname], endDGO);
                                if (commonSliceSig != null) {
                                    //++enterEndDO;
                                    //System.out.println(commonSliceSig);
                                    break;
                                }
                            }

                            if (commonSliceSig != null) {
                                findTmp.add(commonSliceSig); // Добавляем переменную без окончания
                            } else {
                                findTmp.add(s[numElTagname]); // Добавляем просто переменную если нет для нее патерна
                            }
                            findTmp.add("false"); // данные для колонки columnTueFalse
                            
                            boolean findDOCompareOnce = false; // триггер нахождения окончания DGO
                            // прежде чем удалить из DO надо прогнать по всем окончаниям
                            for (String endDGO : listOnOffDGO) { // прогоняем по окончаниям DGO ON всегда первый
                                int jLoc = elMass;
                                boolean findDOCompare = false;
                                while (jLoc < listDO.size()) { // прогоняем по самому себе DO
                                    String[] locSeco = listDO.get(jLoc); // следующие элементы
                                    String strSig = null;
                                    strSig = checkSliceSignal(commonSliceSig, locSeco[numElTagname], endDGO);
                                    if (strSig != null) {
                                        findTmp.add(locSeco[numElTagname]); //если нашлми что там то добавляем
                                        findDOCompare = true;
                                        findDOCompareOnce = true;
                                        listDO.remove(jLoc);// удаляем из списка
                                        ++enterEndDO; // Соответственно триггер вхождения увеличиваем
                                    } else {
                                        ++jLoc;
                                    }
                                }
                                if (findDOCompare == false) {
                                    findTmp.add(""); // не нашли для этого окончания нечего noneDO
                                }
                            }
                            if (findDOCompareOnce == false) {
                                listDO.remove(elMass); //удаляем если не один патер не подошел
                            }
                            
                            int enterEndDI = 0; // триггер вхождения в поиск окончания DI
                            for (String endDGI : listOnOffDGI) { // прогоняем по окончаниям DGI
                                boolean findDICompare = false; // триггер нахождения окончания DGI
                                int j = 0;
                                while (j < listDI.size()) { // Проходим по листу DI
                                    String[] sj = listDI.get(j); // это элемент который 
                                    String retFinStr = null;
                                    retFinStr = checkSliceSignal(commonSliceSig, sj[numElTagname], endDGI); // вызываем функцию сравнения(патерн на основе 1 строки)
                                    //if (retFinStr != null) break;                                 
                                    if (retFinStr != null) {
                                        findTmp.add(sj[numElTagname]); //если нашли,добавляем
                                        listDI.remove(j);// удаляем из списка
                                        ++enterEndDI;// инкримент вхождения
                                        findDICompare = true;
                                    } else {
                                        ++j;
                                    }
                                }
                                if (findDICompare == false) {// не нашли окон.
                                    findTmp.add(""); // noneDI
                                }
                            }
                            //System.out.println("Sig "+ commonSliceSig+" in DO_List " 
                            //        + " " + Integer.toHexString(enterEndDO) +" in DI_List "+ enterEndDI);
                            String entry = "ИМ: " + Integer.toHexString(enterEndDO) + "x" + Integer.toHexString(enterEndDI);
                            findTmp.add(columnEntry, entry);  // данные о совпадении

                            findingTagname.add(findTmp);
                            //}  
                        }
                    }
                    /*  // это для записи в файл(проверка что по данным)
                     for (ArrayList list : findingTagname) { 
                     for (Iterator it = list.iterator(); it.hasNext();) {
                     String s = (String) it.next();
                     //System.out.print(s + " - ");
                     }
                     FileManager.writeCSV("665.txt", list);
                     }
                     */
                }
            }
            if (notFindTables.size() <= 0) { // Добавляем ли мы колонки для таблицы или нет так как данные могут быть пустыми
                //columnT.addAll(listOnOffDGO); // Сращиваем с значениями нод столбцов
                //columnT.addAll(listOnOffDGI); // Сращиваем с значениями нод столбцов
            }

        }
        columns = columnT.toArray(new String[columnT.size()]); // преобразовываем в массив
       
        // и сообщение если есть какие то неполадки
        if (notFindTables.size() > 0) {
            String message = "";
            for (String s : notFindTables) {
                message += s + "\n";
            }
            JOptionPane.showMessageDialog(null, "Не найдены таблицы \n" + message); //сообщение
        }

//         сортировка
       for(int iArr=0; iArr < findingTagname.size(); ++iArr){
           int nextItem = iArr+1;
           if(nextItem >= findingTagname.size()){
               System.out.println("Last value");
               break;
           }else{
               for(int iArrSec=nextItem; iArrSec < findingTagname.size(); ++iArrSec){
                ArrayList<String> arr = findingTagname.get(iArr);
                int summArr = 0;
                for(String s: arr){ // вычисляем сколько элементов в листе кроме пустых строк
                    if(s.equals("")){
                        continue;
                    } else ++summArr;
                }
                ArrayList<String> arrSecond = findingTagname.get(iArrSec);
                int summArrSecond = 0;
                for(String s: arrSecond){ // вычисляем сколько элементов во втором листе
                    if(s.equals("")){
                        continue;
                    } else ++summArrSecond;
                }
                if(summArrSecond > summArr){
                    findingTagname.set(iArr, arrSecond);
                    findingTagname.set(iArrSec, arr);
               }
              }
           }
       }
       
        return findingTagname;
    }
    
     // --- регулярка для сравнения столбцов (определенно что то тут не так и надо пеедлать) --
    String compareData(List<String> columnsB, String str2) {
        String findDat = null;
        String hmiIns = "_plc";
        boolean find = false;
        boolean patt0 = false;
        boolean patt1 = false;
        boolean pattHMI = false;
        Pattern pattern0 = Pattern.compile("^(" + str2 + "|" + str2.toUpperCase() + ")$"); // точное соответствие но с игнорированем CAPS
        //Pattern patternHMI = Pattern.compile("^.*(" + str2 + ".*" + hmiIns + "|" + str2.toUpperCase() + ".*" + hmiIns.toUpperCase() + ").*$"); // если в имени есть HMI
        Pattern patternHMI = Pattern.compile("^.*(" + str2 + ".*" + hmiIns + ")|(" + str2.toUpperCase() + ".*" + hmiIns.toUpperCase() + ").*"); // если в имени есть HMI
        Pattern pattern1 = Pattern.compile("^.*(" + str2 + "|" + str2.toUpperCase() + ").*$"); // Любая строка в верхнем и нижнем регистре
        Matcher matcher0;
        int equalI;
        for (int i = 0; i < columnsB.size(); ++i) { // Пробежим для анализа по ним
            String s = columnsB.get(i);
            matcher0 = pattern0.matcher(s); // Применяем строку к патерну точного совпадения
            if (matcher0.matches()) {
                findDat = s;
                patt0 = true;
                find = true;
                equalI = i;
                //break;
            }
            if (!patt0) { // сюда заходим если никогда не нашли что в списке
                matcher0 = patternHMI.matcher(s); // Применяем строку к патерну все внутри + строка hmiIns после 
                if (matcher0.matches()) {
                    findDat = s;
                    pattHMI = true;
                    find = true;
                    equalI = i;
                    //break;
                }
                if (!pattHMI) {
                    matcher0 = pattern1.matcher(s); // Применяем строку к патерну все внутри
                    if (matcher0.matches()) {
                        findDat = s;
                        patt1 = true;
                        find = true;
                        equalI = i;
                        // break;
                    }
                }
            }
        }
        return findDat;
    }

    
     // --- проверка имени сигнала, общей части окончания ---
    String checkSliceSignal(String str, String sig, String end) {
        String finderS = null;
        String cutString = null;
        boolean fEndStr1 = false; // триггер нахождения окончания по первой входящей строки
        int idElem = 0;
        cutString = "^(" + str + ")(.*)$";
        Pattern pattern0 = Pattern.compile(cutString);
        Matcher matcher0 = pattern0.matcher(sig);  // впихиваем в сравнение 1 строку из входа
        if (matcher0.matches()) { // Окончание совпало
            //System.out.println(matcher0.group(0)); // Системный вывод
            //str1 = matcher0.group(1); // названия сигнала без окончания
            fEndStr1 = true;
        }
        if (fEndStr1) { // общие части совпали
            String patternS = "^(" + str + ")" + end + "$"; // прикручиваем окончание и сравниваем
            Pattern patternOFF = Pattern.compile(patternS);
            Matcher matcherOFF = patternOFF.matcher(sig);  // впихиваем в сравнение 1 строку из входа
            if (matcherOFF.matches()) { // попали под патерн 
                finderS = sig; // Нужный нашли
            }
        }
        return finderS;
    }

    
    // ---  Сверка сигнала по окончанию ---
    String comparSignalEnd(String str1, String endS) {
        String sigF = null;
        String cutString = "^(.*)" + endS + "$";
        Pattern pattern0 = Pattern.compile(cutString);
        Matcher matcher0 = pattern0.matcher(str1);  // строка из входа входа на сравнение 
        if (matcher0.matches()) { // Окончание совпало
            //System.out.println(matcher0.group(0)); // Системный вывод
            sigF = matcher0.group(1); // названия сигнала без окончания
        }
        return sigF;
    }
    
    // --- проверка имени сигнала, есть ли совпадения с списком c начала Строки ---
    boolean checkSignal(String str, String[] massName) {
        boolean enterSig = false;
        String cutString;
        for (int i = 0; i < massName.length; ++i) { // пробегаем по списку окончаний отрезая окончания
            cutString = "^(" + massName[i] + ".*)" + "|" + "^(" + massName[i].toUpperCase() + ".*)" + "$";
            //s= s.substring(0,1).toUpperCase() + s.substring(1); // может пригодится первый символ из строки в верхнем регистре
            //s = Character.toUpperCase(s.charAt(0)) + s.substring(1); // может пригодится первый символ из строки в верхнем регистре
            Pattern pattern0 = Pattern.compile(cutString);
            Matcher matcher0 = pattern0.matcher(str);  // впихиваем в сравнение строку из входа
            if (matcher0.matches()) { // попали под патерн 
                System.out.println("Findind unnecessary sig --> " + matcher0.group(0)); // Системный вывод
                enterSig = true;
                break; // до первого найденного
            }
        }
        return enterSig;
    }
    

}
