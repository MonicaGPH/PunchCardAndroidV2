package com.inverseapps.punchcard.service.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class GsonDateFormatAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final DateFormat primaryDf;
    private final DateFormat secondaryDf;
    private final DateFormat anotherDf;

    public GsonDateFormatAdapter() {

        TimeZone tz = TimeZone.getTimeZone("UTC");

        primaryDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US);
        primaryDf.setTimeZone(tz);

        secondaryDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        secondaryDf.setTimeZone(tz);

        anotherDf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        anotherDf.setTimeZone(tz);
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return primaryDf.parse(json.getAsString());
        } catch (ParseException exPrimary) {
            try {
                return secondaryDf.parse(json.getAsString());
            } catch (ParseException exSecondary) {
                try {
                    return anotherDf.parse(json.getAsString());
                } catch (ParseException anotherEx) {
                    return null;
                }
            }
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(primaryDf.format(src));
    }
}
