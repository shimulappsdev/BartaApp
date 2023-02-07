package com.example.bartaapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bartaapp.ContainerActivity;
import com.example.bartaapp.MainActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.adapter.ChatAdapter;
import com.example.bartaapp.databinding.FragmentChattingBinding;
import com.example.bartaapp.model.Chat;
import com.example.bartaapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingFragment extends Fragment {

    FragmentChattingBinding binding;
    DatabaseReference databaseReference, database;
    StorageReference storageReference;
    Intent intent;
    List<Chat> chatList;
    ProgressDialog dialog;
    String friendUserId, currentUser, senderRoom, receiverRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChattingBinding.inflate(getLayoutInflater(), container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(binding.toolbar.getRoot());
        activity.getSupportActionBar().setTitle("");

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching...");
        dialog.setMessage("Please Wait!");
        dialog.setCancelable(false);

        chatList = new ArrayList<>();
        intent = getActivity().getIntent();

        friendUserId = intent.getStringExtra("key");
        currentUser = FirebaseAuth.getInstance().getUid();

        senderRoom = currentUser+friendUserId;
        receiverRoom = friendUserId+currentUser;

        DatabaseReference databaseRefer = FirebaseDatabase.getInstance().getReference().child("presens").child(friendUserId);
        databaseRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String indicate = snapshot.getValue(String.class);
                    if (!indicate.isEmpty()){
                        if (indicate.equals("offline")){
                            binding.indicate.setVisibility(View.GONE);
                        }else {
                            binding.indicate.setText(indicate);
                            binding.indicate.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database = FirebaseDatabase.getInstance().getReference().child("chats");
        storageReference = FirebaseStorage.getInstance().getReference("user_images").child(currentUser);

        binding.messageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.sendBtn.setVisibility(View.VISIBLE);
                binding.voiceBtn.setVisibility(View.GONE);
                return false;
            }
        });

        binding.sendBtn.setOnClickListener(view -> {
            MessageSend();
            binding.sendBtn.setVisibility(View.GONE);
            binding.voiceBtn.setVisibility(View.VISIBLE);
        });

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        binding.friendProfileInfo.setOnClickListener(view -> {
            Intent intent =new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("key", friendUserId);
            intent.putExtra("profile", "profile");
            startActivity(intent);
        });

        binding.addFileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 500);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.child(friendUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (friendUserId.equals(user.getUser_id())){
                    binding.friendProfileName.setText(user.getUser_name());
                    Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.friendProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child(senderRoom).child("chatting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (chat.getReceiverId().equals(currentUser) && chat.getSenderId().equals(friendUserId) || chat.getReceiverId().equals(friendUserId) && chat.getSenderId().equals(currentUser)){
                        chatList.add(chat);
                    }
                }
                setChatToRecyclerView(chatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        database.child(receiverRoom).child("chatting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (chat.getReceiverId().equals(currentUser) && chat.getSenderId().equals(friendUserId) || chat.getReceiverId().equals(friendUserId) && chat.getSenderId().equals(currentUser)){
                        chatList.add(chat);
                    }
                }
                setChatToRecyclerView(chatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        final Handler handler = new Handler();
        binding.messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                DatabaseReference databaseRefee = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
                databaseRefee.setValue("Typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userTypeingStop,1000);

            }
            Runnable userTypeingStop = new Runnable() {
                @Override
                public void run() {
                    DatabaseReference databaseRefee = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
                    databaseRefee.setValue("online");
                }
            };
        });

        return binding.getRoot();
    }

    private void setChatToRecyclerView(List<Chat> chatList) {
        ChatAdapter adapter = new ChatAdapter(getActivity(),chatList,currentUser, senderRoom, receiverRoom);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        binding.chattingRecyclerView.setLayoutManager(layoutManager);
        binding.chattingRecyclerView.setAdapter(adapter);
    }

    private void MessageSend() {
        Date date = new Date();
        long msgTime = date.getTime();
        String message = binding.messageInput.getText().toString();
        String chatKey = database.push().getKey();

        Chat chat = new Chat(currentUser, friendUserId, message, chatKey, msgTime, -1);
        database.child(senderRoom).child("chatting").child(chatKey).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if (task.isSuccessful()){
                 Toast.makeText(getActivity(), "Message Send", Toast.LENGTH_SHORT).show();
                 binding.messageInput.setText("");

                 Map<String, Object> lastMsgMap = new HashMap<>();
                 lastMsgMap.put("last_message",message);
                 lastMsgMap.put("message_time",msgTime);
                 database.child(senderRoom).child("lastMsgInfo").setValue(lastMsgMap);

             }
            }
        });
        database.child(receiverRoom).child("chatting").child(chatKey).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Message Send", Toast.LENGTH_SHORT).show();
                    binding.messageInput.setText("");

                    Map<String, Object> lastMsgMap = new HashMap<>();
                    lastMsgMap.put("last_message",message);
                    lastMsgMap.put("message_time",msgTime);
                    database.child(receiverRoom).child("lastMsgInfo").setValue(lastMsgMap);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500){
            if (data != null){
                if (data.getData() != null){
                    Uri selectedImg = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference storage = storageReference.child("chat_img").child(calendar.getTimeInMillis()+"");
                    dialog.show();
                    storage.putFile(selectedImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){

                                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String selectedImgUrl = uri.toString();

                                        Date date = new Date();
                                        long msgTime = date.getTime();
                                        String message = binding.messageInput.getText().toString();
                                        String chatKey = database.push().getKey();

                                        Chat chat = new Chat(currentUser, friendUserId, message, chatKey, msgTime, -1);
                                        chat.setImgUrl(selectedImgUrl);
                                        chat.setMessage("photo");
                                        database.child(senderRoom).child("chatting").child(chatKey).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Message Send", Toast.LENGTH_SHORT).show();
                                                    binding.messageInput.setText("");

                                                    Map<String, Object> lastMsgMap = new HashMap<>();
                                                    lastMsgMap.put("last_message",message);
                                                    lastMsgMap.put("message_time",msgTime);
                                                    database.child(senderRoom).child("lastMsgInfo").setValue(lastMsgMap);

                                                }
                                            }
                                        });
                                        database.child(receiverRoom).child("chatting").child(chatKey).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Message Send", Toast.LENGTH_SHORT).show();
                                                    binding.messageInput.setText("");

                                                    Map<String, Object> lastMsgMap = new HashMap<>();
                                                    lastMsgMap.put("last_message",message);
                                                    lastMsgMap.put("message_time",msgTime);
                                                    database.child(receiverRoom).child("lastMsgInfo").setValue(lastMsgMap);
                                                }
                                            }
                                        });


                                    }
                                });

                            }
                        }
                    });
                }
            }

        }

    }
    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference databaseRefee = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
        databaseRefee.setValue("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
        databaseRef.setValue("offline");
    }
}
