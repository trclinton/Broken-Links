package SEO.BrokenLinks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class ValidatingLinks {
	
	public static List<String> anotherDomainlinks = new ArrayList<String>();
	public static List<String> validlinks = new ArrayList<String>();
	public static List<String> brokenlinks = new ArrayList<String>();
	public static List<String> validlinksForReports = new ArrayList<String>();
	public static List<String> brokenlinksForReports = new ArrayList<String>();
	
	public static LinkedHashMap<Object, LinkedHashMap<Object,Object>> read;
	public static LinkedHashSet<String> uniqueLinks;
	
	ExcelData data;
	FeatchingLinks fetchLinks;
	Constants constant;
	GenericUtils genericUtils;
	
	HttpURLConnection huc;
	int respCode = 200;
	
	public void validateLinks(String homePageURL){
		
		genericUtils = new GenericUtils();
		
		data = new ExcelData();
		fetchLinks = new FeatchingLinks();
		uniqueLinks = fetchLinks.uniqueLinks(homePageURL);
		
		System.out.println("*************Number Of Unique Links Identified are: "+ uniqueLinks.size());
		
		read = data.readExcelData(constant.Read_filePath, constant.Read_DataSheet);
		
		String currentURL = genericUtils.removeEndSlash((String) read.get("URL").get("Data"));
		
		String pageTitle = (String) read.get("Title").get("Data");
						
		Iterator<String> it = uniqueLinks.iterator();
						
		while(it.hasNext()) {
			
			String links = it.next();
			
			if(links == null || links.isEmpty()){
				
				System.out.println("URL is either not configured for anchor tag or it is empty");
				continue;
				
			}
			
			if(!links.startsWith(currentURL)&&(!links.startsWith("http://time.com"))
					&&(!links.startsWith("http://money.com"))
					&&(!links.startsWith("http://fortune.com"))
					&&(!links.startsWith("http://www.meredith.com"))
					&&(!links.startsWith("https://www.magazine.store"))
					&&(!links.startsWith("http://mytime.timeinc.com"))
					&&(!genericUtils.pageTitle(links).contains(pageTitle.toLowerCase()))){
				
				anotherDomainlinks.add(links+"|"+"URL belongs to another domain, skipping it."+"|"+homePageURL+"|"+"N/A"+"|"+"Not Executed");
				System.out.println(links+ ": URL belongs to another domain, skipping it.");
				continue;
				
			}
			
			try {
				
				huc = (HttpURLConnection)(new URL(links).openConnection());
			}
			
			catch(IOException e) {
				
				e.printStackTrace();
				brokenlinks.add(links);
				brokenlinksForReports.add(links+"|"+"IO exception"+"|"+homePageURL+"|"+"N"+"|"+"FAIL");
				
			}
			
			try {
				
				huc.setRequestMethod("GET");
			}
			
			catch(ProtocolException e) {
				
				e.printStackTrace();
				brokenlinks.add(links);
				brokenlinksForReports.add(links+"|"+"Protocol exception"+"|"+homePageURL+"|"+"N"+"|"+"FAIL");
				
			}
			
			try {
				
				huc.connect();
				respCode = huc.getResponseCode();
				
				if(respCode == 200 || respCode == 301 || respCode == 302){
					
					System.out.println(links+" is a valid link");
					validlinks.add(links);
					validlinksForReports.add(links+"|"+" "+"|"+homePageURL+"|"+"Y"+"|"+"PASS");
					
				}
				
				else {
					
					System.out.println(links+" is a broken link with Response Code: " + respCode);
					brokenlinks.add(links);
					brokenlinksForReports.add(links+"|"+"Recieved status code other than 200"+"|"+homePageURL+"|"+"N"+"|"+"FAIL");
					
				}
				
			}
			
			catch (IOException e) {
				
				e.printStackTrace();
				brokenlinks.add(links);
				brokenlinksForReports.add(links+"|"+"IO exception occurred"+"|"+homePageURL+"|"+"N"+"|"+"FAIL");
				
			}
			
			validlinks.removeAll(anotherDomainlinks);
			
		}
	}

}
