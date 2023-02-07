package com.example.bartaapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bartaapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profileImage;
    public TextView profileName, lastMsg, msgTime;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImage);
        profileName = itemView.findViewById(R.id.profileName);
        lastMsg = itemView.findViewById(R.id.lastMsg);
        msgTime = itemView.findViewById(R.id.msgTime);
    }
}
