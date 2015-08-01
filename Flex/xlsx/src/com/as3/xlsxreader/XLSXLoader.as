/**
 *  @author VVNSubrahmanyam S
 * 
 */ 
package com.as3.xlsxreader
{	
	import deng.fzip.FZip;
	import deng.fzip.FZipFile;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.net.URLRequest;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	
	[Event(name="complete",type="flash.events.Event")]
	
	/**
	 * A class to load a Microsoft Excel 2007+ .XLSX Spreadsheet (described here: http://en.wikipedia.org/wiki/Office_Open_XML) 
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
	 * //load file using Byte Array
	 *excel_loader.loadFromByteArray(event.target.data as ByteArray);
	 * </listing>
	 *
	 */
	public class XLSXLoader extends EventDispatcher
	{
		private var zipProcessor:FZip =new FZip();
		
		public static var openXMLNS:Namespace=new Namespace("http://schemas.openxmlformats.org/spreadsheetml/2006/main");
		
		private var file:String="none";
		private var sharedStringsCache:XML;
		private var sharedStylesCache:XML;
		private var manifestCache:XML;
		private var worksheetCache:Dictionary=new Dictionary();
		
		default xml namespace=openXMLNS;
		
		/**
		 * A class to load a Microsoft Excel 2007+ .XLSX Spreadsheet  
		 * 
		 * (described here: http://en.wikipedia.org/wiki/Office_Open_XML)
		 * 
		 * @example Loading an Excel file and reading a cell: 
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
		 *   trace("Cell A3="+sheet_1.getCellValue(1,1)) //outputs: Return 2 nd column of 1 Row.
		 * 
		 * 
		 * });
		 * 
		 * //Load the file
		 *excel_loader.load("Example Spreadsheet.xlsx");
		 * //load file using Byte Array
		 *excel_loader.loadFromByteArray(event.target.data as ByteArray);
		 * </listing>
		 *
		 */
		public function XLSXLoader()
		{
			sharedStylesCache = new XML();
			zipProcessor.addEventListener("complete",completed);
			zipProcessor.addEventListener("ioError",function(e:IOErrorEvent):void{trace("ZIP Processor IO error:");trace(e)});
			
		}
		/**
		 * Returns the openxml namespace as a Namespace object
		 * 
		 * @return the openxml namespace as a Namespace object
		 * 
		 */     
		public function getNamespace():Namespace
		{
			return openXMLNS;
		}
		
		/**
		 * Load a spreadsheet from a valid file path or URI
		 *
		 * @param sFile A String specifying a valid file or URI.
		 *
		 */
		public function load(sFile:String):void
		{
			file=sFile;
			addEventHandlers();
			zipProcessor.load(new URLRequest(sFile));
			//this.sharedStyles();
		}
		
		/**
		 * Load a spreadsheet from a valid Byte Array
		 *
		 * @param bytes byte array of the file data
		 *
		 */
		public function loadFromByteArray(bytes:ByteArray, fileSrc:String = "From Byte Array"):void
		{
			file=fileSrc;
			addEventHandlers();
			zipProcessor.loadBytes(bytes);
		 	this.sharedStyles();
		}
		
		
		internal function completed(e:Event):void
		{
			//trace("'"+file+"' unzipped and loaded: " +zipProcessor.getFileCount()+ ' files are inside the xlsx');
		}
		
		/**
		 * Gets the named worksheet from the loaded spreadsheet as a new com.childoftv.xlsxreader.Worksheet Object
		 * 
		 * @param wName a valid worksheet name within the loaded spreadsheet
		 * @return a Worksheet object
		 * 
		 */     
		public function worksheet(wName:String):Worksheet
		{
			
			default xml namespace=openXMLNS;
			if (! manifestCache)
			{
				manifestCache=retrieveXML("xl/workbook.xml");
			}
			var ret:Worksheet;
			var val:*=manifestCache..sheet.(@name==wName);
			var index:Number=val.childIndex();
			
			ret= worksheetbyId(index+1,wName);
			return ret;
		}
		
		/**
		 * Tests whether the provided name is the name of a worksheet in the loaded spreadsheet.
		 * 
		 * @param wName String with the name of a worksheet
		 * @return Returns true
		 * 
		 */     
		public function isSheetName(wName:String):Boolean
		{
			default xml namespace=openXMLNS;
			if (! manifestCache)
			{
				manifestCache=retrieveXML("xl/workbook.xml");
			}
			
			return Boolean(manifestCache.sheets.sheet.(@name==wName).length() > 0);
		}
		
		/**
		 * returns names of sheets in xlsx
		 *
		 * @return Returns Vector.<String> sheet names
		 */
		public function getSheetNames():Vector.<String>
		{
			default xml namespace=openXMLNS;
			if (! manifestCache)
			{
				manifestCache=retrieveXML("xl/workbook.xml");
			}
			
			var sheetNames:Vector.<String> = new Vector.<String>();
			for each(var sheetName:String in manifestCache.sheets.sheet.@name)
			sheetNames.push(sheetName);
			
			return sheetNames;
		}
		
		private function worksheetbyId(id:Number,wName:String="name not available"):Worksheet
		{
			
			if (! worksheetCache[id])
			{
				worksheetCache[id]=new Worksheet(wName,this,retrieveXML("xl/worksheets/sheet"+id+".xml"));			
			}
			return worksheetCache[id];
			
		}
		
		/**
		 * @private  
		 * 
		 * Looks up the internal shared styles database XML
		 * 
		 */     
		internal function sharedStyles():XML
		{
			if (! sharedStylesCache)
			{
				sharedStylesCache=retrieveXML("xl/styles.xml");
			}
			return sharedStylesCache;
		}
		
		/**
		 * @private  
		 * 
		 * Looks up the internal shared string database XML
		 * 
		 */     
		internal function sharedStrings():XML
		{
			if (! sharedStringsCache)
			{
				sharedStringsCache=retrieveXML("xl/sharedStrings.xml");
			}
			return sharedStringsCache;
		}
		
		/**
		 * @private  
		 * 
		 *Retrieves a specific shared string
		 * 
		 */ 
		internal function sharedString(index:String):String
		{
			default xml namespace=openXMLNS;
			if (index==""||! index)
			{
				return "";
			}
			else
			{
				
				
				return sharedStrings().child(index).t.toString();;
			}
		}
		private function retrieveXML(path:String):XML
		{			
			var file:FZipFile=zipProcessor.getFileByName(path);		
			return convertToOpenXMLNS(file.getContentAsString(false));			
		}
		
		
		private function convertToOpenXMLNS(s:String):XML
		{			
			XML.ignoreProcessingInstructions=true;
			var XMLDoc:XML=XML(s);						
			XMLDoc.normalize();
			return XMLDoc;
		}
		
		/**
		 * @private  
		 */ 
		protected function defaultHandler(evt:Event):void
		{
			dispatchEvent(evt.clone());
		}
		/**
		 * @private  
		 */ 
		protected function defaultErrorHandler(evt:Event):void
		{
			trace(evt);
			close();
			dispatchEvent(evt.clone());
		}
		/**
		 * @private  
		 */ 
		protected function addEventHandlers():void
		{
			
			zipProcessor.addEventListener(Event.COMPLETE, defaultHandler);
		}
		
		/**
		 * @private  
		 */ 
		protected function removeEventHandlers():void
		{
			zipProcessor.removeEventListener(Event.COMPLETE, defaultHandler);
			
		}
		/**
		 * Closes the open xlsx file and frees the available memory 
		 * 
		 */         
		public function close():void
		{
			if (zipProcessor)
			{
				
				removeEventHandlers();
				zipProcessor.close();
				zipProcessor=null();
				manifestCache=null;
				worksheetCache=null;
			}
		}
		
		private function getDateBuiltInDateFormat(index:int):String{
			switch (index){
				case 14:
					return ("mm-dd-yy");
				case 15:
					return ("d-mmm-yy");
				case 16:
					return ("d-mmm");
				case 17:
					return ("mmm-yy");
				case 18:
					return ("h:mm AM/PM");
				case 19:
					return ("h:mm:ss AM/PM");
				case 20:
					return ("h:mm");
				case 21:
					return ("h:mm:ss");
				case 22:
					return ("m/d/yy h:mm");
				case 45:
					return ("mm:ss");
				case 46:
					return ("[h]:mm:ss");
				case 47:
					return ("mmss.0");
				default:
					return ("");
			};
		}
		
		
	}
}