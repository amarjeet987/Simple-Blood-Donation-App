package com.example.amarjeet.hola;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Amarjeet on 19-04-2018.
 */

public class RecycleViewAdapter2 extends RecyclerView.Adapter<RecycleViewAdapter2.ViewHolder>{

    private FirebaseFirestore firestore;
    private ArrayList<Accepted> ids = new ArrayList<>();
    Context mContext;
    ProgressBar progressBar;
    TextView message, loading;
    Users mUsers;
    String uid;
    RecyclerView recyclerView;

    public RecycleViewAdapter2(RecyclerView recyclerView,FirebaseFirestore firestore, ArrayList<Accepted> ids, Context mContext, ProgressBar progressBar, TextView message, TextView loading) {
        this.firestore = firestore;
        this.ids = ids;
        this.mContext = mContext;
        this.progressBar = progressBar;
        this.message = message;
        this.loading = loading;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_notif_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(ids.size()>0)
        {
            firestore.collection("Users").addSnapshotListener((Activity) mContext, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        if(doc.getType() == DocumentChange.Type.ADDED)
                        {
                            uid = doc.getDocument().getId();
                            if(uid.equals(ids.get(position).getAccepted()))
                            {
                                mUsers = doc.getDocument().toObject(Users.class);
                            }
                        }
                    }
                    if(mUsers!=null)
                    {

                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        holder.name.setText(mUsers.getName());
                        Glide.with(mContext).load(mUsers.getImage()).asBitmap().into(holder.imageView);
                        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(mContext,InfoActivity.class);
                                i.putExtra("name", mUsers.getName());
                                i.putExtra("bloodgr", mUsers.getBlood_gr());
                                i.putExtra("profpic",mUsers.getImage());
                                i.putExtra("phoneno",mUsers.getPhno());
                                i.putExtra("latitude",mUsers.getLatitude());
                                i.putExtra("longitude",mUsers.getLongitude());
                                mContext.startActivity(i);

                            }
                        });
                    }
                    else
                    {
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        CircleImageView imageView;
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.rellayout);
            imageView =(CircleImageView)itemView.findViewById(R.id.profpic);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }
}
