/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package org.sheffield.crawler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// TODO: Auto-generated Javadoc
/**
 * The Class sql.
 * This class makes all the connections and querys to the MysQl database
 */
public class sql {

	
	/** The ERROR. */
	static String ERROR = "Database connection terminated  4";
	
	/** The debug. */
	private static boolean debug = false;

/**
 * Select.
 * 
 * It receives a select query and returns only the first element found, this is used by the crawler to know what method to go next
 *
 * It has a catch and a finally method that closes every connection
 * 
 * @param q the q
 * @return the string
 */
public static String select(String q){
	
	   Connection conn = null;
	    conn = conection.getConection();

		
		PreparedStatement st = null;
		String response = "";
		ResultSet rs = null;
		
		try{
			
		st = conn.prepareStatement(q.toString());
		rs = st.executeQuery();
		
		if(rs.next()){
			response = rs.getString("url");
		}

		}catch(Exception ex){
			
			System.err.println(ex);
			
			if (conn != null) {try {conn.close ();System.out.println ("select " +ERROR);}catch (Exception e) {  }}
		}
		
		finally {	
			if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
			}
		return response;
	}
	
	/**
	 * Insert.
	 * it receives a query and a list of URLS, this will be added to the different tables, depending of the query received, if the table is 
	 * history or links it will check for the title using JSOUP and for the date of last modified ussing the httpconnection property. If its 
	 * an external or invalid link it will not check for this properties it will only insert the URL in the table
	 * 
	 * It has a catch and a finally method that closes every connection
	 *
	 * @param q the q
	 * @param list the list
	 * @param table the table
	 */
	public static void insert(String q, ArrayList<String> list, String table){

		Connection conn = null;
		conn = conection.getConection();
		URLConnection connn = null;
		URL myURL = null;
		Document doc = null;
		String title = null;
		PreparedStatement st = null;
		
		try{
		for(int x = 0; x<list.size(); x++){

			
			if(table.equals("links")||table.equals("history")){
			myURL = new URL(list.get(x).toString());
			connn = myURL.openConnection();	
		    HttpURLConnection HTTPcon = (HttpURLConnection) myURL.openConnection();
		   
		      
		      if(HTTPcon.getResponseCode() != 200){
		    	  String queryInvalid = "INSERT INTO invalid (url) VALUES (?)";
		    	  st = conn.prepareStatement(queryInvalid.toString());
		    	  st.setString(1, list.get(x).toString());
		      }else{		      
			        doc = Jsoup.connect(list.get(x).toString()).get();
			        title = doc.title();
			        st = conn.prepareStatement(q.toString());
			        st.setString(1, list.get(x).toString());
			        st.setString(2, title.toString());
			        st.setString(3, new Date(connn.getLastModified()).toString());
		          }
			}else{
				st = conn.prepareStatement(q.toString());
				st.setString(1, list.get(x).toString());
			}

		    st.executeUpdate();

		}

		}catch(Exception ex){     

			
			myURL=null; connn= null;if (conn != null) {try {conn.close ();System.out.println (q + ERROR+ ex);
			System.out.println(ex.getStackTrace().toString());
			}catch (Exception e) {  }} 
		}
		finally {	
			myURL=null; connn= null;if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
			}		
	}
	
	/**
	 * Delete.
	 * It receives a delete query and deletes and executes it, its always a delete query with a Where condition
	 * 
	 * It has a catch and a finally method that closes every connection
	 *
	 * @param q the q
	 * @param list the list
	 */
	public static void delete(String q, ArrayList<String> list){
		
		Connection conn = null;
		conn = conection.getConection();
	

		PreparedStatement st = null;
		try{
	
		st = conn.prepareStatement(q.toString());
		
		if (list.size()>0){
		st.setString(1, list.get(0));	
		}
		st.executeUpdate();
	
		
		}catch(Exception ex){System.err.println(ex);
			if (conn != null) {try {conn.close ();System.out.println ("delete" +ERROR);}catch (Exception e) {  }}
		}
		finally {	
			if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
			}
	}
	
	/**
	 * Select all.
	 * Select all the elements and all the properties, this is used when the display button is pressed
	 * 
	 * It has a catch and a finally method that closes every connection
	 * 
	 * @param q the q
	 * @return the array list
	 */
	public static ArrayList selectAll(ArrayList q){

		    Connection conn = null;
		    conn = conection.getConection();
			PreparedStatement st = null;
			ArrayList response = new ArrayList();
			HashMap result = new HashMap();
			ResultSet rs = null;
			
			try{
				
		    for(int x=0; x< q.size();x++){
		    
			   st = conn.prepareStatement(q.get(x).toString());
			   rs = st.executeQuery();
			
			   while(rs.next()){				
				
				   result.put("url", rs.getString("url"));
				   result.put("title", rs.getString("title"));
				   result.put("lastpagemodified", rs.getString("lastpagemodified"));
				   result.put("lastcrawl", rs.getString("lastcrawl"));
				   
				   response.add(result.clone());
				   result.clear();  
			   }
		    }
		
			}catch(Exception ex){System.err.println(ex);
				if (conn != null) {try {conn.close ();System.out.println (ERROR);}catch (Exception e) {  }}
			}
			finally {	
				if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
				}
			return response;
		}
	
	
	/**
	 * Select alldisplay.
	 * 
	 * Returns only the URL of the select query received, this is used to export the results to the TXT files
	 * 
	 * It has a catch and a finally method that closes every connection
	 *
	 * @param q the q
	 * @return the array list
	 */
	public static ArrayList selectAlldisplay(ArrayList q){

		PreparedStatement st = null;
		ArrayList response = new ArrayList();
		ArrayList result = new ArrayList();
		ResultSet rs = null;
		Connection conn = null;
		
		try{			
		    conn = conection.getConection();
			
	    for(int x=0; x< q.size();x++){
	
		   st = conn.prepareStatement(q.get(x).toString());
		   
		   rs = st.executeQuery();
		
		   while(rs.next()){				
			   response.add(rs.getString("url"));	   
		   }
		   
	    }

		}catch(Exception ex){System.err.println(ex);
			if (conn != null) {try {conn.close ();System.out.println (ERROR);}catch (Exception e) {  }}
		}
		finally {	
			if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
			}
		return response;
	}
	
	
	/**
	 * Delete all.
	 * 
	 * This is used to delete everything from all the tables, it receives and arraylist with the querys
	 * 
	 * It has a catch and a finally method that closes every connection
	 *
	 * @param q the q
	 */
	public static void deleteAll(ArrayList q){
		
		    Connection conn = null;
		    conn = conection.getConection();
			PreparedStatement st = null;
			try{

				for(int x=0; x< q.size();x++){
					st = conn.prepareStatement(q.get(x).toString());
					st.executeUpdate();
				}
		    
			}catch(Exception ex){  if(debug){System.err.println(ex);}
				if (conn != null) {try {conn.close ();System.out.println (ERROR);}catch (Exception e) {  }}
			}
			finally {	
				if (conn != null) {try {conn.close ();}catch (Exception e) {  }}
				}
		}

}