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
	 * Toast��Ϣ
	 * 
	 * @param context
	 * @param message
	 */

	public static void Toast(Context context, String message) {
		android.widget.Toast.makeText(context, message,
				android.widget.Toast.LENGTH_SHORT).show();

	}

	/**
	 * �ж����������Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		// ��ȡ�ֻ��������ӹ�����󣨰���wifi��net�����ӵĹ���
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// ��ȡ�������ӹ���Ķ���
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// �жϵ�ǰ�����Ƿ��Ѿ�����
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * ��ȡ��ǰ���ꡢ�¡��� ��Ӧ��ʱ��
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		// System.out.println("��ʽ��������ڣ�" + dateNowStr);
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
				// �������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
				path1.mkdirs();

			}
		}
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/NdgyCache/";

		return path;
	}
	
	/** 
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
