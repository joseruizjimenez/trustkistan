<%-- 
    Document   : index
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@page import="es.blog.trustkistan.model.Article"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="article" scope="request" type="es.blog.trustkistan.model.Article" />
<jsp:useBean id="admin" scope="session" class="java.lang.String" />
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml" xml:lang="en" lang="en"> 
	<head>
		<title>Trustkistan Libre - <%=article.getTitle()%></title>
		<meta name="title" content="Trustkistan Libre - <%=article.getTitle()%>" />
		<%@ include file="/head.html" %>
		<link rel="canonical" href="http://www.trustkistan.com/article<%=article.getCleanURL()%>" />
		<%@ include file="/analytics.html" %>
    </head> 
	<body>
        <header>
            <%@ include file="/header.html" %>
		</header>
		<%@ include file="/like.html" %>
        <%if(article != null) { %>
                    <article>
                        <h2><div class="title"><a href="http://www.trustkistan.com/article<%=article.getCleanURL()%>"><%=article.getTitle()%></a></div></h2>
                        <% for(String urlImg : article.getImageURLs()) { %>
                        <div class="img"><img src="<%=urlImg%>" /></div>
                        <%} %>
                        <h3><div class="body"><%=article.getBody()%></div></h3>
						<div class="date"><%=article.getCreationDateAsString()%></div>
						<div class="social"><g:plusone size="small"></g:plusone><fb:like href="http://www.trustkistan.com/article<%=article.getCleanURL()%>" layout="button_count" send="false" show_faces="false" action="like" ></fb:like></div>
						<div class="comments"><fb:comments href="http://www.trustkistan.com/article<%=article.getCleanURL()%>" num_posts="3" width="500"></fb:comments></div>
					</article>
					<h4><a href="http://www.trustkistan.com"> Back </a></h4>
					<% if(admin != null && "true".equals(admin)) { %>
					<a href="/delete?id=<%=article.getIdAsString()%>"><img src="http://trustkistan.appspot.com/img/delete.gif" style="margin-left:8px" height="15" width="15"/></a>
					<% } %>
		<%} %>
		<%@ include file="/plusone.html" %>
        <footer>
            <%@ include file="/footer.html" %>
		</footer>
	</body>
</html>
