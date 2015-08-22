/**
 * 
 */
package com.m2a.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.util.ReflectionUtils;

/**
 * @author subrahmanyam
 * 
 */
public class M2AMapper {

	public static void assignValue(Object resultObject,Class resultDTO, String field,
			Class fieldClass, Object fieldValue,int columnType) throws Exception {
		
		if(fieldValue == null){
			return;
		}

		Field resultField = ReflectionUtils.findField(resultDTO, field);
		
		if (resultField != null) {
			
			field = String.format("set%C%s", field.charAt(0),
					field.substring(1));
			/**
			 * java.lang.Integer				int						4
				java.lang.Long				big					-5
				java.lang.Integer			small					5
				java.lang.Float				flat					7
				java.lang.Double			double				8
				java.math.BigDecimal		decimal				3
				java.lang.String				char					1
				java.lang.String				varChar				12
				java.lang.String				text					-1
				java.lang.String				longText			-1
				[B									blob					-4
				[B									longblob			-4
				java.sql.Date					date					91
				java.sql.Time					time					92
				java.sql.Timestamp		datTime				93
				java.sql.Timestamp		timeStamp			93
				java.lang.String				enum					1
				java.lang.String				set					1
			 */
			switch(columnType){
			
			   case -4:
				           fieldClass = byte[].class;				           
				           break;
			    case 1  :
			    case -1 :	
			    case 12 : fieldClass = String.class;break; 	
			    
			    case 3  : fieldClass = BigDecimal.class;break;
			    
				case 4 	: 
				case 5 	: 	fieldClass = Integer.class ;break;
				
				case -5 :   fieldClass = Long.class;break;
				
				case 7  :
				case 8  :
					         fieldClass = Double.class;break;
					         
				case 91:
				case 92:
				case 93: fieldClass = java.sql.Timestamp.class;break;
				
				default : fieldClass = String.class;
			}
			
			
			if(fieldClass == java.sql.Timestamp.class || fieldClass == java.sql.Date.class || fieldClass == java.sql.Time.class)
				fieldClass = java.util.Date.class;
			
			Method resultMethod = ReflectionUtils.findMethod(resultDTO, field,
					fieldClass);
			
			if(resultMethod == null && (fieldClass == Integer.class || fieldClass == Double.class || fieldClass == Long.class) ){
				
				if(fieldClass == Integer.class) 
						fieldClass = int.class;				
				else if(fieldClass == Double.class)  
						fieldClass = double.class;
				else
					fieldClass = long.class;
				
				resultMethod = ReflectionUtils.findMethod(resultDTO, field,
						fieldClass);
			}			
			if(resultMethod == null) {
				addErrMessage(resultObject, resultDTO, field, fieldClass, "NoSuchMethod Definition Found \""+field+"=>"+fieldClass+"\"");
				return;
				//throw new Exception("No Setter Method is available :"+field+"=>"+fieldClass);
			}			
			
			if (fieldValue instanceof java.sql.Timestamp) {
				Timestamp sqlDate = (Timestamp) fieldValue;
				java.util.Date javaDate = new java.util.Date(
						sqlDate.getTime());
				resultMethod.invoke(resultObject, javaDate);
			}			
			else {
				resultMethod.invoke(resultObject, fieldValue);
			}
		}
		else {
			addErrMessage(resultObject, resultDTO, field, fieldClass, "Field is not found \""+field+"=>"+fieldClass+"\"");
		}
	}
	
	public static void addErrMessage(Object resultObject,Class resultDTO, String field,
			Class fieldClass,String errMsg)throws Exception{
		
		Method resultMethod = ReflectionUtils.findMethod(resultDTO, "addErrMsg",
				String.class);
			resultMethod.invoke(resultObject, errMsg);
	}

}
