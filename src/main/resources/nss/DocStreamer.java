package nss;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nss.entity.Article;
@Service
public class DocStreamer {
    public ArrayList<Article> fetchArticles(String jsonString) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Gson gson = new Gson();
        articles = gson.fromJson(jsonString, new TypeToken<ArrayList<Article>>(){}.getType());
        return articles;
    }
}