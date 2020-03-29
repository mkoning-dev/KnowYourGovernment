package com.martijnkoning.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView office;
    TextView name;

    MyViewHolder(@NonNull View itemView) {
        super(itemView);
        office = itemView.findViewById(R.id.office);
        name = itemView.findViewById(R.id.name);

    }
}
