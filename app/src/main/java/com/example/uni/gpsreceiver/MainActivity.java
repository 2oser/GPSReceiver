package com.example.uni.gpsreceiver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int NETWORK_CHOSEN = 1;
    private static final int SATELLITE_CHOSEN = 2;
    private int choice = 1;

    private DecimalFormat df = new DecimalFormat("#.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bUpdate = findViewById(R.id.buttonUpdate);
        final Button bNetwork = findViewById(R.id.buttonNetwork);
        final Button bSatellite = findViewById(R.id.buttonSatellite);

        bUpdate.setOnClickListener(this);
        bNetwork.setOnClickListener(this);
        bSatellite.setOnClickListener(this);

        getLocation();
    }

    public void onClick(View v) {
        //case switch to differentiate different operations
        switch (v.getId()) {
            case R.id.buttonUpdate:
                Log.d("", "updated");
                break;
            case R.id.buttonNetwork: {
                choice = NETWORK_CHOSEN;
                Log.d("", "" + choice);
                break;
            }
            case R.id.buttonSatellite: {
                choice = SATELLITE_CHOSEN;
                Log.d("", "" + choice);
                break;
            }
            default:
                break;
        }

        getLocation();

    }

    private void getLocation() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                changedLocation(location);
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION

            }, MY_PERMISSION_REQUEST_CODE);
        }

        //Default benutzt gps
        String provider = LocationManager.GPS_PROVIDER;

        switch (choice) {
            case NETWORK_CHOSEN:
                provider = LocationManager.NETWORK_PROVIDER;
                break;
            case SATELLITE_CHOSEN:
                provider = LocationManager.GPS_PROVIDER;
                break;
        }

        Log.d("", "" + provider);

        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);

    }

    private void makeUseOfNewLocation(Location location) {
    }

    public void changedLocation(Location location) {
        String text = df.format(location.getAltitude());
        TextView altitude = findViewById(R.id.textViewAltitude);
        altitude.setText(text);

        text = df.format(location.getLatitude());
        TextView latLng = findViewById(R.id.textViewLatitude);
        latLng.setText(text);

        text = df.format(location.getLongitude());
        TextView longLng = findViewById(R.id.textViewLongitude);
        longLng.setText(text);

        text = df.format(location.getSpeed());
        TextView speed = findViewById(R.id.textViewSpeed);
        speed.setText(text);
    }
}
