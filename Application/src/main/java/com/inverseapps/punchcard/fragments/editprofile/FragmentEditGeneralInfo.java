package com.inverseapps.punchcard.fragments.editprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.ui.EditProfileActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public class FragmentEditGeneralInfo extends PCFragmentEditInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_edit_general_info;
    }

    @NonNull
    @BindView(R.id.imgViewProfile)
    ImageView imgViewProfile;

    @NonNull
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;

    @NonNull
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;

    @NonNull
    @BindView(R.id.txtMiddleName)
    EditText txtMiddleName;

    @NonNull
    @BindView(R.id.txtLastName)
    EditText txtLastName;

    @NonNull
    @BindView(R.id.txtUsername)
    EditText txtUsername;

    @NonNull
    @BindView(R.id.txtEmail)
    EditText txtEmail;

    @NonNull
    @BindView(R.id.txtMobileNumber)
    EditText txtMobileNumber;

    @NonNull
    @BindView(R.id.txtHomeNumber)
    EditText txtHomeNumber;

    @NonNull
    @BindView(R.id.txtWorkNumber)
    EditText txtWorkNumber;

    @OnClick(R.id.imgViewProfile)
    void onClickedImageViewProfile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.btnChangePassword)
    void onClickedChangePasswordButton() {
        gotoChangePasswordScreen();
    }

    @OnFocusChange({R.id.txtFirstName,
            R.id.txtMiddleName,
            R.id.txtLastName,
            R.id.txtUsername,
            R.id.txtEmail,
            R.id.txtMobileNumber,
            R.id.txtHomeNumber,
            R.id.txtWorkNumber})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtFirstName.hasFocus() &&
                !txtMiddleName.hasFocus() &&
                !txtLastName.hasFocus() &&
                !txtUsername.hasFocus() &&
                !txtEmail.hasFocus() &&
                !txtMobileNumber.hasFocus() &&
                !txtHomeNumber.hasFocus() &&
                !txtWorkNumber.hasFocus()) {
            pcActivity.hideKeyboard();
        }
    }

    @OnTextChanged(value = {R.id.txtFirstName,
            R.id.txtLastName,
            R.id.txtEmail,
            R.id.txtMobileNumber,
            R.id.txtHomeNumber,
            R.id.txtWorkNumber},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(Editable editable) {

        boolean shouldNotify = false;

        if (editable == txtFirstName.getEditableText() &&
                !txtFirstName.getText().toString().equals(user.getFirst_name())) {
            user.setFirst_name(txtFirstName.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtMiddleName.getEditableText() &&
                !txtMiddleName.getText().toString().equals(user.getJdoc().getMiddleName()) &&
                        TextUtils.isEmpty(txtMiddleName.getText().toString())) {
            user.getJdoc().setMiddleName(txtMiddleName.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtLastName.getEditableText() &&
                !txtLastName.getText().toString().equals(user.getLast_name())) {
            user.setLast_name(txtLastName.getText().toString().trim());
            shouldNotify = true;
        }  else  if (editable == txtEmail.getEditableText() &&
                !txtEmail.getText().toString().equals(user.getEmail())) {
            user.setEmail(txtEmail.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtMobileNumber.getEditableText() &&
                !txtMobileNumber.getText().toString().equals(user.getMobileNumber())) {
            user.setMobileNumber(txtMobileNumber.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtHomeNumber.getEditableText() &&
                !txtHomeNumber.getText().toString().equals(user.getJdoc().getHomeNumber())) {
            user.getJdoc().setHomeNumber(txtHomeNumber.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtWorkNumber.getEditableText() &&
                !txtWorkNumber.getText().toString().equals(user.getJdoc().getWorkNumber())) {
            user.getJdoc().setWorkNumber(txtWorkNumber.getText().toString().trim());
            shouldNotify = true;
        }

        if (listener != null && didReload && shouldNotify) {
            listener.onChangedUserInfo(this, user);
        }
    }

    private Picasso picasso;

    private static final int PICK_IMAGE_REQUEST = 1;

    private LoadImageProfileDelegate loadImageProfileDelegate;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtFirst = txtFirstName;
        loadImageProfileDelegate = new LoadImageProfileDelegate(this);
        picasso = pcActivity.getPCApplication().getPcFunctionService().getPicasso();
        txtUsername.setEnabled(false);
        txtEmail.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        picasso.cancelRequest(imgViewProfile);
        super.onDestroyView();
    }

    @Override
    public void reload(User user) {
        super.reload(user);

        String avatarPath = pcActivity.getPCApplication().getPcFunctionService().avatarPath(user.getUniq_id());
        picasso.load(avatarPath)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgViewProfile);
        txtFirstName.setText(user.getFirst_name());
        txtMiddleName.setText(user.getJdoc().getMiddleName());
        txtLastName.setText(user.getLast_name());
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtMobileNumber.setText(user.getMobileNumber());
        txtHomeNumber.setText(user.getJdoc().getHomeNumber());
        txtWorkNumber.setText(user.getJdoc().getWorkNumber());

        didReload = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null &&
                data.getData() != null) {

            Uri uri = data.getData();
            picasso.load(uri).into(loadImageProfileDelegate);
        }
    }

    private void gotoChangePasswordScreen() {
        if (pcActivity instanceof EditProfileActivity) {
            EditProfileActivity activity = (EditProfileActivity)pcActivity;
            activity.checkGoToChangePasswordScreen();
        }
    }

    public void requestFocusFirstNameField() {
        txtFirstName.requestFocus();
    }

    public void requestFocusMiddleNameField() {
        txtMiddleName.requestFocus();
    }

    public void requestFocusLastNameField() {
        txtLastName.requestFocus();
    }

    public void requestFocusUsernameField() {
        txtUsername.requestFocus();
    }

    public void requestFocusEmailField() {
        txtEmail.requestFocus();
    }

    public void requestFocusMobileNumberField() {
        txtMobileNumber.requestFocus();
    }

    public void requestFocusHomeNumberField() {
        txtHomeNumber.requestFocus();
    }

    public void requestFocusWorkNumberField() {
        txtWorkNumber.requestFocus();
    }

    private static class LoadImageProfileDelegate implements Target {

        private final WeakReference<FragmentEditGeneralInfo> fragmentWeakReference;

        public LoadImageProfileDelegate(FragmentEditGeneralInfo fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            FragmentEditGeneralInfo fragment = fragmentWeakReference.get();
            if (bitmap != null &&
                    fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
                fragment.imgViewProfile.setImageBitmap(bitmap);
                if (fragment.listener != null) {
                    fragment.listener.onChangedUserAvatar(fragment, bitmap);
                }
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
//zzXQOXSRBEiUtuE8AikJYKwbHaxvSc0ojez9YXaGp1A.