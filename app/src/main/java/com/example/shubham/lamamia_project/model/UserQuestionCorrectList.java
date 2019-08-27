package com.example.shubham.lamamia_project.model;

public class UserQuestionCorrectList {

    private String question_order;
    private String attended;
    private String current_question_order;

    public UserQuestionCorrectList(String question_order, String attended, String current_question_order){
        this.question_order = question_order;
        this.attended = attended;
        this.current_question_order = current_question_order;
    }

    public String getQuestion_order() {
        return question_order;
    }

    public String getAttended() {
        return attended;
    }

    public String getCurrent_question_order(){
        return current_question_order;
    }
}
