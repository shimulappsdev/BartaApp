package com.example.bartaapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bartaapp.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView messageTV;
    public ImageView feelings;
    public CardView fileImageCard;
    public ImageView fileImage;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        messageTV = itemView.findViewById(R.id.messageTV);
        feelings = itemView.findViewById(R.id.feelings);
        fileImageCard = itemView.findViewById(R.id.fileImageCard);
        fileImage = itemView.findViewById(R.id.fileImage);
    }
}
