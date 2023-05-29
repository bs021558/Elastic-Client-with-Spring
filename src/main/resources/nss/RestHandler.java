package nss;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import nss.entity.Article;
import nss.entity.Order;

/*
 * REST endpoints
 */
@RestController
@RequestMapping("/")
public class RestHandler {

    @Autowired
    RestElasticsearchClient restClient;
    @Autowired
    DocStreamer docStreamer;

    @PostMapping("create.go")
    public void create(@RequestBody String indexName) throws ElasticsearchException, IOException {
        restClient.create(indexName);
    }
    @PostMapping("index.go")
    public Boolean index(@RequestBody String jsonString) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = docStreamer.fetchArticles(jsonString);
        return restClient.indexBulk(articles);
    }

    @GetMapping("search.go")
    public String simpleSearch(@RequestParam String query) throws ElasticsearchException, IOException{
        ArrayList<Article> articles = new ArrayList<Article>();
        articles = restClient.searchMatch(query);
        Gson gson = new Gson();
        String jsonString = gson.toJson(articles);
        return jsonString;
    }

    @GetMapping("test.go")
    public String testSearch(@RequestParam String query) throws ElasticsearchException, IOException{
        ArrayList<Order> orders = new ArrayList<Order>();
        orders = restClient.testSearchMatch(query);
        Gson gson = new Gson();
        String jsonString = gson.toJson(orders);
        return jsonString;
    }
}
