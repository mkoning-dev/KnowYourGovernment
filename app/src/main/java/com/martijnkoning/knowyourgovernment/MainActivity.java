package com.martijnkoning.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Official> officialList = new ArrayList<>();
    OfficialsAdapter oAdapter = new OfficialsAdapter(officialList, this);

    private RecyclerView recyclerView;

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (doNetCheck()) {
            String zip = setLocation();
            if (!zip.equalsIgnoreCase("nolocs"))
                new OfficialDownloader(this).execute(zip);
        }
        else {
            ((TextView) findViewById(R.id.loc_address)).setText(R.string.no_loc_data);
            noNetworkDialog();
        }

    }

    // Runs on post execute of OfficalDownloader
    // Adds downloaded officials to officialList if not empty
    public void createList(String address, List<Official> oList) {
        ((TextView) findViewById(R.id.loc_address)).setText(address);
        officialList.clear();
        if (oList.isEmpty())
            ((TextView) findViewById(R.id.loc_address)).setText(R.string.no_loc_data);
        else
            officialList.addAll(oList);

        oAdapter.notifyDataSetChanged();
    }

    // When official in list is clicked, go to Official Activity
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official o = officialList.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("official", o);
        intent.putExtra("address", ((TextView) findViewById(R.id.loc_address)).getText());
        startActivity(intent);
    }

    // Options menu related items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (doNetCheck())
                    searchClicked();
                else
                    noNetworkDialog();
                return true;
            case R.id.about:
                Intent about = new Intent(this, About.class);
                startActivity(about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Shows the search dialog
    // If data is entered and there is an internet connection, do location search
    // If there is no internet connection, display dialog
    // If there is and no data is entered, do nothing
    // If no results are found, show toast indicating so
    public void searchClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (doNetCheck()) {
                    if (!et.getText().toString().trim().isEmpty()) {
                        String zip = doLocationName(et.getText().toString());
                        if (!zip.equals("noloc"))
                            new OfficialDownloader(MainActivity.this).execute(zip);
                        else
                            Toast.makeText(MainActivity.this, "No location found!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    officialList.clear();
                    oAdapter.notifyDataSetChanged();
                    ((TextView) findViewById(R.id.loc_address)).setText(R.string.no_loc_data);
                    noNetworkDialog();
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setMessage("Enter a City, State, or Zip code:");
        builder.setTitle("Single Input");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Check for internet connection
    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    // If there is no internet connection, show this dialog
    public void noNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded\n" +
                "without an internet connection.");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Location search related code below
    @SuppressLint("MissingPermission")
    private String setLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            int MY_LOCATION_REQUEST_CODE_ID = 123;
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            assert locationManager != null;
            Location currentLocation = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, true)));

            if (currentLocation != null) {
                try {
                    List<Address> addresses;
                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

                    return addresses.get(0).getPostalCode();

                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                return "noloc";
            }
        }
        return "noloc";
    }

    public String doLocationName(String s) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;

            addresses = geocoder.getFromLocationName(s, 1);
            if (addresses.size() > 0) {
                if (addresses.get(0).getPostalCode() != null) {
                    Log.d(TAG, "doLocationName: " + addresses.get(0).getPostalCode());
                    return addresses.get(0).getPostalCode();
                }
                else
                    return doLatLon(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
            else
                return "noloc";
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return "noloc";
    }

    public String doLatLon(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;

            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.get(0).getPostalCode() != null)
                return addresses.get(0).getPostalCode();
            else
                return "noloc";

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return "noloc";
    }

}
