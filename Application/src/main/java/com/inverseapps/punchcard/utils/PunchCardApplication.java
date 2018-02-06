package com.inverseapps.punchcard.utils;

import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.inverseapps.punchcard.BuildConfig;
import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.service.PCFunctionService;
import com.inverseapps.punchcard.service.PCFunctionServiceImpl;

import com.inverseapps.punchcard.ui.GoogleApiHelper;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;


public class PunchCardApplication extends MultiDexApplication {

	private SharedPreferences appPreferences,appPreferences_one;
	private PCFunctionService pcFunctionService;
	private GoogleApiHelper googleApiHelper;
	private static PunchCardApplication mInstance;



	private String mGlobalCheckButton=null;

	@Override
	public void onCreate() { 
		super.onCreate();
		mInstance = this;
		googleApiHelper = new GoogleApiHelper(mInstance);
		//setupLeakCanary();
		setupLogLevel();
		setupMainSettings();
	}

	public static Boolean isInDebugMode() {
		return BuildConfig.DEBUG;
	}

	private void setupLeakCanary() {
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}

		LeakCanary.install(this);
		// Normal app init code...
	}

	private void setupLogLevel() {
		if (!isInDebugMode()) {
			Logger.init("PunchCard")				// default PRETTYLOGGER or use just init()
					.methodCount(3)                 // default 2
					.hideThreadInfo()               // default shown
					.logLevel(LogLevel.NONE)		// default LogLevel.FULL
					.methodOffset(2);				// default 0
		}
	}

	private void setupMainSettings() {
		appPreferences = getSharedPreferences(AppConstant.APP_PREF_FILE, MODE_PRIVATE);
		appPreferences_one = getSharedPreferences(AppConstant.APP_PREF_FILE_ONE, MODE_PRIVATE);

		pcFunctionService = new PCFunctionServiceImpl(this);

	}

	public SharedPreferences getAppPreferences() {
		return appPreferences;
	}

	public SharedPreferences getAppPreferences_one() {
		return appPreferences_one;
	}

	public PCFunctionService getPcFunctionService() {
		return pcFunctionService;
	}



	public static synchronized PunchCardApplication getInstance() {
		return mInstance;
	}

	public GoogleApiHelper getGoogleApiHelperInstance() {
		return this.googleApiHelper;
	}
	public static GoogleApiHelper getGoogleApiHelper() {
		return getInstance().getGoogleApiHelperInstance();
	}

	public String getmGlobalCheckButton() {
		return mGlobalCheckButton;
	}

	public void setmGlobalCheckButton(String mGlobalCheckButton) {
		this.mGlobalCheckButton = mGlobalCheckButton;
	}
}

