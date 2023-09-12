package com.example.campusfriends;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course_page extends AppCompatActivity {

    VideoView videoView;
    Toolbar toolbar1;
    HorizontalScrollView horizontalScroll1;
    TextView textView8, textView9;
    Button button2, button3;

    Button selected_button;

    FirebaseFirestore db;
    private String videoUrl;
    String video_name;

    private String university;
    private String group;
    private String semester;
    private String course;

    private List<String> pdf_names;

    private ListView listView;

    private LinearLayout linearLayout;

    MediaController mediaController;
    OrientationEventListener orientationEventListener;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView != null) {
            int currentPosition = videoView.getCurrentPosition();
            outState.putInt("currentPosition", currentPosition);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        toolbar1 = findViewById(R.id.toolbar1);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);
        linearLayout = findViewById(R.id.linearLayout);
        horizontalScroll1= findViewById(R.id.horizontalScroll1);

        mediaController = new MediaController(this);
        videoView = findViewById(R.id.videoView);

        pdf_names = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        if (savedInstanceState != null) {
            int savedPosition = savedInstanceState.getInt("currentPosition");
            videoView.seekTo(savedPosition);
        }

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                    // Device is in landscape mode
                    makeVideoFullscreen();
                } else {
                    // Device is in portrait mode
                    restoreVideoSize();
                }
            }
        };

        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }

        selected_button = button2;
        button2.setBackgroundResource(R.drawable.rounded_button_selected_course);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_button.setBackgroundResource(R.drawable.rounded_button_courses);
                selected_button = button2;
                selected_button.setBackgroundResource(R.drawable.rounded_button_selected_course);
                textView8.setVisibility(View.VISIBLE);
                textView9.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                videoView.start();
                listView.setVisibility(View.GONE);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_button.setBackgroundResource(R.drawable.rounded_button_courses);
                selected_button = button3;
                selected_button.setBackgroundResource(R.drawable.rounded_button_selected_course);
                textView8.setVisibility(View.GONE);
                textView9.setVisibility(View.GONE);
                videoView.pause();
                linearLayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                getPdfdoc(university, group, semester, course);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            String get_pdf_name = adapterView.getItemAtPosition(i).toString();
                            Intent display = new Intent(Course_page.this, pdfViewer.class);
                            display.putExtra("name_pdf", get_pdf_name);
                            startActivity(display);
                        }catch(Exception e){
                            Log.d("harsh", e.getMessage());
                        }
                    }
                });
            }
        });

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("description");
        university = i.getStringExtra("university");
        group = i.getStringExtra("group");
        semester = i.getStringExtra("semester");
        course = i.getStringExtra("course");

        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        textView9.setText(description);
        fetch_video();
    }

    public void getPdfdoc(String university, String group, String semester, String course){
        db.collection("Universities").document(university).collection("Groups").document(group).collection("Semesters").document(semester).collection("Courses").document(course).collection("PDF")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pdf_names.clear();
                        for(QueryDocumentSnapshot document : task.getResult()){
                            String pdf_name = document.getId();
                            pdf_names.add(pdf_name);
                        }
                        ArrayAdapter pdfAdapter = new ArrayAdapter(Course_page.this, android.R.layout.simple_list_item_1, pdf_names);
                        listView.setAdapter(pdfAdapter);
                    }
                });
    }

    private String fetch_video(){

        db.collection("Universities").document(university).collection("Groups").document(group).collection("Semesters").document(semester).collection("Courses").document(course)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                try {
                                    video_name = document.getString("video").trim();
                                }catch(Exception e){
                                    Log.d("harsh", e.getMessage());
                                }
                                if(video_name!=null) {
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference videoRef = storageRef.child("Videos/"+video_name); // Change this to your video's path
                                    try {
                                        videoRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    Uri downloadUri = task.getResult();
                                                    if (downloadUri != null) {
                                                        videoUrl = downloadUri.toString();
                                                        videoView.setVideoURI(Uri.parse(videoUrl));

                                                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                            @Override
                                                            public void onPrepared(MediaPlayer mediaPlayer) {
                                                                mediaController.setAnchorView(videoView);
                                                                videoView.setMediaController(mediaController);
                                                                videoView.start();
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(Course_page.this, "URL not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(Course_page.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        //        String videoPath = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
                                    }catch(Exception e){
                                        Log.d("harsh", e.toString());
                                    }
                                }else{
                                    Toast.makeText(Course_page.this, "Error fetching video!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("harsh", "error");
                            }
                        } else {
                            Log.d("harsh", "error1");
                        }
                    }
                });


        return videoUrl;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void makeVideoFullscreen() {
        // Get the video view
        VideoView videoView = findViewById(R.id.videoView);

        // Get the screen dimensions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // Update the video view's layout parameters
        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = screenHeight;
        videoView.setLayoutParams(params);

        toolbar1.setVisibility(View.GONE);
        horizontalScroll1.setVisibility(View.GONE);
        textView8.setVisibility(View.GONE);
        textView9.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        // Hide the status bar and make navigation bar semi-transparent
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void restoreVideoSize() {
        // Get the video view
        VideoView videoView = findViewById(R.id.videoView);

        toolbar1.setVisibility(View.VISIBLE);
        horizontalScroll1.setVisibility(View.VISIBLE);
        textView8.setVisibility(View.VISIBLE);
        textView9.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);

        // Restore the original layout parameters
        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        videoView.setLayoutParams(params);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

}