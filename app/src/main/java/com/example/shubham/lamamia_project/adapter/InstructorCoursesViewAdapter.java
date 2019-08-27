package com.example.shubham.lamamia_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.shubham.lamamia_project.InstructorCoursePublishActivity;
import com.example.shubham.lamamia_project.InstructorPreviewCourseActivity;
import com.example.shubham.lamamia_project.R;
import com.example.shubham.lamamia_project.RatingActivity;
import com.example.shubham.lamamia_project.Utils.Constants;
import com.example.shubham.lamamia_project.model.CoursesListViewInstructor;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.ArrayList;

public class InstructorCoursesViewAdapter extends RecyclerView.Adapter<InstructorCoursesViewAdapter.ViewHolder>{

        private ArrayList<CoursesListViewInstructor> listItems;
        private Context context;

        public InstructorCoursesViewAdapter(ArrayList<CoursesListViewInstructor> courses_modelList, Context context) {
            this.listItems = courses_modelList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_view_instructor, parent, false);
            return new ViewHolder(v);
        }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final CoursesListViewInstructor listItem = listItems.get(position);
        String course_name = listItem.getCourseName();
        holder.txtCourseName.setText(course_name);
        Picasso.get().load(listItem.getCourseImgaeMedium()).fit().into(holder.imgCourseImage);
        holder.courseViews.setText(Html.fromHtml(listItem.getCourse_views()));
        holder.ratingText.setText(Html.fromHtml(listItem.getCourse_average_rating()));
        holder.courseReviews.setText(Html.fromHtml(listItem.getCourse_reviews()));
        holder.courseShareCount.setText(listItem.getCourse_share());

            // View course
            holder.linCourses.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent activityChangeIntent = new Intent(context, CourseMainActivity.class);
                    activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            });

            // Share course
            holder.courseShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://lamamia.in/api/public/course?course_id="+listItem.getCourseId());
                    Intent chooserIntent = Intent.createChooser(sharingIntent, "Share using");
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(chooserIntent);

                    String url = Constants.POST_SHARE_COUNT + listItem.getCourseId();
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

            // Course Rating
            holder.courseRating.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent activityChangeIntent = new Intent(context, RatingActivity.class);
                    activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            });

            // Course reviews
            holder.courseReview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent activityChangeIntent = new Intent(context, RatingActivity.class);
                    activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            });

            // Course view
            holder.course_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent activityChangeIntent = new Intent(context, CourseMainActivity.class);
                    activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            });

            // Course revise
            holder.course_revise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add module -> add video/quiz
                    Intent activityChangeIntent = new Intent(context, InstructorPreviewCourseActivity.class);
                    activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                    activityChangeIntent.putExtra("course_name", listItem.getCourseName());
                    activityChangeIntent.putExtra("course_image", listItem.getCourseImgaeMedium());
                    activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityChangeIntent);
                }
            });


            if (listItem.getStatus().equals("3")){

                // Course publish
                holder.course_publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Redirect to course publish for rules
                        Intent activityChangeIntent = new Intent(context, InstructorCoursePublishActivity.class);
                        activityChangeIntent.putExtra("course_id", listItem.getCourseId());
                        activityChangeIntent.putExtra("course_name", listItem.getCourseName());
                        activityChangeIntent.putExtra("course_image", listItem.getCourseImgaeMedium());
                        activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activityChangeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(activityChangeIntent);
                    }
                });

            }else{

                holder.course_publish.setVisibility(View.GONE);

            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtCourseName, ratingText, courseViews, courseReviews, courseShareCount;
            private ImageView imgCourseImage;
            private LinearLayout linCourses, courseShare, courseRating, courseReview;
            private Button course_view, course_revise, course_publish;
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

                course_view = itemView.findViewById(R.id.course_preview);
                course_revise = itemView.findViewById(R.id.course_revise);
                course_publish = itemView.findViewById(R.id.course_publish);

                rq = Volley.newRequestQueue(context);
            }
        }
}
