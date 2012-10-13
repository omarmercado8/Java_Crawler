/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package org.sheffield.crawler;

import java.sql.*;

// TODO: Auto-generated Javadoc
/**
 * The Class conection.
 * This class is used to create a connection with the DatabBase it requires the paramateres url, userName and password
 * to be set from the servlet
 */
public  class  conection {

	/** The ERROR when the database cannot make a connection */
	private static String ERROR = "Database connection terminated  2";

	/** The url of the Web database */
	public static String url = "";
	
	/** The user name of the Web DataBase */
	public static String userName = "";
	
	/** The password of the Web Database */
	public static String password = "";
	
	
	/**
	 * Gets the conection.
	 *M akes  A MYSQL connection , the catch clause will catch any exception and close any connection
	 * @return the conection
	 */
	public static Connection getConection(){
		Connection conn = null;
	try
	{

	    Class.forName ("com.mysql.jdbc.Driver").newInstance ();
	    conn = DriverManager.getConnection (url, userName, password);

		
	}
	catch (Exception e) {

		System.err.println ("getConection  "+ERROR + e);
	
	if (conn != null) {try {conn.close ();System.out.println ("ERROR");}
	catch (Exception ee) {  }
	}
	}

	return conn;

}
	
	/**
	 * Test connection.
	 * Will try make a connection, if the connection is succesfull it will return ok
	 * else it will return NOCON, the finally method will close every connection open
	 * @return the string
	 */
	public static String testConnection(){
		String res = "OK";
		Connection conn = null;
		try
		{
		Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		conn = DriverManager.getConnection (url, userName, password);
		res = "OK";

			
		}
		catch (SQLException e) {
			res = "NOCON";
			if (conn != null) {
				try {conn.close ();}
			    catch (Exception ee) { System.err.println(e); }
			}
		}
		catch (Exception e) {
			res = "NOCON";
		    if (conn != null) {try {conn.close ();}catch (Exception ee) {  }}
	    	}
		finally{
			try {conn.close ();}catch (Exception ee) {  }
		}
		return res;

	}
}