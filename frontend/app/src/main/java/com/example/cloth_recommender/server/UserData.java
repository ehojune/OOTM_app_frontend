package com.example.cloth_recommender.server;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("username")
    public String username;
    @SerializedName("email")
    public String email;
    @SerializedName("ageRange")
    public String ageRange;
    @SerializedName("gender")
    public String gender;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("postArray")
    public List<postData> postArray;

    /**
    @Override
    public String toString() {
        return
    }*/
}
class postData {
    @SerializedName("id")
    private int id;
}
