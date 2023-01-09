package com.careplus.medtracker;

// #################################################################################################
// Idhr HomeFragment ka code h, jisme Medication aur Hospitalization tabs k switch hone ka code h
// #################################################################################################

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.careplus.medtracker.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentAdapter fragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = myView.findViewById(R.id.tabLayout);
        viewPager2 = myView.findViewById(R.id.viewPager2);

        // Creating 2 tabs namely "Medication" & "Hospitalization"
        tabLayout.addTab(tabLayout.newTab().setText("Medication"));
        tabLayout.addTab(tabLayout.newTab().setText("Hospitalization"));

        // viewPager2 ko fragmentAdapter ki help se HomeFragment se link krdia
        // Jiski vjah se HomeFragment me tabs show honge
        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {   }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {   }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        return myView;
    }
}