package com.example.shubham.lamamia_project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.example.shubham.lamamia_project.adapter.RatingAdapter;
import com.example.shubham.lamamia_project.model.CourseRatingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class RatingActivity extends AppCompatActivity {

    // Initiating elements
    private ArrayList<CourseRatingList> course_rating_list = new ArrayList<>();
    private RatingAdapter rating_adapter;
    private RecyclerView recyclerViewRating;
    private LinearLayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private TextView ratingNotAvailable, courseIdHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Validating recycler elements
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRating = findViewById(R.id.recyclerViewRatingActivityRatings);

        // Validating all elements
        final Button buttonStarFive = findViewById(R.id.buttonRatingActivityStarFive);
        final Button buttonStarFour = findViewById(R.id.buttonRatingActivityStarFour);
        final Button buttonStarThree = findViewById(R.id.buttonRatingActivityStarThree);
        final Button buttonStarTwo = findViewById(R.id.buttonRatingActivityStarTwo);
        final Button buttonStarOne = findViewById(R.id.buttonRatingActivityStarOne);
        final Button buttonRecent = findViewById(R.id.buttonRatingActivityRecent);
        final TextView postRating = findViewById(R.id.imageViewRatingActivityPostRating);
        ratingNotAvailable = findViewById(R.id.textViewRatingActivityNotFoundText);
        courseIdHolder = findViewById(R.id.textViewRatingActivityCourseIdHolder);

        // Setting html text
        buttonRecent.setText(Html.fromHtml("नवीन"));
        buttonStarFive.setText(Html.fromHtml("&#9733; " + 5 ));
        buttonStarFour.setText(Html.fromHtml("&#9733; " + 4 ));
        buttonStarThree.setText(Html.fromHtml("&#9733; " + 3 ));
        buttonStarTwo.setText(Html.fromHtml("&#9733; " + 2 ));
        buttonStarOne.setText(Html.fromHtml("&#9733; " + 1 ));

        try{
            String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
            courseIdHolder.setText(course_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        postRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(RatingActivity.this, PostRatingActivity.class);
                activityChangeIntent.putExtra("course_id", courseIdHolder.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RatingActivity.this.startActivity(activityChangeIntent);
            }
        });

        // Most recent
        buttonRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim();
                getCourseRatingByRatings(url);
            }
        });

        // Button star one
        buttonStarOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() + "?rating=1"+"";
                getCourseRatingByRatings(url);
            }
        });

        // Button star two
        buttonStarTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() + "?rating=2"+"";
                getCourseRatingByRatings(url);
            }
        });

        // Button star three
        buttonStarThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() + "?rating=3"+"";
                getCourseRatingByRatings(url);
            }
        });

        // Image star four
        buttonStarFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() + "?rating=4"+"";
                getCourseRatingByRatings(url);
            }
        });

        // Button star five
        buttonStarFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStarFour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarThree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarTwo.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarOne.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonRecent.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.follow_button_style));
                buttonStarFive.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_login_design));
                String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() + "?rating=5"+"";
                getCourseRatingByRatings(url);
            }
        });


    }// End on create

    @Override
    protected void onResume(){
        super.onResume();
        String url = Constants.GET_RATINGS_BY_COURSE + courseIdHolder.getText().toString().trim() ;
        getCourseRatingByRatings(url);
    }

    // Getting course ratings
    public void getCourseRatingByRatings(String url){

        course_rating_list = new ArrayList<>();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getString("status").equals("200")) {

                            JSONArray array = jsonObject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                CourseRatingList item = new CourseRatingList(
                                        o.getString("course_rating_id"),
                                        o.getString("course_id"),
                                        o.getString("user_name"),
                                        o.getString("course_rating_point"),
                                        o.getString("course_rating_text"),
                                        o.getString("total_comments")
                                );
                                course_rating_list.add(item);
                            }

                            ratingNotAvailable.setVisibility(View.GONE);
                            recyclerViewRating.setVisibility(View.VISIBLE);

                            rating_adapter = new RatingAdapter(course_rating_list, getApplicationContext());
                            recyclerViewRating.setLayoutManager(mLayoutManager);
                            recyclerViewRating.setAdapter(rating_adapter);


                        } else if(jsonObject.getString("status").equals("404")){

                            recyclerViewRating.setVisibility(View.GONE);
                            ratingNotAvailable.setVisibility(View.VISIBLE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है। response");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है। error");
                }
            });

            requestQueue.add(stringRequest);

    }// End getting course rating

}// End class RatingActivity
