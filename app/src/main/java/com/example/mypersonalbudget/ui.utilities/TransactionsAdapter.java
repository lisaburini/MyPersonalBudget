package com.example.mypersonalbudget.ui.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypersonalbudget.R;
import com.example.mypersonalbudget.entities.Transaction;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.CViewHolder> {

    class CViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory, textTitle, textAmount, textDate;

        CViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.text_category);
            textTitle = itemView.findViewById(R.id.text_title);
            textAmount = itemView.findViewById(R.id.text_amount);
            textDate = itemView.findViewById(R.id.text_date);
        }
    }

    private ArrayList<Transaction> structure;

    public TransactionsAdapter(ArrayList<Transaction> structure) {
        this.structure = structure;
    }

    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_row, parent, false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.textCategory.setText(structure.get(position).getCategory());
        holder.textTitle.setText(structure.get(position).getTitle());
        holder.textAmount.setText(structure.get(position).getAmount()+" €");
        holder.textDate.setText(structure.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return structure.size();
    }
}
