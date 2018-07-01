package com.example.amarjeet.hola;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Amarjeet on 15-04-2018.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
    private static final String TAG = "RecycleViewAdapter";

    private FirebaseFirestore fstore;
    private ArrayList<Users> mUsers = new ArrayList<>();
    private Context mContext;
    private String uid, currentuid;
    private ProgressBar progressBar;
    private String message;
    private TextView textView, not_available;

    public RecycleViewAdapter(ArrayList<Users> mUsers, Context mContext,ProgressBar progressBar, TextView textView,TextView not_available, String currentuid) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.progressBar = progressBar;
        this.textView = textView;
        this.not_available = not_available;
        this.currentuid = currentuid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(mUsers.size()>0)
            {
                uid = mUsers.get(position).user_id;
                not_available.setVisibility(View.INVISIBLE);
                Log.d(TAG,position + "-" + mUsers.get(position).getName());
                holder.name.setText(mUsers.get(position).getName());
                holder.latitude = mUsers.get(position).getLatitude();
                holder.longitude = mUsers.get(position).getLongitude();
                holder.bloodgr = mUsers.get(position).getBlood_gr();
                holder.phone_no = mUsers.get(position).getPhno();

                Glide.with(mContext).load(mUsers.get(position).getImage()).asBitmap().into(holder.profImg);

                message = "Hey, I need your help!!";

                holder.notify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.notify.setEnabled(false);
                        holder.notify.setText("Notified");
                        fstore = FirebaseFirestore.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("message",message);
                        map.put("user_id",currentuid);
                        fstore.collection("Users/" + uid + "/Notifications").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Sent");
                            }
                        });
                    }
                });
            }
            else
            {
                not_available.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Button notify;
        CircleImageView profImg;
        RelativeLayout rellayout;
        TextView name;
        String latitude, longitude, bloodgr, phone_no;
        public ViewHolder(View itemView) {
            super(itemView);
            profImg = (CircleImageView)itemView.findViewById(R.id.profpic);
            rellayout = (RelativeLayout)itemView.findViewById(R.id.rellayout);
            name = (TextView)itemView.findViewById(R.id.name);
            notify = (Button)itemView.findViewById(R.id.switch_recycler);
        }
    }
}
