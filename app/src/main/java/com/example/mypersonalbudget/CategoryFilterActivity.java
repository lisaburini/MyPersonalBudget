package com.example.mypersonalbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CategoryFilterActivity extends AppCompatActivity {

    private RadioGroup transactionCategory, earnings, outflows;
    private RadioButton earningsCategory, outflowsCategory, selectedCategory, selectedTransaction;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

        getSupportActionBar().setTitle(getString(R.string.app_name));

        transactionCategory = (RadioGroup)findViewById(R.id.radioGroup_filter_type);
        earningsCategory = (RadioButton)findViewById(R.id.filter_earnings);
        outflowsCategory = (RadioButton)findViewById(R.id.filter_outflows);
        earnings = (RadioGroup)findViewById(R.id.radioGroup_filter_earnings);
        outflows = (RadioGroup)findViewById(R.id.radioGroup_filter_outflows);
        btnConfirm = (Button)findViewById(R.id.button_confirm);

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

        try {
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCategory = (RadioButton)findViewById(transactionCategory.getCheckedRadioButtonId());
                    if (selectedCategory == earningsCategory) {
                        selectedTransaction = (RadioButton)findViewById(earnings.getCheckedRadioButtonId());
                    } else if (selectedCategory == outflowsCategory) {
                        selectedTransaction = (RadioButton)findViewById(outflows.getCheckedRadioButtonId());
                    }

                    String category = selectedCategory.getText().toString();
                    String title = selectedTransaction.getText().toString();
                    if(TextUtils.isEmpty(category) || TextUtils.isEmpty(title)) {
                        Toast.makeText(CategoryFilterActivity.this, getString(R.string.inforequired),Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent();
                    intent.putExtra("filter_category", title);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(CategoryFilterActivity.this, getString(R.string.wrong),Toast.LENGTH_SHORT).show();
        }

    }
}
