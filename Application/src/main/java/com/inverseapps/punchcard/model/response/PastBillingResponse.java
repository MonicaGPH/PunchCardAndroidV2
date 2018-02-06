package com.inverseapps.punchcard.model.response;



import com.inverseapps.punchcard.model.PastBilling;

import java.util.List;

/**
 * Created by asus on 05-Dec-17.
 */

public class PastBillingResponse extends PCServiceResponse {

    private List<PastBilling> data;

    public List<PastBilling> getData() {
        return data;
    }

    public void setData(List<PastBilling> data) {
        this.data = data;
    }
}
