package com.inverseapps.punchcard.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.R;


import com.inverseapps.punchcard.model.PresentBilling;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.BillingParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import butterknife.BindView;

/**
 * Created by asus on 05-Dec-17.
 */

public class PresentBillingActivity extends PCActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_present_billing;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    @BindView(R.id.txttotalpunchesLabel)
    TextView txttotalpunchesLabel;

    @NonNull
    @BindView(R.id.txt_total_punchess)
    TextView txt_total_punches;

    @NonNull
    @BindView(R.id.txt_total_amount)
    TextView txt_total_amount;

    @NonNull
    @BindView(R.id.txt_date)
    TextView txt_date;

    @NonNull
    @BindView(R.id.txt_daysleft)
    TextView txt_daysleft;

    @NonNull
    @BindView(R.id.lineardaysleft)
    LinearLayout lineardaysleft;

    private User user;
    private BillingParam param;
    private String price;

    private ForegroundTaskDelegate findpresentBillingDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Current Billing");

        findpresentBillingDelegate = new FindPastBillingDelegate(this);
        listOfForegroundTaskDelegates.add(findpresentBillingDelegate);


        user = getPCApplication().getPcFunctionService().getInternalStoredUser();
        String plan = user.getClient().getPlan_test();

        switch (plan) {

            case "base":
                price = String.valueOf(1);
                break;
            case "standard":
                price = String.valueOf(2);
                break;
            case "premium":
                price = String.valueOf(3);
                break;
            case "enterprise":
                price = String.valueOf(4);
                break;
            case "trial":
                price = String.valueOf(0);
                break;
        }

        if (user.getRole().equalsIgnoreCase("clientadmin")) {
            // version :: trial
            if (user.getClient().getPlan_test().equalsIgnoreCase("trial")) {
                lineardaysleft.setVisibility(View.VISIBLE);
                String currentDate = getCurrentDate();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = getPCApplication().getAppPreferences().getString(AppConstant.PREF_KEY_END_DATE, "endDate");
                try {
                    if (!format.parse(currentDate).after(format.parse(endDate))) {
                        Date date1 = format.parse(currentDate);
                        Date date2 = format.parse(endDate);
                        long diff = date2.getTime() - date1.getTime();

                        txt_daysleft.setText("" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                lineardaysleft.setVisibility(View.GONE);
            }
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);

        param = new BillingParam(user.getClient_id(), user.getId());
        getPCApplication().getPcFunctionService().presentBillingStatus(param, findpresentBillingDelegate);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getPCApplication().getPcFunctionService().presentBillingStatus(param, findpresentBillingDelegate);

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

    private static class FindPastBillingDelegate extends ForegroundTaskDelegate<PresentBilling> {


        public FindPastBillingDelegate(PCActivity activity) {
            super(activity);

        }

        @Override
        public void onPostExecute(PresentBilling presentBilling, Throwable throwable) {
            super.onPostExecute(presentBilling, throwable);

            PresentBillingActivity activity = (PresentBillingActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && presentBilling != null) {
                String today_date = activity.getCurrentDate();
                String total_amount = String.valueOf((float) presentBilling.getPunches() * Float.valueOf(activity.price));
                String current_month = null;
                try {
                    current_month = activity.getMonthSpecific(today_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activity.txttotalpunchesLabel.setText("Total Number of Unique Punches Since " + current_month);
                activity.txt_total_punches.setText("" + presentBilling.getPunches());
                activity.txt_total_amount.setText(total_amount);
                try {
                    activity.txt_date.setText(activity.getMonthSpecific(presentBilling.getNext_billing_date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activity.swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    public static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("dd ,MMMM  ").format(cal.getTime());
        return monthName;
    }

    public static String getMonthSpecific(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM dd, yyyy").format(cal.getTime());
        return monthName;
    }

}
