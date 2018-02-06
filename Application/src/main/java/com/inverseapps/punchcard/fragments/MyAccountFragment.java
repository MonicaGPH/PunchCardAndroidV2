package com.inverseapps.punchcard.fragments;


import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;


import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.ui.BillingStatusActivity;

import com.inverseapps.punchcard.ui.EditProfileActivity;
import com.inverseapps.punchcard.ui.PlanDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAccountFragment extends PCFragment {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.fragment_my_account;
    }

    private User user;

    @BindView(R.id.myAcountHolder)
    RelativeLayout mMyAccountHolder;

    @BindView(R.id.myBillingStatus)
    RelativeLayout myBillingStatus;

    @BindView(R.id.txtdays)
    TextView txtdays;

    @BindView(R.id.trialdaysleft)
    RelativeLayout trialdaysleft;

    @BindView(R.id.planDetailHolder)
    RelativeLayout planDetailHolder;

    @OnClick(R.id.planDetailHolder)
    void onClickedPlanDetailHolder() {
        gotoplanDetailscreen();
    }

    private void gotoplanDetailscreen() {
        Intent intent = new Intent(getContext(), PlanDetailActivity.class);
        getContext().startActivity(intent);
    }

    @OnClick(R.id.myAcountHolder)
    void onClickedMyAccountHolder() {
        gotoEditProfileScreen();
    }

    @OnClick(R.id.myBillingStatus)
    void onCLickedMyBillingStatus() {
        gotoBillingScreen();
    }

    private void gotoBillingScreen() {
        Intent intent = new Intent(getContext(), BillingStatusActivity.class);
        getContext().startActivity(intent);
    }

    private void gotoEditProfileScreen() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = pcActivity.getPCApplication().getPcFunctionService().getInternalStoredUser();
        // role :: clientadmin
        if (user.getRole().equalsIgnoreCase("ClientAdmin")) {
            // version ::trial
            if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {

                trialdaysleft.setVisibility(View.VISIBLE);


                String currentDate = pcActivity.getCurrentDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = pcActivity.getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
                try {
                    if (!format.parse(currentDate).after(format.parse(endDate))) {
                        Date date1 = format.parse(currentDate);
                        Date date2 = format.parse(endDate);
                        long diff = date2.getTime() - date1.getTime();

                        txtdays.setText("" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // version :: base/standard/enterprise/premium
            else {
                trialdaysleft.setVisibility(View.GONE);

            }


        }
        // role :: user/superuser/admin
        else {



                trialdaysleft.setVisibility(View.GONE);
                myBillingStatus.setVisibility(View.GONE);
                planDetailHolder.setVisibility(View.GONE);

        }

    }


}


