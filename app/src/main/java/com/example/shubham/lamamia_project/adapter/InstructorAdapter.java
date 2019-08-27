package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shubham.lamamia_project.InstructorActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.InstructorList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.ViewHolder> {

    private ArrayList<InstructorList> InstructorLists;
    private Context context;

    public InstructorAdapter(ArrayList<InstructorList> instructor_list, Context context) {
        this.InstructorLists = instructor_list;
        this.context = context;
    }

    @NonNull
    @Override
    public InstructorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instructor_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final InstructorList listItem = InstructorLists.get(position);

        holder.txtInstructorName.setText(listItem.getInstructor_name());
        Picasso.get().load(listItem.getInstructor_image()).fit().into(holder.imgInstructorImage);
        holder.txtInstructorItemShortDescription.setText(listItem.getInstructor_achievements());

        holder.linInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorActivity.class);
                activityChangeIntent.putExtra("instructor_id", listItem.getInstructor_id());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return InstructorLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtInstructorName, txtInstructorItemShortDescription;
        public CircleImageView imgInstructorImage;
        public RelativeLayout relativeLayoutInstructorDescription;
        public LinearLayout linInstructor;

        public ViewHolder(View itemView) {
            super(itemView);
            linInstructor = itemView.findViewById(R.id.linearLayoutInstructorItem);
            imgInstructorImage = itemView.findViewById(R.id.imageViewInstructorItemImage);
            txtInstructorName = itemView.findViewById(R.id.textViewInstructorItemName);
            txtInstructorItemShortDescription = itemView.findViewById(R.id.textViewInstructorItemShortDescription);
        }
    }
}
