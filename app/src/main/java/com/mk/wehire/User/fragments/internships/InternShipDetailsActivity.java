package com.mk.wehire.User.fragments.internships;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mk.wehire.User.adapters.RecycPerksAdapter;
import com.mk.wehire.User.adapters.RecycSkillsAdapter;
import com.mk.wehire.databinding.ActivityInternShipDetailsBinding;
import com.mk.wehire.User.models.IntershipModel;

import java.util.ArrayList;

public class InternShipDetailsActivity extends AppCompatActivity {

    ActivityInternShipDetailsBinding binding;

    String courseName,workLocation,companyName,salary,totalMonths,companyImgUrl,partFullTime,
            key,jobOrIntern, compLongDes,compWeb,internRole,openingNum,perks,skillsReq;

    DatabaseReference perkRef,skillRef;
    ArrayList<IntershipModel> list = new ArrayList<>();
    ArrayList<IntershipModel> listSkill = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInternShipDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleIntent();
        Log.d("TAG2", "onCreateView: ======key   "+key);
        perkRef = FirebaseDatabase.getInstance().getReference("Internships").child(key).child("perks");
        skillRef = FirebaseDatabase.getInstance().getReference("Internships").child(key).child("skillsReq");

        getPerks();
        getSkills();

        binding.backBtn.setOnClickListener(view1 -> onBackPressed());
        binding.chatBtn.setOnClickListener(view12 -> {

        });
        binding.ApplyBtn.setOnClickListener(view -> {

        });

    }

    private void getSkills() {
        binding.recycSkills.setHasFixedSize(true);
        GridLayoutManager linearLayout = new GridLayoutManager(this,4);
        binding.recycSkills.setLayoutManager(linearLayout);
        RecycSkillsAdapter adapter = new RecycSkillsAdapter(this,listSkill);
        binding.recycSkills.setAdapter(adapter);

        skillRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSkill.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot data : snapshot.getChildren()){
                            IntershipModel model = data.getValue(IntershipModel.class);
                            listSkill.add(model);

                            Log.d("TAG2", "onDataChange:list.....--- "+listSkill);
                            Log.d("TAG2", "onDataChange:model.....--- "+model.getSkill());
                        }
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: --------perk "+ e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPerks() {

        binding.recycPerks.setHasFixedSize(true);
        GridLayoutManager linearLayout = new GridLayoutManager(this,1);
        binding.recycPerks.setLayoutManager(linearLayout);
        RecycPerksAdapter adapter = new RecycPerksAdapter(this,list);
        binding.recycPerks.setAdapter(adapter);



        perkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()){
                    try {
                        for (DataSnapshot data : snapshot.getChildren()){
                            IntershipModel model = data.getValue(IntershipModel.class);
                            list.add(model);

                            Log.d("TAG2", "onDataChange:list.....--- "+list);
                            Log.d("TAG2", "onDataChange:model.....--- "+model.getPerk());
                        }
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        Log.d("TAG2", "onDataChange: --------perk "+ e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handleIntent(){
        courseName =getIntent().getStringExtra("courseName");
        companyName =getIntent().getStringExtra("companyName");
        workLocation =getIntent().getStringExtra("workLocation");
        salary =getIntent().getStringExtra("salary");
        totalMonths =getIntent().getStringExtra("totalMonths");
        companyImgUrl =getIntent().getStringExtra("companyImgUrl");
        partFullTime =getIntent().getStringExtra("partFullTime");
        key =getIntent().getStringExtra("key");
        jobOrIntern =getIntent().getStringExtra("jobOrIntern");
        compLongDes =getIntent().getStringExtra("compLongDes");
        compWeb =getIntent().getStringExtra("compWeb");
        internRole =getIntent().getStringExtra("internRole");
        openingNum =getIntent().getStringExtra("openingNum");
//        skillsReq =getArguments().getString("skillsReq");

        binding.CourseName.setText(courseName);
        binding.CompanyName.setText(companyName);
        binding.WorkLocation.setText(workLocation);
        binding.salary.setText(salary);
        binding.periodMonths.setText(totalMonths);
        binding.timeFullPart.setText(partFullTime);
        binding.JobOrIntern.setText(jobOrIntern);

        binding.AboutCompDes.setText(compLongDes);
        binding.AboutCompDes.getText().toString().replace("\\n","\n");
        binding.CompWebsite.setText(compWeb);
        binding.internRoleDes.setText(internRole);
        binding.openNumber.setText(openingNum);

        Glide.with(this).load(companyImgUrl).into(binding.CompanyImg);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(this);
    }
}