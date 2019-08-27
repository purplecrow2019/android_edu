package com.example.shubham.lamamia_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.example.shubham.lamamia_project.Utils.MyHttpEntity;
import com.example.shubham.lamamia_project.adapter.FilesInstructorAdapter;
import com.example.shubham.lamamia_project.model.FileInstructorList;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InstructorAddVideoActivity extends AppCompatActivity {

    private TextView videoIdHolder;
    private File file;
    private EditText videoDescriptionText, videoNameText;
    private LinearLayout error, processing, hold, compression;
    private ScrollView mainView;
    private RequestQueue requestQueue;

    // Exo player initials
    private SimpleExoPlayer player;
    final String DEFAULT = "";
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private SimpleExoPlayerView mExoPlayerView;
    private ExtractorMediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private int mResumeWindow;
    private long mResumePosition;
    private EditText txtVideoUlrHolder;
    private RecyclerView fileHolderRecycler;
    private LinearLayoutManager mLayoutManager;
    private FilesInstructorAdapter adapter;

    private FFmpeg ffmpeg;
    private static final String TAG = "LAMAMIA_VALUE";
    private String outPutFilePath;
    private Uri uri;
    private String video_uri = null;
    private ProgressDialog progressDialog;

    // Video Compressor elements
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID = 2;
    private File file_old;
    private Context context = this;
    private int totalDur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        // Holders
        txtVideoUlrHolder = findViewById(R.id.txtVideoUlrHolder);
        videoIdHolder = findViewById(R.id.videoIdHolder);

        // views
        mainView = findViewById(R.id.mainView);
        processing = findViewById(R.id.processing);
        error = findViewById(R.id.error);
        hold = findViewById(R.id.hold);
        compression =  findViewById(R.id.compression);

        try {
            String video_id = Objects.requireNonNull(getIntent().getExtras()).getString("video_id");
            videoIdHolder.setText(video_id);
            video_uri = Objects.requireNonNull(getIntent().getExtras()).getString("video_uri");
            uri = Uri.parse(video_uri);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, uri);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            totalDur = Integer.parseInt(time)/1000;
            retriever.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);
        loadFFMpegBinary();

        // Request queue
        requestQueue = Volley.newRequestQueue(this);

        // Recycler
        fileHolderRecycler = findViewById(R.id.fileHolderRecycler);
        requestQueue = Volley.newRequestQueue(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Change video
        Button changeVideo = findViewById(R.id.changeVideo);
        changeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoIdHolder.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please select course and module first", Toast.LENGTH_LONG).show();
                } else {
                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_choose_upload_video_type);

                    // Choose from gallery
                    Button gallery = dialog.findViewById(R.id.gallery);
                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent activityChangeIntent = new Intent(getApplicationContext(), ChooseVideoListActivity.class);
                            activityChangeIntent.putExtra("video_id", videoIdHolder.getText().toString().trim());
                            activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(activityChangeIntent);
                            dialog.dismiss();
                            finish();
                        }
                    });

                    // Record video
                    Button record = dialog.findViewById(R.id.record);
                    record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent activityChangeIntent = new Intent(getApplicationContext(), RecordVideoActivity.class);
                            activityChangeIntent.putExtra("video_id", videoIdHolder.getText().toString().trim());
                            activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(activityChangeIntent);
                            dialog.dismiss();
                            finish();
                        }
                    });

                    dialog.show();
                }
            }
        }); // End change video

        // Name and description
        videoNameText = findViewById(R.id.videoNameText);
        videoDescriptionText = findViewById(R.id.videoDescriptionText);
        // update information
        Button updateVideoInformation = findViewById(R.id.updateVideoInformation);
        updateVideoInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoNameText.getText().toString().trim().isEmpty() ||
                        videoDescriptionText.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "please fill name and description", Toast.LENGTH_LONG).show();
                } else {
                    updateInformation();
                }
            }
        });

        Button compressVideoButton = findViewById(R.id.compress);
        compressVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });

        Button compressCancel = findViewById(R.id.cancelCompress);
        compressCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_uri != null){
                    file = new File(video_uri);
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(InstructorAddVideoActivity.this);
                    uploadAsyncTask.execute();
                }
            }
        });

        // Attach file
        Button attachFile = findViewById(R.id.attachFile);
        attachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAttachment();
            }
        });

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        if (video_uri != null){

            //create destination directory
            file_old = new File(video_uri);
            MediaMetadataRetriever retriever = new  MediaMetadataRetriever();
            Bitmap bmp;
            int videoHeight = 0;

            try {
                retriever.setDataSource(video_uri);
                bmp = retriever.getFrameAtTime();
                videoHeight=bmp.getHeight();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (videoHeight > 720){
                // show dialogue
                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                compression.setVisibility(View.VISIBLE);

            } else {
                file = new File(video_uri);
                UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(InstructorAddVideoActivity.this);
                uploadAsyncTask.execute();
            }

        } else {
            getVideoData();
        }
    }



    /**
     * Request Permission for writing to External Storage in 6.0 and up
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID);
        } else {
                // Want to compress a video
                File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Lamamia");
                if (f.mkdirs() || f.isDirectory())
                    //compress and output new video specs
                    executeCompressCommand();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Lamamia");
                    if (f.mkdirs() || f.isDirectory())
                        //compress and output new video specs
                        executeCompressCommand();
                } else {
                    Toast.makeText(this, "You need to enable the permission for External Storage Write" +
                            " to test out this library.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            }
            default:
        }
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
            String SERVER_PATH = Constants.UPLOAD_VIDEO_INSTRUCTOR + videoIdHolder.getText().toString().trim();

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
            // Reload activity here
            getVideoData();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress(progress[0]);
        }
    }

    // Update information method
    private void updateInformation(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {

            String url = Constants.UPDATE_VIDEO_INSTRUCTOR + videoIdHolder.getText().toString().trim();

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
                                compression.setVisibility(View.GONE);
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
                                compression.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            processing.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            compression.setVisibility(View.GONE);
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
                        compression.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("content_name", videoNameText.getText().toString().trim());
                        MyData.put("content_description", videoDescriptionText.getText().toString().trim());
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
                compression.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {

            // Making layout changes
            processing.setVisibility(View.GONE);
            mainView.setVisibility(View.GONE);
            hold.setVisibility(View.GONE);
            compression.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);

        }

    }// End update information method


    // Get all information
    private void getVideoData(){

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        hold.setVisibility(View.GONE);
        compression.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final ArrayList<FileInstructorList> listItems = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")
                && sharedPreferences.contains("user_type")) {
            String url = Constants.GET_VIDEO_INSTRUCTOR + videoIdHolder.getText().toString().trim();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {

                    try {
                        JSONObject jsonObject  = new JSONObject(s);
                        if(jsonObject.getString("status").equals("200")){

                            // Checking for video name
                            if(jsonObject.getString("video_name").equals("null")){
                                videoNameText.setText("");
                            }else{
                                videoNameText.setText(jsonObject.getString("video_name"));
                            }

                            // Checking for video description
                            if(jsonObject.getString("video_description").equals("null")){
                                videoDescriptionText.setText("");
                            }else{
                                videoDescriptionText.setText(jsonObject.getString("video_description"));
                            }

                            // Checking for video url
                            if (jsonObject.getString("video_url").equals("null")){
                                txtVideoUlrHolder.setText("");
                            }else{
                                txtVideoUlrHolder.setText(jsonObject.getString("video_url"));
                                playPlayer(jsonObject.getString("video_url"));
                            }

                            JSONArray array = jsonObject.getJSONArray("attachments");
                            if(array.length() > 0){
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject o = array.getJSONObject(i);
                                    FileInstructorList item = new FileInstructorList(
                                            o.getString("video_attachment_id"),
                                            o.getString("attachment_name"),
                                            o.getString("attachment_type")
                                    );
                                    listItems.add(item);
                                }
                            }

                            adapter = new FilesInstructorAdapter(listItems, getApplicationContext());
                            fileHolderRecycler.setLayoutManager(mLayoutManager);
                            fileHolderRecycler.setAdapter(adapter);

                            error.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            compression.setVisibility(View.GONE);
                            mainView.setVisibility(View.VISIBLE);
                        }else{
                            processing.setVisibility(View.GONE);
                            hold.setVisibility(View.GONE);
                            mainView.setVisibility(View.GONE);
                            compression.setVisibility(View.GONE);
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
                    compression.setVisibility(View.GONE);
                    mainView.setVisibility(View.VISIBLE);
                }
            });
            requestQueue.add(stringRequest);
        }
    }// Get all information




    //------------------------------------------------------------ Switching Video Go full/normal Screen ------------------------------------------------
    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        player.prepare(mVideoSource);
        player.setPlayWhenReady(true);

        long position_long = Long.parseLong("0");
        player.seekTo(position_long);
    }


    public void playPlayer(String getStreamUrl){

        String userAgent = Util.getUserAgent(InstructorAddVideoActivity.this, getApplicationContext().getApplicationInfo().packageName);
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(InstructorAddVideoActivity.this, null, httpDataSourceFactory);
        Uri daUri;

        if (getStreamUrl == null){
            getStreamUrl = txtVideoUlrHolder.getText().toString().trim();
            daUri = Uri.parse(getStreamUrl);
        } else {
            if (getStreamUrl.isEmpty()){
                getStreamUrl = txtVideoUlrHolder.getText().toString().trim();
                daUri = Uri.parse(getStreamUrl);
            } else {
                daUri = Uri.parse(getStreamUrl);
            }
        }

        mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(daUri);
        initExoPlayer();

    }// End method playPlayer


    @Override
    protected void onResume() {
        super.onResume();

        if (mExoPlayerView == null) {
            mExoPlayerView = findViewById(R.id.exoplayer);
        }

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.stop();
            player.seekTo(0);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        }
    }


    // add attachment method
    private void createAttachment() {

        // Making layout changes
        mainView.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        processing.setVisibility(View.VISIBLE);

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        final String DEFAULT = "null";
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            String url = Constants.CREATE_VIDEO_ATTACHMENT_INSTRUCTOR;

            try {
                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);

                            if (intStatus == 200) {
                                Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorFileUpdateActivity.class);
                                activityChangeIntent.putExtra("file_id", jsonObject.getString("video_attachment_id"));
                                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(activityChangeIntent);
                            } else {

                                // Making layout changes
                                mainView.setVisibility(View.GONE);
                                processing.setVisibility(View.GONE);
                                compression.setVisibility(View.GONE);
                                error.setVisibility(View.VISIBLE);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            // Making layout changes
                            mainView.setVisibility(View.GONE);
                            processing.setVisibility(View.GONE);
                            compression.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);

                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        mainView.setVisibility(View.GONE);
                        processing.setVisibility(View.GONE);
                        compression.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("instructor_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("video_id", videoIdHolder.getText().toString().trim());
                        return MyData;
                    }
                };

                requestQueue.add(StringRequest);
            } catch (Exception e) {
                e.printStackTrace();

                // Making layout changes
                mainView.setVisibility(View.GONE);
                processing.setVisibility(View.GONE);
                compression.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

        } else {
            // Making layout changes
            mainView.setVisibility(View.GONE);
            processing.setVisibility(View.GONE);
            compression.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }// End video add method



    private void executeCompressCommand(){
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );

        String filePrefix = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String fileExtn = ".mp4";
        String yourRealPath = uri.getPath();
        File file = new File(moviesDir +"/Lamamia/Compressed");
        if (file.mkdirs() || file.isDirectory()){
            File dest = new File(moviesDir +"/Lamamia/Compressed", filePrefix + fileExtn);
            int fileNo = 0;
            while (dest.exists()) {
                fileNo++;
                dest = new File(moviesDir +"/Lamamia/Compressed", filePrefix + fileNo + fileExtn);
            }
            Log.d(TAG, "startTrim: src: " + yourRealPath);
            Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
            outPutFilePath = dest.getAbsolutePath();
            String[] complexCommand = {"-y", "-i", yourRealPath, "-s", "1280x720", "-r", "30", "-vcodec", "libx264", "-b:v", "64k", "-b:a", "48000", "-ac", "2", "-crf", "28", "-ar", "48000", outPutFilePath};
            execFFmpegBinary(complexCommand);
        }
    }

    /**
     * Executing ffmpeg binary
     */
    private void execFFmpegBinary(final String[] command) {
        try {

            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "SUCCESS with output : " + s);
                    file = new File(outPutFilePath);
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(InstructorAddVideoActivity.this);
                    uploadAsyncTask.execute();
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);

                    float showProgress = 0;
                    Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
                    Scanner sc = new Scanner(s);
                    String match = sc.findWithinHorizon(timePattern, 0);
                    if (match != null) {
                        String[] matchSplit = match.split(":");
                        if (totalDur != 0) {
                            float progress = (Integer.parseInt(matchSplit[0]) * 3600 +
                                    Integer.parseInt(matchSplit[1]) * 60 +
                                    Float.parseFloat(matchSplit[2])) / totalDur;
                            showProgress = (progress * 100);
                            Log.d(TAG, "=======PROGRESS======== " + showProgress);
                        }
                    }
                    progressDialog.setMessage("Compressing video " + new DecimalFormat("##.##").format(showProgress) +" %");
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                        progressDialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(InstructorAddVideoActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InstructorAddVideoActivity.this.finish();
                    }
                })
                .create()
                .show();
    }

}// End class
