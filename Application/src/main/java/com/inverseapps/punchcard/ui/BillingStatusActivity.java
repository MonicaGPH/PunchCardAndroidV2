package com.inverseapps.punchcard.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;

import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.dialog.ViewDialog;
import com.orhanobut.logger.Logger;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by asus on 05-Dec-17.
 */

public class BillingStatusActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_billing;
    }

    @NonNull
    @BindView(R.id.PastBillingStatusFolder)
    RelativeLayout PastBillingStatusFolder;
    @NonNull
    @BindView(R.id.currentBillingHolder)
    RelativeLayout currentBillingHolder;

    @BindView(R.id.EditCCFolder)
    RelativeLayout editCCdetails;

    @OnClick(R.id.EditCCFolder)
    void onCLickedEditMyCCdetails() {
        gotoEditCCDetails();
    }

    private String current_plan, price;

    private void gotoEditCCDetails() {

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


        String currentDate = getCurrentDate();
        String endDate = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ////////////////// version :trial
        if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

            ////////////////card is decline or expire
            if(user.getClient().getCreditCardDeactivateDate()!=null && !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW, true);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, true);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW, false);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, true);
                startActivity(intent);
            }
        }
        ////////////////// version : base/standard/enterprice/premium
        else {

            ////////////////card is decline or expire
            if(user.getClient().getCreditCardDeactivateDate()!=null && !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW,true);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(PaymentActivity.KEY_PRICE, price);
                intent.putExtra(PaymentActivity.KEY_VERSION, current_plan);
                intent.putExtra(PaymentActivity.KEY_ISCARDRENEW,false);
                intent.putExtra(PaymentActivity.KEY_CHECKCALLFROMACTIVITY, false);
                startActivity(intent);
            }

        }
    }


    private User user;


    @OnClick(R.id.currentBillingHolder)
    public void onCLickedPresebtBillingHolder() {
        presentBilling();
    }

    private void presentBilling() {
        Intent intent = new Intent(this, PresentBillingActivity.class);
        this.startActivity(intent);
    }

    @OnClick(R.id.PastBillingStatusFolder)
    public void onClickedPastBillingStatusFolder() {
        pastBilling();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Billing");
        user = pcFunctionService.getInternalStoredUser();
        if(user.getRole().equalsIgnoreCase("clientadmin")) {
            if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {
                editCCdetails.setVisibility(View.GONE);
            } else {
                editCCdetails.setVisibility(View.VISIBLE);
            }
        }
        else {
            editCCdetails.setVisibility(View.GONE);
        }
    }


    private void pastBilling() {
        Intent intent = new Intent(this, PastBillingActivity.class);
        this.startActivity(intent);
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


    public void openDialog(Context context, String message) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here


                dialog.dismiss();


            }
        });


        // Showing Alert Message
        alertDialog.show();
    }


}
