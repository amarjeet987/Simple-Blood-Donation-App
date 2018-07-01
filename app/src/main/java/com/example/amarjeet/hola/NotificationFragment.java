package com.example.amarjeet.hola;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "NotifFrag";

    //recyclerview
    RecyclerView recyclerView;
    RecycleViewAdapter2 recycleViewAdapter2;
    ArrayList<Accepted> accepted;

    //layout
    ProgressBar progressBar;
    TextView loading, msg;
    Switch aswitch;
    int j;

    //Firebase
    FirebaseFirestore firestore;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        //firebase
        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //basic stuffs
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview1);
        accepted = new ArrayList<>();
        progressBar = (ProgressBar)view.findViewById(R.id.progbar_notifrag);
        loading = (TextView)view.findViewById(R.id.textview_notifrag);
        msg = (TextView)view.findViewById(R.id.not_available);
        aswitch = (Switch)view.findViewById(R.id.check_accept);

        recycleViewAdapter2 = new RecycleViewAdapter2(recyclerView,firestore,accepted,getContext(),progressBar,msg,loading);
        recyclerView.setAdapter(recycleViewAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        aswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTaskModified(recycleViewAdapter2,getContext(),progressBar,msg,loading){
                        @Override
                        public void run() {
                            firestore.collection("Users").document(uid).collection("Accepts").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                                    {
                                        if(doc.getType() == DocumentChange.Type.ADDED)
                                        {
                                            Log.d(TAG, "");
                                            Accepted id = doc.getDocument().toObject(Accepted.class);

                                            if(id.getAccepted().equals(""))
                                            {
                                                continue;
                                            }
                                            else
                                            {
                                                accepted.add(id);
                                                recycleViewAdapter2.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                }
                            });
                            accepted.clear();
                        }

                    },0,10000);

                }
                if(!aswitch.isChecked())
                {
                    Log.d(TAG, "Inside !aswitch.isChecked()");
                    accepted.clear();
                    recycleViewAdapter2.notifyDataSetChanged();
                    recyclerView.setVisibility(View.INVISIBLE);
                    recycleViewAdapter2.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });


        return view;
    }



}
