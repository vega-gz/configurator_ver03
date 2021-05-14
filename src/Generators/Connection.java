/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Generators;

import java.util.ArrayList;

/**
 *
 * @author nazarov
 * 
 * общий интерфес соединения сигналов в графическом типе
 * 
 */
interface Connection {
    public ArrayList<ConnectionData> getConnectionsSigsVarDeclaration();
    public ArrayList<ConnectionData> getConnectionsSigsEvent();
}
