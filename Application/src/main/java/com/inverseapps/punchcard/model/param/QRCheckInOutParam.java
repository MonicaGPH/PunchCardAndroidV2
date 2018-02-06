package com.inverseapps.punchcard.model.param;

import java.util.Locale;

/**
 * Created by Inverse, LLC on 11/2/16.
 */

public class QRCheckInOutParam {

    private String viewing_project_uniq_id;
    private String qr_provided_project_uniq_id;
    private String qr_provided_user_uniq_id;
    private String latitude;
    private String longitude;
    private String exceptionReason;

    public QRCheckInOutParam(String viewing_project_uniq_id,
                             String qr_provided_project_uniq_id,
                             String qr_provided_user_uniq_id,
                             double latitude,
                             double longitude) {
        this.viewing_project_uniq_id = viewing_project_uniq_id;
        this.qr_provided_project_uniq_id = qr_provided_project_uniq_id;
        this.qr_provided_user_uniq_id = qr_provided_user_uniq_id;
        this.latitude = String.format(Locale.US, "%s", latitude);
        this.longitude = String.format(Locale.US, "%s", longitude);
        this.exceptionReason = "QR input";
    }
}
