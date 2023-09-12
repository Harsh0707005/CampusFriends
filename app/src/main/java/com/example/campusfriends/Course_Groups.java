package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Course_Groups extends AppCompatActivity {

    GroupsAdapter groupsAdapter;
    GridView gridView2;
    Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_groups);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> data = new ArrayList<>();
        gridView2 = findViewById(R.id.gridView2);
        toolbar1 = findViewById(R.id.toolbar1);

        Intent i =  getIntent();
        String university = i.getStringExtra("university");


        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(university);


        db.collection("Universities").document(university).collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Process the list of documents
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String group_name = document.getId();
                                data.add(group_name);
                                groupsAdapter = new GroupsAdapter(Course_Groups.this, R.layout.group_card_items, data, university);
                                gridView2.setAdapter(groupsAdapter);
                            }
                        } else {
                            Log.d("harsh", "error occurred");
                        }
                    }
                });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}