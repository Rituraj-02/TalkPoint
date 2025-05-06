package com.example.talkpoint.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talkpoint.Model.Story;
import com.example.talkpoint.R;
import com.example.talkpoint.databinding.StoryRbDesignBinding;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder>{

    ArrayList<Story> list;
    Context context;

    public StoryAdapter(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //used to take the design that we have created for recycler view
        View view= LayoutInflater.from(context).inflate(R.layout.story_rb_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        //used to set the data for all the view
        Story story = list.get(position);
//        holder.storyImg.setImageResource(model.getStory());
//        holder.profile.setImageResource(model.getProfile());
//        holder.storyType.setImageResource(model.getStoryType());
//        holder.name.setText(model.getName());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
//        ImageView storyImg,profile,storyType;
//        TextView name;

        StoryRbDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding= StoryRbDesignBinding.bind(itemView);




//            storyImg=itemView.findViewById(R.id.story);
//            profile=itemView.findViewById(R.id.profileImage);
//            storyType=itemView.findViewById(R.id.storyType);
//            name=itemView.findViewById(R.id.name);

        }
    }
}
