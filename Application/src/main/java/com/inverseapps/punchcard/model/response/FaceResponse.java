package com.inverseapps.punchcard.model.response;

import com.inverseapps.punchcard.model.Face;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by asus on 08-Sep-17.
 */


public class FaceResponse  {

    private Face data;


    public Face getData() {
        return data;
    }

    public void setData(Face data) {
        this.data = data;
    }
}