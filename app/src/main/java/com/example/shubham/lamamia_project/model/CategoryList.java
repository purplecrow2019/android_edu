package com.example.shubham.lamamia_project.model;

public class CategoryList {

    private String category_id;
    private String category_name;

    public CategoryList(String category_id, String category_name){

        this.category_id = category_id;
        this.category_name = category_name;

    }

    public String getCategory_id(){
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }
}
