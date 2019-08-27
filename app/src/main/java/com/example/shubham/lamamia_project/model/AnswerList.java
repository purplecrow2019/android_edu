package com.example.shubham.lamamia_project.model;

public class AnswerList {

    private String answer_id;
    private String answer_text;

    public AnswerList(String answer_id, String answer_text){
        this.answer_id = answer_id;
        this.answer_text = answer_text;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }
}
