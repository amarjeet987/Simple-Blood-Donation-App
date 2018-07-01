package com.example.amarjeet.hola;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "USERFRAGMENT";

    RecyclerView recyclerView;

    //firebase
    FirebaseFirestore fstore;
    String uid;
    String city;
    ProgressBar progressBar;
    TextView textView, not_available;
    Switch needblood;

    //Arraylist for recyclerview
    ArrayList<Users> mUsers = new ArrayList<>();
    RecycleViewAdapter adapter;

    public UserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"Userfragment Started");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        //firebase stuffs
        fstore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressBar = (ProgressBar)view.findViewById(R.id.progbar_userfrag);
        textView = (TextView)view.findViewById(R.id.textview_userfrag);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview1);
        not_available = (TextView)view.findViewById(R.id.not_available);
        adapter = new RecycleViewAdapter(mUsers, getContext(),progressBar,textView,not_available, uid);
        needblood = (Switch)view.findViewById(R.id.need_blood_switch);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.onActivityCreated(savedInstanceState);

        needblood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    //get the image link
                    fstore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            city = (String) documentSnapshot.get("city");

                            //add stuffs to the adapter
                            fstore.collection("Users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                    for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                                    {
                                        String user_id = doc.getDocument().getId();
                                        if(doc.getType() == DocumentChange.Type.ADDED)
                                        {
                                            Users users = doc.getDocument().toObject(Users.class).withId(user_id);
                                            if(!user_id.equals(uid) && city.equals(doc.getDocument().toObject(Users.class).getCity()))
                                            {
                                                Log.d(TAG, users.getName());
                                                mUsers.add(users);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                    Log.d(TAG, "Inside recyclerviewadapter");
                                    Log.d(TAG, String.valueOf(mUsers.size()));
                                    if(mUsers.size()==0)
                                    {
                                        not_available.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    });
                }
                if(!needblood.isChecked())
                {
                    mUsers.clear();
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    not_available.setVisibility(View.INVISIBLE);
                    DocumentReference ref = fstore.collection("Users").document(uid).collection("Accepts").document();
                    String myId = ref.getId();
                    Log.d(TAG, myId);
                    Map<String, Object> blank = new HashMap<>();
                    blank.put("Accepted","");
                    fstore.collection("Users").document(uid).collection("Accepts").document("accepted").set(blank).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "RECORD DELETED");
                        }
                    });
                }
            }
        });

        return view;
    }
}
