package com.example.shubham.lamamia_project.model;

public class VideoList {

    private String video_id;
    private String video_name;
    private String video_description;
    private String module_id;
    private String video_order;
    private String video_view_status;
    private String order_id;
    private String content_type;

    public VideoList(String video_id, String video_name, String video_description, String module_id,
                     String video_order, String video_view_status, String order_id, String content_type){

        this.video_id = video_id;
        this.video_name = video_name;
        this.video_description = video_description;
        this.module_id = module_id;
        this.video_order = video_order;
        this.video_view_status = video_view_status;
        this.order_id = order_id;
        this.content_type = content_type;

    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getVideo_description() {
        return video_description;
    }

    public String getModule_id() {
        return module_id;
    }

    public String getVideo_order() {
        return video_order;
    }

    public String getVideo_view_status() {
        return video_view_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getContent_type() {
        return content_type;
    }
}
