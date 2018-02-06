package com.inverseapps.punchcard.fragments.viewemployee;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import butterknife.BindView;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public class FragmentViewVehicle extends PCFragmentViewInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_view_vehicle;
    }

    @NonNull
    @BindView(R.id.lblCarMake)
    TextView lblCarMake;

    @NonNull
    @BindView(R.id.lblCarModel)
    TextView lblCarModel;

    @NonNull
    @BindView(R.id.lblCarLicPlateNum)
    TextView lblCarLicPlateNum;

    @NonNull
    @BindView(R.id.lblCarLicPlateState)
    TextView lblCarLicPlateState;

    @NonNull
    @BindView(R.id.lblDriverLicNum)
    TextView lblDriverLicNum;

    @NonNull
    @BindView(R.id.lblDriverLicState)
    TextView lblDriverLicState;

    @Override
    public void reload(Project project, Employee employee) {
        super.reload(project, employee);

        if (employee.getJdoc().getVehicle() != null) {
            lblCarMake.setText(String.format("Car Make: %s", employee.getJdoc().getVehicle().getCarMake()));
            lblCarModel.setText(String.format("Car Model: %s", employee.getJdoc().getVehicle().getCarModel()));
            lblCarLicPlateNum.setText(String.format("Car Lic. Plate Num: %s", employee.getJdoc().getVehicle().getLicPlateNumber()));
            lblCarLicPlateState.setText(String.format("Car Lic. Plate State: %s", employee.getJdoc().getVehicle().getLicPlateState()));
            lblDriverLicNum.setText(String.format("Driver Lic. Num: %s", employee.getJdoc().getVehicle().getDriversLicenseNumber()));
            lblDriverLicState.setText(String.format("Driver Lic. State: %s", employee.getJdoc().getVehicle().getDriversLicenseState()));
        }
    }
}
