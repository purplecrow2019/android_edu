package com.example.shubham.lamamia_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.shubham.lamamia_project.adapter.VideoChooseAdapter;
import com.example.shubham.lamamia_project.model.VideoChooseList;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import pub.devrel.easypermissions.EasyPermissions;

public class ChooseVideoListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVideos;
    private ArrayList<VideoChooseList> videoList;
    private EditText videoIdHolder;
    private static final int READ_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_video_list);

        videoIdHolder = findViewById(R.id.videoIdHolder);

        try{
            String video_id = Objects.requireNonNull(getIntent().getExtras()).getString("video_id");
            videoIdHolder.setText(video_id);
        }catch (Exception e) {
            e.printStackTrace();
        }
        recyclerViewVideos = findViewById(R.id.recyclerViewChooseVideo);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewVideos.setLayoutManager(mLayoutManager);
        videoList = new ArrayList<>();

        //check if app has permission to access the external storage.
        if (EasyPermissions.hasPermissions(ChooseVideoListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            fetchVideoData(videoIdHolder.getText().toString().trim());
        } else {
            //If permission is not present request for the same.
            EasyPermissions.requestPermissions(ChooseVideoListActivity.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

    }

    @SuppressLint("Recycle")
    private void fetchVideoData(String video_id){
        Cursor cursor;
        int thumbnail;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA
        };

        cursor = getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,null, null, null);
        assert cursor != null;
        thumbnail = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext())
        {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            File file = new File(path);

            VideoChooseList item = new VideoChooseList(
                    path,
                    file.getName(),
                    cursor.getString(thumbnail),
                    video_id
            );
            videoList.add(item);
        }

        VideoChooseAdapter adapter = new VideoChooseAdapter(videoList, getApplicationContext(), ChooseVideoListActivity.this);
        recyclerViewVideos.setAdapter(adapter);
    }
}
