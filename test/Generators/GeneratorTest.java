/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators;

import FrameCreate.TableDB;
import Tools.FileManager;
import XMLTools.XMLSAX;
import java.util.ArrayList;
import javax.swing.JProgressBar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Node;

/**
 *
 * @author cherepanov
 */
public class GeneratorTest {
    
    public GeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of genHW method, of class Generator.
     */
//    @Test
//    public void testGenHW() throws Exception {
//        System.out.println("genHW");
//        TableDB ft = null;
//        JProgressBar jProgressBar = null;
//        int expResult = 0;
//        int result = Generator.genHW(ft, jProgressBar);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of genSTcode method, of class Generator.
     */
//    @Test
//    public void testGenSTcode() throws Exception {
//        System.out.println("genSTcode");
//        TableDB ft = null;
//        boolean disableReserve = false;
//        JProgressBar jProgressBar1 = null;
//        int expResult = 0;
//        int result = Generator.genSTcode(ft, disableReserve, jProgressBar1);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of genHMI method, of class Generator.
//     */
    @Test
    public void testGenHMI() throws Exception {
        System.out.println("genHMI");
        TableDB ft = null;
        JProgressBar jProgressBar = null;
        String expResult = "";
        String result = Generator.genHMI(ft, jProgressBar);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of genTypeFile method, of class Generator.
     */
//    @Test
//    public void testGenTypeFile() throws Exception {
//        System.out.println("genTypeFile");
//        TableDB ft = null;
//        JProgressBar jProgressBar = null;
//        int expResult = 0;
//        int result = Generator.genTypeFile(ft, jProgressBar);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setHWdoc method, of class Generator.
//     */
//    @Test
//    public void testSetHWdoc() throws Exception {
//        System.out.println("setHWdoc");
//        XMLSAX hw = null;
//        String hwDew = "";
//        String hwFileSuffix = "";
//        String[] globSigAttr = null;
//        XMLSAX prj = null;
//        String prjFildName = "";
//        Node globSigInPrj = null;
//        String globUUID = "";
//        Node expResult = null;
//        Node result = Generator.setHWdoc(hw, hwDew, hwFileSuffix, globSigAttr, prj, prjFildName, globSigInPrj, globUUID);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of closeByErr method, of class Generator.
//     */
//    @Test
//    public void testCloseByErr() throws Exception {
//        System.out.println("closeByErr");
//        FileManager fm = null;
//        String tmpFile = "";
//        String err = "";
//        int expResult = 0;
//        int result = Generator.closeByErr(fm, tmpFile, err);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of genInFile method, of class Generator.
//     */
//    @Test
//    public void testGenInFile() throws Exception {
//        System.out.println("genInFile");
//        FileManager fm = null;
//        String abSubAb = "";
//        String commonFileST = "";
//        Node nodeGenCode = null;
//        TableDB ft = null;
//        boolean disableReserve = false;
//        String stFileName = "";
//        String abonent = "";
//        JProgressBar jProgressBar = null;
//        int expResult = 0;
//        int result = Generator.genInFile(fm, abSubAb, commonFileST, nodeGenCode, ft, disableReserve, stFileName, abonent, jProgressBar);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createString method, of class Generator.
//     */
//    @Test
//    public void testCreateString() throws Exception {
//        System.out.println("createString");
//        Node args = null;
//        FileManager fm = null;
//        TableDB ft = null;
//        String abonent = "";
//        boolean disableReserve = false;
//        int j = 0;
//        int expResult = 0;
//        int result = Generator.createString(args, fm, ft, abonent, disableReserve, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createFunction method, of class Generator.
//     */
//    @Test
//    public void testCreateFunction() throws Exception {
//        System.out.println("createFunction");
//        Node funcNode = null;
//        FileManager fm = null;
//        TableDB ft = null;
//        String abonent = "";
//        boolean disableReserve = false;
//        int j = 0;
//        int expResult = 0;
//        int result = Generator.createFunction(funcNode, fm, ft, abonent, disableReserve, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getFromDict method, of class Generator.
//     */
//    @Test
//    public void testGetFromDict_4args() {
//        System.out.println("getFromDict");
//        XMLSAX sax = null;
//        Node root = null;
//        String s = "";
//        String attr = "";
//        String expResult = "";
//        String result = Generator.getFromDict(sax, root, s, attr);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getFromDict method, of class Generator.
//     */
//    @Test
//    public void testGetFromDict_String_String() {
//        System.out.println("getFromDict");
//        String s = "";
//        String dict = "";
//        String expResult = "";
//        String result = Generator.getFromDict(s, dict);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPartText method, of class Generator.
//     */
//    @Test
//    public void testGetPartText() {
//        System.out.println("getPartText");
//        Node argPart = null;
//        String abonent = "";
//        TableDB ft = null;
//        int j = 0;
//        String expResult = "";
//        String result = Generator.getPartText(argPart, abonent, ft, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPartTextNonAbon method, of class Generator.
//     */
//    @Test
//    public void testGetPartTextNonAbon() {
//        System.out.println("getPartTextNonAbon");
//        Node argPart = null;
//        TableDB ft = null;
//        int j = 0;
//        String expResult = "";
//        String result = Generator.getPartTextNonAbon(argPart, ft, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSwitchValConfig method, of class Generator.
//     */
//    @Test
//    public void testGetSwitchValConfig() {
//        System.out.println("getSwitchValConfig");
//        Node nodeSwitch = null;
//        TableDB ft = null;
//        int j = 0;
//        String expResult = "";
//        String result = Generator.getSwitchValConfig(nodeSwitch, ft, j);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUUIDfromPrj method, of class Generator.
//     */
//    @Test
//    public void testGetUUIDfromPrj() {
//        System.out.println("getUUIDfromPrj");
//        XMLSAX intFile = null;
//        Node interfaceList = null;
//        String Name = "";
//        String who = "";
//        String expResult = "";
//        String result = Generator.getUUIDfromPrj(intFile, interfaceList, Name, who);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of insertVarInPrj method, of class Generator.
//     */
//    @Test
//    public void testInsertVarInPrj() {
//        System.out.println("insertVarInPrj");
//        XMLSAX intFile = null;
//        Node interfaceList = null;
//        String Name = "";
//        String Type = "";
//        String Comment = "";
//        boolean global = false;
//        boolean usage = false;
//        String uuid = "";
//        String hwFileName1 = "";
//        String backUpFile1 = "";
//        String expResult = "";
//        String result = Generator.insertVarInPrj(intFile, interfaceList, Name, Type, Comment, global, usage, uuid, hwFileName1, backUpFile1);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of genOPC method, of class Generator.
//     */
//    @Test
//    public void testGenOPC() {
//        System.out.println("genOPC");
//        String serverName = "";
//        String id = "";
//        String idType = "";
//        ArrayList opcList = null;
//        JProgressBar jProgressBar = null;
//        int expResult = 0;
//        int result = Generator.genOPC(serverName, id, idType, opcList, jProgressBar);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of genArchive method, of class Generator.
//     */
//    @Test
//    public void testGenArchive() throws Exception {
//        System.out.println("genArchive");
//        int[][] archTyps = null;
//        ArrayList archList = null;
//        String abonent = "";
//        JProgressBar jProgressBar = null;
//        String hmiApp = "";
//        int expResult = 0;
//        int result = Generator.genArchive(archTyps, archList, abonent, jProgressBar, hmiApp);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isStdType method, of class Generator.
//     */
//    @Test
//    public void testIsStdType() {
//        System.out.println("isStdType");
//        String t = "";
//        boolean expResult = false;
//        boolean result = Generator.isStdType(t);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
