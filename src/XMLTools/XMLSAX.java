package XMLTools;

import configurator.StructSelectData;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import DataBaseConnect.DataBase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import ReadWriteExcel.RWExcel;
import fileTools.FileManager;
import globalData.globVar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.sax.SAXSource;

public class XMLSAX {

    Document document = null; // Глобальный документ с которым работаем
    String patchWF = "";
    DataBase workbase = DataBase.getInstance();
    ReadBedXML fixXML; // объект реализации обхода неверно сформированного файл

    // --- прочитать документ и передать корневую ноду ---
    public Node readDocument(String patchF) {
        patchWF = patchF;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        //factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        List<String> lines; // Лист с прочитанным файлом
        boolean findErr = false; // если попали в ексепшен и дробим нашим парсером
        try {
            document = factory.newDocumentBuilder().parse(patchF);
        } catch (IOException ex) {
            System.out.println("Error read standart method XML, reding custom method " + Paths.get(patchF)); // выброс ошибок по файлу
            findErr = true;
        } catch (SAXException ex) {
            System.out.println("Over error, all ride step to two !!!!"); // вот тут уже вызываем метод удаления DOCTYPE и тд если будет
            findErr = true;
        } catch (ParserConfigurationException ex) {
            System.out.println("Standart parser down :(");
            findErr = true; // так же как выше пробуем убирать DOCTYPE
        }
        Node n = null; // это для корневой ноды но может и не надо
        if (findErr) { // Запускаем режим преобразования файлов только после Exception
            try {
                fixXML = new ReadBedXML(patchF);
                String documenWithoutDoctype = fixXML.methodRead(patchF); // Так читаем и получаем преобразованные данные,
                InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
                document = factory.newDocumentBuilder().parse(stream);
                n = document.getDocumentElement(); // Получаем корневой элемент

            } catch (SAXException | InterruptedException | TransformerFactoryConfigurationError | IOException | ParserConfigurationException ex) {
                System.out.println("Not file XML or bed remove data in file" + patchF + " !");
            }

        } else {
            n = document.getDocumentElement(); // Получаем корневой элемент
        }
        return n;
    }

    // --- Создание элемента Node  ---
    public Node createNode(String nameElement) {
        if (document != null) {// если документ зарегистрирован или внесен то
            return document.createElement(nameElement);
        } else {
            FileManager.loggerConstructor(nameElement + "not created but XML document null!");
            return null;
        }
    }

    // --- Вставка и сождании новой ноды с параметрами ---
    public Node insertChildNode(Node parent, String[] arg) {
        // arg[0] Имя ноды которую вставляем, arg[1]-arg[2] ключ значение и так далее  
        Node createN = createNode(arg[0]);
        String attr = null;
        String value = null;
        for (int i = 1; i < arg.length; ++i) {
            if (i % 2 == 0) {
                value = arg[i];
                setDataAttr(createN, attr, value);
            } else {
                attr = arg[i];
            }
        }
        parent.appendChild(createN);
        return createN;
    }

