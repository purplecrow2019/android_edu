package com.example.shubham.lamamia_project.model;

public class VideosInstructorList {

    private String id;
    private String video_id;
    private String video_name;
    private String is_last;
    private String type;

    public VideosInstructorList(String id, String video_id, String video_name, String is_last, String type){
        this.id = id;
        this.video_id = video_id;
        this.video_name = video_name;
        this.is_last = is_last;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getIs_last() {
        return is_last;
    }

    public String getType(){
        return type;
    }
}
