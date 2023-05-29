package nss;

import java.io.IOException;
import java.util.ArrayList;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import nss.entity.Article;

public interface RestElasticsearchClientInterface {

    /*
     * Create a single index
     */
    void create(String indexName) throws ElasticsearchException, IOException;

    /*
     * Simply index a single document
     */
    void index(Article article) throws ElasticsearchException, IOException;

    /*
     * Index multiple documents
     */
    Boolean indexBulk(ArrayList<Article> articles) throws ElasticsearchException, IOException;

    /*
     * Search using simple match query
     */
    ArrayList<Article> searchMatch(String query) throws ElasticsearchException, IOException;

}