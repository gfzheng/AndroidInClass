package com.guifeng.androidinclass;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private IBinder mBinder = null;
    private ServiceConnection mConnection;

    TextView mTime;

    @Override
    protected void onDestroy() {
        quit = true;
        unbindService(mConnection);
        super.onDestroy();
    }
//    MediaPlayer mPlayer = null;
//
//    private void initPlayer() {
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource("/sdcard/1.mp3");
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {// 在此处判断消息类型并更新UI
//                    case 123:
//                        Integer s = mPlayer.getCurrentPosition();
//                        s = s / 1000;
//                        mTime.setText(s.toString() + "秒");
//                        break;
//                }
//            }
//        };
//
//
//        Thread mThread = new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    // 定义接收消息的Handler对象，并将消息加入队列
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    mHandler.obtainMessage(123).sendToTarget();
//                }
//            }
//        };
//       mThread.start();
//    }
//
//    private void onPlayerPlay() {
//        if (mPlayer.isPlaying()) {
//            mPlayer.pause();
//        } else {
//            mPlayer.start();
//        }
//    }
//
//    private void onPlayerStop() {
//        mPlayer.stop();
//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//        }
//    }
//
//    private void onPlayerExit() {
//        mPlayer.release();
//    }

    View.OnClickListener mListenPlay = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            //onPlayerPlay();
            try {
                int code = 101;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                mBinder.transact(code, data, reply, 0);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    View.OnClickListener mListenStop = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            //onPlayerStop();
            try {
                int code = 102;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                mBinder.transact(code, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener mListenExit = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            MainActivity.this.finish();
        }
    };
    private boolean quit = false;
    //MyTask mt = new MyTask();

    @Override
    protected void onResume() {
        //mt.execute();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPlay = (Button) findViewById(R.id.buttonPlay);
        Button btnStop = (Button) findViewById(R.id.buttonStop);
        Button btnExit = (Button) findViewById(R.id.buttonExit);

        mTime = (TextView) findViewById(R.id.textTime);

        btnPlay.setOnClickListener(mListenPlay);
        btnStop.setOnClickListener(mListenStop);
        btnExit.setOnClickListener(mListenExit);
        //initPlayer();
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
/*
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {// 在此处判断消息类型并更新UI
                    case 123:
                        //Integer s = mPlayer.getCurrentPosition();
                        //s = s / 1000;
                        int code = 104;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try {
                            if(mBinder!=null)
                            mBinder.transact(code, data, reply, 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mTime.setText(reply.readInt() + "秒");
                        break;
                }
            }
        };


        Thread mThread = new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    // 定义接收消息的Handler对象，并将消息加入队列
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(123).sendToTarget();
                }
            }
        };
        mThread.start();
*/

        MyTask m = new MyTask();
        m.execute();

    }

    public class MyTask extends AsyncTask<Void,Integer,Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MyTask","post exec");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MyTask","pre exec");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (!quit) {
                int code = 104;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    if(mBinder != null)
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {

                    e.printStackTrace();
                }
                publishProgress(reply.readInt());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("MyTask","do in bg");

            }
            return null;
      }

        @Override
        protected void onProgressUpdate(Integer... values) {

            mTime.setText(values[0]+"秒");
            super.onProgressUpdate(values);
        }
    }

}
