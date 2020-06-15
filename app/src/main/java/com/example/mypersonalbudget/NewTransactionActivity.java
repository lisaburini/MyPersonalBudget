package com.example.mypersonalbudget;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewTransactionActivity extends AppCompatActivity {

    private RadioGroup transactionCategory, earnings, outflows;
    private RadioButton earningsCategory, outflowsCategory, selectedCategory, selectedTransaction;
    private EditText textAmount;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        getSupportActionBar().setTitle(getString(R.string.app_name));

        transactionCategory = (RadioGroup)findViewById(R.id.radioGroup_transaction_type);
        earningsCategory = (RadioButton)findViewById(R.id.earnings);
        outflowsCategory = (RadioButton)findViewById(R.id.outflows);
        earnings = (RadioGroup)findViewById(R.id.radioGroup_earnings);
        outflows = (RadioGroup)findViewById(R.id.radioGroup_outflows);
        textAmount = (EditText)findViewById(R.id.transaction_amount);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);

        earningsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < outflows.getChildCount(); i++) {
                    outflows.getChildAt(i).setEnabled(false);
                }
                for (int i = 0; i < earnings.getChildCount(); i++) {
                    earnings.getChildAt(i).setEnabled(true);
                }
            }
        });

        outflowsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < earnings.getChildCount(); i++) {
                    earnings.getChildAt(i).setEnabled(false);
                }
                for (int i = 0; i < outflows.getChildCount(); i++) {
                    outflows.getChildAt(i).setEnabled(true);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = (RadioButton)findViewById(transactionCategory.getCheckedRadioButtonId());
                if (selectedCategory == earningsCategory) {
                    selectedTransaction = (RadioButton)findViewById(earnings.getCheckedRadioButtonId());
                } else if (selectedCategory == outflowsCategory) {
                    selectedTransaction = (RadioButton)findViewById(outflows.getCheckedRadioButtonId());
                }

                try {
                    String category = selectedCategory.getText().toString();
                    String title = selectedTransaction.getText().toString();
                    String amount = textAmount.getText().toString();
                    if(TextUtils.isEmpty(amount)) {
                        Toast.makeText(NewTransactionActivity.this, getString(R.string.inforequired),Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String idTransaction = writeTransactionToDbAndGetId(category, title, amount, uid);

                    Intent intent = new Intent();
                    intent.putExtra("id", idTransaction);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (NullPointerException e) {
                    Toast.makeText(NewTransactionActivity.this, getString(R.string.inforequired),Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    private String writeTransactionToDbAndGetId(String category, String title, String amount, String uid) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("tipologia", category);
        transaction.put("categoria", title);
        transaction.put("cifra", amount);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        transaction.put("data", date);
        transaction.put("createdAt", Timestamp.now());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("utenti").document(uid).collection("transazioni").document().getId();
        db.collection("utenti").document(uid).collection("transazioni").document(id).set(transaction);
        return id;
    }

}

