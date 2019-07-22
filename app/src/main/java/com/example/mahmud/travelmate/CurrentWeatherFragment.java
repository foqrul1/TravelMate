package com.example.mahmud.travelmate;


import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmud.travelmate.Interface.WeatherResponseServices;
import com.example.mahmud.travelmate.POJO.Weather.RetrofitClientSingleTon;
import com.example.mahmud.travelmate.POJO.Weather.WeatherResponse;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrentWeatherFragment extends Fragment {
    private Context mContext;
    private TextView mTempTV,mCityTV;
    private String unit = "metric";
    private ImageView mIconIMG;
    private TextView mHumidity,mDate,mDayOfWeek,mPressureTV,mTempMaxTV,mTempMinTV,mSunsetTV,
            mSunRiseTV,mRainTV,mCloudTV,mDescTV,mWindSpeedTV,mWindDegreeTV;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }

    public CurrentWeatherFragment() {
        Log.e("-------------","Current Weather Fragment created");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("--------------","cwf oncreateview called");
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTempTV = view.findViewById(R.id.temp_tv_cwf);
        mTempMaxTV = view.findViewById(R.id.max_temp_tv_cwf);
        mTempMinTV = view.findViewById(R.id.min_temp_tv_cwf);
        mSunRiseTV = view.findViewById(R.id.sunrise_tv_cwf);
        mSunsetTV = view.findViewById(R.id.sunset_tv_cwf);
        mCityTV = view.findViewById(R.id.city_tv_cwf);
        mHumidity = view.findViewById(R.id.humidity_tv_cwf);
        mPressureTV = view.findViewById(R.id.pressure_tv_cwf);
        mCloudTV = view.findViewById(R.id.cloud_tv_cwf);
        mRainTV = view.findViewById(R.id.rain_tv_cwf);
        mIconIMG = view.findViewById(R.id.icon_img_cwf);
        mDate = view.findViewById(R.id.date_tv_cwf);
        mDayOfWeek = view.findViewById(R.id.day_of_week_tv_cwf);
        mDescTV = view.findViewById(R.id.desc_tv_cwf);
        mWindDegreeTV = view.findViewById(R.id.wind_degree_tv_cwf);
        mWindSpeedTV = view.findViewById(R.id.wind_speed_tv_cwf);
    }

    public void getWeather(final Context context, double lat, double lng) {
        String apiKeyWeather2 = context.getString(R.string.api_key_weather2);
        String endUrl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", lat, lng, unit, apiKeyWeather2);
        RetrofitClientSingleTon.getService().getCurrentWeather(endUrl).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                //String tmp = String.valueOf(weatherResponse.getMain().getTemp());
                mTempTV.setText("" + weatherResponse.getMain().getTemp()+" °C");
                mHumidity.setText("" + weatherResponse.getMain().getHumidity());
                mCityTV.setText("" + weatherResponse.getName());


                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

                long sunset = weatherResponse.getSys().getSunset();
                sunset = sunset * 1000;
                Timestamp timestampSS = new Timestamp(sunset);
                Date dateSunset = new Date(timestampSS.getTime());
                Calendar calendarSS = Calendar.getInstance();
                calendarSS.setTime(dateSunset);
                String sunsetTime = format.format(calendarSS.getTime());

                mSunsetTV.setText(sunsetTime);


                long sunrise = weatherResponse.getSys().getSunrise();
                sunrise = sunrise * 1000;
                Timestamp timestampSR = new Timestamp(sunrise);
                Date dateSunrise = new Date(timestampSR.getTime());
                Calendar calendarSR = Calendar.getInstance();
                calendarSR.setTime(dateSunrise);
                String sunriseTime = format.format(calendarSR.getTime());

                mSunRiseTV.setText(sunriseTime);




                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                mDate.setText(date);
                String dayOfWeek = new SimpleDateFormat("EEEE").format(new Date());
                mDayOfWeek.setText(dayOfWeek);

                mTempMinTV.setText(weatherResponse.getMain().getTempMin()+" °C");
                mTempMaxTV.setText(weatherResponse.getMain().getTempMax()+" °C");

                mHumidity.setText(weatherResponse.getMain().getHumidity()+" %");
                mPressureTV.setText(weatherResponse.getMain().getPressure()+" hPA");

                //mRainTV.setText(weatherResponse.getRain().get3h()+" 3h");
                mCloudTV.setText("Clouds : "+weatherResponse.getClouds().getAll()+" ");

                Picasso.get().load("https://openweathermap.org/img/w/"+weatherResponse
                        .getWeather().get(0).getIcon()+".png").into(mIconIMG);
                mDescTV.setText(weatherResponse.getWeather().get(0).getDescription()+"");

                mWindSpeedTV.setText("Wind Speed : "+weatherResponse.getWind().getSpeed()+" km");
                mWindDegreeTV.setText("Wind Degree : "+weatherResponse.getWind().getDeg()+"°");

            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                //Log.e("--------------------","Full url is :"+RetrofitClientSingleTon.BASE_URL+endUrl);
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                Log.e("---------------------", "Failure Message is " + t.getMessage());
            }
        });


    }
    public void getWeatherByCity(Context context,String city){
        String apiKeyWeather2 = context.getString(R.string.api_key_weather2);
        String endUrl = String.format("weather?q=%s&units=%s&appid=%s",city, unit, apiKeyWeather2);
        RetrofitClientSingleTon.getService().getCurrentWeather(endUrl).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Toast.makeText(mContext, "CWf called", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    /*WeatherResponse weatherResponse = response.body();
                    //String tmp = String.valueOf(weatherResponse.getMain().getTemp());
                    mTempTV.setText("temp is " + weatherResponse.getMain().getTemp());
                    mHumidity.setText("Humidity " + weatherResponse.getMain().getHumidity());
                    mCityTV.setText("City " + weatherResponse.getName());*/
                    WeatherResponse weatherResponse = response.body();
                    //String tmp = String.valueOf(weatherResponse.getMain().getTemp());
                    mTempTV.setText("" + weatherResponse.getMain().getTemp()+" °C");
                    mHumidity.setText("" + weatherResponse.getMain().getHumidity());
                    mCityTV.setText("" + weatherResponse.getName());


                    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

                    long sunset = weatherResponse.getSys().getSunset();
                    sunset = sunset * 1000;
                    Timestamp timestampSS = new Timestamp(sunset);
                    Date dateSunset = new Date(timestampSS.getTime());
                    Calendar calendarSS = Calendar.getInstance();
                    calendarSS.setTime(dateSunset);
                    String sunsetTime = format.format(calendarSS.getTime());

                    mSunsetTV.setText(sunsetTime);


                    long sunrise = weatherResponse.getSys().getSunrise();
                    sunrise = sunrise * 1000;
                    Timestamp timestampSR = new Timestamp(sunrise);
                    Date dateSunrise = new Date(timestampSR.getTime());
                    Calendar calendarSR = Calendar.getInstance();
                    calendarSR.setTime(dateSunrise);
                    String sunriseTime = format.format(calendarSR.getTime());

                    mSunRiseTV.setText(sunriseTime);




                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    mDate.setText(date);
                    String dayOfWeek = new SimpleDateFormat("EEEE").format(new Date());
                    mDayOfWeek.setText(dayOfWeek);

                    mTempMinTV.setText(weatherResponse.getMain().getTempMin()+" °C");
                    mTempMaxTV.setText(weatherResponse.getMain().getTempMax()+" °C");

                    mHumidity.setText(weatherResponse.getMain().getHumidity()+" %");
                    mPressureTV.setText(weatherResponse.getMain().getPressure()+" hPA");

                    //mRainTV.setText(weatherResponse.getRain().get3h()+" 3h");
                    mCloudTV.setText("Clouds : "+weatherResponse.getClouds().getAll()+" ");

                    Picasso.get().load("https://openweathermap.org/img/w/"+weatherResponse
                            .getWeather().get(0).getIcon()+".png").into(mIconIMG);
                    mDescTV.setText(weatherResponse.getWeather().get(0).getDescription()+"");

                    mWindSpeedTV.setText("Wind Speed : "+weatherResponse.getWind().getSpeed()+" km");
                    mWindDegreeTV.setText("Wind Degree : "+weatherResponse.getWind().getDeg()+"°");
                }


            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                //Log.e("--------------------","Full url is :"+RetrofitClientSingleTon.BASE_URL+endUrl);
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                Log.e("---------------------", "Failure Message is " + t.getMessage());


                //Toast.makeText(mContext, "Failed--"+RetrofitClientSingleTon.BASE_URL+endUrl, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
