package com.example.shubham.lamamia_project.model;

public class VideoInsightsList {

    private String video_id;
    private String video_name;
    private String module_name;
    private String parameter_one_name;
    private String parameter_two_name;
    private String parameter_three_name;
    private String parameter_one;
    private String parameter_two;
    private String parameter_three;

    public VideoInsightsList(String video_id,
                             String video_name,
                             String module_name,
                             String parameter_one_name,
                             String parameter_two_name,
                             String parameter_three_name,
                             String parameter_one,
                             String parameter_two,
                             String parameter_three){

        this.video_id = video_id;
        this.video_name = video_name;
        this.module_name = module_name;
        this.parameter_one_name = parameter_one_name;
        this.parameter_two_name = parameter_two_name;
        this.parameter_three_name = parameter_three_name;
        this.parameter_one = parameter_one;
        this.parameter_two = parameter_two;
        this.parameter_three = parameter_three;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getModule_name() {
        return module_name;
    }

    public String getParameter_one_name() {
        return parameter_one_name;
    }

    public String getParameter_two_name() {
        return parameter_two_name;
    }

    public String getParameter_three_name() {
        return parameter_three_name;
    }

    public String getParameter_one() {
        return parameter_one;
    }

    public String getParameter_two() {
        return parameter_two;
    }

    public String getParameter_three() {
        return parameter_three;
    }
}
