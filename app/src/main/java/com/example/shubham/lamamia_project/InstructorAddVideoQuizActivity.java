package com.example.shubham.lamamia_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.shubham.lamamia_project.adapter.InstructorVideoViewAdapter;
import com.example.shubham.lamamia_project.model.VideosInstructorList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorAddVideoQuizActivity extends AppCompatActivity implements InstructorVideoViewAdapter.AdapterCallback {

    private LinearLayout error, processing, courseNotAvailable, moduleNotAvailable;
    private ScrollView mainView;

    private RecyclerView recyclerViewVideos;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private InstructorVideoViewAdapter adapter;
    private ArrayList<VideosInstructorList> listItems = new ArrayList<>();

    private TextView moduleIdHolder, moduleText;
    private Spinner courseSpinner, moduleSpinner;
    private Button create_video, create_quiz;

    final Context context = this;
    private int vid_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_quiz);

        // ID holders
        moduleIdHolder = findViewById(R.id.module_id_holder);

        // Spinners
        courseSpinner = findViewById(R.id.course_list);
        moduleSpinner = findViewById(R.id.module_list);

        // Initializing view elements
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        mainView = findViewById(R.id.mainView);
        courseNotAvailable = findViewById(R.id.courseNotAvailable);
        moduleNotAvailable = findViewById(R.id.moduleNotAvailable);
        moduleText = findViewById(R.id.moduleText);
        create_video = findViewById(R.id.upload_video);
        create_quiz = findViewById(R.id.upload_quiz);

        // Element by me
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Removing buttons from layout
        create_video.setVisibility(View.GONE);
        create_quiz.setVisibility(View.GONE);
        moduleText.setVisibility(View.GONE);

        // Button create video
        create_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleIdHolder.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please select course and module first", Toast.LENGTH_LONG).show();
                } else {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_choose_upload_video_type);

                    // Choose from gallery
                    Button gallery = dialog.findViewById(R.id.gallery);
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vid_type = 1;
                            dialog.dismiss();
                            addVideo();
                        }
                    });

                    // Record video
                    Button record = dialog.findViewById(R.id.record);
                    record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vid_type = 2;
                            dialog.dismiss();
                            addVideo();
                        }
                    });

                    dialog.show();
                }
            }
        });

        // Button create quiz
        create_quiz = findViewById(R.id.upload_quiz);
        create_quiz.setOnClickListener(new View.OnClickListener() {
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

        getCourseList();

    }

    // Start course list request
    private void makeVideoList(String module_id){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        recyclerViewVideos.setVisibility(View.GONE);
        courseNotAvailable.setVisibility(View.GONE);
        moduleNotAvailable.setVisibility(View.GONE);
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

                            // Type hide/view
                            if (jsonObject.getString("course_type").equals("1")){

                                create_quiz.setVisibility(View.GONE);
                                create_video.setVisibility(View.VISIBLE);

                            } else if(jsonObject.getString("course_type").equals("2")) {

                                create_video.setVisibility(View.GONE);
                                create_quiz.setVisibility(View.VISIBLE);
                            } else {
                                create_video.setVisibility(View.GONE);
                                create_quiz.setVisibility(View.GONE);
                            }

                            JSONArray array = jsonObject.getJSONArray("videos");
                            if (array.length() > 0){

                                for (int i = 0; i < array.length(); i++){
                                    String is_last;
                                    if (i+1 == array.length()){
                                        is_last = "1";
                                    }else{
                                        is_last = "0";
                                    }
                                    JSONObject o = array.getJSONObject(i);
                                    VideosInstructorList item = new VideosInstructorList(
                                            Integer.toString(i+1),
                                            o.getString("content_id"),
                                            o.getString("content_name"),
                                            is_last,
                                            o.getString("content_type")
                                    );
                                    listItems.add(item);
                                }

                                adapter = new InstructorVideoViewAdapter(listItems, getApplicationContext(), InstructorAddVideoQuizActivity.this, InstructorAddVideoQuizActivity.this);
                                recyclerViewVideos.setLayoutManager(mLayoutManager);
                                recyclerViewVideos.setAdapter(adapter);

                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                moduleNotAvailable.setVisibility(View.GONE);
                                recyclerViewVideos.setVisibility(View.VISIBLE);
                                mainView.setVisibility(View.VISIBLE);

                            }else{
                                processing.setVisibility(View.GONE);
                                recyclerViewVideos.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                moduleNotAvailable.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);
                            }

                        } else {

                            processing.setVisibility(View.GONE);
                            recyclerViewVideos.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.GONE);
                            moduleNotAvailable.setVisibility(View.GONE);
                            error.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        processing.setVisibility(View.GONE);
                        recyclerViewVideos.setVisibility(View.GONE);
                        mainView.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
                        moduleNotAvailable.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    processing.setVisibility(View.GONE);
                    recyclerViewVideos.setVisibility(View.GONE);
                    mainView.setVisibility(View.GONE);
                    courseNotAvailable.setVisibility(View.GONE);
                    moduleNotAvailable.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);

    }// End course list request


    public void getCourseList() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        courseNotAvailable.setVisibility(View.GONE);
        moduleNotAvailable.setVisibility(View.GONE);
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

                            if (array.length() > 0){

                                course_id.add(0);
                                course_name.add("Choose Course");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    course_id.add(Integer.parseInt(o.getString("course_id")));
                                    course_name.add(o.getString("course_name"));
                                }

                                // Making layout changes
                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);

                                final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, course_name);
                                adapter.setDropDownViewResource(R.layout.spinner_layout);
                                courseSpinner.setAdapter(adapter);
                                courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        id = course_id.get(position);//This will be the student id.
                                        if (id > 0){
                                            String id_number = Long.toString(id);
                                            getModuleList(id_number);
                                            moduleText.setVisibility(View.VISIBLE);
                                            create_video.setVisibility(View.GONE);
                                            create_quiz.setVisibility(View.GONE);
                                        }else{
                                            moduleSpinner.setVisibility(View.GONE);
                                            recyclerViewVideos.setVisibility(View.GONE);
                                            create_video.setVisibility(View.GONE);
                                            create_quiz.setVisibility(View.GONE);
                                            moduleText.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }

                                });

                            }else{
                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                                moduleNotAvailable.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.VISIBLE);
                            }
                        }else{
                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            error.setVisibility(View.GONE);
                            moduleNotAvailable.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        moduleNotAvailable.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                    // Making layout changes
                    mainView.setVisibility(View.GONE);
                    moduleNotAvailable.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    courseNotAvailable.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);

                }
            });
            requestQueue.add(stringRequest);
        }
    }// End Course list



    // Start course list request
    private void getModuleList(String course_id){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        moduleNotAvailable.setVisibility(View.GONE);
        courseNotAvailable.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final List<Integer> module_id = new ArrayList<>();//add ids in this list
        final List<String> module_name = new ArrayList<>();//add names in this list

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.GET_MODULES_INSTRUCTOR + course_id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getString("status").equals("200")) {
                            JSONArray array = jsonObject.getJSONArray("modules");

                            // Calculating array size
                            if (array.length() > 0){

                                module_id.add(0);
                                module_name.add("Choose Module");

                                // Looping array
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    module_id.add(Integer.parseInt(o.getString("module_id")));
                                    module_name.add(o.getString("module_name"));
                                }// End looping array

                                // Making layout changes
                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);
                                moduleSpinner.setVisibility(View.VISIBLE);

                                final ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, module_name);
                                adapter.setDropDownViewResource(R.layout.spinner_layout);
                                moduleSpinner.setAdapter(adapter);
                                moduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        id = module_id.get(position);//This will be the student id.
                                        String id_number = Long.toString(id);
                                        makeVideoList(id_number);
                                        moduleIdHolder.setText(id_number);
                                        if (id > 0){
                                            create_video.setVisibility(View.VISIBLE);
                                            create_quiz.setVisibility(View.VISIBLE);
                                        }else{
                                            create_video.setVisibility(View.GONE);
                                            create_quiz.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // your code here
                                    }
                                });

                            }else{

                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                                moduleNotAvailable.setVisibility(View.VISIBLE);

                            }// End calculating array size

                        }else{
                            // Making layout changes
                            processing.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.GONE);
                            error.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            moduleNotAvailable.setVisibility(View.VISIBLE);
                        }//

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        moduleNotAvailable.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                    // Making layout changes
                    mainView.setVisibility(View.GONE);
                    moduleNotAvailable.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    courseNotAvailable.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);

                }
            });
            requestQueue.add(stringRequest);
        }
    }// End module list request

    // Start add video method
    private void addQuiz() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        moduleNotAvailable.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        courseNotAvailable.setVisibility(View.GONE);
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
                                moduleNotAvailable.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            moduleNotAvailable.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        moduleNotAvailable.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
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
                moduleNotAvailable.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                courseNotAvailable.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {
            // Making layout changes
            mainView.setVisibility(View.GONE);
            moduleNotAvailable.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            courseNotAvailable.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }// End video add method



    // Start add video method
    private void addVideo() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        moduleNotAvailable.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        courseNotAvailable.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

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

                                if (vid_type == 0){
                                    Toast.makeText(getApplicationContext(),
                                            "Please select course and module first", Toast.LENGTH_LONG).show();
                                } else if(vid_type == 1){

                                    Intent activityChangeIntent = new Intent(getApplicationContext(), ChooseVideoListActivity.class);
                                    activityChangeIntent.putExtra("video_id", jsonObject.getString("video_id"));
                                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(activityChangeIntent);

                                } else if(vid_type == 2){

                                    Intent activityChangeIntent = new Intent(getApplicationContext(), RecordVideoActivity.class);
                                    activityChangeIntent.putExtra("video_id", jsonObject.getString("video_id"));
                                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(activityChangeIntent);

                                }

                            } else {

                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                moduleNotAvailable.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                courseNotAvailable.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            moduleNotAvailable.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            courseNotAvailable.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        moduleNotAvailable.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        courseNotAvailable.setVisibility(View.GONE);
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
                moduleNotAvailable.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                courseNotAvailable.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {
            // Making layout changes
            mainView.setVisibility(View.GONE);
            moduleNotAvailable.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            courseNotAvailable.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }// End video add method


    @Override
    protected void onResume() {
        super.onResume();
        if (!moduleIdHolder.getText().toString().trim().isEmpty()){
            makeVideoList(moduleIdHolder.getText().toString().trim());
        }
    }

    @Override
    public void onItemClicked(int position){
        // call back here
        makeVideoList(moduleIdHolder.getText().toString().trim());
    }



}// End class video add
