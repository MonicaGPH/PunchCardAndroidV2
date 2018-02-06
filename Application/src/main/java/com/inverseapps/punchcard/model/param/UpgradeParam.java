package com.inverseapps.punchcard.model.param;

/**
 * Created by asus on 28-Nov-17.
 */

public class UpgradeParam  {
    private int client_id;
    private int user_id;
    private String version;

    public UpgradeParam(int client_id, int user_id, String version) {
        this.client_id = client_id;
        this.user_id = user_id;
        this.version = version;
    }
}
