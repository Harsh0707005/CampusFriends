package com.example.campusfriends;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class GroupsAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final int resource;

    private final List<String> data;
    private final String university;

    public GroupsAdapter(@NonNull Context mContext, int resource, @NonNull List<String> data, String university) {
        super(mContext, resource, data);
        this.mContext = mContext;
        this.resource = resource;
        this.data = data;
        this.university = university;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_card_items, parent, false);
        Button button = convertView.findViewById(R.id.button);
        button.setText(data.get(position));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, Courses.class);
                i.putExtra("group", data.get(position));
                i.putExtra("university", university);
                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}