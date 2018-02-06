package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.CheckStatus;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class CheckStatusResponse extends PCServiceResponse {

    private CheckStatus data;

    public CheckStatus getData() {
        return data;
    }

    public void setData(CheckStatus data) {
        this.data = data;
    }
}
