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

public class FragmentEditVehicle extends PCFragmentEditInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_edit_vehicle;
    }

    @NonNull
    @BindView(R.id.txtCarMake)
    EditText txtCarMake;

    @NonNull
    @BindView(R.id.txtCarModel)
    EditText txtCarModel;

    @NonNull
    @BindView(R.id.txtCarLicPlateNum)
    EditText txtCarLicPlateNum;

    @NonNull
    @BindView(R.id.txtCarLicPlateState)
    EditText txtCarLicPlateState;

    @NonNull
    @BindView(R.id.txtDriverLicNum)
    EditText txtDriverLicNum;

    @NonNull
    @BindView(R.id.txtDriverLicState)
    EditText txtDriverLicState;

    @OnFocusChange({R.id.txtCarMake, R.id.txtCarModel, R.id.txtCarLicPlateNum, R.id.txtCarLicPlateState, R.id.txtDriverLicNum, R.id.txtDriverLicState})
    public void onFocusChange(View view, boolean hasFocus) {
        if (!txtCarMake.hasFocus() &&
                !txtCarModel.hasFocus() &&
                !txtCarLicPlateNum.hasFocus() &&
                !txtCarLicPlateState.hasFocus() &&
                !txtDriverLicNum.hasFocus() &&
                !txtDriverLicState.hasFocus()) {
            pcActivity.hideKeyboard();
        }
    }

    @OnTextChanged(value = {R.id.txtCarMake, R.id.txtCarModel, R.id.txtCarLicPlateNum, R.id.txtCarLicPlateState, R.id.txtDriverLicNum, R.id.txtDriverLicState},
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(Editable editable) {

        boolean shouldNotify = false;

        if (editable == txtCarMake.getEditableText() &&
                !txtCarMake.getText().toString().equals(user.getJdoc().getVehicle().getCarMake())) {
            user.getJdoc().getVehicle().setCarMake(txtCarMake.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtCarModel.getEditableText() &&
                !txtCarModel.getText().toString().equals(user.getJdoc().getVehicle().getCarModel())) {
            user.getJdoc().getVehicle().setCarModel(txtCarModel.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtCarLicPlateNum.getEditableText() &&
                !txtCarLicPlateNum.getText().toString().equals(user.getJdoc().getVehicle().getLicPlateNumber())) {
            user.getJdoc().getVehicle().setLicPlateNumber(txtCarLicPlateNum.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtCarLicPlateState.getEditableText() &&
                !txtCarLicPlateState.getText().toString().equals(user.getJdoc().getVehicle().getLicPlateState())) {
            user.getJdoc().getVehicle().setLicPlateState(txtCarLicPlateState.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtDriverLicNum.getEditableText() &&
                !txtDriverLicNum.getText().toString().equals(user.getJdoc().getVehicle().getDriversLicenseNumber())) {
            user.getJdoc().getVehicle().setDriversLicenseNumber(txtDriverLicNum.getText().toString().trim());
            shouldNotify = true;
        } else if (editable == txtDriverLicState.getEditableText() &&
                !txtDriverLicState.getText().toString().equals(user.getJdoc().getVehicle().getDriversLicenseState())) {
            user.getJdoc().getVehicle().setDriversLicenseState(txtDriverLicState.getText().toString().trim());
            shouldNotify = true;
        }

        if (listener != null && didReload && shouldNotify) {
            listener.onChangedUserInfo(this, user);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtFirst = txtCarMake;
    }

    @Override
    public void reload(User user) {
        super.reload(user);

        if (user.getJdoc().getVehicle() != null) {
            txtCarMake.setText(user.getJdoc().getVehicle().getCarMake());
            txtCarModel.setText(user.getJdoc().getVehicle().getCarModel());
            txtCarLicPlateNum.setText(user.getJdoc().getVehicle().getLicPlateNumber());
            txtCarLicPlateState.setText(user.getJdoc().getVehicle().getLicPlateState());
            txtDriverLicNum.setText(user.getJdoc().getVehicle().getDriversLicenseNumber());
            txtDriverLicState.setText(user.getJdoc().getVehicle().getDriversLicenseState());
        }

        didReload = true;
    }
}
