package com.example.pickupmeal.model;

import com.google.gson.annotations.SerializedName;

public class MealJ {

    @SerializedName("id") String id = "";
    @SerializedName("status") String status = "";

    public MealJ() {
    }

    public MealJ(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
