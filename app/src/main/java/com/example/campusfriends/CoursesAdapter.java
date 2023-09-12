package com.example.campusfriends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class CoursesAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final int resource;

    private final List<String> titleList;
    private final List<String> descList;
    private final List<String> planAccessList;
    private final String plan;
    private final String group;
    private final String semester;
    private final String university;
    private final List<String> courses;
    private final List<String> imageUrls;

    public CoursesAdapter(@NonNull Context mContext, int resource, @NonNull List<String> titleList, List<String> descList, String plan, List<String> planAccessList, String university, String group, String semester, List<String> courses, List<String> imageUrls) {
        super(mContext, resource, titleList);
        this.mContext = mContext;
        this.resource = resource;
        this.titleList = titleList;
        this.descList = descList;
        this.plan = plan;
        this.planAccessList = planAccessList;
        this.university = university;
        this.group = group;
        this.semester = semester;
        this.courses = courses;
        this.imageUrls = imageUrls;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return titleList.get(position);
    }
    public String getDesc(int position){
        return descList.get(position);
    }

    public String getPlanAccess(int position){
        return planAccessList.get(position);
    }
    public String getcourse(int position){
        return courses.get(position);
    }public String getimageurl(int position){
        return imageUrls.get(position);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.courses_card_items, parent, false);

        TextView title = convertView.findViewById(R.id.textView4);
        TextView description = convertView.findViewById(R.id.textView5);
        TextView planAccess = convertView.findViewById(R.id.textView6);
        ImageView imageView = convertView.findViewById(R.id.imageView2);
        CardView cardView = convertView.findViewById(R.id.cardView);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);

        title.setText(getItem(position));
        description.setText(getDesc(position));
//        imageView.setImageResource(R.drawable.test);
        try {
            Glide.with(mContext)
                    .load(getimageurl(position))
                    .skipMemoryCache(true)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }
        planAccess.setText(getPlanAccess(position));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getPlanAccess(position).equals(plan) || plan.equals("premium")) {
                    String course = getcourse(position);
//                    Toast.makeText(mContext, "Available Soon", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mContext, Course_page.class);
                    i.putExtra("title", getItem(position));
                    i.putExtra("description", getDesc(position));
                    i.putExtra("university", university);
                    i.putExtra("group", group);
                    i.putExtra("semester", semester);
                    i.putExtra("course", course);
                    mContext.startActivity(i);
                }
                else{
                    showAlertDialog();
                }
            }
        });
        return convertView;
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Access Forbidden"); // Set the title of the dialog
        alertDialogBuilder.setMessage("This is a premium content, you cannot access it as a free member"); // Set the message

        // Set positive button and its action
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the click action of the OK button (if needed)
                dialog.dismiss(); // Close the dialog
            }
        });

        // Set negative button and its action (optional)
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the click action of the Cancel button (if needed)
                dialog.dismiss(); // Close the dialog
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
