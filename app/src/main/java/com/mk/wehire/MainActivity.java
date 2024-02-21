package com.mk.wehire;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.mk.wehire.Company.fragments.ApplicantsFragment;
import com.mk.wehire.Company.fragments.MoreFragment;
import com.mk.wehire.User.NetworkUtility.InternetReceiver;
import com.mk.wehire.User.fragments.NotificationFragment;
import com.mk.wehire.User.fragments.ResumeFragment;
import com.mk.wehire.databinding.ActivityMainBinding;
import com.mk.wehire.User.fragments.ApplicationsFragment;
import com.mk.wehire.User.fragments.JobsFragment;
import com.mk.wehire.User.fragments.PreferenceFragment;
import com.mk.wehire.User.fragments.TrainingFragment;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.sessionmanager.SessionManagement;

import net.skoumal.fragmentback.BackFragmentHelper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    DatabaseReference DetailsRef;
    String firstNameF,lastNameF,locatF,genderF,languageF,mobileF,degreeName;

    InternetReceiver netlistener = new InternetReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SessionManagement.init(this);

        FirebaseMessaging.getInstance().subscribeToTopic("users").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG2", "onSuccess: ----Subscribed To Users");
            }
        });


        handleIntent();
        //getNotiIntent();
        replaceFragment(new IntershipsFragment());
        setUpDrawer();
        setUpActionIcon();
        getDetailsFromFirebaseSessions();

    }

    private void setUpActionIcon() {



        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                switch (id){
                    case R.id.search:

                        break;
                    case R.id.noti:
                        replaceFragment(new NotificationFragment());
                        break;

                    default:

                        return true;
                }

                return true;
            }
        });
    }

    private void setUpDrawer() {

        binding.toolbar.setNavigationOnClickListener(view -> {
            binding.drawer.openDrawer(GravityCompat.START);
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);
        TextView profilename = headerview.findViewById(R.id.fullName);
        TextView email = headerview.findViewById(R.id.userEmail);
        ImageView preferences = headerview.findViewById(R.id.preferenceImg);
        ImageView resume = headerview.findViewById(R.id.resumeImg);
        ImageView applications = headerview.findViewById(R.id.applicationImg);
        ImageView close = headerview.findViewById(R.id.close);
        ImageView verified = headerview.findViewById(R.id.verifiedImg);
        if (SessionManagement.isLogged()){
            profilename.setText(SessionManagement.getFirstName()+" "+SessionManagement.getLastName());
            email.setText(SessionManagement.getEmail());
        }


        close.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
        });

        preferences.setOnClickListener(view -> {

            binding.toolbar.setTitle("Preferences");
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","drawer");
            PreferenceFragment prefFrag = new PreferenceFragment();
            prefFrag.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,prefFrag);
            transaction.commit();
            binding.drawer.closeDrawer(GravityCompat.START);

        });
        resume.setOnClickListener(view -> {
            binding.toolbar.setTitle("Resume");
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","drawer");
            ResumeFragment resumeFrag = new ResumeFragment();
            resumeFrag.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,resumeFrag);
            transaction.commit();
            binding.drawer.closeDrawer(GravityCompat.START);
        });
        applications.setOnClickListener(view -> {
            binding.toolbar.setTitle("Applications");
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","drawer");
            ApplicationsFragment appFrg = new ApplicationsFragment();
            appFrg.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,appFrg);
            transaction.commit();
            binding.drawer.closeDrawer(GravityCompat.START);
        });



        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                binding.navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkGreen)));


                //item.setChecked(true);
                binding.drawer.closeDrawer(GravityCompat.START);
                switch (id){

                    case R.id.nav_internships:
                        replaceFragment(new IntershipsFragment());
                        binding.toolbar.setTitle("Internships");
                        break;
                    case R.id.nav_trainings:
                        replaceFragment(new TrainingFragment());
                        binding.toolbar.setTitle("Trainings");
                        break;
                    case R.id.nav_jobs:
                        replaceFragment(new JobsFragment());
                        binding.toolbar.setTitle("Jobs");
                        break;
                    case R.id.nav_helpcenter:
                        Snackbar.make(binding.getRoot(),"Help Clicked",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_report:
                        Snackbar.make(binding.getRoot(),"Report Clicked",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_more:
                        replaceFragment(new MoreFragment());
                        binding.toolbar.setTitle("More");
                        break;
                    default:

                        return true;

                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_cont,fragment);
        fragmentTransaction.commit();
    }

    private void getNotiIntent() {

        String notiFor = getIntent().getStringExtra("notiFor");
        if (notiFor == null ){
            replaceFragment(new IntershipsFragment());
        }else if (notiFor.equals("status")){
            replaceFragment(new ApplicationsFragment());

        }
    }

    private void getDetailsFromFirebaseSessions(){

        DetailsRef= FirebaseDatabase.getInstance().getReference("Users");
        String uid = SessionManagement.getUserLoginID();
        Log.d("TAG2", "getDetailsFromFirebase: ===="+uid);
        DetailsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        try {
                            degreeName = snapshot.child("degreeName").getValue().toString().trim();
                        }catch (Exception e){
                            Log.d("TAG2", "onDataChange: -----"+degreeName);
                        }
                        SessionManagement.saveUserFirstName(firstNameF);
                        SessionManagement.saveUserLastName(lastNameF);
                        SessionManagement.saveGender(genderF);
                        SessionManagement.saveLanguage(languageF);
                        SessionManagement.saveUserLocation(locatF);
                        SessionManagement.saveUserNum(mobileF);
                        SessionManagement.saveDegreeName(degreeName);
                        //Log.d("TAG2", "onDataChange: ======="+isProfCom);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handleIntent(){
        String comesFrom = getIntent().getStringExtra("comesFrom");
        if (comesFrom != null && comesFrom.equals("login")){
            Snackbar.make(binding.getRoot(),"Successfuly Loggedin.",Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        if(!BackFragmentHelper.fireOnBackPressedEvent(this)) {

//            NavigationView navigationView = findViewById(R.id.navigation_view);
//
//            int seletedItemId = navigationView.getId();
//            if (R.id.nav_internships != seletedItemId) {
//                setHomeItem(MainActivity.this);
//            } else {
//                super.onBackPressed();
//            }
            super.onBackPressed();
        }

    }

//    public static void setHomeItem(Activity activity) {
//        NavigationView navigationView = activity.findViewById(R.id.navigation_view);
//        navigationView.setCheckedItem(R.id.nav_internships);
//    }

//    @Override
//    public void onStart() {
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(netlistener, filter);
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        unregisterReceiver(netlistener);
//        super.onStop();
//    }

}