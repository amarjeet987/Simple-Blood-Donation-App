package com.example.amarjeet.hola;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";
    private static final int PERMISSION_LOCATION = 1;

    private TextView profile, users, notifications;
    private ViewPager mViewpager;

    private ViewPagerAdapter viewPagerAdapter;
    private Context mContext;

    //Firebase stuffs
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String uid;
    FirebaseUser user;

    //GoogleMap stuffs
    FusedLocationProviderClient locationProviderClient;
    double latitude, longitude;

    //permission
    private boolean permission = false;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(user==null)
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Intent i = new Intent(MainActivity.this, PermissionActivity.class);
                startActivity(i);
                finish();
            }
            else
            {
                uid = user.getUid();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = (TextView)findViewById(R.id.profile);
        users = (TextView)findViewById(R.id.users);
        notifications = (TextView)findViewById(R.id.notifications);
        mContext = MainActivity.this;


        mViewpager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(viewPagerAdapter);
        mViewpager.setOffscreenPageLimit(2);   //by default is 1

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewpager.setCurrentItem(2);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewpager.setCurrentItem(0);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewpager.setCurrentItem(1);
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"MessagingServiceDestorying");
        Intent i = new Intent(this,FirebaseMessagingService.class);
        stopService(i);
    }

    private void changeTabs(int position) {

            if(position==0)
            {
                users.setTextColor(getColor(R.color.white));
                users.setTextSize(15);

                profile.setTextColor(getColor(R.color.grey));
                profile.setTextSize(14);

                notifications.setTextColor(getColor(R.color.grey));
                notifications.setTextSize(14);
            }
            if(position==1)
            {
                notifications.setTextColor(getColor(R.color.white));
                notifications.setTextSize(15);

                users.setTextColor(getColor(R.color.grey));
                users.setTextSize(14);

                profile.setTextColor(getColor(R.color.grey));
                profile.setTextSize(14);
            }

            if(position==2)
            {
                profile.setTextColor(getColor(R.color.white));
                profile.setTextSize(15);

                users.setTextColor(getColor(R.color.grey));
                users.setTextSize(14);

                notifications.setTextColor(getColor(R.color.grey));
                notifications.setTextSize(14);
            }


    }
}
