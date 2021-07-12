package com.example.cloth_recommender.server;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserData {

    @SerializedName("userName")
    public String userName;
    @SerializedName("userID")
    public String userID;
    @SerializedName("postArray")
    public List<postData> postArray;

}
class postData {
    @SerializedName("postID")
    private int postID;
}
