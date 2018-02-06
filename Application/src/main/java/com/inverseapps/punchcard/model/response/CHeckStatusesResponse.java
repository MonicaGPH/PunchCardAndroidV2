package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.CheckStatus;


import java.util.List;

/**
 * Created by asus on 29-Aug-17.
 */

public class CHeckStatusesResponse extends PCServiceResponse {



    private List<CheckStatus> data;



    public List<CheckStatus> getData() {
        return data;
    }

    public void setData(List<CheckStatus> data) {
        this.data = data;
    }
}

