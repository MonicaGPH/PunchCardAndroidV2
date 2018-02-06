package com.inverseapps.punchcard.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.Badge;
import com.inverseapps.punchcard.model.CheckInOut;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.BadgeCheckInOutParam;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class BadgeEntryActivity extends PCLocationActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_badge_entry;
    }

    @NonNull
    @BindView(R.id.lblResult)
    TextView lblResult;

    @NonNull
    @BindView(R.id.txtBadgeNumber)
    EditText txtBadgeNumber;

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

        checkToPunchInOut();
    }

    @OnFocusChange({R.id.txtBadgeNumber})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtBadgeNumber.hasFocus()) {
            hideKeyboard();
        } else {
            showKeyboard();
        }
    }


    public static final String KEY_PROJECT = "project";
    public static final String KEY_BADGE_ID = "badgeId";
    private static final String KEY_FOR_CHECK_IN = "forCheckIn";

    private Project project;
    private int badgeId;
    boolean forCheckIn;
    private int userBadgeId;

    private ForegroundTaskDelegate checkOutDelegate;
    private ForegroundTaskDelegate checkInDelegate;
    private ForegroundTaskDelegate findBadgeDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Badge Entry");
        project = getIntent().getParcelableExtra(KEY_PROJECT);
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            badgeId = savedInstanceState.getInt(KEY_BADGE_ID);
            forCheckIn = savedInstanceState.getBoolean(KEY_FOR_CHECK_IN);
        }

        if (badgeId != 0) {
            txtBadgeNumber.setText("" + badgeId);
        }
        txtBadgeNumber.requestFocus();

        findBadgeDelegate = new FindBadgeDelegate(this);
        listOfForegroundTaskDelegates.add(findBadgeDelegate);

        checkOutDelegate = new CheckOutDelegate(this);
        listOfForegroundTaskDelegates.add(checkOutDelegate);

        checkInDelegate = new CheckInDelegate(this);
        listOfForegroundTaskDelegates.add(checkInDelegate);

        User user = pcFunctionService.getInternalStoredUser();
        if (user != null) {
            pcFunctionService.findBadge(user.getUniq_id(), project.getUniq_id(), findBadgeDelegate);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putInt(KEY_BADGE_ID, badgeId);
        outState.putBoolean(KEY_FOR_CHECK_IN, forCheckIn);
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

    private void checkToPunchInOut() {

        if (TextUtils.isEmpty(txtBadgeNumber.getText().toString())) {
            Toast.makeText(this, "Please make sure you have provided a badge number!", Toast.LENGTH_SHORT).show();
            return;
        }

        badgeId = Integer.parseInt(txtBadgeNumber.getText().toString());
        if (badgeId <= 0) {
            Toast.makeText(this, "Please make sure you have provided a badge number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (badgeId == userBadgeId) {
            Toast.makeText(this, "You cannot badge yourself into the project!", Toast.LENGTH_SHORT).show();
            return;
        }

        BadgeCheckInOutParam param = new BadgeCheckInOutParam(project.getUniq_id(),
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                badgeId);

        if (forCheckIn) {
            punchIn(param);
        } else {
            punchOut(param);
        }
    }

    private void punchIn(BadgeCheckInOutParam param) {
        pcFunctionService.badgeCheckIn(param, checkInDelegate);
    }

    private void punchOut(BadgeCheckInOutParam param) {
        pcFunctionService.badgeCheckOut(param, checkOutDelegate);
    }


    private void showSuccessfulInput(String msg) {
        lblResult.setText(msg);
        lblResult.setBackgroundColor(Color.GREEN);
        lblResult.setTextColor(Color.WHITE);
    }

    private void showUnSuccessfulInput(CheckInOut checkInOut) {

        lblResult.setText(String.format("Unsuccessful badge for %s", checkInOut.getBadge().getName()));
        lblResult.setBackgroundColor(Color.RED);
        lblResult.setTextColor(Color.WHITE);
    }

    private static class CheckInDelegate extends ForegroundTaskDelegate<CheckInOut> {

        public CheckInDelegate(BadgeEntryActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut, boolean result, Throwable throwable) {
            super.onPostExecute(checkInOut, result, throwable);

            BadgeEntryActivity activity = (BadgeEntryActivity) activityWeakReference.get();



            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                if (!TextUtils.isEmpty(checkInOut.getBadge().getName())) {
                    activity.badgeId = 0;
                    activity.txtBadgeNumber.setText("");
                    activity.txtBadgeNumber.requestFocus();

                    activity.showSuccessfulInput("Successful Check In for " + checkInOut.getBadge().getName());
                }

            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

            }
        }

        protected void handleServiceError(ServiceException ex, CheckInOut checkInOut) {
            BadgeEntryActivity activity = (BadgeEntryActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                String msgForCheckOut = checkInOut.getBadge().getName() + " is already Punched In";
                activity.showUnSuccessfulInput(checkInOut);

            }
        }
    }

    private static class CheckOutDelegate extends ForegroundTaskDelegate<CheckInOut> {

        public CheckOutDelegate(BadgeEntryActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(CheckInOut checkInOut, boolean result, Throwable throwable) {
            super.onPostExecute(checkInOut, result, throwable);

            BadgeEntryActivity activity = (BadgeEntryActivity) activityWeakReference.get();

            if (throwable == null &&
                    result &&
                    activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

                if (!TextUtils.isEmpty(checkInOut.getBadge().getName())) {
                    activity.badgeId = 0;
                    activity.txtBadgeNumber.setText("");
                    activity.txtBadgeNumber.requestFocus();

                    activity.showSuccessfulInput("Successful Check Out for " + checkInOut.getBadge().getName());
                }

            } else if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {

            }
        }

        protected void handleServiceError(ServiceException ex, CheckInOut checkInOut) {
            BadgeEntryActivity activity = (BadgeEntryActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                if (ex.getErrorCode() == 422) {
                    String msgForCheckOut = checkInOut.getBadge().getName() + " is already Punched Out";
                    activity.showUnSuccessfulInput(checkInOut);
                }


            }
        }
    }

    private static class FindBadgeDelegate extends ForegroundTaskDelegate<Badge> {

        public FindBadgeDelegate(BadgeEntryActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Badge badge, Throwable throwable) {
            super.onPostExecute(badge, throwable);
            BadgeEntryActivity activity = (BadgeEntryActivity) activityWeakReference.get();
            if (badge != null && activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                activity.userBadgeId = badge.getBadge_id();
            }
        }
    }
}
