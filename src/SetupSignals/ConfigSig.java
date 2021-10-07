/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SetupSignals;

/**
 *
 * @author nazarov
 */
public abstract class ConfigSig {
    private String  name = null;
    public enum StatusSeting { NEWSIGNAL, FROMBASE, COPY , DELBASE}
    private StatusSeting status = null;
    private String[] data = null; 
    private String id = null;
    private Integer localId = null;
    
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
        3 - NameSeting
        4 - Type
        5 - NameSignalSeting
        6 - Direction
        7 - Delay
        8 - LostSignal
        9 - Value
    */
        data = newData;
        if(id == null ){
            id = data[0];
        }
    }
    
    public String[] getData(){
        return data;
    }
    
    public String getId(){
        return id;
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
}
