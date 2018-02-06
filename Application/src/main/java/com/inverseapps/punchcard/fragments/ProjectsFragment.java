package com.inverseapps.punchcard.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.ProjectsAdapter;
import com.inverseapps.punchcard.Constant.AppConstant;

import com.inverseapps.punchcard.model.CheckStatus;

import com.inverseapps.punchcard.model.OnSite;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.service.retrofit.PCRetrofitService;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegate;
import com.inverseapps.punchcard.ui.PCActivity;
import com.inverseapps.punchcard.ui.ProjectDetailsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

import java.util.List;
import java.util.Vector;


import butterknife.BindView;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;

public class ProjectsFragment extends PCFragment implements SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_projects;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.list)
    ListView listView;


    private static final int REQUEST_PERMISSIONS_LOCATION = 0;

    @OnItemClick(R.id.list)
    public void onClickedItemList(int position) {
        Project project = listOfProjects.get(position);
/*
        Gson gson = new Gson();
        String json = gson.toJson(project); // myObject - instance of MyObject
        pcActivity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_PROJECT, json).apply();*/


        if ("inactive".equals(project.getStatus())) {
            Toast.makeText(getContext(), "This project is now inactive!", Toast.LENGTH_SHORT).show();
        } else {
            FindCheckStatusDelegate delegate = (FindCheckStatusDelegate) findCheckStatusDelegate;
            delegate.setProject(project);
            pcActivity.getPCApplication().getPcFunctionService().findCheckStatus(delegate);
          pcActivity.getPCApplication().getPcFunctionService().findProject(project.getUniq_id(), findProjectDelegate);

        }
    }

    private List<Project> listOfProjects = new Vector<>();
    private ProjectsAdapter adapter;

    private ForegroundTaskDelegate findProjectsDelegate;
    private ForegroundTaskDelegate findCheckStatusDelegate;
    private FindOnSitesDelegate findOnSitesDelegate;
    private FindProjectDelegate findProjectDelegate;
    private User user;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Project project;

    public static final String KEY_CHECKED_IN_PROJECT_UNIQ_ID = "checkedInProjectUniqId";
    public static final String KEY_CHECKED_IN_PROJECT_NAME = "checkedInProjectName";
    public static final String KEY_SCANNED_USER_UNIQUE_ID = "checkedinuseruniqueid";


    private AsyncHttpClient tempClient;

    public static ProjectsFragment newInstance(int position) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
        listOfProjects = new Vector<>();
        adapter = new ProjectsAdapter(getContext(), listOfProjects, user);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);

        findProjectsDelegate = new FindProjectsDelegate(this);
        listOfForegroundTaskDelegates.add(findProjectsDelegate);

        findCheckStatusDelegate = new FindCheckStatusDelegate(this);
        listOfForegroundTaskDelegates.add(findCheckStatusDelegate);

        findOnSitesDelegate = new FindOnSitesDelegate(this);
        listOfForegroundTaskDelegates.add(findOnSitesDelegate);

        findOnSitesDelegate = new FindOnSitesDelegate(this);
        listOfForegroundTaskDelegates.add(findOnSitesDelegate);
        findProjectDelegate = new FindProjectDelegate(this);
        listOfForegroundTaskDelegates.add(findProjectDelegate);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        tempClient = new AsyncHttpClient();
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcActivity.getPCApplication().getPcFunctionService().findProjects(findProjectsDelegate);
    }


    private void checkLocationPermission() {
        String msg = "You have to turn on location permission to use this feature!";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(getActivity(),
                            msg,
                            Toast.LENGTH_LONG).show();
                }  else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_LOCATION);

                    // REQUEST_PERMISSIONS_LOCATION_OPEN_CAMERA is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

                requestLastKnownLocation();
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        msg,
                        Toast.LENGTH_LONG).show();
            } else {

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

                    requestLastKnownLocation();
                } else {
                    // permission denied
                    Toast.makeText(getActivity(), "You have to turn on location permission to use this feature!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void requestLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return;
        }

        if (googleApiClient != null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
    }

    private void checkPermission(final Project project, final CheckStatus checkStatus) {
        tempClient.setBasicAuth(pcActivity.getPCApplication().getPcFunctionService().getUserName(),
                pcActivity.getPCApplication().getPcFunctionService().getPassword());
        tempClient.addHeader("Content-type", "application/json");
        tempClient.addHeader("Accept", "application/json");
        String token = pcActivity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_TOKEN, null);
        tempClient.addHeader("Authorization", "Bearer " + token);

        String domain = pcActivity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }
        String companyHandle = pcActivity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");
        String baseUrl = String.format(PCRetrofitService.API_END_POINT_FORMAT, companyHandle, domain) + "user/";

        tempClient.get(pcActivity, baseUrl, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();

                if (pcActivity != null && !pcActivity.isFinishing() && !pcActivity.isDestroyed()) {
                    pcActivity.showProgressDialog();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                ArrayList<Integer> projectIds = new ArrayList<>();
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    if (dataObject != null) {
                        JSONObject permissionsObject = dataObject.getJSONObject("permissions");
                        JSONArray scannerArray = permissionsObject.getJSONArray("scanner");
                        for (int i = 0; i < scannerArray.length(); i++) {
                            int projectId = scannerArray.getInt(i);
                            projectIds.add(projectId);
                        }
                    }
                } catch (JSONException e) {
                    Logger.e(e.getMessage());
                } finally {
                    user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
                    if (user != null && !user.getRole().equals("user")) {
                        findOnSitesDelegate.setProject(project);
                        findOnSitesDelegate.setCheckStatus(checkStatus);
                        findOnSitesDelegate.setProjectIds(projectIds);
                        pcActivity.getPCApplication().getPcFunctionService().findOnSites(project.getUniq_id(), findOnSitesDelegate);
                    } else {
                        gotoProjectDetailsScreen(project, checkStatus, projectIds, true);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (pcActivity != null && !pcActivity.isFinishing() && !pcActivity.isDestroyed()) {
                    pcActivity.dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {

        tempClient.cancelRequests(pcActivity, true);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
        pcActivity.getPCApplication().getPcFunctionService().findProjects(findProjectsDelegate);
    }

    private void gotoProjectDetailsScreen(Project project,
                                          CheckStatus checkStatus,
                                          ArrayList<Integer> projectIds,
                                          boolean userInProject) {
        Intent intent = new Intent(getContext(), ProjectDetailsActivity.class);

        intent.putExtra(ProjectDetailsActivity.KEY_PROJECT, project);

        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_POLYFENCE, project.getPolyfence());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_COUNTRY, project.getCountry());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_CITY, project.getCity());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_STATE,project.getState());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_ADDRESS, project.getAddress());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_ZIP, project.getZip());
        intent.putExtra(ProjectDetailsActivity.KEY_USER_PROJECT_POLYFENCE, project.getPolyfence());
        if (checkStatus != null && checkStatus.getProject() != null) {
            intent.putExtra(ProjectDetailsActivity.KEY_CHECKED_IN_PROJECT_UNIQ_ID, checkStatus.getProject().getUniq_id());
            intent.putExtra(ProjectDetailsActivity.KEY_CHECKED_IN_PROJECT_NAME, checkStatus.getProject().getName());
        }

        intent.putIntegerArrayListExtra(ProjectDetailsActivity.KEY_SCANNED_PROJECT_IDS, projectIds);
        intent.putExtra(ProjectDetailsActivity.KEY_USER_IN_PROJECT, userInProject);
        getContext().startActivity(intent);
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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//.setInterval(15 * 60000)
        LocationRequest REQUEST = LocationRequest.create()
               .setFastestInterval(1 * 6000)   // in milliseconds
                .setInterval(1 * 6000)         // in milliseconds
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    @Override
    public void onLocationChanged(Location location) {

    }

    private static class FindProjectsDelegate extends ForegroundTaskDelegate<List<Project>> {

        private final WeakReference<ProjectsFragment> fragmentWeakReference;

        private FindProjectsDelegate(PCActivity activity) {
            super(activity);
            fragmentWeakReference = new WeakReference<>(null);
        }

        public FindProjectsDelegate(ProjectsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<Project> projects, Throwable throwable) {
            super.onPostExecute(projects, throwable);
            ProjectsFragment fragment = fragmentWeakReference.get();
            if (throwable == null && fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.listOfProjects.clear();


                if (fragment.lastLocation == null) {
                    Toast.makeText(fragment.getContext(),
                            "Please wait while getting your current location or you have to turn on location to use this feature!",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Location locationA = new Location("point A");

                    locationA.setLatitude(fragment.lastLocation.getLatitude());
                    locationA.setLongitude(fragment.lastLocation.getLongitude());


                    for (int i = 0; i < projects.size(); i++) {
                        Location locationC = new Location("point C");

                        locationC.setLatitude(projects.get(i).getLatitude());
                        locationC.setLongitude(projects.get(i).getLongitude());

                        float distance1 = locationA.distanceTo(locationC) / 1000;



                        projects.get(i).setSmallest_distance(distance1);

                    }
                }
                fragment.listOfProjects.addAll(projects);

                Collections.sort(fragment.listOfProjects, new Comparator<Project>() {
                    @Override
                    public int compare(Project lhs, Project rhs) {
                        return Double.compare(lhs.getSmallest_distance(), rhs.getSmallest_distance());
                    }

                });
                fragment.adapter.notifyDataSetChanged();
                fragment.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private static class FindCheckStatusDelegate extends ForegroundTaskDelegate<CheckStatus> {

        private final WeakReference<ProjectsFragment> fragmentWeakReference;
        private Project project;

        public void setProject(Project project) {
            this.cancel();
            this.project = project;
        }

        private FindCheckStatusDelegate(PCActivity activity) {
            super(activity);
            fragmentWeakReference = new WeakReference<>(null);
        }

        public FindCheckStatusDelegate(ProjectsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(CheckStatus checkStatus, Throwable throwable) {
            super.onPostExecute(checkStatus, throwable);
            ProjectsFragment fragment = fragmentWeakReference.get();
            if (project != null &&
                    throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.checkPermission(project, checkStatus);
            }
        }
    }

    private static class FindOnSitesDelegate extends ForegroundTaskDelegate<List<OnSite>> {

        private final WeakReference<ProjectsFragment> fragmentWeakReference;
        private Project project;
        private CheckStatus checkStatus;
        private ArrayList<Integer> projectIds;

        public void setProject(Project project) {
            this.project = project;
        }

        public void setCheckStatus(CheckStatus checkStatus) {
            this.checkStatus = checkStatus;
        }

        public void setProjectIds(ArrayList<Integer> projectIds) {
            this.projectIds = projectIds;
        }

        public FindOnSitesDelegate(ProjectsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(List<OnSite> onSites, Throwable throwable) {
            super.onPostExecute(onSites, throwable);

            ProjectsFragment fragment = fragmentWeakReference.get();
            if (throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                User user = fragment.pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
                boolean found = false;
                for (OnSite emmployee : onSites) {
                    if (emmployee.getUniq_id().equals(user.getUniq_id())) {
                        found = true;
                        break;
                    }
                }
                fragment.gotoProjectDetailsScreen(project, checkStatus, projectIds, found);
            }
        }
    }

    private static class FindProjectDelegate extends ForegroundTaskDelegate<Project> {

        private final WeakReference<ProjectsFragment> fragmentWeakReference;
        private Project project;

        public void setProject(Project project) {
            this.cancel();
            this.project = project;
        }

        private FindProjectDelegate(PCActivity activity) {
            super(activity);
            fragmentWeakReference = new WeakReference<>(null);
        }

        public FindProjectDelegate(ProjectsFragment fragment) {
            super(fragment.pcActivity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPostExecute(Project project, Throwable throwable) {
            super.onPostExecute(project, throwable);
            ProjectsFragment fragment = fragmentWeakReference.get();
            if (project != null &&
                    throwable == null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.project = project;

            }
        }
    }

}
