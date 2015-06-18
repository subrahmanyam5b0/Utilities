/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author USER
 */
public class JREHomeFinder {
    public static void main(String[] args) {
        System.out.println(new JREHomeFinder().findJavaHome());
    }
    /**
     * Returns JRE 7 Installed Directory
     *  <ul>
     *   <li>Create Batch File which returns the JRE Home Directory</li>
     *   <li>Execute that batch file using Runtime</li>
     * </ul>
     * @return Path to JRE Home Directory
     */
    public String findJavaHome(){
       
        String result="";
        try {
            File f=new File("javahomeFinder.bat");
            if(!f.exists()){
                f.createNewFile();
                BufferedWriter writer=new BufferedWriter(new FileWriter(f));
                result="@ECHO OFF &SETLOCAL ";
                writer.write(result+"\n");
                result="FOR /F \"tokens=2*\" %%a IN ('REG QUERY \"HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Runtime Environment\\1.7\" /v JavaHome') DO set \"JavaHome17=%%b\"\n";
                writer.write(result);
                result="ECHO %JavaHome17%\n";
                writer.write(result);
                writer.flush();
                writer.close();
                result="";
            }
            String[] command = new String[3];
            command[0] = "cmd";
            command[1] = "/c";
            command[2] = ""+f.getAbsolutePath();

            Process process = Runtime.getRuntime().exec(command);
            //System.out.println(""+f.getAbsolutePath());

            // Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read command standard output
            String s;           
            while ((s = stdInput.readLine()) != null) {                
                   result+=s;
            }
            f.delete();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
