package SEO.BrokenLinks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelData {
	
	public LinkedHashMap<Object, LinkedHashMap<Object, Object>> readExcelData(String filePath, String sheetName) {
		
		LinkedHashMap<Object, LinkedHashMap<Object, Object>> readData = new LinkedHashMap<Object, LinkedHashMap<Object,Object>>();
		
		File file = new File(filePath);
		
		try {
			
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(sheetName);
			
			int lastRow = sh.getLastRowNum()+1;
			int lastColoumn = sh.getRow(0).getLastCellNum();
			
			for(int i=0;i<lastRow;i++) {
				
				Object PrimaryKey = fetchCellValue(sh.getRow(i).getCell(0));
				
				LinkedHashMap<Object, Object> SecondaryHasmMap = new LinkedHashMap<Object, Object>();
				
				for(int j=0;j<lastColoumn;j++) {
					
					Object SecondaryKey = fetchCellValue(sh.getRow(0).getCell(j));
					Object SecondaryValue = fetchCellValue(sh.getRow(i).getCell(j));
					
					SecondaryHasmMap.put(SecondaryKey, SecondaryValue);
					
				}
				
				readData.put(PrimaryKey, SecondaryHasmMap);
			}
			
		} 
		
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (EncryptedDocumentException e) {e.printStackTrace();} 
		catch (InvalidFormatException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		return readData;
	}
	
	public Object fetchCellValue(Cell cell) {
		
		CellType type = cell.getCellTypeEnum();
		
		Object CellValue = null;
		
		if(type.equals(CellType.NUMERIC)) CellValue = cell.getNumericCellValue();
		if(type.equals(CellType.STRING)) CellValue = cell.getStringCellValue();
		if(type.equals(CellType.BOOLEAN)) CellValue = cell.getBooleanCellValue();
		if(type.equals(CellType.BLANK)) CellValue = "blank";
		
		return CellValue;
		
	}
	
}
