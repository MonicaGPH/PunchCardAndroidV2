package com.inverseapps.punchcard.service.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.inverseapps.punchcard.exception.NetworkException;
import com.inverseapps.punchcard.exception.OtherException;
import com.inverseapps.punchcard.exception.PCException;
import com.inverseapps.punchcard.exception.ServiceException;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import javax.net.ssl.SSLPeerUnverifiedException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by asus on 08-Sep-17.
 */

public class TranslatedRetrofitExceptionWOResponse {


    public static<T extends Response> PCException translateNetworkException(Call<T> call, Throwable throwable) {
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
                Response pcServiceResponse = new Gson().fromJson(response.errorBody().string(), Response.class);
                return new ServiceException(pcServiceResponse.code(), pcServiceResponse.message(), null);
            } catch (Exception e) {
                Logger.e(e.getMessage());
                return new ServiceException(response.code(), msg, null);
            }
        }
        return null;
    }

    public static PCException translateServiceException(Response response) {
        if (response.errorBody().equals("error")) {
            // Read error from return json
            Logger.e("There are somethings wrong on server.");
            return new ServiceException(response.code(), response.message(), null);
        }
        return null;
    }
}
