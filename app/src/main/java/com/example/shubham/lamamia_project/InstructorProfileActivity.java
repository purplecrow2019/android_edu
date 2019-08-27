package com.example.shubham.lamamia_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class InstructorProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private LinearLayout processing, error;
    private ScrollView mainView;
    private TextView name, designation, about, email, phone;
    public CircleImageView image;
    private RequestQueue requestQueue;
    private LinearLayout emailHolder, phoneHolder, aboutHolder;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_FILE_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);

        emailHolder = findViewById(R.id.emailHolder);
        phoneHolder = findViewById(R.id.phoneHolder);
        aboutHolder = findViewById(R.id.aboutHolder);

        requestQueue = Volley.newRequestQueue(this);

        name = findViewById(R.id.name);
        designation = findViewById(R.id.designation);
        about = findViewById(R.id.about);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        image = findViewById(R.id.image);
        ImageButton back = findViewById(R.id.back);

        FloatingActionButton setImage = findViewById(R.id.setImage);
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if app has permission to access the external storage.
                if (EasyPermissions.hasPermissions(InstructorProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showFileChooserIntent();
                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(InstructorProfileActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityChangeIntent = new Intent(InstructorProfileActivity.this, ProfileEditActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                InstructorProfileActivity.this.startActivity(activityChangeIntent);
            }
        });


        // Back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        getProfileData();
    }

    // Getting profile data
    private void getProfileData(){

        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";

            String url = Constants.VIEW_INSTRUCTOR_PROFILE + sharedPreferences.getString("user_id", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);

                        if(jsonObject.getString("status").equals("200")){
                            String full_name = jsonObject.getString("first_name")+" "+jsonObject.getString("last_name");

                            name.setText(full_name);
                            Picasso.get().load(jsonObject.getString("image")).fit().into(image);

                            if (jsonObject.getString("email").equals("null")){
                                emailHolder.setVisibility(View.GONE);
                            }else{
                                email.setText(jsonObject.getString("email"));
                                emailHolder.setVisibility(View.VISIBLE);
                            }

                            if (jsonObject.getString("phone").equals("null")){
                                phoneHolder.setVisibility(View.GONE);
                            }else{
                                phone.setText(jsonObject.getString("phone"));
                                phoneHolder.setVisibility(View.VISIBLE);
                            }

                            if (jsonObject.getString("about").equals("null")){
                                aboutHolder.setVisibility(View.GONE);
                            }else{
                                about.setText(jsonObject.getString("about"));
                                aboutHolder.setVisibility(View.VISIBLE);
                            }
                            if (!jsonObject.getString("designation").equals("null")){
                                designation.setText(jsonObject.getString("designation"));
                            }
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);

                        } else {

                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
        }
    }// End getting profile data


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){
            if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
                Uri fileUri = data.getData();
                previewFile(fileUri);
            }
        }
    }


    private void previewFile(Uri uri) {
        String filePath = getRealPathFromURIPath(uri);

        Intent activityChangeIntent = new Intent(InstructorProfileActivity.this, InstructorCropImageActivity.class);
        activityChangeIntent.putExtra("image_url", filePath);
        activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        InstructorProfileActivity.this.startActivity(activityChangeIntent);
    }

    /**
     * Shows an intent which has options from which user can choose the file like File manager, Gallery etc
     */
    private void showFileChooserIntent() {
        Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Choose any file
        fileManagerIntent.setType("image/*");
        startActivityForResult(fileManagerIntent, REQUEST_FILE_CODE);

    }

    /**
     * Returns the actual path of the file in the file system
     *
     */
    private String getRealPathFromURIPath(Uri contentURI) {
        String path = null;
        try{
            Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            assert cursor != null;
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    "Please choose a valid image from your phone storage.", Toast.LENGTH_LONG).show();
        }

        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, InstructorProfileActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        showFileChooserIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

}
