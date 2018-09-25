package SEO.BrokenLinks;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class BrokenLinks {
	
	public static ValidatingLinks links;
	public static ExcelData excelData;
	public static Reports reports;
	public static LinkedHashMap<Object, LinkedHashMap<Object,Object>> read;
	
	FeatchingLinks fetchLinks;
	
	public static Constants constant;

	public static void main(String[] args) throws RuntimeException, InvalidFormatException, IOException {
		
		constant = new Constants();
		
		excelData = new ExcelData(); 
		
		reports = new Reports();
		
		read = excelData.readExcelData(constant.Read_filePath, constant.Read_DataSheet);
		
		double depth = (Double) read.get("Depth").get("Data");
		
		String currentURL = (String) read.get("URL").get("Data");
		
		links = new ValidatingLinks();
		
		if(depth==1) {
			
			links.validateLinks(currentURL);
			
		}
		
		if(depth==2) {
			
			int validLinksSize = 0;
			
			if(ValidatingLinks.validlinks.isEmpty()) {
				
				links.validateLinks(currentURL);
				
				validLinksSize = ValidatingLinks.validlinks.size();
			}
			
			System.out.println("*************Number Of Valid Links Indentified are: "+validLinksSize);
			
			for(int i=0;i<validLinksSize;i++) {
				
				FeatchingLinks.HomePagelinks.clear();
				
				if(!(ValidatingLinks.validlinks.get(i).equals(currentURL))) {
						
					currentURL = ValidatingLinks.validlinks.get(i).toString();
					
					System.out.println("");
					
					System.out.println("*************CURRENT URL THAT IS BEING VALIDATED IS: "+ currentURL);
						
					links.validateLinks(currentURL);
					
				}
				
			}
			
		}
		
		if(!ValidatingLinks.brokenlinks.isEmpty()) {
			
			System.out.println("");
			
			System.out.println("************************FOLLOWING ARE THE BROKEN LINKS**************************************");
			
			for(int i=0;i<ValidatingLinks.brokenlinks.size();i++) {
				
				System.out.println(ValidatingLinks.brokenlinks.get(i).toString());
			}
		}
		
		else {
			
			System.out.println("*****************THERE WERE NO BROKEN LINKS FOR THESE WEBPAGES******************************");
		}
		
		reports.writeToExcel(Constants.Read_ReportSheet);
		
	}
	
}
