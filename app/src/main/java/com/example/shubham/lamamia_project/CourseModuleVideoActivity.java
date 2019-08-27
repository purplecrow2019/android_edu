package com.example.shubham.lamamia_project;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.example.shubham.lamamia_project.adapter.CommentAdapter;
import com.example.shubham.lamamia_project.adapter.FilesAdapter;
import com.example.shubham.lamamia_project.adapter.ModuleContentAdapter;
import com.example.shubham.lamamia_project.adapter.UserQuizLeaderBoardAdapter;
import com.example.shubham.lamamia_project.model.FileLists;
import com.example.shubham.lamamia_project.model.LeaderBoardList;
import com.example.shubham.lamamia_project.model.VideoCommentChildList;
import com.example.shubham.lamamia_project.model.VideoCommentParentList;
import com.example.shubham.lamamia_project.model.VideoList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class CourseModuleVideoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Yo";
    //-- For module Content
    private RequestQueue rq;
    private ArrayList<VideoList> videoLists = new ArrayList<>();
    private RecyclerView recyclerViewCategory;
    private ModuleContentAdapter adapter_module_content;
    private LinearLayoutManager mLayoutManager;
    private String streamUrl;

    //-- For videos
    private TextView textViewVideoDescription, currentVideoOrder, txtVideoCommentShow, txtVideoInformationShow, videoLabel, txtTotalQuestions;
    public String status, message, total_videos;
    public Toolbar toolbar;
    private DrawerLayout drawer;
    private EditText currentOrderHolder, totalVideoCountHolder, currentVideoIdHolder, txtVideoParentComment,
            txtVideoUlrHolder, currentModuleIdHolder, courseIdHolder;
    private LinearLayout linearLayoutComment, linearLayoutDescription;

    private SimpleExoPlayer player;
    private LinearLayout relPostComment;
    final String DEFAULT = "";

    //-- Supporting documents
    private RecyclerView recyclerViewFiles;
    private ArrayList<FileLists> fileLists;
    private FilesAdapter adapter_files;
    private LinearLayoutManager mLayoutManagerFiles;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private SimpleExoPlayerView mExoPlayerView;
    private ExtractorMediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private ExpandableListView expListComments;
    private CommentAdapter commentAdapter;
    private ArrayList<VideoCommentParentList> videoCommentParentLists;
    private ArrayList<VideoCommentChildList> videoCommentChildLists;

    private RecyclerView quizLeaderBoardList;
    private UserQuizLeaderBoardAdapter leaderBoardAdapter;
    private ArrayList<LeaderBoardList> leaderBoardLists;
    private LinearLayoutManager mLayoutManagerLeaderBoard;

    private ScrollView mainViewQuiz, mainViewVideo, mainResultQuiz;
    private TextView quizName, quizInstruction, quizTime, questionTime, maxMarks, negativeMarks, quizTimeHolder, timeStatusEndStart;
    private Button startQuiz, score, quizResult, retake;
    private TextView average_time, correct_percentage, wrong_percentage, correct_answers, wrong_answers, left_answers, total_time;
    private TextView daysLeft, hoursLeft, minutesLeft, secondsLeft;
    private LinearLayout timeCounter, questionTotalTimeHolder, quizTotalTimeHolder;
    private ProgressBar exoPlayerProgressBar;

    private int playerStartTime, playerEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_module_video);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        mainViewVideo = findViewById(R.id.mainVideoView);

        // Quiz layout casting
        mainViewQuiz = findViewById(R.id.mainQuizStartView);
        quizName = findViewById(R.id.quizName);
        quizInstruction = findViewById(R.id.quizInstruction);
        startQuiz = findViewById(R.id.startQuiz);
        quizTime = findViewById(R.id.quizTime);
        questionTime = findViewById(R.id.questionTime);
        maxMarks = findViewById(R.id.maxMarks);
        negativeMarks = findViewById(R.id.negativeMarks);
        quizTimeHolder = findViewById(R.id.quizTimeHolder);
        txtTotalQuestions = findViewById(R.id.totalQuestions);
        mainResultQuiz = findViewById(R.id.mainQuizResultView);
        timeStatusEndStart = findViewById(R.id.timeStatusEndStart);
        ImageButton quizShare = findViewById(R.id.quizShareButton);
        Button seeAnswer = findViewById(R.id.seeAnswer);
        questionTotalTimeHolder = findViewById(R.id.questionTotalTimeHolder);
        quizTotalTimeHolder = findViewById(R.id.quizTotalTimeHolder);

        //Result elements
        average_time = findViewById(R.id.averageTime);
        correct_percentage = findViewById(R.id.correctPercentage);
        wrong_percentage = findViewById(R.id.attemptedPercentage);
        correct_answers = findViewById(R.id.correctAnswers);
        wrong_answers = findViewById(R.id.wrongAnswers);
        left_answers = findViewById(R.id.leftAnswers);
        total_time = findViewById(R.id.totalTimeTaken);
        score =  findViewById(R.id.score);
        quizResult = findViewById(R.id.quizResult);
        exoPlayerProgressBar = findViewById(R.id.exoPlayerProgressBar);


        // Time counters
        daysLeft = findViewById(R.id.txtViewDayLeft);
        hoursLeft = findViewById(R.id.txtViewHourLeft);
        minutesLeft = findViewById(R.id.txtViewMinuteLeft);
        secondsLeft = findViewById(R.id.txtViewSecondLeft);
        timeCounter = findViewById(R.id.timeCounter);

        // Comment
        relPostComment = findViewById(R.id.relativeLayoutPostParentComment);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Element by me
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        rq = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        expListComments = findViewById(R.id.expandableListViewComments);

        rq = Volley.newRequestQueue(this);
        textViewVideoDescription = findViewById(R.id.textViewVideoInformation);
        TextView txtNext = findViewById(R.id.textViewNext);
        TextView txtPrevious = findViewById(R.id.textViewPrevious);

        // Video previous/next attrs
        videoLabel = findViewById(R.id.textViewVideoLabel);
        ImageButton imgButtonBack = findViewById(R.id.imageButtonBack);

        recyclerViewFiles = findViewById(R.id.recyclerViewFilesHolder);

        currentOrderHolder = findViewById(R.id.order_for_current_video);
        currentVideoIdHolder = findViewById(R.id.textViewCurrentVideoIdHolder);
        currentModuleIdHolder = findViewById(R.id.textViewCurrentModuleIdHolder);

        txtVideoUlrHolder = findViewById(R.id.video_url_holder);
        mLayoutManagerFiles = new LinearLayoutManager(getApplicationContext());

        courseIdHolder = findViewById(R.id.courseIdHolder);
        totalVideoCountHolder = findViewById(R.id.total_video_count);
        currentVideoOrder = findViewById(R.id.textViewCurrentVideoOrder);
        txtVideoCommentShow = findViewById(R.id.textViewVideoCommentsClick);
        txtVideoInformationShow = findViewById(R.id.textViewVideoDescriptionClick);
        quizLeaderBoardList = findViewById(R.id.QuizLeaderBoardList);
        mLayoutManagerLeaderBoard = new LinearLayoutManager(getApplicationContext());

        linearLayoutComment = findViewById(R.id.linearLayoutCourseModuleComments);
        linearLayoutDescription = findViewById(R.id.linearLayoutVideoInformationHolder);

        // Comment posting elements
        txtVideoParentComment = findViewById(R.id.editTextSendParentComment);
        ImageButton btnVideoParentComment = findViewById(R.id.imageButtonSendParentComment);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null){
            String video_id = appLinkData.getQueryParameter("video_id");
            currentVideoIdHolder.setText(video_id);
        } else {
            try {
                String video_id = Objects.requireNonNull(getIntent().getExtras()).getString("video_id");
                currentVideoIdHolder.setText(video_id);
                if (video_id == null){
                    String module_id = Objects.requireNonNull(getIntent().getExtras()).getString("module_id");
                    String order_id = Objects.requireNonNull(getIntent().getExtras()).getString("order_id");
                    currentOrderHolder.setText(order_id);
                    currentModuleIdHolder.setText(module_id);
                    playVideo(module_id, order_id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Start quiz
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearQuizData(currentVideoIdHolder.getText().toString().trim());
                startQuiz.setVisibility(View.GONE);
            }
        });

        // See result
        quizResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewQuiz.setVisibility(View.GONE);
                mainResultQuiz.setVisibility(View.VISIBLE);
            }
        });

        // see answers
        seeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), QuestionsCorrectAnswersActivity.class);
                activityChangeIntent.putExtra("quiz_id", currentVideoIdHolder.getText().toString().trim());
                activityChangeIntent.putExtra("quiz_time", quizTimeHolder.getText().toString().trim());
                activityChangeIntent.putExtra("quiz_name", videoLabel.getText().toString().trim());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(activityChangeIntent);
            }
        });

        // Share video
        ImageButton shareVideo = findViewById(R.id.videoShareButton);
        shareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Sending data to share
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://lamamia.in/api/public/video?video_id="+ currentVideoIdHolder.getText().toString().trim());
                Intent chooserIntent = Intent.createChooser(sharingIntent, "Share using");
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);

                // Counting every shares
                String url = Constants.POST_SHARE_COUNT + courseIdHolder.getText().toString().trim();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                rq.add(jsonObjectRequest);
            }
        });

        // Share video
        quizShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("api_token")
                        && sharedPreferences.contains("user_type")) {

                    screenShortAndShare();

                    // Counting every shares
                    String url = Constants.POST_SHARE_COUNT + courseIdHolder.getText().toString().trim();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { }
                    });
                    rq.add(jsonObjectRequest);

                }
            }
        });

        // When next Button clicked
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOrderHolder.getText().toString().trim().equals(totalVideoCountHolder.getText().toString().trim())){
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(),"यह इस मॉड्यूल का अंतिम वीडियो है", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                } else {

                    // Saving current position
                    Intent activityChangeIntent = new Intent(getApplicationContext(), CourseModuleVideoActivity.class);
                    int nxtVideo = Integer.parseInt(currentOrderHolder.getText().toString().trim()) + 1;
                    String video_next = Integer.toString(nxtVideo);
                    activityChangeIntent.putExtra("order_id",video_next);
                    activityChangeIntent.putExtra("module_id", currentModuleIdHolder.getText().toString().trim());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(activityChangeIntent);
                    finish();

                }
            }
        });

        // When previous button clicked
        txtPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentOrderHolder.getText().toString().trim().equals("1")){

                    Toast toast_not_sent = Toast.makeText(getApplicationContext(),"आप पहला वीडियो देख रहे हैं।", Toast.LENGTH_SHORT);
                    toast_not_sent.show();

                } else {

                    Intent activityChangeIntent = new Intent(getApplicationContext(), CourseModuleVideoActivity.class);

                    int nxtVideo = Integer.parseInt(currentOrderHolder.getText().toString().trim()) - 1;

                    String video_next = Integer.toString(nxtVideo);
                    activityChangeIntent.putExtra("order_id",video_next);
                    activityChangeIntent.putExtra("module_id",currentModuleIdHolder.getText().toString().trim());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(activityChangeIntent);
                    finish();
                }
            }
        });

        imgButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnVideoParentComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendParentComment();
            }
        });

        txtVideoCommentShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDescription.setVisibility(View.GONE);
                linearLayoutComment.setVisibility(View.VISIBLE);
                relPostComment.setVisibility(View.VISIBLE);
                txtVideoCommentShow.setBackgroundColor(getResources().getColor(R.color.white));
                txtVideoCommentShow.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtVideoInformationShow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txtVideoInformationShow.setTextColor(getResources().getColor(R.color.white));
            }
        });

        txtVideoInformationShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutComment.setVisibility(View.GONE);
                linearLayoutDescription.setVisibility(View.VISIBLE);
                relPostComment.setVisibility(View.GONE);
                txtVideoInformationShow.setBackgroundColor(getResources().getColor(R.color.white));
                txtVideoInformationShow.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtVideoCommentShow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txtVideoCommentShow.setTextColor(getResources().getColor(R.color.white));
            }
        });

        linearLayoutDescription.setVisibility(View.VISIBLE);
        linearLayoutComment.setVisibility(View.GONE);
        txtVideoCommentShow.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        txtVideoCommentShow.setTextColor(getResources().getColor(R.color.white));
        txtVideoInformationShow.setBackgroundColor(getResources().getColor(R.color.white));
        txtVideoInformationShow.setTextColor(getResources().getColor(R.color.colorPrimary));
        relPostComment.setVisibility(View.GONE);

        // Retake button
        retake = findViewById(R.id.quizRetake);
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizResult.setVisibility(View.VISIBLE);
                mainViewQuiz.setVisibility(View.VISIBLE);
                mainResultQuiz.setVisibility(View.GONE);
            }
        });

    }// End onCreate


    @Override
    protected void onResume() {
        super.onResume();

        getVideoInformation(currentVideoIdHolder.getText().toString().trim());

        if (mExoPlayerView == null) {
            mExoPlayerView = findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();
        }

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(CourseModuleVideoActivity.this, R.drawable.ic_fullscreen_exit_black_24dp));
            mFullScreenDialog.show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(CourseModuleVideoActivity.this, R.drawable.ic_fullscreen_exit_black_24dp));
            mFullScreenDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();

