package com.example.shubham.lamamia_project.model;

public class UserQuestionList {

    private String question_order;
    private String attended;
    private String current_question_order;
    private boolean clickeble;

    public UserQuestionList(String question_order, String attended, String current_question_order, boolean clickeble){
        this.question_order = question_order;
        this.attended = attended;
        this.current_question_order = current_question_order;
        this.clickeble = clickeble;
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

    public boolean isClickeble() {
        return clickeble;
    }
}
