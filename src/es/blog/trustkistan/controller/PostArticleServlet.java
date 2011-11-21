package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.blog.trustkistan.model.Article;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Crea una nueva entrada
 * 
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class PostArticleServlet extends BasicUtilitiesServlet {
	private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	String title = request.getParameter("title");
    	String body = request.getParameter("body");
    	
    	if(title != null && body != null) {
    		Article article = new Article();
    		article.setTitle(title);
    		article.setBody(body.replace("\n","<br>"));   		
    		ServletContext context = session.getServletContext();    		
    		ArticleDAO articles = (ArticleDAO) context.getAttribute("articles");
        	articles.createArticle(article);
        	//addArticleToRSS(article);
        	logger.log(Level.INFO, "Publicado: {0}, el {1}", new Object[]{article.getTitle(),
        			article.getCreationDateAsString()});
        	gotoURL(postForm,request,response);
    	} else {
        	response.sendRedirect(session.getServletContext().getContextPath()+errorForm);
        }
    }
    
    static String escapeXML(String s) {
    	StringBuffer str = new StringBuffer();
    	int len = (s != null) ? s.length() : 0;
        for (int i=0; i<len; i++) {
        	char ch = s.charAt(i);
        	switch (ch) {
        		case '<': str.append("&lt;"); break;
        		case '>': str.append("&gt;"); break;
        		case '&': str.append("&amp;"); break;
        		case '"': str.append("&quot;"); break;
        		case '\'': str.append("&apos;"); break;
        		default: str.append(ch);
        	}
        }
        return str.toString();
    }

}