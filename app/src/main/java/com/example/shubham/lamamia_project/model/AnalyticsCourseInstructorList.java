package com.example.shubham.lamamia_project.model;

public class AnalyticsCourseInstructorList {
    private String course_id;
    private String course_name;
    private String course_user_count;
    private String course_views;
    private String course_total_views;

    public AnalyticsCourseInstructorList(String course_id, String course_name, String course_user_count, String course_views, String course_total_views){
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_user_count = course_user_count;
        this.course_views = course_views;
        this.course_total_views = course_total_views;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_user_count() {
        return course_user_count;
    }

    public String getCourse_views() {
        return course_views;
    }

    public String getCourse_total_views() {
        return course_total_views;
    }
}
