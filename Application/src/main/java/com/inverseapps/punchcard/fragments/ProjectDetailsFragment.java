package com.inverseapps.punchcard.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.param.GeoCheckInOutParam;
import com.inverseapps.punchcard.service.GeofenceTransitionsIntentService;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;
import com.inverseapps.punchcard.ui.PCActivity;
import com.inverseapps.punchcard.ui.ProjectDetailsActivity;
import com.inverseapps.punchcard.ui.dialog.PCDialogFragmentListener;
import com.inverseapps.punchcard.ui.dialog.PCExceptionReasonDialogFragment;
import com.inverseapps.punchcard.utils.Utilities;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjectDetailsFragment extends PCProjectDetailsFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        PCDialogFragmentListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_project_details;
    }

    private SupportMapFragment mapFragment;

    @NonNull
    @BindView(R.id.lblProjectDesc)
    TextView lblProjectDesc;

    @NonNull
    @BindView(R.id.btnCheckInOut)
    Button btnCheckInOut;

    @OnClick(R.id.btnCheckInOut)
    void onClickedProjectCheckInOutButton() {
        if (!userInProject) {
            pcActivity.showAlertDialog("User is not on this project!");
            return;
        }

        if (!Utilities.isLocationEnabled(getContext())) {
            Toast.makeText(getActivity(),
                    "You have to turn on location service to use this feature!",
                    Toast.LENGTH_SHORT).show();
        } else if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(),
                    "You have to turn on location permission to use this feature!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestLastKnownLocation();
            checkToPunchInOut();
        }
    }

    public static final String KEY_CHECKED_IN_PROJECT_UNIQ_ID = "checkedInProjectUniqId";
    public static final String KEY_CHECKED_IN_PROJECT_NAME = "checkedInProjectName";
    public static final String KEY_PROJECT_POLYFENCE = "polyfence";
    private static final float defaultZoomLevel = 13;

    private GoogleMap googleMap;
    private Circle circle;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private CheckOutDelegate checkOutDelegate;

    private CheckOutDelegateForAlreadyCheckedInProject checkOutDelegateForAlreadyCheckedInProject;
    private CheckInDelegate checkInDelegate;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocationSource.OnLocationChangedListener mMapLocationListener = null;
    private ArrayList<String> latitudeArrayList = new ArrayList<>();
    private ArrayList<String> longitudeArrayList = new ArrayList<>();
    private String polyfenceStrings;
    private static final int REQUEST_PERMISSIONS_LOCATION = 0;

    protected static final String TAG_DIALOG_EXCEPTION_REASON = "dialogExceptionReason";

    public static ProjectDetailsFragment newInstance(int page,
                                                     String title,
                                                     Project project,
                                                     String checkedInProjectUniqId,
                                                     String polyfenceString,
                                                     String checkedInProjectName,
                                                     boolean userInProject) {
        ProjectDetailsFragment fragment = new ProjectDetailsFragment();
        fragment.polyfenceStrings = polyfenceString;
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString(KEY_TITLE, title);
        args.putParcelable(KEY_PROJECT, project);
        args.putString(KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkedInProjectUniqId);
        args.putString(KEY_PROJECT_POLYFENCE, polyfenceString);
        args.putString(KEY_CHECKED_IN_PROJECT_NAME, checkedInProjectName);
        args.putBoolean(KEY_USER_IN_PROJECT, userInProject);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProjectDetailsFragment newInstance() {
        ProjectDetailsFragment fragment = new ProjectDetailsFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reload(project, checkedInProjectUniqId);

        checkOutDelegate = new CheckOutDelegate(this);
        listOfForegroundTaskDelegates.add(checkOutDelegate);

        checkOutDelegateForAlreadyCheckedInProject = new CheckOutDelegateForAlreadyCheckedInProject(this);
        listOfForegroundTaskDelegates.add(checkOutDelegateForAlreadyCheckedInProject);

        checkInDelegate = new CheckInDelegate(this);
        listOfForegroundTaskDelegates.add(checkInDelegate);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.inverseapps.punchcard.UpdateFragment");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkLocationPermission();


        lblProjectDesc.setText(project.getDescription());
        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            btnCheckInOut.setText("PUNCH OUT");
        } else {
            btnCheckInOut.setText("PUNCH IN");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void reload(Project project, String checkedInProjectUniqId) {
        super.reload(project, checkedInProjectUniqId);

        lblProjectDesc.setText(project.getDescription());
        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            btnCheckInOut.setText("PUNCH OUT");
        } else {
            btnCheckInOut.setText("PUNCH IN");
        }
    }

    private void requestEnableMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);

        }
    }

    private void requestLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (googleApiClient != null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;


        moveMapCameraToProject();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        requestEnableMyLocation();
    }

    private void moveMapCameraToProject() {
        if (project != null) {

            LatLng projectLatLng = new LatLng(project.getLatitude(), project.getLongitude());


            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(projectLatLng)
                    .title(project.getName()));
            marker.setTag(project.getUniq_id());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(projectLatLng)
                    .zoom(defaultZoomLevel)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            if (project.getRadius() != 0) {
                CircleOptions circleOptions = new CircleOptions()
                        .center(projectLatLng)   //set center
                        .radius(project.getRadius())   //set radius in meters
                        .fillColor(Color.GRAY)  //default
                        .strokeColor(Color.RED)
                        .strokeWidth(5);

                circle = googleMap.addCircle(circleOptions);
            } else {
                String polyfenceStringReplace = polyfenceString.replaceAll(Pattern.quote("("), "");
                String polyfenceStringReplace1 = polyfenceStringReplace.replaceAll(Pattern.quote(")"), "");
                String[] splitStr = polyfenceStringReplace1.split(",");


                for (int i = 0; i < splitStr.length; i = i + 2) {
                   latitudeArrayList.add(splitStr[i]);


                }
                for (int i = 1; i < splitStr.length; i = i + 2) {
                    longitudeArrayList.add(splitStr[i]);

                }

                PolygonOptions poly = new PolygonOptions();
                poly.fillColor(Color.GRAY);
                poly.strokeColor(Color.RED);

                for(int i = 0; i < longitudeArrayList.size(); i++)
                {
                    poly.add(new LatLng(Double.parseDouble(latitudeArrayList.get(i)), Double.parseDouble(longitudeArrayList.get(i))));
                }

                googleMap.addPolygon(poly);




            }
        }
    }

    private void checkToPunchInOut() {

        if (lastLocation == null) {
            Toast.makeText(getActivity(),
                    "Please wait while getting your current location or you have to turn on location to use this feature!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Location projectLocation = new Location("");
        projectLocation.setLatitude(project.getLatitude());
        projectLocation.setLongitude(project.getLongitude());
        double distance = projectLocation.distanceTo(lastLocation);

        GeoCheckInOutParam param = new GeoCheckInOutParam(project.getUniq_id(),
                lastLocation.getLatitude(),
                lastLocation.getLongitude(), null, "manual");

        if (project.getUniq_id().equals(checkedInProjectUniqId)) {
            if (distance >= project.getRadius()) {
                // showExceptionReasonDialog(false);
                punchOut(param);
            } else {
                punchOut(param);
            }
        } else if (TextUtils.isEmpty(checkedInProjectUniqId)) {
            if (distance >= project.getRadius()) {
                // showExceptionReasonDialog(true);
                punchIn(param);
            } else {
                punchIn(param);
            }
        } else {
            String msg = String.format(Locale.US, "You are already checked into the project [%s]. Please check out of the project before checking into this one!",
                    checkedInProjectName);
            showAlertBox(msg);

        }
    }

    private void punchIn(GeoCheckInOutParam param) {
        pcActivity.getPCApplication().getPcFunctionService().geoCheckIn(param, checkInDelegate);
    }

    private void punchOut(GeoCheckInOutParam param) {
        pcActivity.getPCApplication().getPcFunctionService().geoCheckOut(param, checkOutDelegate);
    }

    private void punchOutFromCheckkedInProject(GeoCheckInOutParam param) {
        pcActivity.getPCApplication().getPcFunctionService().geoCheckOut(param, checkOutDelegateForAlreadyCheckedInProject);
    }

    public void notifyCheckedInOut() {
        Intent intent = new Intent(ProjectDetailsActivity.EVENT_PUNCH_IN_OUT);
        intent.putExtra(ProjectDetailsActivity.KEY_CHECKED_IN_PROJECT_UNIQ_ID,
                checkedInProjectUniqId);
        intent.putExtra(ProjectDetailsActivity.KEY_CHECKED_IN_PROJECT_NAME,
                checkedInProjectName);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        LocationRequest REQUEST = LocationRequest.create()
                // .setFastestInterval(15 * 60000)   // in milliseconds
                .setInterval(15 * 60000)         // in milliseconds
                .setPriority(LocationRequest.PRIORITY_NO_POWER);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                REQUEST,
                this);  // LocationListener

        requestLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkLocationPermission() {
        String msg = "You have to turn on location permission to use this feature!";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(getActivity(),
                            msg,
                            Toast.LENGTH_LONG).show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(getActivity(),
                            msg,
                            Toast.LENGTH_LONG).show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_PERMISSIONS_LOCATION);

                    // REQUEST_PERMISSIONS_LOCATION_OPEN_CAMERA is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                requestEnableMyLocation();
                requestLastKnownLocation();
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        msg,
                        Toast.LENGTH_LONG).show();
            } else {
                requestEnableMyLocation();
                requestLastKnownLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    requestEnableMyLocation();
                    requestLastKnownLocation();
                } else {
                    // permission denied
                    Toast.makeText(getActivity(), "You have to turn on location permission to use this feature!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void showExceptionReasonDialog(boolean forCheckIn) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prevFrag = getChildFragmentManager().findFragmentByTag(TAG_DIALOG_EXCEPTION_REASON);
        if (prevFrag != null) {
            ft.remove(prevFrag);
        }
        ft.addToBackStack(null);
        PCExceptionReasonDialogFragment newFrag = PCExceptionReasonDialogFragment.newInstance("", forCheckIn);
        newFrag.setListener(this);
        newFrag.show(ft, TAG_DIALOG_EXCEPTION_REASON);
    }

    private void showNoReasonDialog(String message, final boolean forCheckIn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Punch Card");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showExceptionReasonDialog(forCheckIn);
            }
        });
        builder.create().show();
    }

    @Override
    public void onDialogPositiveClick(AppCompatDialogFragment dialog) {
        if (dialog.getTag().equals(TAG_DIALOG_EXCEPTION_REASON)) {

            String exceptionReason = dialog.getArguments()
                    .getString(PCExceptionReasonDialogFragment.KEY_EXCEPTION_REASON);

            if (TextUtils.isEmpty(exceptionReason)) {
                Toast.makeText(getActivity(), "Please provide a reason!", Toast.LENGTH_SHORT).show();
            } else {
                boolean forCheckIn = dialog.getArguments()
                        .getBoolean(PCExceptionReasonDialogFragment.KEY_FOR_CHECK_IN);

                GeoCheckInOutParam param = new GeoCheckInOutParam(project.getUniq_id(),
                        lastLocation.getLatitude(),
                        lastLocation.getLongitude(), exceptionReason, "manual");

                if (forCheckIn) {
                    punchIn(param);
                } else {
                    punchOut(param);
                }
            }
        }
    }

    @Override
    public void onDialogNegativeClick(AppCompatDialogFragment dialog) {

    }

    @Override
    public void onDialogNeutralClick(AppCompatDialogFragment dialog) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMapLocationListener != null) {
            mMapLocationListener.onLocationChanged(location);
        }

    }


    private static class CheckInDelegate extends ForegroundTaskDelegate<Boolean> {

        private final WeakReference<ProjectDetailsFragment> fragmentWeakReference;

        public CheckInDelegate(ProjectDetailsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        protected void handleServiceError(ServiceException ex) {

            PCActivity activity = activityWeakReference.get();
            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                if (ex.getErrorCode() != 422) {
                    activity.showAlertDialog(ex.getMessage());
                } else {
                    fragment.showNoReasonDialog(ex.getMessage(), true);
                }
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    result &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                Toast.makeText(fragment.getContext(), "You have punched in!", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = fragment.getActivity().getSharedPreferences("CheckedInData", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                fragment.checkedInProjectUniqId = fragment.project.getUniq_id();
                fragment.checkedInProjectName = fragment.project.getName();

                editor.putString(GeofenceTransitionsIntentService.KEY_CHECKED_IN_PROJECT_UNIQ_ID, fragment.checkedInProjectUniqId);
                editor.putString(GeofenceTransitionsIntentService.KEY_CHECKED_IN_PROJECT_NAME, fragment.checkedInProjectName);
                editor.apply();
                fragment.reload(fragment.project, fragment.checkedInProjectUniqId);
                fragment.notifyCheckedInOut();
            }
        }
    }

    private static class CheckOutDelegate extends ForegroundTaskDelegate<Boolean> {

        private final WeakReference<ProjectDetailsFragment> fragmentWeakReference;

        public CheckOutDelegate(ProjectDetailsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        protected void handleServiceError(ServiceException ex) {

            PCActivity activity = activityWeakReference.get();
            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                if (ex.getErrorCode() != 422) {
                    activity.showAlertDialog(ex.getMessage());
                } else {
                    fragment.showNoReasonDialog(ex.getMessage(), false);
                }
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    result &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                Toast.makeText(fragment.getContext(), "You have punched out!", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = fragment.getActivity().getSharedPreferences("CheckedInData", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                fragment.checkedInProjectUniqId = "";
                fragment.checkedInProjectName = "";
                editor.clear();
                editor.apply();
                fragment.reload(fragment.project, fragment.checkedInProjectUniqId);
                fragment.notifyCheckedInOut();
            }
        }
    }

///////////////////////////////////// CheckOut Delegate for already checkedIn project.

    private static class CheckOutDelegateForAlreadyCheckedInProject extends ForegroundTaskDelegate<Boolean> {

        private final WeakReference<ProjectDetailsFragment> fragmentWeakReference;

        public CheckOutDelegateForAlreadyCheckedInProject(ProjectDetailsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        protected void handleServiceError(ServiceException ex) {

            PCActivity activity = activityWeakReference.get();
            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                if (ex.getErrorCode() != 422) {
                    activity.showAlertDialog(ex.getMessage());
                } else {
                    fragment.showNoReasonDialog(ex.getMessage(), false);
                }
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    result &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                Toast.makeText(fragment.getContext(), "You have punched out! from" + fragment.checkedInProjectName + ".", Toast.LENGTH_SHORT).show();

                fragment.checkedInProjectUniqId = "";
                fragment.checkedInProjectName = "";
                fragment.reload(fragment.project, fragment.checkedInProjectUniqId);
                fragment.notifyCheckedInOut();

                GeoCheckInOutParam param = new GeoCheckInOutParam(fragment.project.getUniq_id(),
                        fragment.lastLocation.getLatitude(),
                        fragment.lastLocation.getLongitude(), null, "manual");
                fragment.punchIn(param);

            }
        }
    }

    public void showAlertBox(String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here

                GeoCheckInOutParam param = new GeoCheckInOutParam(checkedInProjectUniqId,
                        lastLocation.getLatitude(),
                        lastLocation.getLongitude(), null, "manual");
                punchOutFromCheckkedInProject(param);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed No button. Write Logic Here

                dialog.dismiss();
            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Project result_project = intent.getParcelableExtra(GeofenceTransitionsIntentService.KEY_CHECKED_IN_PROJECT);
            String result_unique_id = intent.getStringExtra(GeofenceTransitionsIntentService.KEY_CHECKED_IN_PROJECT_UNIQ_ID);
            Logger.d(result_unique_id);
            Logger.d(result_project.getLatitude());
            reload(result_project, result_unique_id);
        }
    }
}
