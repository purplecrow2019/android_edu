package com.example.shubham.lamamia_project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubham.lamamia_project.InstructorQuestionUpdateActivity;
import com.example.shubham.lamamia_project.InstructorVideoInsightsActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.VideoInsightsList;

import java.util.ArrayList;

public class InstructorVideoInsightsAdapter extends RecyclerView.Adapter<InstructorVideoInsightsAdapter.ViewHolder> {

    private ArrayList<VideoInsightsList> listItems;
    private Context context;
    private final Activity activity;

    public InstructorVideoInsightsAdapter(ArrayList<VideoInsightsList> video_List, Context context, Activity activity) {
        this.listItems = video_List;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public InstructorVideoInsightsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instructor_video_insights, parent, false);
        return new InstructorVideoInsightsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final InstructorVideoInsightsAdapter.ViewHolder holder, int position) {

        final VideoInsightsList listItem = listItems.get(position);
        holder.videoName.setText(listItem.getVideo_name());
        holder.parameterOneName.setText(listItem.getParameter_one_name());
        holder.parameterTwoName.setText(listItem.getParameter_two_name());
        holder.parameterThreeName.setText(listItem.getParameter_three_name());
        holder.parameterOne.setText(listItem.getParameter_one());
        holder.parameterTwo.setText(listItem.getParameter_two());
        holder.parameterThree.setText(listItem.getParameter_three());

        // view on click
        holder.viewMainVideosInsight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorVideoInsightsActivity.class);
                activityChangeIntent.putExtra("video_id", listItem.getVideo_id());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView videoName, parameterOne, parameterTwo, parameterThree, parameterOneName, parameterTwoName, parameterThreeName;
        private CardView viewMainVideosInsight;

        public ViewHolder(View itemView) {
            super(itemView);

            viewMainVideosInsight = itemView.findViewById(R.id.viewMainVideosInsight);
            videoName = itemView.findViewById(R.id.videoName);
            parameterOneName = itemView.findViewById(R.id.videoInfoOneName);
            parameterTwoName = itemView.findViewById(R.id.videoInfoTwoName);
            parameterThreeName = itemView.findViewById(R.id.videoInfoThreeName);
            parameterOne = itemView.findViewById(R.id.videoInfoOne);
            parameterTwo = itemView.findViewById(R.id.videoInfoTwo);
            parameterThree = itemView.findViewById(R.id.videoInfoThree);

        }
    }

}
