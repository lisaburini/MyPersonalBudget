package com.example.mypersonalbudget;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class EditProfileActivity extends AppCompatActivity {
    private EditText editFirstName, editLastName, editPassword;
    private TextView firstName, lastName, password;
    private Button btnEdit, btnCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editFirstName = (EditText) findViewById(R.id.editName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editPassword = (EditText) findViewById(R.id.editPassword);

        firstName = (TextView) findViewById(R.id.label_name);
        lastName = (TextView) findViewById(R.id.label_lname);
        password = (TextView) findViewById(R.id.label_password);

        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnCancel = (Button) findViewById(R.id.btn_cancel);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

