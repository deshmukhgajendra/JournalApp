package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Signup extends AppCompatActivity {

    EditText email_create , password_create,username_create;
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
        username_create=findViewById(R.id.userName_create_ET);


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
                    String username= username_create.getText().toString().trim();

                    CreateUserEmailAccount(email,password,username);
                }else{
                    Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void CreateUserEmailAccount(String email, String password, final String username) {

        if(!TextUtils.isEmpty(email_create.getText().toString())
                && !TextUtils.isEmpty(password_create.getText().toString())){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // we take our user to next activity
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserId = currentUser.getUid();

                        // Create userMap so we can create a user in the user collection in firebase

                        Map<String , String > userObj= new HashMap<>();
                        userObj.put("userID", currentUserId);
                        userObj.put("username" , username);

                        // Adding users to firestore
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(Objects.requireNonNull(task.getResult().exists())){
                                                    String name = task.getResult().getString("username");

                                                    // If the user is registered sucessfully , then
                                                    // then move to the Journal Activity

                                                    Intent i = new Intent(Signup.this,AddJournalActivity.class);
                                                    i.putExtra("username",name);
                                                    i.putExtra("userId" , currentUserId);
                                                    startActivity(i);
                                                }else {

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Display Failing message
                                                Toast.makeText(Signup.this,"Something went Wrong !",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                }
            });
        }
    }
    
}