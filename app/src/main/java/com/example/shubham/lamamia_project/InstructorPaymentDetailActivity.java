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
import java.util.Objects;

public class InstructorPaymentDetailActivity extends AppCompatActivity {

    private TextView errorText;
    private EditText accountHolderName, accountNumber, ifscCode, bankName;
    private LinearLayout error, processing;
    private ScrollView mainView;
    private String course_id;
    private RequestQueue requestQueue;
    private Button saveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_payment_detail);

        mainView = findViewById(R.id.mainView);
        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);

        errorText = findViewById(R.id.errorText);

        accountHolderName = findViewById(R.id.accountHolderName);
        accountNumber = findViewById(R.id.accountNumber);
        ifscCode = findViewById(R.id.ifscCode);
        bankName = findViewById(R.id.bankName);

        requestQueue = Volley.newRequestQueue(this);

        // Saving information
        saveInfo = findViewById(R.id.addBankDetail);
        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBankDetail();
            }
        });

        // Getting bank account detail
        getAccountDetail();

        // Visibility of main view
        mainView.setVisibility(View.VISIBLE);
        processing.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }


    private void postBankDetail(){

        if (accountHolderName.getText().toString().isEmpty()
                || accountNumber.getText().toString().isEmpty()
                || ifscCode.getText().toString().isEmpty()
                || bankName.getText().toString().isEmpty()) {

            Toast.makeText(getApplicationContext(),"Please do not leave any field blank.", Toast.LENGTH_LONG).show();

        } else {

            // Making layout changes
            mainView.setVisibility(View.GONE);
            error.setVisibility(View.GONE);
            processing.setVisibility(View.VISIBLE);

            final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            final String DEFAULT = "null";
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")) {

                String url = Constants.POST_BANK_ACCOUNT_DETAIL;

                try {
                        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject jsonObject = new JSONObject(response);

                                        if (jsonObject.getString("message").equals("200")){

                                            // response
                                            mainView.setVisibility(View.VISIBLE);
                                            processing.setVisibility(View.GONE);
                                            error.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                            finish();

                                        } else {

                                            // error
                                            mainView.setVisibility(View.GONE);
                                            processing.setVisibility(View.GONE);
                                            error.setVisibility(View.VISIBLE);
                                            errorText.setText(jsonObject.getString("message"));

                                        }

                                    } catch (Exception e){
                                        // error
                                        mainView.setVisibility(View.GONE);
                                        processing.setVisibility(View.GONE);
                                        error.setVisibility(View.VISIBLE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError e) {
                                    // error
                                    mainView.setVisibility(View.GONE);
                                    processing.setVisibility(View.GONE);
                                    error.setVisibility(View.VISIBLE);
                                }
                            }
                        )
                        {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                // Sending data to server
                                Map<String, String>  params = new HashMap<>();
                                params.put("instructor", sharedPreferences.getString("user_id", DEFAULT));
                                params.put("token", sharedPreferences.getString("api_token", DEFAULT));
                                params.put("account_holder_name", accountHolderName.getText().toString().trim());
                                params.put("account_number", accountNumber.getText().toString().trim());
                                params.put("ifsc_code", ifscCode.getText().toString().trim());
                                params.put("bank_name", bankName.getText().toString().trim());
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
        }

    }// End method post



    private void getAccountDetail(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.GET_BANK_ACCOUNT_DETAIL
                    + "?instructor=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&token=" + sharedPreferences.getString("api_token", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        String stringStatus = jsonObject.getString("status");
                        int intStatus = Integer.parseInt(stringStatus);

                        if (intStatus == 200) {

                            accountHolderName.setText(jsonObject.getString("account_holder_name"));
                            accountNumber.setText(jsonObject.getString("account_number"));
                            ifscCode.setText(jsonObject.getString("ifsc_code"));
                            bankName.setText(jsonObject.getString("bank_name"));
                            saveInfo.setVisibility(View.GONE);

                        } else if(intStatus == 201) {
                            saveInfo.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                }
            });

            requestQueue.add(stringRequest);

        } else {
            // Making layout changes

        }
    }

}// End class
