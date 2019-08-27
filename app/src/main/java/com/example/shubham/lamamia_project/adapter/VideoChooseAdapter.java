package com.example.shubham.lamamia_project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shubham.lamamia_project.InstructorAddVideoActivity;
import com.example.shubham.lamamia_project.InstructorVideoEditorActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.VideoChooseList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoChooseAdapter extends RecyclerView.Adapter<VideoChooseAdapter.ViewHolder>{

    private ArrayList<VideoChooseList> listItems;
    private Context context;
    private final Activity activity;

    public VideoChooseAdapter(ArrayList<VideoChooseList> video_List, Context context, Activity activity) {
        this.listItems = video_List;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VideoChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_select_upload, parent, false);

        return new VideoChooseAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoChooseAdapter.ViewHolder holder, int position) {

        final VideoChooseList listItem = listItems.get(position);
        holder.videoName.setText(listItem.getVideo_name());
        Glide.with(context).load("file://" + listItem.getVideo_image()).into(holder.videoImage);

        // Module edit redirect
        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorVideoEditorActivity.class);
                activityChangeIntent.putExtra("video_id", listItem.getVideo_id());
                activityChangeIntent.putExtra("url", listItem.getVideo_uri());
                activityChangeIntent.putExtra("video_name", listItem.getVideo_name());
                activityChangeIntent.putExtra("video_image", listItem.getVideo_image());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private Button upload;
        private TextView videoName;
        private ImageView videoImage;

        public ViewHolder(View itemView) {
            super(itemView);

            videoImage = itemView.findViewById(R.id.videoImage);
            videoName = itemView.findViewById(R.id.videoName);
            upload = itemView.findViewById(R.id.upload);

        }
    }
}
