package com.example.bartaapp.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.bartaapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusViewHolder extends RecyclerView.ViewHolder {
    public CircularStatusView circular_status_view;
    public CircleImageView statusImage;
    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        circular_status_view = itemView.findViewById(R.id.circular_status_view);
        statusImage = itemView.findViewById(R.id.statusImage);
    }
}
