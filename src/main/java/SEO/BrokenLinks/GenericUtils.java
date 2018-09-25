package SEO.BrokenLinks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GenericUtils {
	
	List<String> domainLinks;
	
	ExcelData data;
	Constants constant;
	public static LinkedHashMap<Object, LinkedHashMap<Object,Object>> read;
	
	public int[] orIndex(String links) {
		
		char[] charValidURL = links.toCharArray();
		
		int[] allIndex = new int[10];
		
		int count = 0;
		
		for(int i=0;i<charValidURL.length;i++) {
			
			if(charValidURL[i] == '|') {
				
				allIndex[count++] = i;
			}
		}
		
		return allIndex;
	}
	
	public List<String> allDomainLinks(){
		
		domainLinks = new ArrayList();
		data = new ExcelData();
		read = data.readExcelData(constant.Read_filePath, constant.Read_DomainSheet);
		Object[] links = read.keySet().toArray();
		
		for (int i=1;i<links.length;i++)
			domainLinks.add(links[i].toString());
	
		return domainLinks;
			
	}
	
	public String removeEndSlash(String PageURL) {
		
		String homePageURL = PageURL;
		
		try {
			
			if(homePageURL.endsWith("/")) {
				
				int indexOfSlash;
				indexOfSlash = homePageURL.lastIndexOf("/");
				homePageURL = homePageURL.substring(0, indexOfSlash);
				
			}
		} 
		
		catch (Exception e) {
			
			System.out.println("No / found at the End of URL");
		}
		
		return homePageURL;
	}
	
	public String pageTitle(String link) {
		
		Document doc;
		String title = "";
		
		try {
			
			if(!link.startsWith("mailto")) {
			
			doc = Jsoup.connect(link).get();
			title = doc.title();
			
			}
		} 
		
		catch (IOException e) {
			
			System.out.println("Page Title Not Valid: "+link);
		}
		
		return title;
	}
	
}
