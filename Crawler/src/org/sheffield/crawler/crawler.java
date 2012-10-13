/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package org.sheffield.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// TODO: Auto-generated Javadoc
/**
 * The Class crawler.
 * Main class of the project
 */
public class crawler {
	
	/** The current links. */
	private static ArrayList currentLinks = new ArrayList();
	
	/** The external links. */
	private static ArrayList externalLinks = new ArrayList();
	
	/** The Action variable to control the crawler While loop */
	public static String Action = "";
	
	/** Host of the the local site  */
	public static String localSite = "";
	
	/** Host of the Current site. */
	public static String CurrentSite = "";
	
	/** Host of the restricted URL */
	public static String RestrictedSite = "";
	
	/** The restricted. */
	public static ArrayList restricted = new ArrayList();
	
	/** The robot.txt list of invalid Sites */
	public static ArrayList robot = new ArrayList();
	
	/** The invalid links. */
	public static ArrayList invalidLinks = new ArrayList();
	
	/** The visited links. */
	public static ArrayList visitedLinks = new ArrayList();
	
	/** The Maximum number of requests per second  */
	public static int maxrequest = 3;
	
	/** The last modified. */
	public static String lastModified="";
	 

	

  /**
   * Extract links.
   * With the URL it receives using JSOUP it will parse the HTML and retrieve all the available links, this are going to be stored in List
   * to be able to identified duplicate links the last letter is checked, and it will take the last / if it has one. then all those links 
   * are going to be stored in a hashset. By definition a hashset doesn't store duplicate elements, after that we have a new ArrayList with
   * the whole list of links without duplicate elements.
   * If no connection can be made to the valid URL then it will be inserted in to the invalid links table, in the catch method
   *
   * @param url the url
   * @return the array list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static ArrayList<String>extractLinks(String url) throws IOException {
    final ArrayList<String> result = new ArrayList<String>();
    final ArrayList<String> result2 = new ArrayList<String>();
 
    Document doc = null;
    try{
    org.jsoup.Connection con = Jsoup.connect(url);
    setHeaders(con);
    doc = con.get();
  
    }catch(Exception ex){
    	
    	   ArrayList inval = new ArrayList();
    	  String q = "INSERT INTO invalid (url) VALUES (?)";		  
	      sql.insert(q, inval,"invalid");
    	return result;
    
    }
    
    
  Elements links = doc.select("a[href]");


  for (Element link : links) {
    result2.add(link.attr("abs:href")); //List of Links in the URL
  }


  HashSet res = new HashSet();
  

  for(int x=0; x< result2.size();x++){
	  String link = result2.get(x).toString().trim();

	  if(link.substring(link.length()-1).equals("/")){// taking the / out of the last character to be able to compare
		  link = link.substring(0,link.length()-1);

		  res.add(link);//Add to HashSet to automtically remove duplicates
	  }else{
		  
		  res.add(link);//Add to HashSet to automtically remove duplicates
	  }
  
  
  
  }
  
  result.addAll(res);// Add all links to the ArrayLIst

    return result;
  }
  


  /**
   * Sets the headers.
   *  The method receives the Connection created by JSOUP and with this it establish the headers to comply 
   *  with the good net citizen rules
   * @param con the new headers
   */
  public static void setHeaders(org.jsoup.Connection con){
	  
	  try{

	  con.header("Accept", "*/*");
	  con.header("Accept-Language", "enus");
	  con.header("Content-Type","application/x-www-form-urlencoded");
	  con.header("Accept-Encoding", "gzip,deflate");
	  con.header("User-Agent", "Test Crawler for University Module (Student) ommercadocasillas1@sheffield.ac.uk");

	  }catch(Exception ex){}
	  
  }
  
  
  /**
   * Check external links.
   *
   * This method will receive an arraylist of URL's, it will then check if they contain the
   * string of the localSite established before, if the URL does not contain this string then it 
   * will be marked as a external link, an arraylist of every external link is created, at the end the o
   * Original list get all the external links removed by using RemoveAll option.The same method and functionality 
   * will be used to check if a restricted file has been submitted also checking with the String Contains function.
   * At the end we have a list of only valid links, without external or restricted URL's
   *  
   * @param list the list
   * @return the array list
   */
  public static ArrayList checkExternalLinks(ArrayList list){
	  
	  ArrayList external = new ArrayList();
	  ArrayList restrict = new ArrayList();

	  for(int x = 0; x< list.size(); x++){
	  
		  String link = list.get(x).toString();//Check if each link has the local site context
		  
		  int index = 0;

    	    	if((index = link.indexOf(localSite)) == -1){
    	    		external.add(link);	    	    		
    	    	}
	  }

	  external.removeAll(externalLinks);// remove all elements from  the externalLinks list

	  if(external.size() > 0){
		  String q = "INSERT INTO externallinks (url) VALUES (?)";		  
	      sql.insert(q, external,"external");// Insert to external links
	      externalLinks.addAll(external);
	  }
	  list.removeAll(externalLinks);

	  if (!restricted.isEmpty()){
		  for(int x = 0; x< list.size(); x++){ //Check if every element has the restricted site, if one was supplied 
			  String link = list.get(x).toString();  
	
			  int index = 0;
	    	    	if((index = link.indexOf(RestrictedSite)) == -1){
	    	    		restrict.add(link);	    	    		
	    	    	}
		  }
		  list.removeAll(restrict); 
	  }

	  return list;
  }
  
