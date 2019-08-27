package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.ChooseVideoListActivity;
import com.example.shubham.lamamia_project.InstructorDeleteFileActivity;
import com.example.shubham.lamamia_project.InstructorFileUpdateActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.FileInstructorList;
import java.util.ArrayList;

public class FilesInstructorAdapter extends RecyclerView.Adapter<FilesInstructorAdapter.ViewHolder> {

    private ArrayList<FileInstructorList> FileLists;
    private Context context;

    public FilesInstructorAdapter(ArrayList<FileInstructorList> file_list, Context context) {
        this.FileLists = file_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file_instructor, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FileInstructorList listItem = FileLists.get(position);
        holder.fileName.setText(listItem.getAttachment_name());
        // Color change
        if (listItem.getAttachment_type().equals("1")){
            holder.fileHolder.setBackground(ContextCompat.getDrawable(context, R.drawable.pdf_file_back));
        } else if (listItem.getAttachment_type().equals("2")){
            holder.fileHolder.setBackground(ContextCompat.getDrawable(context,R.drawable.excel_file_back));
        }

        // Edit File
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorFileUpdateActivity.class);
                activityChangeIntent.putExtra("file_id", listItem.getVideo_attachment_id());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        // Delete File
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorDeleteFileActivity.class);
                activityChangeIntent.putExtra("file_id", listItem.getVideo_attachment_id());
                activityChangeIntent.putExtra("file_name", listItem.getAttachment_name());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FileLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView fileName;
        private ImageButton edit, delete;
        private LinearLayout fileHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            fileHolder = itemView.findViewById(R.id.InstructorFileHolder);
            fileName = itemView.findViewById(R.id.fileName);
            edit = itemView.findViewById(R.id.editFile);
            delete = itemView.findViewById(R.id.deleteFile);
        }
    }
}
