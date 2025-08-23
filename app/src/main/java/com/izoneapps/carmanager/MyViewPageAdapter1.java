package com.izoneapps.carmanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.izoneapps.carmanager.fragments.ForthFragment;
import com.izoneapps.carmanager.fragments.ThirdFragment;

public class MyViewPageAdapter1 extends FragmentStateAdapter {
    public MyViewPageAdapter1(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new ThirdFragment();
            case 1:
                return new ForthFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
