package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.CourseChildRatingsActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.CourseRatingList;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{

    private ArrayList<CourseRatingList> course_rating_list;
    private Context context;

    public RatingAdapter(ArrayList<CourseRatingList> course_rating_list, Context context) {
        this.course_rating_list = course_rating_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_course_rating, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        final CourseRatingList course_rating_list_item = course_rating_list.get(position);
        holder.userName.setText(course_rating_list_item.getUser_name());
        holder.ratingView.setText(course_rating_list_item.getRating_count());
        holder.ratingText.setText(course_rating_list_item.getRating_text());
        holder.totalComments.setText(course_rating_list_item.getTotal_comments());

        holder.linearLayoutCourseRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, CourseChildRatingsActivity.class);
                activityChangeIntent.putExtra("course_rating_id", course_rating_list_item.getCourse_rating_id());
                activityChangeIntent.putExtra("course_id", course_rating_list_item.getCourse_id());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return course_rating_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userName, ratingText, ratingView, totalComments;
        public LinearLayout linearLayoutCourseRating;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textViewListRatingUserName);
            ratingText = itemView.findViewById(R.id.textViewListRatingText);
            ratingView = itemView.findViewById(R.id.textViewListRatingCountRating);
            totalComments = itemView.findViewById(R.id.textViewCourseRatingComments);
            linearLayoutCourseRating = itemView.findViewById(R.id.linearLayoutCourseRating);
        }
    }
}
