package com.inverseapps.punchcard.model.param;

import java.util.Locale;

/**
 * Created by Inverse, LLC on 11/2/16.
 */

public class BadgeCheckInOutParam {

    private String uniq_id;
    private String latitude;
    private String longitude;
    private int badge_id;
    private String exceptionReason;

    public BadgeCheckInOutParam(String uniq_id,
                                double latitude,
                                double longitude,
                                int badge_id) {
        this.uniq_id = uniq_id;
        this.latitude = String.format(Locale.US, "%s", latitude);
        this.longitude = String.format(Locale.US, "%s", longitude);
        this.badge_id = badge_id;
        this.exceptionReason = "Badge input";
    }
}
