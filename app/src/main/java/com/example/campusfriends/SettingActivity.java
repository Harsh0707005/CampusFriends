package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campusfriends.databinding.ActivitySettingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding activitySettingBinding;
    FragmentManager fragmentManager;
    FirebaseAuth myAuth;
    FragmentTransaction fragmentTransaction;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());

        fragmentManager = getSupportFragmentManager();

        myAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        activitySettingBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                        activitySettingBinding.profile.setVisibility(View.VISIBLE);
                        activitySettingBinding.logout.setVisibility(View.VISIBLE);
                    } else {
                        Intent i = new Intent(SettingActivity.this, ChatActivity.class);
                        startActivity(i);
                    }
                }catch (Exception e){
                    Log.d("harsh", e.getMessage());
                }
            }
        });


        activitySettingBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activitySettingBinding.profile.setVisibility(View.GONE);
                activitySettingBinding.logout.setVisibility(View.GONE);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.setting_container, ProfileFragment.class, null)
                        .addToBackStack(null)
                        .commit();

            }
        });

        activitySettingBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAuth.signOut();
//                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });



    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
            activitySettingBinding.profile.setVisibility(View.VISIBLE);
            activitySettingBinding.logout.setVisibility(View.VISIBLE);
        }
        else
            super.onBackPressed();

    }
}