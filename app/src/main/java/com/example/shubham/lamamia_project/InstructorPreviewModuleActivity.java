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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.InstructorVideoViewAdapter;
import com.example.shubham.lamamia_project.model.VideosInstructorList;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructorPreviewModuleActivity extends AppCompatActivity implements InstructorVideoViewAdapter.AdapterCallback {

    private RecyclerView recyclerViewContents;
    private EditText courseIdHolder, moduleIdHolder;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private ConstraintLayout mainView;
    private LinearLayout processing, error;
    private ArrayList<VideosInstructorList> listItems = new ArrayList<>();
    private InstructorVideoViewAdapter adapter;
    private TextView moduleName;
    private ImageView courseImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_preview_module);

        // Casting module id holder
        moduleIdHolder = findViewById(R.id.moduleIdHolder);
        courseIdHolder = findViewById(R.id.courseIdHolder);

        // Getting module Id
        try {
            String module_id = Objects.requireNonNull(getIntent().getExtras()).getString("module_id");
            moduleIdHolder.setText(module_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Element casting
        recyclerViewContents = findViewById(R.id.contents);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        moduleName = findViewById(R.id.moduleName);
        courseImage = findViewById(R.id.courseImageHolder);

        mainView = findViewById(R.id.mainView);
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);

        // Going Back
        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Create video
        Button createVideo = findViewById(R.id.createNewVideo);
        createVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleIdHolder.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please select course and module first", Toast.LENGTH_LONG).show();
                }else{
                    addVideo();
                }
            }
        });


        // Create Quiz
        Button createQuiz = findViewById(R.id.createNewQuiz);
        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleIdHolder.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please select course and module first", Toast.LENGTH_LONG).show();
                }else{
                    addQuiz();
                }
            }
        });

        // Module edit
        ImageButton editModule = findViewById(R.id.moduleNameEdit);
        editModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorModuleEditActivity.class);
                activityChangeIntent.putExtra("module_id", moduleIdHolder.getText().toString().trim());
                activityChangeIntent.putExtra("module_name", moduleName.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

        // Module delete
        ImageButton deleteModule = findViewById(R.id.moduleDelete);
        deleteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorModuleDeleteActivity.class);
                activityChangeIntent.putExtra("module_id", moduleIdHolder.getText().toString().trim());
                activityChangeIntent.putExtra("module_name", moduleName.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

    }

    // Making content list
    private void makeContentList(final String module_id){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        // Initializing elements for request
        listItems = new ArrayList<>();
        listItems.clear();

        // Making string url
        String url = Constants.GET_VIDEOS_INSTRUCTOR + module_id;

        // Sending request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject  = new JSONObject(s);
                    if(jsonObject.getString("status").equals("200")){
                        JSONArray array = jsonObject.getJSONArray("videos");

                        courseIdHolder.setText(jsonObject.getString("course_id"));
                        setTitle(jsonObject.getString("course_name"));
                        Picasso.get().load(jsonObject.getString("course_image")).fit().into(courseImage);
                        moduleName.setText(jsonObject.getString("module_name"));

                        if (array.length() > 0) {

                            for (int i = 0; i < array.length(); i++) {
                                String is_last;
                                if (i+1 == array.length()){
                                    is_last = "1";
                                }else{
                                    is_last = "0";
                                }

                                JSONObject o = array.getJSONObject(i);
                                VideosInstructorList item = new VideosInstructorList(
                                        Integer.toString(i +1),
                                        o.getString("content_id"),
                                        o.getString("content_name"),
                                        is_last,
                                        o.getString("content_type")
                                );
                                listItems.add(item);
                            }

                            adapter = new InstructorVideoViewAdapter(listItems, getApplicationContext(), InstructorPreviewModuleActivity.this, InstructorPreviewModuleActivity.this);
                            recyclerViewContents.setLayoutManager(mLayoutManager);
                            recyclerViewContents.setAdapter(adapter);

                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }else{
                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }

                    } else {
                        processing.setVisibility(View.GONE);
                        mainView.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    processing.setVisibility(View.GONE);
                    mainView.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                processing.setVisibility(View.GONE);
                mainView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(stringRequest);

    }//End Content list



    // Start add video method
    private void addVideo() {

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String url = Constants.CREATE_VIDEO_INSTRUCTOR;

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);

                            if (intStatus == 200) {
                                Intent activityChangeIntent = new Intent(getApplicationContext(), ChooseVideoListActivity.class);
                                activityChangeIntent.putExtra("video_id", jsonObject.getString("video_id"));
                                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(activityChangeIntent);
                            } else {

                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
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
                        MyData.put("module_id", moduleIdHolder.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
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
    }// End video add method

    @Override
    protected void onResume() {
        super.onResume();
        makeContentList(moduleIdHolder.getText().toString().trim());
    }


    @Override
    public void onItemClicked(int position){
        // call back here
        makeContentList(moduleIdHolder.getText().toString().trim());
    }


    // Start add video method
    private void addQuiz() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String url = Constants.CREATE_QUIZ_INSTRUCTOR;

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);

                            if (intStatus == 200) {
                                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddQuizActivity.class);
                                activityChangeIntent.putExtra("quiz_id", jsonObject.getString("quiz_id"));
                                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(activityChangeIntent);
                            } else {

                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
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
                        MyData.put("module_id", moduleIdHolder.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
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
    }// End video add method

}// End class
