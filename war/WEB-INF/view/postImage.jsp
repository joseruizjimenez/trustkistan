<%-- 
    Document   : index
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html> 
<html> 
	<head>
		<title>Trustkistan Libre - Post Image</title>
		<meta name="title" content="Trustkistan Libre - Post Image" />
        <%@ include file="/head.html" %> 
    </head> 
	<body>
		<h2>Now... Share your images!</h2>
        <form enctype="multipart/form-data" method ="post" action="<%= blobstoreService.createUploadUrl("/uploadImage") %>">
            <p><input type="file" name="image"></p>
			<p><button>Upload it!</button></p>
		</form> 
    </body> 
</html>
