package com.inverseapps.punchcard.model.param;


import java.util.Locale;

public class GeoFenceCheckInOutParam {



    private String project_uniq_id;
    private String employee_uniq_id;
    private String latitude;
    private String longitude;
    private String exceptionReason;

    public GeoFenceCheckInOutParam(String project_uniq_id,String employee_uniq_id, double latitude, double longitude, String exceptionReason) {
        this.project_uniq_id = project_uniq_id;
        this.employee_uniq_id = employee_uniq_id;
        this.latitude = String.format(Locale.US, "%s", latitude);
        this.longitude = String.format(Locale.US, "%s", longitude);
        this.exceptionReason = exceptionReason;
    }

}