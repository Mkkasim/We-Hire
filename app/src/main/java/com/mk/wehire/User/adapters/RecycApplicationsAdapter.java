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
import com.mk.wehire.User.models.IntershipModel;

import java.util.ArrayList;

public class RecycApplicationsAdapter extends RecyclerView.Adapter<RecycApplicationsAdapter.ApplicationViewHolder>{

    private static final String TAG = RecycApplicationsAdapter.class.getSimpleName();

    Context context;
    ArrayList<ApplicationModel> applicationModelsList;
    OnItemClickListener onItemClickListener;
    OnClickListener onClickListener;


    public RecycApplicationsAdapter(Context context, ArrayList<ApplicationModel> applicationModelsList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.applicationModelsList = applicationModelsList;
        this.onItemClickListener = onItemClickListener;
    }
//    public void setOnClickListener(View.OnClickListener listener) {
//        this.onClickListener= (OnClickListener) listener;
//    }

    public void filterList(ArrayList<ApplicationModel> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        applicationModelsList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.application_items,parent,false);
        return new ApplicationViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {

        ApplicationModel applicationModel = applicationModelsList.get(position);
        holder.course_name.setText(applicationModel.getCourseName());
        holder.company_name.setText(applicationModel.getCompanyName());
        holder.appStatus.setText(applicationModel.getAppStatus());
        
        String appStatus = applicationModel.getAppStatus();
         if (appStatus.equals("Not Selected")){
            holder.appStatus.setTextColor(R.color.grey);
        }
        


        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(applicationModelsList.get(position));
        });

//        holder.quetions.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                onItemClickListener.onquetionClick(view);
//            });
//
//        holder.appStatus.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                onItemClickListener.onAppStatusClick(view);
//            });

        holder.appStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onAppStatusClick(view,position,applicationModelsList.get(position));
            }
        });
        holder.quetions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onQuetionClick(view,position,applicationModelsList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return applicationModelsList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(ApplicationModel application);
    }
    public interface OnClickListener {
        void onQuetionClick(View view, int position,ApplicationModel app);
        void onAppStatusClick(View view, int position,ApplicationModel app);
    }


    public class ApplicationViewHolder extends RecyclerView.ViewHolder {

        ImageView quetions;
        TextView course_name, company_name, appStatus,reviewApplication;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.courseName);
            company_name = itemView.findViewById(R.id.companyName);
            appStatus = itemView.findViewById(R.id.appStatus);
            reviewApplication = itemView.findViewById(R.id.reviewApplication);
            quetions = itemView.findViewById(R.id.questionImg);


        }
    }
}
