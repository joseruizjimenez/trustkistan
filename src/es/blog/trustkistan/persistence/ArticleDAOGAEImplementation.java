package es.blog.trustkistan.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;

import es.blog.trustkistan.model.Article;

/**
 * Implementacion de ArticleDAO para persistir la informacion en ficheros
 * 
 * @param ArticleList lista de articulos para operar sobre el
 * @param fileName fichero donde se persistira la informacion
 * @param articlePersistenceManager ArticleDAO de ficheros
 */
public class ArticleDAOGAEImplementation implements ArticleDAO {
	private Object lockOfSync = new Object();
	private DatastoreService datastore;
    private ArrayList<Article> articleList = new ArrayList<Article>();
    private static ArticleDAOGAEImplementation articlePersistenceManager = null;
    private static final Logger logger = Logger.getLogger(
            ArticleDAOGAEImplementation.class.getName());

    private ArticleDAOGAEImplementation() {
    }

    public static ArticleDAO getArticleDAOGAEImplementation() {
        if (articlePersistenceManager == null) {
            articlePersistenceManager = new ArticleDAOGAEImplementation();
        }
        return articlePersistenceManager;
    }

    @Override
    public boolean createArticle(Article newArticle) {
    	if (articleList.contains(newArticle)) {
            return false;
        } else {
        	Entity article = new Entity("Article");
        	article.setProperty("id",newArticle.getIdAsString());
        	article.setProperty("title", newArticle.getTitle());
        	article.setProperty("body", new Text(newArticle.getBody()));
        	article.setProperty("creationDate", newArticle.getCreationDate().getTime());
        	newArticle.setCleanURL(newArticle.generateCleanURL());
        	if(newArticle.getImageURLs().isEmpty()) {
        		article.setProperty("imageURLs", new Text(""));
        	} else {
        		article.setProperty("imageURLs", newArticle.getTextFromImageURLs());
        	}
        	try{
        		synchronized(lockOfSync) {
        			datastore.put(article);
        			articleList.add(newArticle);
        		}
        		return true;
        	} catch (Exception ex) {
        		return false;
        	}
        }    	
    }

    @Override
    public Article readArticle(String id) {
    	UUID idKey = UUID.fromString(id);
    	synchronized(lockOfSync) {
    		for(Article article : articleList) {
    			if (article.getId().equals(idKey)) {
    				return article;
    			}
    		}
    	}
        return null; 
    }
    
    @Override
    public Article readArticleFromCleanURL(String cleanURL) {
    	synchronized(lockOfSync) {
    		for(Article article : articleList) {
    			if (article.getCleanURL().equals(cleanURL)) {
    				return article;
    			}
    		}
    	}
        return null; 
    }

    @Override
    public ArrayList<Article> listArticle() {
    	synchronized(lockOfSync) {
    		ArrayList<Article> list = articleList;
    		return list;
    	}
    }
    
    @Override
    public ArrayList<Article> listArticle(int year, int month, int date) {
    	ArrayList<Article> list = new ArrayList<Article>();
    	for(Article article : articleList) {
    		int articleYear = article.getCreationDate().get(Calendar.YEAR);
    		int articleMonth = article.getCreationDate().get(Calendar.MONTH+1);
    		int articleDate = article.getCreationDate().get(Calendar.DAY_OF_MONTH);
    		if(articleYear==year&&articleMonth==month&&articleDate==date) {
    			list.add(article);
    		}
    	}
    	return list;
    }

    @Override
    public boolean deleteArticle(String id) {
        UUID idKey = UUID.fromString(id);
        for(Article article : articleList) {
            if(article.getId().equals(idKey)) {
                Query q = new Query("Article");
            	q.addFilter("id", Query.FilterOperator.EQUAL, id);
            	PreparedQuery pq = datastore.prepare(q);
            	Entity result = pq.asSingleEntity();
            	if(result == null) {
            		return false;
            	} else {
            		synchronized(lockOfSync) {
            			articleList.remove(article);
            			if(!article.getImageURLs().isEmpty() && !deleteImageFromStores(id)) {
            				logger.log(Level.WARNING, "La imagen del articulo ({0}) no se pudo borrar",id);
            			}
            			datastore.delete(result.getKey());
            		}
            		return true;
            	}
            }
        }
        return false;
    }
    
