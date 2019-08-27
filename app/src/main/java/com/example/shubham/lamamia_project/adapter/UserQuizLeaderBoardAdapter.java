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
import com.example.shubham.lamamia_project.model.LeaderBoardList;
import java.util.ArrayList;

public class UserQuizLeaderBoardAdapter extends RecyclerView.Adapter<UserQuizLeaderBoardAdapter.ViewHolder> {

    private ArrayList<LeaderBoardList> leaderBoardLists;
    private Context context;

    public UserQuizLeaderBoardAdapter(ArrayList<LeaderBoardList> leaderboard_lists, Context context) {
        this.leaderBoardLists = leaderboard_lists;
        this.context = context;
    }

    @NonNull
    @Override
    public UserQuizLeaderBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_quiz, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LeaderBoardList listItem = leaderBoardLists.get(position);
            holder.text.setText(listItem.getUser_name());
    }

    @Override
    public int getItemCount() {
        return leaderBoardLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.leaderBoardText);
        }
    }
}
