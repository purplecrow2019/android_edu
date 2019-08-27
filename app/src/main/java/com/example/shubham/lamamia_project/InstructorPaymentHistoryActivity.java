package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.adapter.InstructorPaymentHistoryAdapter;
import com.example.shubham.lamamia_project.model.PaymentHistoryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class InstructorPaymentHistoryActivity extends AppCompatActivity {

    private LinearLayout error, processing, mainView;
    private RequestQueue requestQueue;
    private ArrayList<PaymentHistoryList> listItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private InstructorPaymentHistoryAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private String course_id;
    private TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_payment_history);

        // Try catch for quiz id
        try {
            course_id = Objects.requireNonNull(getIntent().getExtras()).getString("course_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        error = findViewById(R.id.error);
        processing = findViewById(R.id.processing);
        mainView = findViewById(R.id.mainView);

        // Elements
        errorText = findViewById(R.id.errorText);
        recyclerView = findViewById(R.id.paymentHistory);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // getting all payment history
        getAllCoursePaymentHistory();

    }

    private void getAllCoursePaymentHistory()
    {
        error.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        listItems = new ArrayList<>();
        listItems.clear();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String url;
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {

            url = Constants.GET_PAYMENT_DETAILS_BY_COURSE + course_id
                    +"?instructor="+sharedPreferences.getString("user_id",null)
                    +"&token="+sharedPreferences.getString("api_token",null);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);

                        if (jsonObject.getString("status").equals("200")){

                            JSONArray array = jsonObject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++){
                                JSONObject o = array.getJSONObject(i);
                                PaymentHistoryList item = new PaymentHistoryList(
                                        o.getString("instructor_payment_id"),
                                        " Amount in INR: " + o.getString("payment_price"),
                                        "Payment Stage: "+o.getString("payment_order"),
                                        o.getString("status")
                                );
                                listItems.add(item);
                            }

                            adapter = new InstructorPaymentHistoryAdapter(listItems, getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(adapter);

                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        } else {

                            processing.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                            errorText.setText(jsonObject.getString("message"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        processing.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    error.setVisibility(View.VISIBLE);
                    processing.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            });

            requestQueue.add(stringRequest);
        }
    }

}
