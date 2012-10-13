/*
 * Project :  Web Crawler
 * Date :  18/03/2012
 * Team : crawlerIW
 * 
 */
package iwebcrawler;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sheffield.crawler.conection;
import org.sheffield.crawler.crawler;
import org.sheffield.crawler.functions;

import com.google.gson.Gson;


/**
 * Servlet implementation class IWebCrawler
 */
public class IWebCrawler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IWebCrawler() {
        super();
        // TODO Auto-generated constructor stub
    }


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		/**
		 * 
		 * Parameters requested from the HTML files
		 * 
		 */
		    String url = request.getParameter("url");
		    String rurl = request.getParameter("rurl");
		    String maxrequest = request.getParameter("maxrequest");
		    String webdb = request.getParameter("webdb");
		    String user = request.getParameter("user");
		    String pass = request.getParameter("pass");
		    String action = request.getParameter("action");

			
			
			/**
			 * Create instance of the crawler 
			 */
		    crawler crawler = new crawler(); 
		    
			/**
			 * Create our GSon object and set our response in json 
			 */
		    Gson json = new Gson();  
		    response.setContentType("application/json");
		    
		    PrintWriter out = response.getWriter();	   
		    
	
		    
					/**
					 * Actions send by the HTML file
					 * pause will change the value of the action variable in the crawler causing it to stop the while loop
					 */
		         
		    

		    if(action.equals("pause")){
			    /**
			     *  pause will change the value of the action variable in the crawler causing it to stop the while loop
			     */
		         crawler.Action = "pause";		    
		    
		             }else if (action.equals("start")){
		            	 /**
		     		     *  Start action will start a new instance of the crawler, it will set the DB information to make a connection
		     		     *  if the previous action is equal to Start means a instance of the crawler was already running, it will change the action
		     		     *  to stop, wait 2 seconds and start again. If a restricted URL has been provided it will set this value to the crawler variable
		     		     *   
		     		     */
		    	
		            	 conection.url = webdb;
		    	         conection.userName  = user;
		    	         conection.password  = pass;
		    	         crawler.maxrequest =  Integer.valueOf(maxrequest);
		    	    	 functions.deleteAll();
		    	         if(crawler.Action.equals("start")){
		    		
		    	        	 crawler.Action = "stop";
		    	             try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								
								e.printStackTrace();
							}
		    		         crawler.Action = "start";	
		    		        
		    	             if(!rurl.trim().equals("")){crawler.restricted.add(rurl);}  			    
		    		         crawler.init(url);
		    		         
		    	          }else{
		    		
		    	        	  crawler.Action = "start";		    	
		    		          if(!rurl.trim().equals("")){crawler.restricted.add(rurl);}  			    
		    		          crawler.init(url);		    	
		    	            }
		    	    crawler.restricted.clear();
		    	    crawler.Action = "";
		    	
	                  }else if (action.equals("display")){ 
	          		    /**
	      			     *  display action, first it will check if the connection is available, if its a 
	      			     *  connection available it will return a list of all the URL visited, if a connection is not available it will return NOCON 
	      			     *  and promop the user to create a new connection
	      			     */
	                	  
			   
	                	  String res = conection.testConnection();
	                	  
	                	  if(!res.equals("OK")){
	                		
	                		  out.write(json.toJson(res));
	                		  
	                	  }else{
	                		  out.write(json.toJson(functions.display()));  
	                	  }
	                	  
	                	  
		   
	                       }else if(action.equals("export")){

	               		    /**
	           			     *  export is not used anymore trough the servlet at the moment
	           			     */
		    
	                       }else if(action.equals("stop")){
	               		    /**
	           			     *  Stop will change the action value causing the crawler to stop, and restart all the 
	           			     *  values, then it will erase the tables
	           			     */
			   
	                    	   crawler.Action = "stop";
			                   functions.stop();
			                   crawler.restricted.clear();
		    	               crawler.Action = "";
	                       } else if(action.equals("resume")){
	               		    /**
	           			     *  resume will first check that theres no instance of the crawler running, if it is it will stop it, wait 500 mls and start again
	           			     *  it will not send any url to the init method, causin the crawler to start from the last available links, 
	           			     *  so restarting when it left before
	           			     */
			   
	                    	   if(crawler.Action.equals("start")){
		    		               
	                    		   crawler.Action = "stop";
		    		               try{
		    		                  Thread.sleep(1000);
		    		               }catch(Exception ex){System.out.println(""  +ex);}
		    		               crawler.Action = "start";		    				    
		    		               crawler.init("");

		    	               }else{
		    		                 crawler.Action = "start";		    			    
		    	                 	crawler.init("");		    	
		    	                 }
		                 	 crawler.restricted.clear();
		    	             crawler.Action = "";
		                     }  else if(action.equals("testCon")){

		             		    /**
		         			     *  Test Con will check if its possible to make a new connection with the values already provides or with new values,
		         			     *  It will return NOCON if no connection was possible and OK if a new connection is possible
		         			     */
		                    	 
		                    	 if(webdb !=null&&user!=null&&pass!=null){
				                    		 
		                    	 conection.url = webdb;
				    	         conection.userName  = user;
				    	         conection.password  = pass;
				    	         out.write(json.toJson(conection.testConnection()));
		                    	 }else{
				    	         out.write(json.toJson(conection.testConnection()));
		                    	 }

		                     }
		    
	                 }

           }