package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shubham.lamamia_project.InstructorAddVideoQuizActivity;
import com.example.shubham.lamamia_project.InstructorModuleDeleteActivity;
import com.example.shubham.lamamia_project.InstructorModuleEditActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.ModulesInstructorList;

import java.util.ArrayList;

public class ModulesInstructorAdapter extends RecyclerView.Adapter<ModulesInstructorAdapter.ViewHolder>{

    private ArrayList<ModulesInstructorList> listItems;
    private Context context;

    public ModulesInstructorAdapter(ArrayList<ModulesInstructorList> module_List, Context context) {
        this.listItems = module_List;
        this.context = context;
    }

    @NonNull
    @Override
    public ModulesInstructorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module_instructor, parent, false);

        return new ModulesInstructorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ModulesInstructorAdapter.ViewHolder holder, int position) {

        final ModulesInstructorList listItem = listItems.get(position);
        holder.moduleName.setText(listItem.getModule_name());

        // Edit module
        holder.editModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorModuleEditActivity.class);
                activityChangeIntent.putExtra("module_id", listItem.getModule_id());
                activityChangeIntent.putExtra("module_name", listItem.getModule_name());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        // Delete module
        holder.deleteModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(context, InstructorModuleDeleteActivity.class);
                activityChangeIntent.putExtra("module_id", listItem.getModule_id());
                activityChangeIntent.putExtra("module_name", listItem.getModule_name());
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageButton editModule, deleteModule;
        private TextView moduleName;

        public ViewHolder(View itemView) {
            super(itemView);

            moduleName = itemView.findViewById(R.id.moduleName);
            editModule = itemView.findViewById(R.id.module_edit);
            deleteModule = itemView.findViewById(R.id.module_delete);

        }
    }
}
