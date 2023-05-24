package nss;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import nss.entity.Article;

public class RestHandler {

    @Autowired
    RestElasticsearchClient resc;
    @Autowired
    DocStreamer docStreamer;

    @RequestMapping("create.go")
    public void create() throws ElasticsearchException, IOException {
        resc.create();
    }
    @RequestMapping("index.go")
    public Boolean index(@RequestBody String jsonStringified) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = docStreamer.fetchArticles(jsonStringified);
        return resc.indexBulk(articles);
    }

    @RequestMapping("search.go")
    public String simpleSearch(@RequestBody String query) throws ElasticsearchException, IOException{
        ArrayList<Article> articles = new ArrayList<Article>();
        articles = resc.searchMatch(query);
        Gson gson = new Gson();
        String jsonStringified = gson.toJson(articles);
        return jsonStringified;
    }
}
