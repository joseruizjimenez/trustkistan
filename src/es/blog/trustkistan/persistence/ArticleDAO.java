package es.blog.trustkistan.persistence;

import java.util.ArrayList;
import es.blog.trustkistan.model.Article;

/**
 * Esta interfaz define un patron de persistencia DAO para los articulos
 */
public interface ArticleDAO {
    /**
     * Inserta un articulo en el sistema de persistencia
     * @param article articulo a insertar
     * @return true si hay exito, false en caso contrario
     */
    public boolean createArticle(Article article);
    
    /**
     * Lee un articulo del sistema de persistencia
     * @param id string identificando el articulo a leer
     * @return articulo solicitado
     */
    public Article readArticle(String id);
    
    /**
     * Lee un articulo del sistema de persistencia
     * @param cleanURL string con la ruta permalink
     * @return articulo solicitado
     */
    public Article readArticleFromCleanURL(String cleanURL);
    
    /**
     * Lista todos los articulos del sistema de persistencia
     * @return ArrayList de Articles. Vacio si no se encontraron coincidencias
     */
    public ArrayList<Article> listArticle();
    
    /**
     * Lista todos los articulos publicados en la fecha dada
     * @return ArrayList de Articles. Vacio si no se encontraron coincidencias
     */
    public ArrayList<Article> listArticle(int year, int month, int date);
    
    /**
     * Borra un articulo del sistema de persistencia
     * @param id del articulo a borrar
     * @return true si hay exito, false en caso contrario
     */
    public boolean deleteArticle(String id);
    
    /**
     * Actualiza un articulo del sistema de persistencia
     * @param Articulo a actualizar
     * @return true si hay exito, false en caso contrario
     */
    public boolean updateArticle(Article article);
    
    /**
     * Devuelve el ultimo articulo insertado
     * @return el articulo mas reciente
     */
    public Article getLatestArticle();
    
    /**
     * Metodo para crear la conexion con el sistema de persistencia.
     * En el caso de trabajar contra ficheros la url define parte del nombre
     * @param url
     * @param driver
     * @param user
     * @param password
     * @return true si hay exito, false en caso contrario
     */
    public boolean setUp(String url, String driver, String user, String password);
    
    /**
     * Cierra la conexion con el sistema de persistencia.
     * @return true si hay exito, false en caso contrario
     */
    public boolean disconnect();
    
    /**
     * Actualiza la lista en memoria, asegurando coherencia con el datastore
     * @return true si hay exito, false en caso contrario
     */
    public boolean updateCachedList();

}
