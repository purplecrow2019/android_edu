package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.FutureCourseList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FutureCoursesAdapter extends RecyclerView.Adapter<FutureCoursesAdapter.ViewHolder> {

    private ArrayList<FutureCourseList> futureCourseLists;
    private Context context;

    public FutureCoursesAdapter(ArrayList<FutureCourseList> future_course_list, Context context) {
        this.futureCourseLists = future_course_list;
        this.context = context;
    }

    @NonNull
    @Override
    public FutureCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_profile, parent, false);

        return new FutureCoursesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FutureCourseList listItem = futureCourseLists.get(position);

        holder.txtCourseName.setText(listItem.getCourse_name());
        Picasso.get().load(listItem.getCourse_image()).fit().into(holder.imgCourseItemImage);
    }

    @Override
    public int getItemCount() {
        return futureCourseLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCourseName;
        CircleImageView imgCourseItemImage;
        RelativeLayout relativeLayoutCourseProfileItem;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayoutCourseProfileItem = itemView.findViewById(R.id.relativeLayoutCourseProfile);
            imgCourseItemImage = itemView.findViewById(R.id.imageViewCourseProfileItemImage);
            txtCourseName = itemView.findViewById(R.id.textViewCourseProfileItemName);
        }
    }


}
