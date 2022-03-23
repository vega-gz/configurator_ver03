/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nazarov
 */
public abstract class ConfigSig {
    private String  name;
    public enum StatusSeting { NEWSIGNAL, FROMBASE, COPY , DELBASE, FROMFILE}
    private StatusSeting status;
    private String[] data; 
    private String id;
    private String abonent;
    private String nameTableFromSignal;
    private String NameSeting;
    private String NameSignalSeting;
    private Integer localId;
    private String[] dataParentSignal;
    
    public void setStatus(StatusSeting status)
    {
        this.status = status;
    }
    public StatusSeting getStatus()
    {
       return status;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setData(String[] newData){
    /*
        0 - id
        1 - abonent
        2 - nameTableFromSignal
        3 - NameSetingRus
        4 - NameSeting
        5 - Type
        6 - NameSignalSeting
        7 - Direction
        8 - Delay
        9 - LostSignal
        10 - Value
    */
        data = newData;
        if(id == null ){
            id = data[0];
        }
        abonent = data[1];
        nameTableFromSignal = data[2];
        NameSeting = data[4];
        NameSignalSeting = data[6];
        
    }
    
    public String[] getData(){
        return data;
    }
    
    public String getId(){
        return id;
    }

    public void setAddetionDataParent(String[] dataParentSignal){
       this.dataParentSignal = dataParentSignal;
    }
    public String[] getDataParent(){
        return dataParentSignal;
    }

    
    
    public String getNameSignalSeting(){
        return NameSignalSeting;
    }
    
    
    
    public void setId(String newId){
        data[0] = newId;
    }
    
     public int getLocalId(){
        return localId;
    }
    
    public void setLocalId(int newId){
        localId = newId;
    }
    
     @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // мы ли это с самим с собой
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // обязательная проверка что классы разные так как параметры могут быть одинаковы
        }
        // ПРоверка по всем полям какие нужны для сравнения
        ConfigSig сonfigSig = (ConfigSig) o;
        if (abonent.equals(сonfigSig.abonent) == false) {
            return false;
        }
        if (nameTableFromSignal.equals(сonfigSig.nameTableFromSignal) == false) {
            return false;
        }
        if (NameSeting.equals(сonfigSig.NameSeting) == false) {
            return false;
        }
        return NameSignalSeting.equals(сonfigSig.NameSignalSeting);
    }

    @Override
    public int hashCode() {
        /* Количество полей должно быть одинаково как и equals
         у двух одинаковых объектов должен быть одинаковый hash
        */
        
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            if(data[i] != null){
               result = 31 * result + data[i].hashCode();
            }
        }
        return result;
    }
    
}
