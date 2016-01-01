package com.example.android_service_download;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class DownLoadService extends Service {
	final String IMAGE_PATH="https://www.baidu.com/img/gif_866f6aded1145a1188095fc88f4593d6.gif";
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				stopSelf();
				Toast.makeText(getApplicationContext(), "下载完成", 1).show();
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new MyThread()).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public class MyThread implements Runnable{
		
		@Override
		public void run() {
			HttpClient httpClient=new DefaultHttpClient();
			HttpPost httpPost=new HttpPost(IMAGE_PATH);
			try {
				HttpResponse response=	httpClient.execute(httpPost);
				if(response.getStatusLine().getStatusCode()==200){
					byte[] result=EntityUtils.toByteArray(response.getEntity());
				    boolean flag=DiskTools.SaveToDisk("aa.gif", result);
				    if(flag){
				    	handler.sendEmptyMessage(1);
				    }
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
