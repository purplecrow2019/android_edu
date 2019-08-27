package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.shubham.lamamia_project.CourseModuleVideoActivity;
import com.example.shubham.lamamia_project.R;
import java.util.ArrayList;

import com.example.shubham.lamamia_project.UpdateApplicationActivity;
import com.example.shubham.lamamia_project.model.NotificationList;
import com.squareup.picasso.Picasso;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<NotificationList> NotificationLists;
    private Context context;

    public NotificationAdapter(ArrayList<NotificationList> notification_list, Context context) {
        this.NotificationLists = notification_list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NotificationList listItem = NotificationLists.get(position);
        holder.txtNotificationText.setText(Html.fromHtml(listItem.getNotification_text()));
        Picasso.get().load(listItem.getImage()).fit().into(holder.notImage);
        holder.linNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listItem.getNotification_type().equals("1")){
                    Intent activityChangeIntent = new Intent(context, CourseModuleVideoActivity.class);
                    activityChangeIntent.putExtra("video_id",listItem.getVideo_id());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                } else if(listItem.getNotification_type().equals("2")) {
                    Intent activityChangeIntent = new Intent(context, UpdateApplicationActivity.class);
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NotificationLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNotificationText;
        private LinearLayoutCompat linNotification;
        private ImageView notImage;

        public ViewHolder(View itemView) {
            super(itemView);
            linNotification = itemView.findViewById(R.id.linearLayoutNotification);
            txtNotificationText = itemView.findViewById(R.id.textViewNotificationText);
            notImage = itemView.findViewById(R.id.notImage);
        }
    }
}
