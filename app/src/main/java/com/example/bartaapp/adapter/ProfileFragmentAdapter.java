package com.example.bartaapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bartaapp.fragments.CallFragment;
import com.example.bartaapp.fragments.ChatFragment;
import com.example.bartaapp.fragments.StatusFragment;
import com.example.bartaapp.fragments.UpdateProfileFragment;
import com.example.bartaapp.fragments.UserProfileFragment;

public class ProfileFragmentAdapter extends FragmentPagerAdapter {
    String[] name = {"Profile", "Setting"};
    public ProfileFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new UserProfileFragment();
            case 1:
                return new UpdateProfileFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return name[position];
    }
}
