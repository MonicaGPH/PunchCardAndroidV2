package com.inverseapps.punchcard.service;

import android.support.annotation.NonNull;


import com.inverseapps.punchcard.model.Badge;
import com.inverseapps.punchcard.model.CheckInOut;
import com.inverseapps.punchcard.model.CheckStatus;
import com.inverseapps.punchcard.model.Employee;
import com.inverseapps.punchcard.model.Face;
import com.inverseapps.punchcard.model.LogNote;

import com.inverseapps.punchcard.model.OnSite;

import com.inverseapps.punchcard.model.PresentBilling;
import com.inverseapps.punchcard.model.Project;
import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.UserCheckInOut;
import com.inverseapps.punchcard.model.param.BadgeCheckInOutParam;
import com.inverseapps.punchcard.model.param.BillingParam;
import com.inverseapps.punchcard.model.param.ChangePasswordParam;
import com.inverseapps.punchcard.model.param.FaceCheckInOutParam;
import com.inverseapps.punchcard.model.param.GeoCheckInOutParam;
import com.inverseapps.punchcard.model.param.CreateLogNoteParam;
import com.inverseapps.punchcard.model.param.PaymentParam;
import com.inverseapps.punchcard.model.param.QRCheckInOutParam;
import com.inverseapps.punchcard.model.param.SubscribeORUnSubscribeParam;

import com.inverseapps.punchcard.model.param.UpdateAvatarParam;
import com.inverseapps.punchcard.model.param.UpdateParam;
import com.inverseapps.punchcard.model.param.UpdateUserParam;

import com.inverseapps.punchcard.model.param.UpgradeParam;
import com.inverseapps.punchcard.model.PastBilling;
import com.squareup.picasso.Picasso;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Inverse, LLC on 10/18/16.
 */

public interface PCFunctionService {

    void internalStoreBasicUserInfoIfNeeded(@NonNull final String userName,
                                            @NonNull final String password,
                                            @NonNull final boolean remember);

    String getCompanyHandle();

    String getUserName();

    String getPassword();

    boolean isRememberMe();

    User getInternalStoredUser();

    Project getInternalStoredProject();

    Picasso getPicasso();

    void loadLoginRequirement(@NonNull final PCServiceCallback<Integer> callback);

    void login(@NonNull final String companyHandle,
               @NonNull final String userName,
               @NonNull final String password,
               @NonNull final PCServiceCallback<Boolean> callback);

    void logout(@NonNull final PCServiceCallback<Boolean> callback);

    void findUser(@NonNull final PCServiceCallback<User> callback);

    void logout_user(@NonNull final PCServiceCallback<Boolean> callback);

    void updateUser(@NonNull final UpdateUserParam param,
                    @NonNull final PCServiceCallback<User> callback);

    void updateAvatar(@NonNull final UpdateAvatarParam param,
                      @NonNull final PCServiceCallback<Boolean> callback);

    void changePassword(@NonNull final ChangePasswordParam param,
                        @NonNull final PCServiceCallback<Boolean> callback);

    String avatarPath(@NonNull final String userUniqId);

    void findLogNotes(@NonNull final String projectUniqId,
                      @NonNull final PCServiceCallback<List<LogNote>> callback);

    void createNewLogNote(@NonNull final CreateLogNoteParam param,
                          @NonNull final PCServiceCallback<Boolean> callback);

    void findUserCheckIns(@NonNull final PCServiceCallback<List<UserCheckInOut>> callback);

    void findCheckStatus(@NonNull final PCServiceCallback<CheckStatus> callback);

    void geoCheckIn(@NonNull final GeoCheckInOutParam param,
                    @NonNull final PCServiceCallback<Boolean> callback);

    void geoCheckOut(@NonNull final GeoCheckInOutParam param,
                     @NonNull final PCServiceCallback<Boolean> callback);

    void badgeCheckIn(@NonNull final BadgeCheckInOutParam param,
                      @NonNull final PCServiceCallback<CheckInOut> callback);

    void badgeCheckOut(@NonNull final BadgeCheckInOutParam param,
                       @NonNull final PCServiceCallback<CheckInOut> callback);

    void qrCheckIn(@NonNull final QRCheckInOutParam param,
                   @NonNull final PCServiceCallback<CheckInOut> callback);

    void qrCheckOut(@NonNull final QRCheckInOutParam param,
                    @NonNull final PCServiceCallback<CheckInOut> callback);

    void faceCheckIn(@NonNull final FaceCheckInOutParam param,
                     @NonNull final PCServiceCallback<CheckInOut> callback);

    void faceCheckOut(@NonNull final FaceCheckInOutParam param,
                      @NonNull final PCServiceCallback<CheckInOut> callback);

    void findBadge(@NonNull final String userUniqId,
                   @NonNull final String projectUniqId,
                   @NonNull final PCServiceCallback<Badge> callback);

    void findProjects(@NonNull final PCServiceCallback<List<Project>> callback);

    void findProject(@NonNull final String projectUniqId,
                     @NonNull final PCServiceCallback<Project> callback);

    void findOnSites(@NonNull final String projectUniqId,
                     @NonNull final PCServiceCallback<List<OnSite>> callback);

    void findOnSites(@NonNull final String projectUniqId,
                     @NonNull final String query,
                     final int page,
                     final int size,
                     @NonNull final PCServiceCallback<List<OnSite>> callback);

    void findEmployee(@NonNull final String employeeUniqId,
                      @NonNull final PCServiceCallback<Employee> callback);


    void findFace(@NonNull final MultipartBody.Part selfie,
                  @NonNull final RequestBody sub_domain,
                  @NonNull final RequestBody uid,
                  @NonNull final RequestBody pro_id,
                  @NonNull final PCServiceCallback<Face> callback);


    void uploadLogesTextANDImages( final MultipartBody.Part[] files,
                  @NonNull final RequestBody note,
                  @NonNull final RequestBody uniq_id,
                  @NonNull final PCServiceCallback<Boolean> callback);



    void sendPaymentDetails(@NonNull final PaymentParam param,
                            @NonNull final PCServiceCallback<Boolean> callback);


    void updatePaymentDetails(@NonNull final UpdateParam param,
                            @NonNull final PCServiceCallback<Boolean> callback);

    void sendUnSubscribtionMsg(@NonNull final SubscribeORUnSubscribeParam param,
                               @NonNull final PCServiceCallback<Boolean> callback);

    void sendupgrade(@NonNull final UpgradeParam param,
                     @NonNull final PCServiceCallback<Boolean> callback);

    void pastBillingStatus(@NonNull final BillingParam param,
                     @NonNull final PCServiceCallback<List<PastBilling>> callback);

    void presentBillingStatus(@NonNull final BillingParam param,
                           @NonNull final PCServiceCallback<PresentBilling> callback);


}
