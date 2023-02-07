package com.example.bartaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bartaapp.ContainerActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.model.User;
import com.example.bartaapp.viewholder.UserViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.usert_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.profileName.setText(user.getUser_name());
        Glide.with(context).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(holder.profileImage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ContainerActivity.class);
            intent.putExtra("key", user.getUser_id());
            intent.putExtra("chat", "chat");
            context.startActivity(intent);
        });

        holder.profileImage.setOnClickListener(view -> {
            Intent intent =new Intent(context, ContainerActivity.class);
            intent.putExtra("key", user.getUser_id());
            intent.putExtra("profile", "profile");
            context.startActivity(intent);
        });

        String currentUser = FirebaseAuth.getInstance().getUid();
        String friendUser = user.getUser_id();
        String senderRoom = currentUser+friendUser;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chats");
        databaseReference.child(senderRoom).child("lastMsgInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String lastMessage = snapshot.child("last_message").getValue(String.class);
                    long time = snapshot.child("message_time").getValue(Long.class);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    holder.msgTime.setText(simpleDateFormat.format(new Date(time)));
                    holder.lastMsg.setText(lastMessage);
                }else {
                    holder.lastMsg.setText("Tap to chat");
                    holder.msgTime.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
