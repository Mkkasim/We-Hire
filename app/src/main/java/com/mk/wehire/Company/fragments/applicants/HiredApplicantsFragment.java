package com.mk.wehire.Company.fragments.applicants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.mk.wehire.Company.adapters.RecycHiredAdapter;
import com.mk.wehire.Company.adapters.RecycMyPostsAdapter;
import com.mk.wehire.Company.fragments.ApplicantsFragment;
import com.mk.wehire.Company.fragments.MyPostsFragment;
import com.mk.wehire.R;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentHiredApplicantsBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class HiredApplicantsFragment extends Fragment implements BackFragment {

   FragmentHiredApplicantsBinding binding;
   
   DatabaseReference applicantRef;
    ArrayList<SignUpUser> list;
    String key;

    public HiredApplicantsFragment() {
    }

//    public static HiredApplicantsFragment newInstance(String key){
//
//        Log.d("TAG2", "newInstance: ----"+key);
//        Bundle bundle = new Bundle();
//        bundle.putString("key",key);
//        HiredApplicantsFragment fragmentInfo = new HiredApplicantsFragment();
//        fragmentInfo.setArguments(bundle);
//        return fragmentInfo;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null){
//            key = getArguments().getString("key");
//            Log.d("TAG2", "onCreate: ------"+key);
//        }
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHiredApplicantsBinding.inflate(inflater,container,false);

        key = SessionManagement.getKey();
        //handleArguments();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("TAG2", "onViewCreated: -----"+key);

        getApplicants();

    }

    private void getApplicants() {

        list = new ArrayList<>();
        applicantRef = FirebaseDatabase.getInstance().getReference("Companys")
                .child(SessionManagement.getUserLoginID())
                .child("myPosts").child(key).child("applicants");
        Log.d("TAG2", "getApplications: ------- "+SessionManagement.getUserLoginID());
        binding.recycHiredApplicants.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycHiredApplicants.setLayoutManager(linearLayout);

        RecycHiredAdapter recycHiredAdapter = new RecycHiredAdapter(getContext(), list, new RecycHiredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SignUpUser signUp) {

            }
        });
        binding.recycHiredApplicants.setAdapter(recycHiredAdapter);
        applicantRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists() ){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            if (dataSnapshot.child("applicantStatus").getValue().equals("Hired")) {
                                SignUpUser signUpUser = dataSnapshot.getValue(SignUpUser.class);
                                list.add(signUpUser);
                            }

                        }
                        Collections.reverse(list);
                        recycHiredAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
//                if (list.size() == 0){
//                    binding.relateImg.setVisibility(View.VISIBLE);
//                    binding.recycMyPosts.setVisibility(View.GONE);
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG2", "onCancelled: ---- "+error.getMessage());
            }
        });

        recycHiredAdapter.setOnClickListener(new RecycHiredAdapter.OnClickListener() {
            @Override
            public void onHireClick(View view, int position, SignUpUser signUp) {

            }

            @Override
            public void onRejectClick(View view, int position, SignUpUser signUp) {

            }

            @Override
            public void onChatClick(View view, int position, SignUpUser signUp) {

            }

            @Override
            public void onCallClick(View view, int position, SignUpUser signUp) {

                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+signUp.getMobile()));
                getActivity().startActivity(call);

            }
        });


        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
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
                ArrayList<SignUpUser> filteredlist = new ArrayList<>();

                for (SignUpUser item : list) {
                    if (item.getFirstName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredlist.add(item);
                    }else if (item.getLastName().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }else if (item.getMobile().toLowerCase().contains(newText.toLowerCase())){
                        filteredlist.add(item);
                    }


                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
                } else {
                    recycHiredAdapter.filterList(filteredlist);
                    //binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });

    }

    @Override
    public boolean onBackPressed() {
        MyPostsFragment applicantsFrag = new MyPostsFragment();
        FragmentTransaction trans = requireActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frame_cont,applicantsFrag);
        trans.commit();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
//
//    private void handleArguments(){
//        key = requireParentFragment().getArguments().getString("key");
//        key = getArguments().getString("key");
//    }
}
