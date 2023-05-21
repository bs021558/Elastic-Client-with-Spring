package nss;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void index(Model model) throws ElasticsearchException, IOException {
        Article article = docStreamer.fetchArticle(model.getAttribute("jsonStringified").toString());
        resc.index(article);
    }
    @RequestMapping("indexBulk.go")
    public Boolean indexBulk(Model model) throws ElasticsearchException, IOException {
        ArrayList<Article> articles = docStreamer.fetchArticles(model.getAttribute("jsonStringified").toString());
        return resc.indexBulk(articles);
    }

    @RequestMapping("search.go")
    public void simpleSearch(Model model) throws ElasticsearchException, IOException{
        ArrayList<Article> articles = new ArrayList<Article>();
        articles = resc.searchMatch((String)model.getAttribute("query"));
        Gson gson = new Gson();
        String jsonStringified = gson.toJson(articles);
        model.addAttribute(jsonStringified);
    }
}
