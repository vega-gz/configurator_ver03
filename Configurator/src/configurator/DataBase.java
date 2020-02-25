/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    Statement stmt;
    Connection connection = null;
    private ArrayList<String[]> currentSelectTable;
    private String[] colums;

    void connectionToBase(String url, String user,String pass) {

        String DB_URL = url;
        String USER = user;
        String PASS=pass;
        try{
            Class.forName("org.postgresql.Drivar");
        }catch(ClassNotFoundException e){
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        System.out.println("");
        System.out.println("PostgreSQL JDBC Driver successfully connected");
        try{
            connection=DriverManager.getConnection(url, user, pass);
        }catch(SQLException e){
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

    }
}
