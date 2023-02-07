package com.example.bartaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bartaapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(view -> {
            String name = binding.userName.getText().toString().trim();
            String phone = binding.userPhone.getText().toString().trim();
            String email = binding.userEmail.getText().toString().trim();
            String password = binding.userPassword.getText().toString().trim();
            String rePassword = binding.userRePassword.getText().toString().trim();

            if (name.equals("")){
                binding.userName.setError("Required");
            }else if (phone.equals("")){
                binding.userPhone.setError("Required");
            }else if (email.equals("")){
                binding.userEmail.setError("Required");
            }else if (password.equals("")){
                binding.password.setError("Required");
            }else if (rePassword.equals("") || !rePassword.equals(password)){
                binding.userRePassword.setError("Not Match");
            }else {
                UserRegister(name, phone, email, password, rePassword);
            }
        });


        binding.click.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }

    private void UserRegister(String name, String phone, String email, String password, String rePassword) {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.registerBtn.setVisibility(View.GONE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String myUser = firebaseUser.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("user");
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("user_id",myUser);
                userMap.put("user_name",name);
                userMap.put("user_email",email);
                userMap.put("user_phone",phone);
                userMap.put("user_password",password);
                userMap.put("user_rePassword",rePassword);
                userMap.put("user_profile","");
                userMap.put("user_cover","");
                userMap.put("user_bio","");
                databaseReference.child(myUser).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            ShowAlert(task.getException().getLocalizedMessage());
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBar.setVisibility(View.GONE);
                binding.registerBtn.setVisibility(View.VISIBLE);
                ShowAlert(e.getLocalizedMessage());
            }
        });
    }
    private void ShowAlert(String errorMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Caution!");
        builder.setMessage(errorMsg);
        builder.setIcon(android.R.drawable.stat_notify_error);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("What's in your mind ?");
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}