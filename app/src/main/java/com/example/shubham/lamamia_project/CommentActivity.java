package com.example.shubham.lamamia_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.Utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private String video_id, video_comment_id, video_comment_text, DEFAULT = "null", status, message, video_commenter_name;
    private TextView txtCommentParent, txtCommenterName;
    private CircleImageView userImage;
    private EditText txtChildComment;
    private ImageButton imgSendChildComment;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rq = Volley.newRequestQueue(this);
        txtChildComment = findViewById(R.id.editTextSendChildComment);
        txtCommenterName = findViewById(R.id.textViewVideoParentCommenterName);
        txtCommentParent = findViewById(R.id.textViewVideoParentComment);
        imgSendChildComment = findViewById(R.id.imageButtonSendChildComment);
        userImage = findViewById(R.id.imageViewVideoParentCommenter);

        try{
            video_id = Objects.requireNonNull(getIntent().getExtras()).getString("videoID");
        }catch (Exception e) {
            video_id = "1";
        }

        try{
            video_comment_id = Objects.requireNonNull(getIntent().getExtras()).getString("videoCommentID");
        }catch (Exception e) {
            video_comment_id = "1";
        }

        try{
            video_comment_text = Objects.requireNonNull(getIntent().getExtras()).getString("videoCommentText");
        }catch (Exception e){
            video_comment_text = "We are having some problem";
        }

        try{
            video_commenter_name = Objects.requireNonNull(getIntent().getExtras()).getString("videoCommenterName");
        }catch(Exception e){
            video_commenter_name = "We are having some problem";
        }

        try{
            String userImageString = Objects.requireNonNull(getIntent().getExtras()).getString("userImage");
            Picasso.get().load(userImageString).fit().into(userImage);
        }catch(Exception e){
            video_commenter_name = "We are having some problem";
        }

        txtCommentParent.setText(video_comment_text);
        txtCommenterName.setText(video_commenter_name);
        imgSendChildComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChildComment();
            }
        });
    }

    public void sendChildComment(){

        final SharedPreferences sharedPreferences = getSharedPreferences("lamamia_user_data", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")
                && sharedPreferences.contains("api_token")) {
            final String user_type = sharedPreferences.getString("user_type", DEFAULT);

            if (txtChildComment.getText().toString().trim().isEmpty()){

                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "कृपया पहले एक टिप्पणी लिखें|");

            }else{

                // Needed user_id, api_token, text, video_id, parent_id;
                String url = Constants.POST_PARENT_COMMENT + video_id;

                StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(status);
                            if (intStatus == 200){

                                txtChildComment.setText("");

                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);

                                assert inputManager != null;
                                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);

                                Utility.showSnackBarSuccessShort(getApplicationContext(), findViewById(android.R.id.content), "आपकी टिप्पणी दर्ज कर दी गई है।");
                                finish();

                            } else {

                                // Making layout changes
                                Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            // Making layout changes
                            Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                        }

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //This code is executed if there is an error.
                        // Making layout changes
                        Utility.showSnackBarFailShort(getApplicationContext(), findViewById(android.R.id.content), "शायद आपका इंटरनेट बहुत धीमा है।");
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();
                        MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                        MyData.put("user_id", sharedPreferences.getString("user_id", DEFAULT));
                        MyData.put("api_token", sharedPreferences.getString("api_token", DEFAULT));
                        MyData.put("text", txtChildComment.getText().toString().trim());
                        MyData.put("parent_id", video_comment_id);
                        MyData.put("user_type", user_type);
                        return MyData;
                    }
                };
                rq.add(StringRequest);
            }
        }
    }// End method sendChildComment

}// End class
