package com.example.shubham.lamamia_project.model;

public class VideoChooseList {

    private String video_uri;
    private String video_name;
    private String video_image;
    private String video_id;

    public VideoChooseList(String video_uri, String video_name, String video_image, String video_id){

        this.video_uri = video_uri;
        this.video_name = video_name;
        this.video_image = video_image;
        this.video_id = video_id;

    }

    public String getVideo_uri() {
        return video_uri;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getVideo_image() {
        return video_image;
    }

    public String getVideo_id() {
        return video_id;
    }
}
