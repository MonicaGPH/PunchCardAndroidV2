package com.inverseapps.punchcard.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Inverse, LLC on 11/9/16.
 */

public abstract class PCLocationActivity extends PCActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    protected Location currentLocation;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final int REQUEST_CHECK_SETTINGS = 1;

    private static final int REQUEST_PERMISSIONS_LOCATION = 2;

    private ResultCallbackDelegate resultCallbackDelegate;
    private LocationListenerDelegate locationListenerDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultCallbackDelegate = new ResultCallbackDelegate(this);
        locationListenerDelegate = new LocationListenerDelegate(this);

        buildGoogleApiClient();
        createLocationReques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
       // stopLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d("Connected Google Api client");
        changingLocationSettingsIfNeeded();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.d("Google Api client connection is suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.d("Google Api client connection is failed");
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationReques() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS / 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_LOCATION);

                    // REQUEST_PERMISSIONS_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                handleLocationPermissionsDidSet();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                handleLocationPermissionsNotSet();
            } else {
                handleLocationPermissionsDidSet();
            }
        }
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListenerDelegate);
    }

    private void changingLocationSettingsIfNeeded() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());
        result.setResultCallback(resultCallbackDelegate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    handleLocationPermissionsDidSet();
                } else {
                    // permission denied
                    handleLocationPermissionsNotSet();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Logger.d("Location settings are enable by user.");
                startLocationUpdates();
            } else if (resultCode == RESULT_CANCELED) {
                Logger.d("Location settings are not enable. User cancel.");
                handleLocationSettingsNotSet();
            }
        }
    }

    private void handleLocationSettingsNotSet() {
        Toast.makeText(this, "You have to turn on location settings to use this feature!", Toast.LENGTH_LONG).show();
    }

    private void handleLocationPermissionsNotSet() {
        Toast.makeText(this, "You have to turn on location permissions to use this feature!", Toast.LENGTH_LONG).show();
    }

    private void handleLocationPermissionsDidSet() {
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, locationListenerDelegate);
    }

    private static class LocationListenerDelegate implements LocationListener {

        private final WeakReference<PCLocationActivity> activityWeakReference;

        public LocationListenerDelegate(PCLocationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onLocationChanged(Location location) {
            PCLocationActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isDestroyed() && !activity.isDestroyed()) {
                activity.currentLocation = location;
            }
        }
    }

    private static class ResultCallbackDelegate implements ResultCallback<LocationSettingsResult> {

        private final WeakReference<PCLocationActivity> activityWeakReference;
        public ResultCallbackDelegate(PCLocationActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onResult(@NonNull LocationSettingsResult result) {
            PCLocationActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isDestroyed() && !activity.isDestroyed()) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Logger.d("All location settings are satisfied. The client can initialize location requests here.");
                        activity.startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Logger.d("Location settings are not satisfied, but this can be fixed by showing the user a dialog.");
                        try {
                            Logger.d("Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().");
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Logger.d(Log.getStackTraceString(e));
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Logger.d("Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.");
                        Toast.makeText(activity, "Your device does not support location service settings!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    }
}
