package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Objects;

public class InstructorDeleteFileActivity extends AppCompatActivity {

    private EditText fileId;
    private ConstraintLayout mainView;
    private LinearLayout processing, error, deleted;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_delete_file);

        // Casting
        fileId = findViewById(R.id.fileId);
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        deleted = findViewById(R.id.deleted);
        requestQueue = Volley.newRequestQueue(this);
        TextView fileName = findViewById(R.id.fileName);
        Button back = findViewById(R.id.back);
        Button delete = findViewById(R.id.delete);

        // Getting object values
        try{
            String file_name = Objects.requireNonNull(getIntent().getExtras()).getString("file_name");
            fileName.setText(file_name);
            String file_id = Objects.requireNonNull(getIntent().getExtras()).getString("file_id");
            fileId.setText(file_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // On back click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // On delete click
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fileId.getText().toString().trim().isEmpty()){
                    deleteVideoFile(fileId.getText().toString().trim());
                }else{
                    Toast toast_not_sent = Toast.makeText(getApplicationContext(), "We are having some problem. Try after some time...", Toast.LENGTH_SHORT);
                    toast_not_sent.show();
                }
            }
        });

    }// End onCreate


    private void deleteVideoFile(String file_id){
        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        deleted.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String DEFAULT = "null";
            String url = Constants.DELETE_VIDEO_ATTACHMENT_INSTRUCTOR + file_id
                    + "?instructor_id=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&api_token=" + sharedPreferences.getString("api_token", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        String stringStatus = jsonObject.getString("status");
                        int intStatus = Integer.parseInt(stringStatus);

                        if (intStatus == 200) {
                            // Making layout changes
                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            deleted.setVisibility(View.VISIBLE);

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },1000);

                        }else{

                            // Making layout changes
                            processing.setVisibility(View.GONE);
                            deleted.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Making layout changes
                        processing.setVisibility(View.GONE);
                        deleted.setVisibility(View.GONE);
                        mainView.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                    // Making layout changes
                    // Making layout changes
                    processing.setVisibility(View.GONE);
                    deleted.setVisibility(View.GONE);
                    mainView.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);

                }
            });
            requestQueue.add(stringRequest);
        }
    }// End method delete file

}
