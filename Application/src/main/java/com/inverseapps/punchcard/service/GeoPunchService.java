package com.inverseapps.punchcard.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;


import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;


import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import com.inverseapps.punchcard.utils.PunchCardApplication;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

public class GeoPunchService extends Service
        implements
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    public GoogleApiClient mGoogleApiClient;
    public PendingIntent mGeofencePendingIntent;
    public IBinder mBinder = new LocalBinder();
    public static final String KEY_EMPLOYEE_ID = "employeeid";
    public static final String KEY_PROJECT = "project";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PROJECT_UNIQUE_ID = "projectUniqueId";
    private Project project;
    private String employeeId;
    private Employee employee;
    private  PendingIntent alarmIntent;
    private Location lastLocation;
    private LatLng latLng;
    private AlarmManager alarmMgr;
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    public List<Geofence> mGeofenceList = new ArrayList<>();
    public String projectUniqueID;
    public String geofenceId;
    public static String custom_action= "CALL_CLEAR_GEOFENCE";
    private final int REQ_PERMISSION = 999;
    public  String action;
    protected PCFunctionService pcFunctionService;

    public class LocalBinder extends Binder {
        public GeoPunchService getGeoPunchServiceInstance() {
            return GeoPunchService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addOnConnectionFailedListener(this)
                 .addConnectionCallbacks(this)
                 .addApi(LocationServices.API)
                 .build();
            mGoogleApiClient.connect();

        project= getPCApplication().getPcFunctionService().getInternalStoredProject();
        projectUniqueID  = project.getUniq_id();
        mGoogleApiClient.connect();
        action = intent.getAction();

        if (action == null) {

        } else if (action.equalsIgnoreCase(custom_action)) {
            if(mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addOnConnectionFailedListener(this)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();

            }

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        geofenceId  = UUID.randomUUID().toString();
        pcFunctionService = getPCApplication().getPcFunctionService();

    }



    public final PunchCardApplication getPCApplication() {
        return (PunchCardApplication) getApplication();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.d("onConnected");
        if( action != null && action.equalsIgnoreCase(custom_action)) {
            getLastKnownLocation();
            clearGeofence();// here you invoke service method
            return;
        }
        getLastKnownLocation();
        try {
            mGeofencePendingIntent = getGeofencePendingIntent();
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    mGeofencePendingIntent
            ).setResultCallback(this); // Result processed in onResult().


        } catch (SecurityException securityException) {
            Log.i(getClass().getSimpleName(), securityException.getMessage());
        }

    }


    // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
    @Override
    public void onConnectionSuspended(int i) {
        Logger.d("onConnectedSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     * <p>
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    @Override
    public void onResult(@NonNull Status status) {


        if (status.isSuccess()) {
            Logger.i("GeofenceAdded");

        } else {
            Logger.i("error");
        }
    }


    private GeofencingRequest getGeofencingRequest() {
        Logger.d("getGeofencingRequest");
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();


        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(getGeofence(project.getLatitude(), project.getLongitude(), project.getRadius()));

        return builder.build();
    }

    private List<Geofence> getGeofence(double latitude, double longitude, float radius) {

        Logger.d("getGeofence");


        //add one object
        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(geofenceId)

                // Set the circular region of this geofence.


                .setCircularRegion(
                        latitude, //lat
                        longitude, //long
                        radius) // radius

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                //1000 millis  * 60 sec * 5 min
                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL )
                .setLoiteringDelay(1000*60*10) // 10 minute
                // Create the geofence.
                .build());

        return mGeofenceList;

    }

    private PendingIntent getGeofencePendingIntent() {

        Logger.d("getGeofencePendingIntent");
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("geopunchdata", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PROJECT_UNIQUE_ID, projectUniqueID);
        editor.putString(KEY_LATITUDE, String.valueOf(lastLocation.getLatitude()));
        editor.putString(KEY_LONGITUDE, String.valueOf(lastLocation.getLongitude()));
        editor.apply();

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Get last known location
    private void getLastKnownLocation() {
        Logger.d("getLastKnownLocation()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED/* && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            Logger.i("LasKnown location. " +
                    "Long: " + lastLocation.getLongitude() +
                    " | Lat: " + lastLocation.getLatitude());


        } else {
            Logger.w( "No location retrieved yet");
            startLocationUpdates();
        }
    }

    // Start location Updates
    private void startLocationUpdates() {
        Logger.i("startLocationUpdates()");
        Location location = null;
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(5000)
                .setInterval(30 * (60 * 1000)) ;// Every 30 mins

        if ( checkPermission() )
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        lastLocation = location;
        String msg = "Updated Location: " +
                Double.toString(lastLocation.getLatitude()) + "," +
                Double.toString(lastLocation.getLongitude());

        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean stopService(Intent name) {


        return super.stopService(name);
    }
    // Clear Geofence
    public void clearGeofence() {
        Logger.d("clearGeofence()");

        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                getGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if ( status.isSuccess() ) {
                    // remove drawing
                   Logger.d("Removed");
                }
            }
        });
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Logger.d("checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }



}
