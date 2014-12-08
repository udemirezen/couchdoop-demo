package com.avira.couchdoop.demo;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {

    private List<RecommendedItem> articles = new ArrayList<>();

    public Recommendation() {
    }


    public void addArticle(RecommendedItem item) {
        articles.add(item);
    }

    public List<RecommendedItem> getArticles() {
        return articles;
    }

    public void setArticles(List<RecommendedItem> articles) {
        this.articles = articles;
    }

}
