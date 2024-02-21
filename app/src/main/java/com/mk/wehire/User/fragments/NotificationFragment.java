package com.mk.wehire.User.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.mk.wehire.Company.fragments.MyPostsFragment;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.NotificationAdapter;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.models.Notification;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentNotificationBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class NotificationFragment extends Fragment implements BackFragment {

    FragmentNotificationBinding binding;

    DatabaseReference refNotifications;
    ArrayList<Notification> list;

    public NotificationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        SessionManagement.init(requireContext());

        if (SessionManagement.isLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Notification");
            MenuItem item = toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(view1 -> {
                onBackPressed();
            });
        }else if (SessionManagement.isCompLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
            toolbar.setTitle("Notification");
            MenuItem item = toolbar.getMenu().getItem(1).setVisible(false);
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

        getNotification();
    }

    private void getNotification() {
        refNotifications = FirebaseDatabase.getInstance().getReference("Notifications").child(SessionManagement.getUserLoginID());

        list = new ArrayList<>();

        Log.d("TAG2", "getApplications: ------- "+SessionManagement.getUserLoginID());
        binding.recycNotification.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycNotification.setLayoutManager(linearLayout);

        NotificationAdapter recycNotiAdapter = new NotificationAdapter(getContext(),list);
        binding.recycNotification.setAdapter(recycNotiAdapter);
        refNotifications.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            Notification notiModel = dataSnapshot.getValue(Notification.class);
                            list.add(notiModel);



                        }
                        Collections.reverse(list);
                        recycNotiAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
                if (list.size() == 0){
                    binding.relateImg.setVisibility(View.VISIBLE);
                    binding.recycNotification.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG2", "onCancelled: ---- "+error.getMessage());
            }
        });

        if (SessionManagement.isLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            MenuItem item = toolbar.getMenu().getItem(0);
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search Your Posts");

            search(searchView,recycNotiAdapter);

        }else if (SessionManagement.isCompLogged()){
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
            toolbar.setTitle("Notification");
            MenuItem item = toolbar.getMenu().getItem(0);
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search Your Posts");

            search(searchView, recycNotiAdapter);
        }



    }

    private void search(SearchView searchView, NotificationAdapter recycNotiAdapter) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Notification> filteredlist = new ArrayList<>();

                for (Notification item : list) {
                    if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredlist.add(item);
                    }else if (item.getBody().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }


                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycNotiAdapter.filterList(filteredlist);
                    //binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {

            getNotification();
            recycNotiAdapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });
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