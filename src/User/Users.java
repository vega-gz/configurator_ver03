/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package User;

import globalData.globVar;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nazarov
 */
public class Users {
    private String nameTable = "Users";
    private String[] columnTable = {"name", "pass", "group"};
    private String curentUser = null;
    //private String currentPass = globVar.PASS; 
    //private String currentUser = globVar.USER;
    
    private static volatile Users instance; //  volatile - гарантированная работа с переменной при синхронизации потоков
    private Users(){
    }
    
    public static synchronized Users getInstance(){ // #3 
      if(instance == null){                     
          synchronized (Users.class){
          if(instance == null){
            instance = new Users();
          }
        }
      }
      return instance;                          
    }
    
    public String getCurentUser(){
        return curentUser;
    }

    public List<String> getUserAccesToBase(){
        List<String> listUser = null;
        if(globVar.DB.isTable(nameTable)){
            List<String[]> namedFromUsers = globVar.DB.getData(nameTable, new String[]{columnTable[0]}); 
            listUser = new LinkedList<>();
            for (String[] arr: namedFromUsers) {
                listUser.add(arr[0]);
            }
        }
        else{
            createTableUsers();
        }
        return listUser;
    }
    
    
    public void setInFile(String str){
    
    }
    public String getInFile(){
        return "";
    }

    private void createTableUsers() {
        globVar.DB.createTableEasy(nameTable, columnTable, "Users to local used.");
    }
}

