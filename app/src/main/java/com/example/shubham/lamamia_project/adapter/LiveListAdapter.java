package com.example.shubham.lamamia_project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.WatchLiveActivity;
import com.example.shubham.lamamia_project.model.LiveList;

import java.util.ArrayList;

public class LiveListAdapter extends RecyclerView.Adapter<LiveListAdapter.ViewHolder> {

    private final Activity activity;
    private ArrayList<LiveList> live_list;
    private Context context;

    public LiveListAdapter(ArrayList<LiveList> live_list, Context context, Activity activity) {
        this.live_list = live_list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module_content, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveListAdapter.ViewHolder holder, int position) {
        final LiveList liveList = live_list.get(position);

        holder.txtVideoName.setText(liveList.getContent_name());
        holder.linCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent activityChangeIntent = new Intent(context, WatchLiveActivity.class);
                activityChangeIntent.putExtra("content_id", liveList.getContent_id());
                activityChangeIntent.putExtra("live_url", liveList.getLive_url());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
                activity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return live_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtVideoName;
        public LinearLayout linCourses;
        public ImageView imgVideoCompleted;

        public ViewHolder(View itemView) {
            super(itemView);

            linCourses = itemView.findViewById(R.id.linearLayoutCourseModuleVideo);
            txtVideoName = itemView.findViewById(R.id.textViewCourseModuleVideoName);
            //imgVideoCompleted = itemView.findViewById(R.id.video_completed); // 3
        }

    }
}
