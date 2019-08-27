package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class InstructorQuizLive extends AppCompatActivity {

    private EditText videoIdHolder;
    private ConstraintLayout mainView;
    private LinearLayout processing, error, deleted, publishTimeSee, unPublishTimeSee, timeCounter;
    private RequestQueue requestQueue;
    private EditText day, hour, minute;
    private int startDay, startMonth, startYear, startHour, startMinute;
    private TextView publish, end, publishNow, publishLater, endNow, endNot, endLater, startDate,
            startTime, publishTimeSeeDate, unPublishTimeSeeDate, daysLeft, hoursLeft, minutesLeft,
            secondsLeft, timeStatusEndStart;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_live);

        // Casting elements
        videoIdHolder = findViewById(R.id.videoId);
        Button back = findViewById(R.id.back);
        Button live = findViewById(R.id.btnLive);

        // Start date time
        publish = findViewById(R.id.publish);
        publishNow = findViewById(R.id.publishNow);
        publishLater = findViewById(R.id.publishLater);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);

        // Time counters
        daysLeft = findViewById(R.id.txtViewDayLeft);
        hoursLeft = findViewById(R.id.txtViewHourLeft);
        minutesLeft = findViewById(R.id.txtViewMinuteLeft);
        secondsLeft = findViewById(R.id.txtViewSecondLeft);
        timeStatusEndStart = findViewById(R.id.timeStatusEndStart);
        timeCounter = findViewById(R.id.timeCounter);

        // End date time
        end = findViewById(R.id.end);
        day = findViewById(R.id.endDay);
        hour = findViewById(R.id.endHour);
        minute = findViewById(R.id.endMinute);
        endNow = findViewById(R.id.txtEndNow);
        endNot = findViewById(R.id.txtNotEnd);
        endLater = findViewById(R.id.txtEndTime);
        publishTimeSee = findViewById(R.id.publishTimeSee);
        publishTimeSeeDate = findViewById(R.id.publishTimeSeeDate);
        unPublishTimeSee = findViewById(R.id.unPublishTimeSee);
        unPublishTimeSeeDate = findViewById(R.id.unPublishTimeSeeDate);

        requestQueue = Volley.newRequestQueue(this);
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        deleted = findViewById(R.id.deleted);

        try {
            String quiz_name = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_name");
            setTitle(quiz_name);
            String quiz_id = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_id");
            videoIdHolder.setText(quiz_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Setting data
        publish.setText("1");
        end.setText("1");
        publishNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
        publishLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);

        endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
        endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
        endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
        startTime.setVisibility(View.GONE);
        startDate.setVisibility(View.GONE);
        day.setVisibility(View.GONE);
        hour.setVisibility(View.GONE);
        minute.setVisibility(View.GONE);

        // Text change start date time
        startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { showStartDateTime(); }
        });
        startTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { showStartDateTime(); }
        });
        // End text change start date time


        // Text change end date time
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { showEndDateTime(); }
        });
        hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { showEndDateTime(); }
        });
        minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { showEndDateTime(); }
        });
        // End Text change end date time

        // delete operation
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        // back operation
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // startDate on click
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                startYear = calendar.get(Calendar.YEAR);
                startMonth = calendar.get(Calendar.MONTH);
                startDay = calendar.get(Calendar.DAY_OF_MONTH);
                showDate(startYear, startMonth+1, startDay);
                setDate(v);
            }
        });

        // startTime on click
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartTime();
            }
        });

        // setting publish now
        publishNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish.setText("1");
                publishNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                publishLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                startTime.setVisibility(View.GONE);
                startDate.setVisibility(View.GONE);
                publishTimeSee.setVisibility(View.GONE);
                startDate.setText("");
                startTime.setText("");
            }
        });

        // setting publish later
        publishLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish.setText("2");
                publishTimeSee.setVisibility(View.VISIBLE);
                publishLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                publishNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                startDate.setVisibility(View.VISIBLE);
                startTime.setVisibility(View.VISIBLE);
            }
        });


        endNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end.setText("1");
                unPublishTimeSee.setVisibility(View.GONE);
                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                day.setVisibility(View.GONE);
                hour.setVisibility(View.GONE);
                minute.setVisibility(View.GONE);
                day.setText("");
                hour.setText("");
                minute.setText("");
            }
        });

        endNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end.setText("2");
                unPublishTimeSee.setVisibility(View.GONE);
                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                day.setVisibility(View.GONE);
                hour.setVisibility(View.GONE);
                minute.setVisibility(View.GONE);
                day.setText("");
                hour.setText("");
                minute.setText("");
            }
        });

        endLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end.setText("3");
                unPublishTimeSee.setVisibility(View.VISIBLE);
                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                day.setVisibility(View.VISIBLE);
                hour.setVisibility(View.VISIBLE);
                minute.setVisibility(View.VISIBLE);
            }
        });

        // Calling to get information
        getQuizInfo();
    }

    //-------------------- START DATE PICKER -------------------------------------------------------
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, startYear, startMonth, startDay);
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
                    showDate(arg1, arg2+1, arg3);
                }
    };


    private void showDate(int year, int month, int day) {

        if (day < 10 && month < 10){
            startDate.setText(new StringBuilder().append("0").append(month).append("/0").append(day).append("/").append(year));
        } else if(day < 10 && month > 10){
            startDate.setText(new StringBuilder().append(month).append("/0").append(day).append("/").append(year));
        } else if(day > 10 && month < 10){
            startDate.setText(new StringBuilder().append("0").append(month).append("/").append(day).append("/").append(year));
        } else if(day > 10 && month > 10){
            startDate.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
        }
    }
    //----------------- / START DATE PICKING ---------------------------------------------------------



    //----------------- START TIME PICKING ---------------------------------------------------------
    private void setStartTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String startingTime = hourOfDay + ":" + minute;
                        startTime.setText(startingTime);

                    }
                }, startHour, startMinute, false);
        timePickerDialog.show();
    }
    //---------------- / START TIME PICKING ----------------------------------------------------------


    // validating date and time
    public void validateData(){
        boolean startDateSet = false, endDateSet = false;

        // Checking for date set
        if (Integer.parseInt(publish.getText().toString().trim()) == 1){
            startDateSet = true;
        } else if(Integer.parseInt(publish.getText().toString().trim()) == 2) {
            if (startDate.getText().toString().trim().isEmpty() || startTime.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Please fill Date and time.", Toast.LENGTH_SHORT).show();
            } else {
                startDateSet = true;
            }
        }

        // Checking for time set
        if (Integer.parseInt(end.getText().toString().trim()) == 1){
            endDateSet = true;
        } else if (Integer.parseInt(end.getText().toString().trim()) == 2){
            endDateSet = true;
        } else if (Integer.parseInt(end.getText().toString().trim()) == 3){
            if (day.getText().toString().trim().isEmpty() || hour.getText().toString().trim().isEmpty() || minute.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Please do not leave day or hour or minute blank.", Toast.LENGTH_SHORT).show();
                endDateSet = false;
            } else {
                if (Integer.parseInt(hour.getText().toString().trim()) <= 300
                        && Integer.parseInt(hour.getText().toString().trim()) <= 24
                        && Integer.parseInt(minute.getText().toString().trim()) < 60){
                    endDateSet = true;
                } else {
                    Toast.makeText(this, "Please correct day | hour | minute.", Toast.LENGTH_SHORT).show();
                    endDateSet = false;
                }
            }
        }

        if (startDateSet && endDateSet){
            publishQuiz();
        }
    }// End validating date and time


    // Delete operation method
    public void publishQuiz() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        deleted.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final String publishTs;
        if(!startDate.getText().toString().trim().isEmpty() && !startTime.getText().toString().trim().isEmpty()){
            publishTs = startDate.getText().toString().trim() + " " + startTime.getText().toString().trim() + ":00";
        } else {
            publishTs = "";
        }

        final String end_hour = hour.getText().toString().trim();
        final String end_day = day.getText().toString().trim();
        final String end_minute = minute.getText().toString().trim();

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String url = Constants.LIVE_QUIZ_INSTRUCTOR + videoIdHolder.getText().toString().trim();

            try {
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("status").equals("200")){
                                        // response
                                        processing.setVisibility(View.GONE);
                                        error.setVisibility(View.GONE);
                                        mainView.setVisibility(View.GONE);
                                        deleted.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        },1000);
                                    } else {
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                } catch (Exception e){
                                    // error
                                    mainView.setVisibility(View.GONE);
                                    processing.setVisibility(View.GONE);
                                    error.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError e) {
                                // error
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        // Sending data to server
                        Map<String, String>  params = new HashMap<>();
                        params.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        params.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        params.put("quiz_start_ts", publishTs);
                        params.put("day", end_day);
                        params.put("hour", end_hour);
                        params.put("minute", end_minute);
                        params.put("end_type", end.getText().toString().trim());
                        params.put("start_type", publish.getText().toString().trim());
                        return params;
                    }
                };

                requestQueue.add(putRequest);
            } catch (Exception e) {
                e.printStackTrace();

                // Making layout changes
                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {
            // Making layout changes
            mainView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }//End delete operation method


    private void getQuizInfo(){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String url = Constants.GET_QUIZ_PUBLISH_DETAILS_INSTRUCTOR + videoIdHolder.getText().toString().trim()
                    + "?instructor_id=" + sharedPreferences.getString("user_id", null)
                    + "&api_token=" + sharedPreferences.getString("api_token", null);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        if(jsonObject.getString("status").equals("200")){


                            if (jsonObject.getString("time_status").equals("2")){
                                long different = Long.parseLong(jsonObject.getString("millis"));
                                MyCount counter = new MyCount(different, 1000);
                                counter.start();
                                timeStatusEndStart.setText("यह क्विज प्रारम्भ होगा");
                                timeCounter.setVisibility(View.VISIBLE);
                            } else if (jsonObject.getString("time_status").equals("3")){
                                timeStatusEndStart.setText("यह क्विज समाप्त हो चूका है");
                                timeCounter.setVisibility(View.VISIBLE);
                            } else if (jsonObject.getString("time_status").equals("1")){
                                long different = Long.parseLong(jsonObject.getString("millis"));
                                if(different > 1000){
                                    MyCount counter = new MyCount(different, 1000);
                                    counter.start();
                                    timeStatusEndStart.setText("के लिए यह क्विज सक्रिय है");
                                    timeCounter.setVisibility(View.VISIBLE);
                                } else {
                                    timeCounter.setVisibility(View.GONE);
                                }
                            }

                            // Publish timing
                            if (jsonObject.getString("quiz_start_status").equals("1")) {
                                publish.setText("1");
                                publishNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                                publishLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                startTime.setVisibility(View.GONE);
                                startDate.setVisibility(View.GONE);
                                startDate.setText("");
                                startTime.setText("");

                            } else if(jsonObject.getString("quiz_start_status").equals("2")) {
                                publish.setText("2");
                                publishNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                publishLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                                startDate.setText(jsonObject.getString("date"));
                                startTime.setText(jsonObject.getString("time"));
                                startDate.setVisibility(View.VISIBLE);
                                startTime.setVisibility(View.VISIBLE);

                            }// End publish timing

                            // Deactivate timing
                            if (jsonObject.getString("quiz_end_status").equals("1")){
                                end.setText("1");
                                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                day.setVisibility(View.GONE);
                                hour.setVisibility(View.GONE);
                                minute.setVisibility(View.GONE);
                                day.setText("");
                                hour.setText("");
                                minute.setText("");

                            } else if (jsonObject.getString("quiz_end_status").equals("2")){
                                end.setText("2");
                                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                day.setVisibility(View.GONE);
                                hour.setVisibility(View.GONE);
                                minute.setVisibility(View.GONE);
                                day.setText("");
                                hour.setText("");
                                minute.setText("");

                            } else if (jsonObject.getString("quiz_end_status").equals("3")){
                                end.setText("3");
                                endNow.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                endNot.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                                endLater.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                                day.setText(jsonObject.getString("day"));
                                hour.setText(jsonObject.getString("hour"));
                                minute.setText(jsonObject.getString("minute"));
                                day.setVisibility(View.VISIBLE);
                                hour.setVisibility(View.VISIBLE);
                                minute.setVisibility(View.VISIBLE);

                            }// End deactivate timing

                            // Viewing main view
                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);

                        } else {

                            // Viewing error
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        // Viewing error
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                    // Viewing error
                    mainView.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }
    }// End getting quiz information



    public class MyCount extends CountDownTimer {
        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getQuizInfo();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String days = Long.toString(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
            String hours = Long.toString(TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
            String minutes = Long.toString(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
            String seconds = Long.toString(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            daysLeft.setText(days);
            hoursLeft.setText(hours);
            minutesLeft.setText(minutes);
            secondsLeft.setText(seconds);
        }
    }

    //
    private void showEndDateTime(){
        if (!publishTimeSeeDate.getText().toString().trim().isEmpty()
                && !startDate.getText().toString().trim().isEmpty()
                && !startTime.getText().toString().trim().isEmpty()
                && end.getText().toString().trim().equals("3")){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            Date date = null;
            try {
                date = sdf.parse(publishTimeSeeDate.getText().toString().trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (!day.getText().toString().trim().isEmpty()){
                calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(day.getText().toString().trim()));
            }

            if (!hour.getText().toString().trim().isEmpty()){
                calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.getText().toString().trim()));
            }

            if (!minute.getText().toString().trim().isEmpty()){
                calendar.add(Calendar.MINUTE, Integer.parseInt(minute.getText().toString().trim()));
            }
//            String new_end_time = .toString();
            String currentDateAndTime = sdf.format(calendar.getTime());
            unPublishTimeSeeDate.setText(currentDateAndTime);
            unPublishTimeSee.setVisibility(View.VISIBLE);
        } else {
            unPublishTimeSee.setVisibility(View.GONE);
        }

    }//

    //
    private void showStartDateTime(){
        if (!startDate.getText().toString().trim().isEmpty() || !startTime.getText().toString().trim().isEmpty()){
            String new_start_time = startDate.getText().toString().trim()+" "+startTime.getText().toString().trim();
            publishTimeSeeDate.setText(new_start_time);
            publishTimeSee.setVisibility(View.VISIBLE);
        }else{
            publishTimeSee.setVisibility(View.GONE);
        }
    }//

}