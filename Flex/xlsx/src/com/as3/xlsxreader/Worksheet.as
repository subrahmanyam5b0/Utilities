package com.as3.xlsxreader
{
	import mx.collections.ArrayCollection;
	import mx.utils.StringUtil;
	
	import spark.formatters.DateTimeFormatter;

	/**
	 * A wrapper class for an individual XLSX Worksheet loaded through an XLSXLoader object
	 * Instances of this class are created by the XLSX.worksheet function.
	 * 
	 * 
	 * 
	 * @example Loading an Excel file and reading a cell:
	 * <listing version="3.0">
	 * 
	 * //Create the Excel Loader
	 * var excel_loader:XLSXLoader=new XLSXLoader();
	 * 
	 * //Listen for when the file is loaded
	 * excel_loader.addEventListener(Event.COMPLETE,function (e:Event) {
	 * 
	 *   //Access a worksheet by name ('Sheet1')
	 *   var sheet_1:Worksheet=excel_loader.worksheet("Sheet1");    
	 *   
	 *   //Access a cell in sheet 1 and output to trace
	 *   trace("Cell A3="+sheet_1.getCellValue("A3")) //outputs: Cell A3=Hello World;
	 * 
	 * 
	 * });
	 * 
	 * //Load the file
	 *excel_loader.load("Example Spreadsheet.xlsx");
	 *
	 * </listing>
	 *
	 */
	public class Worksheet
	{
		
		private var xml:XML;
		private var styles:XML;
		private var fileLink:XLSXLoader;
		private var ns:Namespace;
		private var sheetName:String;		 
		private var _columnsArray:ArrayCollection;/* =new ArrayCollection (['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
															,'AA','AB','AC','AD','AE','AF','AG','AH','AI','AJ','AK','AL','AM','AN','AO','AP','AQ','AR','AS','AT','AU','AV','AW','AX','AY','AZ']);*/
		
		private var _columns:Array;
		
		private  const  columnsSource:ArrayCollection =new ArrayCollection (['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']);
		
		private var _data:ArrayCollection;		
		
		private var _rowCount:Number;
		
		private var  _columnCount:Number;
		   
		public var _dateFormatString:String="";
		
		/**
		 * Creates a new Worksheet Object from a file loader. This consturctor is designed to be called from the XLSXLoader.worksheet() function only
		 * 
		 * @param sSheetName worksheet name
		 * @param FileLink link to the XLSX Loader
		 * @param input the worksheet as XML
		 * 
		 */     
		public function Worksheet(sSheetName:String,FileLink:XLSXLoader,input:XML)
		{
			sheetName=sSheetName;
			xml=input;
			fileLink=FileLink;
			ns=fileLink.getNamespace();
			default xml namespace=ns;		
			_columns = new Array();			
			_data = new ArrayCollection();
			
			_rowCount = xml.sheetData.row.length();
			
			//processing for valid rows
			for(;_rowCount>0;_rowCount--){
			  var _temp:Array = getRowValues(_rowCount);
			  if(_temp !=null && _temp.length>0)
				  break;
			}
			
			_columnCount= 0;
			var prevCol:String = "";			
			for(var _row:int=1;_row <= rowCount;_row++) {				
			var _tempCount:int = xml.sheetData.row.(@r == _row ).c.length();
			var x:XML = xml.sheetData.row.(@r == _row ).c[_tempCount-1] as  XML ;
			var _column:String=x.@r.toString().match(/[A-Z]+/)[0];						
			_column = _column.toUpperCase();
			if(_row==1) {
				prevCol = 	_column; 
			}				
				if(_column>prevCol)
					prevCol = _column;
				}			
			_columnsArray = new ArrayCollection();
			generateColumns(prevCol.length);
			trace(""+prevCol+":"+_columnsArray.length);
			_columnCount = _columnsArray.getItemIndex(prevCol)+1;
			
			//getWorkSheetData();
		}
		
		private function  generateColumns(length:int):void{
			switch(length) {
				case 1: addColumns("");
					     break;
				case 2:
					for(var i:int =0 ;i < columnsSource.length;i++) {
								addColumns(columnsSource.getItemAt(i));						
							}		
					break;
				case 3:
					for(var i:int =0 ;i < columnsSource.length;i++) {
						for(var j:int =0 ;j < columnsSource.length;j++) {
						   addColumns(columnsSource.getItemAt(i)+""+columnsSource.getItemAt(j));			
						}	
					}
					break;
			}
		}
		
		private function addColumns(prefix:Object):void {
			for(var i:int =0 ;i < columnsSource.length;i++) {
				_columnsArray.addItem(prefix+columnsSource.getItemAt(i));
			}
		}
		
		/**
		 *   Returns the file data as a ArrayCollection
		 *  @return data as ArrayCollection
		 */  
		public function  getWorkSheetData():ArrayCollection{
			var rowData:Object;	
			
			for(var _row:int=1;_row<=_rowCount;_row++){
			     	rowData = new Array();
			          for(var _col:int=0;_col<_columnCount;_col++) {
						  rowData[_columnsArray[_col]]= getCellValue(_col,_row);
					  }
					  _data.addItem(rowData);					  
			}
			return data;
		}
		
		/**
		 * Returns the first row of the Sheet, as a columns
		 * 
		 * @return first row data as a Array  
		 */
		public function get columns():Array{	
			if(_data.length > 0) 
				_columns = _data.getItemAt(0) as Array;
			return _columns;
		}
			
		/**
		 *  Return whole data of the worksheet.
		 * @return data as a ArrayCollection
		 */ 
		public function get data():ArrayCollection{
			if(_data.length == 0 && _rowCount > 0) {
				getWorkSheetData();
			}
			return _data;
		}
		
		/**
		 *    Returns the Number of rows available in the sheet
		 *  
		 *  @return the no of rows as Number
		 */
		public  function get rowCount():Number{			
			return _rowCount ;
		}
		
		/**
		 *    Returns the Number of columns in the sheet.Row 1
		 *  
		 *  @return the no of Columns as Number
		 */
		public  function get columnCount():Number{			
			return _columnCount as Number;
		}
		
		/**
		 * Returns an XML representation of the worksheet
		 * 
		 * @return the worksheet as XML
		 * 
		 */     
		private function toXML():XML
		{
			return xml;
		}
		
		/**
		 * Gets the XML representation of a single cell
		 * 
		 * @param cellRef a standard spreadsheet single cell reference (e.g. "A:3")
		 * @return the cell value as XML
		 * 
		 */ 
		private function getCell(cellRef:String):XMLList
		{
			cellRef=cellRef.toUpperCase();
			var row:Number=Number(cellRef.match(/[0-9]+/)[0]);
			var column:String=cellRef.match(/[A-Z]+/)[0];
			//trace("getCell:"+cellRef, row, column);			
			return getRows(column,row,row);
		}
		/**
		 * Gets the String value of a single cell
		 * 
		 * @param cellRef a standard spreadsheet single cell reference (e.g. "A:3")
		 * @return the cell value as a string
		 * 
		 */ 
		private function getValue(cellRef:String):String
		{
			default xml namespace=ns;
			var xml:XMLList=getCell(cellRef);			
			if(xml.v.valueOf())
			{					
				return xml.v.valueOf();
			}else{
				return null;
			}
		}	
		
		
		private function parseDate(time:Number):Object{
			var seconds:Number=(time - 25569) * 86400.0;			
			var _da:Date = new Date(seconds * 1000);
			_da=new Date(_da.fullYear,_da.month,_da.date,_da.hours,_da.minutes,_da.seconds);
			if(_dateFormatString == "" || StringUtil.trim(_dateFormatString) == "" ) {
			  return _da;
			}
			else  {
				try{
					_da=new Date(_da.fullYear,_da.month,_da.date,_da.hours-5,_da.minutes-30,_da.seconds>0?_da.seconds+1:_da.seconds);
					var _form:DateTimeFormatter= new DateTimeFormatter();
					_form.dateTimePattern = _dateFormatString;
						//return	DateField.dateToString(_da,_dateFormatString);
					return _form.format(_da);
				}
				catch(e:Error){
					return _da;
				}
			}
			return _da;
		}
		
		/**
		 * Gets the String value of a single cell
		 * 
		 * @param row row number
		 * @param col column number
		 * @return the cell value as a string
		 * 
		 */ 
		public function getCellValue(col:int,row:int):String
		{
			if(row > _rowCount || col > _columnCount-1){
				return null;
			}
		   	return getValue(_columnsArray.getItemAt(col)+""+row);			
		}
			
		private function getRawRows(column:String="A",from:Number=1,to:Number=-1):XMLList
		{
			// returns the  raw (ie shared strings not converted) 
			//rows in a given column within a certain range
			default xml namespace=ns;
                        if(to==-1)
				to=xml.sheetData.row.length();						
			return xml.sheetData.row.(@r>=from && @r<= to).c.(@r.match(/^[A-Z]+/)[0]==column);
		} 
		/**
		 * Provides an XML list representation of a range of rows in a given column as a list of xml v tags
		 * 
		 * @param column the column name e.g. "A"
		 * @param from the row number to start at e.g. 1
		 * @param to the row number to end at e.g. 10
		 * @return an XMLList of the requested rows in a single column as a list of xml v tags
		 * 
		 */     
		private function getRows(column:String="A",from:Number=1,to:Number=2000):XMLList
		{
			// returns the  converted (ie shared strings are converted) 
			//rows in a given column within a certain range
			return fillRowsWithValues(getRawRows(column,from,to));
		}
		
		/**
		 * Provides an XML list representation of a range of rows in a given column as a list of xml v tags
		 *		 
		 * @param from the row number  e.g. 1		 
		 * @return an XMLList of the requested row  as a list of xml v tags
		 * 
		 */
		public function getRowValues(row:Number=1):Array{
			if(row > _rowCount) {
				return null;
			}
			var co:XMLList = xml.sheetData.row.(@r == row).c;
			var _tempData:Array = new Array();
			for each(var temp:Object in co) {
				//temp.v = fileLink.sharedString(temp.v.toString());
				if(StringUtil.trim(temp.v.valueOf().toString()).length>0) {
					_tempData.push(temp.v.valueOf());
					break;
				}
			}
			return _tempData;
		}
		
		/**
		 * Provides an XML list representation of a range of rows in a given column as a list of xml values
		 * 
		 * @param column the column name e.g. "A"
		 * @param from the row number to start at e.g. 1
		 * @param to the row number to end at e.g. 10
		 * @return an XMLList of the requested rows in a single column as a list of xml values
		 * 
		 */ 
		private function getRowsAsValues(column:String="A",from:Number=1,to:Number=2000):XMLList
		{
			// returns the  converted (ie shared strings are converted) 
			//values in a given column within a certain range
			default xml namespace=ns;
			return getRows(column,from,to).v;
		}
		
		private function rowsToValues(rows:XMLList):XMLList
		{
			//converts a set of rows to values
			default xml namespace=ns;
			return fillRowsWithValues(rows).v;
		}
		
		private function fillRowsWithValues(rows:XMLList):XMLList
		{
			default xml namespace=ns;
			// takes a set of rows and inserts the correct values
			var copy:XMLList=rows.copy();
			//trace("r:"+copy);
			for each (var item:Object in copy)
			{	
				//trace("t:"+item+":"+item.f+":"+item.v);
				if(item.f.(children().length()!=0)+""=="") // If it's the result of a formula, no need to replace
				{					
					if(item.@t=="str")
						item.v=fileLink.sharedString(item.v.toString());
					
					else if(item.@t=="s")
						item.v=fileLink.sharedString(item.v.toString());
					
					else if(item.@t != "s" ) {
							var _tempData:Object = item.v.valueOf();				  
							if( copy.@s != undefined 
								&& _tempData.toString().length > 0 
								//&& _tempData.toString().indexOf(".") == -1 
							) {
								item.v = this.parseDate(parseFloat(""+item.v.valueOf())).toString();
								
							}					  
						}					
				}
			}
			return copy;
		}
		public function toString():String
		{
			return xml.toString();
		}
		public function toXMLString():String
		{
			return xml.toXMLString();
		}
		
	}
}
