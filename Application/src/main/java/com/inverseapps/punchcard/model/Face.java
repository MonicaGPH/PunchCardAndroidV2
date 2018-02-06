
package com.inverseapps.punchcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Face {

    @SerializedName("uniq_id")
    @Expose
    private String uniqId;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private String success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

}
