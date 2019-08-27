package com.example.shubham.lamamia_project.model;

public class CourseRatingList {

    private String course_rating_id;
    private String course_id;
    private String user_name;
    private String rating_count;
    private String rating_text;
    private String total_comments;

    public CourseRatingList(String course_rating_id, String course_id, String user_name, String rating_count, String rating_text, String total_comments){

        this.course_rating_id = course_rating_id;
        this.course_id = course_id;
        this.user_name = user_name;
        this.rating_count = rating_count;
        this.rating_text = rating_text;
        this.total_comments = total_comments;
    }

    public String getCourse_rating_id(){
        return course_rating_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getRating_count() {
        return rating_count;
    }

    public String getRating_text() {
        return rating_text;
    }

    public String getTotal_comments() {
        return total_comments;
    }
}
