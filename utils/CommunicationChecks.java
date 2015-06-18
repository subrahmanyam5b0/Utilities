/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author USER
 */
public class CommunicationChecks {

    public static void main(String[] args) throws Exception {
        System.out.println("" + new CommunicationChecks().checkForCommAPI("", "", ""));
    }

    public String checkForCommAPI(String comPath, String properties, String dll) throws Exception {

        String result = "";
        String jrePath = new JREHomeFinder().findJavaHome();
        //Checking for comm.jar in <jrePath>/lib/ext Installtion
        File f = new File(jrePath + File.separator + "lib" + File.separator + "ext" + File.separator + "comm.jar");
        if (!f.exists()) {
          //  JOptionPane.showMessageDialog(null,""+f.getAbsolutePath()+"\t:"+comPath);
            //System.out.println("comm.jar Not Exists");

            URL url = new URL(comPath);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i = 0;
            while ((i = in.read(data, 0, 1024)) >= 0) {
                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                float Percent = (totalDataRead * 100) / filesize;
            }
            bout.close();
            in.close();

        }
        //Checking for javax.common.properties in <jrePath>/lib Installtion
        f = new File(jrePath + File.separator + "lib" + File.separator + "javax.comm.properties");
        if (!f.exists()) {
            // System.out.println("javax.comm.properties Not Exists");
            URL url = new URL(properties);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i = 0;
            while ((i = in.read(data, 0, 1024)) >= 0) {
                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                float Percent = (totalDataRead * 100) / filesize;

            }

            bout.close();
            in.close();
        }
        f = new File(jrePath + File.separator + "bin" + File.separator + "win32com.dll");
        if (!f.exists()) {
            //System.out.println("win32com.dll Not Exists");
            URL url = new URL(dll);
            HttpURLConnection connection
                    = (HttpURLConnection) url.openConnection();
            int filesize = connection.getContentLength();
            float totalDataRead = 0;
            java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int i = 0;
            while ((i = in.read(data, 0, 1024)) >= 0) {
                totalDataRead = totalDataRead + i;
                bout.write(data, 0, i);
                float Percent = (totalDataRead * 100) / filesize;

            }

            bout.close();
            in.close();
        }
        return result;
    }
}
