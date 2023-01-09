package com.careplus.medtracker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.careplus.medtracker.HospitalizationFragment;
import com.careplus.medtracker.MedicationFragment;

// ######################################################################
// FragmentAdapter for Medication & Hospitalization tabs at Home Fragment
// ######################################################################

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new HospitalizationFragment();   // tab at position 1
        return new MedicationFragment();            // tab at position 0 (default)
    }

    @Override
    public int getItemCount() {
        return 2;   // no. of tabs
    }
}
