package com.inverseapps.punchcard.fragments.viewemployee;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.ui.BadgeActivity;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Inverse, LLC on 10/22/16.
 */

public class FragmentViewGeneralInfo extends PCFragmentViewInfo {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_view_general_info;
    }

    @NonNull
    @BindView(R.id.imgViewEmployee)
    ImageView imgViewEmployee;

    @NonNull
    @BindView(R.id.lblName)
    TextView lblName;

    @NonNull
    @BindView(R.id.lblUsername)
    TextView lblUsername;

    @NonNull
    @BindView(R.id.lblEmail)
    TextView lblEmail;

    @NonNull
    @BindView(R.id.lblMobileNumber)
    TextView lblMobileNumber;

    @NonNull
    @BindView(R.id.lblHomeNumber)
    TextView lblHomeNumber;

    @NonNull
    @BindView(R.id.lblWorkNumber)
    TextView lblWorkNumber;

    @NonNull
    @BindView(R.id.lblAddress1_primary)
    TextView lblAddress1_primary;

    @NonNull
    @BindView(R.id.lblAddress2_primary)
    TextView lblAddress2_primary;

    @NonNull
    @BindView(R.id.lblCity_primary)
    TextView lblCity_primary;

    @NonNull
    @BindView(R.id.lblState_primary)
    TextView lblState_primary;

    @NonNull
    @BindView(R.id.lblZipCode_primary)
    TextView lblZipCode_primary;
    @NonNull
    @BindView(R.id.lblAddress1_secondary)
    TextView lblAddress1_secondary;

    @NonNull
    @BindView(R.id.lblAddress2_secondary)
    TextView lblAddress2_secondary;

    @NonNull
    @BindView(R.id.lblCity_secondary)
    TextView lblCity_secondary;

    @NonNull
    @BindView(R.id.lblState_secondary)
    TextView lblState_secondary;

    @NonNull
    @BindView(R.id.lblZipCode_secondary)
    TextView lblZipCode_secondary;

    @NonNull
    @BindView(R.id.btnViewBadge)
    Button btnViewBadge;

    @OnClick(R.id.btnViewBadge)
    void onClickedViewBadgeButton() {
        gotoProjectBadgeScreen();
    }

    private Picasso picasso;
    private String addressString, zipString, stateString, countryString, cityString;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        picasso = pcActivity.getPCApplication().getPcFunctionService().getPicasso();
    }

    @Override
    public void onDestroyView() {
        picasso.cancelRequest(imgViewEmployee);
        super.onDestroyView();
    }

    @Override
    public void reload(Project project, String projectAddress, String projectCity, String projectCountry, String projectState, String projectZip, Employee employee) {
        super.reload(project, projectAddress, projectCity, projectCountry, projectState, projectZip, employee);
        addressString = projectAddress;
        cityString = projectCity;
        countryString = projectCountry;
        stateString = projectState;
        zipString = projectZip;
        String avatarPath = pcActivity.getPCApplication().getPcFunctionService().avatarPath(employee.getUniq_id());
        picasso.load(avatarPath)
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgViewEmployee);

        lblName.setText(String.format("Name: %s", employee.getName()));
        lblUsername.setText(String.format("Username: %s", employee.getUsername()));
        lblEmail.setText(String.format("Email: %s", employee.getEmail()));
        lblMobileNumber.setText(String.format("Mobile Number: %s", employee.getMobileNumber()));
        lblHomeNumber.setText(String.format("Home Number: %s", employee.getJdoc().getHomeNumber()));
        lblWorkNumber.setText(String.format("Work Number: %s", employee.getJdoc().getWorkNumber()));
        lblAddress1_primary.setText(String.format("Address 1: %s", employee.getJdoc().getPrimaryAddress().getAddress1()));
        lblAddress2_primary.setText(String.format("Address 2: %s", employee.getJdoc().getPrimaryAddress().getAddress2()));
        lblAddress1_secondary.setText(String.format("Address 1: %s", employee.getJdoc().getSecondaryAddress().getAddress1()));
        lblAddress2_secondary.setText(String.format("Address 2: %s", employee.getJdoc().getSecondaryAddress().getAddress2()));
        lblCity_primary.setText(String.format("City: %s", employee.getJdoc().getPrimaryAddress().getCity()));
        lblCity_secondary.setText(String.format("City: %s", employee.getJdoc().getSecondaryAddress().getCity()));
        lblState_primary.setText(String.format("State: %s", employee.getJdoc().getPrimaryAddress().getState()));
        lblState_secondary.setText(String.format("State: %s", employee.getJdoc().getSecondaryAddress().getState()));
        lblZipCode_primary.setText(String.format("Zip Code: %s", employee.getJdoc().getPrimaryAddress().getZip()));
        lblZipCode_secondary.setText(String.format("Zip Code: %s", employee.getJdoc().getSecondaryAddress().getZip()));
    }


    private void gotoProjectBadgeScreen() {

        if (employee != null && project != null) {
            String userUniqId = employee.getUniq_id();
            String projectUniqId = project.getUniq_id();
            String projectLogo = project.getLogo();
            String projectAddress = addressString;
            String projectcity = cityString;
            String projectstate = stateString;
            String projectzip = zipString;

            String projectcountry = project.getCountry();

            Intent intent = new Intent(getContext(), BadgeActivity.class);
            intent.putExtra(BadgeActivity.KEY_USER_UNIQ_ID, userUniqId);
            intent.putExtra(BadgeActivity.KEY_PROJECT_UNIQ_ID, projectUniqId);
            intent.putExtra(BadgeActivity.KEY_PROJECT_LOGO, projectLogo);
            intent.putExtra(BadgeActivity.KEY_ADDRESS, projectAddress);
            intent.putExtra(BadgeActivity.KEY_CITY, projectcity);
            intent.putExtra(BadgeActivity.KEY_STATE, projectstate);
            intent.putExtra(BadgeActivity.KEY_ZIP, projectzip);
            intent.putExtra(BadgeActivity.KEY_COUNTRY, projectcountry);
            intent.putExtra(BadgeActivity.KEY_IS_MY_BADGE, false);

            startActivity(intent);
        }
    }
}
