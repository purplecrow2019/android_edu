package com.example.shubham.lamamia_project.Utils;

public class SmartWebView {

        //Permission variables
        public static boolean ASWP_JSCRIPT     = true;     //enable JavaScript for webview
        public static boolean ASWP_FUPLOAD     = true;     //upload file from webview
        public static boolean ASWP_CAMUPLOAD   = true;     //enable upload from camera for photos
        public static boolean ASWP_ONLYCAM		 = false;    //incase you want only camera files to upload
        public static boolean ASWP_MULFILE     = false;    //upload multiple files in webview
        public static boolean ASWP_LOCATION    = true;     //track GPS locations
        public static boolean ASWP_RATINGS     = true;     //show ratings dialog; auto configured, edit method get_rating() for customizations
        public static boolean ASWP_PBAR        = false;    //show progress bar in app
        public static boolean ASWP_ZOOM        = false;    //zoom control for webpages view
        public static boolean ASWP_SFORM       = false;    //save form cache and auto-fill information
        public static boolean ASWP_OFFLINE     = false;    //whether the loading webpages are offline or online
        public static boolean ASWP_EXTURL      = true;     //open external url with default browser instead of app webview

        //Configuration variables
        public static String ASWV_URL          = null; //complete URL of your website or webpage
        public static String ASWV_F_TYPE       = "image/*";  //to upload any file type using "*/*"; check file type references for more

        //Rating system variables
        public static int ASWR_DAYS            = 3;        //after how many days of usage would you like to show the dialoge
        public static int ASWR_TIMES           = 10;       //overall request launch times being ignored
        public static int ASWR_INTERVAL        = 2;        //reminding users to rate after days interval
}
