package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import org.florescu.android.rangeseekbar.RangeSeekBar;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import static com.example.shubham.lamamia_project.Utils.FilePath.getDataColumn;
import static com.example.shubham.lamamia_project.Utils.FilePath.isDownloadsDocument;
import static com.example.shubham.lamamia_project.Utils.FilePath.isExternalStorageDocument;
import static com.example.shubham.lamamia_project.Utils.FilePath.isMediaDocument;

public class InstructorVideoEditorActivity extends AppCompatActivity {

    private Uri uri;
    private VideoView videoView;
    private boolean isPlaying = false;
    private static final String TAG = "LAMAMIA_VALUE";
    private String filePath;
    private FFmpeg ffmpeg;
    private int choice = 0;
    private static final String POSITION = "position";
    private static final String FILEPATH = "filepath";
    private RangeSeekBar rangeSeekBar;
    private TextView tvLeft, tvRight, videoIdHolder;
    private ProgressDialog progressDialog;
    private int duration;
    private Runnable r;
    private int totalDur;
    private String vidPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editor);

        rangeSeekBar = findViewById(R.id.rangeSeekBar);
        Button saveVideo = findViewById(R.id.editorSaveVideo);
        Button skipVideo = findViewById(R.id.editorSkipVideo);
        videoIdHolder = findViewById(R.id.videoIdHolder);

        videoView = findViewById(R.id.videoView);

        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);

        ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);
        loadFFMpegBinary();

        Intent i = getIntent();
        if (i != null){
            vidPath = i.getStringExtra("url");
            uri = Uri.parse(vidPath);
            videoIdHolder.setText(i.getStringExtra("video_id"));
            isPlaying = true;
            videoView.setVideoURI(uri);
            videoView.start();
        }


        // Save Video
        saveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                if (uri != null) {
                    executeCutVideoCommand(rangeSeekBar.getSelectedMinValue().intValue() * 1000, rangeSeekBar.getSelectedMaxValue().intValue() * 1000);
                }
            }
        });

        // Skip Video
        skipVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null){
                    Intent intent = new Intent(InstructorVideoEditorActivity.this, InstructorAddVideoActivity.class);
                    intent.putExtra("video_uri", vidPath);
                    intent.putExtra("video_id", videoIdHolder.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                duration = mp.getDuration() / 1000;
                tvLeft.setText("00:00:00");
                tvRight.setText(getTime(mp.getDuration() / 1000));
                mp.setLooping(true);
                rangeSeekBar.setRangeValues(0, duration);
                rangeSeekBar.setSelectedMinValue(0);
                rangeSeekBar.setSelectedMaxValue(duration);
                rangeSeekBar.setEnabled(true);

                rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        videoView.seekTo((int) minValue * 1000);
                        tvLeft.setText(getTime((int) bar.getSelectedMinValue()));
                        tvRight.setText(getTime((int) bar.getSelectedMaxValue()));
                    }
                });

                final Handler handler = new Handler();
                handler.postDelayed(r = new Runnable() {
                    @Override
                    public void run() {

                        if (videoView.getCurrentPosition() >= rangeSeekBar.getSelectedMaxValue().intValue() * 1000)
                            videoView.seekTo(rangeSeekBar.getSelectedMinValue().intValue() * 1000);
                        handler.postDelayed(r, 1000);
                    }
                }, 1000);
            }
        });
    }// End onCreate

    @SuppressLint("DefaultLocale")
    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
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
        new AlertDialog.Builder(InstructorVideoEditorActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InstructorVideoEditorActivity.this.finish();
                    }
                })
                .create()
                .show();

    }

    private void executeCutVideoCommand(int startMs, int endMs) {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );

        String filePrefix = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String fileExtn = ".mp4";
        String yourRealPath = uri.getPath();
        File file = new File(moviesDir +"/Lamamia/Trim");
        if (file.mkdirs() || file.isDirectory()){
            File dest = new File(moviesDir +"/Lamamia/Trim", filePrefix + fileExtn);
            int fileNo = 0;
            while (dest.exists()) {
                fileNo++;
                dest = new File(moviesDir +"/Lamamia/Trim", filePrefix + fileNo + fileExtn);
            }
            Log.d(TAG, "startTrim: src: " + yourRealPath);
            Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
            Log.d(TAG, "startTrim: startMs: " + startMs);
            Log.d(TAG, "startTrim: endMs: " + endMs);
            totalDur = (endMs - startMs)/1000;
            filePath = dest.getAbsolutePath();
            String[] complexCommand = {"-ss", "" + startMs / 1000, "-y", "-i", yourRealPath, "-t", "" + (endMs - startMs) / 1000,"-vcodec", "mpeg4", "-b:v", "2097152", "-b:a", "48000", "-ac", "2", "-ar", "22050", filePath};
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
                    Intent intent = new Intent(InstructorVideoEditorActivity.this, InstructorAddVideoActivity.class);
                    intent.putExtra("video_uri", filePath);
                    intent.putExtra("video_id",videoIdHolder.getText().toString().trim());
                    startActivity(intent);
                    finish();
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
                    progressDialog.setMessage("Trimming " + new DecimalFormat("##.##").format(showProgress) +" %");
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
                    if (choice != 8 && choice != 9 && choice != 10) {
                        progressDialog.dismiss();
                    }
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }
}// End class
