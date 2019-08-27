package com.example.shubham.lamamia_project;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileUpdate extends AppCompatActivity {

    TextView fileName;
    private RequestQueue requestQueue;
    private EditText first_name, last_name, email;
    private LinearLayout error, processing;
    private ScrollView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        mainView = findViewById(R.id.mainView);

        first_name = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        Button update_profile = findViewById(R.id.updateProfileInstructor);
        requestQueue = Volley.newRequestQueue(this);


        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_name.getText().toString().trim().isEmpty()
                        || last_name.getText().toString().trim().isEmpty()
                        || email.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please fill out every field.", Toast.LENGTH_LONG).show();
                } else {
                    mainView.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    processing.setVisibility(View.VISIBLE);
                    updateProfile();
                }
            }
        });

        getProfileData();
    }

    // Update profile method
    private void updateProfile(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            if (sharedPreferences.getString("user_type", DEFAULT).equals("2")){

                Toast.makeText(getApplicationContext(),"You are not a valid user now.", Toast.LENGTH_LONG).show();

            }else{

                String url = Constants.UPDATE_PROFILE_USER + sharedPreferences.getString("user_id", DEFAULT);

                try {
                    StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                int intStatus = Integer.parseInt(status);
                                if (intStatus == 200){
                                    Toast.makeText(getApplicationContext(),
                                            "Profile Updated", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Profile Update failed", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //This code is executed if there is an error.
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> MyData = new HashMap<>();
                            MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                            MyData.put("user_id", sharedPreferences.getString("user_id", DEFAULT));
                            MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                            MyData.put("first_name", first_name.getText().toString().trim());
                            MyData.put("last_name", last_name.getText().toString().trim());
                            MyData.put("email", email.getText().toString().trim());
                            return MyData;
                        }
                    };

                    requestQueue.add(StringRequest);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        } else {
            mainView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }// End update profile

    // Getting profile data
    private void getProfileData(){

        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";

            String url = Constants.VIEW_USER_PROFILE + sharedPreferences.getString("user_id", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);

                        if(jsonObject.getString("status").equals("200")){

                            // Checking first name
                            if (jsonObject.getString("first_name").equals("null")){
                                first_name.setText("");
                            }else{
                                first_name.setText(jsonObject.getString("first_name"));
                            }

                            // Checking last name
                            if(jsonObject.getString("last_name").equals("null")){
                                last_name.setText("");
                            }else{
                                last_name.setText(jsonObject.getString("last_name"));
                            }

                            // Checking email
                            if(jsonObject.getString("email").equals("null")){
                                email.setText("");
                            }else{
                                email.setText(jsonObject.getString("email"));
                            }

                            // Layout visibility
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        } else {

                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
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
    }// End getting profile data
}
