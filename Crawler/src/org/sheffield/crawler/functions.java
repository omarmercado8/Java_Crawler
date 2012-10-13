/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package org.sheffield.crawler;


import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class functions.
 * 
 * Each function corresponds to a button pressed.
 */
public  class  functions{
	
	
	  /**
  	 * Stop.
  	 *
  	 *When the stop button is pressed, the links table will be deleted, leaving only the visited links, 
  	 *external and invalid links with data
  	 *
  	 * @return the string
  	 */
  	public static String stop(){
		  String response="";
		  String query="";
		  try{
			  ArrayList n = new ArrayList();
			  query = "DELETE FROM links ";
			  sql.delete(query, n);

		  }catch(Exception ex){
			System.err.println(ex);    
		  }
		  return response;
	  }
	  
	  /**
  	 * Delete all.
  	 *
  	 * Every time the crawler starts it will go to this method to delete all links from all the tables
  	 * @return the string
  	 */
  	public static String deleteAll(){
		  String response="";
		  String query="";
		  try{
			  ArrayList n = new ArrayList();
			  query = "DELETE FROM links ";
			  sql.delete(query, n);
			  query = "DELETE FROM history ";
			  sql.delete(query, n);
			  query = "DELETE FROM externallinks ";
			  sql.delete(query, n);
			  query = "DELETE FROM invalid";
			  sql.delete(query, n);

		  }catch(Exception ex){
			System.err.println(ex);    
		  }
		  return response;
	  }
	  
	  /**
  	 * Display.
  	 * 
  	 * When the display button is pressed this method will return all the links that have been visited, this are stored in the history table
  	 * @return the array list
  	 */
  	public static ArrayList display(){

		  ArrayList querys = new ArrayList();
		  ArrayList response = new ArrayList();
		  String query1 = "SELECT  url,title,lastpagemodified,lastcrawl FROM history ";
		  try{
			  querys.add(query1);

			  response = sql.selectAll(querys);

		  }catch(Exception ex){System.err.println(ex);}
		  
		  return response;
	  }

	 /**
 	 * Write file.
 	 *
 	 * Depending on the requested table by the JSP, it will return all the links from a specified table
 	 * @param option the option
 	 * @return the array list
 	 */
 	public static ArrayList writeFile(String option){
		  
		  ArrayList querys = new ArrayList();
		  ArrayList results = new ArrayList();
		  ArrayList n = new ArrayList();
		  try{
		

			  if(option.equals("history")){
			  String query1 = "SELECT  url FROM history ";	  querys.add(query1);}
			  if(option.equals("invalid")){
			  String query2 = "SELECT  url FROM invalid "; querys.add(query2);}
			  if(option.equals("external")){
			  String query3 = "SELECT  url FROM externallinks ";  querys.add(query3);}

			  results = sql.selectAlldisplay(querys);
			 
		  }catch(Exception ex){System.err.println("Error "+ex);	   }
		  return results; 
	  }

}