package com.hardik.fakewalk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WelcomeActivity extends Activity {

    private PermissionChecker mPermissionChecker;

    @InjectView(R.id.chk_enable_service)
    CheckBox chkEnableService;

    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.inject(this);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
            }
        });

        enableStart(false);

        chkEnableService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startHeadService();
                } else {
                    stopHeadService();
                }
            }
        });

        mPermissionChecker = new PermissionChecker(this);
        if (!mPermissionChecker.isRequiredPermissionGranted()) {
            enableStart(false);
            Intent intent = mPermissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
        } else {
            enableStart(true);
        }

    }

    private void setMockLocation(double latitude, double longitude, float accuracy) {
        lm.addTestProvider(LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(accuracy);

        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        lm.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null, System.currentTimeMillis());

        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);

    }

    private void enableStart(boolean checked) {
        chkEnableService.setChecked(checked);
    }

    private void startHeadService() {
        startService(new Intent(this, HeadService.class));
    }

    private void stopHeadService() {
        stopService(new Intent(this, HeadService.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE) {
            if (!mPermissionChecker.isRequiredPermissionGranted()) {
                Toast.makeText(this, "Required permission is not granted. Please restart the app and grant required permission.", Toast.LENGTH_LONG).show();
            } else {
                enableStart(true);
            }
        }
    }

    public void onMockLocation(View view) {
        setMockLocation(19.1126574, 72.8280304, 2);
    }
}
