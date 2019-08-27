package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.FileLists;

import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    private ArrayList<FileLists> FileLists;
    private Context context;

    public FilesAdapter(ArrayList<FileLists> file_list, Context context) {
        this.FileLists = file_list;
        this.context = context;
    }

    @NonNull
    @Override
    public FilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FileLists listItem = FileLists.get(position);

        if (listItem.getAttachment_type().equals("1")){

            holder.linearLayoutPdfHolder.setVisibility(View.VISIBLE);
            holder.pdfName.setText(listItem.getAttachment_name());

        } else if (listItem.getAttachment_type().equals("2")){

            holder.linearLayoutExcelHolder.setVisibility(View.VISIBLE);
            holder.excelName.setText(listItem.getAttachment_name());

        }

        holder.linearLayoutMainFileHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://docs.google.com/gview?embedded=true&url=" + listItem.getAttachment_url()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FileLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout linearLayoutPdfHolder, linearLayoutExcelHolder, linearLayoutMainFileHolder;
        private TextView pdfName, excelName;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayoutExcelHolder = itemView.findViewById(R.id.excelFileHolder);
            linearLayoutPdfHolder = itemView.findViewById(R.id.pdfFileHolder);
            linearLayoutMainFileHolder = itemView.findViewById(R.id.mainFileHolder);
            pdfName = itemView.findViewById(R.id.pdfFileName);
            excelName = itemView.findViewById(R.id.excelFileName);
        }
    }
}
