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
 * Created by asus on 15-Jan-18.
 */

public class FragmentEditSecondaryAddress extends PCFragmentEditInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_edit_secondaryaddress;
    }

    @NonNull
    @BindView(R.id.txtAddress1)
    EditText txtAddress1;

    @NonNull
    @BindView(R.id.txtAddress2)
    EditText txtAddress2;

    @NonNull
    @BindView(R.id.txtCity)
    EditText txtCity;

    @NonNull
    @BindView(R.id.txtState)
    EditText txtState;

    @NonNull
    @BindView(R.id.txtZip)
    EditText txtZip;

    @OnFocusChange({R.id.txtAddress1, R.id.txtAddress2, R.id.txtCity, R.id.txtState, R.id.txtZip})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtAddress1.hasFocus() &&
                !txtAddress2.hasFocus() &&
                !txtCity.hasFocus() &&
                !txtState.hasFocus() &&
                !txtZip.hasFocus()) {
            pcActivity.hideKeyboard();
        }
    }

    @OnTextChanged(value = {R.id.txtAddress1, R.id.txtAddress2, R.id.txtCity, R.id.txtState, R.id.txtZip},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(Editable editable) {

        boolean shouldNotify = false;

        if (editable == txtAddress1.getEditableText() &&
                !txtAddress1.getText().toString().equals(user.getJdoc().getSecondaryAddress().getAddress1())) {
            user.getJdoc().getSecondaryAddress().setAddress1(txtAddress1.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtAddress2.getEditableText() &&
                !txtAddress2.getText().toString().equals(user.getJdoc().getSecondaryAddress().getAddress2())) {
            user.getJdoc().getSecondaryAddress().setAddress2(txtAddress2.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtCity.getEditableText() &&
                !txtCity.getText().toString().equals(user.getJdoc().getSecondaryAddress().getCity())) {
            user.getJdoc().getSecondaryAddress().setCity(txtCity.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtState.getEditableText() &&
                !txtState.getText().toString().equals(user.getJdoc().getSecondaryAddress().getState())) {
            user.getJdoc().getSecondaryAddress().setState(txtState.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtZip.getEditableText() &&
                !txtZip.getText().toString().equals(user.getJdoc().getSecondaryAddress().getZip())) {
            user.getJdoc().getSecondaryAddress().setZip(txtZip.getText().toString().trim());
            shouldNotify = true;
        }

        if (listener != null && didReload && shouldNotify) {
            listener.onChangedUserInfo(this, user);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtFirst = txtAddress1;
    }

    @Override
    public void reload(User user) {
        super.reload(user);

        if (user.getJdoc().getSecondaryAddress() != null) {
            txtAddress1.setText(user.getJdoc().getSecondaryAddress().getAddress1());
            txtAddress2.setText(user.getJdoc().getSecondaryAddress().getAddress2());
            txtCity.setText(user.getJdoc().getSecondaryAddress().getCity());
            txtState.setText(user.getJdoc().getSecondaryAddress().getState());
            txtZip.setText(user.getJdoc().getSecondaryAddress().getZip());
        }

        didReload = true;
    }
}
