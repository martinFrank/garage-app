package com.github.martinfrank.garageapp;

import org.json.JSONException;
import org.json.JSONObject;

public class GarageGate {

    private String state;
    private String lastRequest;
    private boolean isMoving;

    public static JSONObject json() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("state", "unknown");
            obj.put("lastRequest", "unknown");
            obj.put("isMoving", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
