package com.example.mahmud.travelmate.Interface;

import com.example.mahmud.travelmate.POJO.NearBy.NearByPlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearByResponseServices {
    @GET
    Call<NearByPlacesResponse> getNearByService(@Url String endUrl);
}
