package com.example.android_service_messager;

import javax.crypto.interfaces.PBEKey;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button button;
	
	//用于和服务通信的Messenger
	Messenger messenger=null;
	//标识是否已经绑定服务
	boolean bound;
	
	/**
	 * 绑定服务时的回调对象 
	 */
	ServiceConnection mConnection=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 与服务断开联系后，调用该方法
			bound=false;
			messenger=null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 与服务建立联系后，调用该方法	
			
			//该对象用来向服务器，发送消息
			messenger=new Messenger(service);
			bound=true;
		}
	};
	
	Messenger clientMessenger=new Messenger(new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(),msg.obj.toString(), Toast.LENGTH_SHORT).show();
		};
	});
	
	public void sayHello(View view) {
		if(!bound) return;
		Message message=Message.obtain(null, MessagerService.MSG_SAY_HELLO, 0, 0);
		message.replyTo=clientMessenger;
		try {
			messenger.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button=(Button)this.findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sayHello(v);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//bind to Service
		bindService(new Intent(this,MessagerService.class),mConnection,Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(bound){
			unbindService(mConnection);
			bound=false;
		}
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
