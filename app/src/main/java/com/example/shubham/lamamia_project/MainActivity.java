package com.example.shubham.lamamia_project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.shubham.lamamia_project.adapter.CourseAdapter;
import com.example.shubham.lamamia_project.model.ListItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.shubham.lamamia_project.Utils.Constants.SEND_DEVICE_TOKEN;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RequestQueue rq;
    boolean doubleBackToExitPressedOnce = false;
    private TextView textViewNotificationCount, learnVideo, learnQuiz, teach;
    private LinearLayout error, processing;
    private LinearLayout instructorView;

    // For courses requests
    private RequestQueue requestQueue;
    private ArrayList<ListItem> listItemsVideo = new ArrayList<>();
    private ArrayList<ListItem> listItemsQuiz = new ArrayList<>();
    private RecyclerView recyclerViewVideoCourses, recyclerViewQuizCourses;
    private CourseAdapter adapter;
    private LinearLayoutManager videoLayoutManager, quizLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing view elements
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        instructorView = findViewById(R.id.mainView);

        learnVideo = findViewById(R.id.textViewMainActivityLearnVideo);
        learnQuiz = findViewById(R.id.textViewMainActivityLearnQuiz);
        teach = findViewById(R.id.textViewMainActivityTeach);

        // Element by me
        recyclerViewVideoCourses = findViewById(R.id.userVideoCourseList);
        recyclerViewQuizCourses = findViewById(R.id.userQuizCourseList);
        requestQueue = Volley.newRequestQueue(this);
        videoLayoutManager = new LinearLayoutManager(getApplicationContext());
        quizLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Calling API requests
        getVideoCourses();
        getQuizCourses();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";
            String user_type = sharedPreferences.getString("user_type", DEFAULT);
            final String phone = sharedPreferences.getString("phone", DEFAULT);

            // Fire base id change
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    Log.d("Token_FB", token);
                    sendDeviceToken(token, phone);
                }
            });

            if (user_type.equals("1")){

                // Video option selected
                learnVideo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                learnVideo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                // Learn quiz option
                learnQuiz.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnQuiz.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                // Teach option
                teach.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                teach.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                // Visibility change
                recyclerViewVideoCourses.setVisibility(View.VISIBLE);
                recyclerViewQuizCourses.setVisibility(View.GONE);
                instructorView.setVisibility(View.GONE);

            } else if(user_type.equals("2")) {

                // Teach option selected
                teach.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                teach.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                // Quiz course option
                learnQuiz.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnQuiz.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                // Video course option
                learnVideo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnVideo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                // Visibility change
                instructorView.setVisibility(View.VISIBLE);
                recyclerViewVideoCourses.setVisibility(View.GONE);
                recyclerViewQuizCourses.setVisibility(View.GONE);
            }
        }


        // For help
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityChangeIntent = new Intent(MainActivity.this, HelpActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        // End for help

        // For whatsApp chat
        FloatingActionButton fab_wsp = findViewById(R.id.fab_whatsapp);
        fab_wsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                    if (sharedPreferences.contains("user_type")) {
                        String user_type = sharedPreferences.getString("user_type", "null");
                        if(user_type.equals("2")){
                            Intent sendIntent =new Intent("android.intent.action.MAIN");
                            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                            sendIntent.putExtra("jid", "919811923344" +"@s.whatsapp.net");
                            sendIntent.setPackage("com.whatsapp");
                            startActivity(sendIntent);
                        } else {
                            Intent sendIntent =new Intent("android.intent.action.MAIN");
                            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                            sendIntent.putExtra("jid", "919870136707" +"@s.whatsapp.net");
                            sendIntent.setPackage("com.whatsapp");
                            startActivity(sendIntent);
                        }
                    }
                } catch(Exception e) {
                    Toast.makeText(getApplicationContext(),"आपके पास व्हात्सप्प नहीं है कृपया डाउनलोड करें",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // End for whatsApp chat

        // Floating button fb messenger
        FloatingActionButton fab_fb_messenger = findViewById(R.id.fab_messenger);
        fab_fb_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messengerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/" + "LamamiaOfficial"));
                messengerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                messengerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(messengerIntent);
            }
        });
        // End floating button fb messenger

        // Notification button
        ImageButton imgBtnNotification = findViewById(R.id.imageButtonNotification);
        imgBtnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(MainActivity.this, NotificationActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        });
        // End notification button

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        textViewNotificationCount = findViewById(R.id.textViewMainNotificationCount);

        // Linear layouts button
        LinearLayoutCompat create_profile = findViewById(R.id.create_educator_profile);
        LinearLayoutCompat create_course = findViewById(R.id.create_course);
        LinearLayoutCompat create_module = findViewById(R.id.create_module);
        LinearLayoutCompat create_video = findViewById(R.id.create_video);
        LinearLayoutCompat courses = findViewById(R.id.your_courses);
        LinearLayoutCompat monetization = findViewById(R.id.monetization);
        LinearLayoutCompat insights = findViewById(R.id.insights);

        // Create profile
        create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorProfileActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });
        // End create profile

        // Redirect to course add
        create_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorCourseAddActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });
        // End redirect to course add

        // Redirect to module add
        create_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddModuleActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

        // Redirect to video add
        create_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorAddVideoQuizActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

        // Redirect to course view
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorCoursesActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
            }
        });

        // Redirect to monetization page
        monetization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), MonetizationMainActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        // Redirect to insights
        insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getApplicationContext(), CourseInsightsActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityChangeIntent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        // Element by me
        rq = Volley.newRequestQueue(this);
        getNotificationCount();

        // When clicked on learning option
        learnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // visible video course
                instructorView.setVisibility(View.GONE);
                recyclerViewQuizCourses.setVisibility(View.GONE);
                recyclerViewVideoCourses.setVisibility(View.VISIBLE);

                learnVideo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                learnVideo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                teach.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                teach.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                learnQuiz.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnQuiz.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("phone")
                        && sharedPreferences.contains("user_type")){
                    String DEFAULT = "null";
                    String user_phone = sharedPreferences.getString("phone", DEFAULT);

                    if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
                        String url = Constants.SWITCH_INSTRUCTOR_TO_USER + "?phone="+ user_phone +"";
                        switchUser(url);
                    }

                }else{
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(), "आपके प्रमाण पत्र सेट नहीं हैं। कृपया लॉगआउट करें और फिर से लॉगिन करें।", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                }
            }
        });// End when clicked on learning option

        // When clicked on learning option
        learnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // visible video course
                instructorView.setVisibility(View.GONE);
                recyclerViewVideoCourses.setVisibility(View.GONE);
                recyclerViewQuizCourses.setVisibility(View.VISIBLE);

                learnQuiz.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                learnQuiz.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                learnVideo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnVideo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                teach.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                teach.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("phone")
                        && sharedPreferences.contains("user_type")){
                    String DEFAULT = "null";
                    String user_phone = sharedPreferences.getString("phone", DEFAULT);

                    if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
                        String url = Constants.SWITCH_INSTRUCTOR_TO_USER + "?phone="+ user_phone +"";
                        switchUser(url);
                    }

                }else{
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(), "आपके प्रमाण पत्र सेट नहीं हैं। कृपया लॉगआउट करें और फिर से लॉगिन करें।", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                }
            }
        });// End when clicked on learning option


        // When clicked on teaching option
        teach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // visible video course
                recyclerViewVideoCourses.setVisibility(View.GONE);
                recyclerViewQuizCourses.setVisibility(View.GONE);
                instructorView.setVisibility(View.VISIBLE);

                teach.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));
                teach.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                learnQuiz.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnQuiz.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                learnVideo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                learnVideo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkBlue));

                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("phone")
                        && sharedPreferences.contains("user_type")){
                    String DEFAULT = "null";
                    String user_phone = sharedPreferences.getString("phone", DEFAULT);

                    if (sharedPreferences.getString("user_type", DEFAULT).equals("1")){
                        String url = Constants.SWITCH_USER_TO_INSTRUCTOR + "?phone="+ user_phone +"";
                        switchUser(url);
                    }else if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
                        Toast toast_not_sent = Toast.makeText(getApplicationContext(),"आप अभी सिखा रहे हैं", Toast.LENGTH_SHORT);
                        toast_not_sent.show();
                    }

                } else {
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(), "आपके प्रमाण पत्र सेट नहीं हैं। कृपया लॉगआउट करें और फिर से लॉगिन करें।", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                }
            }
        });


        // Reload activity
        Button reload = findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("phone")
                        && sharedPreferences.contains("user_type")){
                    String DEFAULT = "null";
                    String user_phone = sharedPreferences.getString("phone", DEFAULT);

                    if (sharedPreferences.getString("user_type", DEFAULT).equals("1")){
                        String url = Constants.SWITCH_USER_TO_INSTRUCTOR + "?phone="+ user_phone +"";
                        switchUser(url);
                    }else if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
                        String url = Constants.SWITCH_INSTRUCTOR_TO_USER + "?phone="+ user_phone +"";
                        switchUser(url);
                    }

                }else{
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(), "आपके प्रमाण पत्र सेट नहीं हैं। कृपया लॉगआउट करें और फिर से लॉगिन करें।", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                }
            }
        });

    }// End on create


    public void getNotificationCount(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";
            String user_id = sharedPreferences.getString("user_id", DEFAULT);
            String user_type = sharedPreferences.getString("user_type", DEFAULT);

            String url = Constants.GET_NOTIFICATION_COUNT + user_id + "?user_type=" + user_type + "";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        String status = response.getString("status");
                        int result_status = Integer.parseInt(status);

                        if (result_status == 200) {
                            textViewNotificationCount.setText(response.getString("notification_count"));
                        } else {
                            //Message on sms not sent
                            String message_not_sent = "Bad request.";
                            Toast toast_not_sent = Toast.makeText(getApplicationContext(), message_not_sent, Toast.LENGTH_SHORT);
                            toast_not_sent.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            rq.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("phone")
                && sharedPreferences.contains("user_type")){
            String DEFAULT = "null";
            if (sharedPreferences.getString("user_type", DEFAULT).equals("1")){
                Intent activityChangeIntent = new Intent(MainActivity.this, UserProfileActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(activityChangeIntent);
            } else if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
                Intent activityChangeIntent = new Intent(MainActivity.this, InstructorProfileActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(activityChangeIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_terms) {

            Intent activityChangeIntent = new Intent(MainActivity.this, TermsActivity.class);
            activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.this.startActivity(activityChangeIntent);

        } else if (id == R.id.nav_youtube){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCCySc9HlckG2zFhoNHDb2wA"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        } else if(id == R.id.nav_facebook){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/LamamiaOfficial"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        } else if (id == R.id.nav_twitter){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/LamamiaOfficial"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        } else if (id == R.id.nav_instagram){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/lamamiaofficial"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        } else if (id == R.id.nav_linked_in){

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://in.linkedin.com/company/lamamia-private-limited"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        } else if(id == R.id.nav_app_share){

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.lamamia.android.lamamia");
            Intent chooserIntent = Intent.createChooser(sharingIntent, "Share using");
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(chooserIntent);

        } else if (id == R.id.nav_log_out){

            Intent activityChangeIntent = new Intent(MainActivity.this, LogoutActivity.class);
            activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.this.startActivity(activityChangeIntent);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    public void switchUser(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("200")) {

                        // User not requested previously for instructor
                        if (response.getString("user_type").equals("2") || response.getString("user_type").equals("1")){

                            String phone_from_api = response.getString("phone");
                            String first_name_from_api = response.getString("first_name");
                            String middle_name_from_api = response.getString("middle_name");
                            String last_name_from_api = response.getString("last_name");
                            String user_id_from_api = response.getString("user_id");
                            String api_token_from_api = response.getString("api_token");
                            String user_email_from_api = response.getString("email");
                            String user_type_from_api = response.getString("user_type");

                            // Shared preference saving user data in xml
                            SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("phone", phone_from_api);
                            editor.putString("first_name", first_name_from_api);
                            editor.putString("middle_name", middle_name_from_api);
                            editor.putString("last_name", last_name_from_api);
                            editor.putString("user_id", user_id_from_api);
                            editor.putString("api_token", api_token_from_api);
                            editor.putString("email", user_email_from_api);
                            editor.putString("user_type", user_type_from_api);
                            editor.apply();
                        }

                    } else {

                        // Error
                        instructorView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                        // Error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    // Error
                    instructorView.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                // Error
                instructorView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }
        });
        rq.add(jsonObjectRequest);
    }

    // Version checker
    private void sendDeviceToken(String token, String user_phone){

        try {

            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            int current_app_version_code = pInfo.versionCode;

            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            assert wm != null;
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            String url = SEND_DEVICE_TOKEN + user_phone
                    + "?token=" + token
                    + "&current_version=" + current_app_version_code
                    +"&ip_address=" + ip;
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Initiating request for video courses
    private void getVideoCourses(){

        // Initializing array list
        listItemsVideo = new ArrayList<>();
        listItemsVideo.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String url;
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            url = Constants.GET_VIDEO_COURSES;

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
                            listItemsVideo.add(item);
                        }

                        adapter = new CourseAdapter(listItemsVideo, getApplicationContext());
                        recyclerViewVideoCourses.setLayoutManager(videoLayoutManager);
                        recyclerViewVideoCourses.setAdapter(adapter);
                        //  recyclerViewVideoCourses.setVisibility(View.VISIBLE);

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
    }// End request for video courses


    // Initiating request for quiz courses
    private void getQuizCourses(){
        // Initializing array list
        listItemsQuiz = new ArrayList<>();
        listItemsQuiz.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String url;
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            url = Constants.GET_QUIZ_COURSES;

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
                            listItemsQuiz.add(item);
                        }

                        adapter = new CourseAdapter(listItemsQuiz, getApplicationContext());
                        recyclerViewQuizCourses.setLayoutManager(quizLayoutManager);
                        recyclerViewQuizCourses.setAdapter(adapter);
//                        recyclerViewQuizCourses.setVisibility(View.VISIBLE);

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
    }// End request for quiz courses


}// End class
