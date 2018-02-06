package com.inverseapps.punchcard.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inverseapps.punchcard.Constant.AppConstant;
import com.inverseapps.punchcard.exception.OtherException;
import com.inverseapps.punchcard.exception.PCException;
import com.inverseapps.punchcard.exception.ServiceException;
import com.inverseapps.punchcard.model.Badge;
import com.inverseapps.punchcard.model.CheckInOut;
import com.inverseapps.punchcard.model.CheckStatus;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Face;
import com.inverseapps.punchcard.model.LogNote;

import com.inverseapps.punchcard.model.LoginRequirement;
import com.inverseapps.punchcard.model.OAuth;
import com.inverseapps.punchcard.model.OnSite;
import com.inverseapps.punchcard.model.PresentBilling;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.UserCheckInOut;
import com.inverseapps.punchcard.model.param.BadgeCheckInOutParam;
import com.inverseapps.punchcard.model.param.BillingParam;
import com.inverseapps.punchcard.model.param.ChangePasswordParam;
import com.inverseapps.punchcard.model.param.CreateLogNoteParam;
import com.inverseapps.punchcard.model.param.FaceCheckInOutParam;
import com.inverseapps.punchcard.model.param.GeoCheckInOutParam;
import com.inverseapps.punchcard.model.param.LoginParam;
import com.inverseapps.punchcard.model.param.PaymentParam;
import com.inverseapps.punchcard.model.param.QRCheckInOutParam;
import com.inverseapps.punchcard.model.param.SubscribeORUnSubscribeParam;

