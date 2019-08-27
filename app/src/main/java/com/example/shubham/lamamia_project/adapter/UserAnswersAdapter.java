package com.example.shubham.lamamia_project.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.model.UserAnswersList;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;

public class UserAnswersAdapter extends RecyclerView.Adapter<UserAnswersAdapter.ViewHolder> {

    private ArrayList<UserAnswersList> listItems;
    private Context context;
    private final Activity activity;
    private Boolean clickeble = true;
    private UserQuestionsAllAdapter.AdapterCallback callback;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L, startTime = 0L;

    public UserAnswersAdapter(ArrayList<UserAnswersList> question_List, Context context, Activity activity, UserQuestionsAllAdapter.AdapterCallback callback) {
        this.listItems = question_List;
        this.context = context;
        this.activity = activity;
        this.callback = callback;
    }

    @NonNull
    @Override
    public UserAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_answers, parent, false);
        return new UserAnswersAdapter.ViewHolder(v);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull final UserAnswersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final UserAnswersList listItem = listItems.get(position);

        // Starting milliseconds
        startTime = SystemClock.uptimeMillis();
        Runnable millisecondsUpTimer = new Runnable() {
            @Override
            public void run() {
                timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
                String milliString = Long.toString(timeInMilliseconds);
                holder.upTimeCounter.setText(milliString);
                customHandler.postDelayed(this, 0);
            }
        };
        customHandler.postDelayed(millisecondsUpTimer, 0);

        // Setting text to button
        holder.answerText.setText(listItem.getAnswer_text());

        // Changing background when not attended
        if (listItem.getCorrected().equals("1")){
            holder.cardViewUserAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.lightestBlue));
        }// End changing background when not attended

        // Clicking on button & callback
        holder.currentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String url = Constants.POST_USER_ANSWER + listItem.getQuiz_id();
                    try {
                        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                                // Making layout changes
                                                if (callback != null) {
                                                    callback.onAnswerClicked(position);
                                                }
                                            } catch (Exception e) {
                                                // error
                                                Toast.makeText(context, "Error: Please try again", Toast.LENGTH_LONG).show();

                                                // Making layout changes
                                                if (callback != null) {
                                                    callback.onAnswerClicked(position);
                                                }
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError e) {
                                            // error
                                            Toast.makeText(context, "Error: Please try again", Toast.LENGTH_LONG).show();

                                            // Making layout changes
                                            if (callback != null) {
                                                callback.onAnswerClicked(position);
                                            }
                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() {
                                    // Sending data to server
                                    Map<String, String> params = new HashMap<>();
                                    params.put("user_id", listItem.getUser_id());
                                    params.put("api_token", listItem.getApi_token());
                                    params.put("answer_specifier", listItem.getAnswer_specifier());
                                    params.put("question_order", listItem.getQuestion_order());
                                    params.put("time_taken", holder.upTimeCounter.getText().toString().trim());
                                    return params;
                                }
                            };

                            holder.rq.add(putRequest);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error: Please try again", Toast.LENGTH_LONG).show();
                            // Making layout changes
                            if (callback != null) {
                                callback.onAnswerClicked(position);
                            }
                        }
                    }// End method addQuiz

        });// End clicking on button & callback


        if (listItem.getAnswer_image().equals("null") || listItem.getAnswer_image().isEmpty()){
            holder.answerImage.setVisibility(View.GONE);
        } else {
            holder.answerImage.getSettings().setBuiltInZoomControls(true);
            holder.answerImage.getSettings().setJavaScriptEnabled(true);
            holder.answerImage.loadUrl(listItem.getAnswer_image());
            holder.answerImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout currentLinearLayout;
        private TextView answerText;
        private RequestQueue rq;
        private TextView upTimeCounter;
        private WebView answerImage;
        private CardView cardViewUserAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            currentLinearLayout = itemView.findViewById(R.id.linearLayoutCurrent);
            answerText = itemView.findViewById(R.id.answerText);
            upTimeCounter = itemView.findViewById(R.id.answerUpTimer);
            answerImage = itemView.findViewById(R.id.answerImage);
            cardViewUserAnswer = itemView.findViewById(R.id.cardViewUserAnswer);
            rq = Volley.newRequestQueue(context);
        }
    }
}
