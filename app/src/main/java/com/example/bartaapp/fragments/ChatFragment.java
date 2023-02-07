package com.example.bartaapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bartaapp.R;
import com.example.bartaapp.adapter.StatusAdapter;
import com.example.bartaapp.adapter.UserAdapter;
import com.example.bartaapp.databinding.FragmentChatBinding;
import com.example.bartaapp.model.Status;
import com.example.bartaapp.model.User;
import com.example.bartaapp.model.UserStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    public ChatFragment() {
    }

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String currentUser;
    List<User> userList;
    List<UserStatus> userStatusList;

    private FragmentChatBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(getLayoutInflater(), container, false);

        binding.shimmer.startShimmer();

        userList = new ArrayList<>();
        userStatusList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }
        if (currentUser != null){
            databaseReference = FirebaseDatabase.getInstance().getReference("user");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null){
                            if (!currentUser.equals(user.getUser_id())){
                                userList.add(user);
                            }
                        }
                    }
                    UserAdapter adapter = new UserAdapter(requireActivity(),userList);
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("status");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    userStatusList.clear();

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        UserStatus userStatus = new UserStatus();
                        userStatus.setName(dataSnapshot.child("name").getValue(String.class));
                        userStatus.setProfileImage(dataSnapshot.child("profileImage").getValue(String.class));
                        userStatus.setLastUpdate(dataSnapshot.child("lastUpdate").getValue(Long.class));

                        List<Status> statusList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1: dataSnapshot.child("statuses").getChildren()){
                            Status status = dataSnapshot1.getValue(Status.class);
                            statusList.add(status);
                        }
                        userStatus.setStatusList(statusList);
                        userStatusList.add(userStatus);
                    }
                    StatusAdapter statusAdapter = new StatusAdapter(getContext(),userStatusList);
                    binding.shimmer.stopShimmer();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.statusRecyclerView.setVisibility(View.VISIBLE);
                    binding.statusRecyclerView.setAdapter(statusAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
        databaseRef.setValue("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("presens").child(currentUser);
        databaseRef.setValue("offline");
    }
}