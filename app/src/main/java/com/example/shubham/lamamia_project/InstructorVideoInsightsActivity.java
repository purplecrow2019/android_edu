package com.example.shubham.lamamia_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class InstructorVideoInsightsActivity extends AppCompatActivity {

    private TextView parameterOne, parameterTwo, parameterThree, parameterFour, parameterFive, parameterSix;
    private TextView parameterOneName, parameterTwoName, parameterThreeName, parameterFourName, parameterFiveName, parameterSixName;
    private int video_id;
    private TextView txtStartDate, txtEndDate, publishDate, viewingBetween, name;
    private Calendar calendar;
    private int startDay, startMonth, startYear, endDay, endMonth, endYear, picking = 0;
    private RequestQueue requestQueue;
    private Spinner datesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_video_insights);

        try {
            video_id = Integer.parseInt(Objects.requireNonNull(getIntent().getExtras()).getString("video_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        datesSpinner = findViewById(R.id.spinnerDates);

        //Text holders
        parameterOneName = findViewById(R.id.parameterOneName);
        parameterTwoName = findViewById(R.id.parameterTwoName);
        parameterThreeName = findViewById(R.id.parameterThreeName);
        parameterFourName = findViewById(R.id.parameterFourName);
        parameterFiveName = findViewById(R.id.parameterFiveName);
        parameterSixName = findViewById(R.id.parameterSixName);

        // Value holders
        parameterOne = findViewById(R.id.parameterOne);
        parameterTwo = findViewById(R.id.parameterTwo);
        parameterThree = findViewById(R.id.parameterThree);
        parameterFour = findViewById(R.id.parameterFour);
        parameterFive = findViewById(R.id.parameterFive);
        parameterSix = findViewById(R.id.parameterSix);
        publishDate = findViewById(R.id.publishDate);
        viewingBetween = findViewById(R.id.viewingBetween);
        name = findViewById(R.id.name);

        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));

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

        // Calling method
        getDatesList();
    }


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
            getViewInsight(video_id, 6);
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
            getViewInsight(video_id, 6);
        }

    }
    //----------------- / START DATE PICKING ---------------------------------------------------------


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
                                            getViewInsight(video_id,id);
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


    /*
     * API should include the following values
     * video_name, three_second_views, ten_second_views,
     * */
    private void getViewInsight(int video, long date){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            // Making string url
            String url = Constants.GET_VIDEO_INSIGHTS_REPORT
                    + video
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
                            name.setText(jsonObject.getString("name"));
                            viewingBetween.setText(jsonObject.getString("viewing_between"));
                            publishDate.setText(jsonObject.getString("publish_date"));

                            parameterOne.setText(jsonObject.getString("parameter_one"));
                            parameterTwo.setText(jsonObject.getString("parameter_two"));
                            parameterThree.setText(jsonObject.getString("parameter_three"));
                            parameterFour.setText(jsonObject.getString("parameter_four"));
                            parameterFive.setText(jsonObject.getString("parameter_five"));
                            parameterSix.setText(jsonObject.getString("parameter_six"));

                            parameterOneName.setText(jsonObject.getString("parameter_one_name"));
                            parameterTwoName.setText(jsonObject.getString("parameter_two_name"));
                            parameterThreeName.setText(jsonObject.getString("parameter_three_name"));
                            parameterFourName.setText(jsonObject.getString("parameter_four_name"));
                            parameterFiveName.setText(jsonObject.getString("parameter_five_name"));
                            parameterSixName.setText(jsonObject.getString("parameter_six_name"));
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

    }// End method get Video Insight

}// End class instructor video insights
