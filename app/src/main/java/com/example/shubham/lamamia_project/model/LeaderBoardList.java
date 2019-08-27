package com.example.shubham.lamamia_project.model;

public class LeaderBoardList {

    private String user_id;
    private String user_name;
    private String obtained_marks;
    private String total_time;

    public LeaderBoardList(String user_id, String user_name, String obtained_marks, String total_time){
        this.user_id = user_id;
        this.user_name = user_name;
        this.obtained_marks = obtained_marks;
        this.total_time = total_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getObtained_marks() {
        return obtained_marks;
    }

    public String getTotal_time() {
        return total_time;
    }
}
