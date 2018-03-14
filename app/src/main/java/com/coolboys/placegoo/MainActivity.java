package com.coolboys.placegoo;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.ConnectionRequest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isService()){
            init();
        }
    }
    private void init(){
        Button btnMap = findViewById(R.id.map_btn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isService() {
        Log.d(TAG, "isService : check google service availability");
        int avail = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (avail == ConnectionResult.SUCCESS) {
            //map requests
            Log.d(TAG, "isService : Google Play service works");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avail)) {
            Log.d(TAG, "isService : error");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, avail, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this,"Map not generated", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
