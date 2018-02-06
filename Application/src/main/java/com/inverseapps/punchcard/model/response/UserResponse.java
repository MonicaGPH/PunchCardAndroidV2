package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.User;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class UserResponse extends  PCServiceResponse {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
