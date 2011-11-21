package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Borra el articulo con ese id
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class DeleteArticleServlet extends BasicUtilitiesServlet {
    private static final Logger logger = Logger.getLogger(DeleteArticleServlet.class.getName());

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null) {
            gotoURL(errorForm, request, response);
        } else {
            HttpSession session = request.getSession();
            ServletContext context = session.getServletContext();
            ArticleDAO articleDAO = (ArticleDAO) context.getAttribute("articles");
            if (articleDAO.deleteArticle(id)) {
                logger.log(Level.INFO, "Borrado el articulo: {0}", id);
                gotoURL(frontPage, request, response);
            } else {
                logger.log(Level.SEVERE, "Error borrando el articulo: {0}", id);
                gotoURL(errorForm, request, response);
            }
        }
    }
}