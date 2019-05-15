/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package basepostgresluaxls;


import java.io.IOException;
import java.io.StringReader;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.jse.*;

/**
 *
 * @author admin
 */
public class LuaRun {

Path listing = FileSystems.getDefault().getPath("C:\\Users\\admin\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\LUA_FILE\\Template\\AI\\");

        	public void runL(){

                //String fileFullName = "C:/Users/admin/Desktop/Info_script_file_work/"
                //+ "_actual_config/Config/Design/LUA_FILE/Template/AI/test_02.lua";
                //String fileFullName = "test_02.lua";
               String fileFullName = "C:\\Users\\admin\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\LUA_FILE\\Template\\AI\\test_02.lua";
	Globals globals = JsePlatform.standardGlobals();
       globals.load("package.path = 'C:\\Users\\admin\\Desktop\\Info_script_file_work\\_actual_config\\Config\\Design\\LUA_FILE\\Template\\AI\\?.lua;'");
        globals.load(new JseBaseLib());
     globals.load(new PackageLib());
    System.out.println( globals.get("require").call("foo") );
       // LuaValue chunk = globals.loadfile(fileFullName);
    //	chunk.call();
        }
                        	public void runLua(String arg_str)
	{

                //String fileFullName = "C:/Users/admin/Desktop/Info_script_file_work/"
                //+ "_actual_config/Config/Design/LUA_FILE/Template/AI/test_02.lua";
                String fileFullName = "test_02.lua";
               // C:\Users\admin\Desktop\Info_script_file_work\_actual_config\Config\Design\LUA_FILE\Template\AI\test_02.lua
	Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.loadfile(arg_str);
	chunk.call();
        }
                                

}