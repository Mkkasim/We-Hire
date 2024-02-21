package com.mk.wehire.User.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

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
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.databinding.ActivitySignupProfileBinding;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.sessionmanager.SessionManagement;

public class SignupProfileActivity extends AppCompatActivity {

    ActivitySignupProfileBinding binding;
    String email,pass,gender,firstName,lastName,mobileNum,location,language,uid;
    String imageUrl;

    DatabaseReference refDetails;

    WeHireLoader weHireLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SessionManagement.init(this);

        weHireLoader = new WeHireLoader(this);

        String[] type = new String[]{"Male","Female","Others"};
        String[] lang = new String[]{"English","Hindi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.gender_drop_down_item,type);
        ArrayAdapter<String> adapterLang = new ArrayAdapter<>(this, R.layout.gender_drop_down_item,lang);
        binding.filledExposed.setAdapter(adapter);
        binding.PrefLanguage.setAdapter(adapterLang);

        binding.FirstNameEdt.setText(SessionManagement.getFirstName());
        binding.LastNameEdt.setText(SessionManagement.getLastName());
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

        binding.NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = binding.filledExposed.getText().toString().trim();
                language = binding.PrefLanguage.getText().toString().trim();
                mobileNum = binding.MobileEdt.getText().toString().trim();
                location = binding.LocationEdt.getText().toString().trim();
                firstName = binding.FirstNameEdt.getText().toString().trim();
                lastName = binding.LastNameEdt.getText().toString().trim();

                if (binding.filledExposed.getText().toString().isEmpty()){
                    binding.filledExposed.setError("Choose Your Gender");
                    binding.filledExposed.requestFocus();
                }else if (firstName.isEmpty()){
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
                }else if (language.isEmpty()){
                    binding.PrefLanguage.setError("Field can not be empty");
                    binding.PrefLanguage.requestFocus();
                }else if (location.isEmpty()){
                    binding.LocationEdt.setError("Field can not be empty");
                    binding.LocationEdt.requestFocus();

                }else {

                    weHireLoader.ShowDialog();
                    SessionManagement.saveUserFirstName(firstName);
                    SessionManagement.saveUserLastName(lastName);
                    SessionManagement.saveUserNum(mobileNum);
                    SessionManagement.saveUserLocation(location);
                    SessionManagement.saveLanguage(language);
                    SessionManagement.saveGender(gender);
                    SignUpUser signUpUser = new SignUpUser(email,pass,firstName,lastName,"true","No",location,gender,language,mobileNum,SessionManagement.getUserToken());
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(SessionManagement.getUserLoginID())
                            .setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        weHireLoader.DismissDialog();
                                        Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();

                                        Intent in = new Intent(SignupProfileActivity.this, MainActivity.class);
                                        startActivity(in);
                                        finish();
                                        Animatoo.animateSlideLeft(SignupProfileActivity.this);

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
        refDetails= FirebaseDatabase.getInstance().getReference("Users");
        String uid = SessionManagement.getUserLoginID();
        Log.d("TAG2", "getDetailsFromFirebase: ===="+uid);
        refDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (SessionManagement.isLogged()){
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