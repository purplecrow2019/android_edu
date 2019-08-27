package com.example.shubham.lamamia_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpConfirmActivity extends Activity {

    Button otpButton, btnConfirmInstructor, btnConfirmAsUser, btnConfirmAsInstructor;
    EditText otpTxt, txtPhone;
    RequestQueue rq;
    String message, status, phone_from_api, first_name_from_api, middle_name_from_api, last_name_from_api, user_id_from_api, message_from_api, api_token_from_api, user_email_from_api;
    private SmsVerifyCatcher smsVerifyCatcher;
    LinearLayout linearLayoutOtpConfirmContentHolder, getLinearLayoutOtpConfirmProgressBarHolder, linearLayoutOtpConfirmChoiceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_otp_confirm);

        otpButton = findViewById(R.id.btnOtpConfirm);
        otpTxt = findViewById(R.id.editTxtOtpConfirmation);
        txtPhone = findViewById(R.id.editTextOtpConfirmPhone);
        btnConfirmInstructor = findViewById(R.id.btnOtpConfirmInstructor);
        btnConfirmAsUser = findViewById(R.id.btnOtpConfirmAsUser);
        btnConfirmAsInstructor = findViewById(R.id.btnOtpConfirmAsInstructor);
        linearLayoutOtpConfirmContentHolder = findViewById(R.id.linearLayoutOtpConfirmContentHolder);
        getLinearLayoutOtpConfirmProgressBarHolder = findViewById(R.id.linearLayoutOtpConfirmProgressBar);
        linearLayoutOtpConfirmChoiceHolder = findViewById(R.id.linearLayoutOtpConfirmChoice);

        rq = Volley.newRequestQueue(this);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        assert bundle != null;
        final String phone = bundle.getString("phoneNumber");
        final String user_type = bundle.getString("user_type");

        txtPhone.setText(phone);

        assert user_type != null;

        if (user_type.equals("1")){

            linearLayoutOtpConfirmChoiceHolder.setVisibility(View.GONE);
            btnConfirmInstructor.setVisibility(View.GONE);

        } else if (user_type.equals("2")){

            otpButton.setVisibility(View.GONE);
            linearLayoutOtpConfirmChoiceHolder.setVisibility(View.GONE);

        } else if (user_type.equals("3")){

            otpButton.setVisibility(View.GONE);
            btnConfirmInstructor.setVisibility(View.GONE);

        }

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                otpTxt.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPhone.getText().toString().trim().isEmpty() || otpTxt.getText().toString().trim().isEmpty()){
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, ओटीपी दर्ज करें।");
                } else {
                    linearLayoutOtpConfirmContentHolder.setVisibility(View.GONE);
                    getLinearLayoutOtpConfirmProgressBarHolder.setVisibility(View.VISIBLE);
                    sendJsonRequest("1");
                }
            }
        });

        btnConfirmInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPhone.getText().toString().trim().isEmpty() || otpTxt.getText().toString().trim().isEmpty()){
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, ओटीपी दर्ज करें।");
                } else {
                    linearLayoutOtpConfirmContentHolder.setVisibility(View.GONE);
                    getLinearLayoutOtpConfirmProgressBarHolder.setVisibility(View.VISIBLE);
                    sendJsonRequest("2");
                }
            }
        });

        btnConfirmAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPhone.getText().toString().trim().isEmpty() || otpTxt.getText().toString().trim().isEmpty()){
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, ओटीपी दर्ज करें।");
                } else {
                    linearLayoutOtpConfirmContentHolder.setVisibility(View.GONE);
                    getLinearLayoutOtpConfirmProgressBarHolder.setVisibility(View.VISIBLE);
                    sendJsonRequest("1");
                }
            }
        });

        btnConfirmAsInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPhone.getText().toString().trim().isEmpty() || otpTxt.getText().toString().trim().isEmpty()){
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, ओटीपी दर्ज करें।");
                } else {

                    if (txtPhone.getText().toString().trim().length() == 10
                            && otpTxt.getText().toString().trim().length() == 6){
                        linearLayoutOtpConfirmContentHolder.setVisibility(View.GONE);
                        getLinearLayoutOtpConfirmProgressBarHolder.setVisibility(View.VISIBLE);
                        sendJsonRequest("2");
                    } else {
                        Toast.makeText(getApplicationContext(), "कृपया, ओटीपी दर्ज करें।", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void sendJsonRequest(final String user_type){

            String url =  Constants.LOGIN_USER_CONFIRM + "?phone_number=" + txtPhone.getText().toString().trim() +
                    "&otp=" + otpTxt.getText().toString().trim() + "&user_type=" + user_type + "";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {

                        status = response.getString("status");
                        int result_status = Integer.parseInt(status);

                        if (result_status == 200) {

                            phone_from_api = response.getString("phone");
                            first_name_from_api = response.getString("first_name");
                            middle_name_from_api = response.getString("middle_name");
                            last_name_from_api = response.getString("last_name");
                            user_id_from_api = response.getString("user_id");
                            api_token_from_api = response.getString("api_token");
                            user_email_from_api = response.getString("email");
                            message_from_api = response.getString("message");

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
                            editor.putString("user_type", user_type);
                            editor.apply();

                            Intent activityChangeIntent = new Intent(OtpConfirmActivity.this, MainActivity.class);
                            OtpConfirmActivity.this.startActivity(activityChangeIntent);
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
