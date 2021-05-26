package com.nikunj.messenger.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.nikunj.messenger.R;
import com.nikunj.messenger.ui.fragments.CallFragment;
import com.nikunj.messenger.ui.fragments.ChatFragment;
import com.nikunj.messenger.ui.fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull  FragmentManager fm) {
        super(fm);
    }

    public FragmentsAdapter(@NonNull  FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatFragment();
            case 1: return new StatusFragment();
            case 2: return new CallFragment();
            default: return new ChatFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null;
      if (position==0){
          title="CHATS";

      }
        if (position==1){
            title="STATUS";

        }
        if (position==2){
            title="CALLS";

        }
        return title;
    }
}
