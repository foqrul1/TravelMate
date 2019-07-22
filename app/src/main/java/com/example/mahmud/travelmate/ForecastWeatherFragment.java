package com.example.mahmud.travelmate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mahmud.travelmate.Adapter.WeatherForcastRvAdapter;
import com.example.mahmud.travelmate.POJO.WeatherForecast.RetrofitClassSingleTonWF;
import com.example.mahmud.travelmate.POJO.WeatherForecast.WeatherForecastResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastWeatherFragment extends Fragment {
    private String unit = "metric";
    private WeatherForecastResponse weatherForecastResponses;
    private RecyclerView recyclerView;
    private List<com.example.mahmud.travelmate.POJO.WeatherForecast.ListWF> lists = new ArrayList<>();
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;

    public ForecastWeatherFragment() {
        Log.e("-------------","Forecast Weather Fragment created");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forecast_weather, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_fwf);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void getWeatherForeCast(Context context, double lat, double lng){
        String apiKeyWeather2 = context.getString(R.string.api_key_weather2);
        String endUrl = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",lat,lng,unit,apiKeyWeather2);
        RetrofitClassSingleTonWF.getService().weatherForecastResponses(endUrl).enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if (response.isSuccessful()){
                    weatherForecastResponses = response.body();
                    lists = weatherForecastResponses.getList();
                    Toast.makeText(mContext, "size is "+lists.size(), Toast.LENGTH_SHORT).show();
                    WeatherForcastRvAdapter forcastRvAdapter = new WeatherForcastRvAdapter(mContext,lists);
                    recyclerView.setAdapter(forcastRvAdapter);
                }
                
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                Toast.makeText(mContext, "Forecast Failed", Toast.LENGTH_SHORT).show();
                Log.e("----------------------","errrrrrrrrrrrrr"+t.getMessage());

            }
        });
    }
    public void getWeatherForecastByCity(Context context,String city){
        String apiKeyWeather2 = context.getString(R.string.api_key_weather2);
        String endUrl = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",city,unit,apiKeyWeather2);
        RetrofitClassSingleTonWF.getService().weatherForecastResponses(endUrl).enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                Toast.makeText(mContext, "Forecast success", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    weatherForecastResponses = response.body();
                    lists = weatherForecastResponses.getList();
                    Toast.makeText(mContext, "Size is "+lists.size(), Toast.LENGTH_SHORT).show();
                    WeatherForcastRvAdapter forcastRvAdapter = new WeatherForcastRvAdapter(mContext,lists);
                    linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(forcastRvAdapter);
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(mContext, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(mContext, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                Toast.makeText(mContext, "Forecast Failed", Toast.LENGTH_SHORT).show();
                Log.e("----------------------","errrrrrrrrrrrrr"+t.getMessage());

            }
        });
    }


}
