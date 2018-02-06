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

public class FragmentViewCompany extends PCFragmentViewInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_view_company_info;
    }

    @NonNull
    @BindView(R.id.lblEmployeeID)
    TextView lblEmployeeID;

    @NonNull
    @BindView(R.id.lblCompany)
    TextView lblCompany;

    @NonNull
    @BindView(R.id.lblDepartment)
    TextView lblDepartment;

    @NonNull
    @BindView(R.id.lblTrade)
    TextView lblTrade;

    @NonNull
    @BindView(R.id.lblJobTitle)
    TextView lblJobTitle;

    @Override
    public void reload(Project project, Employee employee) {
        super.reload(project, employee);

        lblEmployeeID.setText(String.format("Employee ID: %s", employee.getId()));
        lblCompany.setText(String.format("Company: %s", project.getClient().getName()));
        lblDepartment.setText(String.format("Department: %s", employee.getJdoc().getDepartment()));
        lblTrade.setText(String.format("Trade: %s", employee.getJdoc().getTrade()));
        lblJobTitle.setText(String.format("Title: %s", employee.getJdoc().getJob_title()));
    }
}
