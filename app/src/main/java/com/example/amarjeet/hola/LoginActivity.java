package com.example.amarjeet.hola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amarjeet on 14-04-2018.
 */

public class LoginActivity extends AppCompatActivity {
    EditText memail, mpassword;
    Button loginbtn;
    TextView signup;
    String email, password;
    ProgressBar progressBar;

    Context mContext;

    //Firebase stuffs
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = (EditText)findViewById(R.id.email);
        mpassword = (EditText)findViewById(R.id.password);
        signup = (TextView)findViewById(R.id.signup);
        loginbtn = (Button)findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_login);
        firestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = memail.getText().toString();
                password = mpassword.getText().toString();

                if(email.equals("") && password.equals(""))
                {
                    Snackbar.make(view, "Please fill up all fields", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);

                                        String token_id = FirebaseInstanceId.getInstance().getToken();
                                        String uid = mAuth.getCurrentUser().getUid();

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("token_id",token_id);

                                        firestore.collection("Users").document(uid).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

}
