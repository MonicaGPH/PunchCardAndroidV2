package com.inverseapps.punchcard.model.param;

/**
 * Created by Inverse, LLC on 11/7/16.
 */

public class ChangePasswordParam {

    private String current_password;
    private String new_password;
    private String new_password_confirmation;

    public ChangePasswordParam(String current_password, String new_password, String new_password_confirmation) {
        this.current_password = current_password;
        this.new_password = new_password;
        this.new_password_confirmation = new_password_confirmation;
    }
}
