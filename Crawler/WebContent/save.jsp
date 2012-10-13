<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="org.sheffield.crawler.functions;" %>

    
<%

functions f = new functions();
String filename= request.getParameter("filename");// Dinamically change the filename of the TXT file
String option= request.getParameter("option");//Get the requested Table
response.setContentType("application/plain");
response.setHeader("Content-Disposition", "inline;filename=\"" + filename + "\"");   
ArrayList data = new ArrayList();
data.addAll(f.writeFile(option));//Add all the results to the ArrayList
%><%
if(data.size() < 0){  String Error= "No data Available OR no Connection available ";
}else{
for(int x= 0; x<data.size();x++ ){  // Print every url as a List in to the File
%><%=data.get(x)%>
<%}}%>
