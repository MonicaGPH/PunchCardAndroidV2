package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.OnSite;

import java.util.List;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class OnSitesResponse extends PCServiceResponse {

    private List<OnSite> data;

    public List<OnSite> getData() {
        return data;
    }

    public void setData(List<OnSite> data) {
        this.data = data;
    }
}
