package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PostRatingActivity extends AppCompatActivity {

    private TextView courseIdHolder;
    private EditText ratingText;
    private RatingBar ratingBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_rating);

        // Validating recycler elements
        requestQueue = Volley.newRequestQueue(this);

        // Validating elements
        Button rateButton = findViewById(R.id.editTextPostRatingActivityRatingButton);
        courseIdHolder = findViewById(R.id.textViewPostRatingActivityCourseIdHolder);
        ratingText = findViewById(R.id.editTextPostRatingActivityRatingText);
        ratingBar = findViewById(R.id.ratingBarPostRatingActivity);

        // Getting course id
        try{
            String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
            courseIdHolder.setText(course_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRating();
            }
        });

    }

    private void postRating(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("user_id") && sharedPreferences.contains("api_token") && sharedPreferences.contains("user_type")) {
            String DEFAULT = "na";
            String user_id = sharedPreferences.getString("user_id", DEFAULT);
            String api_token = sharedPreferences.getString("api_token", DEFAULT);
            int value = (int) ratingBar.getRating();

            String url = Constants.POST_RATING_BY_USER + courseIdHolder.getText().toString().trim()
                    + "?rating_point=" + value
                    + "&rating_text=" + ratingText.getText().toString().trim()
                    + "&user_id=" + user_id
                    + "&api_token=" + api_token +"";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getString("status").equals("200")) {

                            finish();

                        } else if(jsonObject.getString("status").equals("202")) {
                            Utility.showSnackBarSuccessShort(getApplicationContext(), findViewById(android.R.id.content), "आप पहले ही मूल्यांकन कर चुके हैं।");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है। error");
                }
            });

            requestQueue.add(stringRequest);
        }
    }
}
