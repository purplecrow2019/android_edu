package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.CourseRatingList;

import java.util.ArrayList;

public class CourseChildRatingsAdapter extends RecyclerView.Adapter<CourseChildRatingsAdapter.ViewHolder> {


    private ArrayList<CourseRatingList> course_rating_list;
    private Context context;

    public CourseChildRatingsAdapter(ArrayList<CourseRatingList> course_rating_list, Context context) {
        this.course_rating_list = course_rating_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating_child, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseChildRatingsAdapter.ViewHolder holder, int position) {
        final CourseRatingList course_rating_list_item = course_rating_list.get(position);
        holder.userName.setText(course_rating_list_item.getUser_name());
        holder.ratingText.setText(course_rating_list_item.getRating_text());
    }

    @Override
    public int getItemCount() { return course_rating_list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, ratingText;
        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textViewListRatingChildUserName);
            ratingText = itemView.findViewById(R.id.textViewListRatingChildText);
        }
    }
}
