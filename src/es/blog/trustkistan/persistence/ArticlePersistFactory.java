package es.blog.trustkistan.persistence;

/**
 * Genera una factoria de articulos siguiendo el patron DAO: ArticleDAO
 */
public class ArticlePersistFactory {
    
    /**
     * Obten un sistema de persistencia del tipo declarado en persistenceMechanism
     * @param persistenceMechanism puede ser file, jdbc o pool
     * @return ArticleDAO si todo va bien, sino null
     */
    public static ArticleDAO getArticleDAO(String persistenceMechanism){
    	if (persistenceMechanism.equals("gae")){
            return ArticleDAOGAEImplementation.getArticleDAOGAEImplementation();
        }
    	/*if (persistenceMechanism.equals("file")){
            return ArticleDAOFileImplementation.getArticleDAOFileImplementation();
        }
        else if(persistenceMechanism.equals("jdbc")){
            return CommentDAOJDBCImplementation.getCommentDAOJDBCImplementation();
        }
        else if (persistenceMechanism.equals("pool")) {
            return CommentDAOPoolImplementation.getCommentDAOPoolImplementation();
        }*/
        else {
            return null;
        }
    }
}
