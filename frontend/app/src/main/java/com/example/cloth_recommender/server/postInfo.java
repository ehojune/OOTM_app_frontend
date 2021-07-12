package com.example.cloth_recommender.server;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class postInfo {
    @SerializedName("userName")
    public String userName;
    @SerializedName("userID")
    public String userID;
    @SerializedName("userBody")
    public userBody userBody;
    @SerializedName("clothInfo")
    public clothInfo clothInfo;
    @SerializedName("postgenre")
    public String postgenre;

    public class userBody {
        @SerializedName("height")
        public String height;
        @SerializedName("weight")
        public String weight;
    }

    private class clothInfo {
        @SerializedName("top")
        private String top;
        @SerializedName("bot")
        private String bot;
        @SerializedName("sho")
        private String sho;
        @SerializedName("out")
        private String out;
        @SerializedName("acc")
        private String acc;
    }
}
