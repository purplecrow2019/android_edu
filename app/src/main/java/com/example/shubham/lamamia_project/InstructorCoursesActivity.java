package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.InstructorCoursesViewAdapter;
import com.example.shubham.lamamia_project.model.CoursesListViewInstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstructorCoursesActivity extends AppCompatActivity {

    private LinearLayout error, processing;
    private RequestQueue requestQueue;
    private ArrayList<CoursesListViewInstructor> listItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private InstructorCoursesViewAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_courses);

        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);

        // Element by me
        recyclerView = findViewById(R.id.mainView);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Retry
        Button retry = findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                makeCourseList();
            }
        });
    }

    public void makeCourseList()
    {
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        listItems = new ArrayList<>();
        listItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String url;
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";
            String user_id = sharedPreferences.getString("user_id", DEFAULT);
            String user_type = sharedPreferences.getString("user_type", DEFAULT);

            url = Constants.GET_COURSES_ALL_DATA_INSTRUCTOR + user_id +"?&user_type="+ user_type +"";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("courses");

                        for (int i = 0; i < array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            CoursesListViewInstructor item = new CoursesListViewInstructor(
                                    o.getString("course_name"),
                                    o.getString("course_id"),
                                    o.getString("course_image"),
                                    o.getString("course_total_views"),
                                    o.getString("course_average_rating"),
                                    o.getString("course_reviews"),
                                    o.getString("course_share"),
                                    o.getString("status")
                            );
                            listItems.add(item);
                        }

                        adapter = new InstructorCoursesViewAdapter(listItems, getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(adapter);

                        error.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        processing.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    error.setVisibility(View.VISIBLE);
                    processing.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            });

            requestQueue.add(stringRequest);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        makeCourseList();
    }
}
