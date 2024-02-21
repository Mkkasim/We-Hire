package com.mk.wehire.User.fragments.internships;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycPerksAdapter;
import com.mk.wehire.User.adapters.RecycSkillsAdapter;
import com.mk.wehire.User.fragments.ApplicationsFragment;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.FCM;
import com.mk.wehire.User.models.Notification;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentInternDetailsBinding;
import com.mk.wehire.User.models.IntershipModel;

import net.skoumal.fragmentback.BackFragment;

import java.net.URL;
import java.util.ArrayList;


public class InternDetailsFragment extends Fragment implements BackFragment {

    FragmentInternDetailsBinding binding;

    String courseName;
    String workLocation;
    String companyName;
    String salary;
    String totalMonths;
    String companyImgUrl;
    String partFullTime;
    String key;
    String jobOrIntern;
    String compLongDes;
    String compWeb;
    String internRole;
    String openingNum;
    String perkCert,perkLetter,perkFlex,perkDays;
    String skillsReq;
    String formLink;
    String comesFrom,compId,compToken;
    String degreeName = null;

    DatabaseReference perkRef,skillRef,saveAppRef,saveToCompRef,refPerks,refEducation,refNotification;
    ArrayList<IntershipModel> list = new ArrayList<>();
    ArrayList<IntershipModel> listSkill = new ArrayList<>();

    WeHireLoader weHireLoader;

    public InternDetailsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInternDetailsBinding.inflate(inflater,container,false);

        handleIntent();
        Log.d("TAG2", "onCreateView: ======key   "+key);
        if (comesFrom!= null && comesFrom.equals("application")){
            perkRef = FirebaseDatabase.getInstance().getReference("Users").child(SessionManagement.getUserLoginID())
                    .child("myApplications").child(key).child("perks");
        }else {
            perkRef = FirebaseDatabase.getInstance().getReference("Internships").child(key).child("perks");
        }

        skillRef = FirebaseDatabase.getInstance().getReference("Internships").child(key).child("skill");

        weHireLoader = new WeHireLoader(getContext());

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Internship details");
        MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(view1 -> {


            onBackPressed();


        });
        Log.d("TAG2", "onComplete: ----------"+SessionManagement.getNumber());

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPerks();
        getPerksFire();
        getEducation();
        //getSkills();

        if (comesFrom != null && comesFrom.equals("application")){
            binding.ApplyBtn.setVisibility(View.GONE);
            binding.recycPerks.setVisibility(View.GONE);
            binding.relatePerks.setVisibility(View.GONE);
        }

        binding.ApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("formLink",formLink);
//                WebViewFragment wvFrag = new WebViewFragment();
//                wvFrag.setArguments(bundle);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_cont,wvFrag);
//                transaction.addToBackStack(null);
//                transaction.commit();

