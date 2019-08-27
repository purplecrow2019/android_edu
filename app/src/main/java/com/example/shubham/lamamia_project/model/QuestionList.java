package com.example.shubham.lamamia_project.model;

public class QuestionList {

    private String question_id;
    private String question_name;

    public QuestionList(String question_id, String question_name){
        this.question_id = question_id;
        this.question_name = question_name;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_name() {
        return question_name;
    }
}
