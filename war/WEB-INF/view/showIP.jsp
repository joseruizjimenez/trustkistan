<%-- 
    Document   : ShowIP
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="ipAddress" scope="request" class="java.lang.String" />
<jsp:useBean id="timestamp" scope="request" class="java.lang.String" />

<!DOCTYPE html> 
<html> 
	<head>
		<title>Trustkistan Libre - Show IP</title>
		<meta name="title" content="Trustkistan Libre - Show IP" />
        <%@ include file="/head.html" %> 
    </head> 
    <body>
        <header>
            <%@ include file="/header.html" %>
        </header>
        <h2><a href="http://trustkistan.appspot.com.com/ip">Minecraft Server: Diogenes!</a></h2>
        <h3><div class="body">
                La IP del servidor es:   <div class="ip"><%=ipAddress%>:443</div></br>
                o prueba:   <div class="ip"><%=ipAddress%>:110</div></br>
                Y un ejemplo de página web (VentaBilletesAutobus):   <div class="ip"><%=ipAddress%>:80</div></br>
                (last update: <%=timestamp%> +1 hora)
    </div></h3></br></br>        
        <footer>
            <%@ include file="/footer.html" %>
        </footer> 
    </body> 
</html>
