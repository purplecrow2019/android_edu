package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.shubham.lamamia_project.InstructorQuestionDeleteActivity;
import com.example.shubham.lamamia_project.InstructorQuestionUpdateActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.InstructorQuestionList;
import java.util.ArrayList;

public class InstructorQuestionAdapter extends RecyclerView.Adapter<InstructorQuestionAdapter.ViewHolder> {

    private ArrayList<InstructorQuestionList> listItems;
    private Context context;

    public InstructorQuestionAdapter(ArrayList<InstructorQuestionList> courses_modelList, Context context) {
        this.listItems = courses_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instructor_question, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final InstructorQuestionList listItem = listItems.get(position);

        // Setting text
        holder.question.setText(listItem.getQuestion_text());

        // Click listener on update
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorQuestionUpdateActivity.class);
                activityChangeIntent.putExtra("question_id", listItem.getQuestion_id());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        // Click listener on delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorQuestionDeleteActivity.class);
                activityChangeIntent.putExtra("question_id", listItem.getQuestion_id());
                activityChangeIntent.putExtra("question_text", listItem.getQuestion_text());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView question;
        private ImageButton update, delete;

        public ViewHolder(View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.questionName);
            update = itemView.findViewById(R.id.questionUpdate);
            delete = itemView.findViewById(R.id.questionDelete);
        }
    }
}
