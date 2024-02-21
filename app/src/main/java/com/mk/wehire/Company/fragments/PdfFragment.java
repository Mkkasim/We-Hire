package com.mk.wehire.Company.fragments;

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

import com.mk.wehire.R;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentPdfBinding;

import net.skoumal.fragmentback.BackFragment;


public class PdfFragment extends Fragment implements BackFragment {

    FragmentPdfBinding binding;
    String comesFrom;

    public PdfFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPdfBinding.inflate(inflater,container,false);

        handleIntent();

        if (SessionManagement.isLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle(comesFrom);
            MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
            MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(view1 -> {
                onBackPressed();
            });
        }else if (SessionManagement.isCompLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
            toolbar.setTitle(comesFrom);
            MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
            MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(view1 -> {
                onBackPressed();
            });
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (comesFrom.equals("Terms")){
            binding.pdfView.fromAsset("termsconverted.pdf").load();
        }else if (comesFrom.equals("Privacy")){
            binding.pdfView.fromAsset("termsconverted.pdf").load();
        }

    }

    private void handleIntent() {
        comesFrom = getArguments().getString("comesFrom");
    }

    @Override
    public boolean onBackPressed() {
        MoreFragment moreFrag = new MoreFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,moreFrag);
        transaction.commit();
        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}