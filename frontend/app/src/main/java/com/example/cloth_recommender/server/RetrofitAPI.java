package com.example.cloth_recommender.server;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/user/gets")
    Call<List<UserData>> getData();

    @GET("/user/getuser/{userID}")
    Call<UserData> getUser(@Path("userID") String userID);

    @GET("/post/getpost/{postID}")
    Call<postInfo> getPost(@Path("postID") String postID);

    @GET("/post/getpostid")
    Call<List<String>> getPostID();

    @POST("/user/login")
    Call<Void> putNewUser(@Body HashMap<String, String> map);

    @POST("/user/delete")
    Call<Void> deleteUser(@Body HashMap<String,String> userid);

    @POST("/post/add")
    Call<Void> addPost(@Body HashMap<String,String> postmap);

    @POST("/post/delete")
    Call<Void> deletePost(@Body HashMap<String,String> postid);

    @POST("/post/setlike")
    Call<Void> setLike(@Body HashMap<String,String> idmap);

    @POST("/post/setmark")
    Call<Void> setMark(@Body HashMap<String,String> idmap);

    @GET("/post/getlike/{postID}")
    Call<List<String>> getLike(@Path("postID") String postID);

    @GET("/post/getmark/{postID}")
    Call<List<String>> getMark(@Path("postID") String postID);


}
