package com.universl.didul.car_maintenance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.universl.didul.car_maintenance.fragments.FirstFragment;
import com.universl.didul.car_maintenance.fragments.SecondFragment;

public class MyViewPageAdapter extends FragmentStateAdapter {
    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
