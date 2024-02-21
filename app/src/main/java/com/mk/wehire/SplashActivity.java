package com.mk.wehire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mk.wehire.Company.activity.HomeActivity;
import com.mk.wehire.Company.profile.SignupProfileCompanyActivity;
import com.mk.wehire.User.NetworkUtility.InternetReceiver;
import com.mk.wehire.databinding.ActivitySplashBinding;
import com.mk.wehire.User.loginsignup.LoginActivity;
import com.mk.wehire.User.profile.SignupProfileActivity;
import com.mk.wehire.User.sessionmanager.SessionManagement;

public class SplashActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver = null;

    ActivitySplashBinding binding;

    DatabaseReference refDetails,refCompDetails;
    String isProfCom,isCompProfCom,notiFor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        broadcastReceiver = new InternetReceiver();
        InternetStatus();
        getNotiIntent();

        SessionManagement.init(this);

        getFirebaseUserToken();


        if (isConnected()){

            getDetailsFromFirebase();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SessionManagement.isLogged()){
                        if (isProfCom.equals("true")){
                            Intent in = new Intent(SplashActivity.this,MainActivity.class);
                            in.putExtra("notiFor",notiFor);
                            startActivity(in);
                            finish();
                            Animatoo.animateFade(SplashActivity.this);
                        }else {

                            Intent in = new Intent(SplashActivity.this, SignupProfileActivity.class);
                            in.putExtra("comesFrom","splash");
                            startActivity(in);
                            finish();
                            Animatoo.animateFade(SplashActivity.this);
                        }

                    }else if (SessionManagement.isCompLogged()){
                        if (isCompProfCom.equals("true")){
                            Intent in = new Intent(SplashActivity.this, HomeActivity.class);
                            in.putExtra("notiFor",notiFor);
                            startActivity(in);
                            finish();
                            Animatoo.animateFade(SplashActivity.this);
                        }else {

                            Intent in = new Intent(SplashActivity.this, SignupProfileCompanyActivity.class);
                            in.putExtra("comesFrom","splash");
                            startActivity(in);
                            finish();
                            Animatoo.animateFade(SplashActivity.this);
                        }


                    } else{
                        Intent in = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                        Animatoo.animateFade(SplashActivity.this);
                    }


                }
            },5000);
        }

    }

    public void InternetStatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void getNotiIntent() {
        notiFor= getIntent().getStringExtra("notiFor");
        Log.d("TAG2", "getNotiIntent: notiFor ---splash"+ notiFor);
    }


    private void getDetailsFromFirebase(){
        refDetails= FirebaseDatabase.getInstance().getReference("Users");
        refCompDetails = FirebaseDatabase.getInstance().getReference("Companys");

        String uid = SessionManagement.getUserLoginID();
        Log.d("TAG2", "getDetailsFromFirebase: ===="+uid);
        try {
            refDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (SessionManagement.isLogged()){
                        if (snapshot.exists()){
                            isProfCom = snapshot.child("isProfCom").getValue().toString();
                            Log.d("TAG2", "onDataChange: ======="+isProfCom);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            refCompDetails.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (SessionManagement.isCompLogged()){
                        if (snapshot.exists()){
                            isCompProfCom = snapshot.child("isProfCom").getValue().toString();
                            Log.d("TAG2", "onDataChange: ======="+ isCompProfCom);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){
            Log.d("TAG2", "getDetailsFromFirebase: "+e.getMessage());
        }

    }

    private void getFirebaseUserToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.e("TAG2", "onComplete: " + token);
                        SessionManagement.saveUserToken(token);


                        // updateUserToken();
                    }
                });
    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo info = connectivityManager.getActiveNetworkInfo();



        return info !=null && info.isConnected();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}