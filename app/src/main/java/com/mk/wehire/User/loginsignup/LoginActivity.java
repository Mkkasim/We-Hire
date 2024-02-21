package com.mk.wehire.User.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;
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
import com.mk.wehire.Company.profile.SignupProfileCompanyActivity;
import com.mk.wehire.MainActivity;
import com.mk.wehire.R;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.databinding.ActivityLoginBinding;
import com.mk.wehire.User.models.SignUpUser;
import com.mk.wehire.User.profile.SignupProfileActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    FirebaseAuth auth;
    DatabaseReference ref,refCompany;

    String emailF,passF,firstF,lastF;
    float v=0;

    String firstGName,firstNameF;
    String lastGName,lastNameF;
    String isProfComF,uidF,passwordF;
    String GEmail;
    String GFamilyName;
    Uri GImageUrl;
    DatabaseReference Gref,GrefCompany;

    WeHireLoader weHireLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        weHireLoader = new WeHireLoader(this);
        auth = FirebaseAuth.getInstance();
        setGoogleLogin();

        binding.signup.setOnClickListener(view -> {
            Intent in = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(in);
            finish();
            Animatoo.animateSlideLeft(LoginActivity.this);
        });


        binding.EmailId.setTranslationX(800);
        binding.Password.setTranslationX(800);
        binding.ForgotPass.setTranslationX(800);
        binding.loginBtn.setTranslationX(800);

        binding.EmailId.setAlpha(v);
        binding.Password.setAlpha(v);
        binding.ForgotPass.setAlpha(v);
        binding.loginBtn.setAlpha(v);

        binding.EmailId.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.Password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.ForgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.loginBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        binding.GoogleLogin.setOnClickListener(view -> {
            if (binding.searchHireGroup.getCheckedRadioButtonId() == -1){
                Snackbar.make(binding.getRoot(),"Please Select Your Role",Snackbar.LENGTH_SHORT).show();

            }else {
                signIn();
            }
        });

        binding.loginBtn.setOnClickListener(view1 -> {

            loginUser();

        });

        binding.ForgotPass.setOnClickListener(view1 -> {
            resetPass();
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



    private void resetPass() {

        //EditText resetMail = new EditText(LoginActivity.this);
        AlertDialog.Builder passResetDialog = new AlertDialog.Builder(LoginActivity.this);
        View view = getLayoutInflater().inflate(R.layout.forgot_pass,null);
        TextInputEditText resetMail = view.findViewById(R.id.EmailIdEdt);
        TextView yesBtn = view.findViewById(R.id.yesBtn);
        TextView noBtn = view.findViewById(R.id.noBtn);

        passResetDialog.setView(view);

        AlertDialog alertDialog = passResetDialog.create();
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;

        yesBtn.setOnClickListener(view1 -> {
            String mail = resetMail.getText().toString();

            if (mail.isEmpty()){
                resetMail.setError("Field can not be empty");
                resetMail.requestFocus();
            }else if (!isEmailValid(mail)){
                resetMail.setError("Not Valid Email");
                resetMail.requestFocus();
            }else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                        .addOnSuccessListener(unused ->
                                Snackbar.make(binding.getRoot(),"Reset Link Sent To Your Email.",Snackbar.LENGTH_SHORT).show())

                        .addOnFailureListener(e ->
                                Snackbar.make(binding.getRoot(),"Error ! Reset Link Is Not Sent ",Snackbar.LENGTH_SHORT).show()
                        );
            }
        });
        noBtn.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        alertDialog.show();

    }

    private void loginUser() {

        String email = binding.EmailIdEdt.getText().toString();
        String pass = binding.PasswordEdt.getText().toString();

        if (email.isEmpty()){
            binding.EmailIdEdt.setError("Field can not be empty");
            binding.EmailIdEdt.requestFocus();
        }else if (!isEmailValid(email) ){
            binding.EmailIdEdt.setError("Email is not valid");
            binding.EmailIdEdt.requestFocus();
        }else if (pass.isEmpty()){
            binding.PasswordEdt.setError("Field can not be empty");
            binding.PasswordEdt.requestFocus();
        }else if (binding.searchHireGroup.getCheckedRadioButtonId() == -1){
            Snackbar.make(binding.getRoot(),"Please Select Your Role",Snackbar.LENGTH_SHORT);

        }else {


                ref = FirebaseDatabase.getInstance().getReference("Users");
            
                refCompany = FirebaseDatabase.getInstance().getReference("Companys");
            

//            ref = FirebaseDatabase.getInstance().getReference("Users");
//            refCompany = FirebaseDatabase.getInstance().getReference("Companys");

            weHireLoader.ShowDialog();
            String refMail = email.substring(0, email.length() - 10);
            String resultMail = refMail.replaceAll("[-+.^:@,]","");
            String refPass = pass.substring(0,4);
            String resultPass = refPass.replaceAll("[-+.^:@,]","");
            Log.d("TAG2", "signUpUser: "+refMail);
            Log.d("TAG2", "signUpUser: "+refPass);

            try {

                if (binding.searchAsIntern.isChecked()){

                    ref.child(resultMail+resultPass).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.exists() && snapshot.child("email").getValue().equals(email)){

                                emailF = snapshot.child("email").getValue().toString();
                                passF = snapshot.child("pass").getValue().toString();
                                firstF = snapshot.child("firstName").getValue().toString();
                                lastF = snapshot.child("lastName").getValue().toString();
                                Log.d("TAG2", "onDataChange: "+passF);
                                // Log.d("TAG2", "onDataChange: "+auth.getUid());

                                if (passF.equals(pass)){
                                    if (snapshot.exists() && snapshot.child("isProfCom").getValue().equals("true")){
                                        weHireLoader.DismissDialog();
                                        SessionManagement.setLogin(true);
                                        SessionManagement.saveUserLoginID(resultMail+resultPass);
                                        SessionManagement.saveUserEmail(emailF);
                                        SessionManagement.saveUserPass(passF);
                                        SessionManagement.saveUserFirstName(firstF);
                                        SessionManagement.saveUserLastName(lastF);
                                        Intent in = new Intent(LoginActivity.this,MainActivity.class);
                                        in.putExtra("comesFrom","login");
                                        startActivity(in);
                                        finish();
                                        Animatoo.animateSlideLeft(LoginActivity.this);
                                    }else{
                                        weHireLoader.DismissDialog();
                                        SessionManagement.setLogin(true);
                                        SessionManagement.saveUserLoginID(resultMail+resultPass);
                                        SessionManagement.saveUserEmail(email);
                                        SessionManagement.saveUserPass(pass);
                                        SessionManagement.saveUserFirstName(firstF);
                                        SessionManagement.saveUserLastName(lastF);
                                        Intent in = new Intent(LoginActivity.this, SignupProfileActivity.class);
                                        in.putExtra("comesFrom","login");
                                        startActivity(in);
                                        finish();
                                        Animatoo.animateSlideLeft(LoginActivity.this);

                                    }


                                }else {
                                    weHireLoader.DismissDialog();
                                    Snackbar.make(binding.getRoot(),"Password is incorrect",Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                weHireLoader.DismissDialog();
                                Snackbar.make(binding.getRoot(),"Something went wrong",Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            weHireLoader.DismissDialog();
                        }
                    });

                }else if (binding.searchAsCompany.isChecked()){

                    refCompany.child(resultMail+resultPass).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.child("email").getValue().equals(email)) {

                                emailF = snapshot.child("email").getValue().toString();
                                passF = snapshot.child("pass").getValue().toString();
                                firstF = snapshot.child("firstName").getValue().toString();
                                lastF = snapshot.child("lastName").getValue().toString();
                                Log.d("TAG2", "onDataChange: " + passF);
                                // Log.d("TAG2", "onDataChange: "+auth.getUid());

                                if (passF.equals(pass)) {
                                    if (snapshot.exists() && snapshot.child("isProfCom").getValue().equals("true")) {
                                        weHireLoader.DismissDialog();
                                        SessionManagement.setCompLogin(true);
                                        SessionManagement.saveUserLoginID(resultMail + resultPass);
                                        SessionManagement.saveUserEmail(emailF);
                                        SessionManagement.saveUserPass(passF);
                                        SessionManagement.saveUserFirstName(firstF);
                                        SessionManagement.saveUserLastName(lastF);
                                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                                        in.putExtra("comesFrom","login");
                                        startActivity(in);
                                        finish();
                                        Animatoo.animateSlideLeft(LoginActivity.this);
                                    } else {
                                        weHireLoader.DismissDialog();
                                        SessionManagement.setCompLogin(true);
                                        SessionManagement.saveUserLoginID(resultMail + resultPass);
                                        SessionManagement.saveUserEmail(email);
                                        SessionManagement.saveUserPass(pass);
                                        SessionManagement.saveUserFirstName(firstF);
                                        SessionManagement.saveUserLastName(lastF);
                                        Intent in = new Intent(LoginActivity.this, SignupProfileCompanyActivity.class);
                                        in.putExtra("comesFrom", "login");
                                        startActivity(in);
                                        finish();
                                        Animatoo.animateSlideLeft(LoginActivity.this);

                                    }


                                } else {
                                    weHireLoader.DismissDialog();
                                    Snackbar.make(binding.getRoot(), "Password is incorrect", Snackbar.LENGTH_SHORT).show();
                                }
                            }else {
                                weHireLoader.DismissDialog();
                                Snackbar.make(binding.getRoot(), "No Records Found", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            weHireLoader.DismissDialog();
                        }
                    });

                }




            }catch (Exception e){

                Log.d("TAG2", "loginUser: "+e.getMessage());

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
            //Snackbar.make(binding.getRoot(),"Something went wrong",Snackbar.LENGTH_SHORT).show();
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
                                passwordF = snapshot.child("pass").getValue().toString();

                                Log.d("TAG2", "onDataChange: "+firstNameF);
                                Log.d("TAG2", "onDataChange: "+lastNameF);
                                Log.d("TAG2", "onDataChange: "+isProfComF);
                                Log.d("TAG2", "onDataChange: "+passwordF);
                                Log.d("TAG2", "onDataChange: "+uidF);

                                SessionManagement.saveUserEmail(GEmail);
                                SessionManagement.saveUserFirstName(firstNameF);
                                SessionManagement.saveUserLastName(lastNameF);
                                SessionManagement.saveUserPass(passwordF);
                                SessionManagement.saveUserLoginID(uidF);

                            }catch (Exception e){
                                Log.d("TAG2", "onDataChange: "+e.getMessage());
                            }


                            if (snapshot.exists() && snapshot.child("isProfCom").getValue().equals("true")){
                                weHireLoader.DismissDialog();
                                SessionManagement.setLogin(true);
                                Intent in = new Intent(LoginActivity.this,MainActivity.class);
                                in.putExtra("comesFrom","login");
                                startActivity(in);
                                finish();
                                Animatoo.animateSlideLeft(LoginActivity.this);
                            }else {
                                weHireLoader.DismissDialog();
                                SessionManagement.setLogin(true);
                                SessionManagement.saveUserLoginID(uidF);
                                SessionManagement.saveUserProfileImage(GImageUrl);
                                Intent in = new Intent(LoginActivity.this,SignupProfileActivity.class);
                                in.putExtra("uid",uidF);
                                in.putExtra("comesFrom","login");
                                startActivity(in);
                                finish();
                                Animatoo.animateSlideLeft(LoginActivity.this);
                            }




                        } else{

//                        SignUpUser signUpUser = new SignUpUser(GEmail,"nullGoogleLog","false",firstGName,lastGName);
//
//                        FirebaseDatabase.getInstance().getReference("Users")
//                                .child(resultMail+resultPass)
//                                .setValue(signUpUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();
//                                            SessionManagement.setLogin(true);
//                                            SessionManagement.saveUserLoginID(resultMail+resultPass);
//                                            SessionManagement.saveUserEmail(GEmail);
//                                            SessionManagement.saveUserPass("nullGoogleLog");
//                                            SessionManagement.saveUserFirstName(firstGName);
//                                            SessionManagement.saveUserLastName(lastGName);
//                                            SessionManagement.saveUserProfileImage(GImageUrl);
//                                            Intent in = new Intent(LoginActivity.this, SignupProfileActivity.class);
//                                            startActivity(in);
//                                            finish();
//                                            Animatoo.animateSlideLeft(LoginActivity.this);
//                                            Snackbar.make(binding.getRoot(),"SignUp Successful",Snackbar.LENGTH_SHORT).show();
//
//                                        }else {
//                                            Snackbar.make(binding.getRoot(),"Something went wrong!",Snackbar.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });

                            weHireLoader.DismissDialog();
                            Snackbar.make(binding.getRoot(), "No Records Found! Please Signup", Snackbar.LENGTH_SHORT).show();


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

            String refMail1 = GEmail.substring(0, GEmail.length() - 10);
            String resultMail1 = refMail1.replaceAll("[-+.^:@,]","");
            String refPass1 = GEmail.substring(0,4).trim();
            String resultPass1 = refPass1.replaceAll("[-+.^:@,]","");
            Log.d("TAG2", "signUpUser: "+refMail1);
            Log.d("TAG2", "signUpUser: "+refPass1);
            Log.d("TAG2", "signUpUser: "+resultMail1);
            Log.d("TAG2", "signUpUser: "+resultPass1);

            try {
                GrefCompany = FirebaseDatabase.getInstance().getReference("Companys");
                Log.d("TAG2", "onDataChange: ---- " +resultMail1+resultPass1);
                GrefCompany.child(resultMail1+resultPass1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        Log.d("TAG2", "onDataChange: "+ GEmail);
                        try {
                            if (snap.exists() && snap.child("email").getValue().equals(GEmail)){

                                try {
                                    uidF = snap.getKey();
                                    if (snap.child("companyName").exists()){
                                        String CompanyNameF = snap.child("companyName").getValue().toString();
                                        Log.d("TAG2", "onDataChange: " + CompanyNameF);
                                        SessionManagement.saveCompanyName(CompanyNameF);
                                    }

                                    firstNameF = snap.child("firstName").getValue().toString();
                                    lastNameF = snap.child("lastName").getValue().toString();
                                    isProfComF = snap.child("isProfCom").getValue().toString();
                                    passwordF = snap.child("pass").getValue().toString();


                                    Log.d("TAG2", "onDataChange: " + firstNameF);
                                    Log.d("TAG2", "onDataChange: " + lastNameF);
                                    Log.d("TAG2", "onDataChange: " + isProfComF);
                                    Log.d("TAG2", "onDataChange: " + passwordF);
                                    Log.d("TAG2", "onDataChange: " + uidF);

                                    SessionManagement.saveUserEmail(GEmail);

                                    SessionManagement.saveUserFirstName(firstNameF);
                                    SessionManagement.saveUserLastName(lastNameF);
                                    SessionManagement.saveUserPass(passwordF);
                                    SessionManagement.saveUserLoginID(uidF);
                                }catch (Exception e){
                                    Log.d("TAG2", "onDataChange: --"+ e.getMessage());
                                }


                                if (snap.exists() && snap.child("isProfCom").getValue().equals("true")){
                                    weHireLoader.DismissDialog();
                                    SessionManagement.setCompLogin(true);
                                    Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                                    in.putExtra("comesFrom","login");
                                    startActivity(in);
                                    finish();
                                    Animatoo.animateSlideLeft(LoginActivity.this);
                                }else {
                                    weHireLoader.DismissDialog();
                                    SessionManagement.setCompLogin(true);
                                    SessionManagement.saveUserLoginID(uidF);
                                    SessionManagement.saveUserProfileImage(GImageUrl);
                                    Intent in = new Intent(LoginActivity.this,SignupProfileCompanyActivity.class);
                                    in.putExtra("uid",uidF);
                                    in.putExtra("comesFrom","login");
                                    startActivity(in);
                                    finish();
                                    Animatoo.animateSlideLeft(LoginActivity.this);
                                }

                            }else {
                                weHireLoader.DismissDialog();
                                Snackbar.make(binding.getRoot(), "No Records Found! Please Signup", Snackbar.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Log.d("TAG2", "onDataChange: "+e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }catch (Exception e){
                Log.d("TAG2", "onDataChange: ---- " +e.getMessage());
            }


        }



    }


    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}