package com.inverseapps.punchcard.ui;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.CheckInOut;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.QRCodeInfo;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.QRCheckInOutParam;
import com.inverseapps.punchcard.utils.barcode.BarcodeCaptureActivity;
import com.orhanobut.logger.Logger;


import butterknife.BindView;
import butterknife.OnClick;


public class QRScannerActivity extends PCLocationActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_qr_scanner;
    }

    @NonNull
    @BindView(R.id.lblResult)
    TextView lblResult;

    @NonNull
    @BindView(R.id.btnCheckIn)
    Button btnCheckIn;

    @NonNull
    @BindView(R.id.btnCheckOut)
    Button btnCheckOut;

    @OnClick({R.id.btnCheckIn, R.id.btnCheckOut})
    void onClickedScanBarCodeButton(Button button) {

        if (currentLocation == null) {
            Toast.makeText(this,
                    "Please wait while getting your current location or you have to turn on location to use this feature!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (button == btnCheckIn) {
            forCheckIn = true;
        } else {
            forCheckIn = false;
        }
        gotoBarcodeCaptureScreen();
    }

    private static final int BARCODE_READER_REQUEST_CODE = 100;

    public static final String KEY_PROJECT = "project";
    private static final String KEY_FOR_CHECK_IN = "forCheckIn";
    private static final String KEY_QR_CODE_INFO = "qrCodeInfo";

    private Project project;
    boolean forCheckIn;
    private QRCodeInfo qrCodeInfo;
    private CheckOutDelegate checkOutDelegate;
    private CheckInDelegate checkInDelegate;
    private FindEmployeeForCheckInOutDelegate findEmployeeForCheckInOutDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("QR Scanner");


        project = getIntent().getParcelableExtra(KEY_PROJECT);
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            forCheckIn = savedInstanceState.getBoolean(KEY_FOR_CHECK_IN);
            qrCodeInfo = savedInstanceState.getParcelable(KEY_QR_CODE_INFO);
        }

        checkOutDelegate = new CheckOutDelegate(this);
        listOfForegroundTaskDelegates.add(checkOutDelegate);

        checkInDelegate = new CheckInDelegate(this);
        listOfForegroundTaskDelegates.add(checkInDelegate);

        findEmployeeForCheckInOutDelegate = new FindEmployeeForCheckInOutDelegate(this);
        listOfForegroundTaskDelegates.add(findEmployeeForCheckInOutDelegate);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putBoolean(KEY_FOR_CHECK_IN, forCheckIn);
        outState.putParcelable(KEY_QR_CODE_INFO, qrCodeInfo);

        super.onSaveInstanceState(outState);
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

    private void gotoBarcodeCaptureScreen() {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    try {
                        Logger.d(barcode.displayValue);
                        qrCodeInfo = new Gson().fromJson(barcode.displayValue, QRCodeInfo.class);

                        checkToPunchInOut();
                    } catch (Exception ex) {
                        showUnSuccessfulScan(null);
                        showAlertDialog("Not a PunchCard barcode!");
                    }
                } else {
                    lblResult.setText(R.string.no_barcode_captured);
                }
            } else {
                Logger.d(String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
    }



    private void checkToPunchInOut() {

        User user = pcFunctionService.getInternalStoredUser();
        if (!qrCodeInfo.getProject_uniq_id().equals(project.getUniq_id())) {
            showUnSuccessfulScan(null);
            String msg = String.format("The badge you are trying to scan is not for project [%s]", project.getName());
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return;
        } else if (qrCodeInfo.getUser_uniq_id().equals(user.getUniq_id())) {
            Toast.makeText(this, "You cannot badge yourself into the project!", Toast.LENGTH_SHORT).show();
            return;
        }

        QRCheckInOutParam param = new QRCheckInOutParam(project.getUniq_id(),
                qrCodeInfo.getProject_uniq_id(),
                qrCodeInfo.getUser_uniq_id(),
                currentLocation.getLatitude(),
                currentLocation.getLongitude());

        findEmployeeForCheckInOutDelegate.setForCheckIn(forCheckIn);
        findEmployeeForCheckInOutDelegate.setParam(param);
        pcFunctionService.findEmployee(qrCodeInfo.getUser_uniq_id(), findEmployeeForCheckInOutDelegate);
    }

    private void showSuccessfulScan(@NonNull Employee employee,String checkStatus) {
        if (employee == null) {
            lblResult.setText("Successful scan");
        } else {
            lblResult.setText(String.format("Successful %s  for %s",checkStatus, employee.getName()));
        }

        lblResult.setBackgroundColor(Color.GREEN);
        lblResult.setTextColor(Color.WHITE);
    }

    private void showUnSuccessfulScan(@NonNull Employee employee) {
        if (employee == null) {
            lblResult.setText("Unsuccessful scan");
        } else {
            lblResult.setText(String.format("Unsuccessful scan for %s", employee.getName()));
        }

        lblResult.setBackgroundColor(Color.RED);
        lblResult.setTextColor(Color.WHITE);
    }

    private void punchIn(Employee employee, QRCheckInOutParam param) {
        checkInDelegate.setEmployee(employee);
        pcFunctionService.qrCheckIn(param, checkInDelegate);
    }

    private void punchOut(Employee employee, QRCheckInOutParam param) {
        checkOutDelegate.setEmployee(employee);
        pcFunctionService.qrCheckOut(param, checkOutDelegate);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("QRScanner Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private static class CheckInDelegate extends ForegroundTaskDelegate<CheckInOut> {

        @NonNull
        private Employee employee;

        public void setEmployee(@NonNull Employee employee) {
            this.employee = employee;
        }

        public CheckInDelegate(QRScannerActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut,boolean result, Throwable throwable) {
           super.onPostExecute(checkInOut,result, throwable);

            QRScannerActivity activity = (QRScannerActivity) activityWeakReference.get();

            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

                if(! TextUtils.isEmpty(checkInOut.getBadge().getName())){
                activity.showSuccessfulScan(employee,"Check In");
                }
            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

            }
        }

        protected void handleServiceError(ServiceException ex,CheckInOut checkInOut) {
            QRScannerActivity activity = (QRScannerActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                if (ex.getErrorCode() == 422) {
                    String msgForCheckOut = checkInOut.getBadge().getName()+" is already Punched In";
                    activity.showUnSuccessfulScan(employee);
                }



            }
        }
    }

    private static class CheckOutDelegate extends ForegroundTaskDelegate<CheckInOut> {

        @NonNull
        private Employee employee;

        public void setEmployee(@NonNull Employee employee) {
            this.employee = employee;
        }

        public CheckOutDelegate(QRScannerActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut, boolean result, Throwable throwable) {
            super.onPostExecute(checkInOut,result, throwable);

            QRScannerActivity activity = (QRScannerActivity) activityWeakReference.get();
            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

                if(! TextUtils.isEmpty(checkInOut.getBadge().getName())) {
                    activity.showSuccessfulScan(employee,"Check Out");
                }
            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

            }
        }

        protected void handleServiceError(ServiceException ex,CheckInOut checkInOut) {
            QRScannerActivity activity = (QRScannerActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                if (ex.getErrorCode() == 422) {
                    String msgForCheckOut = checkInOut.getBadge().getName()+" is already Punched Out";
                    activity.showUnSuccessfulScan(employee);
                }


            }
        }
    }

    private static class FindEmployeeForCheckInOutDelegate extends ForegroundTaskDelegate<Employee> {

        @NonNull
        private QRCheckInOutParam param;

        @NonNull
        private boolean forCheckIn;

        public void setParam(@NonNull QRCheckInOutParam param) {
            this.param = param;
        }

        public void setForCheckIn(@NonNull boolean forCheckIn) {
            this.forCheckIn = forCheckIn;
        }

        public FindEmployeeForCheckInOutDelegate(QRScannerActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Employee employee, Throwable throwable) {
            super.onPostExecute(employee, throwable);
            QRScannerActivity activity = (QRScannerActivity) activityWeakReference.get();
            if (throwable == null &&
                    employee != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                if (forCheckIn) {
                    activity.punchIn(employee, param);
                } else {
                    activity.punchOut(employee, param);
                }
            }
        }
    }

}
