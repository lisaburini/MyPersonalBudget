package com.example.mypersonalbudget.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mypersonalbudget.MainActivity;
import com.example.mypersonalbudget.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstName, lastName, username, password;
    private Button btnSignUp, btnCancel;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = firstName.getText().toString();
                final String lName = lastName.getText().toString();
                final String email = username.getText().toString();
                final String pass = password.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name) || TextUtils.isEmpty(lName)){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_signup_empty),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()<6){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_psw_length),Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //update profile DisplayName
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name + " " + lName)
                                            .build();
                                    user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //scriviamo nella collection utenti il nome, il cognome e l'email
                                                writeUserToDb(name, lName, email, user.getUid());
                                                //Log.d(TAG, user.getDisplayName());
                                                finish();
                                            }
                                        }
                                    });


                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void writeUserToDb(String nome, String cognome, String email, String uid ){
        Map<String, Object> user = new HashMap<>();
        user.put("nome", nome);
        user.put("cognome", cognome);
        user.put("email", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance(); //istanza del nostro database
        db.collection("utenti").document(uid).set(user);

    }
}
