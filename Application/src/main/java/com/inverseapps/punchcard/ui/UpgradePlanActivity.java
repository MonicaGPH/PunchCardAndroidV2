package com.inverseapps.punchcard.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.MenuItem;

import android.widget.Toast;


import com.inverseapps.punchcard.R;


import com.inverseapps.punchcard.adapters.UpgradeAdapter;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.Choose_Option;
import com.inverseapps.punchcard.model.User;

import com.inverseapps.punchcard.utils.SnappingRecyclerView;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;

public class UpgradePlanActivity extends PCActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_show_options;
    }

    @NonNull
    @BindView(R.id.recyclerView)
    SnappingRecyclerView recyclerView;



    //a list to store all the products
    private List<Choose_Option> choose_optionList;
    private User user;
    public UpgradeDelegate upgradeDelegate;

    private void findUser() {
        pcFunctionService.findUser(findUserDelegate);
    }

    public UpgradePlanActivity() {
    }

    private ForegroundTaskDelegate findUserCheckInOutDelegate;
    private ForegroundTaskDelegate findUserDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upgradeDelegate = new UpgradeDelegate(this);
        listOfForegroundTaskDelegates.add(upgradeDelegate);

        findUserDelegate = new FindUserDelegate(this);
        listOfForegroundTaskDelegates.add(findUserDelegate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change your plan");


        user = pcFunctionService.getInternalStoredUser();

        recyclerView.enableViewScaling(true);
        recyclerView.scrollBy(1);
        choose_optionList = new ArrayList<>();

        String tier = user.getClient().getPlan_test().toLowerCase();
        UpgradePlanActivity fragment = new UpgradePlanActivity();

        UpgradeAdapter adapter;
        switch (tier) {
            case "trial":
                //adding some items to our list
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
                // choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
                //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));


                //creating recyclerview adapter
                adapter = new UpgradeAdapter(this, choose_optionList, UpgradePlanActivity.this, user);

                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
            case "base":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 1));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
                //   choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
                //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));
                //creating recyclerview adapter
                adapter = new UpgradeAdapter(this, choose_optionList, UpgradePlanActivity.this, user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
            case "premium":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
                //  choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 1));
                // choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));

                //creating recyclerview adapter
                adapter = new UpgradeAdapter(this, choose_optionList, UpgradePlanActivity.this, user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
            case "standard":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 1));
                // choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
                // choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 0));
                //creating recyclerview adapter
                adapter = new UpgradeAdapter(this, choose_optionList, UpgradePlanActivity.this, user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;

            case "enterprise":
                choose_optionList.add(new Choose_Option("PunchCard BASE", "1", "base", 0));
                choose_optionList.add(new Choose_Option("PunchCard STANDARD", "2", "standard", 0));
                //   choose_optionList.add(new Choose_Option("PunchCard PREMIUM", "3", "premium", 0));
                //  choose_optionList.add(new Choose_Option("PunchCard ENTERPRISE", "4", "enterprise", 1));
                //creating recyclerview adapter
                adapter = new UpgradeAdapter(this, choose_optionList, UpgradePlanActivity.this, user);
                //setting adapter to recyclerview
                recyclerView.setAdapter(adapter);
                break;
        }
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

    private void gotoHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.KEY_REMIND_ME_LATER_CLICKED,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    private class UpgradeDelegate extends ForegroundTaskDelegate<Boolean> {

        public UpgradeDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        protected void handleServiceError(ServiceException ex) {
            if (ex.getErrorCode() == 401) {
                showAlertDialog("Your credentials were not correct. Please try again!");
            }
            if(ex.getErrorCode() == 200){
                showAlertDialog("Oops!  Something went wrong! Your Credit Card information needs to be updated for Punchcard. Please update your billing information at your earliest convenience here.");

            }
        }
        @Override
        public void onPostExecute(Boolean result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            UpgradePlanActivity activity = (UpgradePlanActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && result) {

                Toast.makeText(activity, "Plan Change Successful.", Toast.LENGTH_SHORT).show();
                activity.findUser();
            }
        }
    }

    private static class FindUserDelegate extends ForegroundTaskDelegate<User> {
        String formatted_endDate;

        public FindUserDelegate(PCActivity activity) {
            super(activity);
        }


        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);


            UpgradePlanActivity activity = (UpgradePlanActivity) activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && user != null) {
                activity.user = user;
                activity.gotoHomeScreen();
            }
        }
    }
}

