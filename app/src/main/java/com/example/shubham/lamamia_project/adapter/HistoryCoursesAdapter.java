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
import com.example.shubham.lamamia_project.model.HistoryCourseList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryCoursesAdapter extends RecyclerView.Adapter<HistoryCoursesAdapter.ViewHolder> {

    private ArrayList<HistoryCourseList> historyCourseLists;
    private Context context;

    public HistoryCoursesAdapter(ArrayList<HistoryCourseList> history_course_list, Context context) {
        this.historyCourseLists = history_course_list;
        this.context = context;
    }


    @NonNull
    @Override
    public HistoryCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_profile, parent, false);

        return new HistoryCoursesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryCourseList listItem = historyCourseLists.get(position);

        holder.txtCourseName.setText(listItem.getCourse_name());
        Picasso.get().load(listItem.getCourse_image()).fit().into(holder.imgCourseItemImage);
    }

    @Override
    public int getItemCount() {
        return historyCourseLists.size();
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
