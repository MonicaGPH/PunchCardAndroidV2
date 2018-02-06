package com.inverseapps.punchcard.model.param;

import com.inverseapps.punchcard.model.User;
import com.inverseapps.punchcard.model.UserJDoc;

/**
 * Created by Inverse, LLC on 10/25/16.
 */

public class UpdateUserParam {

    //private String name;
    private String first_name;
    private String last_name;
    private String email;
    private String mobileNumber;
    private String username;
    private UserJDoc jdoc;

    public UpdateUserParam(User user) {
        //this.name = user.getName();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.email = user.getEmail();
        this.mobileNumber = user.getMobileNumber();
        this.username = user.getUsername();
        this.jdoc = user.getJdoc();
    }
}
