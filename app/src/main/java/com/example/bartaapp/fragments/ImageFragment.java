package com.example.bartaapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bartaapp.MainActivity;
import com.example.bartaapp.R;
import com.example.bartaapp.databinding.FragmentImageBinding;

public class ImageFragment extends Fragment {

    FragmentImageBinding binding;
    Intent intent;
    String name, profilePic, coverPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(getLayoutInflater(), container, false);

        intent = getActivity().getIntent();
        name = intent.getStringExtra("nameKey");
        profilePic = intent.getStringExtra("profilePicKey");
        coverPic = intent.getStringExtra("coverPicKey");

        if (name != null){
            binding.imageProfileName.setText(name);
        }
        if (profilePic != null){
            Glide.with(getActivity()).load(profilePic).placeholder(R.drawable.profilehint).into(binding.myZoomageView);
        }
        if (coverPic != null){
            Glide.with(getActivity()).load(coverPic).placeholder(R.drawable.profilehint).into(binding.myZoomageView);
        }


        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        return binding.getRoot();
    }
}