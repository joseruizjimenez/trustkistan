package es.blog.trustkistan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import es.blog.trustkistan.model.Article;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Adjunta una imagen subida al articulo.
 * 
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class AddImageServlet extends BasicUtilitiesServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession();
        // Comprobamos que se nos manda un formulario multipart/form-data
        if(ServletFileUpload.isMultipartContent(request)) {
            ServletContext context = session.getServletContext();		
    		ArticleDAO articles = (ArticleDAO) context.getAttribute("articles");
            
            // Primero sacamos el BlobKey (apunta a su direccion estatica) de la imagen subida
            Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
            BlobKey blobKey = blobs.get("image");
            // Ahora empleamos ImagesAPI para crear una url directa a la imagen a partir del BlobKEY
            // de esta forma, guardando esa url, evitamos crear un ServImagesServlet
            if(!blobs.isEmpty() && blobKey != null) {
            	Article article = articles.getLatestArticle();
            	try {
                	ImagesService imagesService = ImagesServiceFactory.getImagesService();
                	String imageUrl = imagesService.getServingUrl(blobKey);
                	article.addImageURL(imageUrl);
                	if(articles.updateArticle(article)) {
                		// Y registramos en el Datastore la relacion: id-blobKey
                		Entity uploadedImage = new Entity("UploadedImage");
                		uploadedImage.setProperty("id", article.getIdAsString());
                		uploadedImage.setProperty("blobKey", blobKey);
                		datastore.put(uploadedImage);
                		logger.log(Level.INFO, "Adj.Img. a: {0}, el {1}", new Object[]{article.getTitle(),
                        		article.getCreationDateAsString()});
                		response.sendRedirect(session.getServletContext().getContextPath()+"/uploadImage");
                	}

                } catch(IllegalArgumentException ex) {
                	article.setImageURLs(new ArrayList<String>());
                	articles.updateArticle(article);
                	response.sendRedirect(session.getServletContext().getContextPath()+exceptionError);             	
                } 
            } else {
            	response.sendRedirect(session.getServletContext().getContextPath()+errorForm);
            }
        } else {
        	response.sendRedirect(session.getServletContext().getContextPath()+errorForm);
        }
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	gotoURL(postImage,request,response);
    }
}