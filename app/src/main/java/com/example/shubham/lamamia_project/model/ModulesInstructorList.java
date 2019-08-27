package com.example.shubham.lamamia_project.model;

public class ModulesInstructorList {

    private String module_id;
    private String module_name;

    public ModulesInstructorList(String module_id, String module_name){
        this.module_id = module_id;
        this.module_name = module_name;
    }

    public String getModule_id() {
        return module_id;
    }

    public String getModule_name() {
        return module_name;
    }

}
