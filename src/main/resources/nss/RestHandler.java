package nss;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.google.gson.Gson;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import nss.entity.Article;

public class RestHandler {

    @Autowired
    RestElasticsearchClient restClient;
    @Autowired
    DocStreamer docStreamer;

    @PostMapping("create.go")
    public void create() throws ElasticsearchException, IOException {
        restClient.create();
    }
    @PostMapping("index.go")
    public Boolean index(@RequestBody String jsonStringified) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = docStreamer.fetchArticles(jsonStringified);
        return restClient.indexBulk(articles);
    }

    @GetMapping("search.go")
    public String simpleSearch(@RequestBody String query) throws ElasticsearchException, IOException{
        ArrayList<Article> articles = new ArrayList<Article>();
        articles = restClient.searchMatch(query);
        Gson gson = new Gson();
        String jsonString = gson.toJson(articles);
        return jsonString;
    }
}
