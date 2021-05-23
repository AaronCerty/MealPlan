package com.example.pickupmeal.model;

import com.google.gson.annotations.SerializedName;

public class UserJ {

    @SerializedName("email") String email = "";
    @SerializedName("name") String name = "";
    @SerializedName("password") String password = "";
    @SerializedName("host") Boolean host = false;

    public UserJ() {
    }

    public UserJ(String email, String name, String password, Boolean host) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.host = host;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }
}
