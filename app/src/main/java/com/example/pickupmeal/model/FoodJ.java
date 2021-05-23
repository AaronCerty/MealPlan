package com.example.pickupmeal.model;

import com.google.gson.annotations.SerializedName;

public class FoodJ {

    @SerializedName("name") String name = "";
    @SerializedName("calo") int calo = 0;

    public FoodJ() {
    }

    public FoodJ(String name, int calo) {
        this.name = name;
        this.calo = calo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalo() {
        return calo;
    }

    public void setCalo(int calo) {
        this.calo = calo;
    }
}