    // --- Создание Документа  с root нодой---
    public Node createDocument(String nameElement) {
        Node root = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            document = factory.newDocumentBuilder().newDocument();
            root = document.createElement(nameElement);
            document.appendChild(root);
        } catch (ParserConfigurationException ex) {
            FileManager.loggerConstructor(nameElement + "not created but XML document null!");
        }
        return root;
    }

    // --- Внести данные в ноду списком HashMap---
    public void insertDataNode(Object o, HashMap<String, String> map) {
        //System.out.println(o.getClass().getName());
        Element editElem = null;
        if (o instanceof Node) {
            editElem = (Element) o;
        }
        if (o instanceof Element) {
            editElem = (Element) o;
        }
        for (Map.Entry<String, String> item : map.entrySet()) {
            String key = item.getKey();
            String value = item.getValue();
            editElem.setAttribute(key, value);
        }

    }

    // --- Внести данные в ноду ключ-значение ---
    public void setDataAttr(Object o, String attr, String value) {
        //System.out.println(o.getClass().getName());
        Element editElem = null;
        if (o instanceof Node) {
            editElem = (Element) o;
        }
        if (o instanceof Element) {
            editElem = (Element) o;
        }
        editElem.setAttribute(attr, value);
    }

    // --- инициализировать документ с путем для его сохранения (если стандартными способами создали)---
    public void docInstance(Document document, String patchWF) {
        this.patchWF = patchWF;
        this.document = document;
    }

    // --- пробегаме по ноды рекурсией ---
    public static void stepThroughAll(Node start) {
        System.out.println(start.getNodeName() + " = " + start.getNodeValue());
        if (start.getNodeType() == start.ELEMENT_NODE) {
            NamedNodeMap startAttr = start.getAttributes();
            for (int i = 0; i < startAttr.getLength(); i++) {
                Node attr = startAttr.item(i);//node str=strAttr.item(i);
                System.out.println(" Attribute: " + attr.getNodeName()
                        + " = " + attr.getNodeValue());
            }
        }
        for (Node child = start.getFirstChild();
                child != null;
                child = child.getNextSibling()) {
            stepThroughAll(child);
        }
    }

    // --- получаем данные c ноды в виде ключ значение ---
    public HashMap getDataNode(Node n) {
        HashMap<String, String> findData = null;
        if (n != null) {
            //System.out.println("NodeName" + n.getNodeName() + " NameType" + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                findData = new HashMap<>();// тут инициализируем Мап
                NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента
                for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды
                    Node attr = startAttr.item(i);
                    String attribute = attr.getNodeName(); // Название атрибута
                    String value = attr.getNodeValue();
                    findData.put(attribute, value);
                }
                return findData;
            }

            if (findData == null) { // если не нашли
                for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                    findData = getDataNode(child);
                    if (findData != null) {
                        break;
                    }
                }

            }
        }
        return findData;
    }

    // --- получаем всех наследников ноды именно нод список(стандартно возвразает все подряд) ---
    public ArrayList<Node> getHeirNode(Node n) {
        if(n==null)
            return null;
        ArrayList<Node> kindNode = new ArrayList<>();

        NodeList child = n.getChildNodes();
        for (int i = 0; i < child.getLength(); i++) {
            Node node = child.item(i);
            //System.out.println("NodeName " + node.getNodeName() + " NameType" + node.getNodeType());
            if (node.getNodeType() == 1) { //  Так это сама нода
                kindNode.add(node);
            }
        }
        return kindNode;
    }

    // --- Запипись в файл структурой XML ---
    public void writeDocument() {
        try {
            // удаление пустых строк
            XPath xp = XPathFactory.newInstance().newXPath();
            try {
                NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", document, XPathConstants.NODESET);
                for (int i=0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                node.getParentNode().removeChild(node);
            }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            File file = new File(patchWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //System.out.println(document.getNodeName());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // без этого в одну строку все запишет
            //transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file)); // наш документ в начале
            
//            XMLReader reader = XMLReaderFactory.createXMLReader();
//            TransformerFactory tf = TransformerFactory.newInstance();
//            // Load the transformer definition from the file strip.xsl:
//            Transformer t = tf.newTransformer(new SAXSource(reader, new InputSource(new FileInputStream("strip.xsl"))));
//            // Transform the file test.xml to stdout:
//            t.transform(new SAXSource(reader, new InputSource(new FileInputStream("test.xml"))), new StreamResult(System.out));
            
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
        // тут возвращаем данные которые удаляли
        if (fixXML != null) {
            try {
                // если был вызван парсер удаления возвращамем что удалил
                fixXML.returnToFileDtd(patchWF);//и возвращаем ему удаленный DOCTYPE
            } catch (InterruptedException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // --- Запипись в файл структурой XML с указанием именем файла ---
    public void writeDocument(String patchWF) {
        try {
            // удаление пустых строк
            XPath xp = XPathFactory.newInstance().newXPath();
            try {
                NodeList nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", document, XPathConstants.NODESET);
                for (int i=0; i < nl.getLength(); ++i) {
                Node node = nl.item(i);
                node.getParentNode().removeChild(node);
            }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
            File file = new File(patchWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // без этого в одну строку все запишет
            transformer.transform(new DOMSource(document), new StreamResult(file));
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
        // тут возвращаем данные которые удаляли
        if (fixXML != null) {
            try {
                // если был вызван парсер удаления возвращамем что удалил
                fixXML.returnToFileDtd(patchWF);//и возвращаем ему удаленный DOCTYPE
            } catch (InterruptedException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // --- Метод чтения и подключение к базе посредством конфига ---
    public int setConnectBaseConfig(String patchFile) {
        File f = new File(patchFile);
        String pass = null;
        String user = null;
        String url = null;
        String base = null;
        String DesignDir = null;
        if (f.exists()) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                org.w3c.dom.Document doc = db.parse(f);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("config");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (Node.ELEMENT_NODE == node.getNodeType()) {
                        Element element = (org.w3c.dom.Element) node;
                        pass = element.getElementsByTagName("PASS").item(0).getTextContent();
                        user = element.getElementsByTagName("USER").item(0).getTextContent();
                        url = element.getElementsByTagName("URL").item(0).getTextContent();
                        base = element.getElementsByTagName("BASE").item(0).getTextContent();
                        DesignDir = element.getElementsByTagName("DesignDir").item(0).getTextContent();
                    }
                    globVar.desDir = DesignDir; // добавить переменную пути проекта
                    return DataBase.getInstance().connectionToBase(url, base, user, pass); // Вызов запроса к базе подключения
                }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                errorExecuter("Проверьте существование или структуру " + patchFile);
                return -1;
            }
        }
        return -1;
    }

    // --- Удалить ноду ---
    public void removeNode(Node n) {
        Node parentN = n.getParentNode();
        //System.out.println("What delete " + n.getNodeName());
        //System.out.println("NameParent " + parentN.getNodeName());
       parentN.removeChild(n);
    }

    // --- Найти первую ноду по имени и вернуть ее нижний.верхний регистр игнорирую ---
    public Node returnFirstFinedNode(Node n, String s) {
        Node finding = null;
        if (n != null) {
            System.out.println("NodeName " + n.getNodeName() + " TypeNode " + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                if (n.getNodeName().equalsIgnoreCase(s)) {
                    System.out.println("Find Node " + n.getNodeName());
                    finding = n;
                    return finding;
                }
            }
            if (finding == null) { // если не нашли
                for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                    finding = returnFirstFinedNode(child, s);
                    if (finding != null) {
                        break;
                    }
                }

            }
        }
        return finding;
    }

    // --- Найти ноду по имени и ее значениям ---
    public Node findNodeValue(Node n, String[] arg) {
        String nameFindN = arg[0];// arg первое значение всегда Название ноды
        Node finding = null;
        if (n != null) {
            //System.out.println("NodeName " + n.getNodeName() + " NameType" + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                boolean access = false; // разрешение на нужную ноду
                if (n.getNodeName().equals(nameFindN)) { // нашли нужное имя ноды
                    NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента
                    boolean compared = false;
                    for (int elemArh = 1; elemArh < arg.length; ++elemArh) { // Пробегаем по входящему массиву с 1 элемена так как 0 имя Ноды
                        String el = arg[elemArh]; // значение элемента которое проверяем
                        for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды
                            Node attr = startAttr.item(i);
                            //String Attribute = attr.getNodeName(); // Название атрибута
                            String Value = attr.getNodeValue(); // значение атрибута
                            //System.out.println("Attribute:" + Attribute + " Value:" + Value);
                            if (el.equals(Value)) {
                                compared = true;
                                break; // элемент нашли и не перебираем дальше значения
                            }
                        }
                        if (compared == true) {// после прохода элемента нечего не нашли то ломаем цикл
                            //System.out.println("Find_Value " + el);
                            access = true;
                            compared = false;
                        } else {
                            access = false;
                        }
                    }
                    if (access) {
                        System.out.println("Find Node " + n.getNodeName());
                        finding = n;
                        return finding;
                    }
                }
            }
            if (finding == null) { // если не нашли
                for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                    finding = findNodeValue(child, arg);
                    if (finding != null) {
                        break;
                    }
                }

            }
        }
        return finding;
    }

    // --- получить данные по аттрибуту ноды--- 
    public String getDataAttr(Node n, String s) {
        NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента 
        for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды 
            Node attr = startAttr.item(i);
            if (attr.getNodeName().equals(s)) { // Название атрибута 
                return attr.getNodeValue();
            }
        }
        return null;
    }

    // --- Найти ноду по имени и ее атрибутам ---
    public Node findNodeAtribute(Node n, String[] arg) {
        String nameFindN = arg[0];// arg первое значение всегда Название ноды
        Node finding = null;
        if (n != null) {
            //System.out.println("NodeName " + n.getNodeName() + " NameType" + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                boolean access = true; // разрешение на нужную ноду
                if (n.getNodeName().equals(nameFindN)) { // нашли нужное имя ноды
                    NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента
                    if(arg.length == 1) return n;               // если аргумент был один - искали толькор имя ноды и нашли его
                    boolean compared = false;
                    for (int elemArh = 1; elemArh < arg.length; elemArh++) { // Пробегаем по входящему массиву с 1 элемена так как 0 имя Ноды
                        String key = arg[elemArh]; // значение элемента которое проверяем
                        String val = null;
                        if(elemArh < arg.length- 1) val = arg[++elemArh]; //если есть ещё аргументы, значит это пара "ключ-значение"
                        compared = false;
                        for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды
                            Node attr = startAttr.item(i);
                            String Attribute = attr.getNodeName(); // Название атрибута
                            String Value = attr.getNodeValue(); // значение атрибута
                            //System.out.println("Attribute:" + Attribute + " Value:" + Value);
                            if (key.equals(Attribute) && (val == null || val.equals(Value))) {
                                compared = true;
                                break; // элемент нашли и не перебираем дальше значения
                            }
                        }
                        if (!compared) {// после прохода элемента нечего не нашли то ломаем цикл
                            access = false;
                            break;
                        }
                    }
                    if (access) {   //Если access остался true, значит все пары "ключ-значение" найдены
                        return n;   // и значит мы нашли искомую ноду
                    }
                }
            }
        //if (finding == null) { // если выше не было ретурна, значит нода не найдена. Продолжаем искать среди потомков
            for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                finding = findNodeAtribute(child, arg);
                if (finding != null) return finding;
            }
        //}
        }
        return finding;
    }

    // --- сформировать даные из конфигугации XML для чтения Exel---
    public void ReadExelFromConfig(Node n, String pathExel) {  // pathExel Временно так как мозгов не хватило ночью.                
        RWExcel readExel = new RWExcel(pathExel);
        ArrayList<String> it_list_sheet = readExel.get_list_sheet(pathExel); //забираем список листов в файле и строим итератор из них
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        ArrayList<ArrayList> dataconfig = new ArrayList<>();
        NodeList nodesExcel = null;

        String nameTB = ""; // имя таблицы для базы
        String nameSheetExel = ""; // название листа Exel
        ArrayList<String> columnExcel = new ArrayList<>(); // Колонки из Excell
        ArrayList<String> columnBase = new ArrayList<>(); //названия таблиц для Базы
        ArrayList<String> columnExcelIgnore = new ArrayList<>(); // Колонки из Excell которые пропускаем данные где нет default
        String defaultValue = null; //  элемент аттрибут ноды по которому срабатывает триггер построения
        HashMap<String, String> mapDefault = new HashMap<>();
        boolean defAttrF = false; // триггер добавить сигнал или нет
        boolean createT = false; // триггер строить таблицу или нет

        NodeList signalList = n.getChildNodes();
        if (n != null) { // если не пустая нода
            for (int i = 0; i < signalList.getLength(); i++) {
                Node firsNode = signalList.item(i);
                if (firsNode.getNodeType() == firsNode.ELEMENT_NODE) {  // почему не это
                    nameTB = globVar.abonent + "_" + firsNode.getNodeName(); // Корневая нода это название таблицы
                    System.out.println("NameTableBase " + nameTB);
                    NamedNodeMap atrsig = firsNode.getAttributes();
                    for (int atr = 0; atr < atrsig.getLength(); atr++) { // пробегаем по атрибутам
                        Node sigSheet = atrsig.item(atr);
                        if (sigSheet.getNodeName().equalsIgnoreCase("excelSheetName")) { // проверка что идентификатор атрибута excelSheetName
                            System.out.println("NameExelSheet " + sigSheet.getNodeValue());
                            nameSheetExel = sigSheet.getNodeValue();
                        }
                    }
                    try {
                        XPathExpression xPathExpression = xpath.compile("EXEL"); // путь до 
                        nodesExcel = (NodeList) xPathExpression.evaluate(firsNode, XPathConstants.NODESET);
                    } catch (XPathExpressionException ex) {
                        Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (int j = 0; j < nodesExcel.getLength(); j++) { // перборка Exel ноды хоть она и одна
                        Node element = nodesExcel.item(j);
                        //stepThroughAll(element);
                        for (Node exelN : getHeirNode(element)) {
                            //System.out.println("ColumnExel " + exelN.getNodeName()); 
                            String NameColumnExcel = exelN.getNodeName(); // имя колонки Листа в Excell
                            columnExcel.add(NameColumnExcel); // добавить колонки листа Excell

                            HashMap<String, String> valueEtrColumnExcelN = getDataNode(exelN); // Получить аттрибуты и значения нод
                            String valueColumnPos = valueEtrColumnExcelN.get("nameColumnPos");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                            if (valueColumnPos != null) {//проверка что этот атрибут nameColumnPos
                                //System.out.println("NameColumnToBase: " + Value);
                                columnBase.add(valueColumnPos); // колонки для базы
                            }
                        }
                        workbase.createTable(nameTB, columnBase); // Создание таблицы
                        ArrayList<String[]> sheet_fromsheet_from = new ArrayList<>(readExel.getDataCell(nameSheetExel, columnExcel)); // получить данные с страницы

                        // проход по данным с анализом диаграммы Льва 
                        int tmp = 0; // сюда должна записать переменная начала обработки строк
                        for (String[] araySheet : sheet_fromsheet_from) {
                            for (int si = 1; si < araySheet.length; ++si) { // 1 так как данные с uuid
                                String s = araySheet[si];
                                Node nodeExcel = getHeirNode(element).get(si - 1); // -1 так как идентификатор без UIddберем ноду с таким же идентификатором столбца

                                HashMap<String, String> valueEtrColumnExcelN = getDataNode(nodeExcel); // Получение имена и атрибутов этой ноды
                                if (s.equals("")) { // если данные в ячейки нет(средняя ветка)
                                    // *** ищем switch ***
                                    String valSwitch = valueEtrColumnExcelN.get("switch");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                    if (valSwitch != null) {//проверка данные valSwitch есть
                                        // название столбца от куда мы счтываем данные
                                        for (int colB = 0; colB < columnBase.size(); ++colB) {
                                            String nameColumnBase = columnBase.get(colB);
                                            if (valSwitch.equals(nameColumnBase)) { // совпало какое то имя берем его номер
                                                araySheet[si] = araySheet[colB]; // присваиваем это значение но номеру массива   
                                            }
                                        }
                                        boolean findVar = false; // триггер нашли хоть один var
                                        for (int rep = 0; rep < valueEtrColumnExcelN.size(); ++rep) { // по всем элементам прохожусь
                                            String var = "var" + rep;
                                            String valueVar = valueEtrColumnExcelN.get(var);
                                            // сравниваем значение
                                            if (araySheet[si].equals(valueVar)) {  // если все совпало то вносим с правилами ниже
                                                araySheet[si] = valueEtrColumnExcelN.get("default" + rep);
                                                findVar = true;
                                            }
                                        }
                                        if (!findVar) { // если не одно значение не совпалос default1- до безконечности
                                            String defaultVar = valueEtrColumnExcelN.get("default"); // записываем значения default
                                            if (defaultVar != null) {
                                                araySheet[si] = defaultVar;
                                            } else {
                                                araySheet[si] = ""; // записываем пустую строку
                                                FileManager.loggerConstructor("switch error: List excell " + nameTB + "Column " + nodeExcel.getNodeName() + "str= " + tmp);

                                            }
                                        }
                                    } else {
                                        // ищем default
                                        String valDefault = valueEtrColumnExcelN.get("default");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                        if (valDefault != null) {//проверка данные default1 есть
                                            araySheet[si] = valDefault; // нашли дефаулт то в него и пешем 
                                        } else {
                                            // ищем formula
                                            String valFormula = valueEtrColumnExcelN.get("valFormula");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                            if (valFormula != null) {//проверка данные valFormula есть
                                                araySheet[si] = calcFormula(); // вызываем метод обработки формулы
                                            } else {
                                                araySheet[si] = ""; // записываем пустую строку
                                                FileManager.loggerConstructor("formula error: List excell " + nameTB + "Column " + nodeExcel.getNodeName() + "str= " + tmp);
                                            }
                                        }
                                    }

                                } else {
                                    // ищем registr
                                    String valuRegistr = valueEtrColumnExcelN.get("registr");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                    if (valuRegistr != null) {//проверка что registr данные есть
                                        if (valuRegistr.equals("down")) {
                                            araySheet[si] = araySheet[si].toLowerCase(); // значение в нижний регистр
                                        } else {
                                            araySheet[si] = araySheet[si].toUpperCase(); // тогда к верхнему
                                        }
                                    }
                                    // ищем replace
                                    String valueReplace = valueEtrColumnExcelN.get("replace");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                    if (valueReplace != null) {
                                        for (int rep = 0; rep < valueEtrColumnExcelN.size(); ++rep) { // по всем элементам прохожусь
                                            String repFrom = "repFrom" + rep;
                                            String valueRepFrom = valueEtrColumnExcelN.get(repFrom);
                                            // сравниваем значение
                                            if (araySheet[si].equals(valueRepFrom)) {
                                                araySheet[si] = valueEtrColumnExcelN.get("repTo" + rep);
                                            }
                                        }
                                    }
                                }
                                // второй этап атрибут add1
                                String valueAdd1 = valueEtrColumnExcelN.get("add1");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                if (valueAdd1 != null) { // нашли add1
                                    boolean findAdd = false;
                                    for (int rep = 0; rep < valueEtrColumnExcelN.size(); ++rep) { // по всем элементам прохожусь
                                        String repFrom = "add" + rep;
                                        String valueAdd = valueEtrColumnExcelN.get(repFrom);
                                        if (valueAdd != null) { // если такой add вообще есть
                                            for (int colB = 0; colB < columnBase.size(); ++colB) { // название столбца от куда мы счтываем данные
                                                String nameColumnBase = columnBase.get(colB);
                                                if (valueAdd.equals(nameColumnBase)) { // совпало какое то имя берем его номер
                                                    araySheet[si] = araySheet[si] + araySheet[colB]; // добавляем содежимое найденного
                                                    findAdd = true;
                                                }
                                            }
                                            if (!findAdd) { // если не нашли содержимое столбца add 
                                                araySheet[si] = araySheet[si] + valueAdd;
                                            }
                                        }

                                    }
                                } else {
                                    // *** поиск атрибут fRez ***
                                    String valueFRez = valueEtrColumnExcelN.get("fRez");// можно так не знаю  valueEtrColumnExcelN.containsKey("")
                                    if (valueFRez != null) {
                                       ArrayList<Node> ListNFormulas = getHeirNode(returnFirstFinedNode(firsNode, "Formulas"));// ищем ноду Formulas
                                       String nColumnCurrentT = columnBase.get(si);// Получить название текущего столбца для таблицы
                                       for(Node nFormulas: ListNFormulas){
                                           if(nColumnCurrentT.equals(nFormulas.getNodeName())){ // Совпадает ли названия нод Formulas с названием текущего столбца
                                            System.out.println(nFormulas.getNodeName());
                                            setDataAttr(nFormulas, "result", araySheet[si]);// добавить аттрибут этой ноде result
                                           }
                                       }
                                    }
                                }
                            }
                            ++tmp;
                        }
                        
                        //writeDocument(); // запишем документ так как были преобразования
                        for (String[] massS : sheet_fromsheet_from) {
                            workbase.insertRows(nameTB, massS, columnBase); //Вносим данные в базу
                        }
                        // все обнуляем для следующего сигнала
                        nameTB = "";
                        nameSheetExel = "";
                        columnExcel.clear();
                        columnBase.clear();
                        defAttrF = false;
                        createT = false;
                    }
                }
            }
        }
    }

    // --- Метод для обработки формул в построение таблицы из XML ---
    String calcFormula() {
        return "i am from Formula!";
    }

    // --- обработчик ошибок показывает что было не так сделанно(можно выводить логи в фал) --
    void errorExecuter(String s) {
        JOptionPane.showMessageDialog(null, "Сообщения о ошибке " + s);
    }

//     --- Тестовый вызов метода создания документа нод и прочего ---     
    public static void main(String[] arg) {
        HashMap<String, String> map = new HashMap<>();
        XMLSAX test = new XMLSAX();
        Node n = test.readDocument("test666.xml");
        String[] massD = {"Name66", "attr1", "val1", "attr2", "val2", "attr2", "val2"};
        test.insertChildNode(n, massD);
//        test.writeDocument();
//        HashMap<String,String> dataN = new HashMap<>();
//        dataN.put("attr1", "value1");
//        dataN.put("attr2", "value2");
//        dataN.put("attr3", "value3");
//        test.insertDataNode(rootN, dataN);
//        Node childN = test.createNode("child");
//        dataN.put("child", "value3");
//        test.insertDataNode(childN, dataN);
//        rootN.appendChild(childN);
//        Node child2N = test.createNode("child2");
//        test.insertDataNode(child2N, dataN);
//        childN.appendChild(child2N);
//        test.writeDocument("test666.xml");
//        
//        Node n = test.readDocument("test666.xml");
//        Node n = test.readDocument("/home/ad/NetBeansProjects/Type_Mode.type");
//        String[] value = {"F","TAG_NAME_PLC", "VarName"};// даже если параметром меньше
//        String[] value = {"F", "VarName1"}; // расскоментируй меня и запусти
//        String[] attr = {"G","nameColumnPos", "type"};
//        String[] attr = {"G","nameColumnPos"};
//        Node fNValue = test.findNodeValue(n, value); // поиск по ноде и значениям
////        Node fNAttr = test.findNodeAtribute(n, attr); // поиск по ноде и атрибутам
//        Node fNodName = test.returnFirstFinedNode(n, "Name665"); // поиск по названию ноды
//        test.removeNode(fNodName);
//        test.writeDocument();
//        Node fNodName = test.returnFirstFinedNode(n, "Field"); // поиск по названию ноды
//        HashMap<String,String> mapDataN = test.getDataNode(fNodName); // получаем с этой ноды данные
//        try{
//            System.out.println(fNValue.getNodeName());
//            System.out.println(fNAttr.getNodeName());
//            System.out.println(fNodName.getNodeName() + " Size_data " + mapDataN.size());
//        } catch (NullPointerException ex) {
//            test.errorExecuter("Node Null what is not find \n" + ex);
//        }
    }

}
