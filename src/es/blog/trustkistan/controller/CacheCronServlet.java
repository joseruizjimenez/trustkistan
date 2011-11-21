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
 * Ejecuta un Cronjob para actualizar las caches (de articulos)
 * de todas las instancias desplegadas, evitando listas desactualizadas
 * 
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class CacheCronServlet extends BasicUtilitiesServlet {  
    private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	logger.log(Level.FINE,"Comenzando task actualizando cache");
    	HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();
        ArticleDAO articles = (ArticleDAO) context.getAttribute("articles");
        if(articles.updateCachedList()) {
        	logger.log(Level.INFO,"Concluida task actualizando la cache");
        } else {
        	logger.log(Level.WARNING,"Fallo en el task actualizando cache");
        }
    }
}
