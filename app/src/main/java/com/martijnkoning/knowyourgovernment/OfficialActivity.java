package com.martijnkoning.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    Official o;
    private static final String TAG = "OfficialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        Intent intent = getIntent();

        ((TextView) findViewById(R.id.loc_address2)).setText(intent.getStringExtra("address"));
        o = (Official) intent.getSerializableExtra("official");

        TextView office = findViewById(R.id.office);
        TextView name = findViewById(R.id.name);
        TextView party = findViewById(R.id.party);
        TextView address = findViewById(R.id.address);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView website = findViewById(R.id.website);

        ImageView photo = findViewById(R.id.photo);
        ImageView facebook = findViewById(R.id.facebook);
        ImageView twitter = findViewById(R.id.twitter);
        ImageView youtube = findViewById(R.id.youtube);
        ImageView google = findViewById(R.id.googleplus);

        assert o != null;
        if (o.getPhoto() != null) {
            photo.setClickable(true);
            loadRemoteImage(o.getPhoto());
        }
        else
            photo.setClickable(false);

        if (o.getAddress() != null) {
            String fullAddress = o.getAddress() + "\n" + o.getCity() + ", " + o.getState() + " " + o.getZip();
            address.setText(fullAddress);
            Linkify.addLinks(address, Linkify.ALL);
        } else {
            address.setVisibility(View.GONE);
            findViewById(R.id.addressLabel).setVisibility(View.GONE);
        }
        if (o.getEmail() != null) {
            email.setText(o.getEmail());
            Linkify.addLinks(email, Linkify.ALL);
        } else {
            email.setVisibility(View.GONE);
            findViewById(R.id.emailLabel).setVisibility(View.GONE);
        }
        if (o.getUrl() != null) {
            website.setText(o.getUrl());
            Linkify.addLinks(website, Linkify.ALL);
        } else {
            website.setVisibility(View.GONE);
            findViewById(R.id.websiteLabel).setVisibility(View.GONE);
        }
        if (o.getPhone() != null) {
            phone.setText(o.getPhone());
            Linkify.addLinks(phone, Linkify.ALL);
        } else {
            phone.setVisibility(View.GONE);
            findViewById(R.id.phoneLabel).setVisibility(View.GONE);
        }

        if (o.getFacebook() == null)
            facebook.setVisibility(View.INVISIBLE);
        if (o.getTwitter() == null)
            twitter.setVisibility(View.INVISIBLE);
        if (o.getYoutube() == null)
            youtube.setVisibility(View.INVISIBLE);
        if (o.getGoogle() == null)
            google.setVisibility(View.INVISIBLE);

        office.setText(o.getOffice());
        name.setText(o.getName());
        String partyText = "(" + o.getParty() + ")";
        party.setText(partyText);

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

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + o.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + o.getFacebook();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent;
        String name = o.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void youTubeClicked(View v) {
        String name = o.getYoutube();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void googlePlusClicked(View v) {
        String name = o.getGoogle();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }

    }

    public void photoClicked(View v) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("official", o);
        intent.putExtra("address", ((TextView) findViewById(R.id.loc_address2)).getText());
        startActivity(intent);

    }

}
