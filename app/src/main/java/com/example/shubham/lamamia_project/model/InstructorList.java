package com.example.shubham.lamamia_project.model;

public class InstructorList {

    private String instructor_id;
    private String instructor_name;
    private String instructor_image;
    private String instructor_about;
    private String instructor_achievements;

    public InstructorList(String instructor_id, String instructor_name, String instructor_image, String instructor_about, String instructor_achievements){

        this.instructor_id = instructor_id;
        this.instructor_name = instructor_name;
        this.instructor_image = instructor_image;
        this.instructor_about = instructor_about;
        this.instructor_achievements = instructor_achievements;
    }

    public String getInstructor_id(){
        return instructor_id;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public String getInstructor_image() {
        return instructor_image;
    }

    public String getInstructor_about(){
        return instructor_about;
    }

    public String getInstructor_achievements() {
        return instructor_achievements;
    }
}
