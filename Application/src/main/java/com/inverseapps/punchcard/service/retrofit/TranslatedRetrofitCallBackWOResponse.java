package com.inverseapps.punchcard.service.retrofit;

import com.inverseapps.punchcard.exception.PCException;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asus on 08-Sep-17.
 */

public class TranslatedRetrofitCallBackWOResponse<T extends Response> implements Callback<T> {


    public TranslatedRetrofitCallBackWOResponse() {

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T responseObject = null;
        // 1. Check request exception
        PCException exception = TranslatedRetrofitExceptionWOResponse.translateRequestException(response);
        if(exception!=null)
            Logger.d(exception);
        if (exception == null) {
            // 2. Check service exception
            responseObject = response.body();
            exception = TranslatedRetrofitExceptionWOResponse.translateServiceException(responseObject);
            if(exception!=null)
                Logger.d(exception);
        }
        onFinish(responseObject, exception);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        PCException exception = TranslatedRetrofitExceptionWOResponse.translateNetworkException(call, throwable);
        if(exception!=null)
            Logger.d(exception);
        //   onFinish(null, exception);
    }


    public void onFinish(T responseObject, PCException exception) {

        if (exception == null) {

            Logger.d(responseObject);
            Logger.d("Response is OK. Start parsing json data");
        }
        // TODO: override this method in runtime sub class
    }
}
//http://testclient.puncardllc.com.punchcardllc.com/api/v1.0/projects/abd96836-9c74-4365-90bc-0db94d577028/logo