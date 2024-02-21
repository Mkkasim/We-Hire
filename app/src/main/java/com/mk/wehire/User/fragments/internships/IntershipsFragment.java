package com.mk.wehire.User.fragments.internships;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.mk.wehire.User.NetworkUtility.InternetReceiver;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycInternAdapter;
import com.mk.wehire.databinding.FragmentIntershipsBinding;
import com.mk.wehire.User.fragments.FilterFragment;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.sessionmanager.SessionManagement;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class IntershipsFragment extends Fragment implements BackFragment {

    FragmentIntershipsBinding binding;

    DatabaseReference internRef,appRef,refPerks;
    ArrayList<IntershipModel> list;

    InternetReceiver netlistener = new InternetReceiver();

    RecycInternAdapter adapter;

    String workHome,partTime;

    public IntershipsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentIntershipsBinding.inflate(inflater,container,false);


        binding.shimmerEffect.startShimmer();
        getInternships();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("InternShip");
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


        Log.d("TAG2", "onViewCreated: -----checkWork "+ SessionManagement.isWorkHomeChecked());
        Log.d("TAG2", "onViewCreated: -----checkPart "+ SessionManagement.isPartTimeChecked());
        Log.d("TAG2", "onViewCreated: -----checkPref "+ SessionManagement.isPrefChecked());





        binding.filter.setOnClickListener(view1 -> {
//                Intent in = new Intent(requireContext(), FilterActivity.class);
//                startActivity(in);
//                Animatoo.animateZoom(requireContext());

            FilterFragment filterFragment = new FilterFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,filterFragment);
            transaction.commit();

        });








    }

    private void getInternships() {

        list = new ArrayList<>();
        internRef = FirebaseDatabase.getInstance().getReference().child("Internships");
        appRef = FirebaseDatabase.getInstance().getReference(SessionManagement.getUserLoginID()).child("myApplications");
        binding.RecycIntern.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.RecycIntern.setLayoutManager(linearLayout);

        RecycInternAdapter recycInternAdapter = new RecycInternAdapter(getContext(), list, new RecycInternAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IntershipModel internModel) {

                Bundle bundle = new Bundle();
                bundle.putString("courseName",internModel.getCourseName());
                bundle.putString("workLocation",internModel.getWorkLocation());
                bundle.putString("companyName",internModel.getCompanyName());
                bundle.putString("salary",internModel.getSalary());
                bundle.putString("totalMonths",internModel.getTotalMonths());
                bundle.putString("companyImgUrl",internModel.getCompanyImgUrl());
                bundle.putString("partFullTime",internModel.getPartFullTime());
                bundle.putString("jobOrIntern",internModel.getJobOrIntern());
                bundle.putString("key",internModel.getKey());
                bundle.putString("compLongDes",internModel.getCompLongDes());
                bundle.putString("compWeb",internModel.getCompWeb());
                bundle.putString("openingNum",internModel.getOpeningNum());
                bundle.putString("skill",internModel.getSkill());
                bundle.putString("internRole",internModel.getRoleResponsibility());
                bundle.putString("formLink",internModel.getFormLink());
                bundle.putString("compId",internModel.getCompId());

                InternDetailsFragment internDetails = new InternDetailsFragment();
                internDetails.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,internDetails);
                transaction.commit();

            }
        });

        String partTime1 = SessionManagement.getCheckPart();
        String workHome1 = SessionManagement.getCheckWorkHome();
        String stipend = SessionManagement.getMiniumStipend();
        Log.d("TAG2", "getInternships: here is part time-----"+SessionManagement.getCheckPart()+"---"+partTime1);
        Log.d("TAG2", "getInternships: Stipend -----"+SessionManagement.getMiniumStipend()+"---"+stipend);





        if (isConnected()){
            binding.shimmerEffect.stopShimmer();
            binding.shimmerEffect.setVisibility(View.GONE);
            binding.RecycIntern.setVisibility(View.VISIBLE);
            binding.relateFilter.setVisibility(View.VISIBLE);
            binding.RecycIntern.setAdapter(recycInternAdapter);
        }


        internRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                            IntershipModel intershipModel = dataSnapshot.getValue(IntershipModel.class);
                            list.add(intershipModel);

                            binding.totalInternship.setText("Total "+list.size()+" internships");

                        }
                        Collections.reverse(list);
                        recycInternAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //recycInternAdapter.multipleFilter(workHome1,partTime1,list);
        //recycInternAdapter.filterMonths("3");





        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        MenuItem item = toolbar.getMenu().getItem(0);
        MenuItem item1 = toolbar.getMenu().getItem(1).setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search jobs & internships");


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
                    }else if (item.getWorkLocation().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }else if (item.getJobOrIntern().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }else if (item.getPartFullTime().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }


                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycInternAdapter.filterList(filteredlist);
                    binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });


        binding.swipeRefresh.setOnRefreshListener(() -> {

            getInternships();
            recycInternAdapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });




//        ArrayList<IntershipModel> filter = new ArrayList<>();
//        for (IntershipModel item2 : list) {
//            Log.d("TAG2", "getInternships: -----work from home  -- "+ workHome1);
//            Log.d("TAG2", "getInternships: -----part time  -- "+ partTime1);
//
//            if (item2.getPartFullTime().toLowerCase().contains(partTime1.toLowerCase())) {
//                filter.add(item2);
//            }
//            if (item2.getWorkLocation().toLowerCase().contains(workHome1.toLowerCase())){
//                filter.add(item2);
//            }
//
//
//        }
//        if (filter.isEmpty()) {
//            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            recycInternAdapter.filterList(filter);
//            binding.totalInternship.setText(filter.size()+" total internships");
//        }


    }


    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        return info !=null && info.isConnected();
    }


    @Override
    public void onPause() {
        binding.shimmerEffect.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        binding.shimmerEffect.startShimmer();
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().finish();
        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}