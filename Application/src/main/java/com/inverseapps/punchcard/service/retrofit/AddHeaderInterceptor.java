package com.inverseapps.punchcard.service.retrofit;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class AddHeaderInterceptor implements Interceptor {

    private String token;
    public AddHeaderInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-type", "application/json");
        builder.addHeader("Accept", "application/json");
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }
}
