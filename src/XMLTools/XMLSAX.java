package XMLTools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.JOptionPane;
import Tools.FileManager;
import globalData.globVar;
//import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class XMLSAX {

    Document document = null; // Глобальный документ с которым работаем
    String pathWF = "";
    ReadBedXML fixXML = null; // объект реализации обхода неверно сформированного файл
    Node root = null;
    ArrayList<Node> AllFindingNode = new ArrayList<>(); // все найденные ноды по имени
    
    
    public Node importNode(Node n){
        return document.importNode(n, true);
    }
            
    public void clear(){
        document = null;
        pathWF = "";
        fixXML = null;
        root = null;
    }
    // --- прочитать документ и передать корневую ноду ---
    public Node readDocument(String patchF) {
        pathWF = patchF;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        try {
            fixXML = new ReadBedXML(patchF);
            String documenWithoutDoctype = fixXML.methodRead(patchF); // Так читаем и получаем преобразованные данные,
            InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
            document = factory.newDocumentBuilder().parse(stream);
            root = document.getDocumentElement(); // Получаем корневой элемент

        } catch (SAXException | InterruptedException | TransformerFactoryConfigurationError | IOException | ParserConfigurationException ex) {
            System.out.println(patchF + " - это не XML или ошибки в нём критические");
        }
        return root;
    }
