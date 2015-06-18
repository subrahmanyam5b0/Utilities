package excel;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
/**
 *
 * @author hp
 */
public class ExcelSheetProcessor {

       
    public  void processWorkBook(String file_name){
        try{          
        Workbook wb= Workbook.getWorkbook(new File(file_name));       
        int no_sheets=wb.getNumberOfSheets();
        List result=new ArrayList();
        for(int i=0;i<no_sheets;i++){
            result.add(processSheet(wb.getSheet(i)));
          }
        processResult(result);
//        System.out.println("Processing Sheet By Name Column");
//        result.clear();
//         for(int i=0;i<no_sheets;i++){
//            result.add(processSheetByNameColumn(wb.getSheet(i),"name","class"));
//         }
//        processResultByNameColumn(result);              
        wb.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public  List processSheet(Sheet s){            
            List list=new ArrayList();
            int no_rows=s.getRows();
            list.add(s.getName());            
            for(int i=0;i<no_rows;i++){
                list.add(processRow(s.getRow(i)));
            }
            return list;
        }
    public String[] processRow(Cell[] cl){
        int no_cells=cl.length;
        String[] rows=new String[no_cells];
         for(int i=0;i<no_cells;i++){
             rows[i]=cl[i].getContents();             
         }
        return rows;
    }
    public List processSheetByNameColumn(Sheet s,String label,String label2){
        List list=new ArrayList();
        try{
          Cell cl=s.findCell(label,0,0,s.getColumns(),1,true),cl2=s.findCell(label2,0,0,s.getColumns(),1,true);                
          list.add(s.getName());
          int n_row_target=cl.getRow(),n_col_target=cl.getColumn(),no_rows=s.getRows();
          int c_row_target=cl2.getRow(),c_col_target=cl2.getColumn();
          String [][]result=new String[no_rows-1][2];
          int j=0;
          if(n_row_target!=c_row_target){
              System.out.println("Only First Row Contains Labels");
              return list;
          }
            for(int i=n_row_target+1;i<no_rows;i++){
                result[j][0]=s.getCell(c_col_target, i).getContents();
                result[j++][1]=s.getCell(n_col_target,i).getContents();                              
              }
            list.add(result);
        }
        catch(Exception e){
            //System.err.println(e.getMessage());
        }
        return list;
    }    
    
    public void processResult(List result){
      int size=result.size();
      for(int i=0;i<size;i++){
              processResultSheet((List)result.get(i));
      }
    }
    public void processResultSheet(List sheet){
        System.out.println("Sheet Name : "+sheet.get(0));
        int size=sheet.size();
        for(int i=1;i<size;i++){
            processResultRow((String[])sheet.get(i));            
        }
        
    }
    public void processResultRow(String []row){
        int no_cols=row.length;
        for(int i=0;i<no_cols;i++)
            System.out.print(row[i]+"\t");        
        System.out.println();
    }
    
    public void processResultByNameColumn(List result){
      int size=result.size();
      for(int i=0;i<size;i++){
              processResultSheetByNameColumn((List)result.get(i));
      }
    }
    public void processResultSheetByNameColumn(List sheet){        
        System.out.println("Sheet Name : "+sheet.get(0));                
        if(sheet.size()!=1)
            processResultRowByNameColumn((String[][])sheet.get(1));            
        else
          System.out.println("No Content Found");
    }
    public void processResultRowByNameColumn(String [][]row){
        int no_cols=row.length;
        for(int i=0;i<no_cols;i++){
            System.out.print(row[i][0]+"\t"+row[i][1]);        
        System.out.println();
        }
    }
    
}
