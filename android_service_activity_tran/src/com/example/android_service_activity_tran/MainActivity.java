package com.example.android_service_activity_tran;

import com.example.android_service_activity_tran.DownLoadService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView textview;
	Button button1,button2,button3,button4;
	boolean flag;
	DownLoadService downLoadService;
	LocalBinder localBinder;
	//绑定service时，需要这个参数
	public ServiceConnection connection=new ServiceConnection() {
		//服务断开
		@Override
		public void onServiceDisconnected(ComponentName name) {
			flag=false;
		}
		//服务连接
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		    localBinder=(LocalBinder)service;
			downLoadService=localBinder.getService();
			flag=true;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview=(TextView)this.findViewById(R.id.textView1);
		button1=(Button)this.findViewById(R.id.button1);
		button2=(Button)this.findViewById(R.id.button2);
		button3=(Button)this.findViewById(R.id.button3);
		button4=(Button)this.findViewById(R.id.button4);
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(flag){
					int value=downLoadService.getRandom();
					textview.setText(""+value);
				}
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unbindService(connection);
			}
		});
		
		button4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Parcel parcel=Parcel.obtain();
				parcel.writeInt(22);
				parcel.writeString("jack");
				
				Parcel reply=Parcel.obtain();
				
				try {
					localBinder.transact(IBinder.LAST_CALL_TRANSACTION,parcel, reply,0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				System.out.println("--从Service中获取值-->>"+reply.readInt());
				System.out.println("--从Service中获取值-->>"+reply.readString());
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent=new Intent(this,DownLoadService.class);
		//绑定service
		bindService(intent, connection,Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
