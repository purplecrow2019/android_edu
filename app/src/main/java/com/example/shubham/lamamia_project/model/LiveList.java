package com.example.shubham.lamamia_project.model;

public class LiveList {

    private String content_id;
    private String content_name;
    private String live_url;

    public LiveList(String content_id, String content_name, String live_url){
        this.content_id = content_id;
        this.content_name = content_name;
        this.live_url = live_url;
    }

    public String getContent_id() {
        return content_id;
    }

    public String getContent_name(){
        return content_name;
    }

    public String getLive_url(){
        return live_url;
    }

}
