package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.CoursesInstructorAdapter;
import com.example.shubham.lamamia_project.model.CoursesInstructorList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorCourseAddActivity extends AppCompatActivity {

    private LinearLayout processing, error;
    private ConstraintLayout instructorView;
    private RecyclerView recyclerViewCourses;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private CoursesInstructorAdapter adapter;
    private Spinner spinnerCategoryList;
    private EditText categoryIdHolder, courseName, courseDescription, courseTypeHolder;
    private ArrayList<CoursesInstructorList> listItems = new ArrayList<>();
    private TextView categoryNa, courseNameNa, courseDescriptionNa, videoCourseType, quizCourseType, courseTypeNa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        instructorView = findViewById(R.id.mainView);
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        spinnerCategoryList = findViewById(R.id.categoryList);

        videoCourseType = findViewById(R.id.courseVideoType);
        quizCourseType = findViewById(R.id.courseQuizType);

        // Course elements
        categoryIdHolder = findViewById(R.id.categoryIdHolder);
        courseName = findViewById(R.id.courseName);
        courseDescription = findViewById(R.id.courseDescription);
        courseTypeHolder = findViewById(R.id.courseTypeHolder);

        // Checker text
        categoryNa = findViewById(R.id.categoryNa);
        courseNameNa = findViewById(R.id.courseNameNa);
        courseDescriptionNa = findViewById(R.id.courseDescriptionNa);
        courseTypeNa = findViewById(R.id.courseTypeNa);

        // Visibility setter
        categoryNa.setVisibility(View.GONE);
        courseNameNa.setVisibility(View.GONE);
        courseDescriptionNa.setVisibility(View.GONE);

        // Home
        LinearLayout home = findViewById(R.id.back);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Next
        LinearLayout next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryIdHolder.getText().toString().trim().equals("0")
                        || courseName.getText().toString().trim().length() < 3
                        || courseDescription.getText().toString().trim().length() < 10
                        || courseTypeHolder.getText().toString().trim().isEmpty()){

                    categoryNa.setText("");
                    courseNameNa.setText("");
                    courseDescriptionNa.setText("");

                    // Category check
                    if (categoryIdHolder.getText().toString().trim().equals("0")){
                        categoryNa.setText("Please choose category first");
                        categoryNa.setVisibility(View.VISIBLE);
                    } else {
                        categoryNa.setVisibility(View.GONE);
                    }

                    // Course name check
                    if (courseName.getText().toString().trim().length() < 3){
                        courseNameNa.setText("course name should have at least 3 character");
                        courseNameNa.setVisibility(View.VISIBLE);
                    } else {
                        courseNameNa.setVisibility(View.GONE);
                    }

                    // Course description check
                    if (courseDescription.getText().toString().trim().length() < 10){
                        courseDescriptionNa.setText("course description should have at least 10 character");
                        courseDescriptionNa.setVisibility(View.VISIBLE);
                    } else {
                        courseDescriptionNa.setVisibility(View.GONE);
                    }

                    // Course type choose
                    if (courseTypeHolder.getText().toString().trim().isEmpty()){
                        courseTypeNa.setText("Please select course type");
                        courseTypeNa.setVisibility(View.VISIBLE);
                    } else {
                        courseTypeNa.setVisibility(View.GONE);
                    }

                }else {
                    addCourse();
                }
            }
        });

        // Video type click
        videoCourseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCourseType.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                quizCourseType.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                courseTypeHolder.setText("1");
            }
        });

        // Quiz type click
        quizCourseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizCourseType.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked), null);
                videoCourseType.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked), null);
                courseTypeHolder.setText("2");
            }
        });

    }// End on create

    // Start course list request
    private void makeCourseList(){

        // Making layout changes
        instructorView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        // Initializing elements for request
        listItems = new ArrayList<>();
        listItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";
            String url = Constants.GET_COURSES_INSTRUCTOR + sharedPreferences.getString("user_id", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);

                        if(jsonObject.getString("status").equals("200")){
                            JSONArray array = jsonObject.getJSONArray("courses");

                            // Checking data length
                            if (array.length() > 0){

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject o = array.getJSONObject(i);
                                    CoursesInstructorList item = new CoursesInstructorList(
                                            o.getString("course_id"),
                                            o.getString("course_name")
                                    );
                                    listItems.add(item);
                                }
                                adapter = new CoursesInstructorAdapter(listItems, getApplicationContext());
                                recyclerViewCourses.setLayoutManager(mLayoutManager);
                                recyclerViewCourses.setAdapter(adapter);

                            }else{

                                recyclerViewCourses.setVisibility(View.GONE);


                            }// End checking data length

                            // Making main view visible
                            processing.setVisibility(View.GONE);
                            instructorView.setVisibility(View.VISIBLE);
                            recyclerViewCourses.setVisibility(View.VISIBLE);
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
        }

    }// End course list request


    public void getCategoryList(){

        // Making layout changes
        instructorView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final List<Integer> category_id = new ArrayList<>();//add ids in this list
        final List<String> category_name = new ArrayList<>();//add names in this list

        String url = Constants.GET_CATEGORIES_LIST;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {

                    JSONObject jsonObject = new JSONObject(s);
                    String stringStatus = jsonObject.getString("status");
                    int intStatus = Integer.parseInt(stringStatus);

                    if (intStatus == 200) {
                        JSONArray array = jsonObject.getJSONArray("categories");

                        category_id.add(0);
                        category_name.add("Choose Category");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            category_id.add(Integer.parseInt(o.getString("category_id")));
                            category_name.add(o.getString("category_name"));
                        }

                        final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, category_name);
                        adapter.setDropDownViewResource(R.layout.spinner_layout);
                        spinnerCategoryList.setAdapter(adapter);

                        // Making layout changes
                        error.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        instructorView.setVisibility(View.VISIBLE);

                        spinnerCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                id = category_id.get(position);//This will be the student id.
                                String id_number = Long.toString(id);
                                categoryIdHolder.setText(id_number);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }

                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    // Making layout changes
                    instructorView.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                // Making layout changes
                instructorView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(stringRequest);
    }

    // Start course add request
    private void addCourse() {

        // Making layout changes
        instructorView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

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
                                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddModuleActivity.class);
                                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(activityChangeIntent);
                                finish();
                            } else {
                                // Making layout changes
                                instructorView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            // Making layout changes
                            instructorView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        instructorView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("course_name", courseName.getText().toString().trim());
                        MyData.put("course_description", courseDescription.getText().toString().trim());
                        MyData.put("course_outcome", "");
                        MyData.put("course_requirement", "");
                        MyData.put("course_target_audience", "");
                        MyData.put("category_id", categoryIdHolder.getText().toString().trim());
                        MyData.put("course_type", courseTypeHolder.getText().toString().trim());
                        return MyData;
                    }
                };
                requestQueue.add(StringRequest);
            } catch (Exception e){
                e.printStackTrace();
                // Making layout changes
                instructorView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        } else {
            // Making layout changes
            instructorView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }// End course add request


    @Override
    protected void onResume() {
        super.onResume();
        makeCourseList();
        getCategoryList();
    }

}// End class
