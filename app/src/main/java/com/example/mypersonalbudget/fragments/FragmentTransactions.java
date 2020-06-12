package com.example.mypersonalbudget.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypersonalbudget.MainActivity;
import com.example.mypersonalbudget.NewTransactionActivity;
import com.example.mypersonalbudget.R;
import com.example.mypersonalbudget.entities.Transaction;
import com.example.mypersonalbudget.ui.utilities.TransactionsAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentTransactions extends Fragment {

    public static final int TRANSACTION_REQUEST = 101;
    private RecyclerView recyclerView;
    private TransactionsAdapter transactionsAdapter;
    private ArrayList<Transaction> transactions;
    private ImageButton btnAdd;
    private TextView actualMoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        transactions = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        actualMoney = view.findViewById(R.id.actual_money);
        recyclerView = view.findViewById(R.id.rv_transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsAdapter = new TransactionsAdapter(transactions);
        recyclerView.setAdapter(transactionsAdapter);

        btnAdd = view.findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NewTransactionActivity.class);
                startActivityForResult(intent, TRANSACTION_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == TRANSACTION_REQUEST) {
            if(resultCode == MainActivity.RESULT_OK) {
                    String category = intent.getExtras().getString("category");
                    String title = intent.getExtras().getString("title");
                    float amount = Float.parseFloat(intent.getExtras().getString("amount"));
                    Transaction transaction = new Transaction(category, title, amount);
                    transactions.add(transaction);
                    float tot = 0;
                    for (Transaction i:transactions) {
                        if(i.getCategory().equals("Earnings")) {
                            tot += i.getAmount();
                            actualMoney.setText(tot+" €");
                        } else if(i.getCategory().equals("Outflows")) {
                            tot -= i.getAmount();
                            actualMoney.setText(tot+" €");
                        }
                    }

                    transactionsAdapter.notifyDataSetChanged();
            }
        }
    }

}
