package com.example.campusfriends;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import models.UserModel;

public class get_details extends AppCompatActivity {

    EditText name, email;
    Button submit_details;
    String name_user, email_user, phoneNumber, uid, plan;
    FirebaseFirestore db;

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        submit_details = findViewById(R.id.submit_details);

        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        uid = i.getStringExtra("uid");
        plan = i.getStringExtra("plan");
        phoneNumber = i.getStringExtra("phone");

        submit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_user = name.getText().toString().trim();
                email_user = email.getText().toString().trim();
                if (TextUtils.isEmpty(name_user) || TextUtils.isEmpty(email_user)) {
                    Toast.makeText(get_details.this, "Invalid User Input!!!", Toast.LENGTH_SHORT).show();
                }else {
                    put_details(name_user, email_user, phoneNumber, uid);
                }
            }
        });
    }

    private void put_details(String name_user, String email_user, String phoneNumber, String uid){
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        DocumentReference documentRef = db.collection("Users").document(uid);
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", name_user);
                        data.put("email", email_user);

                        documentRef.update(data)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(get_details.this, "Added Name to Database", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("harsh", e.getMessage());
                                });

                        Intent i = new Intent(get_details.this, Home.class);
                        i.putExtra("uid", uid);
                        i.putExtra("name", name_user);
                        i.putExtra("email", email_user);
                        i.putExtra("plan", plan);
                        startActivity(i);
                    }
                } else {
                    Log.d("harsh", "error1");
                }
            }
        });

        // To not override default user values in DB when signing again with google
        firebaseDatabase.getReference().child("Users").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

//                if(!dataSnapshot.hasChild("userName")){

                String defaultUserName = email_user;
                String about = "Online";


                UserModel userModel = new UserModel(defaultUserName.substring(0, defaultUserName.indexOf('@'))
                        , email_user
                        ,phoneNumber
                        ,"R.drawable.user"
                        , about);

                firebaseDatabase.getReference().child("Users").child(uid).setValue(userModel);


//                }else{
//
//                    sharedPreferences = getSharedPreferences("SavedToken",MODE_PRIVATE);
//                    String tokenInMain =  sharedPreferences.getString("ntoken","mynull");
//                    firebaseDatabase.getReference("Users").child(id).child("token").setValue(tokenInMain);
//
//                }
            }
        });
    }
}