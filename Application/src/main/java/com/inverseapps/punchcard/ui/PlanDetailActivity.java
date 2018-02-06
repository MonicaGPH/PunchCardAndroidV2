package com.inverseapps.punchcard.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.SubscribeORUnSubscribeParam;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by asus on 08-Dec-17.
 */

public class PlanDetailActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_plan_detail;
    }

    @BindView(R.id.currentplan)
    RelativeLayout currentplan;

    @BindView(R.id.txtCurrentPlan)
    TextView txtCurrentPlan;

    @BindView(R.id.upgradePlan)
    RelativeLayout upgradePlan;

    @BindView(R.id.unsubscribe)
    RelativeLayout unsubscribe;
    @NonNull
    @BindView(R.id.subscribe)
    RelativeLayout subscribe;

    @OnClick(R.id.subscribe)
        public void onClickedSubscribe() {
            subscribe();
        }

    private void subscribe() {

        Intent intent = new Intent(this, ChooseOptionActivity.class);
        this.startActivity(intent);
    }


    @OnClick(R.id.upgradePlan)
    public void onClickedUpgradePlan() {
        upgradePlan();
    }

    @OnClick(R.id.unsubscribe)
    public void onClickedunsubscribe() {
        unsubscribe();
    }

    private String current_plan, price;
    private ForegroundTaskDelegate unsubscribedelgate, logOutDelegate;
    private SharedPreferences preferences_saveButton, preferences_geopunchdata, preferences_checkdeindata;
    private User user;

    private void unsubscribe() {
        setStausUnsubscribeORSubscribe();
    }

    private void upgradePlan() {
        Intent intent = new Intent(this, UpgradePlanActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Plan Details");
        unsubscribedelgate = new UnsubscribeDelegate(this);
        listOfForegroundTaskDelegates.add(unsubscribedelgate);
        logOutDelegate = new LogoutDelegate(this);
        listOfForegroundTaskDelegates.add(logOutDelegate);

        user= pcFunctionService.getInternalStoredUser();
        if(user.getRole().equalsIgnoreCase("clientadmin")){


            if(user.getClient().getPlan_test().equalsIgnoreCase("trial")){
                upgradePlan.setVisibility(View.GONE);
                unsubscribe.setVisibility(View.GONE);
                subscribe.setVisibility(View.VISIBLE);
            }
            else {
                upgradePlan.setVisibility(View.VISIBLE);
                unsubscribe.setVisibility(View.VISIBLE);
                subscribe.setVisibility(View.GONE);
            }
        }
        else {
            upgradePlan.setVisibility(View.GONE);
            unsubscribe.setVisibility(View.GONE);
            subscribe.setVisibility(View.GONE);
        }

        txtCurrentPlan.setText(user.getClient().getPlan_test().toUpperCase());
    }

    public void callSubscribeORUnsubscribe(SubscribeORUnSubscribeParam param) {
        pcFunctionService.sendUnSubscribtionMsg(param, unsubscribedelgate);
    }
    private void setStausUnsubscribeORSubscribe() {
        boolean subscribe_status =getPCApplication().getAppPreferences().getBoolean(AppConstant.APP_PREF_SUBSCRIBE_STATUS, false);
        Logger.d(subscribe_status);
        //    if (!subscribe_status) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage("Are you sure you want to unsubscribe?");


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getPCApplication().getAppPreferences().edit().putBoolean(AppConstant.APP_PREF_SUBSCRIBE_STATUS, true).apply();
                SubscribeORUnSubscribeParam param = new SubscribeORUnSubscribeParam(user.getClient_id(), user.getId());
                callSubscribeORUnsubscribe(param);


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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class UnsubscribeDelegate extends ForegroundTaskDelegate<Boolean> {
        public UnsubscribeDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        protected void handleServiceError(ServiceException ex) {
            if (ex.getErrorCode() == 401) {
                showAlertDialog("Your credentials were not correct. Please try again!");
            }
            if(ex.getErrorCode() ==200){
                showAlertDialog("Oops!  Something went wrong! Your Credit Card information needs to be updated for Punchcard. Please update your billing information at your earliest convenience here.");
            }
        }
        @Override
        public void onPostExecute(Boolean aBoolean, Throwable throwable) {
            super.onPostExecute(aBoolean, throwable);
            PlanDetailActivity activity = (PlanDetailActivity) activityWeakReference.get();
            if (throwable == null && aBoolean && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                Toast.makeText(activity, "You have successfully unsubscribed", Toast.LENGTH_SHORT).show();
                logout();
            }

        }


    }

    private class LogoutDelegate extends ForegroundTaskDelegate<Boolean> {

        public LogoutDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        protected void handleServiceError(ServiceException ex) {
            if (ex.getErrorCode() == 401) {
                showAlertDialog("Your credentials were not correct. Please try again!");
            }
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);
            PlanDetailActivity activity = (PlanDetailActivity) activityWeakReference.get();
            if (throwable == null && result && activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                activity.gotoLoginScreen();
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
}
