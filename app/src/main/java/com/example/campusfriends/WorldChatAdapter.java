package com.example.campusfriends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorldChatAdapter extends RecyclerView.Adapter<WorldChatAdapter.ChatViewHolder> {

    private List<WorldChatMessage> worldChatMessageList;

    public WorldChatAdapter(List<WorldChatMessage> worldChatMessageList) {
        this.worldChatMessageList = worldChatMessageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        WorldChatMessage worldChatMessage = worldChatMessageList.get(position);
        holder.senderNameTextView.setText(worldChatMessage.getSenderName());
        holder.messageContentTextView.setText(worldChatMessage.getMessageContent());
        // Set timestamp or any other UI components you want to display for each chat message
    }

    @Override
    public int getItemCount() {
        return worldChatMessageList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderNameTextView;
        TextView messageContentTextView;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderNameTextView = itemView.findViewById(R.id.senderNameTextView);
            messageContentTextView = itemView.findViewById(R.id.messageContentTextView);
        }
    }
}

