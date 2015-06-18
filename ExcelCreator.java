package com.procurement.utils;

import jxl.*;
import jxl.write.*;
import java.io.*;

/**
 * Utility Class to Create Excel Sheet(.xls) for Milk Collections,Bank Wise
 * Payments,Masters Export
 *
 * @author USER
 */
public class ExcelCreator {

    /**
     * Milk Collections as Excel Sheet
     *
     * @param data Rows
     * @param co Columns
     * @param s File Name
     * @return <code>success</code> or <code>fail</code>
     * @throws IOException
     */
    public String createMilkCollection(String data[][], String co[], String s) throws IOException {

        File ffff = new File(s);        
        WritableWorkbook workbook = Workbook.createWorkbook(ffff);
        WritableSheet sheet = workbook.createSheet("MilkCollections", 0);        
        try {
            for (int i = 0; i < co.length; i++) {
                Label label = new Label(i, 0, "" + co[i]);
                sheet.addCell(label);
            }
            for (int j = 0; j < data.length; j++) {
                for (int i = 0; i < data[j].length; i++) {
                    if (data[j][i] == null || data[j][i].equals("null")) {
                        data[j][i] = "";
                    }
                    Label ll = new Label(i, j + 1, "" + data[j][i]);                                                            
                    sheet.addCell(ll);
                    
                }
            }
            workbook.write();                        
            workbook.close();                
            return "Success";
        } catch (Exception e) {

        }
        return "fail";
    }

    /**
     * Masters as Excel Sheet
     *
     * @param data Rows
     * @param co Columns
     * @param s File Name
     * @param sheetName Excel Sheet Name
     * @param sheetIndex Sheet Position
     * @return <code>success</code> or <code>fail</code>
     * @throws Exception
     */
    public String createBackup(Object data[][], Object co[], String s, String sheetName, int sheetIndex) throws Exception {

        File ffff = new File(s);
        WritableWorkbook workbook = null;
        if (sheetIndex != 0) {
            workbook = Workbook.createWorkbook(ffff, Workbook.getWorkbook(ffff));
        } else {
            workbook = Workbook.createWorkbook(ffff);
        }
        WritableSheet sheet = workbook.createSheet(sheetName, sheetIndex);
        try {
            for (int i = 0; i < co.length; i++) {
                Label label = new Label(i, 0, "" + co[i]);
                sheet.addCell(label);
            }
            for (int j = 0; j < data.length; j++) {
                for (int i = 0; i < data[j].length; i++) {
                    if (data[j][i] == null || data[j][i].equals("null")) {
                        data[j][i] = "";
                    }
                    Label ll = new Label(i, j + 1, "" + data[j][i]);
                    sheet.addCell(ll);
                }
            }
            workbook.write();
            workbook.close();
            return "Success";
        } catch (Exception e) {

        }
        return "fail";
    }

    /**
     * Bank Payments as Excel file with each bank name as Sheet Name
     *
     * @param data rows
     * @param co Columns
     * @param s File Name
     * @param sheetName Sheet Name
     * @param sheetIndex Sheet Position
     * @return <code>success</code> or <code>fail</code>
     * @throws Exception
     */
    public String createBankPayments(String data[][], String co[], String s, String sheetName, int sheetIndex) throws Exception {
        File ffff = new File(s);
        WritableWorkbook workbook = null;
        if (sheetIndex != 0) {
            workbook = Workbook.createWorkbook(ffff, Workbook.getWorkbook(ffff));
        } else {
            workbook = Workbook.createWorkbook(ffff);
        }
        WritableSheet sheet = workbook.createSheet(sheetName, sheetIndex);
        try {
            for (int i = 0; i < co.length; i++) {
                Label label = new Label(i, 0, "" + co[i]);
                sheet.addCell(label);
            }
            for (int j = 0; j < data.length; j++) {
                for (int i = 0; i < data[j].length; i++) {
                    if (data[j][i] == null || data[j][i].equals("null")) {
                        data[j][i] = "";
                    }
                    Label ll = new Label(i, j + 1, "" + data[j][i]);
                    sheet.addCell(ll);
                }
            }
            workbook.write();
            workbook.close();
            return "Success";
        } catch (Exception e) {

        }
        return "fail";
    }
}
