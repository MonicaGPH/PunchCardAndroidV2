package com.inverseapps.punchcard.ui;

import android.os.AsyncTask;

import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.service.PCServiceCallback;

import java.lang.ref.WeakReference;

import retrofit2.Call;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class ForegroundTaskDelegate<Result extends Object> implements PCServiceCallback<Result> {

    protected final WeakReference<PCActivity> activityWeakReference;
    private AsyncTask task;
    private Call call;


    public ForegroundTaskDelegate(PCActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }



    @Override
    public void setAsyncTask(AsyncTask task) {
        cancelAsyncTask();
        this.task = task;
    }

    @Override
    public void setCall(Call call) {
        cancelCall();
        this.call = call;
    }

    @Override
    public void cancel() {
        cancelAsyncTask();
        cancelCall();
    }

    protected void cancelAsyncTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    protected void cancelCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    protected void showProgress() {
        PCActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.showProgressDialog();
        }
    }

    protected void dismissProgress() {
        PCActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.dismissProgressDialog();
        }
    }

    protected void handleServiceError(ServiceException ex) {
        PCActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.showAlertDialog(ex.getMessage());
        }
    }
    protected void handleServiceError(Result result,ServiceException ex) {
        PCActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            activity.showAlertDialog(ex.getMessage());
        }
    }


    protected void handleOtherError(Throwable throwable) {
        if (throwable != null && throwable.getMessage()!= null &&
                !throwable.getMessage().equals("Canceled") &&
                !throwable.getMessage().equals("Socket closed") &&
                !throwable.getMessage().equals("Socket is closed") &&
                !throwable.getMessage().equals("stream was reset: CANCEL")) {

            PCActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.showAlertDialog(throwable.getMessage());
            }
        }
    }

    @Override
    public void onPreExecute() {
        showProgress();
    }

    @Override
    public void onPostExecute(Result result, Throwable throwable) {
        dismissProgress();

        if (result == null && throwable == null) {
            return;
        }

        if(throwable instanceof ServiceException) {
            ServiceException se = (ServiceException)throwable;
            if (se.getErrorCode() == 500) {
                PCActivity activity = activityWeakReference.get();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    activity.showAlertDialog("There was a server error. Please try again!");
                }
            } else {
                handleServiceError((ServiceException) throwable);
            }
        } else {
            handleOtherError(throwable);
        }
    }
    @Override
    public void onPostExecute(Result result, boolean value,Throwable throwable) {
        dismissProgress();

        if (result == null && throwable == null) {
            return;
        }

        if(throwable instanceof ServiceException) {
            ServiceException se = (ServiceException)throwable;
            if (se.getErrorCode() == 500) {
                PCActivity activity = activityWeakReference.get();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    activity.showAlertDialog("There was a server error. Please try again!");
                }
            } else {
                handleServiceError((ServiceException) throwable);
            }
        } else {
            handleOtherError(throwable);
        }
    }

    @Override
    public void onPostExecute(Result result) {
        dismissProgress();

        if (result == null) {
            return;
        }

    }

}
