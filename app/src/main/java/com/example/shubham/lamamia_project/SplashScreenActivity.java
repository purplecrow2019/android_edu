package com.example.shubham.lamamia_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import static com.example.shubham.lamamia_project.Utils.Constants.VERSION_CHECKER;

public class SplashScreenActivity extends Activity {

    final String DEFAULT = "null";
    Handler handler;
    protected int time;
    private RequestQueue rq;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.splashScreenProgressBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
        rq = Volley.newRequestQueue(this);
        time = 4000;

        handler=new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                isNetworkAvailable();
                versionUpdater();


                SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);

                // Checking if it contains all required fields
                if (sharedPreferences.contains("phone")
                        && sharedPreferences.contains("first_name")
                        && sharedPreferences.contains("last_name")
                        && sharedPreferences.contains("user_id")
                        && sharedPreferences.contains("api_token")
                        && sharedPreferences.contains("user_type")){

                    final String phone = sharedPreferences.getString("phone", DEFAULT);
                    String first_name = sharedPreferences.getString("first_name", DEFAULT);
                    String last_name = sharedPreferences.getString("last_name", DEFAULT);
                    final String user_id = sharedPreferences.getString("user_id", DEFAULT);
                    String api_token = sharedPreferences.getString("api_token", DEFAULT);
                    String user_type = sharedPreferences.getString("user_type",DEFAULT);

                    if (phone.equals(DEFAULT) || first_name.equals(DEFAULT) || last_name.equals(DEFAULT)
                            || user_id.equals(DEFAULT) || api_token.equals(DEFAULT) || user_type.equals(DEFAULT)) {

                        Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
                        startActivity(intent);

                    } else {

                        // Identifying user type
                        if (user_type.equals("1")){
                            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else if(user_type.equals("2")){
                            Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }
                } else {

                    Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(intent);

                }

                finish();
            }
        },time);
    }

    // Network checker
    private void isNetworkAvailable() {

        try{

            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (!isConnected){

                Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "Internet Not Working!!!");
                time = 100000000;

            }

        }catch (Exception e){

            Utility.showSnackBar(getApplicationContext(), findViewById(android.R.id.content), "We are having some problem!!!");
            time = 100000000;

        }
    }

    // Version checker
    private void versionUpdater(){

        String url = VERSION_CHECKER;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    String status = response.getString("status");
                    int result_status = Integer.parseInt(status);

                    //Checking for status
                    if (result_status == 200){

                        try {
                            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                            int current_app_version_code = pInfo.versionCode;
                            String new_version_code = response.getString("version_code");
                            int newVersionCode = Integer.parseInt(new_version_code);

                            if (current_app_version_code < newVersionCode){
                                time = 100000000;
                                Intent intent=new Intent(SplashScreenActivity.this, UpdateApplicationActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Version update available", Toast.LENGTH_SHORT).show();

                            }else{
                                time = 2000;
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {

                        //When not got response
                        String message = "Please, Check your internet connection.";
                        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                        toast.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonObjectRequest);
    }

}
