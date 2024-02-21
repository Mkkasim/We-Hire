package com.mk.wehire.User.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.mk.wehire.R;
import com.mk.wehire.databinding.FragmentPreferenceBinding;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;

import net.skoumal.fragmentback.BackFragment;


public class PreferenceFragment extends Fragment implements BackFragment {
    FragmentPreferenceBinding binding;
    String comesFrom;

    public PreferenceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreferenceBinding.inflate(inflater,container,false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Preferences");
        MenuItem item1 = toolbar.getMenu().getItem(0).setVisible(false);

        handleArguments();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    private void handleArguments() {
        comesFrom = getArguments().getString("comesFrom");
        if (!comesFrom.equals("drawer")){
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.back);
            NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);

            toolbar.setNavigationOnClickListener(view1 -> {
//                IntershipsFragment internFrag = new IntershipsFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_cont,internFrag);
//                transaction.commit();
                onBackPressed();

            });
        }
    }


    @Override
    public boolean onBackPressed() {
        if (comesFrom.equals("filter")){
            FilterFragment filterFragment = new FilterFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,filterFragment);
            transaction.commit();
        }else{
            IntershipsFragment intershipsFragment = new IntershipsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,intershipsFragment);
            transaction.commit();
        }

        return true;

    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}