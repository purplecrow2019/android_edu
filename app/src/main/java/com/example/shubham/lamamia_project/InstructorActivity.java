package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorActivity extends AppCompatActivity {

    Button followButton, un_followButton;
    TextView followersCountText, instructorDescriptionText, instructorDesignationText,
            instructorIdHolder, instructorName, courseCount;
    LinearLayout progressBar, content;
    CircleImageView instructorImage;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        // Initiating volley requestQueue
        rq = Volley.newRequestQueue(this);

        // Linear layouts
        progressBar = findViewById(R.id.linearLayoutInstructorActivityProgress);
        content = findViewById(R.id.linearLayoutInstructorActivityContainer);

        // Buttons
        followButton = findViewById(R.id.buttonInstructorActivityFollow);
        un_followButton = findViewById(R.id.buttonInstructorActivityUnFollow);

        // TextViews
        followersCountText = findViewById(R.id.followersCount);
        courseCount = findViewById(R.id.courseCount);
        instructorDescriptionText = findViewById(R.id.textViewInstructorActivityDescription);
        instructorDesignationText = findViewById(R.id.textViewInstructorActivityDesignation);
        instructorIdHolder = findViewById(R.id.textViewInstructorActivityInstructorIdHolder);
        instructorName = findViewById(R.id.textViewInstructorActivityInstructorName);

        // Image view
        instructorImage = findViewById(R.id.imageViewInstructorActivityInstructorImage);

        // Setting instructor id
        try{
            String instructor_id = Objects.requireNonNull(getIntent().getExtras()).getString("instructor_id");
            instructorIdHolder.setText(instructor_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Onclick follow button
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                follow(instructorIdHolder.getText().toString().trim());

            }
        });

        // Onclick un_follow button
        un_followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un_follow(instructorIdHolder.getText().toString().trim());
            }
        });

        // Calling load method
        loadInstructor(instructorIdHolder.getText().toString().trim());

    }// End on create



    // Loading instructor
    public void loadInstructor(String instructor_id){

        // Making visible linear layout progress
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String DEFAULT = "";
        String user_id = sharedPreferences.getString("user_id", DEFAULT);

        // Making url
        String url = Constants.GET_INSTRUCTOR_DATA_BY_ID+instructor_id+"?user_id="+user_id+"";

        // Sending request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    // Checking if got status 200
                    if (status.equals("200")){

                        JSONArray instructor_data = jsonObject.getJSONArray("instructor");

                        for (int i = 0; i < instructor_data.length(); i++) {
                            JSONObject row = instructor_data.getJSONObject(i);
                            followersCountText.setText(row.getString("followers"));

                            if (row.getString("following").equals("1")){
                                followButton.setVisibility(View.GONE);
                                un_followButton.setVisibility(View.VISIBLE);
                            } else if(row.getString("following").equals("2")){
                                un_followButton.setVisibility(View.GONE);
                                followButton.setVisibility(View.VISIBLE);
                            }

                            // Course count
                            courseCount.setText(row.getString("total_courses"));
                            // Instructor image
                            Picasso.get().load(row.getString("instructor_image")).fit().into(instructorImage);
                            // Instructor name
                            instructorName.setText(row.getString("instructor_name"));
                            Objects.requireNonNull(getSupportActionBar()).setTitle(row.getString("instructor_name"));
                            instructorDescriptionText.setText(Html.fromHtml(row.getString("instructor_about")));
                            instructorDesignationText.setText(row.getString("instructor_achievements"));

                            progressBar.setVisibility(View.GONE);
                            content.setVisibility(View.VISIBLE);

                        }

                    }else{
                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "");
                    }// End checking if got status 200

                } catch (JSONException e) {
                    e.printStackTrace();

                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
            }
        });

        rq.add(stringRequest);

    }// End loading instructor



    // Follow function
    public void follow(String instructor_id){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String DEFAULT = "";
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        String api_token = sharedPreferences.getString("api_token", DEFAULT);

        // Making url
        String url = Constants.FOLLOW_INSTRUCTOR+instructor_id+"?user_id="+user_id+"&api_token="+api_token+"";

        // Sending request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    // Checking if got status 200
                    if (status.equals("200")){

                        // Calling load method
                        loadInstructor(instructorIdHolder.getText().toString().trim());

                    }else{
                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "");
                    }// End checking if got status 200

                } catch (JSONException e) {
                    e.printStackTrace();

                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
            }
        });

        rq.add(stringRequest);

    }// End follow function

    // un_follow function
    public void un_follow(String instructor_id){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String DEFAULT = "";
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        String api_token = sharedPreferences.getString("api_token", DEFAULT);

        // Making url
        String url = Constants.UN_FOLLOW_INSTRUCTOR+instructor_id+"?user_id="+user_id+"&api_token="+api_token+"";

        // Sending request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    // Checking if got status 200
                    if (status.equals("200")){

                        // Calling load method
                        loadInstructor(instructorIdHolder.getText().toString().trim());

                    }else{
                        String message = jsonObject.getString("status") + "message" + jsonObject.getString("message");
                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), message);
                    }// End checking if got status 200

                } catch (JSONException e) {
                    e.printStackTrace();

                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
            }
        });

        rq.add(stringRequest);

    }// End unfollow function

}// End oncreate
