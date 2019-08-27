package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonetizationMainActivity extends AppCompatActivity {

    private Spinner courseSpinner;
    private RequestQueue requestQueue;
    private Context context = this;
    private LinearLayout linearLayoutTnc, linearLayoutCg, statisticsHolder, viewError, viewNoCourses, courseMonetizationOn;
    private ScrollView mainView;
    private int tncInt = 0, cgInt = 0;
    private Button back, retry, backButton;
    private TextView txtCourseEligibleCriteria, txtFollowers, txtWatchHours;
    private ProgressBar progressBarWatchHours, progressBarFollowers;
    private ImageView termsAndConditionTick, communityGuidelinesTick;
    private long course;
    private String tnc, cmg;
    private Button paymentHistory, turnMonetizationOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monetization_main);

        requestQueue = Volley.newRequestQueue(this);

        // Content holders
        linearLayoutTnc = findViewById(R.id.termsAndCondition);
        linearLayoutCg = findViewById(R.id.communityGuidelines);
        statisticsHolder = findViewById(R.id.statisticsHolder);
        viewError =  findViewById(R.id.viewError);
        viewNoCourses = findViewById(R.id.viewNoCourses);
        courseMonetizationOn = findViewById(R.id.courseMonetizationOn);
        mainView = findViewById(R.id.mainView);

        // Buttons
        back = findViewById(R.id.back);
        backButton = findViewById(R.id.backButton);
        retry = findViewById(R.id.retry);

        termsAndConditionTick = findViewById(R.id.termsAndConditionTick);
        communityGuidelinesTick = findViewById(R.id.communityGuidelinesTick);

        //Text views
        txtCourseEligibleCriteria = findViewById(R.id.txtCourseEligibleCriteria);
        txtFollowers = findViewById(R.id.txtFollowers);
        txtWatchHours = findViewById(R.id.txtWatchHours);

        //Progress bars
        progressBarFollowers = findViewById(R.id.progressBarFollowers);
        progressBarWatchHours = findViewById(R.id.progressBarWatchHours);

        // Course spinner
        courseSpinner = findViewById(R.id.spinnerCourse);

        // Terms and conditions
        linearLayoutTnc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_monetization_terms_and_condition);

                TextView tncText = dialog.findViewById(R.id.tncText);
                tncText.setText(Html.fromHtml(tnc));

                WebView webViewTnC = dialog.findViewById(R.id.webViewTnC);
                webViewTnC.getSettings().setBuiltInZoomControls(true);
                webViewTnC.getSettings().setJavaScriptEnabled(true);
                webViewTnC.loadUrl("https://s3.ap-south-1.amazonaws.com/lamamia-videos/app/app_tnc.png");
                webViewTnC.setVisibility(View.VISIBLE);

                // Choose from gallery
                Button agree = dialog.findViewById(R.id.tncYes);
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tncInt = 1;
                        dialog.dismiss();
                        termsAndConditionTick.setImageResource(R.drawable.ic_check_answer_instructor);
                    }
                });

                // Choose from gallery
                Button cancel = dialog.findViewById(R.id.tncNo);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tncInt = 0;
                        dialog.dismiss();
                        termsAndConditionTick.setImageResource(0);
                    }
                });

                // Web log
                TextView viewImage = dialog.findViewById(R.id.viewImage);
                viewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent activityChangeIntent = new Intent(context, ImageViewerActivity.class);
                        activityChangeIntent.putExtra("image_url", "https://s3.ap-south-1.amazonaws.com/lamamia-videos/app/app_tnc.png");
                        activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(activityChangeIntent);
                    }
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        Button bankDetail = findViewById(R.id.bankDetail);
        bankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityChangeIntent = new Intent(context, InstructorPaymentDetailActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        // Terms and conditions
        linearLayoutCg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_monetization_community_guidelines);

                TextView tncText = dialog.findViewById(R.id.cmgText);
                tncText.setText(Html.fromHtml(cmg));

                // Choose from gallery
                Button agree = dialog.findViewById(R.id.cmgYes);
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cgInt = 1;
                        dialog.dismiss();
                        communityGuidelinesTick.setImageResource(R.drawable.ic_check_answer_instructor);
                    }
                });

                // Choose from gallery
                Button cancel = dialog.findViewById(R.id.cmgNo);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cgInt = 0;
                        dialog.dismiss();
                        communityGuidelinesTick.setImageResource(0);
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });



        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Monetization on
        turnMonetizationOn = findViewById(R.id.monetizationTurnOn);
        turnMonetizationOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tncInt == 1 && cgInt == 1){
                    monetizationOnNow();
                } else {
                    Toast.makeText(getApplicationContext(),"Please agree to terms & conditions and community guidelines.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Calling method
        getCourseList();
    }


    public void getCourseList() {

        // Making layout changes
        final List<Integer> course_id = new ArrayList<>();//add ids in this list
        final List<String> course_name = new ArrayList<>();//add names in this list

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.GET_LIVE_COURSES_INSTRUCTOR
                    + "?instructor=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&token=" + sharedPreferences.getString("api_token", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        String stringStatus = jsonObject.getString("status");
                        int intStatus = Integer.parseInt(stringStatus);

                        if (intStatus == 200) {
                            JSONArray array = jsonObject.getJSONArray("courses");

                            if (array.length() > 0) {

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    course_id.add(Integer.parseInt(o.getString("course_id")));
                                    course_name.add(o.getString("course_name"));
                                }

                                // Making layout changes
                                final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, course_name);
                                adapter.setDropDownViewResource(R.layout.spinner_layout);
                                courseSpinner.setAdapter(adapter);
                                courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        id = course_id.get(position);//This will be the student id.
                                        course = course_id.get(position);
                                        final String id_number = Long.toString(id);

                                        paymentHistory = findViewById(R.id.paymentHistory);
                                        paymentHistory.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent activityChangeIntent = new Intent(context, InstructorPaymentHistoryActivity.class);
                                                activityChangeIntent.putExtra("course_id", id_number);
                                                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(activityChangeIntent);
                                            }
                                        });
                                        getCourseMonetizationStatus(id_number);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }
                                });
                            } else {
                                // Making layout changes
                                viewError.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                viewNoCourses.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // Making layout changes
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Making layout changes
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    // Making layout changes
                }
            });
            requestQueue.add(stringRequest);
        }else{
            // Making layout changes
        }
    }


    // Getting monetization status
    private void getCourseMonetizationStatus(String course){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.GET_COURSE_MONETIZATION_STATUS + course
                    + "?instructor=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&token=" + sharedPreferences.getString("api_token", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        String stringStatus = jsonObject.getString("status");
                        int intStatus = Integer.parseInt(stringStatus);

                        if (intStatus == 200) {

                            txtFollowers.setText(jsonObject.getString("followers_text"));
                            txtWatchHours.setText(jsonObject.getString("watch_hours_text"));
                            txtCourseEligibleCriteria.setText(jsonObject.getString("course_eligibility"));

                            tnc = jsonObject.getString("tnc");
                            cmg = jsonObject.getString("cmg");

                            progressBarFollowers.setMax(Integer.parseInt(jsonObject.getString("max_followers")));
                            progressBarFollowers.setProgress(Integer.parseInt(jsonObject.getString("followers")));

                            progressBarWatchHours.setMax(Integer.parseInt(jsonObject.getString("max_watch_hours")));
                            progressBarWatchHours.setProgress(Integer.parseInt(jsonObject.getString("watch_hours")));

                        } else {
                            // Making layout changes
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Making layout changes
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    // Making layout changes
                }
            });
            requestQueue.add(stringRequest);
        } else {
            // Making layout changes
        }
    }


    // monet turn on
    public void monetizationOnNow(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.POST_COURSE_CREATE_INSTRUCTOR;

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200){

                                viewError.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                viewNoCourses.setVisibility(View.GONE);
                                courseMonetizationOn.setVisibility(View.VISIBLE);
                                finish();

                            } else {
                                // Making layout changes
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            // Making layout changes
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        return MyData;
                    }
                };
                requestQueue.add(StringRequest);
            } catch (Exception e){
                e.printStackTrace();
                // Making layout changes
            }
        } else {
            // Making layout changes
        }

    }// monet turn on

}
