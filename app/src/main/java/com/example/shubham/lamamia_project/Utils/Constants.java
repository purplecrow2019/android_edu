package com.example.shubham.lamamia_project.Utils;

public class Constants {

    // BASE URLs
    private static String url = "https://www.lamamia.in/";
    private static String BASE_URL_USER = url + "api/public/api/user/";
    private static String BASE_URL_INSTRUCTOR = url + "api/public/api/instructor/";
    private static String BASE_WEB_URL_INSTRUCTOR = url + "api/public/instructor/";

    // Device token for fire base notification
    public static String SEND_DEVICE_TOKEN = BASE_URL_USER + "device/token/";

    // LOGIN
    public static String LOGIN_USER = BASE_URL_USER + "login/phone";
    public static String LOGIN_USER_CONFIRM = BASE_URL_USER + "login/phone/verification";

    // SWITCHING REQUESTS
    public static String SWITCH_USER_TO_INSTRUCTOR = BASE_URL_USER + "login/switch/user";
    public static String SWITCH_INSTRUCTOR_TO_USER = BASE_URL_USER + "login/switch/instructor";

    // UPDATE CHECKER
    public static String VERSION_CHECKER = BASE_URL_USER + "updater";

    // USER PROFILE
    public static String VIEW_USER_PROFILE = BASE_URL_USER + "user/profile/view/";
    public static String UPDATE_PROFILE_USER = BASE_URL_USER + "user/profile/update/";
    public static String UPLOAD_PROFILE_IMAGE_USER = BASE_URL_USER + "user/image/upload/";

    // INSTRUCTOR
    public static String GET_INSTRUCTOR_DATA_BY_ID = BASE_URL_USER + "instructor/";
    public static String FOLLOW_INSTRUCTOR = BASE_URL_USER + "instructor/follow/";
    public static String UN_FOLLOW_INSTRUCTOR = BASE_URL_USER + "instructor/unfollow/";

    // COURSE
    public static String GET_INSTRUCTOR_LIST_BY_COURSE = BASE_URL_USER + "instructors/course/";
    public static String GET_MODULE_LIST_BY_COURSE = BASE_URL_USER + "module/data/course/";
    public static String GET_COURSE_INFORMATION_BY_CATEGORY = BASE_URL_USER + "course/category/";
    public static String GET_VIDEO_COURSES = BASE_URL_USER + "courses/video";
    public static String GET_QUIZ_COURSES = BASE_URL_USER + "courses/quiz";

    // NOTIFICATIONS
    public static String GET_NOTIFICATIONS = BASE_URL_USER + "notification/user/";
    public static String GET_NOTIFICATION_COUNT = BASE_URL_USER + "notification/count/";

    // HELP
    public static String SEND_HELP_MESSAGE = BASE_URL_USER + "send/help";

    // MODULE AND VIDEOS
    public static String GET_VIDEO_BY_ORDER = BASE_URL_USER + "video/order/module";
    public static String POST_USER_CURRENT_VIDEO_POSITION = BASE_URL_USER + "post/video/current/position";
    public static String GET_VIDEO_LIST_BY_MODULE = BASE_URL_USER + "video/module/";
    public static String GET_ALL_VALUE_BY_VIDEO = BASE_URL_USER + "module/video/comment/all/"; // video_id
    public static String GET_QUIZ_LEADERBOARD = BASE_URL_USER + "leaderboard/"; // quiz_id
    public static String GET_QUIZ_QUESTION_BY_ORDER = BASE_URL_USER + "question/user/quiz/";
    public static String POST_USER_ANSWER = BASE_URL_USER + "question/answer/add/";
    public static String GET_USER_ANSWER_BY_QUESTION = BASE_URL_USER + "quiz/question/answer/";
    public static String USER_CLEAR_ALL_QUIZ_DATA = BASE_URL_USER + "quiz/clear/"; // quiz_id

    // Comment posting for parent and child
    public static String POST_PARENT_COMMENT = BASE_URL_USER + "comment/";
    public static String GET_VIDEO_COMMENTS = BASE_URL_USER + "comment/video/";
    public static String GET_COURSE_INFORMATION = BASE_URL_USER + "course/information/";

    // Course Ratings
    public static String GET_RATINGS_BY_COURSE = BASE_URL_USER + "rating/course/";
    public static String POST_RATING_BY_USER = BASE_URL_USER + "rating/course/add/";
    public static String POST_COMMENT_ON_RATING = BASE_URL_USER + "rating/comment/add";
    public static String GET_ALL_COMMENTS_ON_RATINGS = BASE_URL_USER + "rating/course/reviews/";

    // Course share
    public static String POST_SHARE_COUNT = BASE_URL_USER + "course/share/count/";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// INSTRUCTOR APIS ////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Profile
    public static String VIEW_INSTRUCTOR_PROFILE = BASE_URL_INSTRUCTOR + "profile/view/";
    public static String UPDATE_PROFILE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "profile/update/";
    public static String UPLOAD_PROFILE_IMAGE = BASE_URL_INSTRUCTOR + "image/upload/";

