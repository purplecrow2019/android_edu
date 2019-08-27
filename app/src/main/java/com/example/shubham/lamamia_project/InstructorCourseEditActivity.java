package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructorCourseEditActivity extends AppCompatActivity {

    private EditText course_name, course_description, courseIdHolder;
    private TextView error_message;
    private LinearLayout processing, mainView, error;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        processing = findViewById(R.id.processing);
        mainView = findViewById(R.id.mainView);
        error = findViewById(R.id.error);
        requestQueue = Volley.newRequestQueue(this);

        // Initializing elements
        course_name = findViewById(R.id.courseName);
        course_description = findViewById(R.id.courseDescription);
        courseIdHolder = findViewById(R.id.courseIdHolder);
        error_message = findViewById(R.id.errorMessage);
        Button update_course_button = findViewById(R.id.updateCourseButton);

        try {
            String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
            courseIdHolder.setText(course_id);
            String course_name = Objects.requireNonNull(getIntent().getExtras()).getString("course_name");
            setTitle(course_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        update_course_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validating fields
                if (course_name.getText().toString().trim().isEmpty()
                        || course_description.getText().toString().trim().isEmpty()){

                    String error_text = "Please do not leave any field blank";
                    error_message.setText(error_text);
                    error_message.setVisibility(View.VISIBLE);

                }else{
                    updateCourse();
                }
            }
        });

        // Calling get course data
        getCourse();

        // Reload button
        Button reload = findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCourse();
            }
        });

    }// End on create


    // Start course add request
    private void updateCourse(){

        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.UPDATE_COURSE_INSTRUCTOR + courseIdHolder.getText().toString().trim();

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200){
                                finish();
                            } else {
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("course_name", course_name.getText().toString().trim());
                        MyData.put("course_description", course_description.getText().toString().trim());
                        MyData.put("course_outcome", "");
                        MyData.put("course_requirement", "");
                        MyData.put("course_target_audience", "");
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e){
                e.printStackTrace();
                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {
            mainView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }// End course add request

    // Start course list request
    private void getCourse(){

        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        String url = Constants.GET_COURSE_INSTRUCTOR + courseIdHolder.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject  = new JSONObject(s);

                    if (jsonObject.getString("status").equals("200")){

                        if(!jsonObject.getString("course_name").equals("null")){
                            course_name.setText(jsonObject.getString("course_name"));
                        }

                        if (!jsonObject.getString("course_description").equals("null")){
                            course_description.setText(jsonObject.getString("course_description"));
                        }

                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        mainView.setVisibility(View.VISIBLE);
                    }else{
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }// End course list request

}// End class
