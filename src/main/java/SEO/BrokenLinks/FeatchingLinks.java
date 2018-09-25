package SEO.BrokenLinks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FeatchingLinks {
	
	public static Connection connection = null;
	public static Document htmlDocument = null;
	
	public static LinkedHashSet<String> allUniqueLinks;
	public static String requiredURL;
	public static List<String> HomePagelinks = new ArrayList<String>();
	
	public static GenericUtils genericUtils;
	
	public LinkedHashSet<String> uniqueLinks(String PageURL){
		
		genericUtils = new GenericUtils();
		
		String homePageURL = genericUtils.removeEndSlash(PageURL);
		
		connection = Jsoup.connect(homePageURL);
		
		if(connection != null) {
			
			try {
				
				htmlDocument = connection.get();
				
				if(htmlDocument!=null) {
					
					Elements allLinksOnPage = htmlDocument.select("a");
					
					String absURL;
					
					for(Element link: allLinksOnPage) {
						
						String hrefLink = link.attr("href");
						
						absURL = link.absUrl("href");
						
						if(hrefLink.startsWith("/")||hrefLink.startsWith("#")||(!hrefLink.contains("https://")||(!hrefLink.contains("http://")))) {
							
							requiredURL = absURL;
							HomePagelinks.add(requiredURL);
									
						}
						
						else {
							
							HomePagelinks.add(hrefLink);
						}
						
						allUniqueLinks = new LinkedHashSet<String>(HomePagelinks);
						
					}
				}
			}
			
			catch(IOException ioe) {
				
				ioe.printStackTrace();
				
			}
		}
		
		return allUniqueLinks;
	}

}
