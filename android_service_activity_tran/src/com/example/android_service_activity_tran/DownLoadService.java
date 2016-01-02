package com.example.android_service_activity_tran;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class DownLoadService extends Service {

	private final String TAG="DownLoadService";
	private Random random=new Random();
	private Binder localBinder=new  LocalBinder();
	public class LocalBinder extends Binder {
		public DownLoadService getService() {
			return DownLoadService.this;
		}
		
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			System.out.println("--从Activity中获取值-->>"+data.readInt());
			System.out.println("--从Activity中获取值-->>"+data.readString());
			
			reply.writeInt(getRandom());
			reply.writeString("jack-rose");
			
			return super.onTransact(code, data, reply, flags);
		}
	}
	
	public int getRandom() {
		return random.nextInt(100);
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "--->>onCreate");
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "--->>onBind");
		//注意这里返回一个Binder
		return localBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "--->>onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onRebind(Intent intent) {
		Log.i(TAG, "--->>onRebind");
		super.onRebind(intent);
	}
	
	public void onDestroy() {
		Log.i(TAG, "--->>onDestroy");
	};
}
