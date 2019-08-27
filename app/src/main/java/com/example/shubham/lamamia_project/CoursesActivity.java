package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.CourseAdapter;
import com.example.shubham.lamamia_project.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CoursesActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<ListItem> listItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private TextView categoryIdHolder;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        categoryIdHolder = findViewById(R.id.categoryIdHolder);
        progress = findViewById(R.id.progress);

        try{
            String category_id = Objects.requireNonNull(getIntent().getExtras()).getString("category_id");
            categoryIdHolder.setText(category_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Element by me
        recyclerView = findViewById(R.id.recyclerViewCourses);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
    }

    @Override
    protected void onResume(){
        super.onResume();
        makeCourseList();
    }

    public void makeCourseList(){
        listItems = new ArrayList<>();
        listItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String url;
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
//            String DEFAULT = "null";
//            String user_id = sharedPreferences.getString("user_id", DEFAULT);
//            String user_type = sharedPreferences.getString("user_type", DEFAULT);

            url = Constants.GET_COURSE_INFORMATION_BY_CATEGORY + categoryIdHolder.getText().toString().trim();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("courses");

                        for (int i = 0; i < array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            ListItem item = new ListItem(
                                    o.getString("course_name"),
                                    o.getString("course_id"),
                                    o.getString("course_image_medium"),
                                    o.getString("course_total_views"),
                                    o.getString("course_average_rating"),
                                    o.getString("course_reviews"),
                                    o.getString("course_share")
                            );
                            listItems.add(item);
                        }

                        adapter = new CourseAdapter(listItems, getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(adapter);
                        progress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

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
    }
}
