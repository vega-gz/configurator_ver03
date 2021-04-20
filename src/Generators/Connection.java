/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

/**
 *
 * @author nazarov
 * 
 * объект который связывает сигналы, храниться в ноде <FBNetwork> -->  <DataConnections>
 */
public class Connection {
    // все переменные подключения
    final String Connection = "Connection";    // название ноды
    String Destination = null;                 // Генерируемое имя соединения
    String DestinationUUID = null;             // два UUID.UUID(UUIDFB.UUIDOrigSignal)
    String Source = null;                      // название оригинального сигнала
    String SourceUUID = null;                  // UUID сигнала VarDeclaration родительского объекта в ком находится генерируемый блок
    String UUIDFB = null;                      // UUID  1- самого блока внутренннего блока который сгенерировали
    String UUIDOrigSignal = null;              // UUID  2 - сигнал на основе которого сделали

    public Connection(String Source){
        this.Source = Source;
    }
    
    public void setUUIDFB(String UUIDFB){
        this.UUIDFB = UUIDFB;
    }
    
    public void setUUIDOrigSignal(String UUIDOrigSignal){
        this.UUIDOrigSignal = UUIDOrigSignal;
    }
    
    public void setUUIDVarDeclaration(String SourceUUID){
        this.SourceUUID = SourceUUID;
    }
    
    public String getName(){ 
        return Source;
    }
     public String getUUIDFB(){
        return UUIDFB;
    }
    
    public String getUUIDOrigSignal(){
        return UUIDOrigSignal;
    }
    
    public String getUUIDVarDeclaration(){
        return SourceUUID;
    }
    
}
