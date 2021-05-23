package com.example.pickupmeal.constants;

import com.example.pickupmeal.model.UserJ;

public class AppDataJ {

    private static AppDataJ instance;
    public String adminEmail = "admin@gmail.com";

    public static AppDataJ g() {
        if (instance == null)
            instance = new AppDataJ();
        return instance;
    }

    public UserJ currentUser;
    public Boolean isHost;

    public Boolean isHost() {
        return isHost;
    }

    public void setHost(Boolean isHost) {
        this.isHost = isHost;
    }

}
