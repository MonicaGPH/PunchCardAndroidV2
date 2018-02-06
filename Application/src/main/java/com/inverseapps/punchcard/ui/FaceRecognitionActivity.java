package com.inverseapps.punchcard.ui;


import android.content.Intent;

import android.graphics.Color;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.view.MenuItem;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.CheckInOut;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.FaceCheckInOutParam;


import com.orhanobut.logger.Logger;


import butterknife.BindView;
import butterknife.OnClick;


public class FaceRecognitionActivity extends PCLocationActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_face_login;
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

    private CheckOutDelegate checkOutDelegate;
    private CheckInDelegate checkInDelegate;
    private FindEmployeeForCheckInOutDelegate findEmployeeForCheckInOutDelegate;
    private static final int SEND_TO_IMAGE_ACTIVITY = 100;
    public Project project;
    public static final String KEY_PROJECT = "project";
    private static final String KEY_FOR_CHECK_IN = "forCheckIn";
    private static final String KEY_QR_CODE_INFO = "qrCodeInfo";
    private String face_user_unique_id;

    boolean forCheckIn;

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
        gotoImageCaptureScreen();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Face Recognition");

        project = getIntent().getParcelableExtra(KEY_PROJECT);
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            forCheckIn = savedInstanceState.getBoolean(KEY_FOR_CHECK_IN);

        }
        checkOutDelegate = new CheckOutDelegate(this);
        listOfForegroundTaskDelegates.add(checkOutDelegate);

        checkInDelegate = new CheckInDelegate(this);
        listOfForegroundTaskDelegates.add(checkInDelegate);

        findEmployeeForCheckInOutDelegate = new FindEmployeeForCheckInOutDelegate(this);
        listOfForegroundTaskDelegates.add(findEmployeeForCheckInOutDelegate);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putBoolean(KEY_FOR_CHECK_IN, forCheckIn);
        super.onSaveInstanceState(outState);
    }

    private void gotoImageCaptureScreen() {
        Intent intent = new Intent(getApplicationContext(), ImageCaptureActivity.class);
        startActivityForResult(intent, SEND_TO_IMAGE_ACTIVITY);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND_TO_IMAGE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    face_user_unique_id = data.getExtras().getString(ImageCaptureActivity.KEY_RESPONSE_USER_UNIQUE_ID);
                    Logger.d(face_user_unique_id);

                    try {

                         checkToPunchInOut();
                    } catch (Exception ex) {
                        showUnSuccessfulScan(null);
                        showAlertDialog("Not Authenticated User");
                    }
                } else {
                    lblResult.setText(R.string.no_image_captured);
                }
            } else {
            }
        }
    }

    private void checkToPunchInOut() {

          User user = pcFunctionService.getInternalStoredUser();

         if (face_user_unique_id.equals(user.getUniq_id())) {
             showAlertDialog("You cannot recognize yourself into the project!");
              return;
          }

          FaceCheckInOutParam param = new FaceCheckInOutParam(project.getUniq_id(),
                  project.getUniq_id(),
                  face_user_unique_id,
                  currentLocation.getLatitude(),
                  currentLocation.getLongitude());

          findEmployeeForCheckInOutDelegate.setForCheckIn(forCheckIn);
          findEmployeeForCheckInOutDelegate.setParam(param);
          pcFunctionService.findEmployee(face_user_unique_id, findEmployeeForCheckInOutDelegate);
      }

    private void showSuccessfulScan(@NonNull Employee employee) {
        if (employee == null) {
            lblResult.setText("Successful scan");
        } else {
            lblResult.setText(String.format("Successful scan for %s", employee.getName()));
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

    private void punchIn(Employee employee, FaceCheckInOutParam param) {
        checkInDelegate.setEmployee(employee);
        pcFunctionService.faceCheckIn(param, checkInDelegate);
    }

    private void punchOut(Employee employee, FaceCheckInOutParam param) {
        checkOutDelegate.setEmployee(employee);
        pcFunctionService.faceCheckOut(param, checkOutDelegate);
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private static class CheckInDelegate extends ForegroundTaskDelegate<CheckInOut> {

        @NonNull
        private Employee employee;

        public void setEmployee(@NonNull Employee employee) {
            this.employee = employee;
        }

        public CheckInDelegate(FaceRecognitionActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut,boolean result, Throwable throwable) {
            super.onPostExecute(checkInOut,result, throwable);

            FaceRecognitionActivity activity = (FaceRecognitionActivity) activityWeakReference.get();
            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                if(!TextUtils.isEmpty(checkInOut.getBadge().getName()))
                activity.showSuccessfulScan(employee);
            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                activity.showUnSuccessfulScan(employee);
            }
        }

        protected void handleServiceError(ServiceException ex) {
            PCActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                String msgForCheckOut = "Unable to check the requested user in. Please check to make sure the user is not already checked in to this project or checked in to another project. If the user is not checked in to a project, please contact the database administrator.";
                activity.showAlertDialog(msgForCheckOut);
            }
        }
    }

    private static class CheckOutDelegate extends ForegroundTaskDelegate<CheckInOut> {

        @NonNull
        private Employee employee;

        public void setEmployee(@NonNull Employee employee) {
            this.employee = employee;
        }

        public CheckOutDelegate(FaceRecognitionActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut,boolean result, Throwable throwable) {
            super.onPostExecute(checkInOut,result, throwable);

            FaceRecognitionActivity activity = (FaceRecognitionActivity) activityWeakReference.get();
            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                if(!TextUtils.isEmpty(checkInOut.getBadge().getName()))
                activity.showSuccessfulScan(employee);
            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                activity.showUnSuccessfulScan(employee);
            }
        }

        protected void handleServiceError(ServiceException ex) {
            PCActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                String msgForCheckOut = "Unable to check the requested user out. Please check to make sure the user is not already checked out of this project or checked out of of another project. If the user is not checked out of a project, please contact the database administrator.";
                activity.showAlertDialog(msgForCheckOut);
            }
        }
    }

    private static class FindEmployeeForCheckInOutDelegate extends ForegroundTaskDelegate<Employee> {

        @NonNull
        private FaceCheckInOutParam param;

        @NonNull
        private boolean forCheckIn;

        public void setParam(@NonNull FaceCheckInOutParam param) {
            this.param = param;
        }

        public void setForCheckIn(@NonNull boolean forCheckIn) {
            this.forCheckIn = forCheckIn;
        }

        public FindEmployeeForCheckInOutDelegate(FaceRecognitionActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Employee employee, Throwable throwable) {
            super.onPostExecute(employee, throwable);
            FaceRecognitionActivity activity = (FaceRecognitionActivity) activityWeakReference.get();
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


