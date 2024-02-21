package com.mk.wehire.User.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.mk.wehire.R;
import com.mk.wehire.databinding.ActivityFilterBinding;
import com.mk.wehire.User.fragments.PreferenceFragment;

public class FilterActivity extends AppCompatActivity {

    ActivityFilterBinding binding;

    String workHomeCheck,prefCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.chatBtn.setOnClickListener(view -> {

        });

        binding.applyBtn.setOnClickListener(view -> {

            if (binding.prefCheck.isChecked()){
                prefCheck = "prefCheck";
            }else if (binding.workHomeCheck.isChecked()){
                workHomeCheck = "work from home";
            }


        });

        binding.textAs.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","filter");
                PreferenceFragment prefFrag = new PreferenceFragment();
                prefFrag.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_cont,prefFrag);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                transaction.addToBackStack(null);
                transaction.commit();
        });

        binding.resetBtn.setOnClickListener(view -> {

            binding.prefCheck.setChecked(false);
            binding.workHomeCheck.setChecked(false);
            binding.partCheck.setChecked(false);
            binding.filledExposed.clearListSelection();
            onBackPressed();
        });

        binding.prefCheck.setOnClickListener(view -> {
            if (binding.prefCheck.isChecked()){
                binding.addCateg.setTextColor(getResources().getColor(R.color.grey));
                binding.addCateg.setEnabled(false);
                binding.addCategImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                binding.addCategImg.setEnabled(false);

                binding.addCity.setTextColor(getResources().getColor(R.color.grey));
                binding.addCity.setEnabled(false);
                binding.addCityImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                binding.addCityImg.setEnabled(false);

                binding.intern.setTextColor(getResources().getColor(R.color.grey));
                binding.workHomeCheck.setEnabled(false);
                binding.workHomeCheck.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                binding.parttime.setTextColor(getResources().getColor(R.color.grey));
                binding.partCheck.setEnabled(false);
                binding.partCheck.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            }else {
                binding.addCateg.setTextColor(getResources().getColor(R.color.green));
                binding.addCateg.setEnabled(true);
                binding.addCategImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.addCategImg.setEnabled(true);

                binding.addCity.setTextColor(getResources().getColor(R.color.green));
                binding.addCity.setEnabled(true);
                binding.addCityImg.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                binding.addCityImg.setEnabled(true);

                binding.intern.setTextColor(getResources().getColor(R.color.darkGreen));
                binding.workHomeCheck.setEnabled(true);
                binding.workHomeCheck.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                binding.parttime.setTextColor(getResources().getColor(R.color.darkGreen));
                binding.partCheck.setEnabled(true);
                binding.partCheck.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

            }

        });
        binding.addCateg.setOnClickListener(view -> {

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateZoom(this);
    }
}