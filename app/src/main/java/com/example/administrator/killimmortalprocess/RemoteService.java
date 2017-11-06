package com.example.administrator.killimmortalprocess;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.administrator.inter.ProcessService;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class RemoteService extends Service {

    private MyBinder binder;

    private MyConn conn;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        if (conn == null) {
            conn = new MyConn();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        bindService(new Intent(this, LocalService.class), conn, Context.BIND_IMPORTANT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class MyBinder extends ProcessService.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "RemoteService";
        }
    }


    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            //该方法会在绑定的服务断开连接时调用,绑定的服务被强杀的时候也会被调用
            //在强杀后服务,重新启动服务
            startService(new Intent(RemoteService.this, LocalService.class));
            //重新绑定服务
            bindService(new Intent(RemoteService.this, LocalService.class), conn, Context.BIND_IMPORTANT);
        }
    }
}
