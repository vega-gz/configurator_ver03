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
 * общий интерфес соединения сигналов в графическом типе
 * 
 */
interface Connection {

    public Object getName();

    public void setUUIDOrigSignal(String get);

    public String getUUIDVarDeclaration();

    public String getUUIDOrigSignal();

    public void setUUIDVarDeclaration(String uuidVarDeclaration);
    
}
