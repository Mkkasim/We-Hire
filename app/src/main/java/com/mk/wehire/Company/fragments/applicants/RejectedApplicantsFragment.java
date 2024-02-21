package com.mk.wehire.Company.fragments.applicants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.Company.activity.HomeActivity;
import com.mk.wehire.Company.adapters.RecycHiredAdapter;
import com.mk.wehire.Company.adapters.RecycRejectAdapter;
import com.mk.wehire.Company.fragments.MyPostsFragment;
import com.mk.wehire.R;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentRejectedApplicantsBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class RejectedApplicantsFragment extends Fragment implements BackFragment {

    FragmentRejectedApplicantsBinding binding;

    DatabaseReference applicantRef;
    ArrayList<SignUpUser> list;
    String key;

    public RejectedApplicantsFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRejectedApplicantsBinding.inflate(inflater,container,false);

        key = SessionManagement.getKey();

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
        binding.recycRejecApp.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycRejecApp.setLayoutManager(linearLayout);

        RecycRejectAdapter recycRejectAdapter = new RecycRejectAdapter(getContext(), list, new RecycRejectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SignUpUser signUp) {

            }
        });
        binding.recycRejecApp.setAdapter(recycRejectAdapter);
        applicantRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists() ){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            if (dataSnapshot.child("applicantStatus").getValue().equals("Reject")) {
                                SignUpUser signUpUser = dataSnapshot.getValue(SignUpUser.class);
                                list.add(signUpUser);
                            }

                        }
                        Collections.reverse(list);
                        recycRejectAdapter.notifyDataSetChanged();

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

        recycRejectAdapter.setOnClickListener(new RecycRejectAdapter.OnClickListener() {
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

    }

    @Override
    public boolean onBackPressed() {
//        MyPostsFragment applicantsFrag = new MyPostsFragment();
//        FragmentTransaction trans = requireActivity().getSupportFragmentManager().beginTransaction();
//        trans.replace(R.id.frame_cont,applicantsFrag);
//        trans.commit();

        Intent in = new Intent(requireContext(), HomeActivity.class);
        startActivity(in);

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}