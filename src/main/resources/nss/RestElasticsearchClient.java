package nss;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import nss.entity.Template;

@RestController
public class RestElasticsearchClient {

    private final String elasticServer = "localhost";

    @Autowired
    Logger logger;
    DataStreamer dataStreamer;

    public RestElasticsearchClient restElasticsearchClient() {
        return this;
    }

    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost(elasticServer, 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);

    @RequestMapping("create.go")
    public void create() throws ElasticsearchException, IOException {
        client.indices().create(c -> c.index("rules"));
    }

    @RequestMapping("bulk.go")
    public Boolean bulk(Model model) throws ElasticsearchException, IOException {
        Boolean success = false;
        ArrayList<Article> articles = dataStreamer.fetchArticles(model);

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

    @RequestMapping("search.go")
    public ArrayList<Article> simpleSearch(String query) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = new ArrayList<Article>();

        // Query using fluent DSL
        SearchResponse<Template> response = client.search(s -> s
                .index("rules")
                .query(q -> q
                        .match(t -> t
                                .field("article_content")
                                .query(query))),
                Template.class);

        // result information
        TotalHits total = response.hits().total();

        logger.info("There are more than " + total.value() + " results");

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
        for (Hit<Template> hit : response.hits().hits()) {
            Article article = (Article) hit.source();
            article.setScore(hit.score());
            articles.add(article);
        }
        return articles;
    }
}