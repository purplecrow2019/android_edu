package com.example.shubham.lamamia_project;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FFMpegService extends Service {

    private FFmpeg fFmpeg;
    private int duration;

    String[] command;
    Callbacks activity;

    public MutableLiveData<Integer> percentage;
    IBinder myBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (intent.getAction() != null) {
            duration = Integer.parseInt(intent.getStringExtra("duration"));
            command = intent.getStringArrayExtra("command");
            try{
                loadFFMpegBinary();
                execFFMpegCommand();
            } catch (Exception e){
                e.printStackTrace();
            }
            return START_STICKY;
//        } else {
//            String source = null == intent ? "intent" : "action";
//            Log.e ("TAGGING", source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString (flags));
//            return START_STICKY;
//        }
    }

    private void execFFMpegCommand() throws FFmpegCommandAlreadyRunningException {
        fFmpeg.execute(command, new ExecuteBinaryResponseHandler(){
            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
            }

            @Override
            public void onProgress(String message) {
                super.onProgress(message);
                String arr[];
                if (message.contains("time=")){
                    arr = message.split("time=");
                    String sm = arr[1];

                    String tm[] = sm.split(":");
                    String[] um = tm[2].split(" ");
                    String seconds = um[0];

                    int hours = Integer.parseInt(tm[0]);
                    hours = hours * 3600;
                    int min = Integer.parseInt(tm[1]);
                    min = min *60;
                    float sec = Float.valueOf(seconds);

                    float timeInSec = hours + min + sec;
                    percentage.setValue((int) ((timeInSec/duration)*100));
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            loadFFMpegBinary();
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
        percentage = new MutableLiveData<>();
    }

    private void loadFFMpegBinary() throws FFmpegNotSupportedException
    {
        if (fFmpeg == null){
            fFmpeg = FFmpeg.getInstance(this);
        }

        fFmpeg.loadBinary(new LoadBinaryResponseHandler()
        {
            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onSuccess() {
                super.onSuccess();
            }
        });
    }

    public FFMpegService(){
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class LocalBinder extends Binder{

        public FFMpegService getServiceInstance(){
            return FFMpegService.this;
        }
    }

    public void registerClient(Activity activity){
        this.activity = (Callbacks)activity;
    }

    public MutableLiveData<Integer> getPercentage(){
        return percentage;
    }

    public interface Callbacks
    {
        void updateClient(float data);
    }
}
