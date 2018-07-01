package com.example.amarjeet.hola;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.TimerTask;

/**
 * Created by Amarjeet on 19-04-2018.
 */

public class TimerTaskModified extends TimerTask {
    RecycleViewAdapter2 recycleViewAdapter2;
    Context mContext;
    ProgressBar progressBar;
    TextView msg, loading;


    public TimerTaskModified(RecycleViewAdapter2 recycleViewAdapter2, Context mContext, ProgressBar progressBar, TextView msg, TextView loading) {
        this.recycleViewAdapter2 = recycleViewAdapter2;
        this.mContext = mContext;
        this.progressBar = progressBar;
        this.msg = msg;
        this.loading = loading;
    }

    @Override
    public void run() {

    }
}
