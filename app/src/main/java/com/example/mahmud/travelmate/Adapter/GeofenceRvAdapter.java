package com.example.mahmud.travelmate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mahmud.travelmate.Interface.MoveCameraPosition;
import com.example.mahmud.travelmate.MapGoogleFragment;
import com.example.mahmud.travelmate.POJO.UserGeofence;
import com.example.mahmud.travelmate.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeofenceRvAdapter extends RecyclerView.Adapter<GeofenceRvAdapter.GeofenctVH>{
    private Context mContext;
    private List<UserGeofence> userGeofenceList = new ArrayList<>();
    private MoveCameraPosition moveCameraPosition;

    public GeofenceRvAdapter(Context mContext, List<UserGeofence> userGeofenceList, MapGoogleFragment mapGoogleFragment) {
        this.mContext = mContext;
        this.userGeofenceList = userGeofenceList;
        moveCameraPosition = mapGoogleFragment;
    }

    @NonNull
    @Override
    public GeofenctVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_geofence_info_layout,viewGroup,false);
        return new GeofenctVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GeofenctVH geofenctVH, final int i) {
        geofenctVH.titleTV.setText("Title : "+userGeofenceList.get(i).getName());
        String dateTime = userGeofenceList.get(i).getStoppingTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date currentDate = new Date();
        try {
            Date stopDate = format.parse(dateTime);
            long diff = stopDate.getTime() - currentDate.getTime();
            int seconds = (int) (diff / 1000) % 60 ;
            int minutes = (int) ((diff / (1000*60)) % 60);
            int hours   = (int) ((diff / (1000*60*60)) % 24);
            geofenctVH.timeTV.setText("Remaining : "+hours+":"+minutes+":"+seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        geofenctVH.goBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraPosition.moveCamera(new LatLng(Double.parseDouble(userGeofenceList.get(i).getLatitude())
                        ,Double.parseDouble(userGeofenceList.get(i).getLongitude())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userGeofenceList.size();
    }

    class GeofenctVH extends RecyclerView.ViewHolder{
        TextView titleTV,timeTV;
        Button goBT;
        public GeofenctVH(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.single_geofence_name_tv_mgf);
            timeTV = itemView.findViewById(R.id.single_geofence_time_tv_mgf);
            goBT = itemView.findViewById(R.id.go_bt_single_mgf);
        }
    }

}
