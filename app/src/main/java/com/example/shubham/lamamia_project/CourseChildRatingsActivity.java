package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.example.shubham.lamamia_project.adapter.CourseChildRatingsAdapter;
import com.example.shubham.lamamia_project.model.CourseRatingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CourseChildRatingsActivity extends AppCompatActivity {

    // Adding view elements type
    private TextView parentCountRating, parentUserName, parentCommentText, courseIdHolder, courseRatingIdHolder;
    private EditText postCommentText;
    private ArrayList<CourseRatingList> course_rating_list = new ArrayList<>();
    private CourseChildRatingsAdapter rating_adapter;
    private RecyclerView recyclerViewChildComments;
    private LinearLayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private LinearLayout progress;
    private ConstraintLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_child_ratings);

        // Initializing views
        ImageButton postCommentButton = findViewById(R.id.courseChildRatingCommentPostButton);
        postCommentText = findViewById(R.id.courseChildRatingCommentPostText);
        recyclerViewChildComments = findViewById(R.id.recyclerViewChildRatings);
        parentCountRating = findViewById(R.id.textViewListRatingCountRating);
        parentUserName = findViewById(R.id.textViewListRatingUserName);
        parentCommentText = findViewById(R.id.textViewListRatingText);
        courseIdHolder = findViewById(R.id.course_id_holder);
        courseRatingIdHolder = findViewById(R.id.course_rating_id_holder);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        progress = findViewById(R.id.progressView);
        content = findViewById(R.id.mainContent);

        // post button on click
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChildCommentRating();
            }
        });

        // Getting course id and course rating id
        try{
            String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
            String course_rating_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_rating_id");

            // Setting value of courses
            courseIdHolder.setText(course_id);
            courseRatingIdHolder.setText(course_rating_id);

            // Getting views
            String url = Constants.GET_ALL_COMMENTS_ON_RATINGS + course_rating_id;
            getAllComments(url);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getAllComments(String url){

        course_rating_list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equals("200")) {

                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject parent = array.getJSONObject(0);

                        parentCommentText.setText(parent.getString("course_rating_text"));
                        parentCountRating.setText(parent.getString("course_rating_point"));
                        parentUserName.setText(parent.getString("user_name"));

                        JSONArray child = parent.getJSONArray("childs");

                        if (child.length() > 0){

                            for (int i = 0; i < child.length(); i++) {
                                JSONObject o = child.getJSONObject(i);
                                CourseRatingList item = new CourseRatingList(
                                        o.getString("course_rating_id"),
                                        o.getString("course_id"),
                                        o.getString("user_name"),
                                        o.getString("course_rating_point"),
                                        o.getString("course_rating_text"),
                                        o.getString("course_id")
                                );
                                course_rating_list.add(item);
                            }
                            rating_adapter = new CourseChildRatingsAdapter(course_rating_list, getApplicationContext());
                            recyclerViewChildComments.setLayoutManager(mLayoutManager);
                            recyclerViewChildComments.setAdapter(rating_adapter);
                            progress.setVisibility(View.GONE);
                            content.setVisibility(View.VISIBLE);

                        } else {

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "हमें कुछ समस्या है। कृपया कुछ समय बाद पुनः प्रयास करें।");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
            }
        });

        requestQueue.add(stringRequest);


    }// End method getAllComments

    public void sendChildCommentRating(){

        String DEFAULT = "null";
        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id") && sharedPreferences.contains("user_type")) {

            if (postCommentText.getText().toString().trim().isEmpty()){
                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया पहले एक टिप्पणी लिखें|");
            }else{

                // Needed user_id, api_token, text, video_id, parent_id;
                String url = Constants.POST_COMMENT_ON_RATING
                        +"?user_id="+ sharedPreferences.getString("user_id", DEFAULT)
                        +"&user_type="+ sharedPreferences.getString("user_type", DEFAULT)
                        +"&text="+ postCommentText.getText().toString().trim()
                        +"&course_id=" + courseIdHolder.getText().toString().trim()
                        +"&course_rating_id="+ courseRatingIdHolder.getText().toString().trim() +"";

                        String new_url = url.replaceAll(" ", "%20");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, new_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            String message = response.getString("message");

                            int result_status = Integer.parseInt(status);

                            if (result_status == 200){

                                String url = Constants.GET_ALL_COMMENTS_ON_RATINGS + courseIdHolder.getText().toString().trim();
                                getAllComments(url);

                                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "आपकी टिप्पणी दर्ज कर दी गई है।");

                            } else {

                                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), message);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");

                    }
                });

                requestQueue.add(jsonObjectRequest);


            }
        }

    }// End method sendChildCommentRating
}
