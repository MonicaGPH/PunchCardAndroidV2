package com.inverseapps.punchcard.model.param;

import java.util.Locale;

/**
 * Created by asus on 09-Sep-17.
 */

public class FaceCheckInOutParam {

    private String viewing_project_uniq_id;
    private String qr_provided_project_uniq_id;
    private String qr_provided_user_uniq_id;
    private String latitude;
    private String longitude;
    private String exceptionReason;

    public FaceCheckInOutParam(String viewing_project_uniq_id,
                             String face_provided_project_uniq_id,
                             String face_provided_user_uniq_id,
                             double latitude,
                             double longitude) {
        this.viewing_project_uniq_id = viewing_project_uniq_id;
        this.qr_provided_project_uniq_id = face_provided_project_uniq_id;
        this.qr_provided_user_uniq_id = face_provided_user_uniq_id;
        this.latitude = String.format(Locale.US, "%s", latitude);
        this.longitude = String.format(Locale.US, "%s", longitude);
        this.exceptionReason = "Face Input";
    }
}
