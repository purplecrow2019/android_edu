package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.shubham.lamamia_project.adapter.InstructorQuestionAdapter;
import com.example.shubham.lamamia_project.model.InstructorQuestionList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class InstructorQuestionsActivity extends AppCompatActivity {

    // Initiating elements
    private EditText quizIdHolder;
    private RecyclerView recyclerViewQuestions;
    private LinearLayoutManager mLayoutManager;
    private RequestQueue requestQueue;
    private LinearLayout processing, error, mainView;
    private InstructorQuestionAdapter adapter;
    private ArrayList<InstructorQuestionList> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_questions_list);

        // Setting title
        setTitle("Questions");

        // Casting views
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);

        // Casting elements
        final TextView quizName = findViewById(R.id.textQuizName);
        Button quizUpdate = findViewById(R.id.updateQuiz);
        Button addNewQuestion = findViewById(R.id.addNewQuestion);
        Button quizLive = findViewById(R.id.quizLive);
        quizIdHolder = findViewById(R.id.quizIdHolder);
        recyclerViewQuestions = findViewById(R.id.questions);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Try catch for quiz id
        try {
            String quiz_id = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_id");
            String quiz_name = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_name");
            quizIdHolder.setText(quiz_id);
            quizName.setText(quiz_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Question add
        addNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorQuestionAddActivity.class);
                activityChangeIntent.putExtra("quiz_id", quizIdHolder.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(activityChangeIntent);
            }
        });

        // Quiz update button
        quizUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddQuizActivity.class);
                activityChangeIntent.putExtra("quiz_id", quizIdHolder.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(activityChangeIntent);
            }
        });

        // Making quiz live process
        quizLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorQuizLive.class);
                activityChangeIntent.putExtra("quiz_id", quizIdHolder.getText().toString().trim());
                activityChangeIntent.putExtra("quiz_name", quizName.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeQuestionList();
    }

    // Starting request
    private void makeQuestionList(){

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

            String url = Constants.GET_QUESTIONS_INSTRUCTOR + quizIdHolder.getText().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);

                        if(jsonObject.getString("status").equals("200")){
                            JSONArray array = jsonObject.getJSONArray("questions");

                            // Checking data length
                            if (array.length() > 0){

                                for (int i = 0; i < array.length(); i++){
                                    JSONObject o = array.getJSONObject(i);
                                    InstructorQuestionList item = new InstructorQuestionList(
                                            o.getString("question_id"),
                                            o.getString("question_text")
                                    );
                                    listItems.add(item);
                                }
                                adapter = new InstructorQuestionAdapter(listItems, getApplicationContext());
                                recyclerViewQuestions.setLayoutManager(mLayoutManager);
                                recyclerViewQuestions.setAdapter(adapter);

                                // Making main view visible
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);

                            }else{

                                // Making main view visible
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);

                            }// End checking data length
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
        }

    }// End course list request
}
