package com.mk.wehire.Company.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mk.wehire.R;
import com.mk.wehire.User.models.FCM;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.models.Notification;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentPostJobsInternshipBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostJobsInternshipFragment extends Fragment implements BackFragment {

    FragmentPostJobsInternshipBinding binding;

    String title,descriptionRes,openings,salary,months,type,partFull,wfh,location,perks,
            skills,formLink,aboutCompany,compName,compWeb;
    String perkCert,perkLetter,perkFlex,perkDays;

    List<String> perksToAdd = new ArrayList<String>();
    List<String> allPerks = new ArrayList<String>();

    boolean[] selectedPerks;
    ArrayList<Integer> perksList = new ArrayList<>();
    String[] perksArray = {"Certificate", "Letter of recommendation", "Flexible work hours", "5 days a week"};

    DatabaseReference postJobRef,postInternRef,saveInternshipRef,saveJobRef,refNotification;
    WeHireLoader weHireLoader;

    Map<String, Object> updatePerks;
    Context context;
    Activity activity;

    public PostJobsInternshipFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostJobsInternshipBinding.inflate(inflater,container,false);
        SessionManagement.init(getContext());
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarHome);
        toolbar.setTitle("Post Jobs & Internships");
        MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(view1 -> {

            onBackPressed();


        });
        weHireLoader = new WeHireLoader(getContext());

        context = getContext();
        activity = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String[] Openings = new String[]{"1","2","4","8","10","20","50","100"};
        String[] Salary = new String[]{"1k-2k /month","2k-4k /month","4k-8k /month","8k-10k /month",
                "20k-15k /month", "15k-20k /month","20k-40k /month","1 lac-2 lac/PA","2 lac-4 lac/PA",
                "4 lac-8 lac/PA","8 lac-10 lac/PA","10 lac-15 lac/PA","15 lac-20 lac/PA"};
        String[] Months = new String[]{"1 Month","2 Months","4 Months","6 Months","Permanent"};
        String[] PartOrFull = new String[]{"Part time allowed","Full time"};
        String[] Type = new String[]{"Job","Internship"};
        ArrayAdapter<String> adapterSalary = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,Salary);
        ArrayAdapter<String> adapterPartOrFull = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,PartOrFull);
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,Months);
        ArrayAdapter<String> adapterOpening = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,Openings);
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,Type);
        binding.openingList.setAdapter(adapterOpening);
        binding.salaryList.setAdapter(adapterSalary);
        binding.monthsList.setAdapter(adapterMonths);
        binding.partFullList.setAdapter(adapterPartOrFull);
        binding.jobInternList.setAdapter(adapterType);

        updatePerks = new HashMap<>();

        selectedPerks = new boolean[perksArray.length];

        setPerks();

        binding.wfhSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.wfhSwitch.isChecked()){
                    location = "Work From Home";
                    binding.JobLocation.setEnabled(false);
                    binding.JobLocationEdt.setText("Work From Home");
                    binding.JobLocation.setVisibility(View.GONE);
                }else {
                    binding.JobLocation.setVisibility(View.VISIBLE);
                    binding.JobLocation.setEnabled(true);
                    wfh = binding.JobLocationEdt.getText().toString().trim();
                }
            }
        });


        binding.PostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postJobInternship();

            }
        });



    }

    private void setPerks() {
//        if (SessionManagement.isCertificateChecked()){
//            binding.chipCertificate.setChecked(true);
//            binding.chipCertificate.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
//        }else{
//            binding.chipCertificate.setChecked(false);
//            binding.chipCertificate.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//        }
//
//        if (SessionManagement.isLetterRecChecked()){
//            binding.chipLofRec.setChecked(true);
//            binding.chipLofRec.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
//        }else {
//            binding.chipLofRec.setChecked(false);
//            binding.chipLofRec.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//        }
//        if (SessionManagement.isFlexWorkChecked()){
//            binding.chipFlexibleWH.setChecked(true);
//            binding.chipFlexibleWH.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
//        }else {
//            binding.chipFlexibleWH.setChecked(false);
//            binding.chipFlexibleWH.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//        }
//        if (SessionManagement.isDaysWeekChecked()){
//            binding.chipDaysWeek.setChecked(true);
//            binding.chipDaysWeek.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
//        }else {
//            binding.chipDaysWeek.setChecked(false);
//            binding.chipDaysWeek.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//        }

        binding.chipCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.chipCertificate.isChecked()){
                    perkCert = binding.chipCertificate.getText().toString();
                    //SessionManagement.setCertificateChecked(true);
                    binding.chipCertificate.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
                }else {
                    perkCert = "null";
                    perksToAdd.remove(perkCert);
                    //SessionManagement.setCertificateChecked(false);
                    binding.chipCertificate.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                }
            }
        });

        binding.chipLofRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.chipLofRec.isChecked()){
                    perkLetter = binding.chipLofRec.getText().toString();
                    //SessionManagement.setLetterRecChecked(true);
                    binding.chipLofRec.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
                }else {
                    perkLetter = "null";
                    perksToAdd.remove(perkLetter);
                    //SessionManagement.setLetterRecChecked(false);
                    binding.chipLofRec.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                }
            }
        });
        binding.chipFlexibleWH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.chipFlexibleWH.isChecked()){
                    perkFlex = binding.chipFlexibleWH.getText().toString();
                    //SessionManagement.setFlexWorkChecked(true);
                    binding.chipFlexibleWH.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
                }else {
                    perkFlex = "null";
                    perksToAdd.remove(perkFlex);
                    //SessionManagement.setFlexWorkChecked(false);
                    binding.chipFlexibleWH.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                }
            }
        });
        binding.chipDaysWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.chipDaysWeek.isChecked()){
                    perkDays = binding.chipDaysWeek.getText().toString();
                    //SessionManagement.setDaysWeekChecked(true);
                    binding.chipDaysWeek.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.litegreen)));
                }else {
                    perkDays = "null";
                    //SessionManagement.setDaysWeekChecked(false);
                    perksToAdd.remove(perkDays);
                    binding.chipDaysWeek.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                }
            }
        });
    }

    private void openPerksDialog(){
//        AlertDialog.Builder perksDialog = new AlertDialog.Builder(requireContext());
//        View view = getLayoutInflater().inflate(R.layout.perks_selection_layout,null);
//        FloatingActionButton doneBtn = view.findViewById(R.id.done);
//        FloatingActionButton cancelBtn = view.findViewById(R.id.cancel);
//        Chip chipCertificate = view.findViewById(R.id.chipCertificate);
//        Chip chipLetterOR = view.findViewById(R.id.chipLofRec);
//        Chip chipFlexWH = view.findViewById(R.id.chipFlexibleWH);
//        Chip chipDaysWeek = view.findViewById(R.id.chipDaysWeek);
//
//        perksDialog.setView(view);
//
//        AlertDialog alertDialog = perksDialog.create();
//        if (alertDialog.getWindow() != null)
//            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            alertDialog.setCancelable(false);
//
//
//
//        doneBtn.setOnClickListener(view1 -> {
//
//            Toast.makeText(getContext(), ""+perkCert + perkLetter+ perkFlex + perkDays,Toast.LENGTH_SHORT).show();
//
//            if (perkCert != null){
//                perksToAdd.add(perkCert);
//            } if (perkLetter != null){
//                perksToAdd.add(perkLetter);
//            } if (perkFlex != null){
//                perksToAdd.add(perkFlex);
//            } if (perkDays != null){
//                perksToAdd.add(perkDays);
//            }
//            allPerks.addAll(perksToAdd);
//            //binding.Perks.setText(Arrays.toString(allPerks.toArray()));
//
//            alertDialog.dismiss();
//
//        });
//        cancelBtn.setOnClickListener(view1 -> {
//            allPerks.removeAll(perksToAdd);
//            alertDialog.dismiss();
//        });
//
//        alertDialog.show();

    }

    private void selectPerksArrayList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // set title
        builder.setTitle("Select Perks");

        // set dialog non cancelable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(perksArray, selectedPerks, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position  in lang list
                    perksList.add(i);
                    // Sort array list
                    Collections.sort(perksList);
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    perksList.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Initialize string builder
                StringBuilder stringBuilder = new StringBuilder();
                // use for loop
                for (int j = 0; j < perksList.size(); j++) {
                    // concat array value
                    stringBuilder.append(perksArray[perksList.get(j)]);
                    // check condition
                    if (j != perksList.size() - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ");
                    }
                }
                // set text on textView
                //binding.Perks.setText(stringBuilder.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selectedPerks.length; j++) {
                    // remove all selection
                    selectedPerks[j] = false;
                    // clear language list
                    perksList.clear();

                    // clear text view value
                    //binding.Perks.setText("");
                }
            }
        });
        // show dialog
        builder.show();

    }

    private void postJobInternship() {

        title = binding.JobRoleEdt.getText().toString().trim();
        descriptionRes= binding.JobDesEdt.getText().toString().trim();
        openings= binding.openingList.getText().toString().trim();
        salary= binding.salaryList.getText().toString().trim();
        months= binding.monthsList.getText().toString().trim();
        type= binding.jobInternList.getText().toString().trim();
        partFull= binding.partFullList.getText().toString().trim();
        location = binding.JobLocationEdt.getText().toString().trim();
        skills = binding.requiredSkillsEdt.getText().toString();
        formLink = binding.formLinkEdt.getText().toString();
        aboutCompany = binding.AboutComapanyEdt.getText().toString();
        compName = binding.CompanyNameEdt.getText().toString();
        compWeb = binding.companyWebEdt.getText().toString();

        if (title.isEmpty()){
            binding.JobRoleEdt.setError("Field can not be empty");
            binding.JobRoleEdt.requestFocus();
        }else if (descriptionRes.isEmpty()){
            binding.JobDesEdt.setError("Field can not be empty");
            binding.JobDesEdt.requestFocus();
        }else if (openings.isEmpty()){
            binding.openingList.setError("Field can not be empty");
            binding.openingList.requestFocus();
        }else if (salary.isEmpty()){
            binding.salaryList.setError("Field can not be empty");
            binding.salaryList.requestFocus();
        }else if (months.isEmpty()){
            binding.monthsList.setError("Field can not be empty");
            binding.monthsList.requestFocus();
        }else if (type.isEmpty()){
            binding.jobInternList.setError("Field can not be empty");
            binding.jobInternList.requestFocus();
        }else if (partFull.isEmpty()){
            binding.partFullList.setError("Field can not be empty");
            binding.partFullList.requestFocus();
        }else if (location.isEmpty()){
            binding.JobLocationEdt.setError("Field can not be empty");
            binding.JobLocationEdt.requestFocus();
        }else if (formLink.isEmpty()){
            binding.formLinkEdt.setError("Field can not be empty");
            binding.formLinkEdt.requestFocus();
        }else if (skills.isEmpty()){
            binding.requiredSkillsEdt.setError("Field can not be empty");
            binding.requiredSkillsEdt.requestFocus();
        }else if (perkCert.isEmpty()){
            binding.chipCertificate.setError("Its Compulsory to give certificate");
            binding.chipCertificate.requestFocus();
        }else if (compName.isEmpty()){
            binding.CompanyNameEdt.setError("Field can not be empty");
            binding.CompanyNameEdt.requestFocus();
        }else if (aboutCompany.isEmpty()){
            binding.AboutComapanyEdt.setError("Field can not be empty");
            binding.AboutComapanyEdt.requestFocus();
        }else if (compWeb.isEmpty()){
            binding.companyWebEdt.setError("Field can not be empty");
            binding.companyWebEdt.requestFocus();
        }else {

            weHireLoader.ShowDialog();
//            Toast.makeText(getContext(), "" +perkCert+ perkLetter+ perkFlex+
//                    perkDays+ wfh + title + descriptionRes + openings + salary +
//                    months + type + partFull + location, Toast.LENGTH_SHORT).show();

            postInternRef = FirebaseDatabase.getInstance().getReference("Internships");
            postJobRef = FirebaseDatabase.getInstance().getReference("Jobs");
            saveJobRef = FirebaseDatabase.getInstance().getReference("Companys");
            saveInternshipRef = FirebaseDatabase.getInstance().getReference("Companys");

            if (type.equals("Internship")){
                postInternship();
            }else if (type.equals("Job")){

                postJob();
            }

        }



    }

    private void postInternship() {
        String key = postInternRef.push().getKey();
        IntershipModel postIntern = new IntershipModel(title,location,compName,
                salary,months,SessionManagement.getCompanyProfileImage(),partFull,key,type,descriptionRes,
                compWeb,openings,skills,aboutCompany,formLink,"Posted",SessionManagement.getUserLoginID());

        IntershipModel postPerksCert = new IntershipModel(perkCert);
        IntershipModel postPerksLetter = new IntershipModel(perkLetter);
        IntershipModel postPerksFlex = new IntershipModel(perkFlex);
        IntershipModel postPerksDays = new IntershipModel(perkDays);





        postInternRef.child(key).setValue(postIntern).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    postInternRef.child(key).child("perks").child("1").child("perk").setValue(perkCert);
                    postInternRef.child(key).child("perks").child("2").child("perk").setValue(perkLetter);
                    postInternRef.child(key).child("perks").child("3").child("perk").setValue(perkFlex);
                    postInternRef.child(key).child("perks").child("4").child("perk").setValue(perkDays);

                    saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(key).setValue(postIntern)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(key).child("perks")
                                        .child("1").child("perk").setValue(perkCert);
                                saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(key).child("perks")
                                        .child("2").child("perk").setValue(perkLetter);
                                saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(key).child("perks")
                                        .child("3").child("perk").setValue(perkFlex);
                                saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(key).child("perks")
                                        .child("4").child("perk").setValue(perkDays);


                                FCM fcmSend = new FCM("/topics/users","Apply Now!","New Internships Are Posted Please Apply If Your Resume Matches.",
                                        "status",context,activity);
                                fcmSend.SendNotifications();


                            }
                        }
                    });



                    weHireLoader.DismissDialog();
                    Snackbar.make(binding.getRoot(),type+" posted SuccessFully",Snackbar.LENGTH_SHORT).show();
                    onBackPressed();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                weHireLoader.DismissDialog();
                Snackbar.make(binding.getRoot(),"Something went wrong",Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void postJob() {
        String keyjob = postJobRef.push().getKey();
        IntershipModel postJob = new IntershipModel(title,location,compName,
                salary,months,SessionManagement.getCompanyProfileImage(),partFull,keyjob,type,descriptionRes,
                compWeb,openings,skills,aboutCompany,formLink);

        IntershipModel postPerksCert = new IntershipModel(perkCert);
        IntershipModel postPerksLetter = new IntershipModel(perkLetter);
        IntershipModel postPerksFlex = new IntershipModel(perkFlex);
        IntershipModel postPerksDays = new IntershipModel(perkDays);



        postJobRef.child(keyjob).setValue(postJob).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    postJobRef.child(keyjob).child("perks").child("1").child("perk").setValue(perkCert);
                    postJobRef.child(keyjob).child("perks").child("2").child("perk").setValue(perkLetter);
                    postJobRef.child(keyjob).child("perks").child("3").child("perk").setValue(perkFlex);
                    postJobRef.child(keyjob).child("perks").child("4").child("perk").setValue(perkDays);

                    saveJobRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(keyjob).setValue(postJob)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(keyjob)
                                                .child("perks").child("1").child("perk").setValue(perkCert);
                                        saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(keyjob)
                                                .child("perks").child("2").child("perk").setValue(perkLetter);
                                        saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(keyjob)
                                                .child("perks").child("3").child("perk").setValue(perkFlex);
                                        saveInternshipRef.child(SessionManagement.getUserLoginID()).child("myPosts").child(keyjob)
                                                .child("perks").child("4").child("perk").setValue(perkDays);
                                    }
                                }
                            });

                    weHireLoader.DismissDialog();

                    Snackbar.make(binding.getRoot(),type+" posted SuccessFully",Snackbar.LENGTH_SHORT).show();

                    onBackPressed();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                weHireLoader.DismissDialog();
                Snackbar.make(binding.getRoot(),"Something went wrong",Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onBackPressed() {
        MyPostsFragment myPostFrg = new MyPostsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,myPostFrg);
        transaction.commit();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}