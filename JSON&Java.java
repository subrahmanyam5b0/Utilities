Convert Java object to JSON

To convert the employee object and write it to some file, to can use below code:

package test.jackson;
 
import java.io.File;
import java.io.IOException;
import java.util.Date;
 
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
 
public class JavaToJSONExample
{
   public static void main(String[] args)
   {
      @SuppressWarnings("deprecation")
      Employee employee = new Employee(1, "Lokesh", "Gupta", new Date(1981,8,18));
      ObjectMapper mapper = new ObjectMapper();
      try
      {
         mapper.writeValue(new File("c://temp/employee.json"), employee);
      } catch (JsonGenerationException e)
      {
         e.printStackTrace();
      } catch (JsonMappingException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
 
Output:
 
//In employee.json file below content will be written
 
{"id":1,"firstName":"Lokesh","lastName":"Gupta"}

Convert Java object to Formatted JSON Output

If you look at above output, then the output written in text file is very raw and not formatted. You can write a formatted JSON content using defaultPrettyPrintingWriter() writerWithDefaultPrettyPrinter instance like below:

package test.jackson;
 
import java.io.File;
import java.io.IOException;
import java.util.Date;
 
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
 
public class JavaToPrettyJSONExample
{
   public static void main(String[] args)
   {
      @SuppressWarnings("deprecation")
      Employee employee = new Employee(1, "Lokesh", "Gupta", new Date(1981,8,18));
      ObjectMapper mapper = new ObjectMapper();
      try
      {
         mapper.defaultPrettyPrintingWriter().writeValue(new File("c://temp/employee.json"), employee);
      } catch (JsonGenerationException e)
      {
         e.printStackTrace();
      } catch (JsonMappingException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
 
Output:
 
{
  "id" : 1,
  "firstName" : "Lokesh",
  "lastName" : "Gupta"
}

Convert JSON to Java object

To convert a json object to java object (e.g. our employee object) use below code:

package test.jackson;
 
import java.io.File;
import java.io.IOException;
 
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
 
public class JSONToJavaExample
{
   public static void main(String[] args)
   {
      Employee employee = null;
      ObjectMapper mapper = new ObjectMapper();
      try
      {
         employee =  mapper.readValue(new File("c://temp/employee.json"), Employee.class);
      } catch (JsonGenerationException e)
      {
         e.printStackTrace();
      } catch (JsonMappingException e)
      {
         e.printStackTrace();
      } catch (IOException e)
      {
         e.printStackTrace();
      }
      System.out.println(employee);
   }
}
 
Output:
 
Employee [id=1, firstName=Lokesh, lastName=Gupta]
Make sure you have defined a default constructor in your POJO class (e.g. Employee.java in our case). 
Jackson uses default constructor to create the instances of java class using reflection. 
If default constructor is not provided, then you will get JsonMappingException in runtime.