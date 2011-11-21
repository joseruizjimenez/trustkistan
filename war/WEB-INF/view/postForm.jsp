<%-- 
    Document   : index
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html> 
<html> 
	<head>
		<title>Trustkistan Libre - Post</title>
		<meta name="title" content="Trustkistan Libre - Post" />
        <%@ include file="/head.html" %> 
    </head> 
    <body>
        <header>
            <%@ include file="/header.html" %>
        </header>
        <h2>Share your story!</h2>
        <form method ="post" action="/post">
            <p><input name="title" value="" type="text" size="62"/></p>
            <p><textarea name="body" cols="48" rows="10"></textarea></p>
			<p><button>Post it!</button></p>
		</form>
		<iframe width="100%" height="175" src="/uploadImage"></iframe>        
        <footer>
            <%@ include file="/footer.html" %>
        </footer> 
    </body> 
</html>
