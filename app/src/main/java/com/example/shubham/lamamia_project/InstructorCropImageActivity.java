package com.example.shubham.lamamia_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.MyHttpEntity;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class InstructorCropImageActivity extends AppCompatActivity implements
        CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnGetCroppedImageCompleteListener {

    /*Stating elements to be used in process*/
    private File myPath;
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 100;

    public static final String FIXED_ASPECT_RATIO = "extra_fixed_aspect_ratio";
    public static final String EXTRA_ASPECT_RATIO_X = "extra_aspect_ratio_x";
    public static final String EXTRA_ASPECT_RATIO_Y = "extra_aspect_ratio_y";

    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private CropImageView mCropImageView;
    private int mAspectRatioX = 100;
    private int mAspectRatioY = 100;
    private Bitmap croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_crop_image);

        // Buttons
        Button cancel = findViewById(R.id.cancel);
        Button upload = findViewById(R.id.upload);

        // cancel on click
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // upload on click
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropImageView.getCroppedImageAsync(mCropImageView.getCropShape(), 0, 0);
            }
        });

        boolean isFixedAspectRatio = getIntent().getBooleanExtra(FIXED_ASPECT_RATIO,true);
        mAspectRatioX=getIntent().getIntExtra(EXTRA_ASPECT_RATIO_X,
                DEFAULT_ASPECT_RATIO_VALUES);
        mAspectRatioY=getIntent().getIntExtra(EXTRA_ASPECT_RATIO_Y,
                DEFAULT_ASPECT_RATIO_VALUES);

        // Initialize components of the app
        mCropImageView = findViewById(R.id.UserCropImageView);
        // If you want to fix the aspect ratio, set it to 'true'
        mCropImageView.setFixedAspectRatio(isFixedAspectRatio);

        try{
            String image_url_string = Objects.requireNonNull(getIntent().getExtras()).getString("image_url");
            //Uri imageUri = Uri.parse(image_url_string);
            if (savedInstanceState == null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image_url_string, options);
                mCropImageView.setImageBitmap(bitmap);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnGetCroppedImageCompleteListener(null);
    }



    private void cropFailed() {
        Toast.makeText(mCropImageView.getContext(), "Image crop failed",
                Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@SuppressWarnings("NullableProblems")
                                               Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    @Override
    protected void onRestoreInstanceState(@SuppressWarnings("NullableProblems")
                                                  Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
            croppedImage = bitmap;
            try {
                String path = saveToInternalStorage(this, bitmap);
                if (myPath != null) {
                    InstructorCropImageActivity.UploadAsyncTask uploadAsyncTask = new InstructorCropImageActivity.UploadAsyncTask(InstructorCropImageActivity.this);
                    uploadAsyncTask.execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select a file first", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                cropFailed();
            }
        } else {
            cropFailed();
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(mCropImageView.getContext(), "Image loaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mCropImageView.getContext(), "Unable to load image", Toast.LENGTH_LONG).show();
            error.printStackTrace();
        }
    }

    private String saveToInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        Bitmap out = Bitmap.createScaledBitmap(bitmapImage, 200, 200, false);
        myPath = new File(directory, "image.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(myPath);
            out.compress(Bitmap.CompressFormat.PNG, 60, fOut);
            fOut.flush();
            fOut.close();
            out.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return directory.getAbsolutePath();
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
            final String DEFAULT = "null";
            if (sharedPreferences.contains("user_id")
                    && sharedPreferences.contains("api_token")) {

                final String SERVER_PATH = Constants.UPLOAD_PROFILE_IMAGE + sharedPreferences.getString("user_id", DEFAULT);

                try {
                    HttpPost httpPost = new HttpPost(SERVER_PATH);
                    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                    // Add the file to be uploaded
                    multipartEntityBuilder.addPart("file", new FileBody(myPath));

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
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress(progress[0]);
        }
    }
}
