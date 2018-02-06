package com.inverseapps.punchcard.ui;

import android.os.AsyncTask;

import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.fragments.ProjectDetailsFragment;
import com.inverseapps.punchcard.service.GeofenceTransitionsIntentService;
import com.inverseapps.punchcard.service.PCServiceCallback;

import java.lang.ref.WeakReference;

import retrofit2.Call;

public class ForegroundTaskDelegateService<Result extends Object> implements PCServiceCallback<Result> {

    protected final WeakReference<GeofenceTransitionsIntentService> serviceWeakReference;
    protected final WeakReference<ProjectDetailsFragment> fragmentWeakReference;
    private AsyncTask task;
    private Call call;


    public ForegroundTaskDelegateService(GeofenceTransitionsIntentService service, ProjectDetailsFragment fragment) {
        serviceWeakReference = new WeakReference<>(service);
        fragmentWeakReference = new WeakReference<>(fragment);
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


    protected void handleServiceError(ServiceException ex) {
        GeofenceTransitionsIntentService service = serviceWeakReference.get();
        if (service != null) {

        }
    }

    protected void handleOtherError(Throwable throwable) {
        if (throwable != null && throwable.getMessage() != null &&
                !throwable.getMessage().equals("Canceled") &&
                !throwable.getMessage().equals("Socket closed") &&
                !throwable.getMessage().equals("Socket is closed") &&
                !throwable.getMessage().equals("stream was reset: CANCEL")) {

            GeofenceTransitionsIntentService service = serviceWeakReference.get();
            if (service != null) {

            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(Result result, Throwable throwable) {


        if (result == null && throwable == null) {
            return;
        }

        if (throwable instanceof ServiceException) {
            ServiceException se = (ServiceException) throwable;
            if (se.getErrorCode() == 500) {
                GeofenceTransitionsIntentService service = serviceWeakReference.get();
                if (service != null) {

                }
            } else {
                handleServiceError((ServiceException) throwable);
            }
        } else {
            handleOtherError(throwable);
        }
    }

    @Override
    public void onPostExecute(Result result,boolean type, Throwable throwable) {


        if (result == null && throwable == null) {
            return;
        }

        if (throwable instanceof ServiceException) {
            ServiceException se = (ServiceException) throwable;
            if (se.getErrorCode() == 500) {
                GeofenceTransitionsIntentService service = serviceWeakReference.get();
                if (service != null) {

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

    }

}
