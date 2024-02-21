package com.mk.wehire.Company.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mk.wehire.Company.fragments.ApplicantsFragment;
import com.mk.wehire.Company.fragments.MoreFragment;
import com.mk.wehire.Company.fragments.MyPostsFragment;
import com.mk.wehire.Company.fragments.PostJobsInternshipFragment;
import com.mk.wehire.Company.fragments.PostTrainingsFragment;
import com.mk.wehire.MainActivity;
import com.mk.wehire.R;
import com.mk.wehire.User.NetworkUtility.InternetReceiver;
import com.mk.wehire.User.fragments.ApplicationsFragment;
import com.mk.wehire.User.fragments.JobsFragment;
import com.mk.wehire.User.fragments.NotificationFragment;
import com.mk.wehire.User.fragments.PreferenceFragment;
import com.mk.wehire.User.fragments.TrainingFragment;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.models.WeHireLoader;
import com.mk.wehire.User.sessionmanager.SessionManagement;
import com.mk.wehire.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    WeHireLoader weHireLoader;

    DatabaseReference Gref;

    InternetReceiver netlistener = new InternetReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SessionManagement.init(this);

        weHireLoader = new WeHireLoader(this);

        FirebaseMessaging.getInstance().subscribeToTopic("companys").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG2", "onSuccess: ----Subscribed To Companys");
            }
        });

        handleIntent();
//        getNotiIntent();
        replaceFragment(new MyPostsFragment());
        setUpDrawer();
        setUpActionIcon();



    }

    private void setUpActionIcon() {



        binding.toolbarHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        binding.toolbarHome.setNavigationOnClickListener(view -> {
            binding.drawer.openDrawer(GravityCompat.START);
        });

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);
        TextView companyName = headerview.findViewById(R.id.compFullName);
        TextView compEmail = headerview.findViewById(R.id.companyEmail);
        ImageView preferences = headerview.findViewById(R.id.preferenceImg);
        ImageView trainingCamp = headerview.findViewById(R.id.trainingCampImg);
        ImageView myPosts = headerview.findViewById(R.id.myPostsImg);
        ImageView close = headerview.findViewById(R.id.close);
        ImageView verified = headerview.findViewById(R.id.verifiedImg);

        companyName.setText(SessionManagement.getCompanyName());
        Log.d("TAG2", "setUpDrawer: -----"+SessionManagement.getCompanyName());
        compEmail.setText(SessionManagement.getEmail());



        close.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
        });

        preferences.setOnClickListener(view -> {

//            binding.toolbar.setTitle("Preferences");
//            Bundle bundle = new Bundle();
//            bundle.putString("comesFrom","drawer");
//            PreferenceFragment prefFrag = new PreferenceFragment();
//            prefFrag.setArguments(bundle);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_cont,prefFrag);
//            transaction.commit();
            binding.drawer.closeDrawer(GravityCompat.START);

        });
        trainingCamp.setOnClickListener(view -> {
//            binding.toolbarHome.setTitle("Training Camp");
//            Bundle bundle = new Bundle();
//            bundle.putString("comesFrom","drawer");
//            PreferenceFragment prefFrag = new PreferenceFragment();
//            prefFrag.setArguments(bundle);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_cont,prefFrag);
//            transaction.commit();
            binding.drawer.closeDrawer(GravityCompat.START);
        });
        myPosts.setOnClickListener(view -> {
            binding.toolbarHome.setTitle("My Posts");
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","drawer");
            MyPostsFragment myPostFrg = new MyPostsFragment();
            myPostFrg.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,myPostFrg);
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

                    case R.id.nav_post_ji:
                        replaceFragment(new PostJobsInternshipFragment());
                        binding.toolbarHome.setTitle("Post Jobs & Internships");
                        break;
                    case R.id.nav_post_trainings:
                        replaceFragment(new PostTrainingsFragment());
                        binding.toolbarHome.setTitle("Post Trainings");
                        break;
                    case R.id.nav_jobs:
//                        replaceFragment(new JobsFragment());
//                        binding.toolbar.setTitle("Jobs");
                        break;
                    case R.id.nav_helpcenter:
                        Snackbar.make(binding.getRoot(),"Help Clicked",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_report:
                        Snackbar.make(binding.getRoot(),"Report Clicked",Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_more:
                        replaceFragment(new MoreFragment());
                        binding.toolbarHome.setTitle("More");
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
        Log.d("TAG2", "getNotiIntent: ----notiFor "+notiFor);
        if (notiFor == null) {
            replaceFragment(new MyPostsFragment());
        }else if (notiFor.equals("application")) {
            replaceFragment(new ApplicantsFragment());
        }
    }

    private void handleIntent(){
        String comesFrom = getIntent().getStringExtra("comesFrom");
        if (comesFrom != null && comesFrom.equals("login")){
            Snackbar.make(binding.getRoot(),"Successfuly Loggedin.",Snackbar.LENGTH_SHORT).show();
        }


    }
}