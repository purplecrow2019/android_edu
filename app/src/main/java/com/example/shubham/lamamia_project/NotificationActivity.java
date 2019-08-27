package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.NotificationAdapter;
import com.example.shubham.lamamia_project.model.NotificationList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private RequestQueue rq;
    private ArrayList<NotificationList> notificationLists = new ArrayList<>();
    private RecyclerView recyclerViewNotification;
    private NotificationAdapter adapter_notification;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout linearLayoutProgress, linearLayoutContent, linearLayoutNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        linearLayoutContent = findViewById(R.id.linearLayoutNotificationHolder);
        linearLayoutProgress = findViewById(R.id.linearLayoutNotificationProgressBarHolder);
        linearLayoutNotFound = findViewById(R.id.linearLayoutNotificationNotFoundHolder);

        // Element by me
        recyclerViewNotification = findViewById(R.id.recyclerViewNotification);
        rq = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        notificationLists = new ArrayList<>();
        makeCategoryList();
    }

    public void makeCategoryList() {

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String user_id = sharedPreferences.getString("user_id", "null");
            String api_token = sharedPreferences.getString("api_token", "null");
            String user_type = sharedPreferences.getString("user_type", "null");

            String url = Constants.GET_NOTIFICATIONS + user_id + "?user_type=" + user_type + "&api_token=" + api_token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")){
                            JSONArray array = jsonObject.getJSONArray("data");
                            if (array.length() > 0){
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    NotificationList item = new NotificationList(
                                            o.getString("notification_id"),
                                            o.getString("notification_text"),
                                            o.getString("for_id"),
                                            o.getString("for_user_type"),
                                            o.getString("of_id"),
                                            o.getString("of_user_type"),
                                            o.getString("video_comment_id"),
                                            o.getString("notification_status"),
                                            o.getString("video_id"),
                                            o.getString("notification_type"),
                                            o.getString("image")
                                    );
                                    notificationLists.add(item);
                                    adapter_notification = new NotificationAdapter(notificationLists, getApplicationContext());
                                    recyclerViewNotification.setLayoutManager(mLayoutManager);
                                    recyclerViewNotification.setAdapter(adapter_notification);
                                    linearLayoutProgress.setVisibility(View.GONE);
                                    linearLayoutContent.setVisibility(View.VISIBLE);
                                }
                            } else {
                                linearLayoutProgress.setVisibility(View.GONE);
                                linearLayoutNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            linearLayoutProgress.setVisibility(View.GONE);
                            linearLayoutNotFound.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        linearLayoutProgress.setVisibility(View.GONE);
                        linearLayoutNotFound.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    linearLayoutProgress.setVisibility(View.GONE);
                    linearLayoutNotFound.setVisibility(View.VISIBLE);
                }
            });
            rq.add(stringRequest);
        }
    }
}
