package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.QuestionList;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{

    private ArrayList<QuestionList> questionLists;
    private Context context;

    public QuestionAdapter(ArrayList<QuestionList> question_list, Context context) {
        this.questionLists = question_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final QuestionList questionList = questionLists.get(position);
        String question_id = questionList.getQuestion_id();
        final String question = "प्रश्न: " + questionList.getQuestion_name();
        holder.textQuestion.setText(question);

    }

    @Override
    public int getItemCount() {
        return questionLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textQuestion;
        public RecyclerView recyclerViewAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            textQuestion = itemView.findViewById(R.id.textViewItemQuestion);
            recyclerViewAnswer = itemView.findViewById(R.id.recyclerViewAnswer);
        }
    }
}
