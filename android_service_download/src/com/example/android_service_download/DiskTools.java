package com.example.android_service_download;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

public class DiskTools {
	public static boolean SaveToDisk(String fileName,byte[] bytes) {
		boolean flag=false;
	    File file=Environment.getExternalStorageDirectory();
	    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	    	FileOutputStream outputStream=null;
	    	try {
				outputStream=new FileOutputStream(new File(file,fileName));
				outputStream.write(bytes, 0, bytes.length);
				flag=true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(outputStream!=null){
						outputStream.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
	    }
		return flag;
	}
}
