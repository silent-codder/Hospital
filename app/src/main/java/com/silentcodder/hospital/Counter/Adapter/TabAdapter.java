package com.silentcodder.hospital.Counter.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.silentcodder.hospital.Counter.Fragments.AppointmentHistoryFragment;
import com.silentcodder.hospital.Counter.Fragments.AppointmentsFragment;
import com.silentcodder.hospital.Counter.Fragments.CounterHomeFragment;

public class TabAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior,int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return new AppointmentsFragment();
            case 1 :
               return new AppointmentHistoryFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
