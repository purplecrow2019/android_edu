package com.example.shubham.lamamia_project.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.InstructorAddVideoActivity;
import com.example.shubham.lamamia_project.InstructorQuestionsActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.InstructorVideoDeleteActivity;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.model.VideosInstructorList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstructorVideoViewAdapter extends RecyclerView.Adapter<InstructorVideoViewAdapter.ViewHolder>{

    private ArrayList<VideosInstructorList> listItems;
    private Context context;
    private final Activity activity;
    private AdapterCallback callback;

    public interface AdapterCallback{
        void onItemClicked(int position);
    }

    public InstructorVideoViewAdapter(ArrayList<VideosInstructorList> video_List, Context context, Activity activity, AdapterCallback callback) {
        this.listItems = video_List;
        this.context = context;
        this.activity = activity;
        this.callback = callback;
    }

    @NonNull
    @Override
    public InstructorVideoViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_instructor, parent, false);

        return new InstructorVideoViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final InstructorVideoViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final VideosInstructorList listItem = listItems.get(position);
        String video_name = listItem.getId() +". " + listItem.getVideo_name();
        holder.videoName.setText(video_name);

        // Module edit redirect
        holder.editVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listItem.getType().equals("1")){
                    Intent activityChangeIntent = new Intent(context, InstructorAddVideoActivity.class);
                    activityChangeIntent.putExtra("video_id", listItem.getVideo_id());
                    activityChangeIntent.putExtra("video_name", listItem.getVideo_name());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }else if (listItem.getType().equals("2")){
                    Intent activityChangeIntent = new Intent(context, InstructorQuestionsActivity.class);
                    activityChangeIntent.putExtra("quiz_id", listItem.getVideo_id());
                    activityChangeIntent.putExtra("quiz_name", listItem.getVideo_name());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            }
        });

        // Module delete redirect
        holder.deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityChangeIntent = new Intent(context, InstructorVideoDeleteActivity.class);
                activityChangeIntent.putExtra("video_id", listItem.getVideo_id());
                activityChangeIntent.putExtra("video_name", listItem.getVideo_name());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        // Moving up/down
        holder.mainHolderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItem.getId().equals("1") && listItem.getIs_last().equals("1")){

                    holder.moveUp.setVisibility(View.GONE);
                    holder.moveDown.setVisibility(View.GONE);

                }else if (listItem.getId().equals("1")){
                    if (holder.moveUp.getVisibility() == View.GONE && holder.moveDown.getVisibility() == View.VISIBLE){
                        holder.moveUp.setVisibility(View.GONE);
                        holder.moveDown.setVisibility(View.GONE);
                    }else{
                        holder.moveUp.setVisibility(View.GONE);
                        holder.moveDown.setVisibility(View.VISIBLE);
                    }
                }else if(listItem.getIs_last().equals("1")){
                    if (holder.moveUp.getVisibility() == View.VISIBLE && holder.moveDown.getVisibility() == View.GONE){
                        holder.moveUp.setVisibility(View.GONE);
                        holder.moveDown.setVisibility(View.GONE);
                    }else{
                        holder.moveUp.setVisibility(View.VISIBLE);
                        holder.moveDown.setVisibility(View.GONE);
                    }
                }else{
                    if (holder.moveUp.getVisibility() == View.VISIBLE && holder.moveDown.getVisibility() == View.VISIBLE){
                        holder.moveUp.setVisibility(View.GONE);
                        holder.moveDown.setVisibility(View.GONE);
                    }else{
                        holder.moveUp.setVisibility(View.VISIBLE);
                        holder.moveDown.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        holder.moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.VIDEO_MOVE_UP_ONE_LEVEL + listItem.getVideo_id();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            JSONObject jsonObject = new JSONObject(s);
                            String stringStatus = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(stringStatus);

                            if (intStatus == 200) {
                                Toast.makeText(context, "Content moved up", Toast.LENGTH_LONG).show();

                                // Making layout changes
                                if(callback != null) {
                                    callback.onItemClicked(position);
                                }
                            } else {
                                Toast.makeText(context, jsonObject.getString("message")+jsonObject.getString("status"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Making layout changes
                            Toast.makeText(context, "Unable to move up", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Making layout changes
                        Toast.makeText(context, "Unable to move up", Toast.LENGTH_LONG).show();
                    }
                });

                holder.rq.add(stringRequest);
            }
        });

        holder.moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.VIDEO_MOVE_DOWN_ONE_LEVEL + listItem.getVideo_id();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            JSONObject jsonObject = new JSONObject(s);
                            String stringStatus = jsonObject.getString("status");
                            int intStatus = Integer.parseInt(stringStatus);

                            if (intStatus == 200) {
                                Toast.makeText(context, "Content moved down", Toast.LENGTH_LONG).show();

                                // Making layout changes
                                if(callback != null) {
                                    callback.onItemClicked(position);
                                }
                            } else {
                                Toast.makeText(context, jsonObject.getString("message")+jsonObject.getString("status"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Making layout changes
                            Toast.makeText(context, "Unable to move down", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Making layout changes
                        Toast.makeText(context, "Unable to move down", Toast.LENGTH_LONG).show();
                    }
                });

                holder.rq.add(stringRequest);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageButton editVideo, deleteVideo;
        private TextView videoName;
        private LinearLayout moveUp, moveDown, mainHolderView;
        private RequestQueue rq;

        public ViewHolder(View itemView) {
            super(itemView);

            videoName = itemView.findViewById(R.id.videoName);
            editVideo = itemView.findViewById(R.id.videoEdit);
            deleteVideo = itemView.findViewById(R.id.videoDelete);
            moveUp = itemView.findViewById(R.id.moveUp);
            moveDown = itemView.findViewById(R.id.moveDown);
            mainHolderView = itemView.findViewById(R.id.mainHolderView);
            rq = Volley.newRequestQueue(context);

        }
    }
}
