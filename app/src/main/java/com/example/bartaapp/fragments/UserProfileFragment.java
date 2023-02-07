package com.example.bartaapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bartaapp.ContainerActivity;
import com.example.bartaapp.LoginActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.databinding.FragmentUserProfileBinding;
import com.example.bartaapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUser, coverePic, profilePic, profileName;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(getLayoutInflater(), container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (currentUser != null){
                        User user = snapshot.getValue(User.class);
                        binding.profileName.setText(user.getUser_name());
                        binding.profilePhone.setText(user.getUser_phone());
                        binding.profileEmail.setText(user.getUser_email());
                        binding.profileBioTV.setText(user.getUser_bio());
                        Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.profileImage);
                        Glide.with(getActivity()).load(user.getUser_cover()).placeholder(R.drawable.profilehint).into(binding.coverImage);

                        profilePic = user.getUser_profile();
                        coverePic = user.getUser_cover();
                        profileName = user.getUser_name();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.profileImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("profilePicKey", profilePic);
            intent.putExtra("nameKey", profileName);
            intent.putExtra("profilepic", "profilepic");
            startActivity(intent);
        });

        binding.coverImage.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContainerActivity.class);
            intent.putExtra("coverPicKey", coverePic);
            intent.putExtra("nameKey", profileName);
            intent.putExtra("profilepic", "profilepic");
            startActivity(intent);
        });

        binding.logoutCard.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Logout");
            builder.setMessage("Are you sure?");
            builder.setIcon(R.drawable.logout);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        return binding.getRoot();
    }
}