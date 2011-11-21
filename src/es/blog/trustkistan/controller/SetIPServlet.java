package es.blog.trustkistan.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * Registra la IP del servidor
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class SetIPServlet extends BasicUtilitiesServlet {
	private DatastoreService datastore;
    private static final Logger logger = Logger.getLogger(SetIPServlet.class.getName());
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String newIPAddress = request.getRemoteAddr();
    	Calendar updateTime = Calendar.getInstance();
        updateTime.clear();
        updateTime.setTimeInMillis(System.currentTimeMillis());
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	datastore = DatastoreServiceFactory.getDatastoreService();
    	Query q = new Query("serverAddress");
    	PreparedQuery pq = datastore.prepare(q);
    	Entity result = pq.asSingleEntity();
    	if(result == null) {
    		Entity serverAddress = new Entity("serverAddress");
        	serverAddress.setProperty("ip",newIPAddress);
        	serverAddress.setProperty("timestamp", dateFormat.format(updateTime.getTime()));
        	datastore.put(serverAddress);
        	logger.log(Level.INFO, "Grabando IP del Server: {0}", newIPAddress);
    	} else {
    		datastore.delete(result.getKey());
    		Entity serverAddress = new Entity("serverAddress");
        	serverAddress.setProperty("ip",newIPAddress);
        	serverAddress.setProperty("timestamp", dateFormat.format(updateTime.getTime()));
        	datastore.put(serverAddress);
        	logger.log(Level.INFO, "Actualizando IP del Server: {0}", newIPAddress);
    	}
    }
}
