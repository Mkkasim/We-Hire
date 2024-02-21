package com.mk.wehire.User.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.wehire.R;
import com.mk.wehire.User.models.IntershipModel;

import java.util.ArrayList;

public class RecycSkillsAdapter extends RecyclerView.Adapter<RecycSkillsAdapter.InternViewHolder> {

    Context context;
    ArrayList<IntershipModel> list = new ArrayList<>();

    public RecycSkillsAdapter(Context context, ArrayList<IntershipModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InternViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.perks_skills_items,parent,false);
        return new InternViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull InternViewHolder holder, int position) {

        holder.perksSkills.setText(list.get(position).getSkill());
        Log.d("TAG2", "onBindViewHolder: List-----"+list.get(position).getPerk());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InternViewHolder extends RecyclerView.ViewHolder {

        TextView perksSkills;

        public InternViewHolder(@NonNull View itemView) {
            super(itemView);

            perksSkills = itemView.findViewById(R.id.perks_skills);

        }
    }
}
