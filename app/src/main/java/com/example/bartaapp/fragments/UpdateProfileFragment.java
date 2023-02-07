package com.example.bartaapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bartaapp.R;
import com.example.bartaapp.databinding.FragmentUpdateProfileBinding;
import com.example.bartaapp.databinding.FragmentUserProfileBinding;
import com.example.bartaapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileFragment extends Fragment {

    FragmentUpdateProfileBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri coverImgUri, profileImgUri;
    String currentUser, coverImgUrl, profileImgUrl, name, phone, bio;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateProfileBinding.inflate(getLayoutInflater(), container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Updating...");
        dialog.setMessage("Please Wait....");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        binding.profileEmail.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Can't Update", Toast.LENGTH_SHORT).show();
        });
        binding.profilePassword.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "Can't Update", Toast.LENGTH_SHORT).show();
        });

        binding.updateProfileImage.setImageResource(R.drawable.profilehint);
        binding.updateCoverImage.setImageResource(R.drawable.profilehint);

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        storageReference = FirebaseStorage.getInstance().getReference("user_images").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (currentUser != null){
                    User user = snapshot.getValue(User.class);
                    binding.profileName.setText(user.getUser_name());
                    binding.profilePhone.setText(user.getUser_phone());
                    binding.profileEmail.setText(user.getUser_email());
                    binding.profilePassword.setText(user.getUser_password());
                    binding.profileBio.setText(user.getUser_bio());
                    Glide.with(getActivity()).load(user.getUser_profile()).placeholder(R.drawable.profilehint).into(binding.updateProfileImage);
                    Glide.with(getActivity()).load(user.getUser_cover()).placeholder(R.drawable.profilehint).into(binding.updateCoverImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addCoverImageBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("image/*");
            startActivityForResult(intent1,101);
        });
        binding.addProfileImageBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
            intent2.setType("image/*");
            startActivityForResult(intent2,201);
        });

        binding.upadateBtn.setOnClickListener(view -> {
            dialog.show();
            FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser1 != null){
                name = binding.profileName.getText().toString().trim();
                phone = binding.profilePhone.getText().toString().trim();
                bio = binding.profileBio.getText().toString().trim();

                if (name.equals("")){
                    binding.profileName.setError("");
                }else if (phone.equals("")){
                    binding.profilePhone.setError("");
                }else {
                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("user_name",name);
                    userMap.put("user_phone",phone);
                    userMap.put("user_bio",bio);
                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
            
            if (coverImgUri != null){
                StorageReference storageReference1 = storageReference.child("cover").child(name+System.currentTimeMillis());
                storageReference1.putFile(coverImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    coverImgUrl = String.valueOf(uri);
                                    Map<String,Object> userMap = new HashMap<>();
                                    userMap.put("user_cover",coverImgUrl);
                                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }

            if (profileImgUri != null){
                StorageReference storageReference2 = storageReference.child("profile").child(name+System.currentTimeMillis());
                storageReference2.putFile(profileImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImgUrl = String.valueOf(uri);
                                    Map<String,Object> userMap = new HashMap<>();
                                    userMap.put("user_profile",profileImgUrl);
                                    databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }
                });

            }
            
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101){
            if (resultCode == RESULT_OK){
                if (data != null){
                    coverImgUri = data.getData();
                    binding.updateCoverImage.setImageURI(coverImgUri);

                }
            }
        }else if (requestCode == 201){
            if (resultCode == RESULT_OK){
                if (data != null){
                    profileImgUri = data.getData();
                    binding.updateProfileImage.setImageURI(profileImgUri);
                }
            }
        }

    }
}