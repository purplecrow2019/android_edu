package com.example.shubham.lamamia_project.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.shubham.lamamia_project.CommentActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.VideoCommentChildList;
import com.example.shubham.lamamia_project.model.VideoCommentParentList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseExpandableListAdapter {

    RequestQueue rq;

    private Context context;
    private ArrayList<VideoCommentParentList> videoCommentParentLists;

    public CommentAdapter(Context context, ArrayList<VideoCommentParentList> videoCommentParentLists) {
        this.context = context;
        this.videoCommentParentLists = videoCommentParentLists;
    }

    @Override
    public int getGroupCount() {
        return videoCommentParentLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<VideoCommentChildList> videoCommentChildLists = videoCommentParentLists.get(groupPosition).getVideoCommentChildLists();
        return videoCommentChildLists.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return videoCommentParentLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<VideoCommentChildList> videoCommentChildLists = videoCommentParentLists.get(groupPosition).getVideoCommentChildLists();
        return videoCommentChildLists.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = videoCommentParentLists.get(groupPosition).getVideo_comment_text();

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.item_video_comment_parent, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.textViewVideoParentComment);
        TextView txtParentCommenterUser = convertView.findViewById(R.id.textViewVideoParentCommenterUser);
        TextView txtParentCommenterInstructor = convertView.findViewById(R.id.textViewVideoParentCommenterInstructor);
        CircleImageView imgParentCommenterUser = convertView.findViewById(R.id.imageViewVideoParentCommenterUser);
        CircleImageView imgParentCommenterInstructor = convertView.findViewById(R.id.imageViewVideoParentCommenterInstructor);
        lblListHeader.setText(headerTitle);

        if (videoCommentParentLists.get(groupPosition).getUser_type().equals("2")){

            txtParentCommenterUser.setVisibility(View.GONE);
            imgParentCommenterUser.setVisibility(View.GONE);
            txtParentCommenterInstructor.setVisibility(View.VISIBLE);
            imgParentCommenterInstructor.setVisibility(View.VISIBLE);
            txtParentCommenterInstructor.setText(videoCommentParentLists.get(groupPosition).getUser_name());
            Picasso.get().load(videoCommentParentLists.get(groupPosition).getUser_image()).fit().into(imgParentCommenterInstructor);

        } else if (videoCommentParentLists.get(groupPosition).getUser_type().equals("1")){

            txtParentCommenterInstructor.setVisibility(View.GONE);
            imgParentCommenterInstructor.setVisibility(View.GONE);
            txtParentCommenterUser.setVisibility(View.VISIBLE);
            imgParentCommenterUser.setVisibility(View.VISIBLE);
            txtParentCommenterUser.setText(videoCommentParentLists.get(groupPosition).getUser_name());
            Picasso.get().load(videoCommentParentLists.get(groupPosition).getUser_image()).fit().into(imgParentCommenterUser);

        }

        ImageView imageView = convertView.findViewById(R.id.imageButtonReply);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityChangeIntent = new Intent(context, CommentActivity.class);
                activityChangeIntent.putExtra("videoID",videoCommentParentLists.get(groupPosition).getVideo_id());
                activityChangeIntent.putExtra("userImage", videoCommentParentLists.get(groupPosition).getUser_image());
                activityChangeIntent.putExtra("videoCommentID",videoCommentParentLists.get(groupPosition).getVideo_comment_id());
                activityChangeIntent.putExtra("videoCommentText",videoCommentParentLists.get(groupPosition).getVideo_comment_text());
                activityChangeIntent.putExtra("videoCommenterName",videoCommentParentLists.get(groupPosition).getUser_name());
                activityChangeIntent.putExtra("videoCommenterImage",videoCommentParentLists.get(groupPosition).getUser_image());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);

            }
        });


        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final VideoCommentChildList videoCommentChildList = (VideoCommentChildList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            convertView = layoutInflater.inflate(R.layout.item_video_comment_child, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.textViewVideoChildComment);
        TextView txtChildCommenterUser = convertView.findViewById(R.id.textViewVideoChildCommenterUser);
        TextView txtChildCommenterInstructor = convertView.findViewById(R.id.textViewVideoChildCommenterInstructor);
        CircleImageView imgChildCommenterUser = convertView.findViewById(R.id.imageViewVideoChildCommenterUser);
        CircleImageView imgChildCommenterInstructor = convertView.findViewById(R.id.imageViewVideoChildCommenterInstructor);
        txtListChild.setText(videoCommentChildList.getVideo_comment_text());


        if (videoCommentChildList.getUser_type().equals("2")){
            txtChildCommenterUser.setVisibility(View.GONE);
            imgChildCommenterUser.setVisibility(View.GONE);
            txtChildCommenterInstructor.setVisibility(View.VISIBLE);
            imgChildCommenterInstructor.setVisibility(View.VISIBLE);
            txtChildCommenterInstructor.setText(videoCommentChildList.getUser_name());
            Picasso.get().load(videoCommentChildList.getUser_image()).fit().into(imgChildCommenterInstructor);
        }else if (videoCommentChildList.getUser_type().equals("1")){
            txtChildCommenterInstructor.setVisibility(View.GONE);
            imgChildCommenterInstructor.setVisibility(View.GONE);
            txtChildCommenterUser.setVisibility(View.VISIBLE);
            imgChildCommenterUser.setVisibility(View.VISIBLE);
            txtChildCommenterUser.setText(videoCommentChildList.getUser_name());
            Picasso.get().load(videoCommentChildList.getUser_image()).fit().into(imgChildCommenterUser);
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // on Child click
            }
        });

        return convertView;
    }




    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
