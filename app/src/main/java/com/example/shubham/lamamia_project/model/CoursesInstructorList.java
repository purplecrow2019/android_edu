package com.example.shubham.lamamia_project.model;

public class CoursesInstructorList {

    private String course_id;
    private String course_Name;

    public CoursesInstructorList(String course_id, String course_name){
        this.course_id = course_id;
        this.course_Name = course_name;
    }

    public String getCourseId(){
        return course_id;
    }

    public String getCourseName() {
        return course_Name;
    }
}
