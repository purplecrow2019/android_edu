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

import com.example.shubham.lamamia_project.CourseModuleVideoActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.VideoList;

import java.util.ArrayList;

public class ModuleContentAdapter extends RecyclerView.Adapter<ModuleContentAdapter.ViewHolder> {

    private final Activity activity;
    private ArrayList<VideoList> videoLists;
    private Context context;

    public ModuleContentAdapter(ArrayList<VideoList> videoLists, Context context, Activity activity) {
        this.videoLists = videoLists;
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
    public void onBindViewHolder(@NonNull ModuleContentAdapter.ViewHolder holder, int position) {
        final VideoList videoList = videoLists.get(position);
        holder.txtVideoName.setText(videoList.getVideo_name());

        if (videoList.getVideo_order().equals(videoList.getOrder_id())){
            holder.imgVideoPlaying.setImageResource(R.drawable.ic_play_video_module);
            holder.txtVideoName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (videoList.getVideo_view_status().equals("1")){
            holder.imgVideoPlaying.setImageResource(R.drawable.ic_not_played);
            holder.txtVideoName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else if (videoList.getVideo_view_status().equals("2")){
            holder.imgVideoPlaying.setImageResource(R.drawable.ic_video_half_watched);
            holder.txtVideoName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else if (videoList.getVideo_view_status().equals("3")){
            holder.imgVideoPlaying.setImageResource(R.drawable.ic_check_circle_black_24dp);
            holder.txtVideoName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        holder.linCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent activityChangeIntent = new Intent(context, CourseModuleVideoActivity.class);
                activityChangeIntent.putExtra("video_id", videoList.getVideo_id());
                activityChangeIntent.putExtra("moduleID", videoList.getModule_id());
                activityChangeIntent.putExtra("orderID", videoList.getVideo_order());
                activityChangeIntent.putExtra("content_type", videoList.getContent_type());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
                activity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtVideoName;
        private LinearLayout linCourses;
        private ImageView imgVideoCompleted, imgVideoLeft, imgVideoPlaying, imgVideoHalfPlayed;

        public ViewHolder(View itemView) {
            super(itemView);

            linCourses = itemView.findViewById(R.id.linearLayoutCourseModuleVideo);
            txtVideoName = itemView.findViewById(R.id.textViewCourseModuleVideoName);

            //imgVideoLeft = itemView.findViewById(R.id.video_left); // 1
            imgVideoPlaying = itemView.findViewById(R.id.video_playing); //
            //imgVideoHalfPlayed = itemView.findViewById(R.id.video_half_played); // 2
            //imgVideoCompleted = itemView.findViewById(R.id.video_completed); // 3
        }

    }
}
