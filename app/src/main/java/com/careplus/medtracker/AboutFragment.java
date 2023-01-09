package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
// #################################################################################################

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_about, container, false);

        return myView;
    }
}