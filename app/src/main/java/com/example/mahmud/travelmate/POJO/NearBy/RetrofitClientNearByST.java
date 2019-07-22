package com.example.mahmud.travelmate.POJO.NearBy;

import com.example.mahmud.travelmate.Interface.NearByResponseServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientNearByST {
    private static Retrofit retrofit;
    private static RetrofitClientNearByST clientNearByST;
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private RetrofitClientNearByST() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static NearByResponseServices getService(){
        if (clientNearByST == null){
            clientNearByST = new RetrofitClientNearByST();
        }
        return retrofit.create(NearByResponseServices.class);
    }
}
