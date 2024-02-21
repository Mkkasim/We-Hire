package com.mk.wehire.Company.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.mk.wehire.R;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.loginsignup.LoginActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentMoreBinding;

import net.skoumal.fragmentback.BackFragment;


public class MoreFragment extends Fragment implements BackFragment {

    FragmentMoreBinding binding;
    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    public MoreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater,container,false);
        SessionManagement.init(requireContext());

        if (SessionManagement.isLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("More");
            MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(view1 -> {
                onBackPressed();
            });
        }else if (SessionManagement.isCompLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
            toolbar.setTitle("More");
            MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
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

        Bundle bundle = new Bundle();

        binding.linLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();

            }
        });

        binding.linAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.linSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.linTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("comesFrom","Terms");
                PdfFragment pdfFrag = new PdfFragment();
                pdfFrag.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,pdfFrag);
                transaction.commit();
            }
        });
        binding.linprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("comesFrom","Privacy");
                PdfFragment pdfFrag = new PdfFragment();
                pdfFrag.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,pdfFrag);
                transaction.commit();
            }
        });
    }

    private void logoutDialog() {

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Do you want to logout ?")
                .setMessage("Please post Jobs, Internships and Trainings Sessions.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mAuth = FirebaseAuth.getInstance();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(),gso);
                        mAuth.signOut();
                        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(),
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (SessionManagement.isCompLogged()){
                                            SessionManagement.setCompLogin(false);
                                        }else if (SessionManagement.isLogged()){
                                            SessionManagement.setLogin(false);
                                        }

                                        Intent in = new Intent(requireActivity(), LoginActivity.class);
                                        startActivity(in);
                                        requireActivity().finish();
                                        Animatoo.animateSlideRight(requireContext());
                                    }
                                });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                }).show();




    }


    @Override
    public boolean onBackPressed() {

        if (SessionManagement.isCompLogged()){
            MyPostsFragment myPostFrg = new MyPostsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,myPostFrg);
            transaction.commit();
        }else if (SessionManagement.isLogged()){
            IntershipsFragment internFrag = new IntershipsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,internFrag);
            transaction.commit();
        }



        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}