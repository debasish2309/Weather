package com.example.devde.myapplication.Rest;

import com.example.devde.myapplication.Model.mainWeather;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("data/2.5/weather?")
    Call<mainWeather> getCurrentWeather(@Query("lat") String lat,@Query("lon") String lon,@Query("appid") String appid);
}
