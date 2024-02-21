package com.mk.wehire.User.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.mk.wehire.R;
import com.mk.wehire.User.models.IntershipModel;
import com.mk.wehire.User.sessionmanager.SessionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecycInternAdapter extends RecyclerView.Adapter<RecycInternAdapter.InternViewHolder>{

    private static final String TAG = RecycInternAdapter.class.getSimpleName();

    Context context;
    ArrayList<IntershipModel> intershipModelList;
    ArrayList<IntershipModel> intershipListFiltered = new ArrayList<>();
    OnItemClickListener onItemClickListener;


    public RecycInternAdapter(Context context, ArrayList<IntershipModel> intershipModelList,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.intershipModelList = intershipModelList;
        this.onItemClickListener = onItemClickListener;
    }

    public void filterList(ArrayList<IntershipModel> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        intershipModelList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InternViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.internship_items,parent,false);
        return new InternViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InternViewHolder holder, int position) {

        IntershipModel intershipModel = intershipModelList.get(position);
        holder.course_name.setText(intershipModel.getCourseName());
        holder.company_name.setText(intershipModel.getCompanyName());
        holder.salary.setText(intershipModel.getSalary());
        holder.work_location.setText(intershipModel.getWorkLocation());
        holder.part_full_time.setText(intershipModel.getPartFullTime());
        holder.period_months.setText(intershipModel.getTotalMonths());
        holder.job_or_intern.setText(intershipModel.getJobOrIntern());

        Glide.with(context)
                .load(intershipModel.getCompanyImgUrl())
                .centerCrop()
                .into(holder.company_img);

        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(intershipModelList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return intershipModelList.size();
    }

    public List<IntershipModel> multipleFilter(String workLocation, String partOrFull , ArrayList<IntershipModel> listAllTasks)
    {
        List<IntershipModel> listTasksAfterFiltering = new ArrayList<>();
        for(IntershipModel task_obj : listAllTasks)
        {
            String WorkLocation = SessionManagement.getCheckWorkHome();
            String PartFullTime = SessionManagement.getCheckPart();


            if(workLocation.equals(WorkLocation) || workLocation.isEmpty())
                if(partOrFull.equals(PartFullTime) || partOrFull.isEmpty())
                    if(!workLocation.isEmpty() || !partOrFull.isEmpty()){
                        listTasksAfterFiltering.add(task_obj);
                    }
        }

        return listTasksAfterFiltering;

    }

    public void filterMonths(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        intershipModelList.clear();
        if (charText.length() == 0) {
            intershipModelList.addAll(intershipListFiltered);
        } else {
            for (IntershipModel wp : intershipListFiltered) {
                if(wp.getTotalMonths().toLowerCase(Locale.getDefault()).contains(charText)){
                    intershipModelList.add(wp);
                }else {

                }
            }
        }
        notifyDataSetChanged();
    }

//    public void filterDateRange(Date charText) {
//        intershipModelList.clear();
//        if (charText.equals("")||charText.equals(null)) {
//            intershipModelList.addAll(intershipListFiltered);
//        } else {
//            for (IntershipModel wp : intershipListFiltered) {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                    Date strDate = sdf.parse(wp.g());
//                    if (charText1.after(strDate)&&charText.before(strDate)) {
//                        intershipModelList.add(wp);
//                    }else{
//
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        notifyDataSetChanged();
//    }

    public interface OnItemClickListener{
        void onItemClick(IntershipModel addCourseModel);
    }


    public class InternViewHolder extends RecyclerView.ViewHolder {

        ImageView company_img;
        TextView course_name, company_name, work_location,salary,part_full_time,period_months,job_or_intern;

        public InternViewHolder(@NonNull View itemView) {
            super(itemView);

            company_img = itemView.findViewById(R.id.CompanyImg);
            course_name = itemView.findViewById(R.id.CourseName);
            company_name = itemView.findViewById(R.id.CompanyName);
            work_location = itemView.findViewById(R.id.WorkLocation);
            salary = itemView.findViewById(R.id.salary);
            part_full_time = itemView.findViewById(R.id.timeFullPart);
            period_months = itemView.findViewById(R.id.periodMonths);
            job_or_intern = itemView.findViewById(R.id.JobOrIntern);


        }
    }
}
