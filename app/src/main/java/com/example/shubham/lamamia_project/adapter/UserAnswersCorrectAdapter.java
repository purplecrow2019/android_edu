package com.example.shubham.lamamia_project.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.shubham.lamamia_project.model.UserAnswersCorrectList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAnswersCorrectAdapter extends RecyclerView.Adapter<UserAnswersCorrectAdapter.ViewHolder> {

    private ArrayList<UserAnswersCorrectList> listItems;
    private Context context;
    private final Activity activity;

    public UserAnswersCorrectAdapter(ArrayList<UserAnswersCorrectList> question_List, Context context, Activity activity) {
        this.listItems = question_List;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserAnswersCorrectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_answers, parent, false);
        return new UserAnswersCorrectAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAnswersCorrectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final UserAnswersCorrectList listItem = listItems.get(position);

        // Setting text to button
        holder.answerText.setText(listItem.getAnswer_text());

        // Changing background when not attended
        if (listItem.getCorrected().equals("1")){
            holder.cardViewUserAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.lightestBlue));
        }

        if (listItem.getIs_correct().equals("1")){
            holder.answerText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_circle_green,0);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView answerText;
        private CardView cardViewUserAnswer;
        public ViewHolder(View itemView) {
            super(itemView);
            answerText = itemView.findViewById(R.id.answerText);
            cardViewUserAnswer = itemView.findViewById(R.id.cardViewUserAnswer);
        }
    }
}
