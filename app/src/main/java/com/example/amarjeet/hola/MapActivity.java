package com.example.amarjeet.hola;
import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int PERMISSION_LOCATION = 1;

    private GoogleMap mMap;
    Double lat, lon;
    String name;
    float zoom;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lat = Double.parseDouble(getIntent().getStringExtra("latitude"));
        lon = Double.parseDouble(getIntent().getStringExtra("longitude"));
        name = getIntent().getStringExtra("name");
        zoom = 15f;
        mContext = MapActivity.this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker
        LatLng latLng = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }
}