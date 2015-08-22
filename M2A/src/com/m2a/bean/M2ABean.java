/**
 * 
 */
package com.m2a.bean;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.m2a.base.M2AInterface;
import com.m2a.utils.M2AMapper;

/**
 * @author Subrahmanyam VVN
 * 
 */
public class M2ABean implements M2AInterface {

	private List<Object> finalResult;
	private int noOfRecords;

	private Connection connection;
	private CallableStatement callableStatement;
	private PreparedStatement preparedStatement;

	public M2ABean(Connection connection) {
		super();
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.m2a.base.M2AInterface#getCollections(java.util.Map,
	 * java.lang.String, java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public List<Object> getCollections(Map<String, Object> inputParameters,
			String queryString, String className, Object dtoClass,
			boolean isProcedure) throws Exception {

		finalResult = new ArrayList<Object>();

		ResultSet resultSet;
		ResultSetMetaData resultSetMetaData;

		// preparing statement and setting parameters
		commonActions(inputParameters, queryString, isProcedure);

		// retrieve the result
		if (isProcedure) {
			resultSet = callableStatement.executeQuery();
		} else {
			resultSet = preparedStatement.executeQuery();
		}

		resultSetMetaData = resultSet.getMetaData();
		int columnsCount = resultSetMetaData.getColumnCount();
		if (resultSet.next()) {
			String columnName;
			String columnClassName;
			int columnType;
			Class columnClass;
			Object value;
			Class resultClass;
			Object resultDTO = null;
			do {
				resultClass = Class.forName(className);
				resultDTO = resultClass.newInstance();

				for (int columnIndex = 1; columnIndex <= columnsCount; columnIndex++) {

					columnName = resultSetMetaData.getColumnName(columnIndex);
					columnClassName = resultSetMetaData
							.getColumnClassName(columnIndex);
					columnClass = Class.forName(columnClassName);
                    columnType = resultSetMetaData.getColumnType(columnIndex);
					value = resultSet.getObject(columnIndex);

					M2AMapper.assignValue(resultDTO, resultClass, columnName,
							columnClass, value,columnType);
				}

				finalResult.add(resultDTO);

			} while (resultSet.next());
		}
		return finalResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.m2a.base.M2AInterface#executeUpdate(java.util.Map,
	 * java.lang.String, boolean)
	 */
	@Override
	public int executeUpdate(Map<String, Object> inputParameters,
			String queryString, boolean isProcedure) throws Exception {

		noOfRecords = 0;

		// preparing statement and setting parameters
		commonActions(inputParameters, queryString, isProcedure);

		// retrieve the result
		if (isProcedure) {
			noOfRecords = callableStatement.executeUpdate();
		} else {
			noOfRecords = preparedStatement.executeUpdate();
		}

		return noOfRecords;
	}

	/**
	 * Creates Statement & passing Parameters to the Statement
	 * 
	 * @param inputParameters
	 *            - Input Parameters
	 * @param queryString
	 *            - Procedure Call/ Prepared Statement
	 * @param isProcedure
	 *            - Procedure/PreparedStatement
	 * @throws Exception
	 */
	private void commonActions(Map<String, Object> inputParameters,
			String queryString, boolean isProcedure) throws Exception {

		if (isProcedure) {
			callableStatement = connection.prepareCall(queryString);
		} else {
			preparedStatement = connection.prepareStatement(queryString);
		}

		// Settings required parameters to the procedure/statement
		if (inputParameters != null && !inputParameters.isEmpty())
			setParameters(inputParameters, isProcedure);

	}

	/**
	 * Passing parameters to Procedure/PreparedStatement
	 * 
	 * @param inputParameters
	 *            - Input Parameters
	 * @param statement
	 *            - CallableStatement/PreparedStatement
	 * @throws Exception
	 */
	private void setParameters(Map<String, Object> inputParameters,
			boolean isProcedure) throws Exception {

		int parameterIndex = 1;

		// Setting Parameters to the Statement
		for (String inputParameter : inputParameters.keySet()) {

			Object inputValue = inputParameters.get(inputParameter);
			inputParameter = String.format("a%C%s", inputParameter.charAt(0),
					inputParameter.substring(1));			
			// Processing Integers
			if (inputValue instanceof Integer) {
				if (isProcedure) {
					callableStatement.setInt(inputParameter, (int) inputValue);
				} else {
					preparedStatement.setInt(parameterIndex, (int) inputValue);
				}
			}

			// Processing Doubles
			if (inputValue instanceof Double) {
				if (isProcedure) {
					callableStatement.setDouble(inputParameter,
							(double) inputValue);
				} else {
					preparedStatement.setDouble(parameterIndex,
							(double) inputValue);
				}
			}
			
			// Processing Long
			if (inputValue instanceof Long) {
				if (isProcedure) {
					callableStatement.setLong(inputParameter,
							(Long) inputValue);
				} else {
					preparedStatement.setLong(parameterIndex,
							(Long) inputValue);
				}
			}
			
			// Processing Big Decimal
			if (inputValue instanceof BigDecimal) {
				if (isProcedure) {
					callableStatement.setBigDecimal(inputParameter,
							(BigDecimal) inputValue);
				} else {
					preparedStatement.setBigDecimal(parameterIndex,
							(BigDecimal) inputValue);
				}
			}


			// Processing Strings
			if (inputValue instanceof String) {
				if (isProcedure) {
					callableStatement.setString(inputParameter,
							(String) inputValue);
				} else {
					preparedStatement.setString(parameterIndex,
							(String) inputValue);
				}
			}

			// Processing Date
			if (inputValue instanceof Date) {
				if (isProcedure) {
					callableStatement.setTimestamp(inputParameter,
							new java.sql.Timestamp(((Date) inputValue).getTime()));
				} else {
					preparedStatement.setTimestamp(parameterIndex,
							new java.sql.Timestamp(((Date) inputValue).getTime()));
				}
			}

			// Processing Blob
			if (inputValue instanceof byte[]) {
				if (isProcedure) {					
					callableStatement
							.setBytes(inputParameter, (byte[]) inputValue);
				} else {
					preparedStatement
					.setBytes(parameterIndex, (byte[]) inputValue);
				}
			}
			parameterIndex++;

		}

	}

	/**
	 * Closing all DB Connections and Statements
	 * 
	 * @throws Exception
	 */
	private void close() throws Exception {

		if (preparedStatement != null) {
			preparedStatement.close();
		}

		if (callableStatement != null) {
			callableStatement.close();
		}

		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public void finalize() {
		try {

			close();
		} catch (Exception e) {

		}
	}

}
