package com.example.bartaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bartaapp.MainActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.fragments.ChatFragment;
import com.example.bartaapp.model.Status;
import com.example.bartaapp.model.UserStatus;
import com.example.bartaapp.viewholder.StatusViewHolder;

import java.util.ArrayList;
import java.util.List;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {
    Context context;
    List<UserStatus> userStatusList;

    public StatusAdapter(Context context, List<UserStatus> userStatusList) {
        this.context = context;
        this.userStatusList = userStatusList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusViewHolder(LayoutInflater.from(context).inflate(R.layout.status_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

        UserStatus userStatus = userStatusList.get(position);

        Status lastStatus = userStatus.getStatusList().get(userStatus.getStatusList().size()-1);
        Glide.with(context).load(lastStatus.getImageUrl()).placeholder(R.drawable.profilehint).into(holder.statusImage);
        holder.circular_status_view.setPortionsCount(userStatus.getStatusList().size());

        holder.circular_status_view.setOnClickListener(view -> {
            ArrayList<MyStory> myStories = new ArrayList<>();
            for (Status status: userStatus.getStatusList()){
                myStories.add(new MyStory(status.getImageUrl()));
            }

            new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                    .setStoriesList(myStories) // Required
                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                    .setTitleText(userStatus.getName()) // Default is Hidden
                    .setSubtitleText("") // Default is Hidden
                    .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            //your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {
                            //your action
                        }
                    }) // Optional Listeners
                    .build() // Must be called before calling show method
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return userStatusList.size();
    }
}
