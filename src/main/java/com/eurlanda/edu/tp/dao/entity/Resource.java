package com.eurlanda.edu.tp.dao.entity;

public class Resource {
    private Integer id;

    private String name;

    private String url;

    private Integer width;

    private Integer height;

    public Resource(Integer id, String name, String url, Integer width, Integer height) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Resource(String name, String url, int width, int height) {
        this(0, name, url, width, height);
    }

    public Resource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}