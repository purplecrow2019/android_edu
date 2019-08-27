package com.example.shubham.lamamia_project.model;

import java.util.ArrayList;

public class ParentModuleList {

    private String module_id;
    private String module_name;
    private String completed_percentage;
    private String total_duration;
    private ArrayList<ChildModuleList> childModuleLists;

    public String getModule_id(){
        return module_id;
    }

    public String setModule_id(){
        return module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String name) {
        this.module_name = name;
    }

    public void setCompleted_percentage(String completed_percentage) {
        this.completed_percentage = completed_percentage;
    }

    public String getCompleted_percentage() {
        return completed_percentage;
    }

    public void setTotal_duration(String total_duration) {
        this.total_duration = total_duration;
    }

    public String getTotal_duration() {
        return total_duration;
    }

    public ArrayList<ChildModuleList> getChildModuleLists() {
        return childModuleLists;
    }

    public void setChildModuleLists(ArrayList<ChildModuleList> childModuleLists) {
        this.childModuleLists = childModuleLists;
    }

}