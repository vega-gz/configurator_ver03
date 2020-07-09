package Tools;

import globalData.globVar;
import java.io.File;
import javax.swing.JOptionPane;

/*@author Lev*/
public class Tools {
    public static boolean isDesDir(){
        File f = new File(globVar.desDir + "\\Design");
        boolean x = false;
        if(f!=null) x = f.isDirectory();
        if(!x) JOptionPane.showMessageDialog(null, "В каталоге \"" + globVar.desDir + "\" нет каталога \"Design\"");
        return x;
    }
    
    public static boolean isDB(){
        if (globVar.DB == null) {
            JOptionPane.showMessageDialog(null, "База " + globVar.currentBase + "по пути " + globVar.dbURL + " не найдена");
            return false;
        }
        return true;
    }
    
}
