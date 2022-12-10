package com.bitcnew.common.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GsonUtils {

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        if (json.isJsonNull()) {
                            return null;
                        }
                        if (!json.isJsonPrimitive()) {
                            return null;
                        }
                        try {
                            return new Date(Long.parseLong(json.getAsString()) * 1000);
                        } catch (Exception e) {
                            Log.e("Gson", e.getMessage(), e);
                        }

//                        if (json.getAsJsonPrimitive().isString()) {
//                            try {
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//                                return dateFormat.parse(json.getAsString());
//                            } catch (Exception e) {
//                                Log.e("Gson", e.getMessage(), e);
//                            }
//                        }
//
//                        try {
//                            return new Date(json.getAsJsonPrimitive().getAsLong());
//                        } catch (Exception e) {
//                            Log.e("Gson", e.getMessage(), e);
//                        }

                        return null;
                    }
                })
                .create();
    }

}