//    public Node readDocument11(String patchF) {
//        pathWF = patchF;
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(false);
//        //factory.setValidating(true);
//        //factory.setIgnoringElementContentWhitespace(true);
//        List<String> lines; // Лист с прочитанным файлом
//        boolean findErr = false; // если попали в ексепшен и дробим нашим парсером
//        try {
//            document = factory.newDocumentBuilder().parse(patchF);
//        } catch (IOException ex) {
//            System.out.println("Error read standart method XML, reding custom method " + Paths.get(patchF)); // выброс ошибок по файлу
//            findErr = true;
//        } catch (SAXException ex) {
//            System.out.println("Over error, all ride step to two !!!!"); // вот тут уже вызываем метод удаления DOCTYPE и тд если будет
//            findErr = true;
//        } catch (ParserConfigurationException ex) {
//            System.out.println("Standart parser down :(");
//            findErr = true; // так же как выше пробуем убирать DOCTYPE
//        }
//        Node n = null; // это для корневой ноды но может и не надо
//        if (findErr) { // Запускаем режим преобразования файлов только после Exception
//            try {
//                fixXML = new ReadBedXML(patchF);
//                String documenWithoutDoctype = fixXML.methodRead(patchF); // Так читаем и получаем преобразованные данные,
//                InputStream stream = new ByteArrayInputStream(documenWithoutDoctype.getBytes(StandardCharsets.UTF_8));
//                document = factory.newDocumentBuilder().parse(stream);
//                n = document.getDocumentElement(); // Получаем корневой элемент
//
//            } catch (SAXException | InterruptedException | TransformerFactoryConfigurationError | IOException | ParserConfigurationException ex) {
//                System.out.println("Not file XML or bed remove data in file" + patchF + " !");
//            }
//
//        } else {
//            n = document.getDocumentElement(); // Получаем корневой элемент
//        }
//        return n;
//    }

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
    public Node insertChildNode(Node parent, String arg) {
        String [] sa = {arg};
        return insertChildNode(parent, sa);
    }
    // --- Вставка и сождании новой ноды с параметрами ---
    public Node insertChildNode(Node parent, String[] arg){
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
        //Node root = null;
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
        this.pathWF = patchWF;
        this.document = document;
    }

    // --- пробегаме по ноды рекурсией ---
    public static void stepThroughAll(Node start) {
        //System.out.println(start.getNodeName() + " = " + start.getNodeValue());
        if (start.getNodeType() == start.ELEMENT_NODE) {
            NamedNodeMap startAttr = start.getAttributes();
            for (int i = 0; i < startAttr.getLength(); i++) {
                Node attr = startAttr.item(i);//node str=strAttr.item(i);
                //System.out.println(" Attribute: " + attr.getNodeName()+ " = " + attr.getNodeValue());
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
        if(n==null)return null;
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
        if(n==null) return null;
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
        if(document == null || "".equals(pathWF)) return;
        writeDocument(pathWF);
    }

     // --- Запипись в файл структурой XML с указанием именем файла ---
    public void writeDocument(String NewPatchWF) {
        if(document == null) return;
        try {
            File file = new File(NewPatchWF);
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
                fixXML.returnToFileDtd(NewPatchWF);//и возвращаем ему удаленный DOCTYPE
            } catch (InterruptedException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   // --- Метод чтения и подключение к базе посредством конфига ---
    public static int getConnectBaseConfig(String patchFile) {
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
                Document doc = db.parse(f);
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
                        int ldd = DesignDir.length();
                        if("design".equalsIgnoreCase(DesignDir.substring(ldd-6))) DesignDir=DesignDir.substring(0, ldd-7);
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                System.out.println("Проверьте существование или структуру " + patchFile);
                return -1;
            }
            if(url != null) globVar.dbURL = url; // добавить переменную пути проекта
            if(pass != null) globVar.PASS = pass; // добавить переменную пути проекта
            if(user != null) globVar.USER = user; // добавить переменную пути проекта
            if(base != null) globVar.currentBase = base; // добавить переменную пути проекта
            if(DesignDir != null) globVar.desDir = DesignDir; // добавить переменную пути проекта
            return 0;
        }else{
            System.out.println("Проверьте существование или структуру " + patchFile);
        }
        return -1;
    }

    // --- Удалить ноду ---
    public void removeNode(Node n) {
        Node parentN = n.getParentNode();
       parentN.removeChild(n);
    }
    // --- очистить ноду ---
    public void cleanNode(Node n) {
        NodeList child = n.getChildNodes();
        for (int i = 0; i < child.getLength(); i++) {
            Node node = child.item(i);
            if (node.getNodeType() == 1) n.removeChild(node);
        }
    }

    // --- Найти первую ноду по имени и вернуть ее ---
    public Node returnFirstFinedNode(String s) {
        return returnFirstFinedNode(root,s);
    }
    // --- Найти первую ноду по имени и вернуть ее ---
    public Node returnFirstFinedNode(Node n, String s) {
        Node finding = null;
        if (n != null) {
            //System.out.println("NodeName " + n.getNodeName() + " TypeNode " + n.getNodeType());
            if (n.getNodeType() == n.ELEMENT_NODE) { //  так имя ноды нашел
                if (n.getNodeName().equals(s)) {
                    //System.out.println("Find Node " + n.getNodeName());
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

    // --- Найти все ноды по имени и вернуть их ---
    public ArrayList<Node> getNodesName(Node n, String s) { 
        if (n.getNodeType() == n.ELEMENT_NODE) { //  проверка хз чего The node is an Element.
            if(n.getNodeName().equals(s)){
                AllFindingNode.add(n);
                System.out.println("Name:Node " + n.getNodeName());
            }
        }
        for (Node child = n.getFirstChild(); // если не элементы ноды то значит есть наследники
                child != null;
                child = child.getNextSibling()) {
            getNodesName(child, s);
        }
        return AllFindingNode;
    }
    
    // --- получить данные по аттрибуту ноды--- 
    public String getDataAttr(Node n, String s) {
        if(n==null || s==null) return null;
        NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента 
        for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды 
            Node attr = startAttr.item(i);
            if (attr.getNodeName().equals(s)) { // Название атрибута 
                return attr.getNodeValue();
            }
        }
        return null;
    }
    
    // --- изменить данные аттрибута ноды--- 
    public boolean editDataAttr(Node n, String nameAtr, String value) {
        boolean request = false;
        if(n !=null){
            NamedNodeMap startAttr = n.getAttributes(); // Получение имена и атрибутов каждого элемента 
            for (int i = 0; i < startAttr.getLength(); i++) { // Переборка значений ноды 
                Node attr = startAttr.item(i);
                if (attr.getNodeName().equals(nameAtr)) { // Название атрибута 
                    attr.setNodeValue(value);
                    return request = true;
                }
            }
        }
        return request;
    }

    // --- Найти ноду по имени и ее атрибутам ---Lev---
    public Node findNodeAtribute(String[] arg) {
        return findNodeAtribute(root, arg);
    }
    public Node findNodeAtribute(Node n, String[] arg) {
        if(arg==null || arg.length==0 || n==null) return null;
        String nameFindN = arg[0];// arg первое значение всегда Название ноды
        Node finding = null;
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
        for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
            finding = findNodeAtribute(child, arg);
            if (finding != null) return finding;
        }
        return finding;
    }

   // --- Запипись в файл структурой XML --- надо бы удалить, но оставлено на свякий случай до 1.09.2020
    public void writeDocument1() {
        if(document == null || "".equals(pathWF)) return;
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
            
            File file = new File(pathWF);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //System.out.println(document.getNodeName());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // без этого в одну строку все запишет
            //transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(file)); // наш документ в начале
            
        } catch (TransformerException e) {
            e.printStackTrace(System.out);
        }
        // тут возвращаем данные которые удаляли
        if (fixXML != null) {
            try {
                // если был вызван парсер удаления возвращамем что удалил
                fixXML.returnToFileDtd(pathWF);//и возвращаем ему удаленный DOCTYPE
            } catch (InterruptedException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // --- Запипись в файл структурой XML с указанием именем файла --- надо бы удалить, но оставлено на свякий случай до 1.09.2020
    public void writeDocument1(String NewPatchWF) {
        if(document == null) return;
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
            File file = new File(NewPatchWF);
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
                fixXML.returnToFileDtd(NewPatchWF);//и возвращаем ему удаленный DOCTYPE
            } catch (InterruptedException ex) {
                Logger.getLogger(XMLSAX.class.getName()).log(Level.SEVERE, null, ex);
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
//        HashMap<String, String> map = new HashMap<>();
//        XMLSAX test = new XMLSAX();
//        Node n = test.readDocument("test666.xml");
//        String[] massD = {"Name66", "attr1", "val1", "attr2", "val2", "attr2", "val2"};
       // test.insertChildNode(n, massD);
       // test.writeDocument();
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
//        Node fNodName = test.returnFirstFinedNode(n, "Name66"); // поиск по названию ноды
//        test.editDataAttr(fNodName, "attr1", "new_dats");// изменить значение ноды
//        test.writeDocument();
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
