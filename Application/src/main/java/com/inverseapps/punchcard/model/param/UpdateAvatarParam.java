package com.inverseapps.punchcard.model.param;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class UpdateAvatarParam {

    String image;

    public UpdateAvatarParam(String imageBase64String) {
        image = imageBase64String;
    }

}
