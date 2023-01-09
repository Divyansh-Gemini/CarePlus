package com.careplus.medtracker;

// #################################################################################################
// Abhi idhr koi implementation nhi h
// #################################################################################################

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpAndFeedbackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_help_and_feedback, container, false);

        return myView;
    }
}