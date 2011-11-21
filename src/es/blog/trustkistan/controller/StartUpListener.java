package es.blog.trustkistan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import es.blog.trustkistan.persistence.ArticleDAO;
import es.blog.trustkistan.persistence.ArticlePersistFactory;

/**
 * Listener encargado de establecer la conexion con el sistema de persistencia,
 * asi como sus parametros de configuracion, establecidos en el fichero de propiedades
 * 'webConfigFile' (descrito en el web.xml).
 * 
 * @author Jose Ruiz Jimenez
 */
public class StartUpListener implements ServletContextListener {
    private ArticleDAO articleDAO;

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        String url = "", driver = "", user = "", password = "", persistenceMechanism = "";
        String show = "";
        int showNumber = 5;
        ServletContext context = evt.getServletContext();
        String webConfigFile = context.getInitParameter("webConfigFile");
        InputStream is = context.getResourceAsStream(webConfigFile);
        Properties webConfig = new Properties();
        try {
            webConfig.load(is);
            persistenceMechanism = webConfig.getProperty("persistenceMechanism");
            url = webConfig.getProperty("url");
            
            show = webConfig.getProperty("show");
            context.setAttribute("persistenceMechanism", persistenceMechanism);
            showNumber = Integer.parseInt(show);
            context.setAttribute("show", showNumber);
            if(!persistenceMechanism.equals("file")) {
                driver = webConfig.getProperty("driver");
                user = webConfig.getProperty("user");
                password = webConfig.getProperty("password");
            }
        } catch (IOException ex) {
            Logger.getLogger(StartUpListener.class.getName()).log(Level.SEVERE,
                    "Error cargando los parametros del fichero de configuracion", ex);
        }
        
        Logger.getLogger(StartUpListener.class.getName()).log(Level.INFO,
                "Estableciendo conexion con la BD empleando: {0}...", persistenceMechanism);
        articleDAO = ArticlePersistFactory.getArticleDAO(persistenceMechanism);
        boolean connected = articleDAO.setUp(url, driver, user, password);
        if (!connected) {
            Logger.getLogger(StartUpListener.class.getName()).log(Level.SEVERE, 
                "Error conectando a la BD");
            context.setAttribute("persistenceMechanism", "none");
            context.setAttribute("articles", "none");
        } else {
            Logger.getLogger(StartUpListener.class.getName()).log(Level.INFO, 
                "Conexion con la BD realizada con exito");
            Logger.getLogger(StartUpListener.class.getName()).log(Level.INFO,
                    "Cargadas {0} noticias", articleDAO.listArticle().size());
            context.setAttribute("articles", articleDAO);
            context.setAttribute("showNumber", String.valueOf(showNumber));
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        boolean ok = articleDAO.disconnect();
        if (!ok) {
            Logger.getLogger(StartUpListener.class.getName()).log(Level.SEVERE,
                    "No se encontro el driver para la base de datos");
        } else {
            Logger.getLogger(StartUpListener.class.getName()).log(Level.INFO,
                "Cerrada correctamente la conexion con la base de datos");
        }
    }
}
