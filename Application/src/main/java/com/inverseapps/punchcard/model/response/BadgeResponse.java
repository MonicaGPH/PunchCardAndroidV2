package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.Badge;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class BadgeResponse extends PCServiceResponse {

    private Badge data;

    public Badge getData() {
        return data;
    }

    public void setData(Badge data) {
        this.data = data;
    }
}
