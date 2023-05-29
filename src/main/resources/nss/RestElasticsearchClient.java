package nss;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import nss.entity.Article;
import nss.entity.Order;

@Repository
public class RestElasticsearchClient implements RestElasticsearchClientInterface {

    /*
     ***** Connection creation *****
     */

    //Elasticsearch server address
    String elasticServerAdress = "localhost";

    @Autowired
    Logger logger;

    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost(elasticServerAdress, 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);

    /*
     * Create a single index
     */
    @Override
    public void create(String indexName) throws ElasticsearchException, IOException {
        client.indices().create(c -> c.index(indexName));
    }

    /*
     * Simply index a single document
     */
    @Override
    public void index(Article article) throws ElasticsearchException, IOException{
        client.index(i -> i
            .index("rules")
            .id(article.getId())
            .document(article)
        );
    }

    /*
     * Index multiple documents
     */
    @Override
    public Boolean indexBulk(ArrayList<Article> articles) throws ElasticsearchException, IOException {
        Boolean success = false;
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Article article : articles) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("rules")
                            .id(article.getId())
                            .document(article)));
        }

        BulkResponse result = client.bulk(br.build());

        // Log errors, if any
        if (result.errors()) {
            logger.info("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    logger.info(item.error().reason());
                }
            }
        }else{
            success = true;
        }
        return success;

    }

    /*
     * Search using simple match query
     */
    @Override
    public ArrayList<Article> searchMatch(String query) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = new ArrayList<Article>();
        // you can desing the query here using fluent DSL
        SearchResponse<Article> response = client.search(s -> s
                .index("articles")
                .query(q -> q
                        .match(t -> t
                                .field("content")
                                .query(query))),
                Article.class);

        // result information
        TotalHits total = response.hits().total();

        //Optional logging
        logger.info("There are more than " + total.value() + " results");

        
        for (Hit<Article> hit : response.hits().hits()) {
        /*
         * hit is like...
         * {
         * "_index": "my_index",
         * "_type": "_doc",
         * "_id": "1",
         * "_score": 1.0,
         * "_source": {
         * "field1": "value1",
         * "field2": "value2"
         * }
         */
            Article article = (Article) hit.source();
            article.setScore(hit.score());
            articles.add(article);
        }
        return articles;
    }
    
    /*
     * Test the search function
     */
    public ArrayList<Order> testSearchMatch(String query) throws ElasticsearchException, IOException {
        SearchResponse<Order> response = client.search(s -> s
                .index("kibana_sample_data_ecommerce")
                .query(q -> q
                        .match(t -> t
                                .field("products.product_name")
                                .query(query))),
                Order.class);

        // result information
        TotalHits total = response.hits().total();
        logger.info("There are more than " + total.value() + " results");
        ArrayList<Order> orders = new ArrayList<Order>();
        /*
         * hit is like...
         * {
         * "_index": "my_index",
         * "_type": "_doc",
         * "_id": "1",
         * "_score": 1.0,
         * "_source": {
         * "field1": "value1",
         * "field2": "value2"
         * }
         */
        for (Hit<Order> hit : response.hits().hits()) {
            Order order = new Order();
            order = (Order) hit.source();
            orders.add(order);
        }
        return orders;
    }

}
    