package com.example.shubham.lamamia_project.model;

public class ModuleContentList {

    private String video_id;
    private String video_name;

    public ModuleContentList(String video_id, String video_name){

        this.video_id = video_id;
        this.video_name = video_name;

    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

}
