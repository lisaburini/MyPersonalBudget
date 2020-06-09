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


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class EditProfileActivity extends AppCompatActivity {
    private EditText editFirstName, editLastName;
    private TextView firstName, lastName;
    private Button btnEdit, btnCancel;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("utenti").document(uid);
    private static final String TAG = "EditProfileActivity";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editFirstName = (EditText) findViewById(R.id.editName);
        editLastName = (EditText) findViewById(R.id.editLastName);

        firstName = (TextView) findViewById(R.id.label_name);
        lastName = (TextView) findViewById(R.id.label_lname);


        btnEdit = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editFirstName.getText().toString();
                final String lName = editLastName.getText().toString();

                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(lName)) {
                    Toast.makeText(getApplicationContext(),getString(R.string.update_profile_error),Toast.LENGTH_SHORT).show();
                    return;
                }

                //se nome non è vuoto allora aggiorna il campo "nome", nella collection "utenti" in corrispondenza dell'attuale uid
                if(!TextUtils.isEmpty(name)) {
                    docRef
                            .update("nome", name)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot nome successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error updating document", e);
                                    return;
                                }
                            });
                }

                if(!TextUtils.isEmpty(lName)) {
                    docRef
                            .update("cognome", lName)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot cognome successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error updating document", e);
                                    return;
                                }
                            });
                }

                //se si arriva qui significa che tutto è andato a buon fine
                Toast.makeText(getApplicationContext(),getString(R.string.update_profile),Toast.LENGTH_SHORT).show();
                finish();
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
