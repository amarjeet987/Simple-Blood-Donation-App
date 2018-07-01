package com.example.amarjeet.hola;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amarjeet on 18-04-2018.
 */

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifactivity);

        TextView textView = (TextView) findViewById(R.id.txtview_name);

        String message = getIntent().getStringExtra("message");
        final String sender_id = getIntent().getStringExtra("sender_id");
        String name = getIntent().getStringExtra("name");
        String phno = getIntent().getStringExtra("phno");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        textView.setText("Message: " + message + "    Sender_ID: " + sender_id + "    Name:" + name + "       phno:" + phno);

        Button accept = (Button) findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firestore;
                firestore = FirebaseFirestore.getInstance();

                Map<String, Object> map = new HashMap<>();
                map.put("Accepted", FirebaseAuth.getInstance().getCurrentUser().getUid());

                firestore.collection("Users").document(sender_id).collection("Accepts").document("accepted").update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added");
                        finish();
                    }
                });
            }
        });

        Button decline  = (Button)findViewById(R.id.decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView.setText(name);
    }
}