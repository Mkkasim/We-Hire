package com.mk.wehire.Company.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.mk.wehire.Company.models.SignUpCompany;
import com.mk.wehire.MainActivity;
import com.mk.wehire.R;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.profile.SignupProfileActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.ActivitySignupProfileCompanyBinding;

public class SignupProfileCompanyActivity extends AppCompatActivity {

    ActivitySignupProfileCompanyBinding binding;

    String email,pass,firstName,lastName,companyName,mobileNum,jobPosition,uid;
    String imageUrl;

    DatabaseReference refDetails;

    WeHireLoader weHireLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupProfileCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManagement.init(this);
        weHireLoader = new WeHireLoader(this);


        binding.FirstNameEdt.setText(SessionManagement.getFirstName());
        binding.LastNameEdt.setText(SessionManagement.getLastName());
//        binding.CompanyNameEdt.setText(SessionManagement.getCompanyName());
//        binding.JobRoleEdt.setText(SessionManagement.getJobPosition());
//        binding.MobileEdt.setText(SessionManagement.getNumber());

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
        Log.d("TAG2", "onClick: ===="+email);
        Log.d("TAG2", "onClick: ===="+pass);

        handleIntent();
        getDetailsFromFirebase();


        binding.NextBtn.setOnClickListener(view -> {

            checkAndUpload();
        });


    }



    private void checkAndUpload() {

                mobileNum = binding.MobileEdt.getText().toString().trim();
                firstName = binding.FirstNameEdt.getText().toString().trim();
                lastName = binding.LastNameEdt.getText().toString().trim();
                companyName = binding.CompanyNameEdt.getText().toString().trim();
                jobPosition = binding.JobRoleEdt.getText().toString().trim();

                if (firstName.isEmpty()){
                    binding.FirstNameEdt.setError("Field can not be empty");
                    binding.FirstNameEdt.requestFocus();
                }else if (lastName.isEmpty()){
                    binding.LastNameEdt.setError("Field can not be empty");
                    binding.LastNameEdt.requestFocus();
                }else if (companyName.isEmpty()){
                    binding.CompanyNameEdt.setError("Field can not be empty");
                    binding.CompanyNameEdt.requestFocus();
                }else if (jobPosition.isEmpty()){
                    binding.JobRoleEdt.setError("Field can not be empty");
                    binding.JobRoleEdt.requestFocus();
                }else if (mobileNum.isEmpty()){
                    binding.MobileEdt.setError("Field can not be empty");
                    binding.MobileEdt.requestFocus();
                }else if (mobileNum.length() < 10){
                    binding.MobileEdt.setError("Enter a Valid Number");
                    binding.MobileEdt.requestFocus();
                }else {


                    SessionManagement.saveUserNum(mobileNum);
                    SessionManagement.saveCompanyName(companyName);
                    SessionManagement.saveJobPosition(jobPosition);
                    Log.d("TAG2", "checkAndUpload: ----Token "+SessionManagement.getUserToken());



                    Intent in = new Intent(SignupProfileCompanyActivity.this, RegisterCompanyActivity.class);
                    startActivity(in);
                    finish();
                    Animatoo.animateSlideLeft(SignupProfileCompanyActivity.this);

                }


    }


    private void handleIntent(){
        String comesFrom = getIntent().getStringExtra("comesFrom");
        if (comesFrom != null && comesFrom.equals("login")){
            Snackbar.make(binding.getRoot(),"Please Complete Your Profile!",Snackbar.LENGTH_LONG).show();
        }else if (comesFrom != null && comesFrom.equals("splash")){
            Snackbar.make(binding.getRoot(),"Please Complete Your Profile!",Snackbar.LENGTH_LONG).show();
        }else if (comesFrom != null && comesFrom.equals("signup")){
            Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();
        }
        uid = getIntent().getStringExtra("uid");
        Log.d("TAG2", "handleIntent: ===uid"+uid);
    }

    private void getDetailsFromFirebase(){
        refDetails= FirebaseDatabase.getInstance().getReference("Companys");
        String uid = SessionManagement.getUserLoginID();
        Log.d("TAG2", "getDetailsFromFirebase: ===="+uid);
        refDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (SessionManagement.isCompLogged()){
                    if (snapshot.exists()){
                        firstName = snapshot.child("firstName").getValue().toString().trim();
                        lastName = snapshot.child("lastName").getValue().toString().trim();
                        //Log.d("TAG2", "onDataChange: ======="+isProfCom);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}