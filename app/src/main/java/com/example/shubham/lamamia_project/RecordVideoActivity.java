package com.example.shubham.lamamia_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RecordVideoActivity extends AppCompatActivity {

    private static final int TYPE_VIDEO = 2;
    private static final int TYPE_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID = 2;
    public static final String LOG_TAG = ChooseVideoListActivity.class.getSimpleName();
    public static final String FILE_PROVIDER_AUTHORITY = "com.lamamia.android.lamamia.provider";
    private String mCurrentPhotoPath;
    private Uri capturedUri = null;
    private static final int REQUEST_TAKE_VIDEO = 200;
    private EditText videoIdHolder;
    private TextView phoneStorage,captureVideo;
    private LinearLayout videoListHolder, recordVideoHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        videoIdHolder = findViewById(R.id.videoIdHolder);

        try{
            String video_id = Objects.requireNonNull(getIntent().getExtras()).getString("video_id");
            videoIdHolder.setText(video_id);
        }catch (Exception e) {
            e.printStackTrace();
        }

        // Floating action button for capturing video
        Button recordVideo = findViewById(R.id.captureVideo);
        recordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(TYPE_VIDEO);
            }
        });
    }

    /**
     * Request Permission for writing to External Storage in 6.0 and up
     */
    private void requestPermissions(int mediaType) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (mediaType != TYPE_IMAGE) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID);
            }
        } else {
            if (mediaType == TYPE_VIDEO) {
                // Want to compress a video
                dispatchTakeVideoIntent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE_VID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakeVideoIntent();
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

    private File createMediaFile(int type) throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = (type == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                type == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
        File file = File.createTempFile(
                fileName,  /* prefix */
                type == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        // Get the path of the file created
        mCurrentPhotoPath = file.getAbsolutePath();
        Log.d(LOG_TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return file;
    }

    // Intent for capturing video
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                capturedUri = FileProvider.getUriForFile(this,FILE_PROVIDER_AUTHORITY,createMediaFile(TYPE_VIDEO));
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedUri);
                Log.d(LOG_TAG, "VideoUri: " + capturedUri.toString());
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method which will process the captured image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                //create destination directory
                File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Lamamia");
                if (f.mkdirs() || f.isDirectory()) {
                    Intent activityChangeIntent = new Intent(getApplicationContext(), InstructorVideoEditorActivity.class);
                    activityChangeIntent.putExtra("url", mCurrentPhotoPath);
                    activityChangeIntent.putExtra("video_id", videoIdHolder.getText().toString().trim());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activityChangeIntent);
                    finish();
                } else {
                    Toast.makeText(this, "We are unable to process your request.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
