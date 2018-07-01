package com.example.amarjeet.hola;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Amarjeet on 14-04-2018.
 */

class ViewPagerAdapter extends FragmentPagerAdapter{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                UserFragment userFragment = new UserFragment();
                return userFragment;

            case 1:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;

            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
