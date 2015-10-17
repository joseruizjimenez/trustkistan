<%-- 
    Document   : index
    Created on : 02-jul-2011, 10:25:28
    Author     : Jose Ruiz Jimenez
--%>

<%@page import="es.blog.trustkistan.model.Article"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="articles" scope="application" type="es.blog.trustkistan.persistence.ArticleDAO" />
<jsp:useBean id="showNumber" scope="application" class="java.lang.String" />
<jsp:useBean id="pageNumber" scope="request" class="java.lang.String" />
<jsp:useBean id="articlesPage" scope="request" type="java.util.ArrayList<Article>" />
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml" xml:lang="en" lang="en"> 
	<head>
		<title>Trustkistan Libre - <%= pageNumber %></title>
		<meta name="title" content="Trustkistan Libre - <%= pageNumber %>" />
		<%@ include file="/head.html" %>
		<link rel="canonical" href="http://trustkistan.appspot.com/page?n=<%= pageNumber %>" />
		<%@ include file="/analytics.html" %>
    </head> 
	<body>
        <header>
            <%@ include file="/header.html" %>
		</header>
		<%@ include file="/like.html" %>
        <%if(articlesPage != null && !articlesPage.isEmpty()) {
            for(Article article : articlesPage) { 
                if(article != null) { %>
                    <article>
                    <h2><div class="title"><a href="http://trustkistan.appspot.com/article<%=article.getCleanURL()%>"><%=article.getTitle()%></a></div></h2>
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
                                <p><a href="http://trustkistan.appspot.com/article<%=article.getCleanURL()%>">CONTINUE READING!</a></p>
							<% } else { %>
                               <%=article.getBody()%> 	
							<% } %>							
					</div></h3>
					<div class="date"><a href="http://trustkistan.appspot.com/article<%=article.getCleanURL()%>"><%=article.getCreationDateAsString()%></a></div>
					<div class="social"><g:plusone size="small" href="http://trustkistan.appspot.com/article<%=article.getCleanURL()%>"></g:plusone><fb:like href="http://trustkistan.appspot.com/article<%=article.getCleanURL()%>" layout="button_count" send="false" show_faces="false" action="like" ></fb:like></div>
                    </article>
                <%}
            } %>
            <h4>
            <%if(Integer.parseInt(pageNumber) != 1) {%>
                <a href="http://trustkistan.appspot.com/page/<%=Integer.parseInt(pageNumber)-1%>">Newer &nbsp;&nbsp </a>
            <%}  
        if(Integer.parseInt(showNumber)*Integer.parseInt(pageNumber)<articles.listArticle().size()) { %>
            <a href="trustkistan.appspot.com/page/<%=Integer.parseInt(pageNumber)+1%>"> &nbsp&nbsp Older</a>
		<% } }%></h4>
		<%@ include file="/plusone.html" %>
        <footer>
            <%@ include file="/footer.html" %>
		</footer>
	</body>
</html>
