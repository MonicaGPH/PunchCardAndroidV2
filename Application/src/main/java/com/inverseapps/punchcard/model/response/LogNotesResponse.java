package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.LogNote;

import java.util.List;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class LogNotesResponse extends PCServiceResponse {

    private List<LogNote> data;

    public List<LogNote> getData() {
        return data;
    }

    public void setData(List<LogNote> data) {
        this.data = data;
    }
}
