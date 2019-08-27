package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    //public Toolbar toolbar;
    ImageButton imageButtonName, imageButtonEmail, imageButtonPhone, imageButtonNameClose, imageButtonEmailClose, imageButtonPhoneClose;
    EditText txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhone;

    final String DEFAULT = "null";

    String user_id, user_first_name, user_last_name, user_middle_name, user_phone, user_email, status, message, first_name, middle_name, last_name;
    CardView cardViewNameChange, cardViewEmailChange, cardViewPhoneChange;
    TextView user_name_saved, user_phone_saved, user_email_saved;
    private RequestQueue rq;
    Button buttonNameChange, buttonEmailChange, buttonPhoneChange, buttonSwitch;
    private LinearLayout linearLayoutContentHolder, linearLayoutProgressHolder;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setDefaultDisplayHomeAsUpEnabled(true);

        rq = Volley.newRequestQueue(this);

        imageButtonName = findViewById(R.id.imageButtonSettingsActivityUserName);
        imageButtonNameClose = findViewById(R.id.imageButtonSettingsActivityUserNameClose);

        imageButtonEmail = findViewById(R.id.imageButtonSettingsActivityUserEmail);
        imageButtonEmailClose = findViewById(R.id.imageButtonSettingsActivityUserEmailClose);

        imageButtonPhone = findViewById(R.id.imageButtonSettingsActivityUserPhone);
        imageButtonPhoneClose = findViewById(R.id.imageButtonSettingsActivityUserPhoneClose);

        user_name_saved = findViewById(R.id.textViewSettingActivityUserName);
        user_email_saved = findViewById(R.id.textViewSettingActivityUserEmail);
        user_phone_saved = findViewById(R.id.textViewSettingActivityUserPhone);

        txtFirstName = findViewById(R.id.editTextSettingActivityFirstName);
        txtMiddleName = findViewById(R.id.editTextSettingsActivityMiddleName);
        txtLastName = findViewById(R.id.editTextSettingsActivityLastName);
        txtEmail = findViewById(R.id.editTextSettingsActivityEmail);
        txtPhone = findViewById(R.id.editTextSettingsActivityPhone);

        buttonNameChange = findViewById(R.id.btnNameChange);
        buttonEmailChange = findViewById(R.id.buttonChangeEmail);
        buttonPhoneChange = findViewById(R.id.buttonChangePhone);

        buttonSwitch = findViewById(R.id.buttonSwitchProfile);

        linearLayoutContentHolder = findViewById(R.id.linearLayoutSettingsHolder);
        linearLayoutProgressHolder = findViewById(R.id.linearLayoutSettingsProgressBarHolder);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", DEFAULT);
        user_first_name = sharedPreferences.getString("first_name", DEFAULT);
        user_middle_name = sharedPreferences.getString("middle_name",DEFAULT);
        user_last_name = sharedPreferences.getString("last_name", DEFAULT);
        user_email = sharedPreferences.getString("email", DEFAULT);
        user_phone = sharedPreferences.getString("phone", DEFAULT);

        if (sharedPreferences.getString("user_type", DEFAULT).equals("1")){
            String switch_as_instructor = "प्रभावक के रूप में स्विच करें";
            buttonSwitch.setText(switch_as_instructor);
        }else if(sharedPreferences.getString("user_type", DEFAULT).equals("2")){
            String switch_as_user = "छात्र के रूप में स्विच करें";
            buttonSwitch.setText(switch_as_user);
        }

        txtFirstName.setText(user_first_name);
        txtLastName.setText(user_last_name);

        if (user_middle_name.equals("null")){

            String name = user_first_name+" "+user_last_name;
            user_name_saved.setText(name);

        } else {

            String name = user_first_name+" "+user_middle_name+" "+user_last_name;
            txtMiddleName.setText(user_middle_name);
            user_name_saved.setText(name);

        }

        if (!user_email.equals(DEFAULT)){

            user_email_saved.setText(user_email);
            txtEmail.setText(user_email);

        }

        user_phone_saved.setText(user_phone);
        txtPhone.setText(user_phone);

        cardViewNameChange = findViewById(R.id.cardViewSettingActivityChangeName);
        cardViewEmailChange = findViewById(R.id.cardViewSettingActivityChangeEmail);
        cardViewPhoneChange = findViewById(R.id.cardViewSettingActivityChangePhone);

        cardViewNameChange.setVisibility(View.GONE);
        cardViewEmailChange.setVisibility(View.GONE);
        cardViewPhoneChange.setVisibility(View.GONE);


        // linearLayoutName = findViewById(R.id.linearLayoutSettingActivityName);

        imageButtonName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    changeName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        buttonNameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameConfirm();
            }
        });

        buttonEmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailConfirm();
            }
        });

        imageButtonNameClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewNameChange.setVisibility(View.GONE);
                imageButtonNameClose.setVisibility(View.GONE);
                imageButtonName.setVisibility(View.VISIBLE);
            }
        });

        imageButtonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail();
            }
        });

        imageButtonEmailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewEmailChange.setVisibility(View.GONE);
                imageButtonEmailClose.setVisibility(View.GONE);
                imageButtonEmail.setVisibility(View.VISIBLE);
            }
        });

        imageButtonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone();
            }
        });

        imageButtonPhoneClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewPhoneChange.setVisibility(View.GONE);
                imageButtonPhoneClose.setVisibility(View.GONE);
                imageButtonPhone.setVisibility(View.VISIBLE);
            }
        });


        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Making linear layout
                linearLayoutContentHolder.setVisibility(View.GONE);
                linearLayoutProgressHolder.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                if (sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("phone")
                        && sharedPreferences.contains("user_type")){

                    user_id = sharedPreferences.getString("user_id", DEFAULT);
                    user_phone = sharedPreferences.getString("phone", DEFAULT);

                    if (sharedPreferences.getString("user_type", DEFAULT).equals("1")){
                        String url = Constants.SWITCH_USER_TO_INSTRUCTOR + "?phone=" + user_phone +"";
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
    }

    // Method for changing name of current user
    public void changeName(){
        imageButtonName.setVisibility(View.GONE);
        imageButtonNameClose.setVisibility(View.VISIBLE);
        cardViewNameChange.setVisibility(View.VISIBLE);

        // Calling api for confirmation
    }

    public void changeNameConfirm(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String user_token = sharedPreferences.getString("api_token", DEFAULT);
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        String first_name_from_user = txtFirstName.getText().toString().trim();
        String middle_name_from_user = txtMiddleName.getText().toString().trim();
        String last_name_from_user = txtLastName.getText().toString().trim();

        String url = "http://lamamia.in/api/public/api/profile/change/name/final?user_id="+ user_id +"&api_token="+ user_token +"&first_name="+ first_name_from_user +"&last_name="+ last_name_from_user +"&middle_name="+ middle_name_from_user +"";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    status = response.getString("status");
                    message = response.getString("message");
                    first_name = response.getString("user_first_name");
                    middle_name = response.getString("user_middle_name");
                    last_name = response.getString("user_last_name");

                    int result_status = Integer.parseInt(status);

                    if (result_status == 200){

                        // Shared preference saving user data in xml
                        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("first_name", first_name);
                        editor.putString("middle_name", middle_name);
                        editor.putString("last_name", last_name);
                        editor.apply();

                        if (middle_name.equals("null")){

                            String name_set = first_name + " " + last_name;
                            user_name_saved.setText(name_set);

                        } else {

                            String name_set = first_name + " " + middle_name + " " + last_name;
                            user_name_saved.setText(name_set);

                        }

                        cardViewNameChange.setVisibility(View.GONE);
                        imageButtonNameClose.setVisibility(View.GONE);
                        imageButtonName.setVisibility(View.VISIBLE);

                        Toast toast_not_sent = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast_not_sent.show();

                    } else {

                        //Message on sms not sent
                        //String message_not_sent = "Please, Check your internet connection.";
                        Toast toast_not_sent = Toast.makeText(getApplicationContext(), "Error code:"+status+" , "+message, Toast.LENGTH_SHORT);
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

    public void changeEmail(){
        imageButtonEmail.setVisibility(View.GONE);
        imageButtonEmailClose.setVisibility(View.VISIBLE);
        cardViewEmailChange.setVisibility(View.VISIBLE);

    }

    public void changeEmailConfirm(){

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        String user_token = sharedPreferences.getString("api_token", DEFAULT);
        String user_id = sharedPreferences.getString("user_id", DEFAULT);
        String user_email = txtEmail.getText().toString().trim();

        String url = "http://lamamia.in/api/public/api/profile/change/email/final?user_id="+ user_id +"&api_token="+ user_token +"&email="+ user_email +"";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");
                    String email = response.getString("email");

                    int result_status = Integer.parseInt(status);

                    if (result_status == 200){

                        // Shared preference saving user data in xml
                        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.apply();

                        user_email_saved.setText(email);

                        cardViewEmailChange.setVisibility(View.GONE);
                        imageButtonEmailClose.setVisibility(View.GONE);
                        imageButtonEmail.setVisibility(View.VISIBLE);

                        Toast toast_not_sent = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast_not_sent.show();

                    } else {

                        //Message on sms not sent
                        //String message_not_sent = "Please, Check your internet connection.";
                        Toast toast_not_sent = Toast.makeText(getApplicationContext(), "Error code:"+status+" , "+message, Toast.LENGTH_SHORT);
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

    public void changePhone(){
        imageButtonPhone.setVisibility(View.GONE);
        imageButtonPhoneClose.setVisibility(View.VISIBLE);
        cardViewPhoneChange.setVisibility(View.VISIBLE);
    }

    public void switchUser(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    status = response.getString("status");
                    final String message_from_api = response.getString("message");
                    int result_status = Integer.parseInt(status);

                    if (result_status == 200) {

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

                        finish();

                    } else {

                        Toast toast_not_sent = Toast.makeText(getApplicationContext(), message_from_api, Toast.LENGTH_SHORT);
                        toast_not_sent.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast_not_sent = Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT);
                toast_not_sent.show();
            }
        });
        rq.add(jsonObjectRequest);

    }
}
