package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.PresentBilling;

/**
 * Created by asus on 06-Dec-17.
 */

public class PresentBillingResponse extends PCServiceResponse{
    private PresentBilling data;

    public PresentBilling getData() {
        return data;
    }

    public void setData(PresentBilling data) {
        this.data = data;
    }
}
