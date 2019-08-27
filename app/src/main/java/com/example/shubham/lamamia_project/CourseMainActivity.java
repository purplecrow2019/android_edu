package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.ExpandableListAdapter;
import com.example.shubham.lamamia_project.adapter.InstructorAdapter;
import com.example.shubham.lamamia_project.model.ChildModuleList;
import com.example.shubham.lamamia_project.model.InstructorList;
import com.example.shubham.lamamia_project.model.ParentModuleList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class CourseMainActivity extends AppCompatActivity {

    private EditText courseIdHolder;

    // Instructor
    private RecyclerView instructorRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<InstructorList> instructorLists;
    private InstructorAdapter adapter_instructor;
    private RequestQueue rq;

    // Course information
    private TextView txtCourseDescription;

    // Module lists
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private ArrayList<ParentModuleList> parentModuleLists;
    private ArrayList<ChildModuleList> childModuleLists;

    // All layouts
    private LinearLayout courseLayout, instructorLayout, moduleLayout;

    // Button actions
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_influencer:

                    // View instructor layout
                    courseLayout.setVisibility(View.GONE);
                    moduleLayout.setVisibility(View.GONE);
                    instructorLayout.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_course:
                    //View course layout
                    moduleLayout.setVisibility(View.GONE);
                    instructorLayout.setVisibility(View.GONE);
                    courseLayout.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_module:
                    // View module/video layout
                    courseLayout.setVisibility(View.GONE);
                    instructorLayout.setVisibility(View.GONE);
                    moduleLayout.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        // EditText course id holder
        courseIdHolder = findViewById(R.id.editTextCourseMainActivityCourseIdHolder);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkData != null){
            String course_id = appLinkData.getQueryParameter("course_id");
            courseIdHolder.setText(course_id);
        }else{
            try {
                String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
                    courseIdHolder.setText(course_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // All layouts
        courseLayout = findViewById(R.id.courseLayout);
        moduleLayout = findViewById(R.id.moduleLayout);
        instructorLayout = findViewById(R.id.instructorLayout);

        // Instructor views
        instructorRecyclerView = findViewById(R.id.recyclerViewInstructors);
        rq = Volley.newRequestQueue(Objects.requireNonNull(this));
        mLayoutManager = new LinearLayoutManager(this);
        instructorLists = new ArrayList<>();

        // Course info views
        txtCourseDescription = findViewById(R.id.textViewCourseDescription);

        // Module lists
        expListView =  findViewById(R.id.expandListViewModules);
        parentModuleLists = new ArrayList<>();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        getInstructorList();
        getCourseInfo();
        getModuleList();

        // Making instructor visible
        courseLayout.setVisibility(View.GONE);
        instructorLayout.setVisibility(View.GONE);
        moduleLayout.setVisibility(View.VISIBLE);
    }


    // Instructor get
    private void getInstructorList(){

        String url_instructor = Constants.GET_INSTRUCTOR_LIST_BY_COURSE + courseIdHolder.getText().toString().trim();
        StringRequest stringRequestInstructor = new StringRequest(Request.Method.GET, url_instructor, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("instructors");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        InstructorList items = new InstructorList(
                                o.getString("instructor_id"),
                                o.getString("instructor_name"),
                                o.getString("instructor_image"),
                                o.getString("instructor_about"),
                                o.getString("instructor_achievements")
                        );
                        instructorLists.add(items);
                    }

                    adapter_instructor = new InstructorAdapter(instructorLists, getApplicationContext());
                    instructorRecyclerView.setLayoutManager(mLayoutManager);
                    instructorRecyclerView.setAdapter(adapter_instructor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(stringRequestInstructor);

    }// Instructor get

    // Course information get
    private void getCourseInfo(){

        // Course API call
        String url = Constants.GET_COURSE_INFORMATION + courseIdHolder.getText().toString().trim();
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    int intStatus = Integer.parseInt(status);

                    if (intStatus == 200) {
                        JSONArray array = jsonObject.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            txtCourseDescription.setText(o.getString("course_description"));
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Check internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Please Check internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        rq.add(MyStringRequest);

    }// End course information get


    // Module lists get
    private void getModuleList(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("phone")
                && sharedPreferences.contains("user_type")) {

            String url = Constants.GET_MODULE_LIST_BY_COURSE + courseIdHolder.getText().toString().trim()
                    + "?user_id=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&user_type=" + sharedPreferences.getString("user_type", DEFAULT) + "";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    if (s != null) {

                        try {
                            JSONObject JsonObjectModule = new JSONObject(s);
                            JSONArray moduleArray = JsonObjectModule.getJSONArray("data");

                            for (int m = 0; m < moduleArray.length(); m++) {
                                JSONObject o = moduleArray.getJSONObject(m);
                                ParentModuleList parentModuleList = new ParentModuleList();
                                parentModuleList.setModule_name(o.getString("module_name"));
                                parentModuleList.setCompleted_percentage(o.getString("user_watch_percentage"));
                                parentModuleList.setTotal_duration(o.getString("total_duration"));

                                childModuleLists = new ArrayList<>();

                                JSONArray ja = o.getJSONArray("videos");

                                for (int i = 0; i < ja.length(); i++) {

                                    JSONObject jo = ja.getJSONObject(i);

                                    ChildModuleList childModuleList = new ChildModuleList();
                                    childModuleList.setVideo_id(jo.getString("content_id"));
                                    childModuleList.setVideo_name(jo.getString("content_name"));
                                    childModuleList.setModule_id(jo.getString("module_id"));
                                    childModuleList.setVideo_order(jo.getString("content_order"));
                                    childModuleList.setContent_type(jo.getString("content_type"));
                                    childModuleList.setCourse_id(courseIdHolder.getText().toString().trim());

                                    childModuleLists.add(childModuleList);
                                }

                                parentModuleList.setChildModuleLists(childModuleLists);
                                parentModuleLists.add(parentModuleList);
                            }

                            listAdapter = new ExpandableListAdapter(getApplicationContext(), parentModuleLists);
                            expListView.setAdapter(listAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Please Check internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Check internet Connection", Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            rq.add(stringRequest);
        }

    }// End module lists get

}
