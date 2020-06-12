package com.example.mypersonalbudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewTransactionActivity extends AppCompatActivity {

    private RadioGroup transactionCategory, earnings, outflows;
    private RadioButton earningsCategory, outflowsCategory, selectedCategory, selectedTransiction;
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
                    selectedTransiction = (RadioButton)findViewById(earnings.getCheckedRadioButtonId());
                } else if (selectedCategory == outflowsCategory) {
                    selectedTransiction = (RadioButton)findViewById(outflows.getCheckedRadioButtonId());
                }
                try {
                    Intent intent = new Intent();
                    intent.putExtra("category", selectedCategory.getText().toString());
                    intent.putExtra("title", selectedTransiction.getText().toString());
                    intent.putExtra("amount", textAmount.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(NewTransactionActivity.this, getString(R.string.inforequired), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

