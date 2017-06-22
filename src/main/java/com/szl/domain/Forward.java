package com.szl.domain;

import java.io.Serializable;

/**
 * Created by zsc on 2016/12/20.
 * 仅用权重作比较
 */
public class Forward implements Serializable,Comparable<Forward>{
    private int id;
    private String title;//标题
    private String url;//网页地址
    private String description;//摘要
    private String qDescription;//摘要
    private int quality;//权重
    private String keyWords;//关键词
    private String TF;

    private String pro;
    private String user;

    public Forward() {}

//    public Forward(String title, String url, String description, int quality, String keyWords) {
//        this.title = title;
//        this.url = url;
//        this.description = description;
//        this.quality = quality;
//        this.keyWords = keyWords;
//    }
//
//    public Forward(String title, String url, String description, int quality, String keyWords, String TF) {
//        this.title = title;
//        this.url = url;
//        this.description = description;
//        this.quality = quality;
//        this.keyWords = keyWords;
//        this.TF = TF;
//    }

    @Override
    public int compareTo(Forward forward) {
        if (forward.quality == quality) {
            return 0;
        } else {
            return forward.quality > quality ? 1 : -1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getqDescription() {
        return description.split("\\r\\n")[2];
    }

    public void setqDescription(String qDescription) {
        this.qDescription = qDescription;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTF() {
        return TF;
    }

    public void setTF(String TF) {
        this.TF = TF;
    }

    public String getPro() {
        return description.split("\\r\\n")[0];
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getUser() {
        return description.split("\\r\\n")[1].equals("") ? "匿名用户" : description.split("\\r\\n")[1];
    }

    public void setUser(String user) {
        this.user = user;
    }
}
