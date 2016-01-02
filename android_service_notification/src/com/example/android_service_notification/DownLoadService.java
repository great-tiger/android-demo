package com.example.android_service_notification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class DownLoadService extends Service {
	final String IMAGE_PATH="https://www.baidu.com/img/gif_866f6aded1145a1188095fc88f4593d6.gif";
	NotificationManager manager;
	NotificationCompat.Builder builder;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				stopSelf();
				Toast.makeText(getApplicationContext(), "下载完成", 1).show();
				builder.setContentText("file download complete");
			}
			
			builder.setProgress(100, msg.arg1,false);
			manager.notify(1000, builder.build());
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		builder=new NotificationCompat.Builder(getApplicationContext());
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("文件下载进度通知");
		builder.setContentTitle("file download");
		builder.setContentText("file download ........");
		manager.notify(1000, builder.build());
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
			InputStream inputStream;
		    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
			try {
				HttpResponse response=	httpClient.execute(httpPost);
				if(response.getStatusLine().getStatusCode()==200){
					long total_length=response.getEntity().getContentLength();
					long sum_length=0;
					int len=0;
					inputStream=response.getEntity().getContent();
					byte[] data=new byte[1024];
					while((len=inputStream.read(data))!=-1){
						sum_length+=len;
						outputStream.write(data, 0, len);
						Message message=Message.obtain();
						message.arg1=(int)(sum_length/(float)total_length*100);
						handler.sendMessage(message);
					}
					if(DiskTools.SaveToDisk("ccc.gif",outputStream.toByteArray())){
						//文件下载完成
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
