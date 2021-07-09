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
    //private String[] columnT = {"id", "Abonent", "NameSeting", "Type", "NameSig", "Direction", "Delay", "LostSignal", "Value"}; // Набор столбцов для базы таблицы
    private String[] data = new String[9]; 
    private String id = data[0];
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
        for (int i = 0; i < newData.length; i++) {
            if(i > data.length - 1) break;  // на всякий если что то не то попало
            data[i] = newData[i];
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
