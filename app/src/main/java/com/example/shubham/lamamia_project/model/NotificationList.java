package com.example.shubham.lamamia_project.model;

public class NotificationList {

    private String notification_id;
    private String notification_text;
    private String for_id;
    private String for_user_type;
    private String of_id;
    private String of_user_type;
    private String video_comment_id;
    private String notification_status;
    private String video_id;
    private String notification_type;
    private String image;

    public NotificationList(String notification_id,
                            String notification_text,
                            String for_id,
                            String for_user_type,
                            String of_id,
                            String of_user_type,
                            String video_comment_id,
                            String notification_status,
                            String video_id,
                            String notification_type,
                            String image){

        this.notification_id = notification_id;
        this.notification_text = notification_text;
        this.for_id = for_id;
        this.for_user_type = for_user_type;
        this.of_id = of_id;
        this.of_user_type = of_user_type;
        this.video_comment_id = video_comment_id;
        this.notification_status = notification_status;
        this.video_id = video_id;
        this.notification_type = notification_type;
        this.image = image;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public String getFor_id() {
        return for_id;
    }

    public String getFor_user_type() {
        return for_user_type;
    }

    public String getOf_id() {
        return of_id;
    }

    public String getOf_user_type() {
        return of_user_type;
    }

    public String getVideo_comment_id() {
        return video_comment_id;
    }

    public String getNotification_status() {
        return notification_status;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getNotification_type(){
        return notification_type;
    }

    public String getImage() {
        return image;
    }
}
