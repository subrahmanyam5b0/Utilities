package com.procurement.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.ZipEntry;

import java.util.zip.ZipOutputStream;

/**
 * Zip Creator : Utility to Create a zip
 * 
 *  @author   Subrahmanyam
 *  @version 1.0
 */
public class ZipCreator implements Serializable {
    @SuppressWarnings("compatibility:4237720919740487893")
    private static final long serialVersionUID = 1L;

    /**
     * Creating ZIP with all File(s) in args.
     * 
     * @param args directory to ZIP
     * @param result destination of ZIP
     */
    public static void generate(String args, String result) throws IOException {
        String dirpath = args;
        String ZipName = result;
        ZipOutputStream zos = null;
        try {
            File ff = new File(ZipName);
            zos = new ZipOutputStream(new FileOutputStream(ff));            
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        if (zos == null)
            return;
        zipdirectory(dirpath, zos);
        zos.close();
    }

    /**
     * Creating Company Reports to ZIP.
     * 
     * @param args directory to ZIP
     * @param result destination of ZIP
     * @param companyid only ZIP this company Files Only
     */
    public static void generate(String args, String result, String companyid) throws IOException {
        String dirpath = args;
        String ZipName = result;
        ZipOutputStream zos = null;
        try {
            File ff = new File(ZipName);
            zos = new ZipOutputStream(new FileOutputStream(ff));
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        if (zos == null)
            return;
        zipdirectory(dirpath, zos, companyid);
        zos.close();
    }

    /**
     * Zipping Files.
     * 
     * @param dirpath directory to ZIP
     * @param zos Result ZIP Output Stream
     * @param companyid only ZIP this company Files Only
     */
    public static void zipdirectory(String dirpath, ZipOutputStream zos, String companyid) throws IOException {
        File f = new File(dirpath);
        String[] flist = f.list();
        for (int i = 0; i < flist.length; i++) {
            File ff = new File(f, flist[i]);
            if (ff.isDirectory()) {
                zipdirectory(ff.getPath(), zos, companyid);
                continue;
            }
            if (ff.getName().toLowerCase().contains("common") || ff.getName().endsWith("html"))
                continue;
            String filepath = ff.getPath();
            ZipEntry entries = new ZipEntry(filepath);
            zos.putNextEntry(entries);
            FileInputStream fis = new FileInputStream(ff);
            int buffersize = 1024;
            byte[] buffer = new byte[buffersize];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, count);
            }
            fis.close();
        }
    }

    /**
     *  Zipping All files in dirpath to ZIP.
     *  
     * @param dirpath directory to ZIP
     * @param zos Result ZIP Output Stream
     */
    public static void zipdirectory(String dirpath, ZipOutputStream zos) throws IOException {
        File f = new File(dirpath);
        String[] flist = f.list();
        for (int i = 0; i < flist.length; i++) {
            File ff = new File(f, flist[i]);
            if (ff.isDirectory()) {
                zipdirectory(ff.getPath(), zos);
                continue;
            } else if (ff.getName().endsWith(".zip"))
                continue;
            String filepath = ff.getPath();
            ZipEntry entries = new ZipEntry(filepath);
            zos.putNextEntry(entries);
            FileInputStream fis = new FileInputStream(ff);
            int buffersize = 1024;
            byte[] buffer = new byte[buffersize];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, count);
            }            
            fis.close();
        }
    }
}
