package com.inverseapps.punchcard.service;

import android.os.AsyncTask;

import retrofit2.Call;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public interface PCServiceCallback <Result extends Object> {

    void setAsyncTask(AsyncTask task);

    void setCall(Call call);

    void cancel();

    void onPreExecute();

    void onPostExecute(Result result, Throwable throwable);
    void onPostExecute(Result result,boolean value, Throwable throwable);
    void onPostExecute(Result result);

}

