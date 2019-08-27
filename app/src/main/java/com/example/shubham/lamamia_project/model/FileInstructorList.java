package com.example.shubham.lamamia_project.model;

public class FileInstructorList {

    private String video_attachment_id;
    private String attachment_name;
    private String attachment_type;

    public FileInstructorList(String video_attachment_id, String attachment_name, String attachment_type){
        this.video_attachment_id = video_attachment_id;
        this.attachment_name = attachment_name;
        this.attachment_type = attachment_type;
    }

    public String getVideo_attachment_id() {
        return video_attachment_id;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public String getAttachment_type() {
        return attachment_type;
    }
}
