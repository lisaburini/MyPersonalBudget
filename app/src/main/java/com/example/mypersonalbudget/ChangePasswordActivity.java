package com.example.mypersonalbudget;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editPassword, editOldPassword;
    private TextView password, oldPassword;
    private Button btnUpdate, btnCancel;
    private static final String TAG = "ChangePasswordActivity";
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editOldPassword = (EditText) findViewById(R.id.editOldPassword);
        editPassword = (EditText) findViewById(R.id.editPassword);

        password = (TextView) findViewById(R.id.label_password);
        oldPassword = (TextView) findViewById(R.id.label_old_password);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPass = editOldPassword.getText().toString();
                final String pass = editPassword.getText().toString();

                if((TextUtils.isEmpty(pass)) && (TextUtils.isEmpty(oldPass))){
                    Toast.makeText(getApplicationContext(),getString(R.string.update_profile_error),Toast.LENGTH_LONG).show();
                    return;
                }

                if( (TextUtils.isEmpty(pass)) && !(TextUtils.isEmpty(oldPass))  || !(TextUtils.isEmpty(pass)) && TextUtils.isEmpty(oldPass)) {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_old_new_password),Toast.LENGTH_LONG).show();
                    return;
                }

                if(!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(oldPass) && pass.length()<6){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_psw_length),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(oldPass)) {
                    final String current_email = firebaseUser.getEmail();
                    final String current_psw = oldPass;
                    AuthCredential credential = EmailAuthProvider.getCredential(current_email,oldPass);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //se autenticazione va a buon fine si entra nell'if (psw vecchia giusta)
                            if(task.isSuccessful()){
                                firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //se non va a buon fine l'aggiornamento della psw si va nell'if
                                        if(!task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            //Log.d(TAG, "Password successfully updated!");
                                            Toast.makeText(getApplicationContext(),getString(R.string.update_password),Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                            }
                            //se psw vecchia sbagliata si entra nell'else (autenticazione non va a buon fine)
                            else {
                                Toast.makeText(getApplicationContext(),getString(R.string.wrong_old_pass),Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }




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
