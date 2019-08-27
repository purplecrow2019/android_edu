package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.model.PaymentHistoryList;

import java.util.ArrayList;

public class InstructorPaymentHistoryAdapter extends RecyclerView.Adapter<InstructorPaymentHistoryAdapter.ViewHolder>{

    private ArrayList<PaymentHistoryList> listItems;
    private Context context;

    public InstructorPaymentHistoryAdapter(ArrayList<PaymentHistoryList> courses_modelList, Context context) {
        this.listItems = courses_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final PaymentHistoryList listItem = listItems.get(position);

        holder.stage.setText(listItem.getStage());
        holder.price.setText(listItem.getPrice());

        if (listItem.getStatus().equals("1")){
            holder.status.setText("DONE");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.status.setText("PENDING");
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView stage, price, status;

        public ViewHolder(View itemView) {
            super(itemView);

            stage = itemView.findViewById(R.id.stage);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
        }
    }
}
