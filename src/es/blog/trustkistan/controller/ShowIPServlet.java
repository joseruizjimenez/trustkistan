package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Sirve la IP del que realiza la consulta
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class ShowIPServlet extends BasicUtilitiesServlet {
	private DatastoreService datastore;
    private static final Logger logger = Logger.getLogger(ShowIPServlet.class.getName());
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	datastore = DatastoreServiceFactory.getDatastoreService();
    	Query q = new Query("serverAddress");
    	PreparedQuery pq = datastore.prepare(q);
    	Entity result = pq.asSingleEntity();
    	String ipAddress = "IP aun no registrada.";
    	String timestamp = "El servidor no ha contactado aun...";
    	if(result != null) {
    		ipAddress = (String)result.getProperty("ip");
    		timestamp = (String)result.getProperty("timestamp");
    	}
    	request.setAttribute("ipAddress", ipAddress);
    	request.setAttribute("timestamp", timestamp);
    	logger.log(Level.INFO, "Solicitan la IP del Server desde: {0}", request.getRemoteAddr());
    	gotoURL(showIP, request, response);
    }
}
