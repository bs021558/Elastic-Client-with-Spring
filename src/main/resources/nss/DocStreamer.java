package nss;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nss.entity.Article;

public class DocStreamer {
    public Article fetchArticle(String jsonStringified) {
        Gson gson = new Gson();
        Article article = gson.fromJson(jsonStringified, Article.class);
        return article;
    }
    public ArrayList<Article> fetchArticles(String jsonStringified) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Gson gson = new Gson();
        articles = gson.fromJson(jsonStringified, new TypeToken<ArrayList<Article>>(){}.getType());
        return articles;
    }
}