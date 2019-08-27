package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructorAddQuizActivity extends AppCompatActivity {

    // Initiating elements
    private EditText quizName, quizInstruction, quizIdHolder, quizTimeMinutes, quizTimeSeconds, questionTimeMinutes, questionTimeSeconds, maxMarks, negativeMarks;
    private TextView quizNameNa, quizInstructionNa, quizTimePut, questionTimePut;
    private LinearLayout mainView, processing, error, linearQuizTimePut, linearQuestionTimePut;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_quiz);

        // Casting elements
        quizName = findViewById(R.id.quizName);
        quizInstruction = findViewById(R.id.quizInstruction);
        quizIdHolder = findViewById(R.id.quizIdHolder);
        quizTimeMinutes = findViewById(R.id.quizTimeMinutes);
        quizTimeSeconds = findViewById(R.id.quizTimeSeconds);
        questionTimeMinutes = findViewById(R.id.questionTimeMinutes);
        questionTimeSeconds =findViewById(R.id.questionTimeSeconds);
        maxMarks = findViewById(R.id.questionMaximumMarks);
        negativeMarks = findViewById(R.id.questionNegativeMarks);

        // Button
        Button btnQuizAdd = findViewById(R.id.btnQuizAdd);

        // Text Views
        quizInstructionNa = findViewById(R.id.quizInstructionNa);
        quizNameNa = findViewById(R.id.quizNameNa);
        quizTimePut = findViewById(R.id.quizTimePut);
        questionTimePut = findViewById(R.id.questionTimePut);
        quizTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);
        questionTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);

        // Layouts
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        linearQuizTimePut = findViewById(R.id.linearQuizTimePut);
        linearQuestionTimePut = findViewById(R.id.linearQuestionTimePut);

        // Request queue
        requestQueue = Volley.newRequestQueue(this);

        // Try catch for quiz id
        try {
            String quiz_id = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_id");
            quizIdHolder.setText(quiz_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show/Hide quiz time section
        quizTimePut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearQuizTimePut.setVisibility(View.VISIBLE);
                linearQuestionTimePut.setVisibility(View.GONE);
                quizTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked) ,null);
                questionTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);
                questionTimeMinutes.setText("");
                questionTimeSeconds.setText("");
            }
        });

        questionTimePut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearQuizTimePut.setVisibility(View.GONE);
                linearQuestionTimePut.setVisibility(View.VISIBLE);
                questionTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked),null);
                quizTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);
                quizTimeMinutes.setText("");
                quizTimeSeconds.setText("");
            }
        });

        // Button call
        btnQuizAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizName.getText().toString().trim().isEmpty()
                        || quizInstruction.getText().toString().trim().isEmpty()
                        || maxMarks.getText().toString().trim().isEmpty()){

                    // Checking for quiz name
                    if (quizName.getText().toString().trim().isEmpty()){
                        String quiz_name = "Please write quiz name.";
                        quizNameNa.setText(quiz_name);
                    }

                    // Checking for quiz instructions
                    if (quizInstruction.getText().toString().trim().isEmpty()){
                        String quiz_instruction = "Please write some instructions.";
                        quizInstructionNa.setText(quiz_instruction);
                    }

                    // required max marks
                    if (maxMarks.getText().toString().trim().isEmpty()){
                        Toast.makeText(getApplicationContext(),"please fill maximum marks per question", Toast.LENGTH_LONG).show();
                    }

                }else{
                    addQuiz();
                }
            }
        });

        // Getting quiz information
        getQuizInfo();

    }// End method on create


    private void getQuizInfo(){

            // Making layout changes
            mainView.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            processing.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")
                    && sharedPreferences.contains("user_type")) {
                String url = Constants.GET_QUIZ_INSTRUCTOR + quizIdHolder.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject jsonObject  = new JSONObject(s);
                            if(jsonObject.getString("status").equals("200")){

                                // Assigning values
                                quizName.setText(jsonObject.getString("quiz_name"));
                                quizInstruction.setText(jsonObject.getString("quiz_instruction"));

                                //
                                if (!jsonObject.getString("quiz_time").isEmpty()){
                                    quizTimeSeconds.setText(jsonObject.getString("quiz_seconds"));
                                    quizTimeMinutes.setText(jsonObject.getString("quiz_minutes"));
                                    linearQuizTimePut.setVisibility(View.VISIBLE);
                                    quizTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked) ,null);
                                    questionTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);
                                }else{
                                    linearQuizTimePut.setVisibility(View.GONE);
                                }

                                if (!jsonObject.getString("question_time").isEmpty()){
                                    questionTimeMinutes.setText(jsonObject.getString("question_minutes"));
                                    questionTimeSeconds.setText(jsonObject.getString("question_seconds"));
                                    linearQuestionTimePut.setVisibility(View.VISIBLE);
                                    questionTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_checked) ,null);
                                    quizTimePut.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_radio_button_unchecked),null);
                                }else{
                                    linearQuestionTimePut.setVisibility(View.GONE);
                                }

                                maxMarks.setText(jsonObject.getString("max_marks"));
                                negativeMarks.setText(jsonObject.getString("negative_marks"));

                                // Viewing main view
                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.VISIBLE);

                            } else {

                                // Viewing error
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            // Viewing error
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {

                        // Viewing error
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                });
                requestQueue.add(stringRequest);
            }
    }// End getting quiz information

    private void addQuiz(){
        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String url = Constants.UPDATE_QUIZ_INSTRUCTOR + quizIdHolder.getText().toString().trim();

            try {
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    // response
                                    mainView.setVisibility(View.VISIBLE);
                                    processing.setVisibility(View.GONE);
                                    error.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    finish();
                                } catch (Exception e){
                                    // error
                                    mainView.setVisibility(View.GONE);
                                    processing.setVisibility(View.GONE);
                                    error.setVisibility(View.VISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError e) {
                                // error
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        }
                    ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        // Sending data to server
                        Map<String, String>  params = new HashMap<>();
                        params.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        params.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        params.put("quiz_name", quizName.getText().toString().trim());
                        params.put("quiz_instruction", quizInstruction.getText().toString().trim());

                        // Quiz time
                        if (quizTimeMinutes.getText().toString().trim().isEmpty()
                                && quizTimeSeconds.getText().toString().trim().isEmpty()){
                            params.put("quiz_time", "");
                        } else {
                            // Converting quiz time
                            if(!quizTimeMinutes.getText().toString().trim().isEmpty() && !quizTimeSeconds.getText().toString().trim().isEmpty()){
                                long quiz_time_minutes = Long.parseLong(quizTimeMinutes.getText().toString().trim()) * 60000;
                                long quiz_time_seconds = Long.parseLong(quizTimeSeconds.getText().toString().trim()) * 1000;
                                params.put("quiz_time", Double.toString(quiz_time_minutes + quiz_time_seconds));
                            }

                            if (!quizTimeMinutes.getText().toString().trim().isEmpty() && quizTimeSeconds.getText().toString().trim().isEmpty()){
                                long quiz_time_minutes = Long.parseLong(quizTimeMinutes.getText().toString().trim()) * 60000;
                                params.put("quiz_time", Double.toString(quiz_time_minutes));
                            }

                            if (quizTimeMinutes.getText().toString().trim().isEmpty() && !quizTimeSeconds.getText().toString().trim().isEmpty()){
                                long quiz_time_seconds = Long.parseLong(quizTimeSeconds.getText().toString().trim()) * 1000;
                                params.put("quiz_time", Double.toString(quiz_time_seconds));
                            }
                        }

                        // Question time
                        if (questionTimeMinutes.getText().toString().trim().isEmpty()
                                && questionTimeSeconds.getText().toString().trim().isEmpty()){
                            params.put("question_time", "");
                        } else {
                            // Converting question time
                            if (!questionTimeMinutes.getText().toString().trim().isEmpty() && !questionTimeSeconds.getText().toString().trim().isEmpty()){
                                long question_time_minutes = Long.parseLong(questionTimeMinutes.getText().toString().trim()) * 60000;
                                long question_time_seconds = Long.parseLong(questionTimeSeconds.getText().toString().trim()) * 1000;
                                params.put("question_time", Double.toString(question_time_minutes + question_time_seconds));
                            }

                            if (!questionTimeMinutes.getText().toString().trim().isEmpty() && questionTimeSeconds.getText().toString().trim().isEmpty()){
                                long question_time_minutes = Long.parseLong(questionTimeMinutes.getText().toString().trim()) * 60000;
                                params.put("question_time", Double.toString(question_time_minutes));
                            }

                            if (questionTimeMinutes.getText().toString().trim().isEmpty() && !questionTimeSeconds.getText().toString().trim().isEmpty()){
                                long question_time_seconds = Long.parseLong(questionTimeSeconds.getText().toString().trim()) * 1000;
                                params.put("question_time", Double.toString(question_time_seconds));
                            }
                        }

                        params.put("max_marks", maxMarks.getText().toString().trim());
                        params.put("negative_marks", negativeMarks.getText().toString().trim());
                        return params;
                    }
                };

                requestQueue.add(putRequest);
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
    }// End method addQuiz

}// End class instructor add quiz activity
