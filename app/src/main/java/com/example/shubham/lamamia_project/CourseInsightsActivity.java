package com.example.shubham.lamamia_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.InstructorVideoInsightsAdapter;
import com.example.shubham.lamamia_project.model.VideoInsightsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CourseInsightsActivity extends AppCompatActivity {

    private Button buttonBack;
    private RecyclerView videoInfoHolder;
    private Spinner courseSpinner, datesSpinner;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<VideoInsightsList> listItems;
    private InstructorVideoInsightsAdapter adapter;
    private long course;
    private ProgressBar VideoInfoLoading;
    private TextView txtCoursePublishedDate, txtStartDate, txtEndDate;
    private Calendar calendar;
    private int startDay, startMonth, startYear, endDay, endMonth, endYear, picking = 0;
    private LinearLayout errorView, courseNotFound;
    private ScrollView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_insights);

        /* Main layout components */
        courseSpinner = findViewById(R.id.spinnerCourse);
        datesSpinner = findViewById(R.id.spinnerDates);
        VideoInfoLoading = findViewById(R.id.VideoInfoLoading);
        txtCoursePublishedDate = findViewById(R.id.txtCoursePublishedDate);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);

        txtStartDate.setVisibility(View.GONE);
        txtEndDate.setVisibility(View.GONE);

        courseNotFound = findViewById(R.id.courseNotAvailable);
        mainView = findViewById(R.id.mainView);
        errorView = findViewById(R.id.errorView);

        // Initializing elements for request
        listItems = new ArrayList<>();

        /* Main Information components */
        videoInfoHolder = findViewById(R.id.videoInfoHolder);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        mLayoutManager = new LinearLayoutManager(this);

        //Back button
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picking = 1;
                calendar = Calendar.getInstance();
                startYear = calendar.get(Calendar.YEAR);
                startMonth = calendar.get(Calendar.MONTH);
                startDay = calendar.get(Calendar.DAY_OF_MONTH);
                startShowDate(startYear, startMonth+1, startDay);
                startSetDate(v);
            }
        });


        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picking = 2;
                calendar = Calendar.getInstance();
                endYear = calendar.get(Calendar.YEAR);
                endMonth = calendar.get(Calendar.MONTH);
                endDay = calendar.get(Calendar.DAY_OF_MONTH);
                endShowDate(endYear, endMonth+1, endDay);
                endSetDate(v);
            }
        });

        /* Calling ge course method */
        getCourseList();

    }// End onCreate


    //-------------------- START DATE PICKER -------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void startSetDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            if (picking == 1){
                return new DatePickerDialog(this, myDateListener, startYear, startMonth, startDay);
            } else if (picking == 2){
                return new DatePickerDialog(this, myDateListener, endYear, endMonth, endDay);
            }
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    if (picking == 1){
                        startShowDate(arg1, arg2+1, arg3);
                    } else if(picking == 2) {
                        endShowDate(arg1, arg2+1, arg3);
                    }
                }
            };


    private void startShowDate(int year, int month, int day) {

        if (day < 10 && month < 10){
            txtStartDate.setText(new StringBuilder().append(year).append("-").append("0").append(month).append("-0").append(day));
        } else if(day < 10 && month >= 10){
            txtStartDate.setText(new StringBuilder().append(year).append("-").append(month).append("-0").append(day));
        } else if(day >= 10 && month < 10){
            txtStartDate.setText(new StringBuilder().append(year).append("-").append("0").append(month).append("-").append(day));
        } else if(day >= 10 && month >= 10){
            txtStartDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        }

        if (txtEndDate.getText().toString().trim().length() > 4
                && txtStartDate.getText().toString().trim().length() > 4){
            getCourseInsightsReport(course, 6);
        }
    }
    //----------------- / START DATE PICKING ---------------------------------------------------------

    //-------------------- START DATE PICKER -------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void endSetDate(View view) {
        showDialog(999);
    }

    private void endShowDate(int year, int month, int day) {

        if (day < 10 && month < 10){
            txtEndDate.setText(new StringBuilder().append(year).append("-").append("0").append(month).append("-0").append(day));
        } else if(day < 10 && month >= 10){
            txtEndDate.setText(new StringBuilder().append(year).append("-").append(month).append("-0").append(day));
        } else if(day >= 10 && month < 10){
            txtEndDate.setText(new StringBuilder().append(year).append("-").append("0").append(month).append("-").append(day));
        } else if(day >= 10 && month >= 10){
            txtEndDate.setText(new StringBuilder().append(year).append("-").append(month).append("-").append(day));
        }

        if (txtEndDate.getText().toString().trim().length() > 4
                && txtStartDate.getText().toString().trim().length() > 4){
            getCourseInsightsReport(course, 6);
        }

    }
    //----------------- / START DATE PICKING ---------------------------------------------------------

    /* method for getting course list */
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
                                        course = course_id.get(position);//This will be the student id.
                                        getDatesList();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }
                                });

                            } else {
                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                courseNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            courseNotFound.setVisibility(View.VISIBLE);
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
            mainView.setVisibility(View.GONE);
            courseNotFound.setVisibility(View.VISIBLE);
        }

    }// End method get course list


    /* method for getting course list */
    public void getDatesList() {

        // Making layout changes
        final List<Integer> date_id = new ArrayList<>();//add ids in this list
        final List<String> date_name = new ArrayList<>();//add names in this list

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.GET_DATES_INSIGHTS_REPORT
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

                            JSONArray array = jsonObject.getJSONArray("dates");

                            if (array.length() > 0) {

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    date_id.add(Integer.parseInt(o.getString("id")));
                                    date_name.add(o.getString("name"));
                                }

                                // Making layout changes
                                final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_small_layout, date_name);
                                adapter.setDropDownViewResource(R.layout.spinner_layout);
                                datesSpinner.setAdapter(adapter);
                                datesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        id = date_id.get(position);//This will be the student id.
                                        if (id != 6){
                                            getCourseInsightsReport(course,id);
                                            txtStartDate.setVisibility(View.GONE);
                                            txtEndDate.setVisibility(View.GONE);
                                        } else {
                                            txtStartDate.setVisibility(View.VISIBLE);
                                            txtEndDate.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }
                                });

                            } else {
                                // Making layout changes
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
        } else {
            // Making layout changes

        }

    }// End method get course list

    /* Method for getting course insights report */
    private void getCourseInsightsReport(long course_id, long date){

        videoInfoHolder.setVisibility(View.GONE);
        VideoInfoLoading.setVisibility(View.VISIBLE);


        listItems.clear();

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            // Making string url
            String url = Constants.GET_COURSE_INSIGHTS_REPORT + course_id
                    + "?instructor=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&token=" + sharedPreferences.getString("api_token", DEFAULT)
                    + "&interval_type=" + date
                    + "&start_date=" + txtStartDate.getText().toString().trim()
                    + "&end_date=" + txtEndDate.getText().toString().trim();

            // Sending request
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equals("200")) {

                            txtCoursePublishedDate.setText(jsonObject.getString("course_publish"));

                            JSONArray array = jsonObject.getJSONArray("data");
                            if (array.length() > 0) {

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject o = array.getJSONObject(i);
                                    VideoInsightsList item = new VideoInsightsList(
                                            o.getString("content_id"),
                                            o.getString("content_name"),
                                            o.getString("module_name"),
                                            o.getString("parameter_one_name"),
                                            o.getString("parameter_two_name"),
                                            o.getString("parameter_three_name"),
                                            o.getString("parameter_one"),
                                            o.getString("parameter_two"),
                                            o.getString("parameter_three")

                                    );
                                    listItems.add(item);
                                }

                                adapter = new InstructorVideoInsightsAdapter(listItems, getApplicationContext(), CourseInsightsActivity.this);
                                videoInfoHolder.setLayoutManager(mLayoutManager);
                                videoInfoHolder.setAdapter(adapter);

                                videoInfoHolder.setVisibility(View.VISIBLE);
                                VideoInfoLoading.setVisibility(View.GONE);

                            } else {

                            }

                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                }
            });
            requestQueue.add(stringRequest);
        }
    }// End method get course insights report


}
