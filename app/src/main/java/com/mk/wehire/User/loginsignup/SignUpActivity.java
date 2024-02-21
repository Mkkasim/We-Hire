package com.mk.wehire.User.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.Company.activity.HomeActivity;
import com.mk.wehire.Company.models.SignUpCompany;
import com.mk.wehire.Company.profile.SignupProfileCompanyActivity;
import com.mk.wehire.MainActivity;
import com.mk.wehire.R;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.databinding.ActivitySignUpBinding;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.profile.SignupProfileActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    float v=0;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    String firstGName,firstNameF;
    String lastGName,lastNameF;
    String isProfComF,passF,uidF;
    String GEmail;
    String GFamilyName;
    String asIntern,asHiring;
    Uri GImageUrl;
    FirebaseAuth auth;
    DatabaseReference ref,Gref,GrefCompany;

    WeHireLoader weHireLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        SessionManagement.init(this);
        weHireLoader = new WeHireLoader(this);

        setGoogleLogin();

        binding.login.setOnClickListener(view -> {
            Intent in = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(in);
            finish();
            Animatoo.animateSlideRight(SignUpActivity.this);
        });

        binding.GoogleSignUp.setOnClickListener(view -> {
            if (binding.searchHireGroup.getCheckedRadioButtonId() == -1){
                Snackbar.make(binding.getRoot(),"Please Select Your Role",Snackbar.LENGTH_SHORT).show();

            }else {
                signIn();
            }
        });


        binding.EmailId.setTranslationX(800);
        binding.Password.setTranslationX(800);
        binding.FirstName.setTranslationX(800);
        binding.LastName.setTranslationX(800);
        binding.signupBtn.setTranslationX(800);

        binding.EmailId.setAlpha(v);
        binding.Password.setAlpha(v);
        binding.FirstName.setAlpha(v);
        binding.LastName.setAlpha(v);
        binding.signupBtn.setAlpha(v);

        binding.EmailId.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.Password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.FirstName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.LastName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.searchHireGroup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.signupBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        binding.signupBtn.setOnClickListener(view1 -> {
            signUpUser();
        });

        checkSelector();

    }

    private void checkSelector() {

        binding.searchAsIntern.setOnClickListener(view -> {
            binding.note.setText("Note - You will be logged in as a job or internship seeker");

        });

        binding.searchAsCompany.setOnClickListener(view -> {
            binding.note.setText("Note - You will be logged in as a organization. You can post jobs and internships");
        });



    }

    private void signUpUser() {


        String email = binding.EmailIdEdt.getText().toString().trim();
        String pass = binding.PasswordEdt.getText().toString().trim();
        String first = binding.FirstNameEdt.getText().toString().trim();
        String last = binding.LastNameEdt.getText().toString().trim();


        if (email.isEmpty()){
            binding.EmailIdEdt.setError("Field can not be empty");
            binding.EmailIdEdt.requestFocus();
        }else if (!isEmailValid(email) ){
            binding.EmailIdEdt.setError("Email is not valid");
            binding.EmailIdEdt.requestFocus();
        }else if (pass.isEmpty()){
            binding.PasswordEdt.setError("Field can not be empty");
            binding.PasswordEdt.requestFocus();
        }else if (pass.length() < 6){
            binding.PasswordEdt.setError("Password should be atleast 6 character");
            binding.PasswordEdt.requestFocus();
        }else if (first.isEmpty()){
            binding.FirstNameEdt.setError("Field can not be empty");
            binding.FirstNameEdt.requestFocus();
        }else if (last.isEmpty()){
            binding.LastNameEdt.setError("Field can not be empty");
            binding.LastNameEdt.requestFocus();

        }else if (binding.searchHireGroup.getCheckedRadioButtonId() == -1){
            Snackbar.make(binding.getRoot(),"Please Select Your Role",Snackbar.LENGTH_SHORT);

        }else {

            if (binding.searchAsIntern.isChecked()){
                asIntern= "Intern";
                ref = FirebaseDatabase.getInstance().getReference("Users");
            }else if (binding.searchAsCompany.isChecked()){
                asHiring = "Companys";
                ref = FirebaseDatabase.getInstance().getReference("Companys");
            }

            weHireLoader.ShowDialog();
            String refMail = email.substring(0, email.length() - 10);
            String resultMail = refMail.replaceAll("[-+.^:@,]","");
            String refPass = email.substring(0,4).trim();
            String resultPass = refPass.replaceAll("[-+.^:@,]","");
            Log.d("TAG2", "signUpUser: "+refMail);
            Log.d("TAG2", "signUpUser: "+refPass);
            Log.d("TAG2", "signUpUser: "+resultMail);
            Log.d("TAG2", "signUpUser: "+resultPass);

            try {
                ref.child(resultMail+resultPass).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.child("email").getValue().equals(email)){
                            weHireLoader.DismissDialog();
                            Snackbar.make(binding.getRoot(),"User Already Registered. Please Login With Email & Password Or Try Login With Google",Snackbar.LENGTH_SHORT).show();
                        }else{

                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){



                                                if (binding.searchAsIntern.isChecked()){
                                                    SignUpUser signUpUser = new SignUpUser(email,pass,"false","No",first,last);
                                                    FirebaseDatabase.getInstance().getReference("Users")
                                                            .child(resultMail+resultPass)
                                                            .setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        weHireLoader.DismissDialog();
                                                                        Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();
                                                                        SessionManagement.setLogin(true);
                                                                        SessionManagement.saveUserLoginID(resultMail+resultPass);
                                                                        SessionManagement.saveUserEmail(email);
                                                                        SessionManagement.saveUserPass(pass);
                                                                        SessionManagement.saveUserFirstName(first);
                                                                        SessionManagement.saveUserLastName(last);
                                                                        Intent in = new Intent(SignUpActivity.this, SignupProfileActivity.class);
                                                                        startActivity(in);
                                                                        finish();
                                                                        Animatoo.animateSlideLeft(SignUpActivity.this);
                                                                        Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();

                                                                    }else {
                                                                        weHireLoader.DismissDialog();
                                                                        Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                }else if (binding.searchAsCompany.isChecked()){
                                                    SignUpCompany signUpCompany = new SignUpCompany(email,pass,"false",first,last,"No");
                                                    FirebaseDatabase.getInstance().getReference("Companys")
                                                            .child(resultMail+resultPass)
                                                            .setValue(signUpCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        weHireLoader.DismissDialog();
                                                                        Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();
                                                                        SessionManagement.setCompLogin(true);
                                                                        SessionManagement.saveUserLoginID(resultMail+resultPass);
                                                                        SessionManagement.saveUserEmail(email);
                                                                        SessionManagement.saveUserPass(pass);
                                                                        SessionManagement.saveUserFirstName(first);
                                                                        SessionManagement.saveUserLastName(last);
                                                                        Intent in = new Intent(SignUpActivity.this, SignupProfileCompanyActivity.class);
                                                                        in.putExtra("comesFrom","signup");
                                                                        startActivity(in);
                                                                        finish();
                                                                        Animatoo.animateSlideLeft(SignUpActivity.this);
//                                                                        Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();

                                                                    }else {
                                                                        weHireLoader.DismissDialog();
                                                                        Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }



                                            }else {
                                                weHireLoader.DismissDialog();
                                                Snackbar.make(binding.getRoot(),"Please try again with other email or sign up with google accounts",Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        weHireLoader.DismissDialog();
                    }
                });
            }catch (Exception e){
                Log.d("TAG2", "signUpUser: "+e.getMessage());
            }




        }

    }


    private void setGoogleLogin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            //Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        String idToken = account.getIdToken();
        if (idToken !=  null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = auth.getCurrentUser();

                                setGoogleLoginToFirebase(account);

//                                Intent in = new Intent(SignUpActivity.this,SignupProfileActivity.class);
//                                startActivity(in);
//                                finish();
//                                Animatoo.animateSlideLeft(SignUpActivity.this);

                            } else {
                                Log.w("TAG", "signInWithCredential:failure", task.getException());

                            }
                        }
                    });
        }

    }

    private void setGoogleLoginToFirebase(GoogleSignInAccount account) {

        if (account != null){
            GFamilyName = account.getFamilyName().trim();
            firstGName = account.getGivenName();
            lastGName = account.getFamilyName();
            GEmail = account.getEmail();
            GImageUrl = account.getPhotoUrl();
            Log.d("TAG2", "onComplete: -------"+firstGName);
            Log.d("TAG2", "onComplete: -------"+lastGName);
            Log.d("TAG2", "onComplete: -------"+GFamilyName);
            Log.d("TAG2", "onComplete: -------"+GEmail);
            Log.d("TAG2", "onComplete: -------"+GImageUrl);
        }


            Gref = FirebaseDatabase.getInstance().getReference("Users");

            GrefCompany = FirebaseDatabase.getInstance().getReference("Companys");


            weHireLoader.ShowDialog();
        String refMail = GEmail.substring(0, GEmail.length() - 10);
        String resultMail = refMail.replaceAll("[-+.^:@,]","");
        String refPass = GEmail.substring(0,4).trim();
        String resultPass = refPass.replaceAll("[-+.^:@,]","");
        Log.d("TAG2", "signUpUser: "+refMail);
        Log.d("TAG2", "signUpUser: "+refPass);
        Log.d("TAG2", "signUpUser: "+resultMail);
        Log.d("TAG2", "signUpUser: "+resultPass);

        if (binding.searchAsIntern.isChecked()){
            try {
                Gref.child(resultMail+resultPass).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.child("email").getValue().equals(GEmail)){
                            //Snackbar.make(binding.getRoot(),"User Already Registered. Please Login With Email And Password",Snackbar.LENGTH_SHORT).show();

                            try {
                                uidF = snapshot.getKey();
                                firstNameF = snapshot.child("firstName").getValue().toString();
                                lastNameF = snapshot.child("lastName").getValue().toString();
                                isProfComF = snapshot.child("isProfCom").getValue().toString();
                                passF = snapshot.child("pass").getValue().toString();

                                Log.d("TAG2", "onDataChange: "+firstNameF);
                                Log.d("TAG2", "onDataChange: "+lastNameF);
                                Log.d("TAG2", "onDataChange: "+isProfComF);
                                Log.d("TAG2", "onDataChange: "+passF);
                                Log.d("TAG2", "onDataChange: "+uidF);

                                SessionManagement.saveUserEmail(GEmail);
                                SessionManagement.saveUserFirstName(firstNameF);
                                SessionManagement.saveUserLastName(lastNameF);
                                SessionManagement.saveUserPass(passF);
                                SessionManagement.saveUserLoginID(uidF);

                            }catch (Exception e){
                                Log.d("TAG2", "onDataChange: "+e.getMessage());
                            }


                            if (snapshot.exists() && snapshot.child("isProfCom").getValue().equals("true")){
                                SessionManagement.setLogin(true);
                                weHireLoader.DismissDialog();
                                Intent in = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(in);
                                finish();
                                Animatoo.animateSlideLeft(SignUpActivity.this);
                            }else {
                                weHireLoader.DismissDialog();
                                SessionManagement.setLogin(true);
                                SessionManagement.saveUserLoginID(uidF);
                                SessionManagement.saveUserProfileImage(GImageUrl);
                                Intent in = new Intent(SignUpActivity.this,SignupProfileActivity.class);
                                in.putExtra("uid",uidF);
                                startActivity(in);
                                finish();
                                Animatoo.animateSlideLeft(SignUpActivity.this);
                            }

                        }else{
                            SignUpUser signUpUser = new SignUpUser(GEmail,"nullGoogleLog","false","No",firstGName,lastGName);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(resultMail+resultPass)
                                    .setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                weHireLoader.DismissDialog();

                                                SessionManagement.setLogin(true);
                                                SessionManagement.saveUserLoginID(resultMail+resultPass);
                                                SessionManagement.saveUserEmail(GEmail);
                                                SessionManagement.saveUserPass("nullGoogleLog");
                                                SessionManagement.saveUserFirstName(firstGName);
                                                SessionManagement.saveUserLastName(lastGName);
                                                SessionManagement.saveUserProfileImage(GImageUrl);
                                                Intent in = new Intent(SignUpActivity.this, SignupProfileActivity.class);
                                                in.putExtra("comesFrom","signup");
                                                startActivity(in);
                                                finish();
                                                Animatoo.animateSlideLeft(SignUpActivity.this);
                                                //Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();

                                            }else {
                                                weHireLoader.DismissDialog();
                                                Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        weHireLoader.DismissDialog();
                    }
                });


            }catch (Exception e){
                Log.d("TAG2", "signUpUser: "+e.getMessage());
            }
        }else if (binding.searchAsCompany.isChecked()){
            try {
                GrefCompany.child(resultMail+resultPass).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.child("email").getValue().equals(GEmail)){
                            //Snackbar.make(binding.getRoot(),"User Already Registered. Please Login With Email And Password",Snackbar.LENGTH_SHORT).show();

                            try {
                                uidF = snapshot.getKey();
                                firstNameF = snapshot.child("firstName").getValue().toString();
                                lastNameF = snapshot.child("lastName").getValue().toString();
                                isProfComF = snapshot.child("isProfCom").getValue().toString();
                                passF = snapshot.child("pass").getValue().toString();

                                Log.d("TAG2", "onDataChange: "+firstNameF);
                                Log.d("TAG2", "onDataChange: "+lastNameF);
                                Log.d("TAG2", "onDataChange: "+isProfComF);
                                Log.d("TAG2", "onDataChange: "+passF);
                                Log.d("TAG2", "onDataChange: "+uidF);

                                SessionManagement.saveUserEmail(GEmail);
                                SessionManagement.saveUserFirstName(firstNameF);
                                SessionManagement.saveUserLastName(lastNameF);
                                SessionManagement.saveUserPass(passF);
                                SessionManagement.saveUserLoginID(uidF);

                                if (snapshot.exists() && snapshot.child("isProfCom").getValue().equals("true")){
                                    weHireLoader.DismissDialog();
                                    SessionManagement.setCompLogin(true);
                                    Intent in = new Intent(SignUpActivity.this, HomeActivity.class);
                                    startActivity(in);
                                    finish();
                                    Animatoo.animateSlideLeft(SignUpActivity.this);
                                }else {
                                    weHireLoader.DismissDialog();
                                    SessionManagement.setCompLogin(true);
                                    SessionManagement.saveUserLoginID(uidF);
                                    SessionManagement.saveUserProfileImage(GImageUrl);
                                    Intent in = new Intent(SignUpActivity.this,SignupProfileCompanyActivity.class);
                                    in.putExtra("uid",uidF);
                                    startActivity(in);
                                    finish();
                                    Animatoo.animateSlideLeft(SignUpActivity.this);
                                }

                            }catch (Exception e){
                                Log.d("TAG2", "onDataChange: "+e.getMessage());
                            }




                        }else{
                            SignUpCompany signUpCompany = new SignUpCompany(GEmail,"nullGoogleLog","false",firstGName,lastGName,"No");

                            FirebaseDatabase.getInstance().getReference("Companys")
                                    .child(resultMail+resultPass)
                                    .setValue(signUpCompany).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                weHireLoader.DismissDialog();
                                                SessionManagement.setCompLogin(true);
                                                SessionManagement.saveUserLoginID(resultMail+resultPass);
                                                SessionManagement.saveUserEmail(GEmail);
                                                SessionManagement.saveUserPass("nullGoogleLog");
                                                SessionManagement.saveUserFirstName(firstGName);
                                                SessionManagement.saveUserLastName(lastGName);
                                                SessionManagement.saveUserProfileImage(GImageUrl);
                                                Intent in = new Intent(SignUpActivity.this, SignupProfileCompanyActivity.class);
                                                in.putExtra("comesFrom","signup");
                                                startActivity(in);
                                                finish();
                                                Animatoo.animateSlideLeft(SignUpActivity.this);
                                                //Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();

                                            }else {
                                                weHireLoader.DismissDialog();
                                                Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        weHireLoader.DismissDialog();
                    }
                });


            }catch (Exception e){
                Log.d("TAG2", "signUpUser: "+e.getMessage());
            }
        }





    }


    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(in);
        finish();
        Animatoo.animateSlideRight(SignUpActivity.this);
    }
}