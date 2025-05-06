package com.example.talkpoint.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkpoint.CommentActivity;
import com.example.talkpoint.Model.Notification;
import com.example.talkpoint.R;
import com.example.talkpoint.User;
import com.example.talkpoint.databinding.Notification2sampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{


    ArrayList<Notification> list;
    Context context;

    public NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification2sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        Notification notification = list.get(position);

        String type = notification.getType();

        String notificationBy = notification.getNotificationBy();

        if (notificationBy == null || notificationBy.isEmpty()) {
            Log.e("NotificationAdapter", "notificationBy is null or empty for position: " + position);
            return;
        }

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {  // Check if user data exists
                            User user = snapshot.getValue(User.class);
                            if (user != null) {   // Check if user is not null
                                Picasso.get().load(user.getProfile())
                                        .placeholder(R.drawable.profile)
                                        .into(holder.binding.profileImage);
                                if(type.equals("like")){
                                    holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>" + " liked your post"));
                                } else if(type.equals("comment")){
                                    holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>" + " commented on your post"));
                                } else {
                                    holder.binding.notify.setText(Html.fromHtml("<b>"+user.getName()+"</b>" + " started following you"));
                                }
                            } else {
                                holder.binding.notify.setText("Unknown user"); // Prevent crash
                            }
                        } else {
                            holder.binding.notify.setText("User data missing"); // Prevent crash
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("follow")) {
                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (notification.getPostId() != null && notification.getPostBy() != null) { // Check for null values
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("postId", notification.getPostId());
                        intent.putExtra("postedBy", notification.getPostBy());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Error: Post data missing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        Notification2sampleBinding binding;
//
//        ImageView profile;
//        TextView notification,time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = Notification2sampleBinding.bind(itemView);

//            profile = itemView.findViewById(R.id.profileImage);
//            notification = itemView.findViewById(R.id.notify);
//            time = itemView.findViewById(R.id.time);
        }
    }
}
