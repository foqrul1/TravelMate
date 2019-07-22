package com.example.mahmud.travelmate;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mahmud.travelmate.Adapter.ViewPagerAdapter;
import com.example.mahmud.travelmate.Interface.BackFromWeatherFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class WeatherFragment extends Fragment {
    private static final int REQUEST_LOCATION_ACCESS_PERMISSION = 3;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentManager manager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Context mContext;
    private double mLatitude;
    private double mLongitude;
    private FusedLocationProviderClient mClient;
    private CurrentWeatherFragment mCurrentWeatherFragment;
    private ForecastWeatherFragment mForecastWeatherFragment;
    private BackFromWeatherFragment backFromWeatherFragment;

    public WeatherFragment() {
        Log.e("-------------","Weather Fragment created");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        backFromWeatherFragment = (BackFromWeatherFragment) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("latitude") && bundle.containsKey("longitude")){
            mLatitude = bundle.getDouble("latitude",0);
            mLongitude = bundle.getDouble("longitude",0);
            mCurrentWeatherFragment.getWeather(mContext,mLatitude,mLongitude);
            mForecastWeatherFragment.getWeatherForeCast(mContext,mLatitude,mLongitude);
        }
        /*Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("city")){
            String city = bundle.getString("city");
            mCurrentWeatherFragment.getWeatherByCity(mContext,city);
            mForecastWeatherFragment.getWeatherForecastByCity(mContext,city);
            return;
        }*/
        /*if (checkLocationPermission()){
            getDeviceLastLocation();
        }*/
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("-------------","Weather Fragment oncreate created");
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        mTabLayout = v.findViewById(R.id.tab_layout_wf);
        mViewPager = v.findViewById(R.id.view_pager_wf);
        mClient = LocationServices.getFusedLocationProviderClient(mContext);

        manager = getChildFragmentManager();
        mCurrentWeatherFragment = new CurrentWeatherFragment();
        mForecastWeatherFragment = new ForecastWeatherFragment();

        fragments.add(mCurrentWeatherFragment);
        fragments.add(mForecastWeatherFragment);

        mTabLayout.addTab(mTabLayout.newTab().setText("Current"));
        mTabLayout.addTab(mTabLayout.newTab().setText("7 day Forecast"));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(manager,fragments);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.setTabTextColors(Color.WHITE,Color.YELLOW);


        /*v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                    //backInEventListener.eventInBackListener();
                }
                return true;
            }
        });*/

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                    backFromWeatherFragment.onBackPressFromWeatherFragment();
                }
                return true;
            }
        });

        return v;
    }
    
    /*private boolean checkLocationPermission(){
        if (ActivityCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_ACCESS_PERMISSION);
            return false;
        }
        return true;
    }*/



    /*private void getDeviceLastLocation() {
        if (checkLocationPermission()){
            mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        //Toast.makeText(mContext, "Getting Location", Toast.LENGTH_SHORT).show();
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        mCurrentWeatherFragment.getWeather(mContext,mLatitude,mLongitude);
                        mForecastWeatherFragment.getWeatherForeCast(mContext,mLatitude,mLongitude);
                    }
                    else {
                        Toast.makeText(mContext, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/













    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //----------------
    }*/
}
