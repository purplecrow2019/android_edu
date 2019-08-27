package com.example.shubham.lamamia_project.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.CourseModuleVideoActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.ChildModuleList;
import com.example.shubham.lamamia_project.model.ParentModuleList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ParentModuleList> parentModuleLists;

    public ExpandableListAdapter(Context context, ArrayList<ParentModuleList> parentModuleLists) {
        this.context = context;
        this.parentModuleLists = parentModuleLists;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildModuleList> childModuleLists = parentModuleLists.get(groupPosition).getChildModuleLists();
        return childModuleLists.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildModuleList childModuleList = (ChildModuleList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.module_child_item, null);
        }

        TextView textViewVideo = convertView.findViewById(R.id.moduleChildItemVideoName);
        textViewVideo.setText(childModuleList.getVideo_name());

        if (childModuleList.getContent_type().equals("1")){
            textViewVideo.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_play_video_module),null, ContextCompat.getDrawable(context,R.drawable.ic_keyboard_arrow_right_black_24dp), null);
        } else if (childModuleList.getContent_type().equals("2")){
            textViewVideo.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.ic_quiz_icon),null, ContextCompat.getDrawable(context,R.drawable.ic_keyboard_arrow_right_black_24dp), null);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, CourseModuleVideoActivity.class);
                activityChangeIntent.putExtra("video_id",childModuleList.getVideo_id());
                activityChangeIntent.putExtra("module_id", childModuleList.getModule_id());
                activityChangeIntent.putExtra("order_id",childModuleList.getVideo_order());
                activityChangeIntent.putExtra("content_type", childModuleList.getContent_type());
                activityChangeIntent.putExtra("course_id", childModuleList.getCourse_id());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildModuleList> childModuleLists = parentModuleLists.get(groupPosition).getChildModuleLists();
        return childModuleLists.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentModuleLists.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parentModuleLists.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.module_item, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textViewModuleItemName);
        lblListHeader.setText(parentModuleLists.get(groupPosition).getModule_name());

        TextView moduleTotalDuration = convertView.findViewById(R.id.textViewModelItemDuration);
        moduleTotalDuration.setText(parentModuleLists.get(groupPosition).getTotal_duration());

        ProgressBar progressBarCoursePercentage = convertView.findViewById(R.id.progressBarCoursePercentage);
        progressBarCoursePercentage.setProgress(Integer.parseInt(parentModuleLists.get(groupPosition).getCompleted_percentage()));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
