package com.example.laur.dissertationvotingapp.camera;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.AsyncTask;
public class Timer extends AsyncTask<Void, Void, Void> {
    Context mContext;
    private Handler threadHandler;
    public Timer(Context context,Handler threadHandler) {
        super();
        this.threadHandler=threadHandler;
        mContext = context;
    }
    @Override
    protected Void doInBackground(Void...params) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message.obtain(threadHandler, CameraService.DONE, "").sendToTarget();
        return null;
    }
}
