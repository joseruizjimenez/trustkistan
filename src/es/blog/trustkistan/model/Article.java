package es.blog.trustkistan.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.google.appengine.api.datastore.Text;

/**
 * Articulo del blog
 * 
 * @param id identificador del articulo
 * @param title titulo del articulo
 * @param body contenido del articulo
 * @param imageUrls con las direcciones de imagenes
 */
@SuppressWarnings("serial")
public class Article implements Serializable {
    private UUID id = null;
    private String title = "";
    private String body = "";
    private Calendar creationDate = null;
    private ArrayList<String> imageURLs = null;
    private String cleanURL = "";
    
    public Article() {
        this.id = UUID.randomUUID();
        this.creationDate = Calendar.getInstance();
        this.creationDate.clear();
        this.creationDate.setTimeInMillis(System.currentTimeMillis());
        this.imageURLs = new ArrayList<String>();
    }
    
    public Article(String id, Calendar creationDate){
        this.id = UUID.fromString(id);
        this.creationDate = creationDate;
        this.imageURLs = new ArrayList<String>();
    }
    
    public Article(UUID id, Calendar creationDate){
        this.id = id;
        this.creationDate = creationDate;
        this.imageURLs = new ArrayList<String>();
    }
    
    /**
     * Constructor de un nuevo articulo
     */
    public Article(String title, String body){
        this();
        this.title = title;
        this.body = body;
    }
    
    /**
     * Constructor para recrear articulos ya existentes
     */
    public Article(String id, Calendar creationDate, String title, String body){
        this(id,creationDate);
        this.title = title;
        this.body = body;
        this.cleanURL = this.generateCleanURL();
    }
    
    @Override
    public boolean equals(Object object) {
        if(object instanceof Article) {
            Article article = (Article) object;
            return (article != null) && (this.getId().equals(article.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }


    public UUID getId() {
        return id;
    }
    
    public String getIdAsString() {
        return id.toString();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }
    
    public String getCreationDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(creationDate.getTime());
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }
    
    public void setCreationDate(String timestamp) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.creationDate.setTime(dateFormat.parse(timestamp));
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
    
    public Text getTextFromImageURLs() {
    	StringBuilder urls = new StringBuilder("");
    	for(String url : imageURLs) {
    		urls.append("$");
    		urls.append(url);
    	}   	
    	return new Text(urls.toString());
    }
    
    public void setImageURLsFromText(Text urls) {
    	String urlString = urls.getValue();
    	if(!"".equals(urlString)) {
    		String urlArray[] = urlString.split("\\$");
    		for(String url : urlArray) {
    			if(!"".equals(url)) {
    				this.imageURLs.add(url);
    			}
    		}
    	}  	
    }

	public void addImageURL(String imageUrl) {
		this.imageURLs.add(imageUrl);		
	}
	
	public String getCleanURL() {
		return this.cleanURL;
	}
	
	public void setCleanURL(String cleanURL) {
		this.cleanURL = cleanURL;
	}
	
	public String generateCleanURL() {
		//String domainURL = "http://www.trustkistan.com/article";
		StringBuilder cleanURL = new StringBuilder("/");
		cleanURL.append(creationDate.get(Calendar.YEAR));
		cleanURL.append("/");
		cleanURL.append(creationDate.get(Calendar.MONTH)+1);
		cleanURL.append("/");
		cleanURL.append(creationDate.get(Calendar.DAY_OF_MONTH));
		cleanURL.append("/");
		cleanURL.append(this.getCleanTitle());
		
		return cleanURL.toString();
	}

	public String getCleanTitle() {
		StringBuilder cleanTitle = new StringBuilder("");
		if ("".equals(title)) {
			cleanTitle.append("timeline/");
			cleanTitle.append(generateCreationTime());
		} else {
			cleanTitle.append(generateTitleWithoutSymbols());
		}
			
		return cleanTitle.toString();
	}
	
	public String generateCreationTime() {
		StringBuilder time = new StringBuilder("");
		time.append(creationDate.get(Calendar.HOUR_OF_DAY));
		time.append("-");
		time.append(creationDate.get(Calendar.MINUTE));
		time.append("-");
		time.append(creationDate.get(Calendar.SECOND));
		return time.toString();
	}

	public String generateTitleWithoutSymbols() {
		StringBuilder titleWithoutSymbols = new StringBuilder("");
		for(int index=0;index<this.title.length();index++) {
			char ch = this.title.charAt(index);
			int charType = Character.getType(ch);
			if(charType==Character.LOWERCASE_LETTER||charType==Character.UPPERCASE_LETTER||
					charType==Character.DECIMAL_DIGIT_NUMBER) {
				titleWithoutSymbols.append(ch);
			} else {
				titleWithoutSymbols.append("_");
			}
		}
		return titleWithoutSymbols.toString();
	}

    
}
