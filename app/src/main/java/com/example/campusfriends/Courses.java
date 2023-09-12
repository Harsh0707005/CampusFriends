package com.example.campusfriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Courses extends AppCompatActivity {

    TextView textView, textView7;
    Toolbar toolbar;
    LinearLayout LinearLayoutButtons;
    Button selectedButton;
    GridView gridView;

    ProgressBar progressBar3;
    List<String> titleList;
    List<String> descList;
    List<String> courses;
    List<String> imageUrls;
    String plan;
    String title;
    String description;
    String planAccess;
    String course;
    String image;
    List<String> planAccessList;
    FirebaseStorage storage;
//    StorageReference imageRef;
    StorageReference storageRef;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        toolbar = findViewById(R.id.toolbar1);
        textView = findViewById(R.id.textView3);
        textView7 = findViewById(R.id.textView7);
        progressBar3 = findViewById(R.id.progressBar3);

        titleList = new ArrayList<>();
        descList = new ArrayList<>();
        planAccessList = new ArrayList<>();
        courses = new ArrayList<>();
        imageUrls = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Courses");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        try {
            db.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            plan = document.getString("plan");
                        } else {
                            Log.d("harsh", "error fetching plan");
                        }
                    } else {
                        Toast.makeText(Courses.this, "Sure", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Log.d("harsh", e.getMessage());
        }

        LinearLayoutButtons = findViewById(R.id.LinearLayoutButtons);

        Intent i = getIntent();
        String group = i.getStringExtra("group");
        String university = i.getStringExtra("university");
        textView.setText(group);

        db = FirebaseFirestore.getInstance();
        gridView = findViewById(R.id.gridView2);
        db.collection("Universities").document(university).collection("Groups").document(group).collection("Semesters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isFirstButton = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String semester = document.getId();
                                Button newButton = new Button(Courses.this);
                                newButton.setText(semester);
                                if(isFirstButton){
                                    newButton.setBackgroundResource(R.drawable.rounded_button_selected_course);
                                    isFirstButton = false;
                                    selectedButton = newButton;
                                    progressBar3.setVisibility(View.VISIBLE);
                                    addToCourseList(university, group, semester, titleList, descList);

                                }else {
                                    newButton.setBackgroundResource(R.drawable.rounded_button_courses);
                                }
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                newButton.setLayoutParams(layoutParams);
                                int paddingInDp = 10;
                                float scale = getResources().getDisplayMetrics().density;
                                int paddingInPx = (int) (paddingInDp * scale + 0.5f);
                                newButton.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
                                int marginInDp = 5;
                                int marginInPx = (int) (marginInDp * getResources().getDisplayMetrics().density + 0.5f);
                                layoutParams.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
                                newButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gridView.setVisibility(View.GONE);
                                        if (selectedButton != null) {
                                            selectedButton.setBackgroundResource(R.drawable.rounded_button_courses);
                                        }
                                        Button clickedButton = (Button) view;

                                        selectedButton = (Button) view;
                                        selectedButton.setBackgroundResource(R.drawable.rounded_button_selected_course);
                                        String buttonText = clickedButton.getText().toString();
                                        progressBar3.setVisibility(View.VISIBLE);
                                        addToCourseList(university, group, semester, titleList, descList);

                                    }
                                });
                                LinearLayoutButtons.addView(newButton);
                            }
                        } else {
                            Log.d("harsh", "error occurred");
                        }

                    }
                });
    }
    public void addToCourseList (String university,String group, String semester, List<String> titleList, List<String> descList){
        db.collection("Universities").document(university).collection("Groups").document(group).collection("Semesters").document(semester).collection("Courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        titleList.clear();
                        descList.clear();
                        courses.clear();
                        planAccessList.clear();
                        imageUrls.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            course = document.getId();
                            try {
                                title = document.getString("name").trim();
                                description = document.getString("description").trim();
                                planAccess = document.getString("plan-access").trim();
                                image = document.getString("image").trim();
                            }catch (Exception e){
                                Log.d("harsh", e.getMessage());
                            }

                            try {
                                StorageReference storageRef = storage.getReference("Course Images/" + image);
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        Log.d("harsh", imageUrl);
                                        imageUrls.add(imageUrl);
                                    }
                                });
                            }catch (Exception e){
                                Log.d("harsh", e.getMessage());
                            }

                            courses.add(course);
                            titleList.add(title);
                            descList.add(description);
                            planAccessList.add(planAccess);
                        }
                        if(title != null) {
                            gridView.setVisibility(View.VISIBLE);
                            textView7.setVisibility(View.GONE);
                            progressBar3.setVisibility(View.GONE);
                            CoursesAdapter myadapter = new CoursesAdapter(Courses.this, R.layout.courses_card_items, titleList, descList, plan, planAccessList, university, group, semester, courses, imageUrls);
                            gridView.setAdapter(myadapter);
                        }else {
                            gridView.setVisibility(View.GONE);
                            textView7.setVisibility(View.VISIBLE);
                            progressBar3.setVisibility(View.GONE);
                            textView7.setText("No Courses to Display");
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