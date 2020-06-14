package com.example.mypersonalbudget.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypersonalbudget.MainActivity;
import com.example.mypersonalbudget.NewTransactionActivity;
import com.example.mypersonalbudget.R;
import com.example.mypersonalbudget.entities.Transaction;
import com.example.mypersonalbudget.ui.utilities.TransactionsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentTransactions extends Fragment {

    public static final int TRANSACTION_REQUEST = 101;
    private RecyclerView recyclerView;
    private TransactionsAdapter transactionsAdapter;
    private ArrayList<Transaction> transactions;
    private ImageButton btnAdd;
    private TextView actualMoney;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;

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

        Query allTransactionsQuery = db.collection("utenti").document(uid).collection("transazioni").orderBy("createdAt", Query.Direction.ASCENDING);
        allTransactionsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String category = document.getString("tipologia");
                        String title = document.getString("categoria");
                        float amount = Float.parseFloat(document.getString("cifra"));
                        String date = document.getString("data");
                        Transaction transaction = new Transaction(category, title, amount, date);
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
                } else {
                    Toast.makeText( getActivity().getBaseContext(), getString(R.string.wrong),Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                String id = intent.getExtras().getString("id");
                docRef = db.collection("utenti").document(uid).collection("transazioni").document(id);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String category = documentSnapshot.getString("tipologia");
                        String title = documentSnapshot.getString("categoria");
                        float amount = Float.parseFloat(documentSnapshot.getString("cifra"));
                        String date = documentSnapshot.getString("data");
                        Transaction transaction = new Transaction(category, title, amount, date);
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
                });
            }
        }
    }

}
