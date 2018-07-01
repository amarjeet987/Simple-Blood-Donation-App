package com.example.amarjeet.hola;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ProfileFragment";
    Button signout;
    CircleImageView circleImageView;
    TextView username;
    ProgressBar progressBar;
    Switch location;
    //Firebase
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String uid;
    TextView assure;

    //constructor
    public ProfileFragment() {}



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        username = (TextView) view.findViewById(R.id.uname);
        signout = (Button)view.findViewById(R.id.signout_btn);
        circleImageView =(CircleImageView)view.findViewById(R.id.profpic);
        progressBar = (ProgressBar)view.findViewById(R.id.progbar);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //get the document from firebase
        firebaseFirestore.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Now we are inside the document, and the snapshot of the document is stored in documentSnapshot
                String user_name = documentSnapshot.getString("name");
                String profpic_uri = documentSnapshot.getString("image");

                //set uname and profpic
                username.setText(user_name);

                Glide.with(container.getContext()).load(profpic_uri)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(circleImageView);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> token_id_remove = new HashMap<>();
                token_id_remove.put("token_id", FieldValue.delete());

                firebaseFirestore.collection("Users").document(uid).update(token_id_remove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        getActivity().stopService(new Intent(getActivity(), LocationService.class));
                        Intent i = new Intent(container.getContext(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
            }
        });

        location = (Switch)view.findViewById(R.id.locate);

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    getActivity().startService(new Intent(getActivity(), LocationService.class));
                }
                else
                {
                    getActivity().stopService(new Intent(getActivity(), LocationService.class));
                }

            }
        });

        return view;
    }

}
