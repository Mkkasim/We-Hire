package com.mk.wehire.User.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycInternAdapter;
import com.mk.wehire.databinding.FragmentJobsBinding;
import com.mk.wehire.User.models.IntershipModel;

import java.util.ArrayList;
import java.util.Collections;


public class JobsFragment extends Fragment {

    FragmentJobsBinding binding;

    DatabaseReference jobsRef;
    ArrayList<IntershipModel> list;

    public JobsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentJobsBinding.inflate(inflater,container,false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Jobs");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getJobs();

    }

    private void getJobs() {

        list = new ArrayList<>();
        jobsRef = FirebaseDatabase.getInstance().getReference().child("Internships");
        binding.RecycJobs.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.RecycJobs.setLayoutManager(linearLayout);

        RecycInternAdapter recycInternAdapter = new RecycInternAdapter(getContext(), list, new RecycInternAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IntershipModel addCourseModel) {

            }
        });

        binding.RecycJobs.setAdapter(recycInternAdapter);

        jobsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            IntershipModel intershipModel = dataSnapshot.getValue(IntershipModel.class);
                            list.add(intershipModel);
                        }
                        Collections.reverse(list);
                        recycInternAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ==="+e.getMessage());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        MenuItem item = toolbar.getMenu().getItem(1).setVisible(false);
        MenuItem item1 = toolbar.getMenu().getItem(0);

        SearchView searchView = (SearchView) item1.getActionView();
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
                    }
                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycInternAdapter.filterList(filteredlist);
                }

                return true;
            }
        });



    }
}