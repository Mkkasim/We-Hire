package com.mk.wehire.Company.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.mk.wehire.Company.activity.HomeActivity;
import com.mk.wehire.Company.models.SignUpCompany;
import com.mk.wehire.R;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.ActivityRegisterCompanyBinding;

import java.util.ArrayList;

public class RegisterCompanyActivity extends AppCompatActivity {

    ActivityRegisterCompanyBinding binding;

    String email,pass,firstName,lastName,companyName,mobileNum,jobPosition,
            companyShortName,yourIndustry,teamSize,companyWeb,companyLocation,uid,companyImgUrl,aboutCompany,token;



    WeHireLoader weHireLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SessionManagement.init(this);

        weHireLoader = new WeHireLoader(this);

        String[] industry = new String[]{"Information Technology","Telecommunication","Media/Entertainment/Advertising"
        ,"Finance","Automobile","Service/Hospitality","Logistics/Retail/Supply Chain","Real Estate/Construction",
                "Education and training","Connsumer Goods/FMCG","Machinery/Manufacturing","Professional Service"
        ,"Pharmaceutical/Medical","Energy/Oil/Mining/Farming","Miscellaneous"};
        String[] size = new String[]{"0-19 members","20-50 members","51-99 members","100-500 members"};
        ArrayAdapter<String> adapterIndustry = new ArrayAdapter<>(this, R.layout.gender_drop_down_item,industry);
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this, R.layout.gender_drop_down_item,size);
        binding.industryList.setAdapter(adapterIndustry);
        binding.sizeList.setAdapter(adapterSize);

        Log.d("TAG2", "onCreate: -----Token "+SessionManagement.getUserToken());

        getAllSessions();


        binding.NextBtn.setOnClickListener(view -> checkAndUpload());

        binding.backBtn.setOnClickListener(view -> {

            onBackPressed();

        });


    }

//    private void SelectIndustry() {
//
//        AlertDialog.Builder selectIndustryDialog = new AlertDialog.Builder(RegisterCompanyActivity.this);
//        View view = getLayoutInflater().inflate(R.layout.selection_layout,null);
//        RecyclerView recycIndustry = view.findViewById(R.id.recycAny);
//        //SearchView searchIndustry = view.findViewById(R.id.searchAny);
//
//
//        selectIndustryDialog.setView(view);
//
//        AlertDialog alertDialog = selectIndustryDialog.create();
//        if (alertDialog.getWindow() != null)
//            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            alertDialog.setCancelable(true);
//
//            recycIndustry.setLayoutManager(new LinearLayoutManager(this));
//            recycIndustry.setHasFixedSize(true);
//
//            industryList.add(new IndustryModel( "Technology"));
//            industryList.add(new IndustryModel( "Internet/IT/Online"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//            industryList.add(new IndustryModel( "Electronics/Telecommunication"));
//
//            industryAdapter = new RecycIndustryAdapter(RegisterCompanyActivity.this,industryList);
//            recycIndustry.setAdapter(industryAdapter);
//
//
//
//
//        alertDialog.show();
//
//    }



    private void getAllSessions() {
        email = SessionManagement.getEmail();
        firstName = SessionManagement.getFirstName();
        lastName = SessionManagement.getLastName();
        pass = SessionManagement.getPass();
        companyName = SessionManagement.getCompanyName();
        mobileNum = SessionManagement.getNumber();
        jobPosition = SessionManagement.getJobPosition();
        uid = SessionManagement.getUserLoginID();
        companyImgUrl = SessionManagement.getUserProfileImage();
        aboutCompany = SessionManagement.getCompAbout();


        //binding.LegalNameEdt.setText(companyName);

    }


    private void checkAndUpload() {

        companyName = binding.LegalNameEdt.getText().toString().trim();
        companyShortName = binding.ShortNameEdt.getText().toString().trim();
        companyLocation = binding.CompanyLocationEdt.getText().toString().trim();
        companyWeb = binding.CompanyWebEdt.getText().toString().trim();
        yourIndustry = binding.industryList.getText().toString().trim();
        teamSize = binding.sizeList.getText().toString().trim();
        aboutCompany = binding.AboutEdt.getText().toString().trim();


        if (companyName.isEmpty()){
            binding.LegalNameEdt.setError("Field can not be empty");
            binding.LegalNameEdt.requestFocus();
        }else if (companyShortName.isEmpty()){
            binding.ShortNameEdt.setError("Field can not be empty");
            binding.ShortNameEdt.requestFocus();
        }else if (companyLocation.isEmpty()){
            binding.CompanyLocationEdt.setError("Field can not be empty");
            binding.CompanyLocationEdt.requestFocus();
        }else if (yourIndustry.isEmpty()){
            binding.industryList.setError("Field can not be empty");
            binding.industryList.requestFocus();
        }else if (teamSize.isEmpty()){
            binding.sizeList.setError("Field can not be empty");
            binding.sizeList.requestFocus();
        }else if (companyWeb.isEmpty()){
            binding.CompanyWebEdt.setError("Field can not be empty");
            binding.CompanyWebEdt.requestFocus();
        }else if (aboutCompany.isEmpty()){
            binding.AboutEdt.setError("Field can not be empty");
            binding.AboutEdt.requestFocus();
        }else{

            weHireLoader.ShowDialog();
            SessionManagement.saveCompanyName(companyName);
            SessionManagement.saveCompanyShortName(companyShortName);
            SessionManagement.saveCompLocation(companyLocation);
            SessionManagement.saveCompIndustry(yourIndustry);
            SessionManagement.saveCompSize(teamSize);
            SessionManagement.saveCompWeb(companyWeb);
            SessionManagement.saveCompAbout(aboutCompany);

            token = SessionManagement.getUserToken();
            Log.d("TAG2", "checkAndUpload: ----token in reg "+SessionManagement.getUserToken());
            Log.d("TAG2", "checkAndUpload: ----token in reg "+token);


            SignUpCompany signUpCompany = new SignUpCompany(email,pass,firstName,lastName,"true",companyLocation,
                    teamSize,companyShortName,mobileNum,"No",companyName,
                    companyWeb,companyImgUrl,yourIndustry,aboutCompany,token);

            FirebaseDatabase.getInstance().getReference("Companys")
                    .child(SessionManagement.getUserLoginID())
                    .setValue(signUpCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                weHireLoader.DismissDialog();
                                SessionManagement.setCompLogin(true);

                                Intent in = new Intent(RegisterCompanyActivity.this, HomeActivity.class);
                                in.putExtra("comesFrom","reg");
                                startActivity(in);

                                finish();
                                Animatoo.animateSlideLeft(RegisterCompanyActivity.this);

                            }else {
                                weHireLoader.DismissDialog();
                                Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent in = new Intent(RegisterCompanyActivity.this,SignupProfileCompanyActivity.class);
        startActivity(in);
        finish();
        Animatoo.animateSlideRight(RegisterCompanyActivity.this);

    }
}