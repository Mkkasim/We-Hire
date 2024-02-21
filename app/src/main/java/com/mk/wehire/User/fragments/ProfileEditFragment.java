package com.mk.wehire.User.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.MainActivity;
import com.mk.wehire.R;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.profile.SignupProfileActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.FragmentProfileEditBinding;

import net.skoumal.fragmentback.BackFragment;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment implements BackFragment {

    FragmentProfileEditBinding binding;

    String email,pass,firstName,lastName,mobileNum,location,firstNameF,lastNameF,genderF,languageF,mobileF,locatF;
    DatabaseReference refDetails;

    WeHireLoader weHireLoader;

    public ProfileEditFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater,container,false);
        SessionManagement.init(requireContext());

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(view1 -> {
             onBackPressed();
        });
        MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
        weHireLoader = new WeHireLoader(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getDetailsFromFirebase();
        getDetails();
        binding.NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNum = binding.MobileEdt.getText().toString().trim();
                location = binding.LocationEdt.getText().toString().trim();
                firstName = binding.FirstNameEdt.getText().toString().trim();
                lastName = binding.LastNameEdt.getText().toString().trim();

                if (firstName.isEmpty()){
                    binding.FirstNameEdt.setError("Field can not be empty");
                    binding.FirstNameEdt.requestFocus();
                }else if (lastName.isEmpty()){
                    binding.LastNameEdt.setError("Field can not be empty");
                    binding.LastNameEdt.requestFocus();
                }else if (mobileNum.isEmpty()){
                    binding.MobileEdt.setError("Field can not be empty");
                    binding.MobileEdt.requestFocus();
                }else if (mobileNum.length() < 10){
                    binding.MobileEdt.setError("Enter a Valid Number");
                    binding.MobileEdt.requestFocus();
                }else if (location.isEmpty()){
                    binding.LocationEdt.setError("Field can not be empty");
                    binding.LocationEdt.requestFocus();

                }else {

                    weHireLoader.ShowDialog();
                    SessionManagement.saveUserFirstName(firstName);
                    SessionManagement.saveUserLastName(lastName);
                    SessionManagement.saveUserNum(mobileNum);
                    SessionManagement.saveUserLocation(location);
                    Map<String, Object> updateProfile = new HashMap<>();
                    SignUpUser signUpUser = new SignUpUser(SessionManagement.getEmail(),SessionManagement.getPass(),SessionManagement.getFirstName(),
                            SessionManagement.getLastName(),"true","No",SessionManagement.getUserLocation(),
                            SessionManagement.getGender(),SessionManagement.getLanguage(),SessionManagement.getNumber(),SessionManagement.getUserToken());

//                    updateProfile.put("firstName",firstName);
//                    updateProfile.put("lastName",lastName);
//                    updateProfile.put("location",location);
//                    updateProfile.put("mobile",mobileNum);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(SessionManagement.getUserLoginID())
                            .setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        weHireLoader.DismissDialog();
                                        Snackbar.make(binding.getRoot(),"Updated Successfuly",Snackbar.LENGTH_SHORT).show();



                                    }else {
                                        weHireLoader.DismissDialog();
                                        Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }



    private void getDetails() {
        binding.FirstNameEdt.setText(SessionManagement.getFirstName());
        binding.LastNameEdt.setText(SessionManagement.getLastName());
        binding.LocationEdt.setText(SessionManagement.getUserLocation());
        binding.MobileEdt.setText(SessionManagement.getNumber());
        binding.MyName.setText("Hii "+SessionManagement.getFirstName()+",");
        try {
            Glide.with(this).load(SessionManagement.getUserProfileImage())
                    .error(R.drawable.wehire_logo).placeholder(R.drawable.wehire_logo)
                    .into(binding.profileImage);
        }catch (Exception e){
            Log.d("TAG2", "onCreate: "+e.getMessage());
        }

        email = SessionManagement.getEmail();
        pass = SessionManagement.getPass();
        genderF = SessionManagement.getGender();
        languageF = SessionManagement.getLanguage();
        locatF = SessionManagement.getUserLocation();
        mobileF = SessionManagement.getNumber();
        Log.d("TAG2", "onClick: ===="+email);
        Log.d("TAG2", "onClick: ===="+pass);
        Log.d("TAG2", "onClick: ===="+genderF);
        Log.d("TAG2", "onClick: ===="+languageF);
        Log.d("TAG2", "onClick: ===="+locatF);
        Log.d("TAG2", "onClick: ===="+mobileF);
    }

    private void getDetailsFromFirebase(){
        refDetails= FirebaseDatabase.getInstance().getReference("Users");
        String uid = SessionManagement.getUserLoginID();
        Log.d("TAG2", "getDetailsFromFirebase: ===="+uid);
        refDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (SessionManagement.isLogged()){
                    if (snapshot.exists()){
                        firstNameF = snapshot.child("firstName").getValue().toString().trim();
                        lastNameF = snapshot.child("lastName").getValue().toString().trim();
                        genderF = snapshot.child("gender").getValue().toString().trim();
                        languageF = snapshot.child("language").getValue().toString().trim();
                        mobileF = snapshot.child("mobile").getValue().toString().trim();
                        locatF = snapshot.child("location").getValue().toString().trim();
                        SessionManagement.saveUserFirstName(firstNameF);
                        SessionManagement.saveUserLastName(lastNameF);
                        SessionManagement.saveGender(genderF);
                        SessionManagement.saveLanguage(languageF);
                        SessionManagement.saveUserLocation(locatF);
                        SessionManagement.saveUserNum(mobileF);
                        //Log.d("TAG2", "onDataChange: ======="+isProfCom);
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
        ResumeFragment resumeFrag = new ResumeFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,resumeFrag);
        transaction.commit();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}