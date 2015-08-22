package com.m2a;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.m2a.bean.M2ABean;
import com.m2a.dto.StudentDTO;

public class SampleTest {

	public static void maininsert(String []a)throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		  Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/subbu","root","root");
		  PreparedStatement st=con.prepareStatement("insert into sample"
		  		+ "(SerialNo,Name,SalaryAmount,DateOfBirth,Signature)"
		  		+ " values(?,?,?,?,?)");
		  File f=new File("D:\\1.jpg");
		  FileInputStream in=new FileInputStream(f);
		  byte[] s=new byte[in.available()];
		  in.read(s);
		  st.setInt(1, 11);
		  st.setString(2, "SDSD");
		  st.setDouble(3, 15.0);		  
		  st.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
		  //st.setBinaryStream(5, in,(int) f.length());
		  st.setBytes(5, s);
		  st.executeUpdate();
		  /*Statement st=con.createStatement();
		  ResultSet rs=st.executeQuery("select lb from other");
		  ResultSetMetaData rsd=rs.getMetaData();
		  rs.next();
		  for(int i=1;i<=rsd.getColumnCount();i++){
			  System.out.println(rsd.getColumnClassName(i)+"\t"+rsd.getColumnName(i)+"\t"+rsd.getColumnType(i));
			 InputStream s = rs.getBinaryStream(1);
			 byte[] s1=new byte[s.available()];
			 s.read(s1);
			 
				Class resultClass = Class.forName("com.m2a.dto.StudentDTO");
				Object result = resultClass.newInstance();

				Field sampleField = ReflectionUtils.findField(resultClass,
						"signature");

				if (sampleField != null) {

					System.out.println("" + sampleField.getName());
					Method sampleMethod = ReflectionUtils.findMethod(resultClass,
							String.format("set%C%s", sampleField.getName().charAt(0),
									sampleField.getName().substring(1)), byte[].class);
					if (sampleMethod != null) {
						System.out.println("" + sampleMethod.getName());
						sampleMethod.invoke(result, s1);
					}
				}
				System.out.println("R"+((StudentDTO)result).getSignature().length);
				}
				*/	
		  
		  
	}
	  public static void main(String []a){
		  try{
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/subbu","root","root");
			  String procName;
			  Map<String,Object> map1=new LinkedHashMap<String,Object>();			  
			  File f=new File("D:\\1.jpg");
			  FileInputStream in=new FileInputStream(f);
			  byte[] imageData = new byte[in.available()];			  
			  in.read(imageData);			  
			  map1.put("serialNo", 1);
			  map1.put("studentName", "Pradeep");
			  map1.put("salaryAmount", 14.57);
			  map1.put("dateOfBirth", new Date());			  
			  map1.put("signature",imageData);
			  map1.put("mobileNo",9533792284L);
			  map1.put("annualSalary",new BigDecimal(121212121212121.123));
			  procName = "{call p_saveStudent(?,?,?,?,?,?,?)}";
			  M2ABean d=new M2ABean(con);
			  int re=d.executeUpdate(map1, procName, true);
			  System.out.println("Result :"+re);
			  
			  HashMap<String,Object> map=new HashMap<String,Object>();
			  map.put("sessionKey", 1);
			  
			   procName = "{call p_getStudentList(?)}";
			 List result =d.getCollections(map, procName, "com.m2a.dto.StudentDTO", new StudentDTO(), true);			 
			 System.out.println(result.size());
			 for(int i=0;i<result.size();i++){
				 System.out.println(result.get(i));
			 }
			 }
		  catch(Exception e){
			  e.printStackTrace();
		  }
	  }
}
