package com.example.android_service_lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HelloService extends Service {

	private final String TAG="HelloService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "----onCreate--->");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String name=intent.getStringExtra("name");
		Log.i(TAG, "----onStartCommand--->"+name);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "----onDestory--->");
		super.onDestroy();
	}

}
