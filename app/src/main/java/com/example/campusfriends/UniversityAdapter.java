package com.example.campusfriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class UniversityAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int layoutResourceId;
    private List<String> data = new ArrayList<>();

    public UniversityAdapter(Context mContext, int layoutResourceId, List<String> data) {
        super(mContext, layoutResourceId, data);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            button = row.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, Course_Groups.class);
                    i.putExtra("university", data.get(position));
                    mContext.startActivity(i);
                }
            });

            row.setTag(button);
        } else {
            button = (Button) row.getTag();
        }

        String item = data.get(position);

        button.setText(item);
        return row;
    }

    private static class ViewHolder {
        Button button;
    }
}
