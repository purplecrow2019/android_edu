package com.example.shubham.lamamia_project.model;

public class UserCourseList {

    private String course_id;
    private String course_name;
    private String course_image;

    public UserCourseList(String course_id, String course_name, String course_image){
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_image = course_image;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_image() {
        return course_image;
    }
}
