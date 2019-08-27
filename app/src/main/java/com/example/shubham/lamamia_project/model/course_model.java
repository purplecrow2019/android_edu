package com.example.shubham.lamamia_project.model;

import java.util.ArrayList;

    public class course_model {

        private Data data;
        private String message;
        private int status;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public class Comment {

            private String comment_id;
            private String course_id;
            private Object user_id;
            private String instructor_id;
            private String comment_text;
            private Object create_ts;
            private Object update_ts;
            private String status;
            private String user_name;
            private String instructor_name;

            public String getComment_id() {
                return comment_id;
            }

            public void setComment_id(String comment_id) {
                this.comment_id = comment_id;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public Object getUser_id() {
                return user_id;
            }

            public void setUser_id(Object user_id) {
                this.user_id = user_id;
            }

            public String getInstructor_id() {
                return instructor_id;
            }

            public void setInstructor_id(String instructor_id) {
                this.instructor_id = instructor_id;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
            }

            public Object getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(Object create_ts) {
                this.create_ts = create_ts;
            }

            public Object getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(Object update_ts) {
                this.update_ts = update_ts;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getInstructor_name() {
                return instructor_name;
            }

            public void setInstructor_name(String instructor_name) {
                this.instructor_name = instructor_name;
            }

        }

        public class Course {

            private String course_id;
            private String course_unique_name;
            private String category_id;
            private String course_name;
            private String course_description;
            private String course_language;
            private String course_outcome;
            private String course_requirement;
            private String course_description_full;
            private String course_target_audience;
            private String course_price;
            private String course_discounted_price;
            private String course_views;
            private String course_image_big;
            private String course_image_medium;
            private String course_image_small;
            private String status;
            private String create_ts;
            private String update_ts;
            private String category_name;

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public String getCourse_unique_name() {
                return course_unique_name;
            }

            public void setCourse_unique_name(String course_unique_name) {
                this.course_unique_name = course_unique_name;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getCourse_description() {
                return course_description;
            }

            public void setCourse_description(String course_description) {
                this.course_description = course_description;
            }

            public String getCourse_language() {
                return course_language;
            }

            public void setCourse_language(String course_language) {
                this.course_language = course_language;
            }

            public String getCourse_outcome() {
                return course_outcome;
            }

            public void setCourse_outcome(String course_outcome) {
                this.course_outcome = course_outcome;
            }

            public String getCourse_requirement() {
                return course_requirement;
            }

            public void setCourse_requirement(String course_requirement) {
                this.course_requirement = course_requirement;
            }

            public String getCourse_description_full() {
                return course_description_full;
            }

            public void setCourse_description_full(String course_description_full) {
                this.course_description_full = course_description_full;
            }

            public String getCourse_target_audience() {
                return course_target_audience;
            }

            public void setCourse_target_audience(String course_target_audience) {
                this.course_target_audience = course_target_audience;
            }

            public String getCourse_price() {
                return course_price;
            }

            public void setCourse_price(String course_price) {
                this.course_price = course_price;
            }

            public String getCourse_discounted_price() {
                return course_discounted_price;
            }

            public void setCourse_discounted_price(String course_discounted_price) {
                this.course_discounted_price = course_discounted_price;
            }

            public String getCourse_views() {
                return course_views;
            }

            public void setCourse_views(String course_views) {
                this.course_views = course_views;
            }

            public String getCourse_image_big() {
                return course_image_big;
            }

            public void setCourse_image_big(String course_image_big) {
                this.course_image_big = course_image_big;
            }

            public String getCourse_image_medium() {
                return course_image_medium;
            }

            public void setCourse_image_medium(String course_image_medium) {
                this.course_image_medium = course_image_medium;
            }

            public String getCourse_image_small() {
                return course_image_small;
            }

            public void setCourse_image_small(String course_image_small) {
                this.course_image_small = course_image_small;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(String create_ts) {
                this.create_ts = create_ts;
            }

            public String getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(String update_ts) {
                this.update_ts = update_ts;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

        }

        public class Course_rating {

            private String course_rating_id;
            private String course_rating_point;
            private String course_rating_text;
            private String course_id;
            private String user_id;
            private String status;
            private String create_ts;
            private Object update_ts;

            public String getCourse_rating_id() {
                return course_rating_id;
            }

            public void setCourse_rating_id(String course_rating_id) {
                this.course_rating_id = course_rating_id;
            }

            public String getCourse_rating_point() {
                return course_rating_point;
            }

            public void setCourse_rating_point(String course_rating_point) {
                this.course_rating_point = course_rating_point;
            }

            public String getCourse_rating_text() {
                return course_rating_text;
            }

            public void setCourse_rating_text(String course_rating_text) {
                this.course_rating_text = course_rating_text;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(String create_ts) {
                this.create_ts = create_ts;
            }

            public Object getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(Object update_ts) {
                this.update_ts = update_ts;
            }

        }

        public class Data {

            private ArrayList<Course> course = null;
            private ArrayList<Course_rating> course_rating = null;
            private ArrayList<Module> module = null;
            private ArrayList<Comment> comment = null;
            private ArrayList<Instructor> instructor = null;

            public ArrayList<Course> getCourse() {
                return course;
            }

            public void setCourse(ArrayList<Course> course) {
                this.course = course;
            }

            public ArrayList<Course_rating> getCourse_rating() {
                return course_rating;
            }

            public void setCourse_rating(ArrayList<Course_rating> course_rating) {
                this.course_rating = course_rating;
            }

            public ArrayList<Module> getModule() {
                return module;
            }

            public void setModule(ArrayList<Module> module) {
                this.module = module;
            }

            public ArrayList<Comment> getComment() {
                return comment;
            }

            public void setComment(ArrayList<Comment> comment) {
                this.comment = comment;
            }

            public ArrayList<Instructor> getInstructor() {
                return instructor;
            }

            public void setInstructor(ArrayList<Instructor> instructor) {
                this.instructor = instructor;
            }

        }

        public class Instructor {

            private String instructor_id;
            private String instructor_email;
            private String instructor_phone;
            private String instructor_password;
            private String instructor_first_name;
            private String instructor_middle_name;
            private String instructor_last_name;
            private String instructor_about;
            private Object instructor_achievements;
            private Object instructor_image;
            private String api_token;
            private String status;
            private String create_ts;
            private String update_ts;
            private String last_login_ts;
            private String course_id;

            public String getInstructor_id() {
                return instructor_id;
            }

            public void setInstructor_id(String instructor_id) {
                this.instructor_id = instructor_id;
            }

            public String getInstructor_email() {
                return instructor_email;
            }

            public void setInstructor_email(String instructor_email) {
                this.instructor_email = instructor_email;
            }

            public String getInstructor_phone() {
                return instructor_phone;
            }

            public void setInstructor_phone(String instructor_phone) {
                this.instructor_phone = instructor_phone;
            }

            public String getInstructor_password() {
                return instructor_password;
            }

            public void setInstructor_password(String instructor_password) {
                this.instructor_password = instructor_password;
            }

            public String getInstructor_first_name() {
                return instructor_first_name;
            }

            public void setInstructor_first_name(String instructor_first_name) {
                this.instructor_first_name = instructor_first_name;
            }

            public String getInstructor_middle_name() {
                return instructor_middle_name;
            }

            public void setInstructor_middle_name(String instructor_middle_name) {
                this.instructor_middle_name = instructor_middle_name;
            }

            public String getInstructor_last_name() {
                return instructor_last_name;
            }

            public void setInstructor_last_name(String instructor_last_name) {
                this.instructor_last_name = instructor_last_name;
            }

            public String getInstructor_about() {
                return instructor_about;
            }

            public void setInstructor_about(String instructor_about) {
                this.instructor_about = instructor_about;
            }

            public Object getInstructor_achievements() {
                return instructor_achievements;
            }

            public void setInstructor_achievements(Object instructor_achievements) {
                this.instructor_achievements = instructor_achievements;
            }

            public Object getInstructor_image() {
                return instructor_image;
            }

            public void setInstructor_image(Object instructor_image) {
                this.instructor_image = instructor_image;
            }

            public String getApi_token() {
                return api_token;
            }

            public void setApi_token(String api_token) {
                this.api_token = api_token;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(String create_ts) {
                this.create_ts = create_ts;
            }

            public String getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(String update_ts) {
                this.update_ts = update_ts;
            }

            public String getLast_login_ts() {
                return last_login_ts;
            }

            public void setLast_login_ts(String last_login_ts) {
                this.last_login_ts = last_login_ts;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

        }

        public class Module {

            private String module_id;
            private String course_id;
            private String module_name;
            private String status;
            private String create_ts;
            private String update_ts;
            private ArrayList<Video> video = null;

            public String getModule_id() {
                return module_id;
            }

            public void setModule_id(String module_id) {
                this.module_id = module_id;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(String create_ts) {
                this.create_ts = create_ts;
            }

            public String getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(String update_ts) {
                this.update_ts = update_ts;
            }

            public ArrayList<Video> getVideo() {
                return video;
            }

            public void setVideo(ArrayList<Video> video) {
                this.video = video;
            }

        }

        public class Video {

            private String video_id;
            private String module_id;
            private String video_name;
            private String video_description;
            private String video_url;
            private Object video_duration;
            private String status;
            private String create_ts;
            private String update_ts;

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getModule_id() {
                return module_id;
            }

            public void setModule_id(String module_id) {
                this.module_id = module_id;
            }

            public String getVideo_name() {
                return video_name;
            }

            public void setVideo_name(String video_name) {
                this.video_name = video_name;
            }

            public String getVideo_description() {
                return video_description;
            }

            public void setVideo_description(String video_description) {
                this.video_description = video_description;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public Object getVideo_duration() {
                return video_duration;
            }

            public void setVideo_duration(Object video_duration) {
                this.video_duration = video_duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreate_ts() {
                return create_ts;
            }

            public void setCreate_ts(String create_ts) {
                this.create_ts = create_ts;
            }

            public String getUpdate_ts() {
                return update_ts;
            }

            public void setUpdate_ts(String update_ts) {
                this.update_ts = update_ts;
            }

        }
    }