import com.inverseapps.punchcard.model.param.UpdateAvatarParam;
import com.inverseapps.punchcard.model.param.UpdateParam;
import com.inverseapps.punchcard.model.param.UpdateUserParam;
import com.inverseapps.punchcard.model.param.UpgradeParam;
import com.inverseapps.punchcard.model.response.BadgeResponse;
import com.inverseapps.punchcard.model.response.CheckInOutResponse;
import com.inverseapps.punchcard.model.response.CheckStatusResponse;
import com.inverseapps.punchcard.model.response.EmployeeResponse;
import com.inverseapps.punchcard.model.response.LogNotesResponse;
import com.inverseapps.punchcard.model.response.OnSitesResponse;
import com.inverseapps.punchcard.model.response.PCServiceResponse;
import com.inverseapps.punchcard.model.PastBilling;
import com.inverseapps.punchcard.model.response.PastBillingResponse;
import com.inverseapps.punchcard.model.response.PaymentResponse;
import com.inverseapps.punchcard.model.response.PresentBillingResponse;
import com.inverseapps.punchcard.model.response.ProjectResponse;
import com.inverseapps.punchcard.model.response.ProjectsResponse;
import com.inverseapps.punchcard.model.response.SubscribeORUnSubscribeResponse;
import com.inverseapps.punchcard.model.response.UpgradeResponse;
import com.inverseapps.punchcard.model.response.UserCheckInOutsResponse;
import com.inverseapps.punchcard.model.response.UserResponse;
import com.inverseapps.punchcard.service.retrofit.AddHeaderInterceptor;
import com.inverseapps.punchcard.service.retrofit.GsonDateFormatAdapter;
import com.inverseapps.punchcard.service.retrofit.PCRetrofitService;
import com.inverseapps.punchcard.service.retrofit.RetryInterceptor;
import com.inverseapps.punchcard.service.retrofit.TranslatedRetrofitCallback;
import com.inverseapps.punchcard.utils.PunchCardApplication;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class PCFunctionServiceImpl implements PCFunctionService {

    private Context context;
    private final SharedPreferences appPreferences;
    private final HttpLoggingInterceptor defaultLogging;
    private final Gson defaultGson;
    private PCRetrofitService defaultService;
    private Picasso picasso;

    public static final String KEY_EMPLOYEE_UNIQUE_ID = "employeeuniqueid";
    public static final String KEY_ROLE = "role";

    public static final String SERVER_PATH = "http://webintellisense.com/webservice/";
    private static final long timeOut = 90; // 90 seconds

    public static final String SERVER_PATH_PAYMENT = "http://webintellisense.com/api/crons/";
    public static final String SERVER_PATH_PAYMENT_TOKEN = "http://webintellisense.com/api/v1.0/webapp/";

    private PCFunctionServiceImpl() {
        // Don't allow default constructor outside
        appPreferences = null;
        defaultLogging = null;
        defaultGson = null;
        defaultService = null;
    }

    public PCFunctionServiceImpl(PunchCardApplication application) {

        context = application.getApplicationContext();
        appPreferences = application.getAppPreferences();
        defaultLogging = newDefaultLogging();
        defaultGson = newDefaultGson();
        defaultService = null;
    }

    private HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (PunchCardApplication.isInDebugMode()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return logging;
    }

    private Gson newDefaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateFormatAdapter())
                .setLenient()
                .create();
    }

    private String getBaseUrl() {
        String domain = appPreferences.getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }
        String companyHandle = appPreferences.getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");
        String baseUrl = String.format(PCRetrofitService.API_END_POINT_FORMAT, companyHandle, domain);
        return baseUrl;
    }

    private String getBaseUrlPayment() {
        String domain = appPreferences.getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }

        String baseUrl = String.format(PCRetrofitService.SERVER_PATH_PAYMENT, domain);
        return baseUrl;
    }

    private String getBaseUrlFace() {
        String domain = appPreferences.getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }

        String baseUrl = String.format(PCRetrofitService.SERVER_PATH, domain);
        return baseUrl;
    }

    private String getBaseUrl_logout() {
        String companyHandle = appPreferences.getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");
        String domain = appPreferences.getString(AppConstant.PREF_KEY_DOMAIN, "");
        if (TextUtils.isEmpty(domain)) {
            domain = AppConstant.DEFAULT_DOMAIN;
        }
        String baseUrl = String.format(PCRetrofitService.API_LOGOUT, companyHandle,domain);
        return baseUrl;
    }

    private String getToken() {
        return appPreferences.getString(AppConstant.PREF_KEY_TOKEN, "");
    }

    private PCRetrofitService getService(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor(token))
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(PCRetrofitService.class);
    }

    private PCRetrofitService getService_logout(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor(token))
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl_logout())
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(PCRetrofitService.class);
    }

    private PCRetrofitService getServiceFace() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Date.class, new GsonDateFormatAdapter())
                .create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()

                .addInterceptor(defaultLogging)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrlFace())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        return retrofit.create(PCRetrofitService.class);
    }

    private PCRetrofitService getServicePayment(String token) {


        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor(token))
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH_PAYMENT_TOKEN)
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(PCRetrofitService.class);
    }

    private PCRetrofitService getServicePayment() {


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrlPayment())
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(PCRetrofitService.class);
    }

    private PCRetrofitService getService() {

        defaultService = getService(getToken());

        return defaultService;
    }

    private PCRetrofitService getServiceP() {

        defaultService = getServicePayment(getToken());

        return defaultService;
    }

    private PCRetrofitService getServiceNoToken_logout() {
        return getService_logout(null);
    }

    private PCRetrofitService getServiceNoToken() {
        return getService(null);
    }

    @Override
    public void internalStoreBasicUserInfoIfNeeded(@NonNull final String userName,
                                                   @NonNull final String password,
                                                   @NonNull final boolean remember) {
        if (remember) {
            appPreferences.edit().putString(AppConstant.PREF_KEY_USER_NAME,
                    userName).apply();
        }

        // store password for reset password
        appPreferences.edit().putString(AppConstant.PREF_KEY_PASSWORD,
                password).apply();
        appPreferences.edit().putBoolean(AppConstant.PREF_KEY_REMEMBER_ME,
                remember).apply();
    }

    @Override
    public String getCompanyHandle() {
        return appPreferences.getString(AppConstant.PREF_KEY_COMPANY_HANDLE, "");
    }

    @Override
    public String getUserName() {
        return appPreferences.getString(AppConstant.PREF_KEY_USER_NAME, "");
    }

    @Override
    public String getPassword() {
        return appPreferences.getString(AppConstant.PREF_KEY_PASSWORD, "");
    }

    @Override
    public boolean isRememberMe() {
        return appPreferences.getBoolean(AppConstant.PREF_KEY_REMEMBER_ME, false);
    }

    @Override
    public User getInternalStoredUser() {
        User user = null;
        String userJson = appPreferences.getString(AppConstant.PREF_KEY_USER, "");

            user = defaultGson.fromJson(userJson, User.class);

        return user;
    }

    @Override
    public Project getInternalStoredProject() {
        Project project = null;
        String userProject = appPreferences.getString(AppConstant.PREF_KEY_PROJECT, "");

            project = defaultGson.fromJson(userProject, Project.class);

        return project;
    }

    @Override
    public Picasso getPicasso() {
        if (picasso == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new AddHeaderInterceptor(getToken()))
                    .addInterceptor(defaultLogging)
                    .addInterceptor(new RetryInterceptor())
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .build();
            picasso = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(client))
                    .build();
        }
        return picasso;
    }

    @Override
    public void loadLoginRequirement(@NonNull final PCServiceCallback<Integer> callback) {
        new AsyncTask<Void, Void, Integer>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
                callback.setAsyncTask(this);
                callback.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    // Normally we would do some work here, like load data from server.
                    // For our sample, we just sleep for 3 seconds.
                    long endTime = System.currentTimeMillis() + 3 * 1000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            wait(endTime - System.currentTimeMillis());
                        }
                    }
                } catch (Exception exception) {
                    Logger.e(exception.getMessage());
                    this.exception = new OtherException("Undefined exception", exception);
                    return LoginRequirement.LOGIN.ordinal();
                }

                String token = getToken();


                if (TextUtils.isEmpty(token)) {
                    return LoginRequirement.LOGIN.ordinal();
                } else {
                    return LoginRequirement.NONE.ordinal();
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                callback.onPostExecute(result, exception);
            }
        }.execute();
    }

    @Override
    public void login(@NonNull final String companyHandle,
                      @NonNull final String userName,
                      @NonNull final String password,
                      @NonNull final PCServiceCallback<Boolean> callback) {

        appPreferences.edit().putString(AppConstant.PREF_KEY_COMPANY_HANDLE, companyHandle).apply();
        appPreferences.edit().putString(AppConstant.PREF_KEY_USER_NAME, userName).apply();


        LoginParam param = new LoginParam(userName, password);
        Call<OAuth> callGetOAuth = getServiceNoToken().getOAuth(param);
        callback.setCall(callGetOAuth);

        callback.onPreExecute();
        callGetOAuth.enqueue(new Callback<OAuth>() {
            @Override
            public void onResponse(Call<OAuth> call, Response<OAuth> response) {


                if (response.body() != null) {

                    String accessToken = response.body().getAccess_token();
                    appPreferences.edit().putString(AppConstant.PREF_KEY_TOKEN, accessToken).apply();
                    Logger.d(accessToken);
                    callback.onPostExecute(true, null);

                } else {
                    ServiceException exception = new ServiceException(401, "The user credentials were incorrect.", null);
                    callback.onPostExecute(true, exception);
                }


            }

            @Override
            public void onFailure(Call<OAuth> call, Throwable t) {
                callback.onPostExecute(false, t);
            }
        });
    }

    @Override
    public void logout(@NonNull final PCServiceCallback<Boolean> callback) {
        new AsyncTask<Void, Void, Boolean>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
                callback.setAsyncTask(this);
                callback.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                Logger.d("Clear token and user info");
                appPreferences.edit().putString(AppConstant.PREF_KEY_TOKEN, "").apply();
                appPreferences.edit().putString(AppConstant.PREF_KEY_USER, "").apply();

                if (!isRememberMe()) {
                    appPreferences.edit().putString(AppConstant.PREF_KEY_COMPANY_HANDLE, "").apply();
                    appPreferences.edit().putString(AppConstant.PREF_KEY_USER_NAME, "").apply();
                    appPreferences.edit().putString(AppConstant.PREF_KEY_PASSWORD, "").apply();
                    appPreferences.edit().putBoolean(AppConstant.PREF_KEY_REMEMBER_ME, false).apply();
                }

                defaultService = null;
                picasso = null;

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                callback.onPostExecute(result, exception);
            }
        }.execute();
    }

    @Override
    public void findUser(@NonNull final PCServiceCallback<User> callback) {
        Call<UserResponse> callGetUser = getService().getUser();
        callback.setCall(callGetUser);

        callback.onPreExecute();
        callGetUser.enqueue(new TranslatedRetrofitCallback<UserResponse>() {

            @Override
            public void onFinish(UserResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                User user = null;
                if (exception == null && responseObject != null) {
                    // Get user from return json
                    user = responseObject.getData();
                    String subdomain = responseObject.getData().getSubdomain();
                    String jsonUser = defaultGson.toJson(user);
                    appPreferences.edit().putString(AppConstant.PREF_KEY_USER, jsonUser).apply();
                    appPreferences.edit().putString(AppConstant.PREF_KEY_SCANNER_ACCESS, user.getPermissions().getScanner().toString()).apply();

                }

                callback.onPostExecute(user, exception);
            }

        });
    }

    @Override
    public void logout_user(@NonNull final PCServiceCallback<Boolean> callback) {
        Call<Void> callLogOut = getServiceNoToken_logout().logout_user();
        callback.setCall(callLogOut);

        callback.onPreExecute();
        callLogOut.enqueue(new Callback<Void>() {


            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                boolean bool = true;
                callback.onPostExecute(bool, null);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });
    }


    @Override
    public void updateAvatar(@NonNull final UpdateAvatarParam param,
                             @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PCServiceResponse> callUpdateAvatar = getService().updateAvatar(param);
        callback.setCall(callUpdateAvatar);

        callback.onPreExecute();
        callUpdateAvatar.enqueue(new TranslatedRetrofitCallback<PCServiceResponse>() {

            @Override
            public void onFinish(PCServiceResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }
                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void findFace(@NonNull MultipartBody.Part selfie,
                         @NonNull RequestBody sub_domain, @NonNull RequestBody uid, @NonNull RequestBody pro_id, @NonNull final PCServiceCallback<Face> callback) {

        Call<Face> callGetFace = getServiceFace().findFace(selfie, sub_domain, uid, pro_id);
        callback.setCall(callGetFace);

        callback.onPreExecute();
        callGetFace.enqueue(new Callback<Face>() {


            @Override
            public void onResponse(Call<Face> call, Response<Face> response) {
                Face faceResponse;
                faceResponse = response.body();
                callback.onPostExecute(faceResponse, null);

            }

            @Override
            public void onFailure(Call<Face> call, Throwable t) {

            }

        });
    }

    @Override
    public void uploadLogesTextANDImages(MultipartBody.Part[] files, @NonNull RequestBody note, @NonNull RequestBody uniq_id, @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PCServiceResponse> callUploadLogesTextANDImages = getService().uploadLogesTextANDImages(files,note,uniq_id);
        callback.setCall(callUploadLogesTextANDImages);


        callback.onPreExecute();
        callUploadLogesTextANDImages.enqueue(new TranslatedRetrofitCallback<PCServiceResponse>() {

            @Override
            public void onFinish(PCServiceResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }
                }
                callback.onPostExecute(result, exception);
            }
        });
    }


    @Override
    public void sendPaymentDetails(@NonNull PaymentParam param, @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PaymentResponse> callPostPayment = getServicePayment().sendPaymentDetails(param);
        callback.setCall(callPostPayment);

        callback.onPreExecute();
        callPostPayment.enqueue(new TranslatedRetrofitCallback<PaymentResponse>() {

            @Override
            public void onFinish(PaymentResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }

                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void updatePaymentDetails(@NonNull UpdateParam param, @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PaymentResponse> callPostPayment = getServicePayment().updatePaymentDetails(param);
        callback.setCall(callPostPayment);

        callback.onPreExecute();
        callPostPayment.enqueue(new TranslatedRetrofitCallback<PaymentResponse>() {

            @Override
            public void onFinish(PaymentResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }

                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void sendUnSubscribtionMsg(@NonNull SubscribeORUnSubscribeParam param, @NonNull final PCServiceCallback<Boolean> callback) {
        Call<SubscribeORUnSubscribeResponse> callSubscribeORUnSubscribe = getServicePayment().sendUnSubscribtionMsg(param);
        callback.setCall(callSubscribeORUnSubscribe);

        callback.onPreExecute();
        callSubscribeORUnSubscribe.enqueue(new TranslatedRetrofitCallback<SubscribeORUnSubscribeResponse>() {

            @Override
            public void onFinish(SubscribeORUnSubscribeResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }

                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void sendupgrade(@NonNull UpgradeParam param, @NonNull final PCServiceCallback<Boolean> callback) {
        Call<UpgradeResponse> callupgrade = getServicePayment().sendupgrade(param);
        callback.setCall(callupgrade);

        callback.onPreExecute();
        callupgrade.enqueue(new TranslatedRetrofitCallback<UpgradeResponse>() {

            @Override
            public void onFinish(UpgradeResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }

                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void pastBillingStatus(@NonNull BillingParam param, @NonNull final PCServiceCallback<List<PastBilling>> callback) {
        Call<PastBillingResponse> pastBillingResponseCall = getServicePayment().pastBillingStatus(param);
        callback.setCall(pastBillingResponseCall);

        callback.onPreExecute();
        pastBillingResponseCall.enqueue(new TranslatedRetrofitCallback<PastBillingResponse>() {

            @Override
            public void onFinish(PastBillingResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<PastBilling> pastBillings = new Vector<>();
                if (exception == null && responseObject != null) {

                    List<PastBilling> returnedPassBillings = responseObject.getData();
                    if (returnedPassBillings != null & returnedPassBillings.size() > 0) {
                        pastBillings.addAll(returnedPassBillings);
                    }
                }
                callback.onPostExecute(pastBillings, exception);
            }

        });
    }

/*    @Override
    public void pastBillingStatus(@NonNull BillingParam param, @NonNull final PCServiceCallback<List<PastBilling>> callback) {
        Call<PastBillingResponse> pastBillingResponseCall = getServiceP().pastBillingStatus(param);
        callback.setCall(pastBillingResponseCall);

        callback.onPreExecute();
        pastBillingResponseCall.enqueue(new TranslatedRetrofitCallback<PastBillingResponse>() {

            @Override
            public void onFinish(PastBillingResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<PastBilling> pastBillings = new Vector<>();
                if (exception == null && responseObject != null) {

                    List<PastBilling> returnedPassBillings = responseObject.getData();
                    if ( returnedPassBillings!= null & returnedPassBillings.size() > 0) {
                        pastBillings.addAll(returnedPassBillings);
                    }
                }
                callback.onPostExecute(pastBillings, exception);
            }

        });
    }*/

    @Override
    public void presentBillingStatus(@NonNull BillingParam param, @NonNull final PCServiceCallback<PresentBilling> callback) {
        Call<PresentBillingResponse> presentBillingResponseCall = getServicePayment().presentBillingStatus(param);
        callback.setCall(presentBillingResponseCall);

        callback.onPreExecute();
        presentBillingResponseCall.enqueue(new TranslatedRetrofitCallback<PresentBillingResponse>() {

            @Override
            public void onFinish(PresentBillingResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                PresentBilling presentBilling = null;
                if (exception == null && responseObject != null) {

                    presentBilling = responseObject.getData();

                }
                callback.onPostExecute(presentBilling, exception);
            }

        });
    }


    @Override
    public void updateUser(@NonNull final UpdateUserParam param,
                           @NonNull final PCServiceCallback<User> callback) {
        Call<UserResponse> callUpdateUser = getService().updateUser(param);
        callback.setCall(callUpdateUser);

        callback.onPreExecute();
        callUpdateUser.enqueue(new TranslatedRetrofitCallback<UserResponse>() {

            @Override
            public void onFinish(UserResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                User user = null;
                if (exception == null && responseObject != null) {
                    // Get user from return json
                    user = responseObject.getData();
                }

                callback.onPostExecute(user, exception);


            }
        });
    }


    @Override
    public void changePassword(@NonNull final ChangePasswordParam param,
                               @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PCServiceResponse> callChangePassword = getService().changePassword(param);
        callback.setCall(callChangePassword);

        callback.onPreExecute();
        callChangePassword.enqueue(new TranslatedRetrofitCallback<PCServiceResponse>() {

            @Override
            public void onFinish(PCServiceResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }
                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public String avatarPath(@NonNull String userUniqId) {
        String path = String.format(getBaseUrl() + "user/avatar/%s", userUniqId);
        return path;
    }

    @Override
    public void findLogNotes(@NonNull final String projectUniqId,
                             @NonNull final PCServiceCallback<List<LogNote>> callback) {
        Call<LogNotesResponse> callGetLogNotes = getService().getLogs(projectUniqId);
        callback.setCall(callGetLogNotes);

        callback.onPreExecute();
        callGetLogNotes.enqueue(new TranslatedRetrofitCallback<LogNotesResponse>() {

            @Override
            public void onFinish(LogNotesResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<LogNote> logNotes = new Vector<>();
                if (exception == null && responseObject != null) {
                    // Get log notes from return json
                    List<LogNote> returnedLogNotes = responseObject.getData();
                    if (returnedLogNotes != null & returnedLogNotes.size() > 0) {
                        logNotes.addAll(returnedLogNotes);
                    }
                }
                callback.onPostExecute(logNotes, exception);
            }

        });
    }

    @Override
    public void createNewLogNote(@NonNull final CreateLogNoteParam param,
                                 @NonNull final PCServiceCallback<Boolean> callback) {
        Call<PCServiceResponse> callCreateLogNote = getService().createLogNote(param);
        callback.setCall(callCreateLogNote);

        callback.onPreExecute();
        callCreateLogNote.enqueue(new TranslatedRetrofitCallback<PCServiceResponse>() {

            @Override
            public void onFinish(PCServiceResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;
                    }
                }
                callback.onPostExecute(result, exception);
            }
        });
    }

    @Override
    public void findUserCheckIns(@NonNull final PCServiceCallback<List<UserCheckInOut>> callback) {
        Call<UserCheckInOutsResponse> callGetUserCheckIns = getService().getUserCheckIns();
        callback.setCall(callGetUserCheckIns);

        callback.onPreExecute();
        callGetUserCheckIns.enqueue(new TranslatedRetrofitCallback<UserCheckInOutsResponse>() {

            @Override
            public void onFinish(UserCheckInOutsResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<UserCheckInOut> userCheckInOuts = new Vector<>();
                if (exception == null && responseObject != null) {
                    // Get user checkins from return json
                    List<UserCheckInOut> returnedUserCheckInOuts = responseObject.getData();
                    if (returnedUserCheckInOuts != null & returnedUserCheckInOuts.size() > 0) {
                        userCheckInOuts.addAll(returnedUserCheckInOuts);
                    }
                }
                callback.onPostExecute(userCheckInOuts, exception);
            }

        });
    }


    @Override
    public void geoCheckIn(@NonNull final GeoCheckInOutParam param,
                           @NonNull final PCServiceCallback<Boolean> callback) {
        Call<CheckInOutResponse> callCheckIn = getService().geoCheckIn(param);
        callback.setCall(callCheckIn);

        callback.onPreExecute();
        callCheckIn.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);


                boolean result = false;

                if (exception == null && responseObject != null) {
                    Logger.d(responseObject.toString());
                    if (responseObject.getResult().equals("success")) {
                        result = true;

                    }

                }
                callback.onPostExecute(result, exception);
            }

        });
    }

    @Override
    public void geoCheckOut(@NonNull final GeoCheckInOutParam param,
                            @NonNull final PCServiceCallback<Boolean> callback) {
        Call<CheckInOutResponse> callCheckOut = getService().geoCheckOut(param);
        callback.setCall(callCheckOut);

        callback.onPreExecute();
        callCheckOut.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                if (exception == null && responseObject != null) {
                    if (responseObject.getResult().equals("success")) {
                        result = true;

                    }
                }
                callback.onPostExecute(result, exception);
            }

        });
    }


    @Override
    public void badgeCheckIn(@NonNull final BadgeCheckInOutParam param,
                             @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckIn = getService().badgeCheckIn(param);
        callback.setCall(callCheckIn);

        callback.onPreExecute();
        callCheckIn.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut= null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }

                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void badgeCheckOut(@NonNull final BadgeCheckInOutParam param,
                              @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckOut = getService().badgeCheckOut(param);
        callback.setCall(callCheckOut);

        callback.onPreExecute();
        callCheckOut.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut = null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }
                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void qrCheckIn(@NonNull final QRCheckInOutParam param,
                          @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckIn = getService().qrCheckIn(param);
        callback.setCall(callCheckIn);

        callback.onPreExecute();
        callCheckIn.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut = null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }
                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void faceCheckIn(@NonNull final FaceCheckInOutParam param,
                            @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckIn = getService().faceCheckIn(param);
        callback.setCall(callCheckIn);

        callback.onPreExecute();
        callCheckIn.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut = null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }
                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void faceCheckOut(@NonNull final FaceCheckInOutParam param,
                             @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckOut = getService().faceCheckOut(param);
        callback.setCall(callCheckOut);

        callback.onPreExecute();
        callCheckOut.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut = null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }
                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void qrCheckOut(@NonNull final QRCheckInOutParam param,
                           @NonNull final PCServiceCallback<CheckInOut> callback) {
        Call<CheckInOutResponse> callCheckOut = getService().qrCheckOut(param);
        callback.setCall(callCheckOut);

        callback.onPreExecute();
        callCheckOut.enqueue(new TranslatedRetrofitCallback<CheckInOutResponse>() {

            @Override
            public void onFinish(CheckInOutResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                boolean result = false;
                CheckInOut checkInOut = null;
                if (exception == null && responseObject != null) {
                    if(responseObject.getData().equals( " ") ){
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }else {
                        checkInOut = responseObject.getData();
                        if (responseObject.getResult().equals("success")) {
                            result = true;
                        }
                    }
                }
                callback.onPostExecute(checkInOut,result, exception);
            }

        });
    }

    @Override
    public void findBadge(@NonNull final String userUniqId,
                          @NonNull final String projectUniqId,
                          @NonNull final PCServiceCallback<Badge> callback) {
        Call<BadgeResponse> callGetBadge = getService().getBadge(userUniqId, projectUniqId);
        callback.setCall(callGetBadge);

        callback.onPreExecute();
        callGetBadge.enqueue(new TranslatedRetrofitCallback<BadgeResponse>() {

            @Override
            public void onFinish(BadgeResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                Badge badge = null;
                if (exception == null && responseObject != null) {
                    // Get badge from return json
                    badge = responseObject.getData();
                }
                callback.onPostExecute(badge, exception);
            }

        });
    }

    @Override
    public void findProjects(@NonNull final PCServiceCallback<List<Project>> callback) {
        Call<ProjectsResponse> callGetProjects = getService().getProjects();
        callback.setCall(callGetProjects);

        callback.onPreExecute();
        callGetProjects.enqueue(new TranslatedRetrofitCallback<ProjectsResponse>() {

            @Override
            public void onFinish(ProjectsResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<Project> projects = new Vector<>();
                if (exception == null && responseObject != null) {
                    // Get projects from return json
                    List<Project> returnedProjects = responseObject.getData();
                    if (returnedProjects != null & returnedProjects.size() > 0) {
                        projects.addAll(returnedProjects);
                    }
                }
                callback.onPostExecute(projects, exception);
            }

        });
    }

    @Override
    public void findCheckStatus(@NonNull final PCServiceCallback<CheckStatus> callback) {
        Call<CheckStatusResponse> callGetCheckStatus = getService().getCheckStatus();
        callback.setCall(callGetCheckStatus);

        callback.onPreExecute();
        callGetCheckStatus.enqueue(new TranslatedRetrofitCallback<CheckStatusResponse>() {

            @Override
            public void onFinish(CheckStatusResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                CheckStatus checkStatus = null;

                if (exception == null && responseObject != null) {
                    // Get check status from return json
                    checkStatus = responseObject.getData();
                }
                callback.onPostExecute(checkStatus, exception);
            }

        });
    }

    @Override
    public void findProject(@NonNull final String projectUniqId,
                            @NonNull final PCServiceCallback<Project> callback) {
        Call<ProjectResponse> callGetProject = getService().getProject(projectUniqId);
        callback.setCall(callGetProject);

        callback.onPreExecute();
        callGetProject.enqueue(new TranslatedRetrofitCallback<ProjectResponse>() {

            @Override
            public void onFinish(ProjectResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                Project project = null;
                if (exception == null && responseObject != null) {
                    // Get project from return json


                    project = responseObject.getData();
                    String jsonProject = defaultGson.toJson(project);
                    appPreferences.edit().putString(AppConstant.PREF_KEY_PROJECT, jsonProject).apply();
                }
                callback.onPostExecute(project, exception);
            }

        });
    }

    @Override
    public void findOnSites(@NonNull final String projectUniqId,
                            @NonNull final PCServiceCallback<List<OnSite>> callback) {
        Call<OnSitesResponse> callGetEmployees = getService().getEmployeeInfoOnSites(projectUniqId);
        callback.setCall(callGetEmployees);

        callback.onPreExecute();
        callGetEmployees.enqueue(new TranslatedRetrofitCallback<OnSitesResponse>() {

            @Override
            public void onFinish(OnSitesResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<OnSite> employees = new Vector<>();
                if (exception == null && responseObject != null) {
                    // Get employees from return json
                    List<OnSite> returnedEmployees = responseObject.getData();
                    if (returnedEmployees != null & returnedEmployees.size() > 0) {
                        employees.addAll(returnedEmployees);
                    }
                }
                callback.onPostExecute(employees, exception);
            }
        });
    }

    @Override
    public void findOnSites(@NonNull final String projectUniqId,
                            @NonNull final String query,
                            final int page,
                            final int size,
                            @NonNull final PCServiceCallback<List<OnSite>> callback) {
        Call<OnSitesResponse> callGetEmployees = getService().findEmployeeInfoOnSites(projectUniqId, query, page, size);
        callback.setCall(callGetEmployees);

        callback.onPreExecute();
        callGetEmployees.enqueue(new TranslatedRetrofitCallback<OnSitesResponse>() {

            @Override
            public void onFinish(OnSitesResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                List<OnSite> employees = new Vector<>();
                if (exception == null && responseObject != null) {
                    // Get employees from return json
                    List<OnSite> returnedEmployees = responseObject.getData();
                    if (returnedEmployees != null & returnedEmployees.size() > 0) {
                        employees.addAll(returnedEmployees);
                    }
                }
                callback.onPostExecute(employees, exception);
            }
        });
    }

    @Override
    public void findEmployee(@NonNull final String employeeUniqId,
                             @NonNull final PCServiceCallback<Employee> callback) {
        Call<EmployeeResponse> callGetEmployee = getService().getEmployee(employeeUniqId);
        callback.setCall(callGetEmployee);

        callback.onPreExecute();
        callGetEmployee.enqueue(new TranslatedRetrofitCallback<EmployeeResponse>() {

            @Override
            public void onFinish(EmployeeResponse responseObject, PCException exception) {
                super.onFinish(responseObject, exception);

                Employee employee = null;
                if (exception == null && responseObject != null) {
                    // Get project from return json
                    employee = responseObject.getData();
                }
                callback.onPostExecute(employee, exception);
            }

        });
    }
}
