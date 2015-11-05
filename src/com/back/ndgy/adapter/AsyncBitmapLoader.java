package com.back.ndgy.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.back.ndgy.ui.Activity_img;
import com.back.ndgy.utils.ActivityUtils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsyncBitmapLoader {
	/**
	 * 图片下载
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadBitmap(final ImageView imageView, final String imageURL,
			int isScrolling, final ImageCallBack imageCallBack) {
		// 从内存中取
		if (imageCache.containsKey(imageURL)) {
			SoftReference<Bitmap> reference = imageCache.get(imageURL);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		} else {
			/**
			 * 从本地取
			 */
			String bitmapName = imageURL
					.substring(imageURL.lastIndexOf("/") + 1);
			File cacheDir = new File(ActivityUtils.getCacheDirectory(1));
			File[] cacheFiles = cacheDir.listFiles();
			int i = 0;
			if (null != cacheFiles) {
				for (; i < cacheFiles.length; i++) {
					if (bitmapName.equals(cacheFiles[i].getName())) {
						break;
					}
				}

				if (i < cacheFiles.length) {
					return BitmapFactory.decodeFile(ActivityUtils
							.getCacheDirectory(1) + bitmapName);
				}
			}
		}
		if (isScrolling == 0) {
			Log.i("����", isScrolling + "");
			final Handler handler = new Handler() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see android.os.Handler#handleMessage(android.os.Message)
				 */
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
				}
			};
			// 开启线程从网络下载
			new Thread() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					Log.i("��������ͼƬ", "��������ͼƬ������������");
					// TODO Auto-generated method stub
					InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);

					Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs);
					imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
					Message msg = handler.obtainMessage(0, bitmap);
					handler.sendMessage(msg);

					File dir = new File(ActivityUtils.getCacheDirectory(1));
					if (!dir.exists()) {
						dir.mkdirs();
					}

					File bitmapFile = new File(
							ActivityUtils.getCacheDirectory(1)
									+ imageURL.substring(imageURL
											.lastIndexOf("/") + 1));
					if (!bitmapFile.exists()) {
						try {
							bitmapFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(bitmapFile);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
						fos.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();

		}

		return null;
	}

	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}
