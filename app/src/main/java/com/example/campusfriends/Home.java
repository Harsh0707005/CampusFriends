package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campusfriends.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    String plan, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        String uid = FirebaseAuth.getInstance().getUid();
        name = i.getStringExtra("name");
        plan = i.getStringExtra("plan");


        Bundle args = new Bundle();
        args.putString("name", "");
        if (name == null || plan == null) {
            db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            plan = document.getString("plan");
                            name = document.getString("name");
                            args.putString("name", name);

                            HomeFragment fragment = new HomeFragment();
                            fragment.setArguments(args);
                            replaceFragment(fragment);
                        } else {
                            Log.d("harsh", "error");
                        }
                    } else {
                        Log.d("harsh", "error");

                    }
                }
            });
        }

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        replaceFragment(fragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
//            Log.d("harsh", String.valueOf(itemId));
            if (itemId == R.id.home) {
//                replaceFragment(new HomeFragment());
//                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(args);
                replaceFragment(fragment);
            } else if (itemId == R.id.mocktests) {
                Intent k = new Intent(Home.this, mock_tests_list.class);
                startActivity(k);
//                replaceFragment(new MockTestsFragment());
            } else if (itemId == R.id.chat) {
                Intent j = new Intent(Home.this, ChatDashboard.class);
                j.putExtra("name", name);
//                replaceFragment(new ChatFragment());
                startActivity(j);
            }
            return true;
        });

        db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        plan = document.getString("plan");
                        name = document.getString("name");
                    }
                } else {
                    Log.d("harsh", "error1");

                }
            }
        });


    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}