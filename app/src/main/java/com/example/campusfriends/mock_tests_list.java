package com.example.campusfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class mock_tests_list extends AppCompatActivity {

    FirebaseFirestore db;
    List<String> pdf_names;
    List<String> mocks;

    ListView mock_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_tests_list);

        mock_names = findViewById(R.id.mock_names);

        pdf_names = new ArrayList<>();
        mocks = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        getMockNames();
    }
    public void getMockNames(){
        try {
            db.collection("Mock Tests")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            mocks.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String mock = document.getId();
                                mocks.add(mock);
                            }
                            ArrayAdapter mockAdapter = new ArrayAdapter(mock_tests_list.this, android.R.layout.simple_list_item_1, mocks);
                            mock_names.setAdapter(mockAdapter);

                            mock_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                    Toast.makeText(mock_tests_list.this, mock_names.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(mock_tests_list.this, MockTest.class);
                                    i.putExtra("name", mock_names.getItemAtPosition(position).toString());
                                    startActivity(i);
                                }
                            });


                        }
                    });
        }catch (Exception e){
            Log.d("harsh", e.getMessage());
        }
    }
}