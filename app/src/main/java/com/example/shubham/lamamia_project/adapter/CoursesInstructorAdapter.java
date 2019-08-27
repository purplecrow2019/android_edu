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

import com.example.shubham.lamamia_project.InstructorCourseDeleteActivity;
import com.example.shubham.lamamia_project.InstructorCourseEditActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.CoursesInstructorList;
import java.util.ArrayList;

public class CoursesInstructorAdapter extends RecyclerView.Adapter<CoursesInstructorAdapter.ViewHolder>{

    private ArrayList<CoursesInstructorList> listItems;
    private Context context;

    public CoursesInstructorAdapter(ArrayList<CoursesInstructorList> courses_modelList, Context context) {
        this.listItems = courses_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CoursesInstructorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_courses_instructor, parent, false);

        return new CoursesInstructorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoursesInstructorAdapter.ViewHolder holder, int position) {

        final CoursesInstructorList listItem = listItems.get(position);
        holder.courseName.setText(listItem.getCourseName());

        holder.editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorCourseEditActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                activityChangeIntent.putExtra("course_name", listItem.getCourseName());
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        holder.deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorCourseDeleteActivity.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                activityChangeIntent.putExtra("course_name", listItem.getCourseName());
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

        private ImageButton editCourse, deleteCourse;
        private TextView courseName;

        public ViewHolder(View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.courseName);
            editCourse = itemView.findViewById(R.id.updateCourse);
            deleteCourse = itemView.findViewById(R.id.deleteCourse);

        }
    }
}
