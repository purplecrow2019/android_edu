package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import java.util.zip.GZIPInputStream;

public class LogoutActivity extends AppCompatActivity {

    private LinearLayoutCompat mainView, processing, error, success, optionHolder;
    private AppCompatButton yes, no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        // Lear layouts
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        success = findViewById(R.id.success);
        optionHolder = findViewById(R.id.optionHolder);

        // Buttons
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        // Yes on click
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // shared preference clear
                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Visibility manage
                mainView.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                success.setVisibility(View.GONE);
                optionHolder.setVisibility(View.GONE);
                processing.setVisibility(View.VISIBLE);

                // View success
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Visibility manage
                        mainView.setVisibility(View.GONE);
                        error.setVisibility(View.GONE);
                        optionHolder.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        success.setVisibility(View.VISIBLE);
                    }
                },1000);

                Handler handler_new = new Handler();
                handler_new.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Finishing activity
                        finish();
                    }
                },1000);
            }
        });

        // No on click
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finish();
            }
        });

    }// End on create

}// End class
