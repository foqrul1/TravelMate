package com.example.mahmud.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmud.travelmate.POJO.WeatherForecast.ListWF;
import com.example.mahmud.travelmate.POJO.WeatherForecast.WeatherForecastResponse;
import com.example.mahmud.travelmate.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import java.util.ArrayList;

public class WeatherForcastRvAdapter extends RecyclerView.Adapter<WeatherForcastRvAdapter.WeatherVH>{
    private List<ListWF> weatherLists = new ArrayList<>();
    private Context mContext;

    public WeatherForcastRvAdapter(Context mContext, List<ListWF> weatherLists) {
        this.weatherLists = weatherLists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WeatherVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_weather_layout,viewGroup,false);
        return new WeatherVH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull WeatherVH weatherVH, int i) {
       weatherVH.tempTV.setText(weatherLists.get(i).getMain().getTemp()+" °C");
       weatherVH.maxTempTV.setText("Max : "+weatherLists.get(i).getMain().getTempMax()+" °C");
       weatherVH.minTempTV.setText("Min : "+weatherLists.get(i).getMain().getTempMin()+" °C");

        Picasso.get().load("https://openweathermap.org/img/w/"+weatherLists.get(i).getWeather().get(0).getIcon()+".png")
                .into(weatherVH.iconIMG);
       Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE,i);

       String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
       String daysOfWeek = new SimpleDateFormat("EEEE").format(calendar.getTime());
       weatherVH.dateTV.setText(date);
       weatherVH.daysOfWeekTV.setText(daysOfWeek);
        if (i == 0){
            weatherVH.daysOfWeekTV.setText("Today");
        }
        if (i == 1){
            weatherVH.daysOfWeekTV.setText("Tomorrow");
        }
    }

    @Override
    public int getItemCount() {
        return weatherLists.size();
    }

    class WeatherVH extends RecyclerView.ViewHolder{
        private TextView tempTV,maxTempTV,dateTV,daysOfWeekTV,minTempTV;
        private ImageView iconIMG;
        public WeatherVH(@NonNull View itemView) {
            super(itemView);
            tempTV = itemView.findViewById(R.id.single_temp_tv_fwf);
            minTempTV = itemView.findViewById(R.id.single_minn_temp_tv_fwf);
            maxTempTV = itemView.findViewById(R.id.single_max_temp_tv_fwf);
            dateTV = itemView.findViewById(R.id.single_date_tv_fwf);
            daysOfWeekTV = itemView.findViewById(R.id.single_day_of_week_tv_fwf);
            iconIMG = itemView.findViewById(R.id.single_weather_ic_img);
        }
    }
}
