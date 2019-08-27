package com.example.shubham.lamamia_project.model;

public class UserAnswersList {

    private String user_id;
    private String api_token;
    private String quiz_id;
    private String question_id;
    private String question_order;
    private String answer_specifier;
    private String answer_text;
    private String answer_image;
    private String corrected;

    public UserAnswersList(String user_id, String api_token, String quiz_id, String question_id, String question_order,
                           String answer_specifier, String answer_text, String answer_image, String corrected){
        this.user_id = user_id;
        this.api_token = api_token;
        this.quiz_id = quiz_id;
        this.question_id = question_id;
        this.question_order = question_order;
        this.answer_specifier = answer_specifier;
        this.answer_text = answer_text;
        this.answer_image = answer_image;
        this.corrected = corrected;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getApi_token() {
        return api_token;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuestion_order() {
        return question_order;
    }

    public String getAnswer_specifier() {
        return answer_specifier;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public String getAnswer_image() {
        return answer_image;
    }

    public String getCorrected() {
        return corrected;
    }
}
