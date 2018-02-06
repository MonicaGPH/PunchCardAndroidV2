package com.inverseapps.punchcard.ui;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.service.retrofit.PCRetrofitService;
import com.inverseapps.punchcard.ui.dialog.ViewDialog;
import com.inverseapps.punchcard.ui.dialog.ViewDialog_CA;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;


public class LoginActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_login;
    }

    @NonNull
    @BindView(R.id.btnSettings)
    Button btnSettings;

    @NonNull
    @BindView(R.id.txtCompanyHandle)
    EditText txtCompanyHandle;

    @NonNull
    @BindView(R.id.txtUserName)
    EditText txtUserName;

    @NonNull
    @BindView(R.id.txtPassword)
    TextInputEditText txtPassword;

    @NonNull
    @BindView(R.id.txtLayout)
    TextInputLayout txtLayout;

    @NonNull
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @NonNull
    @BindView(R.id.chkRememberMe)
    CheckBox chkRememberMe;

    @NonNull
    @BindView(R.id.lblForgotPassword)
    TextView lblForgotPassword;


    @OnClick(R.id.btnSettings)
    void onClickedSettingsButton() {

        inputDomain();

    }


    @OnClick(R.id.btnLogin)
    public void onClickedLoginButton() {
        checkToLogin();
    }

    @OnClick(R.id.lblForgotPassword)
    void onClickedForgotPasswordLabel() {
        String companyHandle = txtCompanyHandle.getText().toString().trim();
        if (TextUtils.isEmpty(companyHandle)) {
            showAlertDialog("Please make sure you have provided a company handle!");
        } else if (companyHandle.contains(" ")) {
            showAlertDialog("Please include a company handle with no spaces in the correct format!");
        } else {
            String domain = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_DOMAIN, "");
            if (TextUtils.isEmpty(domain)) {
                domain = AppConstant.DEFAULT_DOMAIN;
            }
            String baseUrl = String.format(PCRetrofitService.API_END_POINT_FORMAT, companyHandle, domain) + "password/reset";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl));
            startActivity(browserIntent);
        }
    }

    @OnFocusChange({R.id.txtCompanyHandle, R.id.txtUserName})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtCompanyHandle.hasFocus() && !txtUserName.hasFocus()) {
            hideKeyboard();
        }

    }

    @OnFocusChange(R.id.txtPassword)
    public void onFocusChange1(View view, boolean hasFocus) {

        if (!txtPassword.hasFocus()) {
            txtPassword.setHint("Password");
            hideKeyboard();
        }
    }

    @OnEditorAction(R.id.txtPassword)
    public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            checkToLogin();
            return true;
        }
        return false;
    }

    private LoginDelegate loginDelegate;
    private FindUserDelegate findUserDelegate;
    public static LogOuteUserDelegate logOutUserDelegate;
    private static final String KEY_COMPANY_HANDLE = "companyHandle";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "isRememberMe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginDelegate = new LoginDelegate(this);
        listOfForegroundTaskDelegates.add(loginDelegate);

        findUserDelegate = new FindUserDelegate(this);
        listOfForegroundTaskDelegates.add(findUserDelegate);

        logOutUserDelegate = new LogOuteUserDelegate(this);
        listOfForegroundTaskDelegates.add(logOutUserDelegate);

        String companyHandle = pcFunctionService.getCompanyHandle();
        String userName = pcFunctionService.getUserName();
        String password = pcFunctionService.getPassword();
        Logger.d(password);
        boolean rememberMe = pcFunctionService.isRememberMe();

        txtPassword.setHint("Password");
        if (getIntent().hasExtra(ChangePasswordActivity.KEY_NEW_PASSWORD)) {

            if (!getIntent().getExtras().getString(ChangePasswordActivity.KEY_NEW_PASSWORD).equals(null)) {
                password = getIntent().getStringExtra(ChangePasswordActivity.KEY_NEW_PASSWORD);
            }
        }

        if (savedInstanceState != null) {
            companyHandle = savedInstanceState.getString(KEY_COMPANY_HANDLE);
            userName = savedInstanceState.getString(KEY_USER_NAME);
            password = savedInstanceState.getString(KEY_PASSWORD);
            rememberMe = savedInstanceState.getBoolean(KEY_REMEMBER_ME);
        }
        if (rememberMe) {
            txtCompanyHandle.setText(companyHandle);
            txtUserName.setText(userName);
            txtPassword.setText(password);
        }
        txtCompanyHandle.setText(companyHandle);
        txtUserName.setText(userName);
        txtPassword.setText(password);
        chkRememberMe.setChecked(rememberMe);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        String companyHandle = txtCompanyHandle.getText().toString().trim();
        String userName = txtUserName.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        outState.putString(KEY_COMPANY_HANDLE, companyHandle);
        outState.putString(KEY_USER_NAME, userName);
        outState.putString(KEY_PASSWORD, password);
        outState.putBoolean(KEY_REMEMBER_ME, chkRememberMe.isChecked());

        super.onSaveInstanceState(outState);
    }

    private void inputDomain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings");
        builder.setMessage("Please enter a domain");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        input.setHint("Domain");

        String domain = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            input.setText(AppConstant.DEFAULT_DOMAIN);
        } else {
            input.setText(domain);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String domain = input.getText().toString().trim();
                getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_DOMAIN, domain).apply();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void checkToLogin() {

        String companyHandle = txtCompanyHandle.getText().toString().trim();
        String userName = txtUserName.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(companyHandle) ||
                TextUtils.isEmpty(userName) ||
                TextUtils.isEmpty(password)) {
            showAlertDialog("Please make sure you have provided a company handle, a username and password!");
        } else if (companyHandle.contains(" ")) {
            showAlertDialog("Please include a company handle with no spaces in the correct format!");
        } else {
            login();
        }
    }

    private void login() {
        String companyHandle = txtCompanyHandle.getText().toString().trim();
        String userName = txtUserName.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        pcFunctionService.login(companyHandle, userName, password, loginDelegate);
    }

    public void gotoHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.KEY_REMIND_ME_LATER_CLICKED, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void gotoHomeScreenRemindLater() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.KEY_REMIND_ME_LATER_CLICKED, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void findUser() {
        pcFunctionService.findUser(findUserDelegate);
    }

    private class LoginDelegate extends ForegroundTaskDelegate<Boolean> {

        public LoginDelegate(PCActivity activity) {
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
            LoginActivity activity = (LoginActivity) activityWeakReference.get();
            if (throwable == null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                String userName = activity.txtUserName.getText().toString().trim();
                String password = activity.txtPassword.getText().toString().trim();
                boolean remember = activity.chkRememberMe.isChecked();

                activity.getPCApplication()
                        .getPcFunctionService()
                        .internalStoreBasicUserInfoIfNeeded(userName, password, remember);

                activity.findUser();
            }
        }
    }

    private class FindUserDelegate extends ForegroundTaskDelegate<User> {
        String formatted_endDate;

        public FindUserDelegate(LoginActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            LoginActivity activity = (LoginActivity) activityWeakReference.get();
            if (throwable == null
                    && user != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                try {

                    /////////////////////credential of Parent Company's user
                    if (user.getClient().getChild_of_id() == 0) {
                        formatted_endDate = activity.checkDateAfter30days(user.getClient().getCreated_at(), activity);
                        Logger.d(formatted_endDate);

                    }
                    /////////////////////credential of Child Company's user
                    else {
                        formatted_endDate = activity.checkDateAfter30days(user.getCreated_dateParent(), activity);
                        Logger.d(formatted_endDate);
                    }


                    activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_END_DATE, formatted_endDate).apply();
                    activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TIER, user.getClient().getPlan_test());

                    /////////////////////////////// role :: clientadmin

                    if (user.getRole().equalsIgnoreCase("clientadmin")) {
                        String currentDate = activity.getCurrentDate();
                        String endDate = activity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        try {

                            /////////////// version :: trial
                            if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

                                if (format.parse(currentDate).after(format.parse(endDate))) {
                                    Logger.d("more than 30 days");
                                    ViewDialog_CA alert = new ViewDialog_CA();
                                    activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                                    alert.showDialogATLogInActivity(activity, "Your Punchcard Trial has expired on " + endDate +
                                            ". To reactivate your account, please resubscribe today.", "trial_plan");

                                } else {
                                    Date date1 = format.parse(currentDate);
                                    Date date2 = format.parse(endDate);
                                    long diff = date2.getTime() - date1.getTime();
                                    Logger.i("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                    activity.gotoHomeScreen();
                                }

                            }
                            /////////////// version :: trialExpired
                            else if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired")) {
                                ViewDialog_CA alert = new ViewDialog_CA();
                                activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                alert.showDialogATLogInActivity(activity, "Your Punchcard Trial has expired on " + endDate +
                                        ". To reactivate your account, please resubscribe today.", "trial_plan");
                            }
                            /////////////// version :: unsubscribe
                            else if (user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                                ViewDialog_CA alert = new ViewDialog_CA();
                                activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                                alert.showDialogATLogInActivity(activity, "You previously unsubscribed from PunchCard. If you would like to continue using PunchCard, please resubscribe,select plan and enter your billing information.", "unsubscribe");
                            } else if (user.getClient().getCreditCardDeactivateDate() != null & !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                                /////////////////check card is decline or not !!
                                String dateofcarddecline = null;
                                String datebefore2days = getMonthSpecific(checkDatebefore2Days(user.getClient().getCreditCardDeactivateDate(), activity));
                                //////////////////////card is decline

                                try {
                                    if (format.parse(currentDate).after(format.parse(user.getClient().getCreditCardDeactivateDate()))) {
                                        // date is more than creditCardDeactivateDate
                                        Logger.d("more than 2 days");
                                        ViewDialog_CA alert = new ViewDialog_CA();
                                        activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                        alert.showDialogForCardDecline(activity, "Oops! Something went wrong with your payment and your Credit Card information needs to be updated. \n \nTo reactivate your account,please update your payment info at the link below.", user);

                                    } else {
                                        Date date1 = format.parse(currentDate);
                                        Date date2 = format.parse(user.getClient().getCreditCardDeactivateDate());
                                        long diff = date2.getTime() - date1.getTime();
                                        long hoursleft = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                                        Logger.i("Days: " + hoursleft);
                                        ViewDialog_CA alert = new ViewDialog_CA();
                                        if (hoursleft == 0) {
                                            activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                            alert.showDialogForCardDecline(activity, "Oops! Something went wrong with your payment and your Credit Card information needs to be updated. \n \nTo reactivate your account,please update your payment info at the link below.", user);

                                        } else {
                                            alert.showDialogForCardDeclineWarning(activity, "Oops! It appears the CC was declined on " + datebefore2days + ".\n You have  " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) +
                                                    " hours to update your payment information before your account is deactivated.\n\n" +
                                                    "To update your payment info, click the link below.\n", user);
                                        }

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                activity.gotoHomeScreen();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    /////////////////////////////// role :: user/superuser/admin
                    else {
                        String currentDate = activity.getCurrentDate();
                        String endDate = activity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            /////////////// version :: trial
                            if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

                                if (format.parse(currentDate).after(format.parse(endDate))) {
                                    Logger.d("more than 30 days");
                                    ViewDialog alert = new ViewDialog();
                                    activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();

                                    alert.showDialogATLogInActivity(activity, "The trial period has expired, please contact your client admin or contact PunchCard support.");

                                } else {
                                    Date date1 = format.parse(currentDate);
                                    Date date2 = format.parse(endDate);
                                    long diff = date2.getTime() - date1.getTime();
                                    Logger.i("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                    activity.gotoHomeScreen();
                                }
                            }
                            /////////////// version :: trialExpired
                            else if (user.getClient().getPlan_test().equalsIgnoreCase("trialExpired")) {
                                ViewDialog alert = new ViewDialog();
                                activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                alert.showDialogATLogInActivity(activity, "The trial period has expired, please contact your client admin or contact PunchCard support.");
                            }
                            /////////////// version :: unsubscribe
                            else if (user.getClient().getPlan_test().equalsIgnoreCase("unsubscribe")) {
                                ViewDialog alert = new ViewDialog();
                                activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                alert.showDialogATLogInActivity(activity, "Status Unsubscribed, please contact your client admin or contact PunchCard support.");
                            } else if (user.getClient().getCreditCardDeactivateDate() != null & !TextUtils.isEmpty(user.getClient().getCreditCardDeactivateDate())) {
                                /////////////////check card is decline or not !!
                                String dateofcarddecline = null;

                                //////////////////////card is decline

                                try {
                                    if (format.parse(currentDate).after(format.parse(user.getClient().getCreditCardDeactivateDate()))) {
                                        // date is more than creditCardDeactivateDate
                                        Logger.d("more than 2 days");
                                        ViewDialog alert = new ViewDialog();
                                        activity.getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                                        alert.showDialogATLogInActivity(activity, "Credit Card may Expired or Declined due to some reason.  Please contact your client admin or contact PunchCard support.");

                                    } else {
                                        activity.gotoHomeScreen();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                activity.gotoHomeScreen();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public class LogOuteUserDelegate extends ForegroundTaskDelegate<Boolean> {

        public LogOuteUserDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean aBoolean, Throwable throwable) {
            super.onPostExecute(aBoolean, throwable);
            LoginActivity activity = (LoginActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                Logger.d("LogOut Successfull");
            }
        }
    }

    public static String getMonthSpecific(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        return monthName;
    }

}
