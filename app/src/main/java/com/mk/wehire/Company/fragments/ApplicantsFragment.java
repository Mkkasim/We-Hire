package com.mk.wehire.Company.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mk.wehire.Company.activity.HomeActivity;
import com.mk.wehire.Company.adapters.ViewPagerAdapter;
import com.mk.wehire.R;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentApplicantsBinding;

import net.skoumal.fragmentback.BackFragment;


public class ApplicantsFragment extends Fragment implements BackFragment {


    FragmentApplicantsBinding binding;

    ViewPagerAdapter viewPagerAdapter;
    String key;

    public ApplicantsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplicantsBinding.inflate(inflater,container,false);

        handleArguments();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbarHome);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("Applicants");

        toolbar.setNavigationOnClickListener(view1 -> {
            onBackPressed();
        });

        viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(),key);
        binding.homeViewpager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        binding.homeTabs.setupWithViewPager(binding.homeViewpager);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void handleArguments() {
        key = getArguments().getString("key");

    }

    @Override
    public boolean onBackPressed() {

        Intent in = new Intent(requireActivity(),HomeActivity.class);
        startActivity(in);
        requireActivity().finish();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}