  /**
   * Insert new links.
   *
   * All the links available in the ArrayList will be sent to the insert method, all this links 
   * will be inserted into the links table to be visited later
   * But first the list will be checked to look for external or restricted links
   * 
   * @param list the list
   */
  public static void insertNewLinks(ArrayList list){

	list =   checkExternalLinks(list);
	 
	 if(list.size() > 0){
	  String query = "INSERT INTO links (url,title,lastpagemodified) VALUES(?,?,?)";
	  sql.insert(query, list,"links");
	 }
  }
  
  /**
   * Insert history.
   *
   * The visited link will be added to the History table by sending it to the insert method
   * @param site the site
   */
  public static void insertHistory(String site){
	  
	  ArrayList<String> list = new ArrayList<String>();
	  if(site.trim().length() > 0){
		  list.add(site);
		  String query = "INSERT INTO history (url,title,lastpagemodified) VALUES(?,?,?)";
		  sql.insert(query, list,"history");
		 }
  }
  
  /**
   * Delete previous.
   *
   * The current visited link will be deleted from the links table so it can go to the next link, 
   * if the same link is twice in the list it will also be deleted. This is a second way we check for duplicate links
   * 
   * @param site the site
   */
  public static void deletePrevious(String site){
	  
	  ArrayList<String> list = new ArrayList<String>();
	  if(site.trim().length() > 0){
		  list.add(site);
		  String query = "DELETE FROM links WHERE url = ? ";
		  sql.delete(query, list);
		 }
  }
  
  
  /**
   * Crawl.
   * 
   * Main part of the Crawling,first it will take the valid URL and send it to the ExtractLinks method where it will be 
   * getting all the available links second it will go to get the robot TXT and ensures none of our links are in our links list,
   * the next step is to remove from this list all the list we have already visited in the session.then remove all known external links 
   * from the list and all previously visited links. Whats left of this list will be checked by the  inserlinks method and only insert valid links
   * into the links table.
   * After that it will insert the original link into the history links, and deleted this link from the links table.At the end it will insert all 
   * these list into the visited list
   * 
   * @param site the site
   * @return the string
   */
  public static String crawl(String site){
	  
	  ArrayList list = new ArrayList();
	   
	  try{

	   list = crawler.extractLinks(site);// Get list ofURL available 

	    if(!list.isEmpty()){

	    	if(!site.trim().equals(localSite.trim())){

	    		robot = robots.robot(site);}// Get list of robot.txt directories

	    	if(!robot.isEmpty()){

	    		for(int x=0; x<robot.size();x++)
	    		{
	    			
	    			for(int y=0; y<list.size();y++){
	    				if(list.get(y).toString().trim().contains(robot.get(x).toString().trim())){//compare each element against the robots.txt rules
	    					list.remove(y);
	    				}	    					    				
	    			}
	    		}
	    		
	    	}

	    list.removeAll(currentLinks);
	    list.removeAll(externalLinks);
	    list.removeAll(visitedLinks);
	    insertNewLinks(list);// Go to insert new links(before it will check for external and restricted)
	    currentLinks.addAll(list);// Add full valid list to the currentlinks list to remove all visited links later
	    insertHistory(site);//insert to history table
	    deletePrevious(site);// delete from links the link already visited
	    visitedLinks.add(site);// add to already visited lins list to never visit the same link twice

	    }else{ deletePrevious(site);visitedLinks.add(site);}
	  }catch(Exception ex){System.err.println("crawl() "+ex);}

	  return "";
  }
  
  /**
   * Init
   *  This method controls what link is going to be visited, is the first method of the crawler. If no link is provided at the begining that
   *  means then the crawler is starting after been paused, it will go to the links table and start from there. If a links is supplied then it
   *  will start from this link, after that it will go into the while loop.
   *  The while loop will check that more links are available in the links table, and that the action variable is set to Start, if a Pause or Stop 
   *  action is submited the servlet will change this variable and the crawler will exit from the while loop.
   *  Before it enters the while loop the time will be checked, and if the  number of requests is bigger than the maxrequests value it will restart 
   *  the time and the number of reuests, if the number of requests is bigger and the time is less than 1 seccond then
   *  the crawler will sleep for the difference until it  get to 1 second.
   *
   * @param firstLink the first link
   */
  public static void init(String firstLink){
	  
	  String nextLink = "";
	  String query = "SELECT  url FROM links ";

	  
	  if(firstLink.toString().trim().length()>0){// if no start link is supplied it will check for the list of valid links(Only happens when the Paused button is pressed)
	  
		  crawl(firstLink);
	      nextLink = sql.select(query);

	  }else{
		  nextLink = sql.select(query);
	  }
	  
	  
	  //Variables used to check the number of requests made every secon
	  int request = 1;
	  long StartTime = new Date().getTime(); 
	  
	  
	  while(!nextLink.trim().equals("") && Action.equals("start")){
	try{
		
		if(!visitedLinks.contains(nextLink))//check link against list of visited links
	     {
		  crawl(nextLink);  }
		
		  
		  nextLink = sql.select(query);
		  request++;
		 
		  long EndTime = new Date().getTime();
		  long dif = EndTime-StartTime;
		 
		  if(request > maxrequest){ request =1;StartTime = new Date().getTime();// restart values 
				  if(dif<1000){ 
			  
			  try {
				   Thread.sleep(1000-dif);
			         } catch (InterruptedException e) {
				       e.printStackTrace();
		        	}
			  }
		  }
	  }
	  catch(Exception ex){System.err.println(ex);}

  }
  }
}