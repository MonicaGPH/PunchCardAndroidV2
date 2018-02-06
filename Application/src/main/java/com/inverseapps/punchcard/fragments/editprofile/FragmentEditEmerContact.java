package com.inverseapps.punchcard.fragments.editprofile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.User;

import butterknife.BindView;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public class FragmentEditEmerContact extends PCFragmentEditInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_edit_emer_contact;
    }

    @NonNull
    @BindView(R.id.txtEmerName)
    EditText txtEmerName;

    @NonNull
    @BindView(R.id.txtEmerRelation)
    EditText txtEmerRelation;

    @NonNull
    @BindView(R.id.txtEmerEmail)
    EditText txtEmerEmail;

    @NonNull
    @BindView(R.id.txtEmerMobileNumber)
    EditText txtEmerMobileNumber;

    @NonNull
    @BindView(R.id.txtEmerHomeNumber)
    EditText txtEmerHomeNumber;

    @BindView(R.id.txtEmerWorkNumber)
    EditText txtEmerWorkNumber;

    @OnFocusChange({R.id.txtEmerName, R.id.txtEmerRelation, R.id.txtEmerEmail, R.id.txtEmerMobileNumber, R.id.txtEmerHomeNumber, R.id.txtEmerWorkNumber})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtEmerName.hasFocus() &&
                !txtEmerRelation.hasFocus() &&
                !txtEmerEmail.hasFocus() &&
                !txtEmerMobileNumber.hasFocus() &&
                !txtEmerHomeNumber.hasFocus() &&
                !txtEmerWorkNumber.hasFocus()) {
            pcActivity.hideKeyboard();
        }
    }

    @OnTextChanged(value = {R.id.txtEmerName, R.id.txtEmerRelation, R.id.txtEmerEmail, R.id.txtEmerMobileNumber, R.id.txtEmerHomeNumber, R.id.txtEmerWorkNumber},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(Editable editable) {

        boolean shouldNotify = false;

        if (editable == txtEmerName.getEditableText() &&
                !txtEmerName.getText().toString().equals(user.getJdoc().getEmerContact().getName())) {
            user.getJdoc().getEmerContact().setName(txtEmerName.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtEmerRelation.getEditableText() &&
                !txtEmerRelation.getText().toString().equals(user.getJdoc().getEmerContact().getRelation())) {
            user.getJdoc().getEmerContact().setRelation(txtEmerRelation.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtEmerEmail.getEditableText() &&
                !txtEmerEmail.getText().toString().equals(user.getJdoc().getEmerContact().getEmail())) {
            user.getJdoc().getEmerContact().setEmail(txtEmerEmail.getText().toString().trim());
            shouldNotify = true;
        }  else if (editable == txtEmerMobileNumber.getEditableText() &&
                !txtEmerMobileNumber.getText().toString().equals(user.getJdoc().getEmerContact().getMobileNumber())) {
            user.getJdoc().getEmerContact().setMobileNumber(txtEmerMobileNumber.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtEmerHomeNumber.getEditableText() &&
                !txtEmerHomeNumber.getText().toString().equals(user.getJdoc().getEmerContact().getHomeNumber())) {
            user.getJdoc().getEmerContact().setHomeNumber(txtEmerHomeNumber.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtEmerWorkNumber.getEditableText() &&
                !txtEmerWorkNumber.getText().toString().equals(user.getJdoc().getEmerContact().getWorkNumber())) {
            user.getJdoc().getEmerContact().setWorkNumber(txtEmerWorkNumber.getText().toString().trim());
            shouldNotify = true;
        }

        if (listener != null && didReload && shouldNotify) {
            listener.onChangedUserInfo(this, user);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtFirst = txtEmerName;
    }

    @Override
    public void reload(User user) {
        super.reload(user);

        if (user.getJdoc().getEmerContact() != null) {
            txtEmerName.setText(user.getJdoc().getEmerContact().getName());
            txtEmerRelation.setText(user.getJdoc().getEmerContact().getRelation());
            txtEmerEmail.setText(user.getJdoc().getEmerContact().getEmail());
            txtEmerMobileNumber.setText(user.getJdoc().getEmerContact().getMobileNumber());
            txtEmerHomeNumber.setText(user.getJdoc().getEmerContact().getHomeNumber());
            txtEmerWorkNumber.setText(user.getJdoc().getEmerContact().getWorkNumber());
        }

        didReload = true;
    }

    public void requestFocusEmailField() {
        txtEmerEmail.requestFocus();
    }

    public void requestFocusMobileNumberField() {
        txtEmerMobileNumber.requestFocus();
    }

    public void requestFocusHomeNumberField() {
        txtEmerHomeNumber.requestFocus();
    }

    public void requestFocusWorkNumberField() {
        txtEmerWorkNumber.requestFocus();
    }
}
