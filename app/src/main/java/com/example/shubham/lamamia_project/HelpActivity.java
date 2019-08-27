package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

public class HelpActivity extends AppCompatActivity {

    EditText editTextTopic, editTextMessage;
    Button buttonHelpSend;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        editTextTopic = findViewById(R.id.editTextHelpTopic);
        editTextMessage = findViewById(R.id.editTextHelpMessage);

        buttonHelpSend = findViewById(R.id.buttonHelpSend);

        rq = Volley.newRequestQueue(this);

        buttonHelpSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHelpData();
            }
        });
    }


    public void sendHelpData(){

        if (editTextMessage.getText().toString().trim().isEmpty() && editTextTopic.getText().toString().trim().isEmpty()){

            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "");

        }else{

            SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")) {
                String DEFAULT = null;
                String user_id = sharedPreferences.getString("user_id", DEFAULT);
                String api_token = sharedPreferences.getString("api_token", DEFAULT);

                    // Needed user_id, api_token, topic, message;
                    String url = Constants.SEND_HELP_MESSAGE + "?user_id="+ user_id +"&api_token="
                            + api_token +"&topic="+ editTextTopic.getText().toString().trim() +
                            "&message="+ editTextMessage.getText().toString().trim() +"";

                    url = url.replaceAll(" ", "%20");

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String status = response.getString("status");
                                String message = response.getString("message");

                                int result_status = Integer.parseInt(status);

                                if (result_status == 200){

                                    finish();
                                    Utility.showSnackBarSuccessShort(getApplicationContext(), findViewById(android.R.id.content), "आपकी टिप्पणी दर्ज कर दी गई है।");

                                } else {

                                    Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");

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

        }

    }
}
