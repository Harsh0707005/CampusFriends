package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText phone_no, otp_text;
    Button send_otp, verify;
    ProgressBar progressBar;
    String verificationId, plan, name, phoneNumber;

    CountryCodePicker codePicker;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if(user!=null){
            Intent i = new Intent(MainActivity.this, Home.class);
            startActivity(i);
            finish();
//            check_name(FirebaseAuth.getInstance().getUid());
        }
        phone_no = findViewById(R.id.phone_no);
        otp_text = findViewById(R.id.otp_text);
        send_otp = findViewById(R.id.send_otp);
        verify = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressBar);
        codePicker=findViewById(R.id.country_code);
        mAuth = FirebaseAuth.getInstance();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phone_no.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    String country_code=codePicker.getSelectedCountryCode();
                    phoneNumber = "+" + country_code + phone_no.getText().toString().trim();
                    sendOTP(phoneNumber);
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp_user = otp_text.getText().toString();
                if (TextUtils.isEmpty(otp_text.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    verifyOTP(otp_user);
                }
            }
        });
    }

    private void sendOTP(String number) {

        otp_text.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        verify.setVisibility(View.VISIBLE);
        verify.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verify.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            verificationId = s;
        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String otp = phoneAuthCredential.getSmsCode();
            verifyOTP(otp);
            if (otp != null) {
                verifyOTP(otp);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("harsh", e.getMessage());
            Toast.makeText(MainActivity.this, "SMS verification failed! Please try again later!!!", Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyOTP(String otp){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signinWithCredential(credential);
    }
    private void signinWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Toast.makeText(MainActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();

                            check_name(userId);

                        } else {
                            Toast.makeText(MainActivity.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void check_name(String uid){
        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        plan = document.getString("plan");
                        name = document.getString("name");
                        if(name==null) {
                            Intent i = new Intent(MainActivity.this, get_details.class);
                            i.putExtra("uid", uid);
                            i.putExtra("plan", plan);
                            i.putExtra("phone", phoneNumber);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Log.d("harsh", name);
                            Intent i = new Intent(MainActivity.this, Home.class);
                            i.putExtra("uid", uid);
                            i.putExtra("name", name);
                            i.putExtra("plan", plan);
                            startActivity(i);
                            finish();
                        }
                        finish();
                    } else {
                        Log.d("harsh", "error");
                        DocumentReference documentRef = db.collection("Users").document(uid);
                        Map<String, Object> data = new HashMap<>();
                        data.put("plan", "free");
                        plan = "free";

                        documentRef.set(data)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(MainActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("harsh", e.getMessage());
                                });
//                        Log.d("harsh", "done");
                        Intent i = new Intent(MainActivity.this, get_details.class);
                        i.putExtra("uid", uid);
                        i.putExtra("plan", plan);
                        startActivity(i);
                    }
                } else {
                    Log.d("harsh", "error1");

                }
            }
        });
    }
}