package com.example.shubham.lamamia_project.model;

import java.util.ArrayList;

public class VideoCommentParentList {

    private String video_comment_id;
    private String video_id;
    private String video_comment_text;
    private String user_name;
    private String user_type;
    private String user_image;
    private ArrayList<VideoCommentChildList> videoCommentChildLists;

    public String getVideo_comment_id(){
        return video_comment_id;
    }

    public void setVideo_comment_id(String video_comment_id){
        this.video_comment_id = video_comment_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_comment_text() {
        return video_comment_text;
    }

    public void setVideo_comment_text(String video_comment_text) {
        this.video_comment_text = video_comment_text;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_image() {
        return user_image;
    }

    public ArrayList<VideoCommentChildList> getVideoCommentChildLists() {
        return videoCommentChildLists;
    }

    public void setVideoCommentChildLists(ArrayList<VideoCommentChildList> videoCommentChildLists) {
        this.videoCommentChildLists = videoCommentChildLists;
    }


}
