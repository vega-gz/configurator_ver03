/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools;

import java.util.List;

/**
 *
 * @author nazarov
 */
public interface LoggerInterface {
    public void writeLog(String str);
    public List<String> readLog();
}
