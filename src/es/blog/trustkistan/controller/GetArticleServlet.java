package es.blog.trustkistan.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.blog.trustkistan.model.Article;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Obtiene el articulo con la URL dada
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class GetArticleServlet extends BasicUtilitiesServlet {
    //private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String pathInfo = request.getPathInfo();
    	HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        ArticleDAO articleDAO = (ArticleDAO) context.getAttribute("articles");
        Article articleRequested = articleDAO.readArticleFromCleanURL(pathInfo);
        if (articleRequested == null) {
        	gotoURL(errorForm, request, response);
        } else {
        	request.setAttribute("article", articleRequested);
            gotoURL(readArticle, request, response);
        }
    		
    		/*
    		String[] parsedPathInfo = pathInfo.split("/");
    		if(parsedPathInfo.length < 4) {
    			gotoURL(errorForm, request, response);
    		} else {
    			try {
    				int year = Integer.parseInt(parsedPathInfo[0]);
    				int month = Integer.parseInt(parsedPathInfo[1]);
    				int date = Integer.parseInt(parsedPathInfo[2]);
    				
    				HttpSession session = request.getSession();
    	            ServletContext context = session.getServletContext();
    	            ArticleDAO articleDAO = (ArticleDAO) context.getAttribute("articles");
    	            
    				ArrayList<Article> articlesFound =  articleDAO.listArticle(year, month, date);
    				if(articlesFound.isEmpty()) {
    					gotoURL(errorForm, request, response);
    				} else {
    					if(articlesFound.size()==1) {
    						request.setAttribute("article", articlesFound.get(0));
    						gotoURL(readArticle, request, response);
    					} else {
    						if("timeline".equals(parsedPathInfo[3])&&parsedPathInfo.length==5) {
    							for(Article article : articlesFound) {
    								if(article.generateCreationTime().equals(parsedPathInfo[4])) {
    									request.setAttribute("article", article);
        	    						gotoURL(readArticle, request, response);
        	    						break;
    								}
    							}
    							gotoURL(errorForm, request, response);
    						} else {
    							for(Article article : articlesFound) {
    								if(article.generateTitleWithoutSymbols().equals(parsedPathInfo[3])) {
    									request.setAttribute("article", article);
        	    						gotoURL(readArticle, request, response);
        	    						break;
    								}
    							}
    							gotoURL(errorForm, request, response);
    						}
    					}
    				}
    			} catch (NumberFormatException ex) {
    				gotoURL(errorForm, request, response);
    			}
    		}
    		*/
    	
    }
}