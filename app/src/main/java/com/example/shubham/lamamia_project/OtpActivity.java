package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class OtpActivity extends AppCompatActivity {

    Button otpButton;
    EditText otpTxt, CustomerFirstName, CustomerLastName;
    RequestQueue rq;
    String message, status, otp_txt, customerFirstName, customerLastName;
    LinearLayout linearLayoutOtpContentHolder, linearLayoutOtpProgressBarHolder;

    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        rq = Volley.newRequestQueue(this);

        otpButton = findViewById(R.id.btnConfirm);
        otpTxt = findViewById(R.id.editTxtOtp);
        CustomerFirstName = findViewById(R.id.editTxtCustomerFirstName);
        CustomerLastName = findViewById(R.id.editTxtCustomerLastName);
        linearLayoutOtpContentHolder = findViewById(R.id.linearLayoutOtpMainContainer);
        linearLayoutOtpProgressBarHolder = findViewById(R.id.linearLayoutOtpProgressBar);

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
                if (CustomerFirstName.getText().toString().trim().isEmpty() || CustomerLastName.getText().toString().trim().isEmpty()){
                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, सुनिश्चित करें कि आपका पहला नाम और अंतिम नाम खाली नहीं है।");
                }else{

                    if (otpTxt.getText().toString().trim().length() == 6){
                        linearLayoutOtpContentHolder.setVisibility(View.GONE);
                        linearLayoutOtpProgressBarHolder.setVisibility(View.VISIBLE);
                        sendJsonRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), "कृपया, ओटीपी दर्ज करें।", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }// End on create


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

    public void sendJsonRequest(){

        otp_txt = otpTxt.getText().toString().trim();
        customerFirstName = CustomerFirstName.getText().toString().trim();
        customerLastName = CustomerLastName.getText().toString().trim();

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        assert bundle != null;
        final String phone = bundle.getString("phoneNumber");

        String url = Constants.LOGIN_USER_CONFIRM + "?phone_number="+phone+"&otp="+otp_txt+"&first_name="+customerFirstName+"&last_name="+customerLastName+"&user_type=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    String phone_from_api, first_name_from_api, last_name_from_api, user_id_from_api, api_token_from_api;

                    phone_from_api = response.getString("phone");
                    first_name_from_api = response.getString("first_name");
                    last_name_from_api = response.getString("last_name");
                    user_id_from_api = response.getString("user_id");
                    api_token_from_api = response.getString("api_token");
                    status = response.getString("status");

                    int result_status = Integer.parseInt(status);

                    if (result_status == 200){

                        //Message on sms sent
                        String message_sent = "Welcome to Lamamia";
                        Toast toast_sent = Toast.makeText(getApplicationContext(), message_sent, Toast.LENGTH_LONG);
                        toast_sent.show();

                        // Shared preference saving user data in xml
                        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("phone", phone_from_api);
                        editor.putString("first_name", first_name_from_api);
                        editor.putString("last_name", last_name_from_api);
                        editor.putString("api_token", api_token_from_api);
                        editor.putString("user_id", user_id_from_api);
                        editor.putString("user_type", "1");
                        editor.apply();

                        //Changing activity
                        Intent activityChangeIntent = new Intent(OtpActivity.this, MainActivity.class);
                        OtpActivity.this.startActivity(activityChangeIntent);
                        finish();

                    } else {

                        //Message on sms not sent
                        String message_not_sent = "Please, Check your OTP.";
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
