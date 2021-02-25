/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Generators;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cherepanov
 */
public class FBVTest {
    
    public FBVTest() {
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
     * Test of addVarValue method, of class FBV.
     */
    @Test
    public void testAddVarValue() {
        System.out.println("addVarValue");
        FBVarValue fbVar = null;
        FBV instance = new FBV();
        instance.addVarValue(fbVar);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListValue method, of class FBV.
     */
    @Test
    public void testGetListValue() {
        System.out.println("getListValue");
        FBV instance = new FBV();
        ArrayList<FBVarValue> expResult = null;
        ArrayList<FBVarValue> result = instance.getListValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editVar method, of class FBV.
     */
    @Test
    public void testEditVar() {
        System.out.println("editVar");
        ArrayList match = null;
        FBV instance = new FBV();
        instance.editVar(match);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delVar method, of class FBV.
     */
    @Test
    public void testDelVar() {
        System.out.println("delVar");
        ArrayList<String> match = null;
        FBV instance = new FBV();
        instance.delVar(match);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
