package com.martijnkoning.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    Official o;
    private static final String TAG = "PhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent intent = getIntent();

        ((TextView) findViewById(R.id.loc_address2)).setText(intent.getStringExtra("address"));
        o = (Official) intent.getSerializableExtra("official");

        if (o != null) {
            loadRemoteImage(o.getPhoto());
        }

        TextView office = findViewById(R.id.office);
        TextView name = findViewById(R.id.name);

        office.setText(o.getOffice());
        name.setText(o.getName());

        ImageView republican = findViewById(R.id.republican);
        ImageView democrat = findViewById(R.id.democrat);

        // set the background color and party logo based on party affiliation
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        if (o.getParty().toLowerCase().startsWith("republican")) {
            constraintLayout.setBackgroundColor(Color.parseColor("#EA3223"));
            republican.setVisibility(View.VISIBLE);
        } else if (o.getParty().toLowerCase().startsWith("democrat")) {
            constraintLayout.setBackgroundColor(Color.parseColor("#0023F4"));
            democrat.setVisibility(View.VISIBLE);
        } else {
            constraintLayout.setBackgroundColor(Color.parseColor("#000000"));
        }


    }

    private void loadRemoteImage(final String imageURL) {
        Log.d(TAG, "loadImage: " + imageURL);
        ImageView photo = findViewById(R.id.photo);
        Picasso picasso = new Picasso.Builder(this).build();
        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(photo);
    }
}