    // Course
    public static String GET_CATEGORIES_LIST = BASE_URL_INSTRUCTOR + "categories";
    public static String POST_COURSE_CREATE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "course";
    public static String GET_COURSES_INSTRUCTOR = BASE_URL_INSTRUCTOR + "courses/";
    public static String GET_COURSES_ALL_DATA_INSTRUCTOR = BASE_URL_INSTRUCTOR + "courses/view/";
    public static String UPDATE_COURSE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "course/update/";
    public static String GET_COURSE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "course/";
    public static String DELETE_COURSE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "course/delete/";
    public static String COURSE_PUBLISH_REQUEST_INSTRUCTOR = BASE_URL_INSTRUCTOR + "course/publish/";
    public static String GET_LIVE_COURSES_INSTRUCTOR = BASE_URL_INSTRUCTOR + "live/courses"; // Instructor id and token

    // Module
    public static String GET_MODULES_INSTRUCTOR = BASE_URL_INSTRUCTOR + "modules/";
    public static String POST_CREATE_MODULE = BASE_URL_INSTRUCTOR + "module";
    public static String UPDATE_MODULE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "module/update/";
    public static String GET_MODULE_INSTRUCTOR = BASE_URL_INSTRUCTOR +"module/";
    public static String DELETE_MODULE_INSTRUCTOR = BASE_URL_INSTRUCTOR + "module/delete/";

    // Videos APIs
    public static String GET_VIDEOS_INSTRUCTOR = BASE_URL_INSTRUCTOR + "videos/";
    public static String GET_VIDEO_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/";
    public static String CREATE_VIDEO_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video";
    public static String UPDATE_VIDEO_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/update/";
    public static String DELETE_VIDEO_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/delete/";
    public static String UPLOAD_VIDEO_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/upload/";
    public static String VIDEO_MOVE_UP_ONE_LEVEL = BASE_URL_INSTRUCTOR + "video/move/up/";
    public static String VIDEO_MOVE_DOWN_ONE_LEVEL = BASE_URL_INSTRUCTOR + "video/move/down/";

    // Video files APIs
    public static String GET_VIDEO_ATTACHMENT_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/attachment/";
    public static String CREATE_VIDEO_ATTACHMENT_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/attachment";
    public static String UPLOAD_VIDEO_ATTACHMENT_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/attachment/upload/";
    public static String UPDATE_VIDEO_ATTACHMENT_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/attachment/update/";
    public static String DELETE_VIDEO_ATTACHMENT_INSTRUCTOR = BASE_URL_INSTRUCTOR + "video/attachment/delete/";

    // Quiz APIs
    public static String CREATE_QUIZ_INSTRUCTOR = BASE_URL_INSTRUCTOR + "quiz";
    public static String GET_QUIZ_INSTRUCTOR = BASE_URL_INSTRUCTOR + "quiz/";
    public static String UPDATE_QUIZ_INSTRUCTOR = BASE_URL_INSTRUCTOR + "quiz/"; // quiz id
    public static String LIVE_QUIZ_INSTRUCTOR = BASE_URL_INSTRUCTOR + "live/quiz/"; //quiz_id
    public static String GET_QUIZ_PUBLISH_DETAILS_INSTRUCTOR = BASE_URL_INSTRUCTOR + "quiz/publish/details/";

    // Question APIs
    public static String GET_QUESTIONS_INSTRUCTOR = BASE_URL_INSTRUCTOR + "question/all/"; // quiz id
    public static String DELETE_QUESTION_INSTRUCTOR = BASE_URL_INSTRUCTOR + "question/"; // question id

    // Activity center
    public static String GET_COURSE_MONETIZATION_STATUS = BASE_URL_INSTRUCTOR + "monetization/report/course/";// course_id
    public static String POST_MONETIZATION_TURN_ON = BASE_URL_INSTRUCTOR + "monetization/turn/on/";// course_id
    public static String GET_COURSE_INSIGHTS_REPORT = BASE_URL_INSTRUCTOR + "insights/report/course/";// course_id
    public static String GET_DATES_INSIGHTS_REPORT = BASE_URL_INSTRUCTOR + "insights/dates";
    public static String GET_VIDEO_INSIGHTS_REPORT = BASE_URL_INSTRUCTOR + "insights/report/video/"; // video_id
    public static String GET_BANK_ACCOUNT_DETAIL = BASE_URL_INSTRUCTOR + "bank/detail";
    public static String POST_BANK_ACCOUNT_DETAIL = BASE_URL_INSTRUCTOR + "bank/detail";
    public static String GET_PAYMENT_DETAILS_BY_COURSE = BASE_URL_INSTRUCTOR + "payments/";

    // WEB URLS
    public static String CREATE_WEB_INSTRUCTOR_QUESTION = BASE_WEB_URL_INSTRUCTOR + "question/"; // quiz id
    public static String UPDATE_WEB_INSTRUCTOR_QUESTION = BASE_WEB_URL_INSTRUCTOR + "question/update/"; //question id

}
