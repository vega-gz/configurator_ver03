/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import DataBaseConnect.DataBase;
import FrameCreate.TaskBarFrame;
import XMLTools.XMLSAX;
import globalData.globVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.w3c.dom.Node;

/**
 *
 * @author admin
 */
public class ExecutiveMechanismObject {

    DataBase workbase = globVar.DB; // подключаемся к базе и берем список
    String nameTable; // название таблицы и заголовка
    String[] columns; // названия колонок для таблицы в формате масива
    ArrayList<String> columnT; // названия колонок для таблицы
    int identNodecase = 0; // по идентификатор выбора какой механизм использовать
    String[] arrNameExecute; // Список имен нод механизмов для передачи выбора пользователю
    ArrayList<Node> listNodeRootN; // ноды рута AM_Classica, AM_NKU и тд
    XMLSAX readXML; // наш конфиг файл
    ArrayList<Integer> iniySplitList = new ArrayList<>();  // список разделителя таблиц механизмов

    // Формируем первоначальные данные для пользователя, выбор механизма обработки
    public ExecutiveMechanismObject() {
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
    // возвратит данные без обреза по окончания или с ними
    public ArrayList<ArrayList> getDataCurrentNode(int i, boolean missWE) {
        identNodecase = i; // может быть ексэпшен при get из Листа
        if (identNodecase <= arrNameExecute.length) {
            Node n = listNodeRootN.get(identNodecase);
            return getMecha(n, missWE); // возратить полные данные
        }
        return null;
    }

    // Получить все данные механизмов 
    public ArrayList<ArrayList> getDataAllMechaNode(boolean missWE) {
        ArrayList<ArrayList> fistConnectList = null;
        ArrayList<ArrayList> finalList = null;
        String nameTableFirst;
        String[] columnsFirst = null;
        ArrayList<ArrayList> secondConnectList;
        String nameTableSecond;
        String[] columnsSecond;
        
        for (int i=0; i< listNodeRootN.size(); ++i) { //проходим по всем нодам механизмов
            Node n = listNodeRootN.get(i);
            
        
        if (i == 0) {
                fistConnectList = getMecha(n, missWE);
                nameTableFirst = getNameTable(); // получим название таблицы строга после getDataCurrentNode
                columnsFirst = getColumns(); // получить колонки для построки таблицы
                continue;
            } else if (i > 0) {
                secondConnectList = getMecha(n, missWE);
                nameTableSecond = getNameTable(); // получим название таблицы строга после getDataCurrentNode
                columnsSecond = getColumns(); // получить колонки для построки таблицы

                // Сращиваем данные
                columnsFirst = Stream.concat(Arrays.stream(columnsFirst), Arrays.stream(columnsSecond)).toArray(String[]::new);  // контекация массивов колонок
                finalList = connectListMechan(fistConnectList, secondConnectList); // сращиваем Листы Листов            }

        }
        }
        return finalList;
    }

    // --- Получить ноды механизмов обработки--
    public String[] getNodeMechRun() {
        return arrNameExecute;

    }

    // --- Получить список столбцов данных массивом--
    public String[] getColumns() {
        return columns;
    }

    // --- Получить список столбцов данных листом (зачем два разных ?)--
    public ArrayList<String> getColumnsT() {
        return columnT;
    }

    // -- вернуть имя таблицы --
    public String getNameTable() {
        return nameTable;
    }

    // --- чтение xml и формирование  из него каких таблиц читаем и что сапоставлять ---
    // На вход документ и выбранная уже нода, и пропускать сигнал коорорый ни с кем не совпал или нет
    public ArrayList<ArrayList> getMecha(Node n, boolean missWE) {
        ArrayList<ArrayList> findingTagname = new ArrayList();//листы для хранения найденного Что передаем
        nameTable = globVar.abonent + "_AM";    //  формируем название таблицы строится
        //this.setTitle(nameTable); // Установить заголовок
        columnT = new ArrayList<>(); // заготовка названия колонок
        columnT.add("Наименование");
        columnT.add("TAG_PLC");
        columnT.add("совпадения");
        columnT.add("true/false");
        String[] nameColumnList = new String[]{"TAG_NAME_PLC", "Наименование сигнала", "Наименование"}; // наименование колонок для выборки из базы
        String[] endRusName = new String[]{"Открыть","закрыть", "включить"};
        String[] simbolRemove = new String[]{"-",","}; // символы которые нужно удалить из конца строки русских названий
        ArrayList<String> listColumnSelect = new ArrayList(); // листы с колонками для запроса к базе
        ArrayList<ArrayList> listTagName = new ArrayList(); // 
        boolean firstStep = true; // Переменная первого прохода формирования списка
        int sumArraySize = 0; // Переменная для определения длинны формирования массива (не верно)
        int biforeSumArraySize = 0;

        int columnEntry = 2;// как то правильно нужно прикрутить столбец совпадений
        ArrayList<String> notFindTables = new ArrayList(); // лист не найденных таблиц для запроса из фала

        ArrayList<Node> listNodeMethodExe = readXML.getHeirNode(n); // получить наследников с ними и будем работать
        ArrayList<String> nameDGODGI = new ArrayList<>();
        ArrayList<String> listNameOnOff = new ArrayList<>();

        ArrayList<ObjAnalize> listObjectDGODGI = new ArrayList<>(); // список объектов обработки

        // старт основного алгоритма  по две ноды анализ(может быть и больше но это не точно)
        for (int i = 0; i < listNodeMethodExe.size(); ++i) {
            Node nodeConEnd = listNodeMethodExe.get(i);
            String nameDGOorDGI = nodeConEnd.getNodeName();
            nameDGODGI.add(nodeConEnd.getNodeName());

            ObjAnalize anlizObjOI; // объект который содержит названия окончания и списки сишналов 
            // в зависимости от итерации ему будет присваиватся свойства кем он буде входом или выходом
            if (i % 2 == 0) {
                anlizObjOI = new ObjAnalize(nameDGOorDGI, true);
            } else {
                anlizObjOI = new ObjAnalize(nameDGOorDGI, false);
            }

            ArrayList<String> listEnding = new ArrayList<>(); // окончания в нодах
            for (Node nodeOnOff : readXML.getHeirNode(nodeConEnd)) { // по наследникам проходим считываем ноды окончаний
                String nNanmeEnd = nodeOnOff.getNodeName();
                listNameOnOff.add("_" + nNanmeEnd); // имена первых нод названия иструментов(уникальность названия стобцов для базы)

                HashMap<String, String> dataOnOff = readXML.getDataNode(nodeOnOff); // только одно пока данное
                for (Map.Entry<String, String> item : dataOnOff.entrySet()) {
                    listEnding.add(item.getValue());
                }
            }
            anlizObjOI.setEnding(listEnding); // присвоить сформированные окончания
            listObjectDGODGI.add(anlizObjOI);

            // (Доп условие)если у ноды есть аттрибут alt= То нужно пройтись по еще одной таблице
            HashMap<String, String> dataNodeALT = readXML.getDataNode(nodeConEnd); // забираем данные и ищем alt
            {
                String tableALT = dataNodeALT.get("alt"); // забираем название нужной таблицы если оно есть
                if (tableALT != null) { // есть дополнительная таблица
                    ObjAnalize anlizObjOIAlt;
                    if (i % 2 == 0) { // опять же желаю так так как нужно имлиментировать глубокое копирование(а оно надо?)
                        anlizObjOIAlt = new ObjAnalize(tableALT, true);
                    } else {
                        anlizObjOIAlt = new ObjAnalize(tableALT, false);
                    }
                    anlizObjOIAlt.setEnding(listEnding);  // добавляем те же окончания
                    listObjectDGODGI.add(anlizObjOIAlt);  // и добавляем в общий лист
                }
            }
        }

        // получаем исходные данные для нод механизмов из базы
        listObjectDGODGI.forEach(mechDODI -> {
            String nameTreq = globVar.abonent + "_" + mechDODI.getName(); // Формируется название таблицы
            List<String> columnsB = workbase.selectColumns(nameTreq); // возмем Названия колонок из таблицы

            for (int ib = 0; ib < nameColumnList.length; ++ib) { // Формируем список столбцов к базе (мин.2)
                String s = nameColumnList[ib];
                String nameAlgColumn = compareData(columnsB, s); // передаем лист и строку на анализ соответствия(есть ли подобные столбцы). первый попавшийся возвращаем.
                if (nameAlgColumn != null) {
                    listColumnSelect.add(nameAlgColumn); // Добавляем колонки для передачи в базу для запроса SELECT 
                }
            }

            if (listColumnSelect.size() > 1) { //если есть что то для запроса к базе по столбцам
                ArrayList<String[]> dataFromBase = workbase.getData(nameTreq, listColumnSelect); // получили массивы листов из базы 
                mechDODI.setDataList(dataFromBase);
                //listTagName.add(dataFromBase); // Кладем структуру в Лист

            } else {
                notFindTables.add(nameTreq); // Нечего не нашли добавляем  название таблицы в массив для проблемных
            }

        });
            //System.out.println("This formating start data"); // системный вывод

        // анализ исходных данных
        // вычисление кто DGO
        ObjAnalize objDGO = null;
        for (int i = 0; i < listObjectDGODGI.size(); ++i) {
            ObjAnalize o = listObjectDGODGI.get(i);
            if (o.getThisTypeDGO()) {
                objDGO = o;
                listObjectDGODGI.remove(i); // удалим из списка
                break;
            }
        }
        if (objDGO != null) {
            // проходим и формируем DGO
            ArrayList<String[]> listDO = objDGO.getDataSig();
            int elMass = 0; // первый элемент Листа
            int numElTagName = 0;// номер элемента с tag_name массива для сравнения (надо определять автоматом)
            int numElRusName = 1;//
            ArrayList<String> listOnOffDGO = objDGO.getEnding(); // получаем окончания
            // формируем  названия колонки DGO окончаний
            for (String sE : listOnOffDGO) {
                columnT.add(objDGO.getName() + "_" + sE);
            }
            // дописываем  названия колонки DGi окончаний
            for (ObjAnalize obgDGI : listObjectDGODGI) {
                for (String sE : obgDGI.getEnding()) {
                    columnT.add(obgDGI.getName() + "_" + sE);
                }
            }

            while (listDO.size() > 0) {  // пробегаем по листу но тут только 1 так как Tag_name 
                ArrayList<String> findTmp = new ArrayList(); // Временный 
                String[] s = listDO.get(elMass); // Первый элемент DO 

                // добавить русское название в столбец с обрезаниеме
                if (s.length > 1) { // для подстраховки если не нашли имена столбцов
                    String commonSliceRusEnd = null; //
                    
                    String regex = "(";
                    for (int endR=0; endR < endRusName.length; ++endR){ // проходим по ненужным окончаниям и формируем патерн
                        String endRus = endRusName[endR];
                        if (endR == endRusName.length - 1){
                            regex += endRus + ")";
                        } else regex += endRus + "|";
                    }
                    // эта Pattern.CASE_INSENSITIVE не работает с русским языком обязательно Pattern.UNICODE_CASE
                    commonSliceRusEnd = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(s[numElRusName]).replaceAll(""); // убираем все что есть в строке не нужного
                    //commonSliceRusEnd = Pattern.compile(regex, Pattern.CASE_INSENSITIVE). s[numElRusName], endRus);
                    
                    if (commonSliceRusEnd != null) {
                        boolean missWord = true;
                        while(missWord){
                            boolean cleanEndRus = false;
                            if(commonSliceRusEnd != null && commonSliceRusEnd.length() > 0){
                                // обрубаем пробелы с обоих концов
                                commonSliceRusEnd = commonSliceRusEnd.trim();
                                for(String sRem: simbolRemove){
                                    // перебираем ненужные последние символы
                                    if (commonSliceRusEnd.substring(commonSliceRusEnd.length() - 1).equals(sRem)){
                                      commonSliceRusEnd =  commonSliceRusEnd.substring(0, commonSliceRusEnd.length() - 1); // без последнего символа
                                      cleanEndRus = false;
                                      break;
                                    } else cleanEndRus = true;
                                }
                                // Если на всем переборе не найдено нечего то пропускаем это названия сигнала
                                if(cleanEndRus){
                                    missWord = false;
                                }
                            
                            } else commonSliceRusEnd = "errorNamedRusName"; //   но эта проверка может не понадобиться
                            System.out.println("while work");
                        }
                        findTmp.add(commonSliceRusEnd);
                        
                    } else findTmp.add(s[numElRusName]);
                }
                
                
                int enterEndDO = 0; // триггер вхождения в поиск окончания DO

                // сигнал DO но по всем окончаниям on Off
                String commonSliceSig = null; // общая часть DO перед окончанием
                for (String endDGO : listOnOffDGO) {
                    commonSliceSig = comparSignalEnd(s[numElTagName], endDGO);
                    if (commonSliceSig != null) {
                        //++enterEndDO;
                        //System.out.println(commonSliceSig);
                        break;
                    }
                }

                if (commonSliceSig != null) {
                    findTmp.add(commonSliceSig); // Добавляем переменную без окончания
                } else {
                    findTmp.add(s[numElTagName]); // Добавляем просто переменную если нет для нее патерна
                }
                findTmp.add("false"); // данные для колонки columnTueFalse

                boolean findDOCompareOnce = false; // триггер нахождения окончания DGO
                // прежде чем удалить из DO надо прогнать по всем окончаниям
                for (String endDGO : listOnOffDGO) { // прогоняем по окончаниям DGO 
                    int jLoc = elMass;
                    boolean findDOCompare = false;
                    while (jLoc < listDO.size()) { // прогоняем по самому себе DO
                        String[] locSeco = listDO.get(jLoc); // следующие элементы
                        String strSig = null;
                        strSig = checkSliceSignal(commonSliceSig, locSeco[numElTagName], endDGO);
                        if (strSig != null) {
                            findTmp.add(locSeco[numElTagName]); //если нашлми что там то добавляем
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
                // тут по списку оставшихся OBJ элементов(максимум пока 2)
                for (ObjAnalize obgDGI : listObjectDGODGI) {
                    ArrayList<String[]> listDI = obgDGI.getDataSig();

                    ArrayList<String> listOnOffDGI = obgDGI.getEnding();

                    for (String endDGI : listOnOffDGI) { // прогоняем по окончаниям DGI
                        boolean findDICompare = false; // триггер нахождения окончания DGI
                        int j = 0;

                        while (j < listDI.size()) { // Проходим по листу DI
                            String[] sj = listDI.get(j); // это элемент который 
                            String retFinStr = null;
                            retFinStr = checkSliceSignal(commonSliceSig, sj[numElTagName], endDGI); // вызываем функцию сравнения(патерн на основе 1 строки)
                            //if (retFinStr != null) break;                                 
                            if (retFinStr != null) {
                                findTmp.add(sj[numElTagName]); //если нашли,добавляем
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
                }
                //System.out.println("Sig "+ commonSliceSig+" in DO_List " 
                //        + " " + Integer.toHexString(enterEndDO) +" in DI_List "+ enterEndDI);
                String entry = "ИМ: " + Integer.toHexString(enterEndDO) + "x" + Integer.toHexString(enterEndDI);
                findTmp.add(columnEntry, entry);  // данные о совпадении

                if (missWE & enterEndDO == 0 & enterEndDI == 0) {
                } // Пропуск сигнала если нечего не нашли и есть триггер
                else {
                    findingTagname.add(findTmp);
                }

            }

            // 
        }
        //System.out.println(objDGO.getName()); // Системный вывод

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
        for (int iArr = 0; iArr < findingTagname.size(); ++iArr) {
            int nextItem = iArr + 1;
            if (nextItem >= findingTagname.size()) {
                System.out.println("Last value");
                break;
            } else {
                for (int iArrSec = nextItem; iArrSec < findingTagname.size(); ++iArrSec) {
                    ArrayList<String> arr = findingTagname.get(iArr);
                    int summArr = 0;
                    for (String s : arr) { // вычисляем сколько элементов в листе кроме пустых строк
                        if (s.equals("")) {
                            continue;
                        } else {
                            ++summArr;
                        }
                    }
                    ArrayList<String> arrSecond = findingTagname.get(iArrSec);
                    int summArrSecond = 0;
                    for (String s : arrSecond) { // вычисляем сколько элементов во втором листе
                        if (s.equals("")) {
                            continue;
                        } else {
                            ++summArrSecond;
                        }
                    }
                    if (summArrSecond > summArr) {
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
        Pattern pattern0 = Pattern.compile(cutString); // Должен игнорировать регистр
        //System.out.println(pattern0.pattern() +" flag -> "+pattern0.flags());
        //Pattern pattern0 = Pattern.compile(Pattern.quote(cutString), Pattern.CASE_INSENSITIVE + Pattern.LITERAL); // Должен игнорировать регистр
        Matcher matcher0 = pattern0.matcher(str1);  // строка из входа входа на сравнение 
        if (matcher0.matches()) { // Окончание совпало(не срабатывает при CASE_INSENSITIVE)
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

    //  --- добавления данных в базу  из таблицы ---
    public void addDataToBase(ArrayList<String[]> updatetedData) {
        // прежде чем создать новую базу нужно прочитать имеющуюся и взять все сигналы у который есть true
        // только потом затереть
        ArrayList<String[]> dataFromDBTrue = new ArrayList<>(); // массив с выборкой true
        for (String table : workbase.getListTable()) { // есть ли вообще таблица в базе
            if (nameTable.equals(table)) {
                ArrayList<String[]> allDataExecTable = workbase.getData(nameTable);
                for (String[] arr : allDataExecTable) {
                    for (String s : arr) {
                        if (s.equals("true")) {
                            dataFromDBTrue.add(arr);
                            break;
                        }
                    }
                }
                break;
            }
        }

        int elemCompare = 1; // номер элемента в массиве который сравниваем
        for (int i = 0; i < updatetedData.size(); ++i) {
            String[] arr = updatetedData.get(i);
            String tagNameTable = arr[elemCompare]; // так как из базы еще id и UUID
            for (String[] arrTrue : dataFromDBTrue) {
                //String[] arr = updatetedData.get(i);
                String tagNameTrue = arrTrue[elemCompare + 2]; // тут совсем другие значения
                if (tagNameTable.equals(tagNameTrue)) {
                    // вставляем значение где не было true из базы вырезав нужные данные без id и uuid
                    updatetedData.set(i, Arrays.copyOfRange(arrTrue, 2, arrTrue.length));
                }
            }
        }

        //создадим таск бар в потоке(не работает)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaskBarFrame testBar = new TaskBarFrame();
                workbase.addObserver(testBar); // добавить слушателя для базы
            }
        });

        // данные для передачи наблюдателю
        int min = 0;
        int max = updatetedData.size();
        workbase.createTable(nameTable, columnT);

        for (int i = 0; i < updatetedData.size(); ++i) {
            String[] array = updatetedData.get(i);
            workbase.insertRows(nameTable, array, columnT); // опять значения на оборот
            workbase.setValueObserver(min, max, i);
        }
    }

    // -- метод сращивания Листов для общей таблицы --
    public ArrayList<ArrayList> connectListMechan(ArrayList<ArrayList> first, ArrayList<ArrayList> second) {
        ArrayList<String> emptyData = new ArrayList<>(); // пустые строки будут для выравниявания матрицы
        // проверка длинны при добавлении недостающих данных
        // если второго лист длиннее первого()
        if (second.size() > first.size()) {
            int lenStrList = 0; // длинна строки-листа первого массива
            for (ArrayList<String> enterList : first) {
                if (enterList.size() > lenStrList) { // перестраховка так как они все должны быть одинаковы
                    lenStrList = enterList.size();
                }
            }
            // создаем недостающий отрезак данных для первого Листа 
            for (int i = 0; i < lenStrList; ++i) {
                emptyData.add(""); // просто пустая строка
            }
            // сращивание данных двух листов 
            for (int i = 0; i < second.size(); ++i) {
                int test = first.size();
                if (i <= first.size()-1) { // пока не достигли окончания первого листа соединяем
                    first.get(i).addAll(second.get(i)); // пристыковать один Лист к другому
                } else {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.addAll(emptyData); // прикручиваем пустое не достающее
                    tmp.addAll(second.get(i));
                    first.add(tmp); // и прикручиваем сформированный массимуже к первому Листу(может делать новый?)
                }
            }
            
        } else if (first.size() > second.size()) { // если первый больше по длине чем тот который прикручиваем(добавить в конец данные) 
            int lenStrList = 0; // длинна строки-листа уже для второго массива Листов
            for (ArrayList<String> enterList : second) {
                if (enterList.size() > lenStrList) { // перестраховка так как они все должны быть одинаковы
                    lenStrList = enterList.size();
                }
            }
            // создаем недостающий отрезак данных для второго Листа 
            for (int i = 0; i < lenStrList; ++i) {
                emptyData.add(""); // просто пустая строка
            }
            // сращивание данных двух листов 
            for (int i = 0; i < first.size(); ++i) {
                if (i <= second.size()) { // пока не достигли окончания лист соединяем
                    first.get(i).addAll(second.get(i)); // пристыковать один Лист к другому
                } else {
                    first.add(emptyData); //просто добавляем недостающие строки
                }
            }
        }
        return first; 

    }
}

// --- объект анализа ИМ ---
class ObjAnalize {

    private String name = null;
    private boolean exitOut; // выходной это параметр или входной
    ArrayList<String> ending = new ArrayList<>(); // лист окончаний
    ArrayList<String[]> listFromBase = new ArrayList<>(); // лист сигналов из базы  русское и PLC имя

    public ObjAnalize(String name, boolean exitOut) {
        this.name = name;
        this.exitOut = exitOut;
    }

    // -- добавить окончания --
    public void addEnding(String s) {
        ending.add(s);
    }

    // --  Установить окончания --
    public void setEnding(ArrayList<String> ending) {
        this.ending = ending;
    }

    // --  вернуть список окончаний --
    public ArrayList<String> getEnding() {
        return ending;
    }

    // -- установить данные из таблицы --
    public void setDataList(ArrayList<String[]> listFromBase) {
        this.listFromBase = listFromBase;
    }

    // получить что взяли из базы
    public ArrayList<String[]> getDataSig() {
        return listFromBase;
    }

    //получить русские имена
    public ArrayList<String> getListSigRus() {
        ArrayList<String> listRusSig = new ArrayList<>();
        for (String[] arr : listFromBase) {
            listRusSig.add(arr[0]);
        }
        return listRusSig;
    }

    // получить имена сигналов
    public ArrayList<String> getListSig() {
        ArrayList<String> lisSig = new ArrayList<>();
        for (String[] arr : listFromBase) {
            lisSig.add(arr[1]);
        }
        return lisSig;
    }

    // получить имя объекта 
    public String getName() {
        return name;
    }

    //вернуть тип объекта DGO или DGI
    public boolean getThisTypeDGO() {
        return exitOut;
    }

}
