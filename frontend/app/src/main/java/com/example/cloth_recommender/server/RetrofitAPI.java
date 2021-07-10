package com.example.cloth_recommender.server;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @GET("/user/gets")
    Call<List<UserData>> getData();

    @POST("/user/login")
    Call<Void> putNewUser(@Body HashMap<String, String> map);

    @POST("/user/delete")
    Call<Void> deleteUser(@Body HashMap<String,String> userid);

}
