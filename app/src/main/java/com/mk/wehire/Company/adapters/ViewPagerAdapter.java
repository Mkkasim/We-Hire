package com.mk.wehire.Company.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mk.wehire.Company.fragments.applicants.AllApplicantFragment;
import com.mk.wehire.Company.fragments.applicants.HiredApplicantsFragment;
import com.mk.wehire.Company.fragments.applicants.RejectedApplicantsFragment;
import com.mk.wehire.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    
    public ViewPagerAdapter(@NonNull FragmentManager fm,String key) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
            fragment = new AllApplicantFragment();
        else if (position == 1)
            fragment = new HiredApplicantsFragment();
        else if (position == 2)
            fragment = new RejectedApplicantsFragment();



        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "All";
        else if (position == 1)
            title = "Hired";
        else if (position == 2)
            title = "Rejected";
        return title;
    }


}
