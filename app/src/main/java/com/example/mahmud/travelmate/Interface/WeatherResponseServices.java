package com.example.mahmud.travelmate.Interface;

import com.example.mahmud.travelmate.POJO.Weather.WeatherResponse;
import com.example.mahmud.travelmate.POJO.WeatherForecast.WeatherForecastResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherResponseServices {
    @GET
    Call<WeatherResponse> getCurrentWeather(@Url String endUrl);
    @GET
    Call<WeatherForecastResponse> weatherForecastResponses(@Url String endUrl);
}
