package com.mk.wehire.Company.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.Company.adapters.RecycMyPostsAdapter;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycApplicationsAdapter;
import com.mk.wehire.User.fragments.internships.InternDetailsFragment;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.models.OpenBottomSheet;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentMyPostsBinding;

import java.util.ArrayList;
import java.util.Collections;

public class MyPostsFragment extends Fragment {

    FragmentMyPostsBinding binding;

    DatabaseReference appRef,removePostRef,removeIntern;
    ArrayList<IntershipModel> list;

    OpenBottomSheet openBottomSheet;

    public MyPostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPostsBinding.inflate(inflater,container,false);
        SessionManagement.init(requireContext());

        openBottomSheet = new OpenBottomSheet(getContext());

        Toolbar toolbar = getActivity().findViewById(R.id.toolbarHome);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("My Posts");
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer);

        MenuItem item = toolbar.getMenu().getItem(0).setVisible(true);
        MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(true);


        toolbar.setNavigationOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyPosts();

    }

    private void getMyPosts() {

        list = new ArrayList<>();
        appRef = FirebaseDatabase.getInstance().getReference("Companys").child(SessionManagement.getUserLoginID())
                .child("myPosts");
        Log.d("TAG2", "getApplications: ------- "+SessionManagement.getUserLoginID());
        binding.recycMyPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycMyPosts.setLayoutManager(linearLayout);

        RecycMyPostsAdapter recycPostAdapter = new RecycMyPostsAdapter(getContext(), list, new RecycMyPostsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IntershipModel application) {


            }
        });
        binding.recycMyPosts.setAdapter(recycPostAdapter);
        appRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            IntershipModel postModel = dataSnapshot.getValue(IntershipModel.class);
                            list.add(postModel);



                        }
                        Collections.reverse(list);
                        recycPostAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
                if (list.size() == 0){
                    binding.relateImg.setVisibility(View.VISIBLE);
                    binding.recycMyPosts.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG2", "onCancelled: ---- "+error.getMessage());
            }
        });

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
        MenuItem item = toolbar.getMenu().getItem(0);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Your Posts");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<IntershipModel> filteredlist = new ArrayList<>();

                for (IntershipModel item : list) {
                    if (item.getCourseName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredlist.add(item);
                    }else if (item.getCompanyName().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }


                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycPostAdapter.filterList(filteredlist);
                    //binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {

            getMyPosts();
            recycPostAdapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });

        recycPostAdapter.setOnClickListener(new RecycMyPostsAdapter.OnClickListener(){


            @Override
            public void onQuetionClick(View view, int position, IntershipModel app) {
                openBottomSheet.openBottomSheet("Application status: "+app.getAppStatus(),getString(R.string.postedStatus));
            }

            @Override
            public void seeAllApplicantsClick(View view, int position, IntershipModel app) {

                Bundle bundle = new Bundle();
                bundle.putString("key",app.getKey());
                Log.d("TAG2", "seeAllApplicantsClick: ------"+ app.getKey());
                SessionManagement.saveKey(app.getKey());
                ApplicantsFragment applicantsFragment = new ApplicantsFragment();
                applicantsFragment.setArguments(bundle);
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.frame_cont,applicantsFragment);
                trans.commit();

            }

//            @Override
//            public void onEditImgClick(View view, int position, IntershipModel application) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString("courseName",application.getCourseName());
//                bundle.putString("workLocation",application.getWorkLocation());
//                bundle.putString("companyName",application.getCompanyName());
//                bundle.putString("salary",application.getSalary());
//                bundle.putString("totalMonths",application.getTotalMonths());
//                bundle.putString("companyImgUrl",application.getCompanyImgUrl());
//                bundle.putString("partFullTime",application.getPartFullTime());
//                bundle.putString("jobOrIntern",application.getJobOrIntern());
//                bundle.putString("key",application.getKey());
//                bundle.putString("compLongDes",application.getCompLongDes());
//                bundle.putString("compWeb",application.getCompWeb());
//                bundle.putString("openingNum",application.getOpeningNum());
//                bundle.putString("skill",application.getSkill());
//                bundle.putString("internRole",application.getRoleResponsibility());
//                bundle.putString("formLink",application.getFormLink());
//                bundle.putString("comesFrom","application");
//
//                InternDetailsFragment internDetails = new InternDetailsFragment();
//                internDetails.setArguments(bundle);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_cont,internDetails);
//                transaction.commit();
//
//            }

            @Override
            public void onDeleteImgClick(View view, int position, IntershipModel app) {

                removePostRef = FirebaseDatabase.getInstance().getReference("Companys").child(SessionManagement.getUserLoginID())
                        .child("myPosts");
                removeIntern = FirebaseDatabase.getInstance().getReference("Internships");

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Do you want to Remove ?")
                        .setMessage("Your Intership/Job will be removed.")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                removeIntern.child(app.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                                removePostRef.child(app.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Snackbar.make(binding.getRoot(),"SuccesFuly Removed",Snackbar.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Snackbar.make(binding.getRoot(),"Something Went Wrong! Try Again.",Snackbar.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

            }





        });

    }


}