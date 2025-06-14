package com.sasaadamovic.brzevijesti;

import com.google.gson.annotations.SerializedName;

public class NewsArticle {
    private String category;
    private long datetime;
    private String headline;
    private long id;
    private String image;
    private String related;
    private String source;
    private String summary;
    private String url;

    public NewsArticle() {
    }

    public NewsArticle(String category, long datetime, String headline, long id, String image, String related, String source, String summary, String url) {
        this.category = category;
        this.datetime = datetime;
        this.headline = headline;
        this.id = id;
        this.image = image;
        this.related = related;
        this.source = source;
        this.summary = summary;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public long getDatetime() {
        return datetime;
    }

    public String getHeadline() {
        return headline;
    }

    public long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getRelated() {
        return related;
    }

    public String getSource() {
        return source;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}