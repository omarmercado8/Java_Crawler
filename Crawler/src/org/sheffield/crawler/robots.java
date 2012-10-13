/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package org.sheffield.crawler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class robots.
 * This class is used to parse the Robots txt file
 */
public class robots {
	

	  
	  
	  /**
  	 * Robot.
  	 * It will get and parse the Robots.txt File, looking for the Disallowed sites. It will also take the host of the 
  	 * original site to check for external sites, and if a restricted site is provided it will also get the host of this URL
  	 * It will also add the Host to every link found in this robots file and return a list of all this URL's
  	 * @param URLrobot the uRl robot
  	 * @return the array list
  	 */
  	public static ArrayList<String> robot(String URLrobot){

	  ArrayList<String> response = new ArrayList<String>();

	  try{
	 
		  URL myURL = new URL(URLrobot);

		  URLConnection conn = myURL.openConnection();
		  crawler.lastModified = new Date(conn.getLastModified()).toString();
		   
          /**
           * Get local and restricted Host to be able to compare later
           */
		  
		  crawler.localSite = myURL.getHost();
		  crawler.localSite = "http://" +  crawler.localSite;
	  

	  if(!  crawler.restricted.isEmpty()){
		  URL URLrestricted = new URL( crawler.restricted.get(0).toString());
		  crawler.RestrictedSite = URLrestricted.getHost();
		  crawler.RestrictedSite = "http://" +  crawler.RestrictedSite;

	  }
	  

    URLrobot =  crawler.localSite + "/robots.txt";
    
    
    myURL = new URL(URLrobot);
    
    
    InputStream urlStream = myURL.openStream();
	  byte b[] = new byte[urlStream.available()]; 
    
	  int numRead = urlStream.read(b);
	  String content = new String(b, 0, numRead);
	    
	  Pattern p = Pattern.compile("Disallow:\\s*/[~ | ([a-zA-Z] | [0-9]) | / | .| % | = ]*", Pattern.CASE_INSENSITIVE);//REGEX to look for Disallowed pattern
	  Matcher m = p.matcher(content);

	  while (m.find()) {
		  String link = m.group(0).trim();

	    	
	    	int index = 0;
	    	if ((index = link.indexOf("Disallow:")) != -1) {// if the word Disallow is find in the line
	    	    index += "Disallow:".length();
	    	    link = link.substring(index).trim();
	    	    
	    	    
	    	    if(link.trim().substring(0,1).equals("/")){// Recreate the full URL adding HTTP and the host 
	    	    	link = link.substring(1).trim();
	    	    	link = "http://" + myURL.getHost() +"/"+link;
	    	    }

	    	}
	    	response.add(link);
	  }
	 
	  }catch(Exception ex){System.err.println("robot  "+ex);}
	  return response;
	  }
}