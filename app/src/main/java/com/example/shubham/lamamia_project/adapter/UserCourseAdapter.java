package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.UserCourseList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserCourseAdapter extends RecyclerView.Adapter<UserCourseAdapter.ViewHolder> {

    private ArrayList<UserCourseList> userCourseLists;
    private Context context;

    public UserCourseAdapter(ArrayList<UserCourseList> user_course_list, Context context) {
        this.userCourseLists = user_course_list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_course_details, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserCourseList listItem = userCourseLists.get(position);
        holder.user_course_name.setText(listItem.getCourse_name());
        Picasso.get().load(listItem.getCourse_image()).fit().into(holder.user_course_image);
    }

    @Override
    public int getItemCount() {
        return userCourseLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView user_course_name;
        public ImageView user_course_image;

        public ViewHolder(View itemView) {
            super(itemView);
            user_course_image = itemView.findViewById(R.id.userCourseImage);
            user_course_name = itemView.findViewById(R.id.userCourseName);

        }
    }

}
