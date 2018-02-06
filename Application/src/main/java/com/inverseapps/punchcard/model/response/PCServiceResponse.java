package com.inverseapps.punchcard.model.response;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class PCServiceResponse {

    private String result;
    private String message;
    private int http_code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttp_code() {
        return http_code;
    }

    public void setHttp_code(int http_code) {
        this.http_code = http_code;
    }
}
