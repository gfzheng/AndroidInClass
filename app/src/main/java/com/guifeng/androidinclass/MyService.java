package com.guifeng.androidinclass;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.IOException;

public class MyService extends Service {
    public IBinder mBinder = new MyBinder();

    @Override
    public void onDestroy() {
        onPlayerExit();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        initPlayer();
        super.onCreate();
    }

    public MyService() {
    }

    public class MyBinder extends Binder
    {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch(code)
            {
                case 101:
                    onPlayerPlay();
                    break;
                case 102:
                    onPlayerStop();
                    break;
                case 103:
                    onPlayerExit();
                    break;
                case 104:
                    if(mPlayer!=null)
                        reply.writeInt(mPlayer.getCurrentPosition()/1000);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private MediaPlayer mPlayer = null;

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource("/sdcard/1.mp3");
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onPlayerPlay() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }

    private void onPlayerStop() {
        mPlayer.stop();
        try {
            mPlayer.prepare();
        } catch (IOException e) {
        }
    }

    private void onPlayerExit() {
        mPlayer.release();
        mPlayer = null;
    }
}
