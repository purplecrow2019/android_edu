package com.example.shubham.lamamia_project.model;

public class CoursesListViewInstructor {

    private String course_Name;
    private String course_id;
    private String course_image_medium;
    private String course_views;
    private String course_average_rating;
    private String course_reviews;
    private String course_share;
    private String status;

    public CoursesListViewInstructor(String course_name, String course_id, String course_image_medium, String course_views, String course_average_rating, String course_reviews, String course_share, String status){
        this.course_Name = course_name;
        this.course_id = course_id;
        this.course_image_medium = course_image_medium;
        this.course_views = course_views;
        this.course_average_rating = course_average_rating;
        this.course_reviews = course_reviews;
        this.course_share = course_share;
        this.status = status;
    }

    public String getCourseName() {
        return course_Name;
    }

    public String getCourseId(){
        return course_id;
    }

    public String getCourseImgaeMedium(){
        return course_image_medium;
    }

    public String getCourse_views() {
        return course_views;
    }

    public String getCourse_average_rating() {
        return course_average_rating;
    }

    public String getCourse_reviews() {
        return course_reviews;
    }

    public String getCourse_share(){return course_share;}

    public String getStatus() {
        return status;
    }
}