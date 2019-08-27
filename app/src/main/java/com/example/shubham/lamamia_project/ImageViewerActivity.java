package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import java.util.Objects;

public class ImageViewerActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        String image_url = null;
        WebView imageView = findViewById(R.id.imageView);

        // Try catch for quiz id
        try {
            image_url = Objects.requireNonNull(getIntent().getExtras()).getString("image_url");
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.getSettings().setBuiltInZoomControls(true);
        imageView.getSettings().setJavaScriptEnabled(true);
        imageView.loadUrl(image_url);
        imageView.setVisibility(View.VISIBLE);
    }
}
