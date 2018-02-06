package com.inverseapps.punchcard.fragments.viewemployee;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.fragments.PCFragment;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public abstract class PCFragmentViewInfo extends PCFragment {

    @NonNull
    @BindView(R.id.btnHeader)
    Button btnHeader;

    @NonNull
    @BindView(R.id.content)
    ViewGroup content;

    @OnClick(R.id.btnHeader)
    void onClickedHeaderButton() {

        if (content.getVisibility() == View.VISIBLE) {
            btnHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
            content.setVisibility(View.GONE);
        } else if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            btnHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0);
        }
    }

    public static final String KEY_PROJECT = "project";
    public static final String KEY_EMPLOYEE = "employee";

    protected Project project;
    protected  String projectAddress,projectCity,projectCountry,projectState,projectZip;
    protected Employee employee;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        project = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredProject();
        if (savedInstanceState != null) {
            project = savedInstanceState.getParcelable(KEY_PROJECT);
            employee = savedInstanceState.getParcelable(KEY_EMPLOYEE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putParcelable(KEY_EMPLOYEE, employee);

        super.onSaveInstanceState(outState);
    }

    public void reload(Project project, Employee employee) {
        this.project = project;
        this.employee = employee;
    }
    public void reload(Project project,String projectAddress,String projectCity, String projectCountry, String projectState, String projectZip, Employee employee) {
        this.project = project;
        this.projectAddress = projectAddress;
        this.projectCity = projectCity;
        this.projectCountry = projectCountry;
        this.projectState = projectState;
        this.projectZip = projectZip;
        this.employee = employee;
    }
}