    @Override
    public boolean updateArticle(Article newArticle) {
        UUID idKey = newArticle.getId();
        synchronized(lockOfSync) {
        	for(Article article : articleList) {
        		if(article.getId().equals(idKey)) {
        			Query q = new Query("Article");
        			q.addFilter("id", Query.FilterOperator.EQUAL, idKey.toString());
        			PreparedQuery pq = datastore.prepare(q);
        			Entity result = pq.asSingleEntity();
        			if(result == null) {
        				return false;
        			} else {
        				article.setCleanURL(newArticle.generateCleanURL());
        				article.setImageURLs(newArticle.getImageURLs());
        				result.setProperty("imageURLs", article.getTextFromImageURLs());
        				datastore.put(result);
        				return true;
        			}
        		}
        	}
        }
        return false;
    }

    @Override
    public boolean setUp(String url, String driver, String user, String password) {
    	return this.setUp();
    }
    
    public boolean setUp() {
    	datastore = DatastoreServiceFactory.getDatastoreService();
    	logger.log(Level.INFO, "Cargando articulos de la BD...");
    	int numArticles = 0;
    	Query q = new Query("Article");
    	q.addSort("creationDate", SortDirection.ASCENDING);
    	PreparedQuery pq = datastore.prepare(q);
    	for (Entity result : pq.asIterable()) {
    		Calendar creationDate = Calendar.getInstance();
    		creationDate.setTime((Date) result.getProperty("creationDate"));
    		Text bodyText = (Text)result.getProperty("body");
    		Article newArticle = new Article(
    				(String)result.getProperty("id"),
    				creationDate,
    				(String)result.getProperty("title"),
    				bodyText.getValue());
    		newArticle.setImageURLsFromText((Text)result.getProperty("imageURLs"));
    		newArticle.setCleanURL(newArticle.generateCleanURL());
    		articleList.add(newArticle);
    		numArticles++;
    	}
    	logger.log(Level.INFO, "Cargados: {0} articulos", numArticles);    	
        return true;
    }

    @Override
    public boolean disconnect() {
    	articleList.clear();
    	datastore = null;
    	logger.log(Level.INFO, "Desconectando la aplicacion...");
        return true;
    }
    
    @Override
    public boolean updateCachedList() {
    	synchronized(lockOfSync) {
    		int oldArticles = articleList.size();
    		int numArticles = 0;
    		Query q = new Query("Article");
    		q.addSort("creationDate", SortDirection.ASCENDING);
    		PreparedQuery pq = datastore.prepare(q);
    		articleList.clear();
    		for (Entity result : pq.asIterable()) {
    			Calendar creationDate = Calendar.getInstance();
    			creationDate.setTime((Date) result.getProperty("creationDate"));
    			Text bodyText = (Text)result.getProperty("body");
    			Article newArticle = new Article(
    				(String)result.getProperty("id"),
    				creationDate,
    				(String)result.getProperty("title"),
    				bodyText.getValue());
    			newArticle.setImageURLsFromText((Text)result.getProperty("imageURLs"));
    			newArticle.setCleanURL(newArticle.generateCleanURL());
    			articleList.add(newArticle);
    			numArticles++;
    		}
    		if(oldArticles != numArticles) {
    			logger.log(Level.INFO, "Actualizada cache a: {0} articulos", numArticles);
    		}
    	}
    	return true;
    }
    
    private boolean deleteImageFromStores(String id) {
    	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    	Query q = new Query("UploadedImage");
    	q.addFilter("id", Query.FilterOperator.EQUAL, id);
    	PreparedQuery pq = datastore.prepare(q);
    	for (Entity result : pq.asIterable()) {
    		// Borramos del Blobstore    		
    		BlobKey blobKey = (BlobKey) result.getProperty("blobKey");
    		blobstoreService.delete(blobKey);
    		// Borramos de nuestra tabla en la Datastore
    		datastore.delete(result.getKey());    		   		
    	}
    	return true;
    }

	@Override
	public Article getLatestArticle() {
		synchronized(lockOfSync) {
			return articleList.get(articleList.size()-1);
		}
	}

}
