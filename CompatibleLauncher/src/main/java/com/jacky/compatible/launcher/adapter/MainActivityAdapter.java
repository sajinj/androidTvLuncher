package com.jacky.compatible.launcher.adapter;

import android.os.Parcelable;

import com.jacky.uikit.fragment.BaseFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Main UI Adapter
 *
 * @author jacky
 * @version v1.0
 * @since 2016.4.1
 */
public class MainActivityAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseFragment> mFragments;
    private FragmentManager fm;

    public MainActivityAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

}
