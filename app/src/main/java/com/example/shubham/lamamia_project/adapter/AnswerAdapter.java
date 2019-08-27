package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.AnswerList;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private ArrayList<AnswerList> answerLists;
    private Context context;

    public AnswerAdapter(ArrayList<AnswerList> category_list, Context context) {
        this.answerLists = category_list;
        this.context = context;
    }


    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AnswerList answer_list = answerLists.get(position);
        holder.radioAnswer.setText(answer_list.getAnswer_text());
    }

    @Override
    public int getItemCount() {
        return answerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public RadioButton radioAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            radioAnswer = itemView.findViewById(R.id.radioAnswer);

        }
    }
}
