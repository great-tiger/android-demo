package com.example.android_service_intentservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends IntentService {

	final String IMAGE_PATH="https://www.baidu.com/img/gif_866f6aded1145a1188095fc88f4593d6.gif";
	//这个构造函数必须要创建，并且super方法也必须调用
    public DownloadService() {
    	super("DownloadService");
    }
	
    /**
     * 主要在这个方法中处理intent
     * 
     */
	@Override
	protected void onHandleIntent(Intent intent) {
		HttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(IMAGE_PATH);
		try {
			HttpResponse response=httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				byte[] result=EntityUtils.toByteArray(response.getEntity());
			    boolean flag=DiskTools.SaveToDisk("bb.gif", result);
			    if(flag){
			    	//这里使用toast会报错 ？？？？？？
			    	//Toast.makeText(getApplicationContext(), "下载完成", 1).show();
			    	Log.i("tag", "图片下载完成！");
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
