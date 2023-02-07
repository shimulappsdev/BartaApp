package com.example.bartaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bartaapp.adapter.FragmentsAdapter;
import com.example.bartaapp.fragments.ChattingFragment;
import com.example.bartaapp.fragments.FriendProfileFragment;
import com.example.bartaapp.fragments.ImageFragment;
import com.example.bartaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContainerActivity extends AppCompatActivity {

    ChattingFragment chattingFragment = new ChattingFragment();
    FriendProfileFragment friendProfileFragment = new FriendProfileFragment();
    ImageFragment imageFragment = new ImageFragment();
    Intent intent;
    String chat, profile, profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        intent = getIntent();
        chat = intent.getStringExtra("chat");
        profile = intent.getStringExtra("profile");
        profilepic = intent.getStringExtra("profilepic");

        if (chat != null){
            if (chat.equals("chat")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, chattingFragment).commit();
            }
        }
        if (profile != null){
            if (profile.equals("profile")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, friendProfileFragment).commit();
            }
        }
        if (profilepic != null){
            if (profilepic.equals("profilepic")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerActivity, imageFragment).commit();
            }
        }

    }
}