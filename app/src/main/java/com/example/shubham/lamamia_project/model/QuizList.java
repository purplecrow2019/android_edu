package com.example.shubham.lamamia_project.model;

public class QuizList {

    private String quiz_id;
    private String quiz_name;

    public QuizList(String quiz_id, String quiz_name){
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }
}
