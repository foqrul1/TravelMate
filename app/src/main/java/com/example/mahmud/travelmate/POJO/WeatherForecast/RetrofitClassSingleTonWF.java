package com.example.mahmud.travelmate.POJO.WeatherForecast;

import com.example.mahmud.travelmate.Interface.WeatherResponseServices;
import com.example.mahmud.travelmate.POJO.Weather.RetrofitClientSingleTon;
import com.example.mahmud.travelmate.POJO.Weather.WeatherResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClassSingleTonWF {
    private static Retrofit retrofit;
    private static RetrofitClassSingleTonWF client2;
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String ICON_URL = "https://openweathermap.org/img/w/";
    private RetrofitClassSingleTonWF(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static WeatherResponseServices getService(){
        if (client2 == null){
            client2 = new RetrofitClassSingleTonWF();
        }
        return retrofit.create(WeatherResponseServices.class);
    }
}
