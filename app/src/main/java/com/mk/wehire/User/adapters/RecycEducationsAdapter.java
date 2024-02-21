package com.mk.wehire.User.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.wehire.R;
import com.mk.wehire.User.models.ApplicationModel;
import com.mk.wehire.User.models.EducationModel;
import com.mk.wehire.User.models.SignUpUser;

import java.util.ArrayList;

public class RecycEducationsAdapter extends RecyclerView.Adapter<RecycEducationsAdapter.EducationViewHolder>{

    private static final String TAG = RecycEducationsAdapter.class.getSimpleName();

    Context context;
    ArrayList<SignUpUser> educationModelList;
    OnItemClickListener onItemClickListener;
    OnClickListener onClickListener;


    public RecycEducationsAdapter(Context context, ArrayList<SignUpUser> educationModelList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.educationModelList = educationModelList;
        this.onItemClickListener = onItemClickListener;
    }
//    public void setOnClickListener(View.OnClickListener listener) {
//        this.onClickListener= (OnClickListener) listener;
//    }

    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_education,parent,false);
        return new EducationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SignUpUser educationModel = educationModelList.get(position);
        holder.degree.setText(educationModel.getDegreeName());
        holder.college.setText(educationModel.getCollegeName());
        holder.stream.setText(educationModel.getStreamName());
        if (educationModel.getPerformanceScale().equals("Percentage")){
            holder.performance.setText(educationModel.getPerformane()+" "+educationModel.getPerformanceScale());
        }else {
            holder.performance.setText(educationModel.getPerformane()+".00/10 "+educationModel.getPerformanceScale());
        }



        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(educationModelList.get(position));
        });


        holder.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onEditClick(view,position,educationModelList.get(position));
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onDeleteClick(view,position,educationModelList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return educationModelList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(SignUpUser education);
    }
    public interface OnClickListener {
        void onEditClick(View view, int position,SignUpUser education);
        void onDeleteClick(View view, int position,SignUpUser education);
    }


    public class EducationViewHolder extends RecyclerView.ViewHolder {

        ImageView editImg,deleteImg;
        TextView degree, college, stream,performance,performanceScale;

        public EducationViewHolder(@NonNull View itemView) {
            super(itemView);

            degree = itemView.findViewById(R.id.degreetxt);
            college = itemView.findViewById(R.id.collegeNametxt);
            stream = itemView.findViewById(R.id.streamtxt);
            performance = itemView.findViewById(R.id.cgpatxt);
            editImg = itemView.findViewById(R.id.editImg);
            deleteImg = itemView.findViewById(R.id.deleteImg);


        }
    }
}
