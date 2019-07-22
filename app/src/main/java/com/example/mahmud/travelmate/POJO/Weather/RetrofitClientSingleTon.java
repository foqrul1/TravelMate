package com.example.mahmud.travelmate.POJO.Weather;

import com.example.mahmud.travelmate.Interface.WeatherResponseServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientSingleTon {
    private static Retrofit retrofit;
    private static RetrofitClientSingleTon retrofitClientSingleTon;
    public final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String ICON_URL = "https://openweathermap.org/img/w/";
    private RetrofitClientSingleTon(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static WeatherResponseServices getService(){
        if (retrofitClientSingleTon == null){
            retrofitClientSingleTon = new RetrofitClientSingleTon();
        }
        return retrofit.create(WeatherResponseServices.class);
    }

}
