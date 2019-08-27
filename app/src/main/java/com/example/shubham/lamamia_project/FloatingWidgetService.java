package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class FloatingWidgetService extends Service {

    private WindowManager mWindowManager;
    private View mOverlayView;
    int mWidth;
    CircleImageView counterFab, screenPausePlay, screenStop, screenMarker;
    boolean activity_background;
    private Callback callback;
    private final IBinder mBinder = new LocalBinder();

    public interface Callback{
        void screenOption(int position);
    }

    //returns the instance of the service
    public class LocalBinder extends Binder {
        public FloatingWidgetService getServiceInstance(){
            return FloatingWidgetService.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @SuppressLint({"ClickableViewAccessibility", "RtlHardcoded", "InflateParams"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false);
        }

        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.bubble_button_record, null);
            final WindowManager.LayoutParams params;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            //Specify the view position
            params.gravity = Gravity.TOP | Gravity.LEFT;
            //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 100;

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            assert mWindowManager != null;
            mWindowManager.addView(mOverlayView, params);

            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            // Elements declaration
            counterFab = mOverlayView.findViewById(R.id.fabHead);
            screenMarker = mOverlayView.findViewById(R.id.screenMarker);
            screenPausePlay = mOverlayView.findViewById(R.id.screenPausePlay);
            screenStop = mOverlayView.findViewById(R.id.screenStop);

            final ConstraintLayout layout = mOverlayView.findViewById(R.id.layout);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = layout.getMeasuredWidth();

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width;
                }
            });

            counterFab.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            //remember the initial position.
                            initialX = params.x;
                            initialY = params.y;
                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();

                            return true;
                        case MotionEvent.ACTION_UP:

                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            if (activity_background) {
                                float xDiff = event.getRawX() - initialTouchX;
                                float yDiff = event.getRawY() - initialTouchY;
                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
                                    if (screenMarker.getVisibility() == View.VISIBLE
                                            && screenPausePlay.getVisibility() == View.VISIBLE
                                            && screenStop.getVisibility() == View.VISIBLE){
                                        screenStop.setVisibility(View.GONE);
                                        screenPausePlay.setVisibility(View.GONE);
                                        screenMarker.setVisibility(View.GONE);
                                    } else {
                                        screenStop.setVisibility(View.VISIBLE);
                                        screenPausePlay.setVisibility(View.VISIBLE);
                                        screenMarker.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            int middle = mWidth / 2;
                            float nearestXWall = params.x >= middle ? mWidth : 0;
                            params.x = (int) nearestXWall;
                            mWindowManager.updateViewLayout(mOverlayView, params);

                            return true;
                        case MotionEvent.ACTION_MOVE:

                            int xDiff = Math.round(event.getRawX() - initialTouchX);
                            int yDiff = Math.round(event.getRawY() - initialTouchY);
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff;
                            params.y = initialY + yDiff;
                            //Update the layout with new X & Y coordinates
                            mWindowManager.updateViewLayout(mOverlayView, params);

                            return true;
                    }
                    return true;
                }
            });

            screenStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null) {
                        callback.screenOption(3);
                    }
                    Toast.makeText(getApplicationContext(),"STOP Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            screenPausePlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null) {
                        callback.screenOption(1);
                    }
                    Toast.makeText(getApplicationContext(),"pause/play Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            screenMarker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.screenOption(2);
                }
            });

        } else {
            //counterFab.increase();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //Here Activity register to the service as Callbacks client
    public void registerClient(Activity activity){
        this.callback = (Callback)activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }
}
