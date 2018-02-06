package com.inverseapps.punchcard.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.fragments.viewemployee.FragmentViewCompany;
import com.inverseapps.punchcard.fragments.viewemployee.FragmentViewEmerContact;
import com.inverseapps.punchcard.fragments.viewemployee.FragmentViewGeneralInfo;
import com.inverseapps.punchcard.fragments.viewemployee.FragmentViewVehicle;
import com.inverseapps.punchcard.fragments.viewemployee.PCFragmentViewInfo;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;

import butterknife.BindView;

public class ViewEmployeeActivity extends PCActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_view_employee;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private PCFragmentViewInfo fragmentViewCompany;
    private PCFragmentViewInfo fragmentViewGeneralInfo;
    private PCFragmentViewInfo fragmentViewEmerContact;
    private PCFragmentViewInfo fragmentViewVehicle;

    public static final String KEY_PROJECT = "project";
    public static final String KEY_PROJECT_ADD = "project_add";
    public static final String KEY_PROJECT_CITY = "project_city";
    public static final String KEY_PROJECT_COUNTRY = "project_country";
    public static final String KEY_PROJECT_STATE = "project_state";
    public static final String KEY_PROJECT_ZIP = "project_zip";
    public static final String KEY_EMPLOYEE_ID = "employeeId";

    private Project project;
    private String employeeId;
    private Employee employee;
    private String addressString,zipString,stateString,countryString,cityString;
    private ForegroundTaskDelegate findEmployeeDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        project = getIntent().getParcelableExtra(KEY_PROJECT);
        addressString = getIntent().getStringExtra(KEY_PROJECT_ADD);
        cityString = getIntent().getStringExtra(KEY_PROJECT_CITY);
        countryString = getIntent().getStringExtra(KEY_PROJECT_COUNTRY);
        stateString = getIntent().getStringExtra(KEY_PROJECT_STATE);
        zipString = getIntent().getStringExtra(KEY_PROJECT_ZIP);
        employeeId = getIntent().getStringExtra(KEY_EMPLOYEE_ID);
        if (savedInstanceState != null) {
            project = getIntent().getParcelableExtra(KEY_PROJECT);
            employeeId = savedInstanceState.getString(KEY_EMPLOYEE_ID);
        }

        findEmployeeDelegate = new FindEmployeeDelegate(this);
        listOfForegroundTaskDelegates.add(findEmployeeDelegate);

        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pcFunctionService.findEmployee(employeeId, findEmployeeDelegate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pcFunctionService.findEmployee(employeeId, findEmployeeDelegate);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_PROJECT, project);
        outState.putString(KEY_EMPLOYEE_ID, employeeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof FragmentViewCompany) {
            fragmentViewCompany = (PCFragmentViewInfo)fragment;
        } else if (fragment instanceof FragmentViewGeneralInfo) {
            fragmentViewGeneralInfo = (PCFragmentViewInfo)fragment;
        } else if (fragment instanceof FragmentViewEmerContact) {
            fragmentViewEmerContact = (PCFragmentViewInfo)fragment;
        } else if (fragment instanceof FragmentViewVehicle) {
            fragmentViewVehicle = (PCFragmentViewInfo)fragment;
        }
    }

    private void reload() {
        getSupportActionBar().setTitle(employee.getName());
        fragmentViewCompany.reload(project, employee);
        fragmentViewGeneralInfo.reload(project,addressString,cityString,countryString,stateString,zipString, employee);
        fragmentViewEmerContact.reload(project, employee);
        fragmentViewVehicle.reload(project, employee);
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

    private static class FindEmployeeDelegate extends ForegroundTaskDelegate<Employee> {

        public FindEmployeeDelegate(ViewEmployeeActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Employee employee, Throwable throwable) {
            super.onPostExecute(employee, throwable);
            ViewEmployeeActivity activity = (ViewEmployeeActivity)activityWeakReference.get();
            if (throwable == null &&
                    employee != null &&
                    activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.employee = employee;
                activity.reload();
                activity.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
