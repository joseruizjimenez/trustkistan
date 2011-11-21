package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet para gestionar la Excepciones.
 * Las logueara y redirige la peticion a la pagina de error 500
 */
@SuppressWarnings("serial")
public class ManageExceptionServlet extends BasicUtilitiesServlet {
    public final static Logger logger = Logger.getLogger(ManageExceptionServlet.class.getName());
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Exception exception =
                (Exception) request.getAttribute("javax.servlet.error.exception");
        String exceptionSource =
                (String) request.getAttribute("javax.servlet.error.request_uri");
        
        logger.log(Level.SEVERE,"Excepcion en "+exceptionSource,exception);
        gotoURL(exceptionError,request,response);      
    }

}
