package com.inverseapps.punchcard.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.adapters.PastBillingAdapter;

import com.inverseapps.punchcard.model.LogNote;
import com.inverseapps.punchcard.model.PastBilling;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.param.BillingParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

/**
 * Created by asus on 05-Dec-17.
 */

public class PastBillingActivity extends PCActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_past_billing;
    }

    @NonNull
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @NonNull
    @BindView(R.id.contenttext)
    RelativeLayout contenttext;
    @NonNull
    @BindView(R.id.content)
    LinearLayout content;
    @NonNull
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<PastBilling> listOfPastBillings;
    private PastBillingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private User user;
    private BillingParam param;

    private ForegroundTaskDelegate findpastBillingDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Past Billing");

        findpastBillingDelegate = new FindPastBillingDelegate(this);
        listOfForegroundTaskDelegates.add(findpastBillingDelegate);

        listOfPastBillings = new Vector<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PastBillingAdapter(listOfPastBillings,this);
        adapter.buildUserCheckInOutTable();
        recyclerView.setAdapter(adapter);


        user = getPCApplication().getPcFunctionService().getInternalStoredUser();
        swipeRefreshLayout.setColorSchemeResources(R.color.logo_orange);
        swipeRefreshLayout.setOnRefreshListener(this);

        param = new BillingParam(user.getClient_id(), user.getId());
        getPCApplication().getPcFunctionService().pastBillingStatus(param, findpastBillingDelegate);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getPCApplication().getPcFunctionService().pastBillingStatus(param, findpastBillingDelegate);

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


    private static class FindPastBillingDelegate extends ForegroundTaskDelegate<List<PastBilling>> {


        public FindPastBillingDelegate(PCActivity activity) {
            super(activity);

        }

        @Override
        public void onPostExecute(List<PastBilling> pastBillings, Throwable throwable) {
            super.onPostExecute(pastBillings, throwable);

            PastBillingActivity activity = (PastBillingActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                if (pastBillings.size() == 0) {
                    activity.contenttext.setVisibility(View.VISIBLE);
                    activity.content.setVisibility(View.GONE);
                } else {


                    activity.contenttext.setVisibility(View.GONE);
                    activity.content.setVisibility(View.VISIBLE);
                    activity.listOfPastBillings.clear();
                    activity.listOfPastBillings.addAll(pastBillings);
                    Collections.sort(activity.listOfPastBillings, new Comparator<PastBilling>() {
                        @Override
                        public int compare(PastBilling lhs, PastBilling rhs) {
                            return rhs.getBilling_start_date().compareTo(lhs.getBilling_start_date());
                        }

                    });
                   activity.adapter.buildUserCheckInOutTable();
                    activity.adapter.notifyDataSetChanged();

                    activity.swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }
}
