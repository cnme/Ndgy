package com.back.ndgy.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class ActivityUtils {
	/**
	 * Toast消息
	 * 
	 * @param context
	 * @param message
	 */

	public static void Toast(Context context, String message) {
		android.widget.Toast.makeText(context, message,
				android.widget.Toast.LENGTH_SHORT).show();

	}

	/**
	 * 判断网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		// 获取手机所有连接管理对象（包括wifi，net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * 获取当前的年、月、日 对应的时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		// System.out.println("格式化后的日期：" + dateNowStr);
		Date d2 = null;
		try {
			d2 = sdf.parse(dateNowStr);
			// System.out.println(d2);
			// System.out.println(d2.getTime());
			return d2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getCacheDirectory(int num) {
		if (num == 1) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			String path = sdcardDir.getPath() + "/NdgyCache/" + "Original/";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();

			}
		}
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/NdgyCache/";

		return path;
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
