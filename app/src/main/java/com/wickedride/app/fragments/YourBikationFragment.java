package com.wickedride.app.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Madhumita on 11-02-2016.
 */
public class YourBikationFragment  extends BaseFragment{

    @InjectView(R.id.back_button)
    ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_bikation, container, false);
        ButterKnife.inject(this, view);
        ((BaseActivity)getActivity()).hideToolBar();
        return view;
    }

    @OnClick(R.id.back_button)
    void openNavdrawer(){
        if (((BaseActivity)getActivity()).isDrawerOpen()){
            ((BaseActivity)getActivity()).closeDrawer();
        }
        else{
            ((BaseActivity)getActivity()).openDrwer();
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public String getSelfTag() {
        return null;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return null;
    }
}
