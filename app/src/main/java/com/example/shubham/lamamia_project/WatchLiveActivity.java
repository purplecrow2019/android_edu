package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.example.shubham.lamamia_project.adapter.CommentAdapter;
import com.example.shubham.lamamia_project.adapter.ExpandableListAdapter;
import com.example.shubham.lamamia_project.model.VideoCommentChildList;
import com.example.shubham.lamamia_project.model.VideoCommentParentList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class WatchLiveActivity extends AppCompatActivity {

    private ExpandableListView expListComments;
    private CommentAdapter commentAdapter;
    private ArrayList<VideoCommentParentList> videoCommentParentLists;
    private ArrayList<VideoCommentChildList> videoCommentChildLists;
    private LinearLayout linearLayoutComment;
    private RequestQueue rq;
    private LinearLayoutManager mLayoutManager;

    // form comment
    private EditText txtVideoParentComment, currentVideoIdHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_live);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(Build.VERSION.SDK_INT >= 21) {
            //only api 21 above
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        expListComments = findViewById(R.id.expandableListViewCommentsLive);
        rq = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        txtVideoParentComment = findViewById(R.id.editTextSendParentCommentLive);
        currentVideoIdHolder = findViewById(R.id.textViewCurrentVideoIdHolderLive);
        ImageButton btnVideoParentComment = findViewById(R.id.imageButtonSendParentCommentLive);

        try{

            String live_url = Objects.requireNonNull(getIntent().getExtras()).getString("live_url");
            String video_id = Objects.requireNonNull(getIntent().getExtras()).getString("content_id");
            currentVideoIdHolder.setText(video_id);
            watchVideoLive(live_url);
            getVideoComments(video_id);

        }catch (Exception e) {
            e.printStackTrace();
        }


        btnVideoParentComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendParentComment();
            }
        });

    }

    private void watchVideoLive(String live_url){

            WebView wbView = (WebView) findViewById(R.id.webViewWatchVideoLive);
            wbView.getSettings().setJavaScriptEnabled(true);
            wbView.loadUrl(live_url);

    }// End method getQuizData



    //------------------------------------------------------------- Getting comments for videos ----------------------------------------------
    public void getVideoComments(String video_id){

        videoCommentParentLists = new ArrayList<>();

        String url = Constants.GET_VIDEO_COMMENTS + video_id+"";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                if (s != null) {

                    try {
                        JSONArray JsonArrayComments = new JSONArray(s);

                        if(JsonArrayComments.length() > 0){

                            for (int k = 0; k < JsonArrayComments.length(); k++) {

                                JSONObject o = JsonArrayComments.getJSONObject(k);

                                VideoCommentParentList videoCommentParentList = new VideoCommentParentList();
                                videoCommentParentList.setVideo_id(o.getString("video_id"));
                                videoCommentParentList.setVideo_comment_text(o.getString("video_comment_text"));
                                videoCommentParentList.setVideo_comment_id(o.getString("video_comment_id"));
                                videoCommentParentList.setUser_name(o.getString("user_name"));
                                videoCommentParentList.setUser_image(o.getString("user_image"));
                                videoCommentParentList.setUser_type(o.getString("user_type"));

                                videoCommentParentList.setVideo_comment_text(videoCommentParentList.getVideo_comment_text());

                                videoCommentChildLists = new ArrayList<>();

                                JSONArray ja = new JSONArray(o.getString("child"));

                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jo = ja.getJSONObject(i);

                                    VideoCommentChildList videoCommentChildList = new VideoCommentChildList();

                                    videoCommentChildList.setVideo_id(jo.getString("video_id"));
                                    videoCommentChildList.setVideo_comment_id(jo.getString("video_comment_id"));
                                    videoCommentChildList.setVideo_comment_text(jo.getString("video_comment_text"));
                                    videoCommentChildList.setUser_name(jo.getString("user_name"));
                                    videoCommentChildList.setUser_image(jo.getString("user_image"));
                                    videoCommentChildList.setUser_type(jo.getString("user_type"));

                                    videoCommentChildLists.add(videoCommentChildList);
                                }

                                videoCommentParentList.setVideoCommentChildLists(videoCommentChildLists);
                                videoCommentParentLists.add(videoCommentParentList);
                            }
                        }

                        commentAdapter = new CommentAdapter(getApplicationContext(), videoCommentParentLists);
                        expListComments.setAdapter(commentAdapter);

                        // Setting dynamic height
                        int height = JsonArrayComments.length() * 500;

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please Check internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(stringRequest);

    }// End method getVideoComments

    //---------------------------------------------------------- Parent comment ----------------------------------------------------------------

    public void sendParentComment(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String DEFAULT = "";

            String user_id = sharedPreferences.getString("user_id", DEFAULT);
            String api_token = sharedPreferences.getString("api_token", DEFAULT);
            String user_type = sharedPreferences.getString("user_type", DEFAULT);
            String parent_comment_id = "0";
            String comment_text = txtVideoParentComment.getText().toString().trim();
            final String current_video_id = currentVideoIdHolder.getText().toString().trim();

            if (comment_text.isEmpty()){

                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया पहले एक टिप्पणी लिखें|");

            }else{

                // Needed user_id, api_token, text, video_id, parent_id;
                String url = Constants.POST_PARENT_COMMENT + current_video_id +"?user_id="+ user_id +"&api_token="
                        + api_token +"&user_type="+ user_type +"&text="+ comment_text +"&parent_id="+ parent_comment_id +"";

                url = url.replaceAll(" ", "%20");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            String message = response.getString("message");

                            int result_status = Integer.parseInt(status);

                            if (result_status == 200){

                                txtVideoParentComment.setText("");

                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);

                                Utility.showSnackBarSuccessShort(getApplicationContext(), findViewById(android.R.id.content), "आपकी टिप्पणी दर्ज कर दी गई है।");
                                getVideoComments(current_video_id);

                            } else {

                                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");

                    }
                });

                rq.add(jsonObjectRequest);

            }

        }

    }


}// End class
