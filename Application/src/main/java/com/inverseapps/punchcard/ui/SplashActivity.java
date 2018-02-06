package com.inverseapps.punchcard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.model.LoginRequirement;


public class SplashActivity extends PCActivity {

    @LayoutRes
    protected int getRootLayoutRes() {
        return R.layout.activity_splash;
    }

    private ForegroundTaskDelegate loadLoginRequirementDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLoginRequirementDelegate = new LoadLoginRequirementDelegate(this);
        listOfForegroundTaskDelegates.add(loadLoginRequirementDelegate);

        pcFunctionService.loadLoginRequirement(loadLoginRequirementDelegate);
    }


    private void gotoLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.KEY_REMIND_ME_LATER_CLICKED,false);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private static class LoadLoginRequirementDelegate extends ForegroundTaskDelegate<Integer> {

        public LoadLoginRequirementDelegate(PCActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Integer result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            SplashActivity activity = (SplashActivity)activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                if (result == LoginRequirement.LOGIN.ordinal()) {
                    activity.gotoLoginScreen();
                } else {
                    activity.gotoHomeScreen();
                }
            }
        }
    }
}


