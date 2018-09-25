package SEO.BrokenLinks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Reports {
	
	GenericUtils genericUtils;
	ExcelData data;
	Constants constant;
	
	public static List<String> links;
	public static LinkedHashMap<Object, LinkedHashMap<Object,Object>> read;
	
	public void writeToExcel(String sheetname) throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		genericUtils = new GenericUtils();
		data = new ExcelData();
		read = data.readExcelData(constant.Read_filePath, constant.Read_DataSheet);
		String currentURL = (String) read.get("URL").get("Data");
		
		links = new ArrayList<String>();
		links.addAll(ValidatingLinks.validlinksForReports);
		links.addAll(ValidatingLinks.brokenlinksForReports);
		links.addAll(ValidatingLinks.anotherDomainlinks);
		
		int allLinks = links.size();
			
		int[] seperators = new int[10];
		
		FileInputStream fis = new FileInputStream(Constants.Read_filePath);
		
		Workbook wb = WorkbookFactory.create(fis);
		
		Sheet sh = wb.getSheet(sheetname);
		
		int lastRowNumber = sh.getLastRowNum();
		
		//Deleting Additional Rows from previous Results
		
		try {
			
			for(int i=1;i<lastRowNumber;i++) {
				
				if(!sh.getRow(i).getCell(0).equals(null)) {
					
					Row removeRow = sh.getRow(i);
					sh.removeRow(removeRow);
				}
			}
			
		}
		
		catch (NullPointerException N) {
			
			System.out.println("Excel Cell is empty, Hence Proceding with Report Creation");
			
		}
			
		for(int row=1; row<=allLinks; row++) {
					
			String allURL = links.get(row-1);
			seperators = genericUtils.orIndex(allURL);
			
			Row r = sh.createRow(row);
			
			Cell cell = r.createCell(0);
			cell.setCellValue(allURL.substring(0, seperators[0]));
			
			cell = r.createCell(1);
			cell.setCellValue(allURL.substring(seperators[0]+1,seperators[1]));
			
			cell = r.createCell(2);
			String parentURL = allURL.substring(seperators[1]+1,seperators[2]);
			cell.setCellValue(allURL.substring(seperators[1]+1,seperators[2]));
			
			cell = r.createCell(3);
			
			if(!currentURL.equals(parentURL)) {
				cell.setCellValue(2);
			}
			
			else {
				cell.setCellValue(1);
			}
			
			cell = r.createCell(4);
			cell.setCellValue(allURL.substring(seperators[2]+1, seperators[3]));
			
			cell = r.createCell(6);
			cell.setCellValue(allURL.substring(seperators[3]+1));
						
		}
		
		FileOutputStream fos = new FileOutputStream(Constants.Read_filePath);
		wb.write(fos);
		fos.close();
		
	}
	
}
