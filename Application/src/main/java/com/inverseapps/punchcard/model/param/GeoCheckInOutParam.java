package com.inverseapps.punchcard.model.param;

import java.util.Locale;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class GeoCheckInOutParam {

    private String uniq_id;
    private String latitude;
    private String longitude;
    private String exceptionReason;
    private String checkMethod;

    public GeoCheckInOutParam(String uniq_id, double latitude, double longitude, String exceptionReason,String checkMethod) {
        this.uniq_id = uniq_id;
        this.latitude = String.format(Locale.US, "%s", latitude);
        this.longitude = String.format(Locale.US, "%s", longitude);
        this.exceptionReason = exceptionReason;
        this.checkMethod=checkMethod;
    }

}
