package com.example.shubham.lamamia_project.model;

public class InstructorQuestionList {

    private String question_id;
    private String question_text;

    public InstructorQuestionList(String question_id, String question_text){
        this.question_id = question_id;
        this.question_text = question_text;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }
}
