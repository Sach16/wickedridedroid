package com.wickedride.app.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wickedride.app.R;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class TermsAndConditionFragment extends BaseFragment {


    public TermsAndConditionFragment() {
        // Required empty public constructor
    }


    @Override
    public String getSelfTag() {
        return null;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
