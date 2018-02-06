package com.inverseapps.punchcard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.param.ChangePasswordParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

public class ChangePasswordActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_change_password;
    }

    @NonNull
    @BindView(R.id.txtCurrentPassword)
    TextInputEditText txtCurrentPassword;

    @NonNull
    @BindView(R.id.txtNewPassword)
    TextInputEditText txtNewPassword;

    @NonNull
    @BindView(R.id.txtConfirmNewPassword)
    TextInputEditText txtConfirmNewPassword;

    @NonNull
    @BindView(R.id.btnSave)
    Button btnSave;

    @OnFocusChange({R.id.txtCurrentPassword, R.id.txtNewPassword, R.id.txtConfirmNewPassword})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtCurrentPassword.hasFocus()) {
            txtCurrentPassword.setHint("Current Password");
            hideKeyboard();
        }

        if (!txtNewPassword.hasFocus()) {
            txtNewPassword.setHint("New Password");
            hideKeyboard();
        }
        if (!txtConfirmNewPassword.hasFocus()) {
            txtConfirmNewPassword.setHint("Confirm New Password");
            hideKeyboard();
        }
    }

    @OnEditorAction(R.id.txtConfirmNewPassword)
    public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.btnSave)
    void onClickedSaveButton() {
        checkToChangePassword();
    }

    public static final String KEY_CURRENT_PASSWORD = "currentPassword";
    public static final String KEY_NEW_PASSWORD = "newPassword";
    public static final String KEY_CONFIRM_NEW_PASSWORD = "confirmNewPassword";
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    private ForegroundTaskDelegate changePasswordDelegate;
    private ForegroundTaskDelegate logOutDelegate;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        if (savedInstanceState != null) {
            currentPassword = savedInstanceState.getString(KEY_CURRENT_PASSWORD);
            newPassword = savedInstanceState.getString(KEY_NEW_PASSWORD);
            confirmNewPassword = savedInstanceState.getString(KEY_CONFIRM_NEW_PASSWORD);
        }
        pattern = Pattern.compile(PASSWORD_PATTERN);
        txtCurrentPassword.setText(currentPassword);
        txtNewPassword.setText(newPassword);
        txtConfirmNewPassword.setText(confirmNewPassword);

        changePasswordDelegate = new ChangePasswordDelegate(this);
        listOfForegroundTaskDelegates.add(changePasswordDelegate);

        logOutDelegate = new LogoutDelegate(this);
        listOfForegroundTaskDelegates.add(logOutDelegate);

        txtNewPassword.setHint("New Password");
        txtConfirmNewPassword.setHint("Confirm New Password");
        txtCurrentPassword.setHint("Current Password");
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_CURRENT_PASSWORD, currentPassword);
        outState.putString(KEY_NEW_PASSWORD, newPassword);
        outState.putString(KEY_CONFIRM_NEW_PASSWORD, confirmNewPassword);

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

    private String validateFields() {
        String msg = "";
        String savedPassword = pcFunctionService.getPassword();
        if (TextUtils.isEmpty(currentPassword)) {
            msg = "Please make sure you have provided the current password!";
            txtCurrentPassword.requestFocus();
        } else if (!savedPassword.equals(currentPassword)) {
            msg = "The input current password is not right!";
            txtCurrentPassword.requestFocus();
        } else if (TextUtils.isEmpty(newPassword)) {
            msg = "Please make sure you have provide the new password!";
            txtNewPassword.requestFocus();
        } else if (!confirmNewPassword.equals(newPassword)) {
            msg = "The new password and confirm new password do not match!";
            txtConfirmNewPassword.requestFocus();
        }
       /* else if (!validate(newPassword)){
            msg = "Please make sure you have provided the current password! ";
            txtNewPassword.requestFocus();
        }
        else if (!validate(confirmNewPassword)){
            msg = "Please make sure you have provided the current password! ";
            txtNewPassword.requestFocus();
        }*/
        return msg;
    }

    private void logout() {
        pcFunctionService.logout(logOutDelegate);
    }

    private void checkToChangePassword() {
        currentPassword = txtCurrentPassword.getText().toString().trim();
        newPassword = txtNewPassword.getText().toString().trim();
        confirmNewPassword = txtConfirmNewPassword.getText().toString().trim();

        String msg = validateFields();
        if (TextUtils.isEmpty(msg)) {
            changePassword();
        } else {
            showAlertDialog(msg);
        }
    }

    private void changePassword() {
        ChangePasswordParam param = new ChangePasswordParam(currentPassword, newPassword, confirmNewPassword);
        pcFunctionService.changePassword(param, changePasswordDelegate);
    }

    private void gotoLoginScreen() {

        getPCApplication().getAppPreferences().edit().putString(AppConstant.PREF_KEY_PASSWORD, newPassword).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ChangePasswordActivity.KEY_NEW_PASSWORD, newPassword);
        startActivity(intent);
        finish();
    }

    private static class ChangePasswordDelegate extends ForegroundTaskDelegate<Boolean> {

        public ChangePasswordDelegate(ChangePasswordActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            ChangePasswordActivity activity = (ChangePasswordActivity) activityWeakReference.get();
            if (throwable == null && result &&
                    activity != null &&
                    !activity.isDestroyed() &&
                    !activity.isFinishing()) {
                activity.logout();
            }
        }
    }

    private class LogoutDelegate extends ForegroundTaskDelegate<Boolean> {

        public LogoutDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean aBoolean, Throwable throwable) {
            super.onPostExecute(aBoolean, throwable);
            final ChangePasswordActivity activity = (ChangePasswordActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                final Toast toast = Toast.makeText(getApplicationContext(), "Please login again with your new credentials", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                        activity.gotoLoginScreen();
                    }
                }, 4000);


            }
        }
    }
}
