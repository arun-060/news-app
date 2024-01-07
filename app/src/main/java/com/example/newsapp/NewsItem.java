package com.example.newsapp;

public class NewsItem {
    private String title;
    private String description;
    private String url;
    private String urlToImage;

    public NewsItem(String title, String description, String url, String urlToImage) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getTitle() {return this.title;}
    public String getDescription() { return this.description; }
    public String getUrl() { return this.url; }
    public String getUrlToImage() { return this.urlToImage; }
}