//        if (player != null) {
//            player.setPlayWhenReady(false);
//            player.stop();
//            player.seekTo(0);
//        }
        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (player != null) {
            addVideoCurrentTime(Long.toString(player.getCurrentPosition()));
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_course_module_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu) {
            if (drawer.isDrawerOpen(Gravity.END)) {
                drawer.closeDrawer(Gravity.END);
            } else {
                drawer.openDrawer(Gravity.END);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }



    private void playVideo(final String module_id, final String order_id){

        fileLists = new ArrayList<>();

        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String user_id = sharedPreferences.getString("user_id", DEFAULT);
            String user_type = sharedPreferences.getString("user_type", DEFAULT);

            // Course API call
            String url = Constants.GET_VIDEO_BY_ORDER +
                    "?module_id=" + module_id +
                    "&order_id=" + order_id +
                    "&user_id=" + user_id +
                    "&user_type=" + user_type + "";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        status = response.getString("status");
                        message = response.getString("message");
                        int result_status = Integer.parseInt(status);

                        if (result_status == 200) {

                            try {
                                JSONArray attachments_array = response.getJSONArray("attachments");

                                for (int i = 0; i < attachments_array.length(); i++) {
                                    JSONObject o = attachments_array.getJSONObject(i);
                                    FileLists item = new FileLists(
                                            o.getString("video_attachment_id"),
                                            o.getString("attachment_url"),
                                            o.getString("attachment_type"),
                                            o.getString("attachment_name")
                                    );
                                    fileLists.add(item);
                                }

                                adapter_files = new FilesAdapter(fileLists, getApplicationContext());
                                recyclerViewFiles.setLayoutManager(mLayoutManagerFiles);
                                recyclerViewFiles.setAdapter(adapter_files);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (response.getString("content_type").equals("1")){

                                mainViewQuiz.setVisibility(View.GONE);
                                mainViewVideo.setVisibility(View.VISIBLE);
                                videoLabel.setText(response.getString("content_name"));
                                streamUrl = response.getString("video_url");
                                total_videos = response.getString("total_videos");
                                String currentStreamVideoByTotal = order_id + "/" + total_videos;
                                currentVideoOrder.setText(currentStreamVideoByTotal);
                                String position = response.getString("current_video_position");
                                textViewVideoDescription.setText(Html.fromHtml(response.getString("video_description")));
                                currentVideoIdHolder.setText(response.getString("content_id"));
                                totalVideoCountHolder.setText(response.getString("total_videos"));
                                getVideoComments(response.getString("content_id"));
                                playPlayer(streamUrl, position);
                                txtVideoUlrHolder.setText(streamUrl);

                            } else if (response.getString("content_type").equals("2")){

                                // If content is quiz
                                videoLabel.setText(response.getString("content_name"));
                                quizName.setText(response.getString("content_name"));
                                quizInstruction.setText(Html.fromHtml(response.getString("video_description")));
                                total_videos = response.getString("total_videos");
                                String currentStreamVideoByTotal = order_id + "/" + total_videos;
                                currentVideoOrder.setText(currentStreamVideoByTotal);
                                textViewVideoDescription.setText(Html.fromHtml("<![CDATA[" + response.getString("video_description") + "]]>"));
                                currentVideoIdHolder.setText(response.getString("content_id"));
                                totalVideoCountHolder.setText(response.getString("total_videos"));
                                maxMarks.setText(response.getString("max_marks"));

                                // If quiz time not available
                                if (response.getString("quiz_time").equals("00:00")){
                                    quizTotalTimeHolder.setVisibility(View.GONE);
                                }
                                quizTime.setText(response.getString("quiz_time"));

                                // If question time not available
                                if (response.getString("question_time").equals("00:00")){
                                    questionTotalTimeHolder.setVisibility(View.GONE);
                                }
                                questionTime.setText(response.getString("question_time"));

                                // Negative marks show
                                if (response.getString("negative_marks").equals("null")){
                                    negativeMarks.setText("0");
                                } else {
                                    negativeMarks.setText(response.getString("negative_marks"));
                                }
                                quizTimeHolder.setText(response.getString("quiz_time_mili"));
                                txtTotalQuestions.setText(response.getString("total_questions"));
                                mainViewQuiz.setVisibility(View.VISIBLE);
                                mainViewVideo.setVisibility(View.GONE);
                                mainResultQuiz.setVisibility(View.GONE);

                                // Time of quiz
                                if (response.getString("time_status").equals("2")){
                                    long different = Long.parseLong(response.getString("millis"));
                                    MyCount counter = new MyCount(different, 1000);
                                    counter.start();
                                    startQuiz.setVisibility(View.GONE);
                                    timeStatusEndStart.setText("के बाद यह क्विज प्रारम्भ होगा");
                                    timeCounter.setVisibility(View.VISIBLE);
                                } else if (response.getString("time_status").equals("3")){
                                    startQuiz.setVisibility(View.GONE);
                                    retake.setVisibility(View.GONE);
                                    timeStatusEndStart.setText("यह क्विज समाप्त हो चूका है");
                                    timeCounter.setVisibility(View.VISIBLE);
                                } else if (response.getString("time_status").equals("1")){
                                    long different = Long.parseLong(response.getString("millis"));
                                    if(different > 1000){
                                        MyCount counter = new MyCount(different, 1000);
                                        counter.start();
                                        timeStatusEndStart.setText("के लिए यह क्विज सक्रिय है");
                                        startQuiz.setVisibility(View.VISIBLE);
                                        timeCounter.setVisibility(View.VISIBLE);
                                    } else {
                                        timeCounter.setVisibility(View.GONE);
                                        startQuiz.setVisibility(View.VISIBLE);
                                    }
                                }// End time of quiz

                                if(response.getString("old_data").equals("1")){
                                    makeLeaderBoard(response.getString("content_id"));
                                    correct_percentage.setText(response.getString("correct_percentage"));
                                    wrong_percentage.setText(response.getString("wrong_percentage"));
                                    average_time.setText(response.getString("average_time"));
                                    correct_answers.setText(response.getString("total_correct_answers"));
                                    wrong_answers.setText(response.getString("total_wrong_answers"));
                                    left_answers.setText(response.getString("total_left_answers"));
                                    total_time.setText(response.getString("total_time"));
                                    score.setText(response.getString("final_marks"));
                                    mainViewQuiz.setVisibility(View.GONE);
                                    mainViewVideo.setVisibility(View.GONE);
                                    mainResultQuiz.setVisibility(View.VISIBLE);
                                    quizResult.setVisibility(View.VISIBLE);
                                } else {
                                    quizResult.setVisibility(View.GONE);
                                }
                            }
                            makeModuleVideoList(currentModuleIdHolder.getText().toString().trim(), currentOrderHolder.getText().toString().trim());
                        } else {
                            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है या कनेक्ट नहीं है।");
                }
            });
            rq.add(jsonObjectRequest);
        }
    }// End method makeVideoPlay


    //------------------------------------------------     Making videos list by Module --------------------------------------------------------
    public void makeModuleVideoList(String module_id, final String order_id){

        videoLists = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String user_id = sharedPreferences.getString("user_id", DEFAULT);

            String url = Constants.GET_VIDEO_LIST_BY_MODULE + module_id + "?user_id=" + user_id + "";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("videos");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            VideoList item = new VideoList(
                                    o.getString("content_id"),
                                    o.getString("content_name"),
                                    o.getString("video_description"),
                                    o.getString("module_id"),
                                    o.getString("content_order"),
                                    o.getString("video_view_status"),
                                    order_id,
                                    o.getString("content_type")
                            );
                            videoLists.add(item);
                        }

                        adapter_module_content = new ModuleContentAdapter(videoLists, getApplicationContext(), CourseModuleVideoActivity.this);
                        recyclerViewCategory.setLayoutManager(mLayoutManager);
                        recyclerViewCategory.setAdapter(adapter_module_content);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "module list not available");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Server error in making module list");
                }
            });

            rq.add(stringRequest);
        }
    }

    //---------------------------------------------------------- Parent comment ----------------------------------------------------------------
    public void sendParentComment(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            final String user_type = sharedPreferences.getString("user_type", DEFAULT);
            final String current_video_id = currentVideoIdHolder.getText().toString().trim();

            if (txtVideoParentComment.getText().toString().trim().isEmpty()){

                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया पहले एक टिप्पणी लिखें|");

            }else{

                // Needed user_id, api_token, text, video_id, parent_id;
                String url = Constants.POST_PARENT_COMMENT + current_video_id;

                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200){

                                txtVideoParentComment.setText("");

                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);

                                assert inputManager != null;
                                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);

                                Utility.showSnackBarSuccessShort(getApplicationContext(), findViewById(android.R.id.content), "आपकी टिप्पणी दर्ज कर दी गई है।");
                                getVideoComments(current_video_id);

                            } else {

                                // Making layout changes
                                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            // Making layout changes
                            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("user_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("text", txtVideoParentComment.getText().toString().trim());
                        MyData.put("parent_id", "0");
                        MyData.put("user_type", user_type);
                        return MyData;
                    }
                };

                rq.add(StringRequest);

            }

        }

    }

    //--------------------------- Switching Video Go full/normal Screen ----------------------------
    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(CourseModuleVideoActivity.this, R.drawable.ic_fullscreen_exit_black_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();

    }


    private void closeFullscreenDialog() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(CourseModuleVideoActivity.this, R.drawable.ic_fullscreen_black_24dp));
    }


    private void initFullscreenButton() {
        PlayerControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer(String position) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);


        player.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

            @Override
            public void onLoadingChanged(boolean isLoading) {}

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == SimpleExoPlayer.STATE_BUFFERING){
                    exoPlayerProgressBar.setVisibility(View.VISIBLE);
                    mExoPlayerView.setVisibility(View.GONE);
                } else {
                    exoPlayerProgressBar.setVisibility(View.INVISIBLE);
                    mExoPlayerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {}

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

            @Override
            public void onSeekProcessed() {

            }
        });

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        player.prepare(mVideoSource);
        player.setPlayWhenReady(true);

        long position_long = Long.parseLong(position);
        player.seekTo(position_long);
    }


    public void playPlayer(String getStreamUrl, String position){

        String userAgent = Util.getUserAgent(CourseModuleVideoActivity.this, getApplicationContext().getApplicationInfo().packageName);
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(CourseModuleVideoActivity.this, null, httpDataSourceFactory);
        Uri daUri;

        if (getStreamUrl == null){
            getStreamUrl = txtVideoUlrHolder.getText().toString().trim();
            daUri = Uri.parse(getStreamUrl);
        } else {
            if (getStreamUrl.isEmpty()){
                getStreamUrl = txtVideoUlrHolder.getText().toString().trim();
                daUri = Uri.parse(getStreamUrl);
            } else {
                daUri = Uri.parse(getStreamUrl);
            }
        }

        mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(daUri);
        initExoPlayer(position);

    }// End method playPlayer

    //------------------------------------------------------------- Getting comments for videos ----------------------------------------------
    public void getVideoComments(String video_id){

        videoCommentParentLists = new ArrayList<>();

        String url = Constants.GET_VIDEO_COMMENTS + video_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                if (s != null) {

                    try {
                            JSONArray JsonArrayComments = new JSONArray(s);

                            if(JsonArrayComments.length() > 0) {

                                for (int k = 0; k < JsonArrayComments.length(); k++) {

                                    JSONObject o = JsonArrayComments.getJSONObject(k);

                                    VideoCommentParentList videoCommentParentList = new VideoCommentParentList();
                                    videoCommentParentList.setVideo_id(o.getString("video_id"));
                                    videoCommentParentList.setVideo_comment_text(o.getString("video_comment_text"));
                                    videoCommentParentList.setVideo_comment_id(o.getString("video_comment_id"));
                                    videoCommentParentList.setUser_name(o.getString("user_name"));
                                    videoCommentParentList.setUser_image(o.getString("user_image"));
                                    videoCommentParentList.setUser_type(o.getString("user_type"));

                                    videoCommentParentList.setVideo_comment_text(videoCommentParentList.getVideo_comment_text());

                                    videoCommentChildLists = new ArrayList<>();

                                    JSONArray ja = new JSONArray(o.getString("child"));

                                    for (int i = 0; i < ja.length(); i++) {

                                        JSONObject jo = ja.getJSONObject(i);

                                        VideoCommentChildList videoCommentChildList = new VideoCommentChildList();

                                        videoCommentChildList.setVideo_id(jo.getString("video_id"));
                                        videoCommentChildList.setVideo_comment_id(jo.getString("video_comment_id"));
                                        videoCommentChildList.setVideo_comment_text(jo.getString("video_comment_text"));
                                        videoCommentChildList.setUser_name(jo.getString("user_name"));
                                        videoCommentChildList.setUser_image(jo.getString("user_image"));
                                        videoCommentChildList.setUser_type(jo.getString("user_type"));

                                        videoCommentChildLists.add(videoCommentChildList);
                                    }

                                    videoCommentParentList.setVideoCommentChildLists(videoCommentChildLists);
                                    videoCommentParentLists.add(videoCommentParentList);
                                }

                            }
                            commentAdapter = new CommentAdapter(getApplicationContext(), videoCommentParentLists);
                            expListComments.setAdapter(commentAdapter);

                            // Setting dynamic height
                            linearLayoutComment.getLayoutParams().height = JsonArrayComments.length() * 500;
                            linearLayoutComment.requestLayout();

                    } catch (JSONException e) {
                        e.printStackTrace();
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

    }// End method getVideoComments


    //----------------------------------------------------------- Saving current time for current user -----------------------------------------
    private void addVideoCurrentTime(String currentTime){

        // Ending the player after saving current data
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
        }

        if (player != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

            if (sharedPreferences.contains("user_id") && sharedPreferences.contains("api_token") && sharedPreferences.contains("user_type")) {
                String DEFAULT = "na";
                String user_id = sharedPreferences.getString("user_id", DEFAULT);
                String api_token = sharedPreferences.getString("api_token", DEFAULT);
                String user_type = sharedPreferences.getString("user_type", DEFAULT);

                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                // Needed user_id, api_token, text, video_id, parent_id;
                String url = Constants.POST_USER_CURRENT_VIDEO_POSITION + "?user_id=" + user_id + "&api_token="
                        + api_token + "&user_type=" + user_type
                        + "&content_id=" + currentVideoIdHolder.getText().toString()
                        + "&current_position=" + currentTime
                        + "&current_date_time=" + currentDateTimeString;
                url = url.replaceAll(" ", "%20");

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Ending the player after saving current data
                        if (player != null) {
                            player.setPlayWhenReady(false);
                            player.stop();
                            player.seekTo(0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ending the player if any problem occurs
                        if (player != null) {
                            player.setPlayWhenReady(false);
                            player.stop();
                            player.seekTo(0);
                        }
                    }
                });

                rq.add(jsonObjectRequest);

            }// End checking for shared data

        }// End checking if player playing

    }// End method addVideoCurrentTime



    // Calling first for getting video information ----------------------------------------------------------------------------------------
    private void getVideoInformation(String video_id){
        // Checking for video id
        if (video_id != null && !video_id.equals("")){
            String url = Constants.GET_ALL_VALUE_BY_VIDEO + video_id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        String status = jsonObject.getString("status");
                        int result_status = Integer.parseInt(status);

                        if (result_status == 200) {

                            currentModuleIdHolder.setText(jsonObject.getString("module_id"));
                            currentOrderHolder.setText(jsonObject.getString("content_order"));
                            courseIdHolder.setText(jsonObject.getString("course_id"));
                            // Calling video play method
                            playVideo(jsonObject.getString("module_id"), jsonObject.getString("content_order"));

                        } else {
                            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "unable to get video data");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            rq.add(stringRequest);
        }
    }// End method getVideoInformation



    //------------------------------------------------ Making videos list by Module --------------------------------------------------------
    private void makeLeaderBoard(String quiz_id){

        leaderBoardLists = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String url = Constants.GET_QUIZ_LEADERBOARD + quiz_id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("leaderboard");

                        // Checking for array length
                        if (array.length() > 0){

                            // Looping array data
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                LeaderBoardList item = new LeaderBoardList(
                                        o.getString("user_id"),
                                        i+1 +". "+ o.getString("user_name"),
                                        o.getString("obtained_marks"),
                                        o.getString("total_time")
                                );
                                leaderBoardLists.add(item);
                            }

                            // Assigning to adapter
                            leaderBoardAdapter = new UserQuizLeaderBoardAdapter(leaderBoardLists, getApplicationContext());
                            quizLeaderBoardList.setLayoutManager(mLayoutManagerLeaderBoard);
                            quizLeaderBoardList.setAdapter(leaderBoardAdapter);

                        }else{
                            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "Leader-board list not available");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "Leader-board list not available");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "Leader-board list not available");
                }
            });
            rq.add(stringRequest);
        }
    }

    //----------- Quiz clear -----------------------------------------------------------------------
    private void clearQuizData(String quiz_id){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            String url = Constants.USER_CLEAR_ALL_QUIZ_DATA + quiz_id
                    + "?user_id=" + sharedPreferences.getString("user_id", "null")
                    + "&api_token=" + sharedPreferences.getString("api_token", "null");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("status");

                        if (status.equals("200")){
                            startQuiz.setVisibility(View.VISIBLE);
                            Intent activityChangeIntent = new Intent(getApplicationContext(), QuestionsActivity.class);
                            activityChangeIntent.putExtra("quiz_id", currentVideoIdHolder.getText().toString().trim());
                            activityChangeIntent.putExtra("quiz_time", quizTimeHolder.getText().toString().trim());
                            activityChangeIntent.putExtra("quiz_name", videoLabel.getText().toString().trim());
                            activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(activityChangeIntent);
                        } else {
                            startQuiz.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),
                                    "Please try again later", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        startQuiz.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Please try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    startQuiz.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
            rq.add(stringRequest);
        }
    }


    public class MyCount extends CountDownTimer {
        MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            playVideo(currentModuleIdHolder.getText().toString().trim(), currentOrderHolder.getText().toString().trim());
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String days = Long.toString(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
            String hours = Long.toString(TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)));
            String minutes = Long.toString(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
            String seconds = Long.toString(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            daysLeft.setText(days);
            hoursLeft.setText(hours);
            minutesLeft.setText(minutes);
            secondsLeft.setText(seconds);
        }
    }



    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 24) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    screenShortAndShare();
                } else {
                    // Please grant permissions
                    Toast.makeText(getApplicationContext(),
                            "Please grant permission and try again.", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    screenShortAndShare();
                } else {
                    // Please grant permissions
                    Toast.makeText(getApplicationContext(),
                            "Please grant permission and try again.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void screenShortAndShare() {

        // Checking for write permission
        boolean data =  isWriteStoragePermissionGranted();
        if (data){
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            try {
                // image naming and path  to include sd card  appending name you choose for file
                String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";

                // Getting view of result
                mainResultQuiz.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mainResultQuiz.getDrawingCache());
                mainResultQuiz.setDrawingCacheEnabled(false);

                File imageFile = new File(mPath);

                FileOutputStream outputStream = new FileOutputStream(imageFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();

                //setting screenshot in imageView
                String filePath = imageFile.getPath();
                Log.d("FilePath: ", filePath);

                shareIntentNow(filePath);

            } catch (Throwable e) {
                // Several error may come out with file handling or DOM
                e.printStackTrace();
            }

        } else {
            screenShortAndShare();
        }
    }


    private void shareIntentNow(String filePath){

        // Checking for read storage permission
        boolean data = isReadStoragePermissionGranted();
        if (data){
            SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")
                    && sharedPreferences.contains("user_type")) {
                File file = new File(filePath);

                if (Build.VERSION.SDK_INT >= 24){
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".my.package.name.provider", file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://lamamia.in/api/public/video?video_id="
                            + currentVideoIdHolder.getText().toString().trim()
                            + "&user=" + sharedPreferences.getString("user_id", DEFAULT) + "\n ");
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    Uri photoURI = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://lamamia.in/api/public/video?video_id="
                            + currentVideoIdHolder.getText().toString().trim()
                            + "&user=" + sharedPreferences.getString("user_id", DEFAULT) + "\n ");
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }
        } else {
            shareIntentNow(filePath);
        }
    }

}// End Activity
