package com.inverseapps.punchcard.service.retrofit;

import com.inverseapps.punchcard.exception.PCException;
import com.inverseapps.punchcard.model.response.PCServiceResponse;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class TranslatedRetrofitCallback<T extends PCServiceResponse> implements Callback<T> {

    public TranslatedRetrofitCallback() {

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T responseObject = null;
        // 1. Check request exception
        PCException exception = TranslatedRetrofitException.translateRequestException(response);
        if (exception == null) {
            // 2. Check service exception
            responseObject = response.body();
            exception = TranslatedRetrofitException.translateServiceException(responseObject);
        }
        onFinish(responseObject, exception);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        PCException exception = TranslatedRetrofitException.translateNetworkException(call, throwable);
        onFinish(null, exception);
    }


    public void onFinish(T responseObject, PCException exception) {
        if (exception == null) {
            Logger.d("Response is OK. Start parsing json data");
        }
        // TODO: override this method in runtime sub class
    }
}
