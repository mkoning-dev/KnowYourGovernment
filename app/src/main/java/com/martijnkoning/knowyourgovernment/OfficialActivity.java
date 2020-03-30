package com.martijnkoning.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OfficialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        Intent intent = getIntent();

        Official o = (Official) intent.getSerializableExtra("official");

        TextView office = findViewById(R.id.office);
        TextView name = findViewById(R.id.name);
        TextView party = findViewById(R.id.party);
        TextView address = findViewById(R.id.address);
        TextView phone = findViewById(R.id.phone);
        TextView website = findViewById(R.id.website);
        assert o != null;
        String fullAddress = o.getAddress() + "\n" + o.getCity() + ", " + o.getState() + " " + o.getZip();
        office.setText(o.getOffice());
        name.setText(o.getName());
        party.setText(o.getParty());
        address.setText(fullAddress);
        phone.setText(o.getPhone());
        website.setText(o.getUrl());

        ImageView republican = findViewById(R.id.republican);
        ImageView democrat = findViewById(R.id.democrat);

        // set the background color and party logo based on party affiliation
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        if (o.getParty().toLowerCase().startsWith("republican")) {
            constraintLayout.setBackgroundColor(Color.parseColor("#EA3223"));
            republican.setVisibility(View.VISIBLE);
        }
        else if (o.getParty().toLowerCase().startsWith("democrat")) {
            constraintLayout.setBackgroundColor(Color.parseColor("#0023F4"));
            democrat.setVisibility(View.VISIBLE);
        }
        else {
            constraintLayout.setBackgroundColor(Color.parseColor("#000000"));
        }
    }

}
