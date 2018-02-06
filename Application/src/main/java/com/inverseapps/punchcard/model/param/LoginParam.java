package com.inverseapps.punchcard.model.param;

/**
 * Created by Inverse, LLC on 12/1/16.
 */

public class LoginParam {

    private String grant_type;
    private int client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;

    public LoginParam(String username, String password) {
        this.grant_type = "password";
        this.client_id = 1;
        this.client_secret = "w1kReIyXz09b6BmPleS9bIpHDUwfqdWBrUKJww5R";
        this.username = username;
        this.password = password;
        this.scope = "*";
    }
}
