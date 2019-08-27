package com.example.shubham.lamamia_project.model;

public class FileLists {

    private String video_attachment_id;
    private String attachment_url;
    private String attachment_type;
    private String attachment_name;

    public FileLists(String video_attachment_id, String attachment_url, String attachment_type, String attachment_name){

        this.video_attachment_id = video_attachment_id;
        this.attachment_url = attachment_url;
        this.attachment_type = attachment_type;
        this.attachment_name = attachment_name;

    }

    public String getVideo_attachment_id() {
        return video_attachment_id;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public String getAttachment_name() {
        return attachment_name;
    }
}
