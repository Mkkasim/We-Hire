package com.mk.wehire.Company.adapters;

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

public class RecycMyPostsAdapter extends RecyclerView.Adapter<RecycMyPostsAdapter.ApplicationViewHolder>{

    private static final String TAG = RecycMyPostsAdapter.class.getSimpleName();

    Context context;
    ArrayList<IntershipModel>  internModelList;
    OnItemClickListener onItemClickListener;
    OnClickListener onClickListener;


    public RecycMyPostsAdapter(Context context, ArrayList<IntershipModel> internModelList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.internModelList = internModelList;
        this.onItemClickListener = onItemClickListener;
    }
//    public void setOnClickListener(View.OnClickListener listener) {
//        this.onClickListener= (OnClickListener) listener;
//    }

    public void filterList(ArrayList<IntershipModel> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        internModelList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_myposts,parent,false);
        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {

        IntershipModel intershipModel = internModelList.get(position);
        holder.course_name.setText(intershipModel.getCourseName());
        holder.company_name.setText(intershipModel.getCompanyName());
        holder.appStatus.setText(intershipModel.getAppStatus());



        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(internModelList.get(position));
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

        holder.seeAllApplicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.seeAllApplicantsClick(view,position,internModelList.get(position));
            }
        });
        holder.quetions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onQuetionClick(view,position,internModelList.get(position));
            }
        });

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onDeleteImgClick(view,position,internModelList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return internModelList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnItemClickListener{
        void onItemClick(IntershipModel application);
    }
    public interface OnClickListener {
        void onQuetionClick(View view, int position,IntershipModel app);
        void seeAllApplicantsClick(View view, int position,IntershipModel app);
        void onDeleteImgClick(View view, int position,IntershipModel app);

    }


    public class ApplicationViewHolder extends RecyclerView.ViewHolder {

        ImageView quetions,editImg,deleteImg;
        TextView course_name, company_name, appStatus,seeAllApplicants,totalApp;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.courseName);
            company_name = itemView.findViewById(R.id.companyName);
            appStatus = itemView.findViewById(R.id.appStatus);
            seeAllApplicants = itemView.findViewById(R.id.seeAllApplicants);
            quetions = itemView.findViewById(R.id.questionImg);
            deleteImg = itemView.findViewById(R.id.deletePostImg);


        }
    }
}
