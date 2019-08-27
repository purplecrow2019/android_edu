package com.example.shubham.lamamia_project.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.UserQuestionList;
import java.util.ArrayList;

public class UserQuestionsAllAdapter extends RecyclerView.Adapter<UserQuestionsAllAdapter.ViewHolder>{

    private ArrayList<UserQuestionList> listItems;
    private Context context;
    private final Activity activity;
    private AdapterCallback callback;

    public interface AdapterCallback {
        void onItemClicked(int question_position, int answer_position);
        void onAnswerClicked(int answer_position);
    }

    public UserQuestionsAllAdapter(ArrayList<UserQuestionList> question_List, Context context, Activity activity, AdapterCallback callback) {
        this.listItems = question_List;
        this.context = context;
        this.activity = activity;
        this.callback = callback;
    }

    @NonNull
    @Override
    public UserQuestionsAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_questions_all, parent, false);
        return new UserQuestionsAllAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserQuestionsAllAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final UserQuestionList listItem = listItems.get(position);

        // Setting text to button
        holder.btnQuestionOrder.setText(listItem.getQuestion_order());

        // Changing background when not attended
        if (listItem.getQuestion_order().equals(listItem.getCurrent_question_order())){
            holder.btnQuestionOrder.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_blue));
            holder.btnQuestionOrder.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            if (listItem.getAttended().equals("1")){
                holder.btnQuestionOrder.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_green));
                holder.btnQuestionOrder.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.btnQuestionOrder.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_white));
                holder.btnQuestionOrder.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
        }// End changing background when not attended

        // Clicking on button & callback
        holder.btnQuestionOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItem.isClickeble()) {
                    // Making layout changes
                    if (callback != null) {
                        callback.onItemClicked(position, 1000);
                    }
                }
            }
        });// End clicking on button & callback
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button btnQuestionOrder;
        public ViewHolder(View itemView) {
            super(itemView);
            btnQuestionOrder = itemView.findViewById(R.id.txtQuestionOrder);
        }
    }
}
