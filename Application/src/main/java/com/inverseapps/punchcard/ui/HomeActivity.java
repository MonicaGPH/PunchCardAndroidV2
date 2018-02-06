package com.inverseapps.punchcard.ui;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.DrawerAdapter;
import com.inverseapps.punchcard.fragments.AboutAppFragment;
import com.inverseapps.punchcard.fragments.MyAccountFragment;
import com.inverseapps.punchcard.fragments.MyPunchesFragment;
import com.inverseapps.punchcard.fragments.ProjectsFragment;

import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.ui.dialog.ViewDialog;
import com.inverseapps.punchcard.ui.dialog.ViewDialog_CA;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class HomeActivity extends PCActivity implements DrawerAdapter.OnItemClickListener {
    private SharedPreferences preferences_saveButton, preferences_geopunchdata, preferences_checkdeindata;
    private User user;
    private boolean remindmelater;
    public static final String KEY_REMIND_ME_LATER_CLICKED = "remindmelater";
    private String[] drawerOptions = new String[10];


    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_home;
    }

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.left_drawer)
    RecyclerView leftDrawer;

    private ActionBarDrawerToggle drawerToggle;

    private ForegroundTaskDelegate logOutDelegate;
    public FindUserDelegate findUserDelegate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logOutDelegate = new LogoutDelegate(this);
        listOfForegroundTaskDelegates.add(logOutDelegate);

        findUserDelegate = new FindUserDelegate(this, savedInstanceState);
        listOfForegroundTaskDelegates.add(findUserDelegate);


        if(getIntent() !=null){
            remindmelater= getIntent().getBooleanExtra(KEY_REMIND_ME_LATER_CLICKED,false);
        }




        findUser();



        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_icon);


    }

    private void findUser() {
        pcFunctionService.findUser(findUserDelegate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position) {


        selectItemTrial(position);
    }


    private void selectItemTrial(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ProjectsFragment();
                break;
            case 1:
                fragment = new MyAccountFragment();
                break;
            case 2:
                fragment = new MyPunchesFragment();
                break;
            case 3:
                fragment = new AboutAppFragment();
                break;
            case 4:
               logout();

                break;
            default:
                break;
        }

        if (fragment != null) {

            String[] drawerOptions;

            drawerOptions = getResources().getStringArray(R.array.drawer_options_array_trial);


            getSupportActionBar().setTitle(drawerOptions[position]);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        drawerLayout.closeDrawer(leftDrawer);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void setStatus(Bundle savedInstanceState, String[] drawerOptions) {

        ///////////////// role :: clientadmin
        if (user.getRole().equalsIgnoreCase("clientadmin")) {
            String currentDate = getCurrentDate();
            String endDate = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
            Logger.d(endDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {

                /////// version :: trial
                if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

                    if (format.parse(currentDate).after(format.parse(endDate))) {
                        Logger.d("more than 30 days");
                        ViewDialog_CA alert = new ViewDialog_CA();
                        logout();
                        getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                    } else {
                        Date date1 = format.parse(currentDate);
                        Date date2 = format.parse(endDate);
                        long diff = date2.getTime() - date1.getTime();
                        Logger.i("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        Toast.makeText(HomeActivity.this, "You have " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days remaining in your free trial.", Toast.LENGTH_SHORT).show();
                        if (savedInstanceState == null)
                            selectItemTrial(0);
                    }


                }
                /////// version :: trialExpired
                else if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired")) {
                    ViewDialog_CA alert = new ViewDialog_CA();
                    Toast.makeText(this, "Trial expired", Toast.LENGTH_SHORT).show();
                    logout();
                    getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                }
                /////// version :: unsubscribe
                else if (user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                    ViewDialog_CA alert = new ViewDialog_CA();
                    Toast.makeText(this, "UnSubscribed", Toast.LENGTH_SHORT).show();
                    logout();
                    getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                } else {
                    /////////////////check card is decline or not !!
                    String dateofcarddecline = null;
                    if (user.getClient().getCreditCardDeactivateDate() != null & !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                        //////////////////////card is decline
                        String datebefore2days = getMonthSpecific(checkDatebefore2Days(user.getClient().getCreditCardDeactivateDate(), this));
                        try {
                            if (format.parse(currentDate).after(format.parse(user.getClient().getCreditCardDeactivateDate()))) {
                                // date is more than creditCardDeactivateDate
                                Logger.d("more than 2 days");
                                Toast.makeText(this, "Your Credit Card may Expired or Declined", Toast.LENGTH_SHORT).show();
                                getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                logout();
                                return;

                            } else {

                                Date date1 = format.parse(currentDate);
                                Date date2 = format.parse(user.getClient().getCreditCardDeactivateDate());
                                long diff = date2.getTime() - date1.getTime();
                                Logger.i("Days: " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));
                                if(!remindmelater) {
                                    ViewDialog_CA alert = new ViewDialog_CA();
                                    alert.showDialogForCardDeclineWarning(this, "Oops! It appears the CC was declined on " + datebefore2days + ".\n You have  " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) +
                                            " hours to update your payment information before your account is deactivated.\n\n" +
                                            "To update your payment info, click the link below.\n", user);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (savedInstanceState == null)
                        selectItemTrial(0);

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        ///////////////// role :: user/superuser/admin
        else {
            String currentDate = getCurrentDate();
            String endDate = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {

                ////////////////////version :: trial
                if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

                    if (format.parse(currentDate).after(format.parse(endDate))) {
                        Logger.d("more than 30 days");
                        ViewDialog alert = new ViewDialog();
                        logout();
                        getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                    } else {
                        Date date1 = format.parse(currentDate);
                        Date date2 = format.parse(endDate);
                        long diff = date2.getTime() - date1.getTime();
                        // Toast.makeText(HomeActivity.this, "You have " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days remaining in your free trial.", Toast.LENGTH_SHORT).show();

                        if (savedInstanceState == null)
                            selectItemTrial(0);

                    }
                }
                ////////////////////version :: trialExpired
                else if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired")) {
                    ViewDialog_CA alert = new ViewDialog_CA();
                    Toast.makeText(this, "Trial expired", Toast.LENGTH_SHORT).show();
                    logout();
                    getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                }
                ////////////////////version :: unsubscribe
                else if (user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                    ViewDialog_CA alert = new ViewDialog_CA();
                    Toast.makeText(this, "UnSubscribed", Toast.LENGTH_SHORT).show();
                    logout();
                    getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                } else {
                    /////////////////check card is decline or not !!
                    String dateofcarddecline = null;
                    if (user.getClient().getCreditCardDeactivateDate() != null & !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                        //////////////////////card is decline

                        try {
                            if (format.parse(currentDate).after(format.parse(user.getClient().getCreditCardDeactivateDate()))) {
                                // date is more than creditCardDeactivateDate
                                Logger.d("more than 2 days");
                                Toast.makeText(this, "Credit Card may Expired or Declined", Toast.LENGTH_SHORT).show();
                                getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                logout();
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    if (savedInstanceState == null)
                        selectItemTrial(0);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void logout() {
        preferences_saveButton = getApplicationContext().getSharedPreferences("saveButtonState", 0); // 0 - for private mode
        preferences_checkdeindata = getApplicationContext().getSharedPreferences("CheckedInData", 0);
        preferences_geopunchdata = getApplicationContext().getSharedPreferences("geopunchdata", 0);

        //////////////////////////////// preferences_saveButton
        SharedPreferences.Editor editor_saveButton = preferences_saveButton.edit();
        editor_saveButton.clear();
        editor_saveButton.apply();

        //////////////////////////////// preferences_checkdeindata
        SharedPreferences.Editor editor_checkdeindata = preferences_checkdeindata.edit();
        editor_checkdeindata.clear();
        editor_checkdeindata.apply();

        //////////////////////////////// preferences_geopunchdata
        SharedPreferences.Editor editor_geopunchdata = preferences_geopunchdata.edit();
        editor_geopunchdata.clear();
        editor_geopunchdata.apply();

        pcFunctionService.logout(logOutDelegate);
    }



    private void gotoLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private class LogoutDelegate extends ForegroundTaskDelegate<Boolean> {

        public LogoutDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean aBoolean, Throwable throwable) {
            super.onPostExecute(aBoolean, throwable);
            HomeActivity activity = (HomeActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.gotoLoginScreen();
            }
        }
    }

    private static class FindUserDelegate extends ForegroundTaskDelegate<User> {
        Bundle savedInstanceState;
        boolean subscribe_Status;
        String[] drawerOptions;

        public FindUserDelegate(HomeActivity activity, Bundle savedInstanceState) {
            super(activity);
            this.savedInstanceState = savedInstanceState;
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            HomeActivity activity = (HomeActivity) activityWeakReference.get();
            if (throwable == null
                    && user != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.user = user;

                if (user.getRole().equalsIgnoreCase("clientAdmin")) {


                    if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {
                        drawerOptions = activity.getResources().getStringArray(R.array.drawer_options_array_trial);
                        activity.leftDrawer.setHasFixedSize(true);
                        activity.leftDrawer.setAdapter(new DrawerAdapter(drawerOptions, activity));
                        activity.setStatus(savedInstanceState, drawerOptions);
                    } else if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired") || user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                        if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired")) {
                            activity.logout();
                            Toast.makeText(activity, "Trial expired", Toast.LENGTH_SHORT).show();
                        }
                        if (user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                            activity.logout();
                            Toast.makeText(activity, "UnSubscribed", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        subscribe_Status = activity.getPCApplication().getAppPreferences().getBoolean(AppConstant.APP_PREF_SUBSCRIBE_STATUS, false);
                   /*
                        drawerOptions = new String[]
                                {"Projects",
                                        "My Account",
                                        "My Punches",
                                        "About App",
                                        "Unsubscribe",
                                        "Upgrade your plan",
                                        "Sign Out"};*/
                        drawerOptions = activity.getResources().getStringArray(R.array.drawer_options_array_trial);

                        activity.leftDrawer.setHasFixedSize(true);
                        activity.leftDrawer.setAdapter(new DrawerAdapter(drawerOptions, activity));
                        activity.setStatus(savedInstanceState, drawerOptions);
                    }

                } else {
                    drawerOptions = activity.getResources().getStringArray(R.array.drawer_options_array_trial);
                    activity.leftDrawer.setHasFixedSize(true);
                    activity.leftDrawer.setAdapter(new DrawerAdapter(drawerOptions, activity));

                    activity.setStatus(savedInstanceState, drawerOptions);
                }

            }
        }
    }

    public void showAlert(String message, final Context context, String role) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);
        if (role.equalsIgnoreCase("clientadmin")) {
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Update\n" +
                    "Payment Info", new DialogInterface.OnClickListener() {
                String current_plan, price;

                public void onClick(DialogInterface dialog, int which) {
                    current_plan = user.getClient().getPlan_test().toLowerCase();
                    if (current_plan.equalsIgnoreCase("base"))
                        price = String.valueOf(1);
                    else if (current_plan.equalsIgnoreCase("standard"))
                        price = String.valueOf(2);
                    else if (current_plan.equalsIgnoreCase("premium"))
                        price = String.valueOf(3);
                    else if (current_plan.equalsIgnoreCase("enterprise"))
                        price = String.valueOf(4);
                    else if (current_plan.equalsIgnoreCase("trial"))
                        price = String.valueOf(0);
                    Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
                    intent.putExtra(PaymentActivity.KEY_PRICE, price);
                    intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                    intent.putExtra(PaymentActivity.KEY_ISCARDRENEW, true);
                    intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
                    startActivity(intent);

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Remind me later.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed No button. Write Logic Here

                    dialog.dismiss();
                }
            });

        } else {
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed No button. Write Logic Here

                    dialog.dismiss();
                }
            });
        }
        // Showing Alert Message
        alertDialog.show();
    }

    public static String getMonthSpecific(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        return monthName;
    }
}