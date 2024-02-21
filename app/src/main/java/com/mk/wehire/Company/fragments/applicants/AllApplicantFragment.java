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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.Company.adapters.RecycAllAdapter;
import com.mk.wehire.Company.fragments.MyPostsFragment;
import com.mk.wehire.R;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.FCM;
import com.mk.wehire.User.models.Notification;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentAllApplicantBinding;


import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;


public class AllApplicantFragment extends Fragment implements BackFragment {

    FragmentAllApplicantBinding binding;

    DatabaseReference applicantRef,statusRef,userStatusRef,refNotification;
    ArrayList<SignUpUser> list;
    String key,InternRole,CompName,UserToken;

    public AllApplicantFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllApplicantBinding.inflate(inflater,container,false);

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
        binding.recycAllApplicants.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycAllApplicants.setLayoutManager(linearLayout);

        RecycAllAdapter recycAllAdapter = new RecycAllAdapter(getContext(), list, new RecycAllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SignUpUser signUp) {

                Log.d("TAG2", "onItemClick: -----"+signUp.getKey());

            }
        });
        binding.recycAllApplicants.setAdapter(recycAllAdapter);
        applicantRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            if (dataSnapshot.child("applicantStatus").getValue().equals("All")){
                                SignUpUser signUpUser = dataSnapshot.getValue(SignUpUser.class);
                                list.add(signUpUser);
                            }


                        }
                        Collections.reverse(list);
                        recycAllAdapter.notifyDataSetChanged();



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

        recycAllAdapter.setOnClickListener(new RecycAllAdapter.OnClickListener() {
            @Override
            public void onHireClick(View view, int position, SignUpUser signUp) {

                statusRef = FirebaseDatabase.getInstance().getReference("Companys")
                        .child(SessionManagement.getUserLoginID())
                        .child("myPosts").child(key).child("applicants").child(signUp.getKey());

                userStatusRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(signUp.getKey()).child("myApplications").child(key);

                refNotification = FirebaseDatabase.getInstance().getReference("Notifications");

                getPostDetails(signUp);



                statusRef.child("applicantStatus").setValue("Hired").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(binding.getRoot(),"Hurrey! You Have Hired "+signUp.getFirstName()+" For Your Company",Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(binding.getRoot(),"Oops! Something Went Wrong",Snackbar.LENGTH_SHORT).show();
                            }
                        });

                userStatusRef.child("appStatus").setValue("Hired").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FCM fcmSend = new FCM(UserToken,"Congratulations","You have been hired by "+CompName+" for role: "+InternRole,"status",requireContext(),requireActivity());
                        fcmSend.SendNotifications();

                        try {
                            String key = refNotification.push().getKey();
                            Notification noti = new Notification(UserToken,"Congratulations","You have been hired by "+CompName+" for role: "+InternRole,SessionManagement.getUserLoginID(),signUp.getKey(),key);
                            refNotification.child(signUp.getKey())
                                    .child(key).setValue(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }catch (Exception e){
                            Log.d("TAG2", "onComplete: ---"+e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }

            @Override
            public void onRejectClick(View view, int position, SignUpUser signUp) {

                statusRef = FirebaseDatabase.getInstance().getReference("Companys")
                        .child(SessionManagement.getUserLoginID())
                        .child("myPosts").child(key).child("applicants").child(signUp.getKey());

                userStatusRef = FirebaseDatabase.getInstance().getReference("Users")
                                .child(signUp.getKey()).child("myApplications").child(key);

                refNotification = FirebaseDatabase.getInstance().getReference("Notifications");

                getPostDetails(signUp);

                statusRef.child("applicantStatus").setValue("Reject").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Snackbar.make(binding.getRoot(),"Oops! You Have Rejeccted "+signUp.getFirstName()+"'s Application",Snackbar.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(binding.getRoot(),"Oops! Something Went Wrong",Snackbar.LENGTH_SHORT).show();
                    }
                });

                userStatusRef.child("appStatus").setValue("Not Selected").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FCM fcmSend = new FCM(UserToken,"Don't Loose Hope!","You have not Selected by "+CompName+" for role: "+InternRole,"status",requireContext(),requireActivity());
                        fcmSend.SendNotifications();

                        try {
                            String key = refNotification.push().getKey();
                            Notification noti = new Notification(UserToken,"Don't Loose Hope!","You have not Selected by "+CompName+" for role: "+InternRole,SessionManagement.getUserLoginID(),signUp.getKey(),key);
                            refNotification.child(signUp.getKey())
                                    .child(key).setValue(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }catch (Exception e){
                            Log.d("TAG2", "onComplete: ---"+e.getMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

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
                    recycAllAdapter.filterList(filteredlist);
                    //binding.totalInternship.setText(filteredlist.size()+" total internships");
                }

                return true;
            }
        });


    }

    private void getPostDetails(SignUpUser signUp){

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Companys")
                .child(SessionManagement.getUserLoginID())
                .child("myPosts").child(key);
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(signUp.getKey());

        tokenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    try {
                        UserToken = snapshot.child("ftoken").getValue().toString();
                        Log.d("TAG2", "onDataChange: ----Token of user "+UserToken);
                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ---"+e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    try {
                        InternRole = snapshot.child("courseName").getValue().toString();
                        CompName = snapshot.child("companyName").getValue().toString();
                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ---"+e.getMessage());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}