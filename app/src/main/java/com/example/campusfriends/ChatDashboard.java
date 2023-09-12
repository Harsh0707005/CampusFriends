package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campusfriends.databinding.ActivityChatDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatDashboard extends AppCompatActivity {

    ActivityChatDashboardBinding binding;
    BottomNavigationView bottomNavigationView2;
    FirebaseFirestore db;
    String plan, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dashboard);
        bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        binding = ActivityChatDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ChatActivity());

        Intent i = getIntent();
        String uid = FirebaseAuth.getInstance().getUid();
        name = i.getStringExtra("name");
//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        plan = i.getStringExtra("plan");
//        binding.bottomNavigationView.setSelectedItemId(2131231019);


        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.individual_chat) {
                replaceFragment(new ChatActivity());
            } else if (itemId == R.id.world_chat) {
                replaceFragment(WorldChatActivity.newInstance(name));
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}