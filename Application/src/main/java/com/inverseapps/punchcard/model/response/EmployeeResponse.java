package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.Employee;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class EmployeeResponse extends PCServiceResponse {

    private Employee data;

    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }
}
