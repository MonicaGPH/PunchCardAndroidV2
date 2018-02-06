package com.inverseapps.punchcard.ui;

import android.Manifest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;

import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.service.GeoPunchService;
import com.orhanobut.logger.Logger;



import butterknife.BindView;
import butterknife.OnClick;

public class VirtualGateActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_virtual_gate;
    }

    @NonNull
    @BindView(R.id.btnQRScanner)
    Button btnQRScanner;

    @NonNull
    @BindView(R.id.btnBadgeEntry)
    Button btnBadgeEntry;
    @NonNull
    @BindView(R.id.btnFaceRecognition)
    Button btnFaceRecognition;
    @NonNull
    @BindView(R.id.btnAutoGeopunch)
    Button btnAutoGeopunch;

    @OnClick(R.id.btnQRScanner)
    void onClickedQRScanner() {

        gotoQRScanner();

    }

    @OnClick(R.id.btnBadgeEntry)
    void onClickedBadgeEntry() {

        gotoBadgeEntryScreen();

    }

    @OnClick(R.id.btnFaceRecognition)
    void onClickedFaceRecognition() {
        gotoFaceRcognition();



    }


    @OnClick(R.id.btnAutoGeopunch)
    void onClickedAutoGeoPunch() {
        gotoAutoGeoPunch();


    }


    public static final String KEY_PROJECT = "project";
    private Project project;
    private static final String KEY_AUTOPUNCH_BUTTON_TEXT = "btn_text";
    private static final String PREF_AUTOPUNCH_BUTTON_TEXT = "btn_text";
    String autoPunch_btn_text = "";
    String autoPunch_btn_State = "";
    String scanner_access;
    static boolean checkAutoPunchStatus;
    private SharedPreferences preferences;
    private User user;
    public static String custom_action = "CALL_CLEAR_GEOFENCE";
    private static final int REQUEST_PERMISSIONS_LOCATION = 0;
    public GeoPunchService geoPunchService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Virtual Gate");
        preferences = getApplicationContext().getSharedPreferences("saveButtonState", 0); // 0 - for private mode
        autoPunch_btn_State = preferences.getString(PREF_AUTOPUNCH_BUTTON_TEXT, "null");
        scanner_access = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_SCANNER_ACCESS, null);
        project = getIntent().getParcelableExtra(KEY_PROJECT);
        user = pcFunctionService.getInternalStoredUser();
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            btnAutoGeopunch.setText(savedInstanceState.getString(KEY_AUTOPUNCH_BUTTON_TEXT));
        }
        if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {
            btnAutoGeopunch.setEnabled(true);
            btnFaceRecognition.setEnabled(true);
            btnBadgeEntry.setEnabled(true);
            btnQRScanner.setEnabled(true);
        } else if (user.getClient().getPlan_test().equalsIgnoreCase("base")) {
            btnAutoGeopunch.setEnabled(false);
            btnFaceRecognition.setEnabled(false);
            btnBadgeEntry.setEnabled(false);
            btnQRScanner.setEnabled(false);
        } else if (user.getClient().getPlan_test().equalsIgnoreCase("standard")) {
            btnAutoGeopunch.setEnabled(false);
            btnFaceRecognition.setEnabled(false);
            btnBadgeEntry.setEnabled(true);
            btnQRScanner.setEnabled(true);
        } else if (user.getClient().getPlan_test().equalsIgnoreCase("premium")) {
            btnAutoGeopunch.setEnabled(true);
            btnFaceRecognition.setEnabled(true);
            btnBadgeEntry.setEnabled(true);
            btnQRScanner.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putString(KEY_AUTOPUNCH_BUTTON_TEXT, autoPunch_btn_text);
        //  super.onSaveInstanceState(outState);
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


    @Override
    public void onResume() {
        super.onResume();


        preferences = getApplicationContext().getSharedPreferences("saveButtonState", 0); // 0 - for private mode
        autoPunch_btn_State = preferences.getString(PREF_AUTOPUNCH_BUTTON_TEXT, "null");

        if (!autoPunch_btn_State.equals("null")) {


            if (autoPunch_btn_State.equalsIgnoreCase("Enable")) {
                btnAutoGeopunch.setText("Disable AutoPunch");
                autoPunch_btn_text = "Disable AutoPunch";

            } else {
                btnAutoGeopunch.setText("Enable AutoPunch");
                autoPunch_btn_text = "Enable AutoPunch";
            }
        } else {
            btnAutoGeopunch.setText("Enable AutoPunch");
            checkAutoPunchStatus = true;
        }
    }

    private void gotoQRScanner() {

        Intent intent = new Intent(this, QRScannerActivity.class);
        intent.putExtra(VirtualGateActivity.KEY_PROJECT, project);
        startActivity(intent);

    }

    private void gotoBadgeEntryScreen() {

        Intent intent = new Intent(this, BadgeEntryActivity.class);
        intent.putExtra(BadgeEntryActivity.KEY_PROJECT, project);
        startActivity(intent);
    }

    private void gotoAutoGeoPunch() {
        if (checkLocationPermission()) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                doAutoPunchButtonTask();
            }
        }

    }

    private void gotoFaceRcognition() {
        Intent intent = new Intent(this, FaceRecognitionActivity.class);
        intent.putExtra(VirtualGateActivity.KEY_PROJECT, project);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed(); //Check if you still want to go back
    }

    public boolean checkLocationPermission() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("PunchCard")
                    .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();

                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();


            return false;
        } else {
            return true;
        }
    }

    public void doAutoPunchButtonTask() {
        if (scanner_access != "") {

            if (btnAutoGeopunch.getText().equals("Enable AutoPunch")) {
                String checkStatus = getPCApplication().getmGlobalCheckButton();

                if (TextUtils.isEmpty(checkStatus)) {
                    showAlertBox("By enabling the autopunch feature, you agree to allow your employer and Punchcard LLC to track your gps location automatically.");
                    //  }
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    autoPunch_btn_text = "Enable";
                    editor.putString(PREF_AUTOPUNCH_BUTTON_TEXT, autoPunch_btn_text);
                    editor.apply();

                    checkAutoPunchStatus = false;
                    btnAutoGeopunch.setText("Disable AutoPunch");


                    Intent intent = new Intent(VirtualGateActivity.this, GeoPunchService.class);
                    startService(intent);

                    Toast.makeText(VirtualGateActivity.this, "Auto Punch Enabled", Toast.LENGTH_SHORT).show();
                }
            } else {

                SharedPreferences.Editor editor = preferences.edit();
                autoPunch_btn_text = "Disable";
                editor.putString(PREF_AUTOPUNCH_BUTTON_TEXT, autoPunch_btn_text);
                editor.apply();
                Intent intent = new Intent(this, GeoPunchService.class);
                intent.setAction(custom_action);
                startService(intent);
                checkAutoPunchStatus = true;
                btnAutoGeopunch.setText("Enable AutoPunch");
                Toast.makeText(this, "Auto Punch Disabled", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "You do not have permission to enable AutoPunch", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAlertBox(String message) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle("Punch Card");
        alertDialog.setMessage(message);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here

                SharedPreferences.Editor editor = preferences.edit();
                autoPunch_btn_text = "Enable";
                editor.putString(PREF_AUTOPUNCH_BUTTON_TEXT, autoPunch_btn_text);
                editor.apply();

                checkAutoPunchStatus = false;
                btnAutoGeopunch.setText("Disable AutoPunch");


                Logger.d(user.getRole());
                Logger.d(user.getUniq_id());
                Logger.d(user.getFirst_name());
                Intent intent = new Intent(VirtualGateActivity.this, GeoPunchService.class);
                startService(intent);

                Toast.makeText(VirtualGateActivity.this, "Auto Punch Enabled", Toast.LENGTH_SHORT).show();
                getPCApplication().setmGlobalCheckButton("agreed");
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

}
