package com.inverseapps.punchcard.service.retrofit;


import com.inverseapps.punchcard.model.Face;

import com.inverseapps.punchcard.model.OAuth;

import com.inverseapps.punchcard.model.param.BadgeCheckInOutParam;
import com.inverseapps.punchcard.model.param.BillingParam;
import com.inverseapps.punchcard.model.param.ChangePasswordParam;
import com.inverseapps.punchcard.model.param.FaceCheckInOutParam;
import com.inverseapps.punchcard.model.param.GeoCheckInOutParam;
import com.inverseapps.punchcard.model.param.CreateLogNoteParam;
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
import com.inverseapps.punchcard.model.response.PastBillingResponse;
import com.inverseapps.punchcard.model.response.PaymentResponse;
import com.inverseapps.punchcard.model.response.PresentBillingResponse;
import com.inverseapps.punchcard.model.response.ProjectResponse;
import com.inverseapps.punchcard.model.response.ProjectsResponse;
import com.inverseapps.punchcard.model.response.SubscribeORUnSubscribeResponse;
import com.inverseapps.punchcard.model.response.UpgradeResponse;
import com.inverseapps.punchcard.model.response.UserCheckInOutsResponse;
import com.inverseapps.punchcard.model.response.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public interface PCRetrofitService {

    String API_END_POINT_FORMAT = "http://%s.%s/api/v1.0/";
    String SERVER_PATH_PAYMENT = "http://%s/api/crons/";
    String SERVER_PATH = "http://%s/webservice/";
    String API_LOGOUT = "http://%s.%s/";

    @POST("oauth/token")
    Call<OAuth> getOAuth(@Body LoginParam param);

    @GET("user")
    Call<UserResponse> getUser();

    @PUT("user")
    Call<UserResponse> updateUser(@Body UpdateUserParam param);

    /*
    * => Use Picasso
    * @GET("user/avatar")
    */
    @GET("logout")
    Call<Void> logout_user();

    @PUT("user/avatar")
    Call<PCServiceResponse> updateAvatar(@Body UpdateAvatarParam param);

    @PUT("user/password")
    Call<PCServiceResponse> changePassword(@Body ChangePasswordParam param);

    /*
    * => Use Picasso
    * @GET("user/avatar/{userUniqId}")
    */

    @GET("projects/{projectUniqId}/logs")
    Call<LogNotesResponse> getLogs(@Path("projectUniqId") String projectUniqId);

    @POST("user/logs")
    Call<PCServiceResponse> createLogNote(@Body CreateLogNoteParam param);

    @POST("projects/{projectUniqId}/logs")
    Call<PCServiceResponse> createLogNote(@Path("projectUniqId") String projectUniqId,
                                          @Body CreateLogNoteParam param);

    @GET("user/checkins")
    Call<UserCheckInOutsResponse> getUserCheckIns();

    @GET("user/check")
    Call<CheckStatusResponse> getCheckStatus();

    @POST("user/check")
    Call<CheckInOutResponse> geoCheckIn(@Body GeoCheckInOutParam param);

    @PUT("user/check")
    Call<CheckInOutResponse> geoCheckOut(@Body GeoCheckInOutParam param);

    @POST("check/badge")
    Call<CheckInOutResponse> badgeCheckIn(@Body BadgeCheckInOutParam param);

    @PUT("check/badge")
    Call<CheckInOutResponse> badgeCheckOut(@Body BadgeCheckInOutParam param);

    @POST("check/qr")
    Call<CheckInOutResponse> qrCheckIn(@Body QRCheckInOutParam param);

    @POST("check/qr")
    Call<CheckInOutResponse> faceCheckIn(@Body FaceCheckInOutParam param);

    @PUT("check/qr")
    Call<CheckInOutResponse> faceCheckOut(@Body FaceCheckInOutParam param);

    @PUT("check/qr")
    Call<CheckInOutResponse> qrCheckOut(@Body QRCheckInOutParam param);

    @GET("user/badge/{userUniqId}/{projectUniqId}")
    Call<BadgeResponse> getBadge(@Path("userUniqId") String userUniqId,
                                 @Path("projectUniqId") String projectUniqId);

    /*
    * => Use Picasso
    * @GET("user/qr/{userUniqId}/{projectUniqId}")
    */

    @GET("projects")
    Call<ProjectsResponse> getProjects();

    @GET("projects/{projectUniqId}")
    Call<ProjectResponse> getProject(@Path("projectUniqId") String projectUniqId);

    @GET("projects/{projectUniqId}/employees")
    Call<OnSitesResponse> getEmployeeInfoOnSites(@Path("projectUniqId") String projectUniqId);

    @GET("search/projects/{projectUniqId}")
    Call<OnSitesResponse> findEmployeeInfoOnSites(@Path("projectUniqId") String projectUniqId,
                                                  @Query("q") String query,
                                                  @Query("p") int page,
                                                  @Query("n") int size);

    @GET("employee/{employeeUniqId}")
    Call<EmployeeResponse> getEmployee(@Path("employeeUniqId") String employeeUniqId);

    @Multipart
    @POST("facedetect.php")
    Call<Face> findFace(@Part MultipartBody.Part selfie,
                        @Part("sub_domain") RequestBody sub_domain,
                        @Part("uid") RequestBody uid,
                        @Part("pro_id") RequestBody pro_id);

    @Multipart
    @POST("user/logs")
    Call<PCServiceResponse> uploadLogesTextANDImages(@Part MultipartBody.Part[] files,
                                                 @Part ("note") RequestBody note,
                                                 @Part("uniq_id") RequestBody uniq_id);
    @POST("punchcardSubscription")
    Call<PaymentResponse> sendPaymentDetails(@Body PaymentParam param);

    @POST("updateCC")
    Call<PaymentResponse> updatePaymentDetails(@Body UpdateParam param);

    @POST("punchcardUnsubscribe")
    Call<SubscribeORUnSubscribeResponse> sendUnSubscribtionMsg(@Body SubscribeORUnSubscribeParam param);


    @POST("punchcardupgrade")
    Call<UpgradeResponse> sendupgrade(@Body UpgradeParam param);

    @POST("punch_history")
    Call<PastBillingResponse> pastBillingStatus(@Body BillingParam param);

    @POST("currentMonthPunch")
    Call<PresentBillingResponse> presentBillingStatus(@Body BillingParam param);

}
