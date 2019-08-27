package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.lamamia_project.CourseMainActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.RatingActivity;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.model.ListItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private ArrayList<ListItem> listItems;
    private Context context;

    public CourseAdapter(ArrayList<ListItem> courses_modelList, Context context) {
        this.listItems = courses_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ListItem listItem = listItems.get(position);
        String course_name = listItem.getCourseName();
        final String course_id = listItem.getCourseId();

        holder.txtCourseName.setText(course_name);
        Picasso.get().load(listItem.getCourseImgaeMedium()).fit().into(holder.imgCourseImage);

        holder.courseViews.setText(Html.fromHtml(listItem.getCourse_views()));
        holder.ratingText.setText(Html.fromHtml(listItem.getCourse_average_rating()));
        holder.courseReviews.setText(Html.fromHtml(listItem.getCourse_reviews()));
        holder.courseShareCount.setText(listItem.getCourse_share());

        holder.linCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent activityChangeIntent = new Intent(context, CourseMainActivity.class);
                activityChangeIntent.putExtra("course_id", course_id);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        holder.courseShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://lamamia.in/api/public/course?course_id="+course_id);
                Intent chooserIntent = Intent.createChooser(sharingIntent, "Share using");
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(chooserIntent);

                String url = Constants.POST_SHARE_COUNT + course_id;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                holder.rq.add(jsonObjectRequest);
            }
        });

        holder.courseRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent activityChangeIntent = new Intent(context, RatingActivity.class);
                activityChangeIntent.putExtra("course_id", course_id);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });

        holder.courseReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent activityChangeIntent = new Intent(context, RatingActivity.class);
                activityChangeIntent.putExtra("course_id", course_id);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityChangeIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtCourseName, ratingText, courseViews, courseReviews, courseShareCount;
        private ImageView imgCourseImage;
        private LinearLayout linCourses, courseShare, courseRating, courseReview;
        public RequestQueue rq;

        public ViewHolder(View itemView) {
            super(itemView);
            linCourses = itemView.findViewById(R.id.linearLayoutCourses);
            imgCourseImage = itemView.findViewById(R.id.imageViewCourseItemImage);
            txtCourseName = itemView.findViewById(R.id.textViewCourseItemName);
            ratingText = itemView.findViewById(R.id.courseItemRatingText);
            courseViews = itemView.findViewById(R.id.courseItemViews);
            courseReviews = itemView.findViewById(R.id.courseItemReviews);
            courseShareCount = itemView.findViewById(R.id.userCourseTextShareCount);
            courseShare = itemView.findViewById(R.id.course_share);
            courseReview = itemView.findViewById(R.id.course_comment);
            courseRating = itemView.findViewById(R.id.course_rating);
            rq = Volley.newRequestQueue(context);
        }
    }
}
