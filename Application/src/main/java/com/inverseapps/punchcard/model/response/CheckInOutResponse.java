package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.CheckInOut;

/**
 * Created by Inverse, LLC on 11/2/16.
 */

public class CheckInOutResponse extends PCServiceResponse {

    private CheckInOut data;

    public CheckInOut getData() {
        return data;
    }

    public void setData(CheckInOut data) {
        this.data = data;
    }
}
