package com.example.shubham.lamamia_project;

import android.app.Activity;
import android.content.Intent;
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
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    RequestQueue rq;
    EditText phoneNumberField;
    Button loginButton;
    String message, status, phone;
    LinearLayout linearLayoutLoginContentHolder, linearLayoutLoginProgressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button) ;
        phoneNumberField = findViewById(R.id.phoneNumber);

        linearLayoutLoginContentHolder = findViewById(R.id.linearLayoutLoginContentHolder);
        linearLayoutLoginProgressBarHolder = findViewById(R.id.linearLayoutLoginProgressBar);

        rq = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberField.getText().toString().trim().isEmpty()) {

                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया, फ़ोन नंबर दर्ज करें।");

                } else {

                    if (phoneNumberField.getText().toString().trim().length() == 10){
                        linearLayoutLoginContentHolder.setVisibility(View.GONE);
                        linearLayoutLoginProgressBarHolder.setVisibility(View.VISIBLE);
                        sendJsonRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), "कृपया, फ़ोन नंबर दर्ज करें।", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

    }

    public void sendJsonRequest(){

        phone = phoneNumberField.getText().toString().trim();
        String url = Constants.LOGIN_USER + "?phone_number="+phone;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    status = response.getString("status");

                    int user_status = Integer.parseInt(response.getString("user_status"));

                    int result_status = Integer.parseInt(status);

                    //Checking for status
                    if (result_status == 200){

                        if (user_status == 1) {

                            Intent activityChangeIntent = new Intent(LoginActivity.this, OtpConfirmActivity.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();

                            //Add your data to bundle
                            bundle.putString("phoneNumber", phone);

                            //Adding user type to bundle
                            bundle.putString("user_type", "1");

                            //Add the bundle to the intent
                            activityChangeIntent.putExtras(bundle);

                            //Starting new activity
                            LoginActivity.this.startActivity(activityChangeIntent);

                            // Finishing activity
                            finish();

                        } else if( user_status == 2){

                            Intent activityChangeIntent = new Intent(LoginActivity.this, OtpActivity.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();

                            //Adding phone number your data to bundle
                            bundle.putString("phoneNumber", phone);

                            // Adding user type to bundle
                            bundle.putString("user_type", "1");

                            //Add the bundle to the intent
                            activityChangeIntent.putExtras(bundle);

                            //Starting new activity
                            LoginActivity.this.startActivity(activityChangeIntent);

                            // finishing activity
                            finish();

                        } else if(user_status == 3) {

                            Intent activityChangeIntent = new Intent(LoginActivity.this, OtpConfirmActivity.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();

                            //Add your data to bundle
                            bundle.putString("phoneNumber", phone);

                            //Adding user type to bundle
                            bundle.putString("user_type", "2");

                            //Add the bundle to the intent
                            activityChangeIntent.putExtras(bundle);

                            //Starting new activity
                            LoginActivity.this.startActivity(activityChangeIntent);

                            // Finishing activity
                            finish();

                        } else if(user_status == 4) {

                            Intent activityChangeIntent = new Intent(LoginActivity.this, OtpConfirmActivity.class);

                            //Create the bundle
                            Bundle bundle = new Bundle();

                            //Add your data to bundle
                            bundle.putString("phoneNumber", phone);

                            //Adding user type to bundle
                            bundle.putString("user_type", "3");

                            //Add the bundle to the intent
                            activityChangeIntent.putExtras(bundle);

                            //Starting new activity
                            LoginActivity.this.startActivity(activityChangeIntent);

                            // Finishing activity
                            finish();

                        } else {

                            linearLayoutLoginProgressBarHolder.setVisibility(View.GONE);
                            linearLayoutLoginContentHolder.setVisibility(View.VISIBLE);

                            //When not got response
                            String message = "Please, Check your internet connection.";
                            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                            toast.show();

                        }

                    } else {

                        //When not got response
                        String message = "Please, Check your internet connection.";
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast.show();

                        linearLayoutLoginProgressBarHolder.setVisibility(View.GONE);
                        linearLayoutLoginContentHolder.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    linearLayoutLoginProgressBarHolder.setVisibility(View.GONE);
                    linearLayoutLoginContentHolder.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                linearLayoutLoginProgressBarHolder.setVisibility(View.GONE);
                linearLayoutLoginContentHolder.setVisibility(View.VISIBLE);
            }
        });

        rq.add(jsonObjectRequest);
    }

}
