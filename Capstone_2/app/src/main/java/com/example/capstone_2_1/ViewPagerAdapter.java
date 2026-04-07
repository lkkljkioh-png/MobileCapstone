package com.example.capstone_2_1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InfoFragment();
            case 1:
                return new PastFragment();
            case 2:
                return new MockFragment();
            default:
                return new InfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 탭 3개
    }
}