                if (degreeName == null){

                    Snackbar.make(binding.getRoot(),"Please Fill Your Education Details In Resume",Snackbar.LENGTH_SHORT).show();

                }else {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.apply_internship_bottomsheet);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_round);

                    TextView applyBtn = dialog.findViewById(R.id.applyText);

                    applyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!formLink.startsWith("https://") && !formLink.startsWith("http://")){
                                formLink = "http://" + formLink;
                            }
                            Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(formLink));
                            startActivity(openUrlIntent);
                            dialog.dismiss();

                            saveApplications();
                        }
                    });
                    dialog.create();
                    dialog.show();
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                }


            }
        });

        binding.CompWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = binding.CompWebsite.getText().toString();
                if (!website.startsWith("https://") && !website.startsWith("http://")){
                    website = "http://" + website;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(openUrlIntent);
            }
        });


    }

    private void saveApplications() {
        saveAppRef = FirebaseDatabase.getInstance().getReference("Users");
        saveToCompRef = FirebaseDatabase.getInstance().getReference("Companys");
        refNotification = FirebaseDatabase.getInstance().getReference("Notifications").child(compId);

        getCompToken();

        weHireLoader.ShowDialog();

        ApplicationModel saveApplication = new ApplicationModel(courseName,workLocation,companyName,salary,
                totalMonths,companyImgUrl,partFullTime,key,jobOrIntern,internRole,compWeb,openingNum,
                skillsReq,compLongDes,formLink,"Applied");

        SignUpUser saveUser = new SignUpUser(SessionManagement.getEmail(),SessionManagement.getFirstName(),
                SessionManagement.getLastName(),SessionManagement.getNumber(),"All",
                SessionManagement.getUserLocation(),SessionManagement.getDegreeName(),
                SessionManagement.getUserLoginID());

        saveAppRef.child(SessionManagement.getUserLoginID()).child("myApplications")
                .child(key).setValue(saveApplication).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Log.d("TAG2", "onComplete: ----------"+compId);
                            Log.d("TAG2", "onComplete: ----------"+key);
                            Log.d("TAG2", "onComplete: ----------Company Token "+compToken);
                            Log.d("TAG2", "onComplete: ----------"+SessionManagement.getUserLoginID());


                        saveToCompRef.child(compId).child("myPosts").child(key).child("applicants")
                                .child(SessionManagement.getUserLoginID()).setValue(saveUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            weHireLoader.DismissDialog();

                                            FCM fcmsend = new FCM(compToken,"New Application",""+SessionManagement.getFirstName()+" "+
                                                    SessionManagement.getLastName()+" Applied For "+courseName,"application",
                                                    requireContext(),requireActivity());
                                            fcmsend.SendNotifications();

                                            try {
                                                String key = refNotification.push().getKey();
                                                Notification noti = new Notification(compToken,"New Application",""+SessionManagement.getFirstName()+" "+
                                                        SessionManagement.getLastName()+" Applied For "+courseName,compId,SessionManagement.getUserLoginID(),key);
                                                refNotification.child(key).setValue(noti).addOnCompleteListener(new OnCompleteListener<Void>() {
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



                                            onBackPressed();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



    }

    private void getCompToken() {

        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Companys").child(compId);

        tokenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    try {

                        compToken = snapshot.child("ftoken").getValue().toString();
                        Log.d("TAG2", "onDataChange: ------Token "+compToken);

                    }catch (Exception e){
                        Toast.makeText(requireActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private void getSkills() {
//        binding.recycSkills.setHasFixedSize(true);
//        GridLayoutManager linearLayout = new GridLayoutManager(requireContext(),3);
//        binding.recycSkills.setLayoutManager(linearLayout);
//        RecycSkillsAdapter adapter = new RecycSkillsAdapter(getContext(),listSkill);
//        binding.recycSkills.setAdapter(adapter);
//
//        skillRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listSkill.clear();
//                if (snapshot.exists()){
//                    try {
//                        for (DataSnapshot data : snapshot.getChildren()){
//                            if (data.child("perk").getValue()!= null){
//                                IntershipModel model = data.getValue(IntershipModel.class);
//                                listSkill.add(model);
//                                Log.d("TAG2", "onDataChange:list.....--- "+listSkill);
//                                Log.d("TAG2", "onDataChange:model.....--- "+model.getSkill());
//                            }
//
//                        }
//                        adapter.notifyDataSetChanged();
//
//                    }catch (Exception e){
//                        Log.d("TAG2", "onDataChange: --------perk "+ e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getPerksFire() {
        refPerks = FirebaseDatabase.getInstance().getReference("Users");

        refPerks.child(SessionManagement.getUserLoginID()).child("myApplications").child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            //perkCert = snapshot.child("perks")
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getEducation() {
        refEducation = FirebaseDatabase.getInstance().getReference("Users");

        refEducation.child(SessionManagement.getUserLoginID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists() && snapshot.hasChild("degreeName")){
                            //perkCert = snapshot.child("perks")
                            try {
                                degreeName = snapshot.child("degreeName").getValue().toString();
                            }catch (Exception e){
                                Log.d("TAG2", "onDataChange: ---"+e);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getPerks() {

            binding.recycPerks.setHasFixedSize(true);
            GridLayoutManager linearLayout = new GridLayoutManager(requireContext(),1);
            binding.recycPerks.setLayoutManager(linearLayout);
            RecycPerksAdapter adapter = new RecycPerksAdapter(getContext(),list);
            binding.recycPerks.setAdapter(adapter);

            perkRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    if (snapshot.exists()){
                        try {
                            for (DataSnapshot data : snapshot.getChildren()){
                                if (data.child("perk").getValue()!= null) {
                                    IntershipModel model = data.getValue(IntershipModel.class);
                                    list.add(model);

                                    Log.d("TAG2", "onDataChange:list.....--- " + list);
                                    Log.d("TAG2", "onDataChange:model.....--- " + model.getPerk());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Log.d("TAG2", "onDataChange: --------perk "+ e.getMessage());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void handleIntent(){
        courseName =getArguments().getString("courseName");
        companyName =getArguments().getString("companyName");
        workLocation =getArguments().getString("workLocation");
        salary =getArguments().getString("salary");
        totalMonths =getArguments().getString("totalMonths");
        companyImgUrl =getArguments().getString("companyImgUrl");
        partFullTime =getArguments().getString("partFullTime");
        key =getArguments().getString("key");
        jobOrIntern =getArguments().getString("jobOrIntern");
        compLongDes =getArguments().getString("compLongDes");
        compWeb =getArguments().getString("compWeb");
        internRole =getArguments().getString("internRole");
        openingNum =getArguments().getString("openingNum");
        skillsReq =getArguments().getString("skill");
        formLink =getArguments().getString("formLink");
        comesFrom =getArguments().getString("comesFrom");
        compId = getArguments().getString("compId");

        binding.CourseName.setText(courseName);
        binding.CompanyName.setText(companyName);
        binding.WorkLocation.setText(workLocation);
        binding.salary.setText(salary);
        binding.periodMonths.setText(totalMonths);
        binding.timeFullPart.setText(partFullTime);
        binding.JobOrIntern.setText(jobOrIntern);

        binding.AboutCompDes.setText(compLongDes);
//        binding.AboutCompDes.getText().toString();
        binding.CompWebsite.setText(compWeb);
        binding.internRoleDes.setText(internRole);
        binding.openNumber.setText(openingNum);
        binding.AboutCompTitle.setText("About "+getArguments().getString("companyName"));
        binding.skillReq.setText(skillsReq);

        Glide.with(getContext()).load(companyImgUrl).into(binding.CompanyImg);

    }



    @Override
    public boolean onBackPressed() {
        if (comesFrom!=null && comesFrom.equals("application")){
            ApplicationsFragment appFrag = new ApplicationsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,appFrag);
            transaction.commit();
        }else {
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