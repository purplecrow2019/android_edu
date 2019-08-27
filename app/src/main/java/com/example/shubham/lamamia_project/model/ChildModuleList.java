package com.example.shubham.lamamia_project.model;

public class ChildModuleList {

    private String video_id;
    private String video_name;
    private String module_id;
    private String video_order;
    private String content_type;
    private String course_id;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getVideo_order() {
        return video_order;
    }

    public void setVideo_order(String video_order) {
        this.video_order = video_order;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_id() {
        return course_id;
    }
}
