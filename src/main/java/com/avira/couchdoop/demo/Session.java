package com.avira.couchdoop.demo;

import java.util.List;

/**
 * Data bind bean for Session Couchbase documents.
 */
public class Session {

  private List<Article> articles;

  public static class Article {

    private String name;
    private String requestTime;
    private int timeSpent;

    public Article() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getRequestTime() {
      return requestTime;
    }

    public void setRequestTime(String requestTime) {
      this.requestTime = requestTime;
    }

    public int getTimeSpent() {
      return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
      this.timeSpent = timeSpent;
    }
  }

  public Session() {
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }
}
