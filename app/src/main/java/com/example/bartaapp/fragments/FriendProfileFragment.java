package com.example.bartaapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bartaapp.ContainerActivity;
import com.example.bartaapp.MainActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.databinding.FragmentFriendProfileBinding;
import com.example.bartaapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendProfileFragment extends Fragment {

    FragmentFriendProfileBinding binding;
    DatabaseReference databaseReference;
    String friendUserId, name, friendProfilePic, friendCoverPic;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendProfileBinding.inflate(getLayoutInflater(), container, false);

        intent = getActivity().getIntent();
        friendUserId = intent.getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(friendUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (friendUserId.equals(user.getUser_id())){
                    binding.friendProfileName.setText(user.getUser_name());
                    binding.friemdProfilePhone.setText(user.getUser_phone());
                    binding.friendProfileEmail.setText(user.getUser_email());
                    binding.friendProfileBio.setText(user.getUser_bio());
                    Glide.with(getActivity()).load(user.getUser_cover()).placeholder(R.drawable.profilehint).into(binding.friendCoverImage);
                    Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.friendProfileImage);
                    name = user.getUser_name();
                    friendProfilePic = user.getUser_profile();
                    friendCoverPic = user.getUser_cover();
                    binding.createGroupTV.setText("Create Group with "+name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.friendProfileImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profilePicKey", friendProfilePic);
            intent.putExtra("nameKey", name);
            intent.putExtra("profilepic", "profilepic");
            startActivity(intent);
        });

        binding.friendCoverImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("coverPicKey", friendCoverPic);
            intent.putExtra("nameKey", name);
            intent.putExtra("profilepic", "profilepic");
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });


        return binding.getRoot();
    }
}