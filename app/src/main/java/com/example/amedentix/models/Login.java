package com.example.amedentix.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Login {

    @SerializedName("api_token")
    private String api_token;

    @SerializedName("id")
    private String id;

    public Login(String api_token, String id) {
        this.api_token = api_token;
        this.id = id;
    }

    public String getApi_token() {
        return api_token;
    }

    public String getId() {
        return id;
    }
}
