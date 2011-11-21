package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.blog.trustkistan.model.Article;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Obtiene la lista de posts pertenecientes a esa pagina
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class GetPageServlet extends BasicUtilitiesServlet {  
    //private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String pathInfo = request.getPathInfo().substring(1);
        if(pathInfo == null) {
            gotoURL(errorForm,request,response);
        } else {
            try {
                int pageNumber = Integer.parseInt(pathInfo);
                if(pageNumber <=0) {
                	gotoURL(errorForm,request,response);
                } else {
                	HttpSession session = request.getSession();
                	ServletContext context = session.getServletContext();
                	int showNumber = (Integer) context.getAttribute("show");
                	ArticleDAO articleDAO = (ArticleDAO) context.getAttribute("articles");
                	ArrayList<Article> articleList = articleDAO.listArticle();
                	double paginate = (double) articleList.size()/showNumber;
                	if(paginate>pageNumber-1) {
                		ArrayList<Article> articlesPage = new ArrayList<Article>();
                		int articleIndex = articleList.size()-(pageNumber*showNumber)+showNumber-1;
                		for(int i=articleIndex;i>=0 && articleIndex-i<showNumber;i--) {
                			articlesPage.add(articleList.get(i));
                		}
                		request.setAttribute("articlesPage", articlesPage);
                		request.setAttribute("pageNumber", String.valueOf(pageNumber));
                		gotoURL(pageArticle,request,response);
                	} else {
                		gotoURL(errorForm,request,response);
                	}
                }
            } catch (NumberFormatException ex) {
                gotoURL(errorForm,request,response);
            }
        }
    }
}
