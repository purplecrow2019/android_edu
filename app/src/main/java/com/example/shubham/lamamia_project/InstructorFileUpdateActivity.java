package com.example.shubham.lamamia_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.shubham.lamamia_project.Utils.FilePath;
import com.example.shubham.lamamia_project.Utils.MyHttpEntity;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

public class InstructorFileUpdateActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private EditText fileId, fileUrlHolder, fileName;
    private static final int REQUEST_FILE_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private File file;
    private RequestQueue requestQueue;
    private TextView fileNameInternal;
    private Button fileChoose, fileView, fileUpload, back, save;
    private LinearLayout error, processing, hold;
    private ScrollView mainView;

    // Getting progress
    private static final String TAG = "AndroidUploadService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_file_update);

        // Casting elements
        fileId = findViewById(R.id.fileId);
        fileNameInternal = findViewById(R.id.fileNameInternal);
        fileView = findViewById(R.id.fileView);
        fileChoose = findViewById(R.id.fileChoose);
        fileUpload = findViewById(R.id.fileUpload);
        fileName = findViewById(R.id.fileName);
        fileUrlHolder = findViewById(R.id.fileUrlHolder);

        // Request queue
        requestQueue = Volley.newRequestQueue(this);

        // views
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        hold = findViewById(R.id.hold);

        try {
            String file_id = Objects.requireNonNull(getIntent().getExtras()).getString("file_id");
            fileId.setText(file_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUrlHolder.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "File not valid", Toast.LENGTH_LONG).show();
                }else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/gview?embedded=true&url=" + fileUrlHolder.getText().toString().trim()));
                    startActivity(browserIntent);
                }
            }
        });

        fileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if app has permission to access the external storage.
                if (EasyPermissions.hasPermissions(InstructorFileUpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showFileChooserIntent();
                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(InstructorFileUpdateActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        fileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileId.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Unable to generate file", Toast.LENGTH_LONG).show();
                }else {
                    InstructorFileUpdateActivity.UploadAsyncTask uploadAsyncTask = new InstructorFileUpdateActivity.UploadAsyncTask(InstructorFileUpdateActivity.this);
                    uploadAsyncTask.execute();
                }
            }
        });

        // On back button click
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // On save button click
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileName.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "please fill name", Toast.LENGTH_LONG).show();
                }else{
                    updateFileName();
                }
            }
        });

    }// End onCreate


    @Override
    protected void onStart(){
        super.onStart();

        if (fileId.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "Unable to fetch attachment data.", Toast.LENGTH_LONG).show();
        }else{
            getAttachmentData();
        }
    }

    private void getAttachmentData(){
        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        hold.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String DEFAULT = "null";
            // Url String
            String url = Constants.GET_VIDEO_ATTACHMENT_INSTRUCTOR + fileId.getText().toString().trim()
                    + "?instructor_id=" + sharedPreferences.getString("user_id", DEFAULT)
                    + "&api_token=" + sharedPreferences.getString("api_token", DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        if(jsonObject.getString("status").equals("200")){

                            // Checking for video name
                            if(jsonObject.getString("attachment_name").equals("null")){
                                fileName.setText("");
                            }else{
                                fileName.setText(jsonObject.getString("attachment_name"));
                            }

                            if (jsonObject.getString("attachment_url").equals("null")){
                                fileUrlHolder.setText("");
                                fileView.setVisibility(View.GONE);
                                String attach = "Attach file";
                                fileChoose.setText(attach);
                            }else{
                                fileUrlHolder.setText(jsonObject.getString("attachment_url"));
                                fileView.setVisibility(View.VISIBLE);
                                String update = "Update File";
                                fileChoose.setText(update);
                            }

                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }else{
                            processing.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    error.setVisibility(View.GONE);
                    processing.setVisibility(View.GONE);
                    hold.setVisibility(View.GONE);
                    mainView.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }
    }//  End get attachment data

    // File choosing -------------------------------------------------------------------------------
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

        try{
            String filePath = FilePath.getPath(this, uri);
            assert filePath != null;
            file = new File(filePath);
            if(file.getName().contains(".pdf")){
                fileNameInternal.setText(file.getName());
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
            }else if(file.getName().contains(".xlsx")){
                fileNameInternal.setText(file.getName());
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
            }else if(file.getName().contains(".xlt")){
                fileNameInternal.setText(file.getName());
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
            }else if(file.getName().contains(".xltm")){
                fileNameInternal.setText(file.getName());
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);

            }else if(file.getName().contains(".xltx")){
                fileNameInternal.setText(file.getName());
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);

            }else{
                String file_not_found = "File not found";
                fileNameInternal.setText(file_not_found);
                // Layout changes
                fileView.setVisibility(View.GONE);
                fileChoose.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                fileUpload.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    "We are unable to locate the file. Please move your file in internal storage of your device.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Shows an intent which has options from which user can choose the file like File manager, Gallery etc
     */
    private void showFileChooserIntent() {

        String[] mimeTypes = {"application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(intent, REQUEST_FILE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, InstructorFileUpdateActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        showFileChooserIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Background network task to handle file upload.
     */
    @SuppressLint("StaticFieldLeak")
    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;
        private ProgressDialog progressDialog;

        private UploadAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            org.apache.http.HttpResponse httpResponse;
            HttpEntity httpEntity;
            String responseString = null;

            final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")) {

                final String SERVER_PATH = Constants.UPLOAD_VIDEO_ATTACHMENT_INSTRUCTOR + fileId.getText().toString().trim();

                try {
                    HttpPost httpPost = new HttpPost(SERVER_PATH);
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                    // Add the file to be uploaded
                    multipartEntityBuilder.addPart("file", new FileBody(file));

                    // Progress listener - updates task's progress
                    MyHttpEntity.ProgressListener progressListener =
                            new MyHttpEntity.ProgressListener() {
                                @Override
                                public void transferred(float progress) {
                                    publishProgress((int) progress);
                                }
                            };

                    // POST
                    httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                            progressListener));

                    httpResponse = httpClient.execute(httpPost);
                    httpEntity = httpResponse.getEntity();

                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(httpEntity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                } catch (UnsupportedEncodingException | ClientProtocolException e) {
                    e.printStackTrace();
                    Log.e("UPLOAD", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return responseString;
        }

        @Override
        protected void onPreExecute() {

            // Init and show dialog
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            // Close dialog
            this.progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    result, Toast.LENGTH_LONG).show();

            // Layout changes
            fileView.setVisibility(View.GONE);
            fileChoose.setVisibility(View.GONE);
            fileUpload.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress(progress[0]);
        }
    }










    // --------------------- Update name of file ---------------------------------------------------
    private void updateFileName(){
        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.UPDATE_VIDEO_ATTACHMENT_INSTRUCTOR + fileId.getText().toString().trim();

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200) {

                                // Making layout changes
                                error.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                hold.setVisibility(View.VISIBLE);

                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },1000);

                            } else {

                                // Making layout changes
                                processing.setVisibility(View.GONE);
                                mainView.setVisibility(View.GONE);
                                hold.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        processing.setVisibility(View.GONE);
                        mainView.setVisibility(View.GONE);
                        hold.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("file_name", fileName.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                // Making layout changes
                processing.setVisibility(View.GONE);
                mainView.setVisibility(View.GONE);
                hold.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {

            // Making layout changes
            processing.setVisibility(View.GONE);
            mainView.setVisibility(View.GONE);
            hold.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);

        }
    }// End update file name

}// End class