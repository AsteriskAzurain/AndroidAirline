package com.ishang.asterisk.application05191.global;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Asterisk on 5/23/2020.
 */

public class WelcomeAdapter extends FragmentPagerAdapter {
    List<WelcomeFragment> fragments;

    public WelcomeAdapter(FragmentManager fm, List<WelcomeFragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    /**
     * 根据位置返回对应的fragment
     * */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * 返回fragments的总数
     */
    @Override
    public int getCount() {
        return fragments.size();
    }
}
