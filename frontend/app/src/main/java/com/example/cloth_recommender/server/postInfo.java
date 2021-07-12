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

    public class clothInfo {
        @SerializedName("top")
        public String top;
        @SerializedName("bot")
        public String bot;
        @SerializedName("sho")
        public String sho;
        @SerializedName("out")
        public String out;
        @SerializedName("acc")
        public String acc;
    }
}
