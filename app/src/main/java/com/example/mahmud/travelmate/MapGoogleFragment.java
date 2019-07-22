package com.example.mahmud.travelmate;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mahmud.travelmate.Adapter.GeofenceRvAdapter;
import com.example.mahmud.travelmate.Interface.MoveCameraPosition;
import com.example.mahmud.travelmate.POJO.MapClusterItem;
import com.example.mahmud.travelmate.POJO.NearBy.Location;
import com.example.mahmud.travelmate.POJO.NearBy.Photo;
import com.example.mahmud.travelmate.POJO.NearBy.Result;
import com.example.mahmud.travelmate.POJO.TestEvent;
import com.example.mahmud.travelmate.POJO.UserGeofence;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MapGoogleFragment extends Fragment implements OnMapReadyCallback, MoveCameraPosition {
    private static final int REQUEST_LOCATION_ACCESS = 1;
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    private List<Result> fullResult;
    private ClusterManager<MapClusterItem> clusterManager;
    private Button showPlacesBT;
    private Context mContext;
    private LinearLayout mItemLinearLayout;
    private double latitudePos;
    private double longitudePos;
    private ImageView itemIMG;
    private TextView itemNameTV;
    private TextView itemLocationTV;

    private GeofencingClient geofencingClient;
    private GeofencingRequest.Builder geofenceRequestBuilder;
    private PendingIntent pendingIntent;
    private List<Geofence> geofenceList = new ArrayList<>();
    private List<UserGeofence> userGeofenceList = new ArrayList<>();

    private Button mShowGeofences;
    private String mtime;
    private String mHour;
    private String mMinute;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mGeofencesUserRef;

    private AlertDialog alertDialog;

    public MapGoogleFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_google, container, false);
        geofencingClient = LocationServices.getGeofencingClient(mContext);
        showPlacesBT = view.findViewById(R.id.show_places_bt_mgf);
        mItemLinearLayout = view.findViewById(R.id.item_ll_mgf);
        itemIMG = view.findViewById(R.id.item_img_mgf);
        itemNameTV = view.findViewById(R.id.item_name_tv_mgf);
        itemLocationTV = view.findViewById(R.id.item_location_tv_mgf);
        mShowGeofences = view.findViewById(R.id.show_geofences_list_bt_mgf);
        mShowGeofences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View dlgView = LayoutInflater.from(mContext).inflate(R.layout.geofence_dlg_layout,null);
                RecyclerView rv = dlgView.findViewById(R.id.geofence_rv_mgf);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(linearLayoutManager);
                GeofenceRvAdapter rvAdapter = new GeofenceRvAdapter(mContext,userGeofenceList
                        ,MapGoogleFragment.this);
                rv.setAdapter(rvAdapter);
                builder.setTitle("Geofence Active List");
                builder.setView(dlgView);
                //builder.show();
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //mItemLinearLayout.setVisibility(View.VISIBLE);

        showPlacesBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(getActivity()),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK){
            Place place = PlacePicker.getPlace(mContext,data);
            Toast.makeText(mContext, place.getAddress().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mGeofencesUserRef = FirebaseDatabase.getInstance().getReference().child("Geofences").child(mUser.getUid());
        if (getActivity() != null){
            SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (fragment != null){
                fragment.getMapAsync(this);
            }
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        mGeofencesUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                geofenceList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    UserGeofence newUserGeofence = d.getValue(UserGeofence.class);
                    try {
                        Date date = dateFormat.parse(newUserGeofence.getStoppingTime());
                        Date today = new Date();
                        long diff = date.getTime() - today.getTime();
                        if (diff > 0){
                            drawCircleAndAddGeofence(newUserGeofence.getLatitude(),newUserGeofence.getLongitude(),newUserGeofence.getName(),
                                    newUserGeofence.getRadius());
                            userGeofenceList.add(newUserGeofence);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        if (checkLocationPermission()){
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Add new Geofence");
                View view = LayoutInflater.from(mContext).inflate(R.layout.map_geofences_layout_dlg,null);
                final EditText titleET = view.findViewById(R.id.title_et_dlg_mgf);
                final EditText radiusET = view.findViewById(R.id.radius_et_dlg_mgf);
                final Button timeLeftBT = view.findViewById(R.id.time_bt_dlg_mgf);


                final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        int remainingH = hourOfDay - calendar.get(Calendar.HOUR_OF_DAY);
                        int remainingM = minute - calendar.get(Calendar.MINUTE);
                        String minuteInSt = "";
                        String hourInSt = "";

                        if (remainingM < 0){
                            remainingM = 60 + remainingM;
                            remainingH = remainingH -1;
                        }
                        if (remainingH < 0){
                            remainingH = 24 + remainingH;
                        }


                        if (remainingM < 10){
                            minuteInSt = "0"+remainingM;
                        } else {
                            minuteInSt = String.valueOf(remainingM);
                        }
                        mMinute = minuteInSt;

                        if (remainingH < 10){
                            hourInSt = "0"+remainingH;
                        } else {
                            hourInSt = String.valueOf(remainingH);
                        }
                        mHour = hourInSt;

                        String timeInSt = hourInSt+":"+minuteInSt;
                        mtime = timeInSt;
                        timeLeftBT.setText(timeInSt);

                    }
                };

                timeLeftBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hOfD = calendar.get(Calendar.HOUR_OF_DAY);
                        int mOfH = calendar.get(Calendar.MINUTE);
                        TimePickerDialog tdlg = new TimePickerDialog(mContext,timeSetListener,hOfD,mOfH,false);
                        tdlg.show();
                    }
                });
                builder.setView(view);
                builder.setNegativeButton("cancel",null)
                        .setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = titleET.getText().toString();
                                String radius = radiusET.getText().toString();
                                String time = "";
                                time = timeLeftBT.getText().toString();
                                if (title.isEmpty() || radius.isEmpty() || time.isEmpty() || (time.length() > 6)){
                                    return;
                                }
                                createGeoFences(latLng,radius,title,mHour,mMinute);
                            }
                        });
                builder.show();


                /*mMap.addMarker(new MarkerOptions().position(latLng).title("You are here")
                .snippet(latLng.latitude+", "+latLng.longitude))
                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));*/
            }
        });


        Bundle bundle = getArguments();



        if (bundle != null && bundle.containsKey("Result") && bundle.containsKey("Latitude") && bundle.containsKey("Longitude")){
            latitudePos = bundle.getDouble("Latitude",0);
            longitudePos = bundle.getDouble("Longitude",0);
            Result result = (Result) bundle.getSerializable("Result");
            mItemLinearLayout.setVisibility(View.VISIBLE);
            if (result != null){
                String urlPrefix = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                List<Photo> photos = result.getPhotos();
                String apiKeyNearBy = getString(R.string.api_key_nearby);
                String photoref = photos.get(0).getPhotoReference();
                String photoUrl = String.format("%s%s&key=%s",urlPrefix,photoref,apiKeyNearBy);
                itemNameTV.setText(result.getName());
                itemLocationTV.setText(result.getVicinity());
                Picasso.get().load(photoUrl).into(itemIMG);
                LatLng llatllong = new LatLng(result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng());
                mMap.addMarker(new MarkerOptions().position(llatllong).title(result.getName()).snippet(result.getVicinity()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(llatllong,16f));
                return;


            }


        }




        if (bundle != null && bundle.containsKey("FullResult") && bundle.containsKey("Latitude") && bundle.containsKey("Longitude")){
            clusterManager = new ClusterManager<>(getActivity(),mMap);
            mMap.setOnCameraIdleListener(clusterManager);
            mMap.setOnMarkerClickListener(clusterManager);
            latitudePos = bundle.getDouble("Latitude",0);
            longitudePos = bundle.getDouble("Longitude",0);
            LatLng latLngPosition = new LatLng(latitudePos,longitudePos);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngPosition,14f));
            fullResult = (List<Result>) bundle.getSerializable("FullResult");
            if (fullResult != null){
                for (Result r:fullResult){
                    String title = r.getName();
                    String vicinity = r.getVicinity();
                    Location location = r.getGeometry().getLocation();
                    double latitude = location.getLat();
                    double longitude = location.getLng();
                    LatLng latLng = new LatLng(latitude,longitude);
                    MapClusterItem clusterItem = new MapClusterItem(latLng,title,vicinity);
                    clusterManager.addItem(clusterItem);
                    clusterManager.cluster();
                }
                /*mMap.addMarker(new MarkerOptions().position(new LatLng(latitudePos,latitudePos)).
                        title("Your Location").snippet("You are Here").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));*/

            } else {
                //
            }
            return;
        }
        if (bundle != null && bundle.containsKey("Latitude") && bundle.containsKey("Longitude")){
            LatLng pos = new LatLng(bundle.getDouble("Latitude",0),bundle.getDouble("Longitude",0));
            mMap.addMarker(new MarkerOptions().position(pos).title("Your Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,16f));
            /*UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            if (checkLocationPermission()){
                mMap.setMyLocationEnabled(true);
            }*/
        }


    }

    private void drawCircleAndAddGeofence(String latitude, String longitude, String name, String radius) {
        LatLng newLatlng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        mMap.addCircle(new CircleOptions()
                .center(newLatlng)
                .radius(Integer.parseInt(radius))
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2));

        mMap.addMarker(new MarkerOptions()
                .position(newLatlng).title(name)
                .snippet(newLatlng.latitude+", "+newLatlng.longitude))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        Geofence nGeofence = new Geofence.Builder()
                .setCircularRegion(Double.parseDouble(latitude),Double.parseDouble(longitude),Float.parseFloat(radius))
                .setRequestId(name)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(1 * 60 * 60 * 1000)
                .build();
        geofenceList.add(nGeofence);

    }

    private void createGeoFences(LatLng latLng, String radius, String title, String hour, String minute) {
        int totalTime = (Integer.parseInt(hour) * 60 + Integer.parseInt(minute)) * 60 * 1000;
        Geofence geofence = new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,Float.parseFloat(radius))
                .setRequestId(title)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(totalTime)
                .build();
        geofenceList.add(geofence);


        //Adding GeoFencing Service-----------------------
        if (checkLocationPermission()){
            geofencingClient.addGeofences(getGeofencignRequest(),getPendingIntent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mContext, "Geofences added successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(Integer.parseInt(radius))
                .fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);
        mMap.addCircle(circleOptions);
        mMap.addMarker(new MarkerOptions()
                .position(latLng).title(title)
                .snippet(latLng.latitude+", "+latLng.longitude))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        String key = mGeofencesUserRef.push().getKey();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
        calendar.add(Calendar.MINUTE,Integer.parseInt(minute));
        String dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(calendar.getTime());
        UserGeofence userGeofence = new UserGeofence(title,key,dateTime,radius,
                String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
        mGeofencesUserRef.child(key).setValue(userGeofence);


    }
    private GeofencingRequest getGeofencignRequest(){
        geofenceRequestBuilder = new GeofencingRequest.Builder();
        geofenceRequestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        geofenceRequestBuilder.addGeofences(geofenceList);
        return geofenceRequestBuilder.build();

    }

    private PendingIntent getPendingIntent(){
        if (pendingIntent != null){
            return pendingIntent;
        }
        Intent intent = new Intent(mContext,GeofenceIntentService.class);
        pendingIntent = PendingIntent.getService(mContext,111,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_ACCESS);
            return false;
        }
        return true;
    }

    @Override
    public void moveCamera(LatLng latLng) {
        alertDialog.cancel();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
    }
}
