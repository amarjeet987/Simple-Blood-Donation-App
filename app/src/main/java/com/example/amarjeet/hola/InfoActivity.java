package com.example.amarjeet.hola;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Amarjeet on 16-04-2018.
 */

public class InfoActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        final String name = getIntent().getStringExtra("name");
        final String latitude = getIntent().getStringExtra("latitude");
        final String longitude = getIntent().getStringExtra("longitude");
        final String phno = getIntent().getStringExtra("phoneno");
        String bloodgr = getIntent().getStringExtra("bloodgr");
        String profpic = getIntent().getStringExtra("profpic");

        TextView mname = (TextView)findViewById(R.id.name);
        mname.setText(name);

        CircleImageView profimg = (CircleImageView)findViewById(R.id.profpic);
        Glide.with(this).load(profpic).asBitmap().into(profimg);

        TextView bloodgrp = (TextView)findViewById(R.id.blood_gr);
        bloodgrp.setText(bloodgr);

        Button mapview = (Button)findViewById(R.id.map);
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InfoActivity.this, MapActivity.class);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude",longitude);
                i.putExtra("name",name);
                startActivity(i);

            }
        });

        Button call = (Button)findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + phno));
                if(ActivityCompat.checkSelfPermission(InfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(InfoActivity.this, new String[] {Manifest.permission.CALL_PHONE},0);
                }
                else
                {
                    startActivity(call);
                }

            }
        });
    }
}
