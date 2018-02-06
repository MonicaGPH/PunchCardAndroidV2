package com.inverseapps.punchcard.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.fragments.ProjectDetailsFragment;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.GeoCheckInOutParam;
import com.inverseapps.punchcard.ui.ForegroundTaskDelegateService;
import com.inverseapps.punchcard.ui.GeofenceErrorMessages;
import com.inverseapps.punchcard.ui.ProjectDetailsActivity;
import com.inverseapps.punchcard.utils.PunchCardApplication;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class GeofenceTransitionsIntentService extends PCService {
    protected static final String TAG = "GeofenceTransitionsIS";
    public static final String KEY_PROJECT = "project";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PROJECT_UNIQUE_ID = "projectUniqueId";
    public static final String KEY_CHECKED_IN_PROJECT_UNIQ_ID = "checkedInProjectUniqId";
    public static final String KEY_CHECKED_IN_PROJECT_NAME = "checkedInProjectName";
    public static final String KEY_CHECKED_IN_PROJECT = "project";
    private Project project;
    private String projectUniqueID;
    private double latitude, longitude;
    private CheckOutDelegate checkOutDelegate;
    private CheckInDelegate checkInDelegate;
    private LatLng latLng;
    private String checkedInProjectName, checkedInProjectUniqueId;
    private String name;
    private User user;
    private int count = 1;
    private String status;
    protected List<ForegroundTaskDelegateService> listOfForegroundTaskDelegatesService;
    protected PCFunctionService pcFunctionService;

    public GeofenceTransitionsIntentService() {
        super("ABC");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        listOfForegroundTaskDelegatesService = new Vector<>();
        pcFunctionService = getPCApplication().getPcFunctionService();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        checkOutDelegate = new CheckOutDelegate(this, ProjectDetailsFragment.newInstance());
        listOfForegroundTaskDelegatesService.add(checkOutDelegate);

        checkInDelegate = new CheckInDelegate(this, ProjectDetailsFragment.newInstance());
        listOfForegroundTaskDelegatesService.add(checkInDelegate);


        user = pcFunctionService.getInternalStoredUser();
        project = pcFunctionService.getInternalStoredProject();


        SharedPreferences pref_geopunchdata = getApplicationContext().getSharedPreferences("geopunchdata", 0); // 0 - for private mode
        SharedPreferences pref_checkindata = getApplicationContext().getSharedPreferences("CheckedInData", 0); // 0 - for private mode

        projectUniqueID = project.getUniq_id();


        //projectUniqueID=pref_geopunchdata.getString(GeoPunchService.KEY_PROJECT_UNIQUE_ID,"uniues");
        latitude = Double.parseDouble(pref_geopunchdata.getString(GeoPunchService.KEY_LATITUDE, "0"));
        longitude = Double.parseDouble(pref_geopunchdata.getString(GeoPunchService.KEY_LONGITUDE, "0"));

        checkedInProjectName = pref_checkindata.getString(KEY_CHECKED_IN_PROJECT_NAME, "name");
        checkedInProjectUniqueId = pref_checkindata.getString(ProjectDetailsFragment.KEY_CHECKED_IN_PROJECT_UNIQ_ID, "uniqueID");


        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Logger.d(errorMessage);

        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        Logger.d(geofenceTransition);
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            getGeofenceTrasitionDetails(geofenceTransition, triggeringGeofences);


        } else {
            // Log the error.
            Logger.e(getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }

    }


    public final PunchCardApplication getPCApplication() {
        return (PunchCardApplication) getApplication();
    }


    // Create a detail message with Geofences received
    private void getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {


        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }


        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Logger.d("Enter triggers");
            if (!checkedInProjectUniqueId.equalsIgnoreCase(projectUniqueID)) {
                GeoCheckInOutParam param = new GeoCheckInOutParam(projectUniqueID,
                        latitude,
                        longitude, null, "geo");
                punchIn(param);

            }

        }
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Logger.d("Exit triggers");
            if (checkedInProjectUniqueId.equalsIgnoreCase(projectUniqueID)) {
                GeoCheckInOutParam param = new GeoCheckInOutParam(projectUniqueID,
                        latitude,
                        longitude, null, "geo");
                punchOut(param);

            }

        }


    }


    private void punchIn(GeoCheckInOutParam param) {
        Logger.d("punchIn");
        getPCApplication().getPcFunctionService().geoCheckIn(param, checkInDelegate);
    }

    private void punchOut(GeoCheckInOutParam param) {
        Logger.d("punchOut");
        getPCApplication().getPcFunctionService().geoCheckOut(param, checkOutDelegate);
    }

    @Override
    public void onCreate() {
        Logger.d("onCreate");
        super.onCreate();

    }

    private void sendNotification(String notificationDetails) {

        // Current System Time in dd/yyyy/MM
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        // Create an explicit content Intent that starts MainActivity.
        Intent notificationIntent = new Intent(getApplicationContext(), ProjectDetailsActivity.class);

        // Get a PendingIntent containing the entire back stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ProjectDetailsActivity.class).addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("At  " + dateFormat.format(cal.getTime()))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentIntent(notificationPendingIntent)
                .setSmallIcon(R.drawable.geo_check)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);


        // Fire and notify the built Notification.
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, builder.build());
        count++;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);

            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }


    private class CheckOutDelegate extends ForegroundTaskDelegateService<Boolean> {

        private final WeakReference<GeofenceTransitionsIntentService> serviceWeakReference;
        private final WeakReference<ProjectDetailsFragment> fragmentWeakReference;

        CheckOutDelegate(GeofenceTransitionsIntentService intentService, ProjectDetailsFragment fragment) {
            super(intentService, fragment);
            Logger.d("CheckOUTDelegate");
            serviceWeakReference = new WeakReference<>(intentService);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        protected void handleServiceError(ServiceException ex) {


            GeofenceTransitionsIntentService intentService = serviceWeakReference.get();
            if (intentService != null) {
                if (ex.getErrorCode() != 422) {
                    Logger.e("error.422 in CheckOutDelegate");
                } else {
                    Logger.e("error.CheckOutDelegate");
                }
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            GeofenceTransitionsIntentService intentService = serviceWeakReference.get();
            ProjectDetailsFragment fragment = fragmentWeakReference.get();
            if (throwable == null && result && intentService != null) {
                Toast.makeText(intentService.getBaseContext(), "You have punched out!", Toast.LENGTH_SHORT).show();
                status = "" + user.getFirst_name() + "  " + "Checked Out ";
                sendNotification(status);
                fragment.project = project;
                fragment.checkedInProjectUniqId = "";
                fragment.checkedInProjectName = "";
                sendUpdate(fragment.project, fragment.checkedInProjectUniqId);
                fragment.notifyCheckedInOut();


            }
        }
    }


    private class CheckInDelegate extends ForegroundTaskDelegateService<Boolean> {

        private final WeakReference<GeofenceTransitionsIntentService> serviceWeakReference;
        private final WeakReference<ProjectDetailsFragment> fragmentWeakReference;

        CheckInDelegate(GeofenceTransitionsIntentService intentService, ProjectDetailsFragment fragment) {
            super(intentService, fragment);
            Logger.d("CheckInDelegate");
            serviceWeakReference = new WeakReference<>(intentService);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        protected void handleServiceError(ServiceException ex) {


            GeofenceTransitionsIntentService intentService = serviceWeakReference.get();
            if (intentService != null) {
                if (ex.getErrorCode() != 422) {
                    Logger.e("error.422");
                } else {
                    Logger.e("error checkInDelegate");
                }
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            GeofenceTransitionsIntentService intentService = serviceWeakReference.get();
            ProjectDetailsFragment fragment = fragmentWeakReference.get();

            if (throwable == null && result && intentService != null) {
                Toast.makeText(intentService.getBaseContext(), "You have punched in!", Toast.LENGTH_SHORT).show();
                status = "" + user.getFirst_name() + "  " + "Checked In";
                sendNotification(status);
                fragment.checkedInProjectUniqId = project.getUniq_id();
                fragment.checkedInProjectName = project.getName();
                fragment.project = project;
                sendUpdate(fragment.project, fragment.checkedInProjectUniqId);
                fragment.notifyCheckedInOut();

            }
        }
    }


    public void sendUpdate(Project project, String checkedInProjectUniqueId) {

        //return result
        Intent intentResponse = new Intent();
        intentResponse.setAction("com.inverseapps.punchcard.UpdateFragment");
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(KEY_CHECKED_IN_PROJECT, project);
        intentResponse.putExtra(KEY_CHECKED_IN_PROJECT_UNIQ_ID, project.getUniq_id());
        sendBroadcast(intentResponse);
    }
}
