package com.martijnkoning.knowyourgovernment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfficialsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "EmployeesAdapter";
    private List<Official> officialList;
    private MainActivity mainAct;

    OfficialsAdapter(List<Official> offList, MainActivity ma) {
        this.officialList = offList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_row, parent, false);

        itemView.setOnClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Official " + position);

        Official official = officialList.get(position);

        holder.office.setText(official.getOffice());
        String name;
        if (official.getParty() != null)
            name = official.getName() + " (" + official.getParty() + ")";
        else
            name = official.getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
