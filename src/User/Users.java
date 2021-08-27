/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package User;

import globalData.globVar;

/**
 *
 * @author nazarov
 */
public class Users {
    private String nameTable = "Users";
    private String[] columnTable = {"name", "pass", "group"};
    //private String currentPass = globVar.PASS; 
    //private String currentUser = globVar.USER;
    
    private static volatile Users instance; //  volatile - гарантированная работа с переменной при синхронизации потоков
    private Users(){
    }
    
    public static synchronized Users getInstance(){ // #3 
      if(instance == null){                     //если объект еще не создан
          synchronized (Users.class){
          if(instance == null){
            instance = new Users();
          }
        }
      }
      return instance;                          
    }
    

    public void setInFile(String str){
    
    }
    public String getInFile(){
        return "";
    }
}

