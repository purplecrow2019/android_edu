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

public class InstructorModuleDeleteActivity extends AppCompatActivity {
    private EditText moduleIdHolder;
    private ConstraintLayout mainView;
    private LinearLayout processing, error, deleted;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_delete);
        // Casting elements
        moduleIdHolder = findViewById(R.id.moduleId);
        TextView moduleName = findViewById(R.id.moduleName);
        Button back = findViewById(R.id.back);
        Button delete = findViewById(R.id.delete);

        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        deleted = findViewById(R.id.deleted);

        requestQueue = Volley.newRequestQueue(this);

        try{
            String module_name = Objects.requireNonNull(getIntent().getExtras()).getString("module_name");
            moduleName.setText(module_name);
            String module_id = Objects.requireNonNull(getIntent().getExtras()).getString("module_id");
            moduleIdHolder.setText(module_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // delete operation
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteModule(moduleIdHolder.getText().toString().trim());
            }
        });

        // back operation
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }// End on create

    // Delete operation method
    public void deleteModule(String module_id) {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        deleted.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.DELETE_MODULE_INSTRUCTOR + module_id;

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
    }//End delete operation method
}
