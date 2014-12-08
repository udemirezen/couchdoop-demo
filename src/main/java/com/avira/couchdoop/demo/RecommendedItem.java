package com.avira.couchdoop.demo;

public class RecommendedItem {

    private String name;

    private Float score;

    public RecommendedItem() {
    }

    public RecommendedItem(String name, Float score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
