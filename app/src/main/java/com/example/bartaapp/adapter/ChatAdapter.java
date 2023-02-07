package com.example.bartaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bartaapp.R;
import com.example.bartaapp.model.Chat;
import com.example.bartaapp.viewholder.ChatViewHolder;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private Context context;
    private List<Chat> chatList;
    private String myUserId;
    final int LEFT_LAYOUT = 1;
    final int RIGHT_LAYOUT = 2;

    String senderRoom;
    String receiverRoom;

    public ChatAdapter(Context context, List<Chat> chatList, String myUserId, String senderRoom, String receiverRoom) {
        this.context = context;
        this.chatList = chatList;
        this.myUserId = myUserId;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_LAYOUT){
            view = LayoutInflater.from(context).inflate(R.layout.message_send_layout,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.message_receive_layout,parent,false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        if (chat.getMessage().equals("photo")){
            holder.fileImageCard.setVisibility(View.VISIBLE);
            holder.messageTV.setVisibility(View.GONE);
            Glide.with(context).load(chat.getImgUrl()).placeholder(R.drawable.imagehint).into(holder.fileImage);
        }else {
            holder.fileImageCard.setVisibility(View.GONE);
        }

        int reaction [] = new int[]{
                R.drawable.like,
                R.drawable.love,
                R.drawable.lovekiss,
                R.drawable.lovesmile,
                R.drawable.wow,
                R.drawable.laugh,
                R.drawable.kiss,
                R.drawable.angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reaction)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (pos<0){
                return false;
            }
            chat.setFeelings(pos);
            String chatKey = chat.getChatKey();
            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(senderRoom)
                    .child("chatting")
                    .child(chatKey).setValue(chat);
            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(receiverRoom)
                    .child("chatting")
                    .child(chatKey).setValue(chat);
            return true;
        });

        if (chat.getFeelings() >= 0){
            holder.feelings.setImageResource(reaction[chat.getFeelings()]);
            holder.feelings.setVisibility(View.VISIBLE);
        }
        else {
            holder.feelings.setVisibility(View.GONE);
        }

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popup.onTouch(view, motionEvent);
                return false;
            }
        });

        holder.messageTV.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getSenderId().equals(myUserId)){
            return RIGHT_LAYOUT;
        }else {
            return LEFT_LAYOUT;
        }
    }
}
