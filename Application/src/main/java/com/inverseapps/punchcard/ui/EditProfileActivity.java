package com.inverseapps.punchcard.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.fragments.editprofile.FragmentEditPrimaryAddress;
import com.inverseapps.punchcard.fragments.editprofile.FragmentEditEmerContact;
import com.inverseapps.punchcard.fragments.editprofile.FragmentEditGeneralInfo;
import com.inverseapps.punchcard.fragments.editprofile.FragmentEditSecondaryAddress;
import com.inverseapps.punchcard.fragments.editprofile.FragmentEditVehicle;
import com.inverseapps.punchcard.fragments.editprofile.PCFragmentEditInfo;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.UpdateAvatarParam;
import com.inverseapps.punchcard.model.param.UpdateUserParam;
import com.inverseapps.punchcard.ui.dialog.PCDialogFragmentListener;
import com.inverseapps.punchcard.utils.Utilities;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProfileActivity extends PCActivity implements
        PCFragmentEditInfo.OnChangedUserListener,
        PCDialogFragmentListener, SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_edit_profile;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.btnSave)
    Button btnSave;

    @OnClick(R.id.btnSave)
    void onClickedSaveButton() {
        updateUserDelegate.setShouldGotoChangePasswordScreen(false);
        checkToUpdateProfile();
    }

    private FragmentEditGeneralInfo fragmentEditGeneralInfo;
    private PCFragmentEditInfo fragmentEditPrimaryAddress;
    private PCFragmentEditInfo fragmentEditSecondaryAddress;
    private FragmentEditEmerContact fragmentEditEmerContact;
    private PCFragmentEditInfo fragmentEditVehicle;

    private static final String KEY_USER = "user";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE_NUMBER = "mobileNumber";

    private User user;
    private Bitmap avatar;
    private String email;
    private String mobileNumber;

    private ForegroundTaskDelegate findUserDelegate;
    private ForegroundTaskDelegate updateAvatarDelegate;
    private UpdateUserDelegate updateUserDelegate;
    private ForegroundTaskDelegate logOutDelegate;

    private final String TAG_NOT_YET_SAVED_DIALOG = "notYetSavedDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");

        btnSave.setVisibility(View.GONE);

        user = pcFunctionService.getInternalStoredUser();
        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable(KEY_USER);
            avatar = savedInstanceState.getParcelable(KEY_AVATAR);
            email = savedInstanceState.getString(KEY_EMAIL);
            mobileNumber = savedInstanceState.getString(KEY_MOBILE_NUMBER);
        }

        if (user != null) {
            reload();
        }

        findUserDelegate = new FindUserDelegate(this);
        listOfForegroundTaskDelegates.add(findUserDelegate);

        updateAvatarDelegate = new UpdateAvatarDelegate(this);
        listOfForegroundTaskDelegates.add(updateAvatarDelegate);

        updateUserDelegate = new UpdateUserDelegate(this);
        listOfForegroundTaskDelegates.add(updateUserDelegate);

        logOutDelegate = new LogoutDelegate(this);
        listOfForegroundTaskDelegates.add(logOutDelegate);

        pcFunctionService.findUser(findUserDelegate);

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcFunctionService.findUser(findUserDelegate);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_USER, user);
        outState.putParcelable(KEY_AVATAR, avatar);
        outState.putString(KEY_EMAIL, email);
        outState.putString(KEY_MOBILE_NUMBER, mobileNumber);
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

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof FragmentEditGeneralInfo) {
            fragmentEditGeneralInfo = (FragmentEditGeneralInfo) fragment;
        } else if (fragment instanceof FragmentEditPrimaryAddress) {
            fragmentEditPrimaryAddress = (FragmentEditPrimaryAddress) fragment;
        } else if (fragment instanceof FragmentEditSecondaryAddress) {
            fragmentEditSecondaryAddress = (FragmentEditSecondaryAddress) fragment;
        } else if (fragment instanceof FragmentEditEmerContact) {
            fragmentEditEmerContact = (FragmentEditEmerContact) fragment;
        } else if (fragment instanceof FragmentEditVehicle) {
            fragmentEditVehicle = (FragmentEditVehicle) fragment;
        }
    }

    private void reload() {
        fragmentEditGeneralInfo.reload(user);
        fragmentEditPrimaryAddress.reload(user);
        fragmentEditSecondaryAddress.reload(user);
        fragmentEditEmerContact.reload(user);
        fragmentEditVehicle.reload(user);
        email = user.getEmail();
        mobileNumber = user.getMobileNumber();
    }

    @Override
    public void onChangedUserInfo(PCFragmentEditInfo fragment, User user) {
        btnSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangedUserAvatar(PCFragmentEditInfo fragment, Bitmap avatar) {
        btnSave.setVisibility(View.VISIBLE);
        this.avatar = avatar;
    }

    public void checkGoToChangePasswordScreen() {
        if (btnSave.getVisibility() != View.VISIBLE) {
            gotoChangePasswordScreen();
        } else {
            showAlertDialog(TAG_NOT_YET_SAVED_DIALOG, "You have not yet saved your changes?", "Save", "Cancel", "Continue");
        }
    }

    private void gotoChangePasswordScreen() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private String validateFields() {
        String msg = "";
        if (TextUtils.isEmpty(user.getFirst_name())) {
            msg = "Your first name cannot be blank.";
            fragmentEditGeneralInfo.requestFocusFirstNameField();
        } else if (TextUtils.isEmpty(user.getLast_name())) {
            msg = "Your last name cannot be blank.";
            fragmentEditGeneralInfo.requestFocusLastNameField();
        } else if (TextUtils.isEmpty(user.getUsername())) {
            msg = "Your username cannot be blank.";
            fragmentEditGeneralInfo.requestFocusUsernameField();
        } else if (!Utilities.isValidEmail(user.getEmail())) {
            msg = "The email must be a valid email address.";
            if (TextUtils.isEmpty(user.getEmail())) {
                msg = "Your email cannot be blank.";
            }
            fragmentEditGeneralInfo.requestFocusEmailField();
        } else if (user.getMobileNumber().length() != 10) {
            msg = "The mobile number must be 10 digits.";
            if (TextUtils.isEmpty(user.getMobileNumber())) {
                msg = "Your mobile number cannot be blank.";
            }
            fragmentEditGeneralInfo.requestFocusMobileNumberField();
        } else if (!TextUtils.isEmpty(user.getJdoc().getHomeNumber()) &&
                user.getJdoc().getHomeNumber().length() != 10) {
            msg = "The home number must be 10 digits.";
            fragmentEditGeneralInfo.requestFocusHomeNumberField();
        } else if (!TextUtils.isEmpty(user.getJdoc().getWorkNumber()) &&
                user.getJdoc().getWorkNumber().length() != 10) {
            msg = "The work number must be 10 digits.";
            fragmentEditGeneralInfo.requestFocusWorkNumberField();
        }
        if (!TextUtils.isEmpty(user.getJdoc().getEmerContact().getEmail()) &&
                !Utilities.isValidEmail(user.getJdoc().getEmerContact().getEmail())) {
            msg = "The emer email must be a valid email address.";
            fragmentEditEmerContact.requestFocusEmailField();
        } else if (!TextUtils.isEmpty(user.getJdoc().getEmerContact().getMobileNumber()) &&
                user.getJdoc().getEmerContact().getMobileNumber().length() != 10) {
            msg = "The emer mobile number must be 10 digits.";
            fragmentEditEmerContact.requestFocusMobileNumberField();
        } else if (!TextUtils.isEmpty(user.getJdoc().getEmerContact().getHomeNumber()) &&
                user.getJdoc().getEmerContact().getHomeNumber().length() != 10) {
            msg = "The emer home number must be 10 digits.";
            fragmentEditEmerContact.requestFocusHomeNumberField();
        } else if (!TextUtils.isEmpty(user.getJdoc().getEmerContact().getWorkNumber()) &&
                user.getJdoc().getEmerContact().getWorkNumber().length() != 10) {
            msg = "The emer work number must be 10 digits.";
            fragmentEditEmerContact.requestFocusWorkNumberField();
        }
        return msg;
    }

    private void checkToUpdateProfile() {
        String msg = validateFields();
        if (!TextUtils.isEmpty(msg)) {
            showAlertDialog(msg);
        } else {
            if (avatar != null) {
                updateAvatar();
            } else {
                updateUser();
            }
        }
    }

    private void updateUser() {
        UpdateUserParam param = new UpdateUserParam(user);
        pcFunctionService.updateUser(param, updateUserDelegate);
    }

    private void updateAvatar() {
        Bitmap image = Utilities.cropToSquare(this, avatar);
        String imageBase64 = Utilities.bitmapToBase64(image);
        UpdateAvatarParam param = new UpdateAvatarParam(imageBase64);
        pcFunctionService.updateAvatar(param, updateAvatarDelegate);
    }

    @Override
    public void onDialogPositiveClick(AppCompatDialogFragment dialog) {
        if (dialog.getTag().equals(TAG_NOT_YET_SAVED_DIALOG)) {
            updateUserDelegate.setShouldGotoChangePasswordScreen(true);
            checkToUpdateProfile();
        }
    }

    @Override
    public void onDialogNegativeClick(AppCompatDialogFragment dialog) {
        // Do nothing
    }

    @Override
    public void onDialogNeutralClick(AppCompatDialogFragment dialog) {
        if (dialog.getTag().equals(TAG_NOT_YET_SAVED_DIALOG)) {
            gotoChangePasswordScreen();
        }
    }

    private static class FindUserDelegate extends ForegroundTaskDelegate<User> {

        public FindUserDelegate(EditProfileActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            EditProfileActivity activity = (EditProfileActivity) activityWeakReference.get();
            if (throwable == null
                    && user != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.user = user;
                activity.reload();
                activity.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private static class UpdateAvatarDelegate extends ForegroundTaskDelegate<Boolean> {

        private UpdateAvatarDelegate(EditProfileActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            EditProfileActivity activity = (EditProfileActivity) activityWeakReference.get();
            if (throwable == null &&
                    result &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.avatar = null;
                activity.btnSave.setVisibility(View.GONE);
                activity.updateUser();
            }
        }
    }

    private static class UpdateUserDelegate extends ForegroundTaskDelegate<User> {

        private boolean shouldGotoChangePasswordScreen;

        public UpdateUserDelegate(EditProfileActivity activity) {
            super(activity);
        }

        public void setShouldGotoChangePasswordScreen(boolean shouldGotoChangePasswordScreen) {
            this.shouldGotoChangePasswordScreen = shouldGotoChangePasswordScreen;
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            EditProfileActivity activity = (EditProfileActivity) activityWeakReference.get();
            if (throwable == null &&
                    user != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {

                activity.btnSave.setVisibility(View.GONE);
                if (!activity.email.equals(user.getEmail()) ||
                        !activity.mobileNumber.equals(user.getMobileNumber())) {
                    activity.logout();
                } else {
                    Toast.makeText(activity, "Update succeeded!", Toast.LENGTH_SHORT).show();
                    activity.user = user;
                    if (shouldGotoChangePasswordScreen) {
                        activity.gotoChangePasswordScreen();
                    }
                }
            }
        }
    }

    private void logout() {
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
            EditProfileActivity activity = (EditProfileActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                Toast.makeText(activity, "Please login again with your new credentials", Toast.LENGTH_SHORT).show();
                activity.gotoLoginScreen();
            }
        }
    }
}
