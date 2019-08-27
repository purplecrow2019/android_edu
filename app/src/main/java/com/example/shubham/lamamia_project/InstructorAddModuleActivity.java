package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.shubham.lamamia_project.adapter.ModulesInstructorAdapter;
import com.example.shubham.lamamia_project.model.ModulesInstructorList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorAddModuleActivity extends AppCompatActivity {

    private LinearLayout error, processing, courseNotAvailable;
    private ConstraintLayout mainView;
    private RecyclerView recyclerViewModules;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private ModulesInstructorAdapter adapter;
    private ArrayList<ModulesInstructorList> listItems = new ArrayList<>();
    private TextView courseIdHolder;
    private TextView moduleNameText;
    private Spinner courseSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        courseIdHolder = findViewById(R.id.course_id_holder);
        courseSpinner = findViewById(R.id.course_list);

        // Initializing view elements
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        mainView = findViewById(R.id.mainView);
        courseNotAvailable = findViewById(R.id.courseNotAvailable);

        Button addModuleButton = findViewById(R.id.moduleAddButton);
        moduleNameText = findViewById(R.id.moduleNameText);

        // Element by me
        recyclerViewModules = findViewById(R.id.recyclerViewModules);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Module creation
        addModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!moduleNameText.getText().toString().trim().isEmpty() && !courseIdHolder.getText().toString().trim().equals("0")){
                    addModule();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select course and write module name.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Home
        LinearLayout home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Back
        LinearLayout next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddVideoQuizActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
                finish();
            }
        });

    }// End on create

    // Start course list request
    private void getModuleList(String course_id){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        // Initializing elements for request
        listItems = new ArrayList<>();
        listItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String url = Constants.GET_MODULES_INSTRUCTOR + course_id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        if(jsonObject.getString("status").equals("200")){
                            JSONArray array = jsonObject.getJSONArray("modules");
                            for (int i = 0; i < array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                ModulesInstructorList item = new ModulesInstructorList(
                                        o.getString("module_id"),
                                        o.getString("module_name")
                                );
                                listItems.add(item);
                            }
                            adapter = new ModulesInstructorAdapter(listItems, getApplicationContext());
                            recyclerViewModules.setLayoutManager(mLayoutManager);
                            recyclerViewModules.setAdapter(adapter);
                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            recyclerViewModules.setVisibility(View.VISIBLE);
                            mainView.setVisibility(View.VISIBLE);
                        } else {
                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            recyclerViewModules.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    error.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    recyclerViewModules.setVisibility(View.GONE);
                    mainView.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }

    }// End course list request


    // Start course add request
    private void addModule() {

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.POST_CREATE_MODULE;
            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200) {
                                getModuleList(courseIdHolder.getText().toString().trim());
                                moduleNameText.setText("");
                            } else {

                                // Making layout changes
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        processing.setVisibility(View.GONE);
                        mainView.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("module_name", moduleNameText.getText().toString().trim());
                        MyData.put("course_id", courseIdHolder.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e) {
                e.printStackTrace();

                // Making layout changes
                processing.setVisibility(View.GONE);
                mainView.setVisibility(View.GONE);
                courseNotAvailable.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {

            // Making layout changes
            processing.setVisibility(View.GONE);
            mainView.setVisibility(View.GONE);
            courseNotAvailable.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);

        }
    }


        public void getCourseList() {

            // Making layout changes
            mainView.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            courseNotAvailable.setVisibility(View.GONE);
            processing.setVisibility(View.VISIBLE);

            final List<Integer> course_id = new ArrayList<>();//add ids in this list
            final List<String> course_name = new ArrayList<>();//add names in this list

            final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            final String DEFAULT = "null";
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")) {

                String url = Constants.GET_COURSES_INSTRUCTOR+sharedPreferences.getString("user_id", DEFAULT);

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

                                    course_id.add(0);
                                    course_name.add("Choose course");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject o = array.getJSONObject(i);
                                        course_id.add(Integer.parseInt(o.getString("course_id")));
                                        course_name.add(o.getString("course_name"));
                                    }

                                    // Making layout changes
                                    error.setVisibility(View.GONE);
                                    processing.setVisibility(View.GONE);
                                    courseNotAvailable.setVisibility(View.GONE);
                                    mainView.setVisibility(View.VISIBLE);

                                    final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, course_name);
                                    adapter.setDropDownViewResource(R.layout.spinner_layout);
                                    courseSpinner.setAdapter(adapter);
                                    courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                            id = course_id.get(position);//This will be the student id.
                                            String id_number = Long.toString(id);
                                            getModuleList(id_number);
                                            courseIdHolder.setText(id_number);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parentView) {
                                            // your code here
                                        }
                                    });

                                } else {
                                    // Making layout changes
                                    error.setVisibility(View.GONE);
                                    processing.setVisibility(View.GONE);
                                    mainView.setVisibility(View.GONE);
                                    courseNotAvailable.setVisibility(View.VISIBLE);
                                }
                            }else{
                                // Making layout changes
                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Making layout changes
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {

                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                });
                requestQueue.add(stringRequest);
            }else{
                // Making layout changes
                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                courseNotAvailable.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        getCourseList();
    }

}// End class
