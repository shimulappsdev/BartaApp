package com.example.bartaapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bartaapp.R;
import com.example.bartaapp.databinding.FragmentStatusBinding;
import com.example.bartaapp.model.Status;
import com.example.bartaapp.model.User;
import com.example.bartaapp.model.UserStatus;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatusFragment extends Fragment {

    FragmentStatusBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Uri statusImgUri;
    String statusImgUrl, currentUser;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(getLayoutInflater(), container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Status Uploading...");
        progressDialog.setMessage("Please Wait !");
        progressDialog.setCancelable(false);

        binding.addStatusImgBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 300);
        });
        currentUser = FirebaseAuth.getInstance().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("user").child(currentUser);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("status").child(currentUser);
        storageReference = FirebaseStorage.getInstance().getReference("user_images").child(currentUser).child("status");

        binding.addStatusBtn.setOnClickListener(view -> {
            if (statusImgUri != null){
                progressDialog.show();
                Date date = new Date();
                StorageReference storageReference1 = storageReference.child("status"+date.getTime());
                        storageReference1.putFile(statusImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getUser_name());
                                    userStatus.setProfileImage(user.getUser_profile());
                                    userStatus.setLastUpdate(date.getTime());

                                    Map<String, Object> statusMap = new HashMap<>();
                                    statusMap.put("name", userStatus.getName());
                                    statusMap.put("profileImage", userStatus.getProfileImage());
                                    statusMap.put("lastUpdate", userStatus.getLastUpdate());

                                    statusImgUrl = String.valueOf(uri);
                                    Status status = new Status(statusImgUrl, userStatus.getLastUpdate());

                                    databaseReference.updateChildren(statusMap);
                                    databaseReference.child("statuses").push().setValue(status);
                                    binding.statusImageView.setImageResource(R.drawable.profilehint);

                                }
                            });
                        }

                        Toast.makeText(getActivity(), "Status Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300){
            if (resultCode == RESULT_OK){
                if (data != null){
                    statusImgUri = data.getData();
                    binding.statusImageView.setImageURI(statusImgUri);
                }
            }
        }
    }
}