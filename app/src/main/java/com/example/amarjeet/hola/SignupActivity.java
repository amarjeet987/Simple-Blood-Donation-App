package com.example.amarjeet.hola;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Amarjeet on 14-04-2018.
 */

public class SignupActivity extends AppCompatActivity{
    private static final String TAG = "SIGNUP ACTIVITY";

    String mname, memail, mpassword, mcity, mphno, mbldgr;

    EditText name, email, password, city, phno, bldgr;
    Button signup;
    CircleImageView profileimg;
    Context mContext;
    Uri imguri;
    ProgressBar progbar_signup;
    TextView tnc;

    private static final int PROFILE_IMG = 1;

    //firebase
    private FirebaseAuth mAuth;                         //logging in and stuffs
    private StorageReference mStorage;                  //storing the image
    private FirebaseFirestore firebaseFirestore;        //storing other info

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText)findViewById(R.id.sname);
        email = (EditText)findViewById(R.id.semail);
        password = (EditText)findViewById(R.id.spassword);
        phno = (EditText)findViewById(R.id.sphno);
        profileimg = (CircleImageView)findViewById(R.id.profileimg);
        signup = (Button)findViewById(R.id.signup_button);
        bldgr = (EditText)findViewById(R.id.blood_group);
        city = (EditText)findViewById(R.id.scity);
        tnc = (TextView)findViewById(R.id.tnc);
        mContext = SignupActivity.this;
        imguri = null;
        //progress bar
        progbar_signup = (ProgressBar)findViewById(R.id.progbar_signup);
        //firebase
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, TermsnCond.class);
                startActivity(i);
            }
        });

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takepermission();
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), PROFILE_IMG);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mname = name.getText().toString();
                memail = email.getText().toString();
                mpassword = password.getText().toString();
                mcity = city.getText().toString();
                mphno = phno.getText().toString();
                mbldgr = bldgr.getText().toString();

                if(imguri != null && !mname.equals("") && !memail.equals("") && !mpassword.equals("")
                        && !mcity.equals("") && !mphno.equals("") && !mbldgr.equals(""))
                {
                    progbar_signup.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(memail, mpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "Inside on complete");
                            if(task.isSuccessful())
                            {
                                Log.d(TAG, "Inside on success");
                                final String uid = mAuth.getCurrentUser().getUid();
                                StorageReference user_profile = mStorage.child(uid + ".jpg");
                                user_profile.putFile(imguri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadtask) {
                                        if(uploadtask.isSuccessful())
                                        {

                                            String download_uri = uploadtask.getResult().getDownloadUrl().toString();
                                            progbar_signup.setVisibility(View.INVISIBLE);
                                            Log.d(TAG, "Inside on line3");

                                            //hash map
                                            Map<String, Object> map= new HashMap<>();


                                            //put the values in the hash map
                                            map.put("name", mname);
                                            map.put("image", download_uri);
                                            map.put("city",mcity);
                                            map.put("phno",mphno);
                                            map.put("blood_gr",mbldgr);


                                            //put values from hash map to the firestore collection
                                            firebaseFirestore.collection("Users").document(uid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    String token_id = FirebaseInstanceId.getInstance().getToken(); //get the token id
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("token_id",token_id);
                                                    firebaseFirestore.collection("Users").document(uid).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            sendToMain();
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(mContext, "Profile photo upload failed", Toast.LENGTH_SHORT).show();
                                            progbar_signup.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                FirebaseException e = (FirebaseException)task.getException();
                                Log.d(TAG, "Inside on failure:" + e.getMessage());
                                progbar_signup.setVisibility(View.INVISIBLE);
                                Toast.makeText(mContext, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Snackbar.make(view, "Please fill up all fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void takepermission() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {"Manifest.permissions.READ_EXTERNAL_STORSGE"}, 2);
        }
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {"Manifest.permission.WRITE_EXTERNAL_STORAGE"}, 3);
        }
    }

    private void sendToMain() {
        Intent i = new Intent(mContext, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == PROFILE_IMG)
            {
                imguri = data.getData();
                profileimg.setImageURI(imguri);
            }
        }
        else
        {
            Log.d(TAG, "Profilepic not set");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}




















