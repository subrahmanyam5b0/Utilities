/**	
 * 
 */
package com.m2a.base;

import java.util.List;
import java.util.Map;
/*
 * @author Subrahmanyam
 *
 */
public interface M2AInterface {

	/**
	 * Retrieve the DB Result as ArrayCollection from query or a procedure
	 * @param inputParameters - Input Parameters
	 * @param queryString     - SQL Query / Procedure
	 * @param className       - Object(DTO) Class Name 
	 * @param dtoClass        - Required Object(DTO) to be stored in List
	 * @param isProcedure     - Procedure Call / Query
	 * @return                - Final Result 
	 */
	@SuppressWarnings("rawtypes")
	public List getCollections(Map<String,Object> inputParameters,String queryString,String className,Object dtoClass,boolean isProcedure)throws Exception;
	
	/**
	 * Insert / Update Table Data
	 * @param inputParameters   - Input Parameters
	 * @param queryString       - SQL Query / Procedure
	 * @param isProcedure       - Procedure Call / SQL Query 
	 * @return                  - No. of records updated/Inserted 
	 */
	public int executeUpdate(Map<String,Object> inputParameters,String queryString,boolean isProcedure)throws Exception;
	
}
