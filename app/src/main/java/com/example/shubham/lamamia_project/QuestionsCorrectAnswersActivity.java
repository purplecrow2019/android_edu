package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.OnSwipeTouchListener;
import com.example.shubham.lamamia_project.adapter.UserAnswersCorrectAdapter;
import com.example.shubham.lamamia_project.adapter.UserQuestionsCorrectAdapter;
import com.example.shubham.lamamia_project.model.UserAnswersCorrectList;
import com.example.shubham.lamamia_project.model.UserQuestionCorrectList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class QuestionsCorrectAnswersActivity extends AppCompatActivity implements UserQuestionsCorrectAdapter.AdapterCallback {

    // Declaration of elements
    private ArrayList<UserQuestionCorrectList> questionLists;
    private ArrayList<UserAnswersCorrectList> answerLists;
    private RecyclerView recyclerViewAllQuestions;
    private RecyclerView recyclerViewAnswers;
    private RequestQueue requestQueue;
    private UserQuestionsCorrectAdapter questionsAllAdapter;
    private UserAnswersCorrectAdapter answersAdapter;
    private TextView quizIdHolder, questionOrderHolder, questionText, questionIdHolder, totalQuestions, questionNotes;
    private LinearLayout processing, mainView, swipeListenerView;
    private WebView webViewQuestionImage;
    private ScrollView mainViewScroll;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_correct_answers);

        // Linear layouts
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        swipeListenerView = findViewById(R.id.swipeListenerView);
        mainViewScroll = findViewById(R.id.mainViewScroll);

        requestQueue = Volley.newRequestQueue(this);
        quizIdHolder = findViewById(R.id.quizIdHolder);
        questionOrderHolder = findViewById(R.id.questionOrderHolder);
        questionText = findViewById(R.id.questionText);
        questionIdHolder = findViewById(R.id.questionIdHolder);
        totalQuestions = findViewById(R.id.totalQuestions);
        questionNotes = findViewById(R.id.questionNotes);
        webViewQuestionImage = findViewById(R.id.webViewQuestionImage);

        recyclerViewAllQuestions = findViewById(R.id.recyclerViewAllQuestions);
        recyclerViewAnswers = findViewById(R.id.recyclerAnswers);

        // Try catch for quiz id
        try {
            String quiz_time = Objects.requireNonNull(getIntent().getExtras()).getString("quiz_time");
            quizIdHolder.setText(Objects.requireNonNull(getIntent().getExtras()).getString("quiz_id"));
            setTitle(Objects.requireNonNull(getIntent().getExtras()).getString("quiz_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // End quiz
        Button endQuiz = findViewById(R.id.endQuiz);
        endQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Calling get question all
        questionView("1", quizIdHolder.getText().toString().trim());

        // Swiping area
        swipeListenerView.setOnTouchListener(new OnSwipeTouchListener(QuestionsCorrectAnswersActivity.this) {

            public void onSwipeTop() { }

            public void onSwipeRight() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) <= Integer.parseInt(totalQuestions.getText().toString().trim()) &&
                        Integer.parseInt(questionOrderHolder.getText().toString().trim()) > 1){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())-1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromLeftAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "Cannot swipe right", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeLeft() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) < Integer.parseInt(totalQuestions.getText().toString().trim())){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())+1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromRightAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "Cannot swipe left.", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() { }
        });

        // recycler swipe
        recyclerViewAnswers.setOnTouchListener(new OnSwipeTouchListener(QuestionsCorrectAnswersActivity.this) {

            public void onSwipeTop() { }

            public void onSwipeRight() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) <= Integer.parseInt(totalQuestions.getText().toString().trim()) &&
                        Integer.parseInt(questionOrderHolder.getText().toString().trim()) > 1){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())-1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromLeftAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह पहला प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeLeft() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) < Integer.parseInt(totalQuestions.getText().toString().trim())){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())+1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromRightAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह आखरी प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() { }
        });

        mainView.setOnTouchListener(new OnSwipeTouchListener(QuestionsCorrectAnswersActivity.this) {

            public void onSwipeTop() { }

            public void onSwipeRight() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) <= Integer.parseInt(totalQuestions.getText().toString().trim()) &&
                        Integer.parseInt(questionOrderHolder.getText().toString().trim()) > 1){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())-1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromLeftAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह पहला प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeLeft() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) < Integer.parseInt(totalQuestions.getText().toString().trim())){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())+1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromRightAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह आखरी प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() { }
        });

        mainViewScroll.setOnTouchListener(new OnSwipeTouchListener(QuestionsCorrectAnswersActivity.this) {

            public void onSwipeTop() { }

            public void onSwipeRight() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) <= Integer.parseInt(totalQuestions.getText().toString().trim()) &&
                        Integer.parseInt(questionOrderHolder.getText().toString().trim()) > 1){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())-1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromLeftAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह पहला प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeLeft() {
                if(Integer.parseInt(questionOrderHolder.getText().toString().trim()) < Integer.parseInt(totalQuestions.getText().toString().trim())){
                    questionView(Integer.toString(Integer.parseInt(questionOrderHolder.getText().toString().trim())+1), quizIdHolder.getText().toString().trim());
                    swipeListenerView.startAnimation(inFromRightAnimation());
                } else {
                    Toast.makeText(QuestionsCorrectAnswersActivity.this, "यह आखरी प्रश्न है", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() { }
        });

    }// End onCreate method



    // Getting question
    private void questionView(final String question_order, final String quiz_id){

        mainView.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        questionLists = new ArrayList<>();
        questionLists.clear();

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String url = Constants.GET_QUIZ_QUESTION_BY_ORDER + quiz_id
                    + "?question_order=" + question_order
                    +"&user_id=" + sharedPreferences.getString("user_id", "null")
                    + "&api_token=" + sharedPreferences.getString("api_token", "null");

            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @SuppressLint("SetJavaScriptEnabled")
                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equals("200")) {

                            // Current question
                            questionIdHolder.setText(jsonObject.getString("question_id"));
                            questionText.setText(jsonObject.getString("question_text"));
                            String current_question_order = jsonObject.getString("question_order");
                            questionOrderHolder.setText(current_question_order);

                            if (jsonObject.getString("question_notes").isEmpty() ||
                                    jsonObject.getString("question_notes").equals("null")) {
                                questionNotes.setVisibility(View.GONE);
                            } else {
                                questionNotes.setText(jsonObject.getString("question_notes"));
                                questionNotes.setVisibility(View.VISIBLE);
                            }
                            // End current question

                            // Question image
                            if (!jsonObject.getString("question_image").isEmpty()){
                                webViewQuestionImage.getSettings().setBuiltInZoomControls(true);
                                webViewQuestionImage.getSettings().setJavaScriptEnabled(true);
                                webViewQuestionImage.loadUrl(jsonObject.getString("question_image"));
                                webViewQuestionImage.setVisibility(View.VISIBLE);
                            } else {
                                webViewQuestionImage.setVisibility(View.GONE);
                            }

                            // Getting all questions
                            final JSONArray questions_array = jsonObject.getJSONArray("all_questions");
                            int total_question_int = questions_array.length();
                            String total_question_string = Integer.toString(total_question_int);
                            totalQuestions.setText(total_question_string);
                            if (questions_array.length() > 0){
                                for (int i = 0; i < questions_array.length(); i++) {
                                    JSONObject o = questions_array.getJSONObject(i);
                                    UserQuestionCorrectList item = new UserQuestionCorrectList(
                                            o.getString("question_order"),
                                            o.getString("attended"),
                                            current_question_order
                                    );
                                    questionLists.add(item);
                                }
                                questionsAllAdapter = new UserQuestionsCorrectAdapter(questionLists, getApplicationContext(), QuestionsCorrectAnswersActivity.this, QuestionsCorrectAnswersActivity.this);
                                recyclerViewAllQuestions.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerViewAllQuestions.setAdapter(questionsAllAdapter);
                                recyclerViewAllQuestions.smoothScrollToPosition(Integer.parseInt(question_order));

                            }// End getting all questions

                            // Getting answers
                            getAnswers();

                            mainView.setVisibility(View.VISIBLE);
                            processing.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(getApplicationContext(), "Question not available", Toast.LENGTH_LONG).show();
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mainView.setVisibility(View.GONE);
                    processing.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }
    }// End getting question


        private void getAnswers(){
            answerLists = new ArrayList<>();
            answerLists.clear();

            recyclerViewAnswers.setVisibility(View.GONE);
            final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")
                    && sharedPreferences.contains("user_type")) {

                String url = Constants.GET_USER_ANSWER_BY_QUESTION + quizIdHolder.getText().toString().trim()
                        + "?question_id=" + questionIdHolder.getText().toString().trim()
                        + "&question_order=" + questionOrderHolder.getText().toString().trim()
                        +"&user_id=" + sharedPreferences.getString("user_id", "null")
                        + "&api_token=" + sharedPreferences.getString("api_token", "null");

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("status").equals("200")) {

                                // Getting all answers
                                JSONArray answers_array = jsonObject.getJSONArray("answers");
                                if (answers_array.length() > 0){
                                    for (int i = 0; i < answers_array.length(); i++) {
                                        JSONObject ans = answers_array.getJSONObject(i);
                                        UserAnswersCorrectList ans_item = new UserAnswersCorrectList(
                                                sharedPreferences.getString("user_id", "null"),
                                                sharedPreferences.getString("api_token", "null"),
                                                quizIdHolder.getText().toString().trim(),
                                                ans.getString("question_id"),
                                                questionOrderHolder.getText().toString().trim(),
                                                ans.getString("answer_specifier"),
                                                ans.getString("answer_text"),
                                                ans.getString("answer_image"),
                                                ans.getString("corrected"),
                                                ans.getString("is_correct")
                                        );
                                        answerLists.add(ans_item);
                                    }
                                    answersAdapter = new UserAnswersCorrectAdapter(answerLists, getApplicationContext(), QuestionsCorrectAnswersActivity.this);
                                    recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                    recyclerViewAnswers.setAdapter(answersAdapter);
                                    recyclerViewAnswers.setVisibility(View.VISIBLE);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Answers not available", Toast.LENGTH_LONG).show();
                                    recyclerViewAnswers.setVisibility(View.VISIBLE);
                                }// End getting all answers

                            } else {
                                Toast.makeText(getApplicationContext(), "Answers not available", Toast.LENGTH_LONG).show();
                                recyclerViewAnswers.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerViewAnswers.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recyclerViewAnswers.setVisibility(View.VISIBLE);
                    }
                });
                requestQueue.add(stringRequest);
            }
        }


        @Override
        public void onItemClicked(int question_position, int answer_position){

            if (question_position < 1000){
                // call back here
                String print = Integer.toString(question_position + 1);
                // Calling get question all
                questionView(print, quizIdHolder.getText().toString().trim());
            } else {
                Toast.makeText(getApplicationContext(), "something wrong! please try gain later.", Toast.LENGTH_LONG).show();
            }
        }


    // Animations
    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outToLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outToLeft.setDuration(500);
        outToLeft.setInterpolator(new AccelerateInterpolator());
        return outToLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation() {
        Animation outToRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outToRight.setDuration(500);
        outToRight.setInterpolator(new AccelerateInterpolator());
        return outToRight;
    }
    // Animations

}// End activity class
