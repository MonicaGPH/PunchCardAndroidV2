package com.inverseapps.punchcard.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.inverseapps.punchcard.R;
import com.inverseapps.punchcard.service.PCFunctionService;
import com.inverseapps.punchcard.ui.dialog.PCAlertDialogFragment;
import com.inverseapps.punchcard.ui.dialog.PCProgressDialogFragment;
import com.inverseapps.punchcard.utils.PunchCardApplication;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;

/**
 * Created by Inverse, LLC on 10/17/16.
 */

public abstract class PCActivity extends AppCompatActivity {

    @LayoutRes
    protected int getRootLayoutRes() {
        return R.layout.holder_empty;
    }

    @NonNull
    protected View rootLayout;

    protected List<ForegroundTaskDelegate> listOfForegroundTaskDelegates;

    protected PCFunctionService pcFunctionService;

    private int onStartCount = 0;

    protected final String TAG_PROGRESS_DIALOG = "progressDialog";
    protected static final String TAG_ALERT_DIALOG = "alertDialog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onStartCount = 1;
        if (savedInstanceState == null) {   // 1st time
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }   // already created so reverse animation
        else {
            onStartCount = 2;
        }

        listOfForegroundTaskDelegates = new Vector<>();

        rootLayout = getLayoutInflater().inflate(getRootLayoutRes(), null);
        setContentView(rootLayout);

        ButterKnife.bind(this);

        pcFunctionService = getPCApplication().getPcFunctionService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }
    }

    @Override
    protected void onDestroy() {
        for (ForegroundTaskDelegate delegate : listOfForegroundTaskDelegates) {
            if (delegate != null) {
                delegate.cancel();
            }
        }
        super.onDestroy();
    }

    public final PunchCardApplication getPCApplication() {
        return (PunchCardApplication) getApplication();
    }

    public void showProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (prevFrag == null) {
            PCProgressDialogFragment newFrag = PCProgressDialogFragment.newInstance();
            ft.add(newFrag, TAG_PROGRESS_DIALOG);
        } else {
            ft.remove(prevFrag);
            getSupportFragmentManager().popBackStackImmediate();
        }
        ft.commitAllowingStateLoss();
    }

    public void dismissProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (prevFrag != null) {
            ft.remove(prevFrag);
            PCProgressDialogFragment dlgFrag = (PCProgressDialogFragment) prevFrag;
            if (dlgFrag.getDialog() != null && dlgFrag.getDialog().isShowing() && dlgFrag.isResumed()) {
                dlgFrag.dismissAllowingStateLoss();
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        ft.commitAllowingStateLoss();
    }

    public void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        final View view = getWindow().getDecorView();
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showKeyboard() {

        final InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        final View view = getWindow().getDecorView();
        im.showSoftInput((View) view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
    }

    public void showAlertDialog(String message) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_ALERT_DIALOG);
        if (prevFrag == null) {
            PCAlertDialogFragment newFrag = PCAlertDialogFragment.newInstance(message);
            ft.add(newFrag, TAG_ALERT_DIALOG);
        } else {
            ft.remove(prevFrag);
            getSupportFragmentManager().popBackStackImmediate();
        }
        ft.commitAllowingStateLoss();
    }

    public void showAlertDialog(String tag,
                                String message,
                                String positiveTitle,
                                String negativeTitle,
                                String neutralTitle) {
        showAlertDialog(tag, message, positiveTitle, negativeTitle, neutralTitle, new Bundle());
    }

    public void showAlertDialog(String tag,
                                String message,
                                String positiveTitle,
                                String negativeTitle,
                                String neutralTitle,
                                Bundle args) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(tag);
        if (prevFrag != null) {
            ft.remove(prevFrag);
        }
        ft.addToBackStack(null);
        PCAlertDialogFragment newFrag = PCAlertDialogFragment.newInstance(message,
                positiveTitle,
                negativeTitle,
                neutralTitle);
        newFrag.getArguments().putAll(args);
        newFrag.show(ft, tag);
    }


    public String getCurrentDate() {
        ///get Current date
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String DateToStr = format.format(curDate);
        return DateToStr;
    }

    public String checkDateAfter30days(String createdDate, Context activity) throws ParseException {
        String dateInString = createdDate;  // Start date
        Logger.d(dateInString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dateInString));
        c.add(Calendar.DATE, 30);
        Date expDate = c.getTime();
        String formatted_endDate = sdf.format(expDate);
        return formatted_endDate;
    }


    public String checkDatebefore2Days(String createdDate, Context activity) throws ParseException {
        String dateInString = createdDate;  // Start date
        Logger.d(dateInString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dateInString));
        c.add(Calendar.DATE, -2);
        Date expDate = c.getTime();
        String formatted_2daybeforedate = sdf.format(expDate);
        return formatted_2daybeforedate;
    }



}
