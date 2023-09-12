package com.example.campusfriends;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorldChatActivity extends Fragment {

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<WorldChatMessage> chatMessageList;
    private WorldChatAdapter chatAdapter;
    private DatabaseReference chatMessagesRef;
    private static final String ARG_NAME = "name";
    private String name;

    public static WorldChatActivity newInstance(String name) {
        WorldChatActivity fragment = new WorldChatActivity();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_world_chat, container, false);

        Bundle args = getArguments();
        if (args != null) {
            name = args.getString(ARG_NAME);
        }

        recyclerViewChat = rootView.findViewById(R.id.recyclerViewChat);
        editTextMessage = rootView.findViewById(R.id.editTextMessage);
        buttonSend = rootView.findViewById(R.id.buttonSend);

        chatMessageList = new ArrayList<>();
        chatAdapter = new WorldChatAdapter(chatMessageList);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewChat.setAdapter(chatAdapter);

        // Get the reference to the "messages" node in Firebase Realtime Database
        chatMessagesRef = FirebaseDatabase.getInstance().getReference().child("WorldChat");

        // Set up ValueEventListener to listen for new chat messages
        chatMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    WorldChatMessage worldChatMessage = messageSnapshot.getValue(WorldChatMessage.class);
                    chatMessageList.add(worldChatMessage);
                }
                chatAdapter.notifyDataSetChanged();
                // Scroll to the bottom of the RecyclerView to see the latest message
                recyclerViewChat.scrollToPosition(chatMessageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error fetching messages: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Handle send button click
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = editTextMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(messageContent)) {
                    sendMessage(messageContent);
                }
            }
        });

        return rootView;
    }

    private void sendMessage(String messageContent) {
        String senderName = name; // Replace with the actual sender name (e.g., user's display name)
        long timestamp = System.currentTimeMillis();

        WorldChatMessage worldChatMessage = new WorldChatMessage(senderName, messageContent, timestamp);

        // Push the new chat message to Firebase Realtime Database
        chatMessagesRef.push().setValue(worldChatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Clear the message input after sending
                editTextMessage.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
