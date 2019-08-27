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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.InstructorModulesViewAdapter;
import com.example.shubham.lamamia_project.model.ModulesInstructorList;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructorPreviewCourseActivity extends AppCompatActivity {

    private RecyclerView recyclerViewModules;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private InstructorModulesViewAdapter adapter;
    private ArrayList<ModulesInstructorList> listItems = new ArrayList<>();
    private EditText moduleNameText, courseIdHolder;
    private LinearLayout error, processing;
    private ConstraintLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_preview_course);

        // Casting view parents
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        courseIdHolder = findViewById(R.id.course_id_holder);

        try {
            String course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
            courseIdHolder.setText(course_id);
            String course_image = Objects.requireNonNull(getIntent().getExtras()).getString("course_image");
            ImageView courseImage = findViewById(R.id.courseImageHolder);
            Picasso.get().load(course_image).fit().into(courseImage);
            String course_name = Objects.requireNonNull(getIntent().getExtras()).getString("course_name");
            setTitle(course_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Element initializing
        recyclerViewModules = findViewById(R.id.modules);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        moduleNameText = findViewById(R.id.moduleNameText);

        // Add module
        ImageButton addModuleButton = findViewById(R.id.moduleAddButton);
        addModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleNameText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please write module name", Toast.LENGTH_LONG).show();
                }else {
                    addModule();
                }
            }
        });

        // Going Back
        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Course update
        ImageButton course_update = findViewById(R.id.updateCourse);
        course_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorCourseEditActivity.class);
                activityChangeIntent.putExtra("course_id", courseIdHolder.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

        // Retry
        Button retry = findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getModuleList(courseIdHolder.getText().toString().trim());
            }
        });

    }

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
                            adapter = new InstructorModulesViewAdapter(listItems, getApplicationContext());
                            recyclerViewModules.setLayoutManager(mLayoutManager);
                            recyclerViewModules.setAdapter(adapter);

                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }else{
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    mainView.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }

    }// End Module list request


    // Start course add request
    private void addModule() {

        error.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

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
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
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
                        MyData.put("module_name", moduleNameText.getText().toString().trim());
                        MyData.put("course_id", courseIdHolder.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            processing.setVisibility(View.GONE);
            mainView.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getModuleList(courseIdHolder.getText().toString().trim());
    }
}
