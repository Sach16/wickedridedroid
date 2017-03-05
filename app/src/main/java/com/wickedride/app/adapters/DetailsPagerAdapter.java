package com.wickedride.app.adapters;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import com.wickedride.app.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class DetailsPagerAdapter extends FragmentPagerAdapterExt {

    private final Resources mResources;
    List<BaseFragment> mFragments = new ArrayList<BaseFragment>();

    public DetailsPagerAdapter(FragmentManager fm, Resources resources, List<BaseFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
        this.mResources = resources;
    }

    @Override
    public BaseFragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public String makeFragmentTag(int position) {
        return mFragments.get(position).getSelfTag();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle(mResources);
    }

    public boolean canScrollVertically(int position, int direction) {
        return getItem(position).canScrollVertically(direction);
    }
}
