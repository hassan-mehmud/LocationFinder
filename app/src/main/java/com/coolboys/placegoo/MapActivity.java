package com.coolboys.placegoo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by hssn- on 2/19/2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mloc) {
            getLoc();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CURSE_LOC = Manifest.permission.ACCESS_COARSE_LOCATION;
    ////////////////
    private Boolean mloc = false;
    private static final int loCode = 12;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Button saveMessage = findViewById(R.id.msg_btn);
        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fp = new Intent(getApplicationContext(),MessageActivity.class);
                startActivity(fp);
            }
        });
        locationPerm();
    }
    private void getLoc(){
        Log.d(TAG, "dev loc: get location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mloc){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "Loc found !!");
                            Location currentLoc = (Location) task.getResult();
                            saveInfo(currentLoc);
                            mcam(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()),DEFAULT_ZOOM);
                        }else {
                            Log.d(TAG, "no loc !!");
                            Toast.makeText(MapActivity.this, "not getting loc", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }catch (SecurityException e){
            Log.d(TAG, "dev loc: SecurityException" + e.getMessage());

        }
    }
    private void mcam(LatLng latLng, float zoom){
        Log.d(TAG,"movement" + latLng.latitude + ",long:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
    private void initMap(){
        Log.d(TAG, "initMap: Initializing mapooo");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }
    private void locationPerm() {
        Log.d(TAG, "perm: Permission requesting");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CURSE_LOC) == PackageManager.PERMISSION_GRANTED) {
            mloc = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions, loCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mloc = false;
        switch (requestCode) {
            case loCode: {
                if (grantResults.length > 0){
                    for (int i=0; i < grantResults.length; i++){
                        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                            mloc = false;
                            return;
                        }
                    }
                    Log.d(TAG, "perm: Granted");
                    mloc = true;
                    //start map
                    initMap();

                }
            }
        }
    }
    public void saveInfo(Location location){
        SharedPreferences sharedPreferences = getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lan", String.valueOf (location.getLongitude() ));
        editor.putString("lat", String.valueOf (location.getLatitude () ));
        editor.apply();
    }

}
