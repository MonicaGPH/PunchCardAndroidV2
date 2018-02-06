package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.Employee;

import java.util.List;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class EmployeesResponse extends PCServiceResponse {

    private List<Employee> data;

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }

}
