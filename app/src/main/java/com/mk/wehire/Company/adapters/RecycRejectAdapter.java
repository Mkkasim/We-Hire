package com.mk.wehire.Company.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mk.wehire.R;
import com.mk.wehire.User.models.SignUpUser;

import java.util.ArrayList;

public class RecycRejectAdapter extends RecyclerView.Adapter<RecycRejectAdapter.ApplicationViewHolder>{

    private static final String TAG = RecycRejectAdapter.class.getSimpleName();

    Context context;
    ArrayList<SignUpUser>  signUpUsersList;
    OnItemClickListener onItemClickListener;
    OnClickListener onClickListener;


    public RecycRejectAdapter(Context context, ArrayList<SignUpUser> signUpUsersList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.signUpUsersList = signUpUsersList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_applicants,parent,false);
        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SignUpUser signUpUser = signUpUsersList.get(position);
        holder.userName.setText(signUpUser.getFirstName()+" "+signUpUser.getLastName());
        holder.degreeName.setText(signUpUser.getDegreeName());
        holder.locationAdd.setText(signUpUser.getLocation());
        holder.mobileNum.setText(signUpUser.getMobile());

        holder.RejectBtn.setClickable(false);
        holder.RejectBtn.setText("Rejected");
        holder.RejectBtn.setBackgroundColor(context.getResources().getColor(R.color.grey));
        holder.hireBtn.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(signUpUsersList.get(position));
        });


//        holder.hireBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickListener.onHireClick(view,position,signUpUsersList.get(position));
//            }
//        });
        holder.RejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onRejectClick(view,position,signUpUsersList.get(position));
            }
        });


        holder.mobileNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onCallClick(view,position,signUpUsersList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return signUpUsersList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(SignUpUser signUp);
    }
    public interface OnClickListener {
        void onHireClick(View view, int position,SignUpUser signUp);
        void onRejectClick(View view, int position,SignUpUser signUp);
        void onChatClick(View view, int position,SignUpUser signUp);
        void onCallClick(View view, int position,SignUpUser signUp);
    }


    public class ApplicationViewHolder extends RecyclerView.ViewHolder {

        MaterialButton hireBtn,RejectBtn;
        TextView userName, degreeName, locationAdd,mobileNum,chatTxt;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.UserName);
            degreeName = itemView.findViewById(R.id.degreeName);
            locationAdd = itemView.findViewById(R.id.locationName);
            mobileNum = itemView.findViewById(R.id.mobileNum);

            hireBtn = itemView.findViewById(R.id.HireBtn);
            RejectBtn = itemView.findViewById(R.id.rejectBtn);



        }
    }
}
