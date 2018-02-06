package com.inverseapps.punchcard.model.param;

/**
 * Created by Inverse, LLC on 10/21/16.
 */

public class CreateLogNoteParam {

    private String note;
    private String uniq_id;

    public CreateLogNoteParam(String note, String projectUniqId) {
        this.note = note;
        this.uniq_id = projectUniqId;
    }

}
