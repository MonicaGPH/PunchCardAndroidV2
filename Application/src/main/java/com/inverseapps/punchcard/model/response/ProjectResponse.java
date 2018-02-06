package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.Project;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class ProjectResponse extends PCServiceResponse {

    private Project data;

    public Project getData() {
        return data;
    }

    public void setData(Project data) {
        this.data = data;
    }
}
