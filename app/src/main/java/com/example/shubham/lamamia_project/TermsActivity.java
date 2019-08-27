package com.example.shubham.lamamia_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        WebView wbView = (WebView) findViewById(R.id.webViewTerms);
        wbView.getSettings().setJavaScriptEnabled(true);
        wbView.loadUrl("https://lamamia.in/api/public/terms");
    }
}
