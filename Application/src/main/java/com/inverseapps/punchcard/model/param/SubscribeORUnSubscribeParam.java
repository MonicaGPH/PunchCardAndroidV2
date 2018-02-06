package com.inverseapps.punchcard.model.param;

/**
 * Created by asus on 27-Nov-17.
 */

public class SubscribeORUnSubscribeParam {
    private int client_id;
    private int user_id;

    public SubscribeORUnSubscribeParam(int client_id, int user_id) {
        this.client_id = client_id;
        this.user_id = user_id;
    }
}
