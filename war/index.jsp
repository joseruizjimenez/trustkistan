<%-- 
    Document   : index
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@page import="es.blog.trustkistan.model.Article"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="articles" scope="application" type="es.blog.trustkistan.persistence.ArticleDAO" />
<jsp:useBean id="showNumber" scope="application" class="java.lang.String" />
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml" xml:lang="en" lang="en"> 
	<head>
		<title>Trustkistan Libre</title>
		<meta name="title" content="Trustkistan Libre" />
		<%@ include file="/head.html" %>
		<link rel="canonical" href="http://www.trustkistan.com" />
		<%@ include file="/analytics.html" %>
    </head> 
	<body>
		<header>
            <%@ include file="/header.html" %>
		</header>
		<%@ include file="/like.html" %>
        <%for(int i= 0;i<Integer.parseInt(showNumber)&&i<articles.listArticle().size();i++) {
                Article article = articles.listArticle().get(articles.listArticle().size()-1-i);
                if(article != null) { %>
                    <article>
                        <h2><div class="title"><a href="http://www.trustkistan.com/article<%=article.getCleanURL()%>"><%=article.getTitle()%></a></div></h2>
                        <% for(String urlImg : article.getImageURLs()) { %>
                        <div class="img"><img src="<%=urlImg%>" /></div>
                        <%} %>
						<h3><div class="body">
							<% if(article.getBody().length() > 850) {
								int returnPos = article.getBody().substring(749,850).lastIndexOf("<br>");
								if(returnPos != -1) { %>
                                    <%=article.getBody().substring(0,749+returnPos)%> ...
								<% } else { %>
                                    <%=article.getBody().substring(0,750)%> ...
								<% } %>								
                                <p><a href="http://www.trustkistan.com/article<%=article.getCleanURL()%>">CONTINUE READING!</a></p>
							<% } else { %>
                               <%=article.getBody()%> 	
							<% } %>							
						</div></h3>
						<div class="date"><a href="http://www.trustkistan.com/article<%=article.getCleanURL()%>"><%=article.getCreationDateAsString()%></a></div>
						<div class="social"><g:plusone size="small" href="http://www.trustkistan.com/article<%=article.getCleanURL()%>"></g:plusone><fb:like href="http://www.trustkistan.com/article<%=article.getCleanURL()%>" layout="button_count" send="false" show_faces="false" action="like" ></fb:like></div>
                    </article>
                <%}
        } 
        if(Integer.parseInt(showNumber)<articles.listArticle().size()) { %>
        <h4><a href="http://www.trustkistan.com/page/2"> Older</a></h4>
		<% } %>
		<%@ include file="/plusone.html" %>
        <footer>
            <%@ include file="/footer.html" %>
		</footer>
	</body>
</html>
