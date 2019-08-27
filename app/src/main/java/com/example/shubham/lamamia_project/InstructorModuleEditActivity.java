package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructorModuleEditActivity extends AppCompatActivity {

    private EditText moduleIdHolder, moduleName;
    private RequestQueue requestQueue;
    private LinearLayout mainView, processing, error;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_edit);

        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        error_message = findViewById(R.id.errorMessage);

        Button updateModuleButton = findViewById(R.id.updateModuleButton);
        moduleIdHolder = findViewById(R.id.moduleIdHolder);
        moduleName = findViewById(R.id.moduleName);
        requestQueue = Volley.newRequestQueue(this);

        try {
            String module_id = Objects.requireNonNull(getIntent().getExtras()).getString("module_id");
            moduleIdHolder.setText(module_id);
            String module_name = Objects.requireNonNull(getIntent().getExtras()).getString("module_name");
            setTitle(module_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateModuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moduleName.getText().toString().trim().isEmpty()){
                    String error_text = "Please do not leave any field blank";
                    error_message.setText(error_text);
                    error_message.setVisibility(View.VISIBLE);
                }else{
                    updateModule();
                }
            }
        });

        getModule();
    }

    // Start course add request
    private void updateModule(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.UPDATE_MODULE_INSTRUCTOR + moduleIdHolder.getText().toString().trim();

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")){
                                finish();
                            } else {
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("module_name", moduleName.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e){
                e.printStackTrace();

                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {

            mainView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);

        }
    }// End course add request

    // Start course list request
    private void getModule(){

        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        String url = Constants.GET_MODULE_INSTRUCTOR + moduleIdHolder.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject  = new JSONObject(s);

                    if (jsonObject.getString("status").equals("200")){
                        moduleName.setText(jsonObject.getString("module_name"));
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        mainView.setVisibility(View.VISIBLE);
                    }else{
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mainView.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
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
    }// End course list request

}
