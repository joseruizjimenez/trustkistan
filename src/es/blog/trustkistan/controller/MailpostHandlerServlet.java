package es.blog.trustkistan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
//import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
//import com.google.appengine.api.images.ImagesService;
//import com.google.appengine.api.images.ImagesServiceFactory;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import es.blog.trustkistan.model.Article;
import es.blog.trustkistan.persistence.ArticleDAO;

/**
 * Crea una nueva entrada desde un correo electronico
 * 
 * @author Jose Ruiz Jimenez
 */
@SuppressWarnings("serial")
public class MailpostHandlerServlet extends BasicUtilitiesServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	//private ImagesService imagesService = ImagesServiceFactory.getImagesService();
    private static final Logger logger = Logger.getLogger(GetPageServlet.class.getName());

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	/*Properties props = new Properties(); 
        Session mailSession = Session.getDefaultInstance(props, null); 
        try {
			MimeMessage message = new MimeMessage(mailSession, request.getInputStream());
		} catch (MessagingException ex) {
			logger.log(Level.SEVERE, "Error parseando a MimeMessage el mail", ex);
		}*/
        
    	
        // Comprobamos que se nos manda un formulario multipart/form-data
        if(ServletFileUpload.isMultipartContent(request)) {
            HttpSession session = request.getSession();
            ServletContext context = session.getServletContext();
            
            // Primero sacamos el BlobKey (apunta a su direccion estatica) de la imagen subida
            Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
            BlobKey blobKey = blobs.get("image");
            
            // Ahora el resto de campos del formulario, siendo este multipart/form-data
            ServletFileUpload upload = new ServletFileUpload();
            try{
            	Article article = new Article();
            	FileItemIterator iter = upload.getItemIterator(request);
            	while(iter.hasNext()) {
            		FileItemStream item = iter.next();
            		String fieldName = item.getFieldName();
            		InputStream stream = item.openStream();
            		if(item.isFormField()) {
            			String value = Streams.asString(stream);
            			if("title".equals(fieldName)) {
            				article.setTitle(value);
            			} else if("body".equals(fieldName)) {
            				article.setBody(value.replace("\n","<br>"));
            			}
            		}
            	}
            	// Ahora empleamos ImagesAPI para crear una url directa a la imagen a partir del BlobKEY
            	// de esta forma, guardando esa url, evitamos crear un ServImagesServlet
            	if(blobKey != null) {
            		//article.setHasImage(true);
                	//String imageUrl = imagesService.getServingUrl(blobKey);
                	//article.setImageUrl(imageUrl);
                }
            	
            	ArticleDAO articles = (ArticleDAO) context.getAttribute("articles");
            	articles.createArticle(article);
            	//logger.log(Level.INFO, "Publicado: {0} (imagen={1}) el {2}", new Object[]{article.getTitle(),article.hasImage(), article.getCreationDateAsString()});
            	response.sendRedirect(session.getServletContext().getContextPath()+frontPage);
            }catch (FileUploadException ex) {
            	logger.log(Level.WARNING, "Fallo leyendo datos subidos...", ex);
            	response.sendRedirect(session.getServletContext().getContextPath()+exceptionError);
            }
            /*try {
                // Fichero temporal para almacenar en MEMORIA lo recivido en la peticion
            	MemoryFileItemFactory fileItem = new MemoryFileItemFactory();
                //DiskFileItemFactory fileItem = new DiskFileItemFactory();
                // Creamos un manejador para todos los items de la peticion
                ServletFileUpload upload = new ServletFileUpload(fileItem);
               // Obtenemos la lista con todos los items de la peticion
                List<FileItem> items = upload.parseRequest(request);
                Article article = new Article();
                for(FileItem item : items) {
                    //Procesamos primero los datos de texto
                    if(item.isFormField()) {
                        String fieldName = item.getFieldName();
                        if("title".equals(fieldName)) {
                            article.setTitle(item.getString());
                        } else if("body".equals(fieldName)) {
                            article.setBody(item.getString().replace("\n","<br>"));
                        }
                    } else {
                        //Ahora la imagen (si la contiene)
                        if(item.getContentType().contains("image")) {
                            article.setHasImage(true);
                            String imageType = item.getName().substring(item.getName().lastIndexOf(".")+1);
                            article.setImageType(imageType);
                            StringBuilder path = new StringBuilder(context.getRealPath("/"));
                            path.append("img\\");
                            path.append(article.getIdAsString());
                            path.append(".");
                            path.append(imageType);
                            File saveTo = new File(path.toString());
                            try {
                                item.write(saveTo);
                                logger.log(Level.INFO, "Imagen {0} guardada en disco", article.getIdAsString());
                            } catch (Exception ex) {
                                logger.log(Level.WARNING, "Error grabando la imagen en disco", ex);
                                article.setHasImage(false);
                            }
                        }
                    }
                }
                ArticleDAO articles = (ArticleDAO) context.getAttribute("articles");
                articles.createArticle(article);
                logger.log(Level.INFO, "Publicado: {0} el {1}", new Object[]{article.getTitle(), article.getCreationDateAsString()});
                gotoURL(frontPage, request, response);
            } catch (FileUploadException ex) {
                logger.log(Level.WARNING, "Fallo subiendo la imagen", ex);
                gotoURL(exceptionError, request, response);
            }*/
        } else {
            gotoURL(errorForm, request, response);
        }
    }
}