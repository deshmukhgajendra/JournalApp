package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup extends AppCompatActivity {

    EditText email_create , password_create;
    Button createBTN;

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Firebase Connection

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference= db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_create=findViewById(R.id.email_create);
        password_create=findViewById(R.id.password_create);
        createBTN=findViewById(R.id.acc_sign_up_button);


        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    // user already logged in
                }else {
                    // no user loggerd in
                }
            }
        };
        
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email_create.getText().toString())
                 && !TextUtils.isEmpty(password_create.getText().toString())){
                    
                    String email = email_create.getText().toString().trim();
                    String password = password_create.getText().toString().trim();
                    
                    CreateUserEmailAccount(email,password);
                }else{
                    Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CreateUserEmailAccount(String email, String password) {
    }
}