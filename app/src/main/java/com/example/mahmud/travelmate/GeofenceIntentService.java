package com.example.mahmud.travelmate;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceIntentService extends IntentService {
    private String channelId = "TravelMate";
    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        int transitionType = event.getGeofenceTransition();
        String transitionString = "";
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
            transitionString = "entered";
        }
        if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
            transitionString = "exited";
        }
        List<Geofence> geofenceList = event.getTriggeringGeofences();
        List<String> geofenceIds = new ArrayList<>();

        for (Geofence g : geofenceList){
            geofenceIds.add(g.getRequestId());
        }
        String notificationString = "You have "+transitionString+" "+TextUtils.join(", ",geofenceIds);
        sendNotification(notificationString);

    }

    private void sendNotification(String notificationString) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId);
        notificationBuilder.setContentTitle("Geofence detected");
        notificationBuilder.setContentText(notificationString);
        notificationBuilder.setSmallIcon(R.drawable.ic_notifications_black);
        notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "GeoFencing";
            String description = "To notify you";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId,name,importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(100,notificationBuilder.build());

    }

}
