package com.mk.wehire.User.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycApplicationsAdapter;
import com.mk.wehire.User.fragments.internships.InternDetailsFragment;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.models.OpenBottomSheet;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentApplicationsBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class ApplicationsFragment extends Fragment implements BackFragment {

    FragmentApplicationsBinding binding;

    DatabaseReference appRef;
    ArrayList<ApplicationModel> list;

    OpenBottomSheet openBottomSheet;
    String appStatus;

    public ApplicationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplicationsBinding.inflate(inflater,container,false);

        openBottomSheet = new OpenBottomSheet(getContext());

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("Applications");
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer);

        MenuItem item = toolbar.getMenu().getItem(0).setVisible(true);

        toolbar.setNavigationOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.dropDown.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                binding.relateFacingIssue.setVisibility(View.VISIBLE);
                binding.dropUp.setVisibility(View.VISIBLE);
                binding.dropDown.setVisibility(View.GONE);
            }
        });

        binding.dropUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                binding.relateFacingIssue.setVisibility(View.GONE);
                binding.dropDown.setVisibility(View.VISIBLE);
                binding.dropUp.setVisibility(View.GONE);
            }
        });

        getApplications();


    }

    private void getApplications() {
        list = new ArrayList<>();
        appRef = FirebaseDatabase.getInstance().getReference("Users").child(SessionManagement.getUserLoginID())
                .child("myApplications");
        Log.d("TAG2", "getApplications: ------- "+SessionManagement.getUserLoginID());
        binding.recycApplications.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycApplications.setLayoutManager(linearLayout);

        RecycApplicationsAdapter recycAppAdater = new RecycApplicationsAdapter(getContext(), list, new RecycApplicationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ApplicationModel application) {
                Bundle bundle = new Bundle();
                bundle.putString("courseName",application.getCourseName());
                bundle.putString("workLocation",application.getWorkLocation());
                bundle.putString("companyName",application.getCompanyName());
                bundle.putString("salary",application.getSalary());
                bundle.putString("totalMonths",application.getTotalMonths());
                bundle.putString("companyImgUrl",application.getCompanyImgUrl());
                bundle.putString("partFullTime",application.getPartFullTime());
                bundle.putString("jobOrIntern",application.getJobOrIntern());
                bundle.putString("key",application.getKey());
                bundle.putString("compLongDes",application.getCompLongDes());
                bundle.putString("compWeb",application.getCompWeb());
                bundle.putString("openingNum",application.getOpeningNum());
                bundle.putString("skill",application.getSkill());
                bundle.putString("internRole",application.getRoleResponsibility());
                bundle.putString("formLink",application.getFormLink());
                bundle.putString("comesFrom","application");

                InternDetailsFragment internDetails = new InternDetailsFragment();
                internDetails.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,internDetails);
                transaction.commit();

            }
        });
        binding.recycApplications.setAdapter(recycAppAdater);
        appRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            ApplicationModel appModel = dataSnapshot.getValue(ApplicationModel.class);
                            list.add(appModel);

                        }
                        Collections.reverse(list);
                        recycAppAdater.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
                if (list.size() == 0){
                    binding.relateImg.setVisibility(View.VISIBLE);
                    binding.recycApplications.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG2", "onCancelled: ---- "+error.getMessage());
            }
        });

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        MenuItem item = toolbar.getMenu().getItem(0);
        //MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Applications");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<ApplicationModel> filteredlist = new ArrayList<>();

                for (ApplicationModel item : list) {
                    if (item.getCourseName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredlist.add(item);
                    }else if (item.getCompanyName().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }


                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycAppAdater.filterList(filteredlist);
                    //binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });


        recycAppAdater.setOnClickListener(new RecycApplicationsAdapter.OnClickListener(){


            @Override
            public void onQuetionClick(View view, int position, ApplicationModel app) {
                String status = app.getAppStatus();
                if (status.equals("Applied")){
                    openBottomSheet.openBottomSheet("Application status: "+app.getAppStatus(),getString(R.string.appliedStatus));
                }else if (status.equals("Hired")){
                    openBottomSheet.openBottomSheet("Application status: "+app.getAppStatus(),getString(R.string.Hired));
                }else if (status.equals("Not Selected")){
                    openBottomSheet.openBottomSheet("Application status: "+app.getAppStatus(),getString(R.string.Rejected));
                }

            }

            @Override
            public void onAppStatusClick(View view, int position, ApplicationModel app) {

            }
        });

    }

    @Override
    public boolean onBackPressed() {

        IntershipsFragment internFrag = new IntershipsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,internFrag);
        transaction.commit();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}