package com.example.amarjeet.hola;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Amarjeet on 16-04-2018.
 */

public class LocationService extends Service {
    private FusedLocationProviderClient locationProviderClient;
    private static final String TAG = "LocationService";
    private String latitude, longitude;
    private Map<String, Object> map;
    //Firebase
    private FirebaseFirestore mfirestore;
    private String uid;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //timer
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG,"Service started");
                try {
                    Task getlocation = locationProviderClient.getLastLocation();
                    getlocation.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Location mylocation = (Location)task.getResult();
                                latitude = String.valueOf(mylocation.getLatitude());
                                longitude = String.valueOf(mylocation.getLongitude());

                                map = new HashMap<>();
                                map.put("latitude",latitude);
                                map.put("longitude",longitude);
                                Log.d(TAG,"Got location, adding to db");

                                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                                {
                                    /*----------Add stuffs to firebase-------------*/
                                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    mfirestore = FirebaseFirestore.getInstance();

                                    mfirestore.collection("Users").document(uid).update(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> dbtask) {
                                                    if(dbtask.isSuccessful())
                                                    {
                                                        Log.d(TAG, "Lat lon added to db");
                                                    }
                                                    else
                                                    {
                                                        Log.d(TAG, "Lat lon not to db");
                                                    }
                                                }
                                            });
                                }


                            }
                            else
                            {
                                Log.d(TAG, "Error" + task.getException().getMessage());
                            }
                        }
                    });
                }
                catch (SecurityException e)
                {
                    Log.d(TAG,"Error:" + e.getMessage());
                }
            }

        }, 0, 10000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
