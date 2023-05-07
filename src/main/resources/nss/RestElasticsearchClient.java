package nss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import nss.entity.Article;
import nss.entity.ResponseTemplate;


public class RestElasticsearchClient {
    public RestElasticsearchClient restElasticsearchClient(){
        return this;
    }
    // Create the low-level client
    RestClient restClient = RestClient.builder(
        new HttpHost("localhost", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
        restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);

    @Autowired
    Logger logger;
    
    @RequestMapping("create.go")
    public void create() throws ElasticsearchException, IOException{
        client.indices().create(c -> c.index("rules"));
    }

    @RequestMapping("search.go")
    public ArrayList<Article> simpleSearch(String query) throws ElasticsearchException, IOException{
        ArrayList<Article> articles = new ArrayList<Article>(); 
        
        //Query using fluent DSL
        SearchResponse<ResponseTemplate> response = client.search(s -> s
            .index("rules")
            .query(q -> q
                .match(t -> t
                    .field("article_content")
                    .query(query)
                )
            ),
            ResponseTemplate.class
        );
        
        //result information
        TotalHits total = response.hits().total();
        
        logger.info("There are more than " + total.value() + " results");

        for (Hit<ResponseTemplate> hit: response.hits().hits()) {
            Article article = new Article();
            article = hit.source();
            

        }
        return articles;
    }

    @RequestMapping("smartSearch.go")
    public SearchResponse<ResponseTemplate> multiMatchSearch(String query) throws ElasticsearchException{
        //Query using fluent DSL
        SearchResponse<ResponseTemplate> response = client.search(s -> s
            .index("rules")
            .query(q -> q      
                .multiMatch(t -> t   
                    .query(query)
                    .fields("*")
                )
            ),
            ResponseTemplate.class
        );
         logger.info("There are more than " + total.value() + " results");
 
         List<Hit<ResponseTemplate>> hits = response.hits().hits();
         for (Hit<ResponseTemplate> hit: hits) {
            ResponseTemplate responseTemplate = hit.source();
            article.setScore(hit.score());
         }
        return response;
    }
}
