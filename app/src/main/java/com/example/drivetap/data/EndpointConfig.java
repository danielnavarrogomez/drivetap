package com.example.drivetap.data;

import org.json.JSONException;
import org.json.JSONObject;

public class EndpointConfig {
    public String id;
    public String name;
    public String url;
    public String method;
    public String username;
    public String password;
    public boolean credentialsAsParams;
    public String params;
    public String color;
    public String iconId;

    public EndpointConfig() {
        id = String.valueOf(System.currentTimeMillis());
        name = "New tap";
        url = "https://example.com";
        method = "GET";
        username = "";
        password = "";
        credentialsAsParams = false;
        params = "";
        color = "#0B6EFD";
        iconId = "bolt";
    }

    public static EndpointConfig fromJson(JSONObject json) throws JSONException {
        EndpointConfig endpoint = new EndpointConfig();
        endpoint.id = json.optString("id", endpoint.id);
        endpoint.name = json.optString("name", endpoint.name);
        endpoint.url = json.optString("url", endpoint.url);
        endpoint.method = json.optString("method", endpoint.method);
        endpoint.username = json.optString("username", "");
        endpoint.password = json.optString("password", "");
        endpoint.credentialsAsParams = json.optBoolean("credentialsAsParams", false);
        endpoint.params = json.optString("params", "");
        endpoint.color = json.optString("color", endpoint.color);
        endpoint.iconId = json.optString("iconId", endpoint.iconId);
        return endpoint;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("url", url);
        json.put("method", method);
        json.put("username", username);
        json.put("password", password);
        json.put("credentialsAsParams", credentialsAsParams);
        json.put("params", params);
        json.put("color", color);
        json.put("iconId", iconId);
        return json;
    }
}
