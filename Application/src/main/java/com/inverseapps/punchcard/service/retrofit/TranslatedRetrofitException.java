package com.inverseapps.punchcard.service.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.inverseapps.punchcard.exception.NetworkException;
import com.inverseapps.punchcard.exception.OtherException;
import com.inverseapps.punchcard.exception.PCException;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.response.PCServiceResponse;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import javax.net.ssl.SSLPeerUnverifiedException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class TranslatedRetrofitException {
    public static<T extends PCServiceResponse> PCException translateNetworkException(Call<T> call, Throwable throwable) {
        if (call.isCanceled()) {
            return null;
        }

        Logger.e(throwable.getMessage());
        Logger.t(Log.getStackTraceString(throwable));

        PCException networkException = new OtherException("Undefined exception", throwable);
        if (throwable instanceof IOException) {
            networkException = new NetworkException("There is no network connection. Please try again later or turn off airplane mode.",
                    throwable);
        } else if (throwable instanceof SSLPeerUnverifiedException) {
            networkException = new OtherException("Your credentials were not correct. Please try again!", throwable);
        }
        return networkException;
    }

    public static PCException translateRequestException(Response response) {
        if (response.body() == null) {
            // This case should not happen. You must have something wrong!
            String msg = "There is something wrong with request. Please check your request again";

            try {
                PCServiceResponse pcServiceResponse = new Gson().fromJson(response.errorBody().string(), PCServiceResponse.class);
                return new ServiceException(pcServiceResponse.getHttp_code(), pcServiceResponse.getMessage(), null);
            } catch (Exception e) {
                Logger.e(e.getMessage());
                return new ServiceException(response.code(), msg, null);
            }
        }
        return null;
    }

    public static PCException translateServiceException(PCServiceResponse response) {
        if (response.getResult().equals("error")) {
            // Read error from return json
            Logger.e("There are somethings wrong on server.");
            return new ServiceException(response.getHttp_code(), response.getMessage(), null);
        }
        if(response.getResult().equalsIgnoreCase("failed")){
            // Read error from return json
            Logger.e("There are somethings wrong on server.");
            return new ServiceException(response.getHttp_code(), response.getMessage(), null);
        }
        return null;
    }
}
