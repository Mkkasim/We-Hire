package com.mk.wehire.User.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mk.wehire.R;
import com.mk.wehire.databinding.FragmentFilterBinding;
import com.mk.wehire.User.fragments.internships.IntershipsFragment;
import com.mk.wehire.User.sessionmanager.SessionManagement;

import net.skoumal.fragmentback.BackFragment;


public class FilterFragment extends Fragment implements BackFragment {

    FragmentFilterBinding binding;

    String stipend,duration;

    public FilterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater,container,false);

        SessionManagement.init(getContext());
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Filter");
        toolbar.setNavigationIcon(R.drawable.back);
        MenuItem item = toolbar.getMenu().getItem(0).setVisible(false);
        toolbar.setNavigationOnClickListener(view1 -> {

            onBackPressed();

        });

        String[] stipendType = new String[]{"2,000","4,000","6,000","8,000","10,000"};
        String[] durationType = new String[]{"1","2","3","4","5","6","12","24","36"};
        ArrayAdapter<String> stipend = new ArrayAdapter<>(getContext(), R.layout.gender_drop_down_item,stipendType);
        ArrayAdapter<String> duration = new ArrayAdapter<>(getContext(), R.layout.gender_drop_down_item,durationType);
        binding.stipendSelect.setAdapter(stipend);
        binding.duration.setAdapter(duration);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        stipend = String.valueOf(binding.stipendSelect.getSelectionStart());
//        duration = String.valueOf(binding.duration.getSelectionStart());

        binding.stipendSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stipend = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.duration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                duration = String.valueOf(i);
            }
        });

        onPrefClick();
        checkFilters();
        checkSession();



    }

    private void onPrefClick() {
        if (binding.prefCheck.isChecked() || SessionManagement.isPrefChecked()){
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

    }

    private void checkSession() {

        if (SessionManagement.isPrefChecked()){
            binding.prefCheck.setChecked(true);
            binding.partCheck.setChecked(false);
            binding.workHomeCheck.setChecked(false);
            SessionManagement.setCheckWorkHome(false);
            SessionManagement.setCheckPartTime(false);
        }
        if (SessionManagement.isWorkHomeChecked()){
            binding.workHomeCheck.setChecked(true);
            SessionManagement.saveCheckWorkHome("workfromhome");

        }
        if (SessionManagement.isPartTimeChecked()){
            binding.partCheck.setChecked(true);
            SessionManagement.saveCheckPart("parttime");
        }

//        stipend = String.valueOf(binding.stipendSelect.getSelectionStart());
//        duration = String.valueOf(binding.duration.getSelectionStart());


        if (SessionManagement.getMiniumStipend() != null){
            Log.d("TAG2", "checkSession: ----Stipend stipend ----"+ SessionManagement.getMiniumStipend()+stipend);
            //binding.stipendSelect.setSelection(Integer.parseInt(SessionManagement.getMiniumStipend()));
//            binding.stipendSelect.setText(binding.stipendSelect.getAdapter().getItem(Integer.parseInt(SessionManagement.getMiniumStipend())).toString(),false);
        }
        if (SessionManagement.getMaximumDuration() != null){
            Log.d("TAG2", "checkSession: ----Duration---"+SessionManagement.getMaximumDuration());
            
//            binding.duration.setSelection(Integer.parseInt(SessionManagement.getMaximumDuration()));
        }

    }

    private void checkFilters() {
        binding.prefCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.prefCheck.isChecked()){

                    SessionManagement.setCheckWorkHome(false);
                    SessionManagement.setCheckPartTime(false);

                    binding.workHomeCheck.setChecked(false);
                    binding.partCheck.setChecked(false);

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
            }
        });





        binding.applyBtn.setOnClickListener(view -> {
            if (binding.workHomeCheck.isChecked()){
                SessionManagement.setCheckWorkHome(true);
                SessionManagement.saveCheckWorkHome("workfromhome");
                Log.d("TAG2", "onViewCreated: ------WorkHomeCheck"+SessionManagement.isWorkHomeChecked());
            }else {
                SessionManagement.setCheckWorkHome(false);
                SessionManagement.saveCheckWorkHome("");
            }

            if (binding.partCheck.isChecked()){
                SessionManagement.setCheckPartTime(true);
                SessionManagement.saveCheckPart("parttime");
                Log.d("TAG2", "onViewCreated: ------part time ---"+SessionManagement.getCheckPart());
            }else {
                SessionManagement.setCheckPartTime(false);
                SessionManagement.saveCheckPart("");
            }

            if (binding.prefCheck.isChecked()){
                SessionManagement.setCheckPref(true);
                SessionManagement.setCheckPartTime(false);
                SessionManagement.setCheckWorkHome(false);
                SessionManagement.saveCheckPart("");
                SessionManagement.saveCheckWorkHome("");
                Log.d("TAG2", "onViewCreated: ------PrefCheck"+SessionManagement.isWorkHomeChecked());
            }else {
                SessionManagement.setCheckPref(false);
            }

//            stipend = String.valueOf(binding.stipendSelect.getSele());
//            duration = String.valueOf(binding.duration.getSelectionEnd());

            binding.stipendSelect.setOnItemClickListener((adapterView, view12, i, l) -> stipend = String.valueOf(i));

            binding.duration.setOnItemClickListener((adapterView, view1, i, l) -> duration = String.valueOf(i));

            if (stipend != null){
                SessionManagement.saveMinimumStipend(stipend);
            }else {
                SessionManagement.saveMinimumStipend("");
            }

            if (duration != null){
                SessionManagement.saveMaximumDuration(duration);
            }else{
                SessionManagement.saveMaximumDuration("");
            }

            onBackPressed();


        });

        binding.preferences.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("comesFrom","filter");
            PreferenceFragment prefFrag = new PreferenceFragment();
            prefFrag.setArguments(bundle);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_cont,prefFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.resetBtn.setOnClickListener(view -> {

            binding.prefCheck.setChecked(false);
            binding.workHomeCheck.setChecked(false);
            binding.partCheck.setChecked(false);

            binding.stipendSelect.clearListSelection();
            binding.duration.clearListSelection();
        });




        binding.addCateg.setOnClickListener(view -> {

        });
    }

    @Override
    public boolean onBackPressed() {
        IntershipsFragment internFrag = new IntershipsFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_cont,internFrag);
        transaction.commit();

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}