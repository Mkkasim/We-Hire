package com.mk.wehire.User.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.R;
import com.mk.wehire.User.adapters.RecycEducationsAdapter;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.models.EducationModel;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentResumeBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class ResumeFragment extends Fragment implements BackFragment {

    FragmentResumeBinding binding;

    String status,collegeName,startYear,endYear,degreeName,streamName,percentageUnit,percentage,key;

    DatabaseReference saveEduRef,educationRef,saveDegreeRef;
    ArrayList<SignUpUser> list;

    WeHireLoader weHireLoader;
//    HashMap<String> updateDegree;

    public ResumeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentResumeBinding.inflate(inflater,container,false);

        SessionManagement.init(requireContext());
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        MenuItem item1 = toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setTitle("Resume");
        toolbar.setNavigationIcon(R.drawable.menu);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer);
        toolbar.setNavigationOnClickListener(view -> {
            drawer.openDrawer(GravityCompat.START);
        });

        weHireLoader = new WeHireLoader(getContext());



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDetails();
        getEducations();

        binding.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileEditFragment profEdtFrag = new ProfileEditFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,profEdtFrag);
                transaction.commit();
            }
        });

        binding.addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("comesFrom","top");
                showBottomSheetDialog(bundle);
            }
        });

    }

    private void showBottomSheetDialog(Bundle bundle) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(),R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.addeducation_bottomsheet);

        ImageView closeImg = bottomSheetDialog.findViewById(R.id.close);
        MaterialAutoCompleteTextView statusList = bottomSheetDialog.findViewById(R.id.statusList);
        MaterialAutoCompleteTextView startYearList = bottomSheetDialog.findViewById(R.id.startYearList);
        MaterialAutoCompleteTextView endYearList = bottomSheetDialog.findViewById(R.id.endYearList);
        TextInputEditText degreeNameEdt = bottomSheetDialog.findViewById(R.id.DegreeEdt);
        TextInputEditText streamNameEdt = bottomSheetDialog.findViewById(R.id.streamEdt);
        MaterialAutoCompleteTextView performanceList = bottomSheetDialog.findViewById(R.id.unitList);
        TextInputEditText performanceEdt = bottomSheetDialog.findViewById(R.id.performannceEdt);
        TextInputEditText collegeNameEdt = bottomSheetDialog.findViewById(R.id.collegeNameEdt);
        MaterialButton saveBtn = bottomSheetDialog.findViewById(R.id.saveBtn);


        String[] status = new String[]{"Persuing","Completed"};
        String[] startYear = new String[]{"2024","2023","2022","2021","2020","2019","2018","2017","2016","2015","2014",
                "2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
                "1999","1998","1997","1996","1995","1994","1993","1992","1991","1990"};
        String[] endYear = new String[]{"2024","2023","2022","2021","2020","2019","2018","2017","2016","2015","2014",
                "2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
                "1999","1998","1997","1996","1995","1994","1993","1992","1991","1990"};

        String[] performance = new String[]{"Percentage","CGPA"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,status);
        ArrayAdapter<String> adapterStartYear = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,startYear);
        ArrayAdapter<String> adapterEndYear = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,endYear);
        ArrayAdapter<String> adapterperformane = new ArrayAdapter<>(requireContext(), R.layout.gender_drop_down_item,performance);
        statusList.setAdapter(adapterStatus);
        startYearList.setAdapter(adapterStartYear);
        endYearList.setAdapter(adapterEndYear);
        performanceList.setAdapter(adapterperformane);

        statusList.setText(bundle.getString("status"));
        collegeNameEdt.setText(bundle.getString("collegeName"));
        startYearList.setText(bundle.getString("startYear"));
        endYearList.setText(bundle.getString("endYear"));
        degreeNameEdt.setText(bundle.getString("degree"));
        streamNameEdt.setText(bundle.getString("stream"));
        performanceList.setText(bundle.getString("performanceScale"));
        performanceEdt.setText(bundle.getString("performance"));

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEducation(bottomSheetDialog,degreeNameEdt,streamNameEdt,collegeNameEdt,startYearList,endYearList,performanceEdt,performanceList,statusList,bundle);
            }
        });


        bottomSheetDialog.show();

    }

    private void saveEducation(BottomSheetDialog bottomSheetDialog, TextInputEditText degreeNameEdt, TextInputEditText streamNameEdt, TextInputEditText collegeNameEdt, MaterialAutoCompleteTextView startYearList, MaterialAutoCompleteTextView endYearList, TextInputEditText performanceEdt, MaterialAutoCompleteTextView performanceList, MaterialAutoCompleteTextView statusList, Bundle bundle) {
        degreeName = degreeNameEdt.getText().toString().trim();
        streamName = streamNameEdt.getText().toString().trim();
        collegeName= collegeNameEdt.getText().toString().trim();
        percentage= performanceEdt.getText().toString().trim();
        startYear= startYearList.getText().toString().trim();
        endYear= endYearList.getText().toString().trim();
        percentageUnit= performanceList.getText().toString().trim();
        status= statusList.getText().toString().trim();

        if (degreeName.isEmpty()){
            degreeNameEdt.setError("Field can not be empty");
            degreeNameEdt.requestFocus();
        }else if (streamName.isEmpty()){
            streamNameEdt.setError("Field can not be empty");
            streamNameEdt.requestFocus();
        }else if (collegeName.isEmpty()){
            collegeNameEdt.setError("Field can not be empty");
            collegeNameEdt.requestFocus();
        }else if (percentage.isEmpty()){
            performanceEdt.setError("Field can not be empty");
            performanceEdt.requestFocus();
        }else if (startYear.isEmpty()){
            startYearList.setError("Field can not be empty");
            startYearList.requestFocus();
        }else if (endYear.isEmpty()){
            endYearList.setError("Field can not be empty");
            endYearList.requestFocus();
        }else if (percentageUnit.isEmpty()){
            performanceList.setError("Field can not be empty");
            performanceList.requestFocus();
        }else if (status.isEmpty()){
            statusList.setError("Field can not be empty");
            statusList.requestFocus();
        }else {
            weHireLoader.ShowDialog();

            saveEduRef = FirebaseDatabase.getInstance().getReference("Users").child(SessionManagement.getUserLoginID())
                    .child("Educations");
            saveDegreeRef = FirebaseDatabase.getInstance().getReference("Users").child(SessionManagement.getUserLoginID());

            if (bundle.getString("comesFrom").equals("resume")){
                key = bundle.getString("key");
            }else if (bundle.getString("comesFrom").equals("top")){
                key = saveEduRef.push().getKey();
            }

            Log.d("TAG2", "saveEducation: ----"+ key);

            SignUpUser signUpUser = new SignUpUser(status,collegeName,startYear,endYear,degreeName,streamName,percentageUnit,percentage,key);



            saveEduRef.child(key).setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        weHireLoader.DismissDialog();
                        Snackbar.make(binding.getRoot(),degreeName+" Added SuccessFully",Snackbar.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(binding.getRoot(),"Something Went Wrong! Try Again.",Snackbar.LENGTH_SHORT).show();

                }
            });

            saveDegreeRef.child("degreeName").setValue(degreeName+" "+streamName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    SessionManagement.saveDegreeName(degreeName+" "+streamName);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG2", "onFailure: ----"+e);
                }
            });
        }

    }

    private void getDetails() {

        binding.Nametxt.setText(SessionManagement.getFirstName()+" "+SessionManagement.getLastName());
        binding.emailtxt.setText(SessionManagement.getEmail());
        binding.mobiletxt.setText(SessionManagement.getNumber());
        binding.locattxt.setText(SessionManagement.getUserLocation());

    }

    private void getEducations() {

        list = new ArrayList<>();
        educationRef = FirebaseDatabase.getInstance().getReference("Users").child(SessionManagement.getUserLoginID())
                .child("Educations");
        Log.d("TAG2", "getApplications: ------- "+SessionManagement.getUserLoginID());
        binding.recycEducation.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.recycEducation.setLayoutManager(linearLayout);

        RecycEducationsAdapter recycEducationsAdapter = new RecycEducationsAdapter(getContext(), list, new RecycEducationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SignUpUser educationModel) {


            }
        });
        binding.recycEducation.setAdapter(recycEducationsAdapter);
        educationRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            SignUpUser educationModel = dataSnapshot.getValue(SignUpUser.class);
                            list.add(educationModel);

                        }
                        //Collections.reverse(list);
                        recycEducationsAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: ===="+e.getMessage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG2", "onCancelled: ---- "+error.getMessage());
            }
        });

        recycEducationsAdapter.setOnClickListener(new RecycEducationsAdapter.OnClickListener() {
            @Override
            public void onEditClick(View view, int position, SignUpUser education) {
                Bundle bundle = new Bundle();
                bundle.putString("status",education.getGraduationStatus());
                bundle.putString("collegeName",education.getCollegeName());
                bundle.putString("startYear",education.getStartYear());
                bundle.putString("endYear",education.getEndYear());
                bundle.putString("degree",education.getDegreeName());
                bundle.putString("stream",education.getStreamName());
                bundle.putString("performanceScale",education.getPerformanceScale());
                bundle.putString("performance",education.getPerformane());
                bundle.putString("key",education.getKey());
                bundle.putString("comesFrom","resume");

                showBottomSheetDialog(bundle);

            }

            @Override
            public void onDeleteClick(View view, int position, SignUpUser education) {

                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Do you want to Remove ?")
                        .setMessage("Your education details will be removed.")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        educationRef.child(education.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Snackbar.make(binding.getRoot(),education.getDegreeName()+" Details SuccesFuly Removed",Snackbar.LENGTH_SHORT).show();
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