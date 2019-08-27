package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.AnalyticsCourseInstructorList;

import java.util.ArrayList;

public class ProfileCourseInstructorAdapter extends RecyclerView.Adapter<ProfileCourseInstructorAdapter.ViewHolder> {

    private ArrayList<AnalyticsCourseInstructorList> analyticsCourseInstructorLists;
    private Context context;

    public ProfileCourseInstructorAdapter(ArrayList<AnalyticsCourseInstructorList> analyticsCourseInstructorListArrayList, Context context) {
        this.analyticsCourseInstructorLists = analyticsCourseInstructorListArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileCourseInstructorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_course_instructor, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AnalyticsCourseInstructorList listItem = analyticsCourseInstructorLists.get(position);
        holder.txtTotalPaidUsers.setText(listItem.getCourse_user_count());
        holder.txtCourseName.setText(listItem.getCourse_name());
        holder.txtTotalViews.setText(listItem.getCourse_views());
        holder.txtCourseTotalViews.setText(listItem.getCourse_total_views());
    }

    @Override
    public int getItemCount() {
        return analyticsCourseInstructorLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtCourseName, txtTotalPaidUsers, txtTotalViews, txtCourseTotalViews;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTotalPaidUsers = itemView.findViewById(R.id.textViewProfileInstructorTotalPaidUsers);
            txtCourseName = itemView.findViewById(R.id.textViewProfileInstructorCourseName);
            txtTotalViews = itemView.findViewById(R.id.textViewProfileInstructorTotalViews);
            txtCourseTotalViews = itemView.findViewById(R.id.textViewProfileInstructorAdapterCourseTotalViews);
        }
    }
}
