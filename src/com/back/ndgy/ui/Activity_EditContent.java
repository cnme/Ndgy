package com.back.ndgy.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.back.ndgy.R;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.data.LoveData;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.StudyData;
import com.back.ndgy.data.TravelData;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_EditContent extends Activity implements OnClickListener {
	LinearLayout open_pic, take_pic;
	EditText edit_content;
	TextView tv_sned;
	ImageView iv_open_pic, iv_take_pic;
	long currenttime;
	public static final int PHOTOZOOM = 1;
	public static final int PHOTORESOULT = 2;
	public static final int REQUEST_CODE_CAMERA = 3;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		Myapplication.getInstance().addActivity(this);
		Init();

	}

	private void Init() {
		tv_sned = (TextView) findViewById(R.id.tv_send);
		edit_content = (EditText) findViewById(R.id.edit_content);
		open_pic = (LinearLayout) findViewById(R.id.open_layout);
		take_pic = (LinearLayout) findViewById(R.id.take_layout);
		iv_open_pic = (ImageView) findViewById(R.id.open_pic);
		iv_take_pic = (ImageView) findViewById(R.id.take_pic);
		tv_sned.setOnClickListener(this);
		open_pic.setOnClickListener(this);
		take_pic.setOnClickListener(this);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_send:
			send();
			break;

		case R.id.open_layout:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTOZOOM);
			break;
		case R.id.take_layout:
			currenttime = System.currentTimeMillis();
			File file = new File(ActivityUtils.getCacheDirectory(2)
					+ "Original/", currenttime + ".jpg");
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Uri uri = Uri.fromFile(file);
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(camera, REQUEST_CODE_CAMERA);
			break;
		}

	}

	private void send() {
		String content = edit_content.getText().toString().trim();
		if (TextUtils.isEmpty(targeturl)) {
			ActivityUtils.Toast(this, "请选择一张图片");
		} else {
			publish(content);
		}
	}

	private void publish(final String commitContent) {
		
		final BmobFile figureFile = new BmobFile(new File(targeturl));
		figureFile.upload(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				Toast("图片上传成功");
				publishWithoutFigure(commitContent, figureFile);
			
			}

			@Override
			public void onProgress(Integer arg0) { // TODO

			}

			@Override
			public void onFailure(int arg0, String arg1) {
		
			}
		});

	}

	private void publishWithoutFigure(final String commitContent,
			BmobFile figureFile) {
		Toast("开始发表" + figureFile);

		View view = View.inflate(Activity_EditContent.this, R.layout.dialog_loading_data, null);
		final Dialog dialog = new Dialog(Activity_EditContent.this, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		User user = BmobUser.getCurrentUser(Activity_EditContent.this,
				User.class);

		switch (sp.getInt("index", 0)) {
		case 0:
			FreedomSpeechDate freedomSpeechDate = new FreedomSpeechDate();
			freedomSpeechDate.setAuthor(user);
			freedomSpeechDate.setContent(commitContent);
			freedomSpeechDate.setPicurl(figureFile);
			freedomSpeechDate.setLove(0);
			freedomSpeechDate.setComment(0);
			freedomSpeechDate.setPass(true);
			Toast("发表last");
			freedomSpeechDate.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub

					ActivityUtils.Toast(Activity_EditContent.this, "发表成功");

					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtils.Toast(Activity_EditContent.this, "发表失败~"
							+ arg1);
				}
			});
			break;
		case 1:
			LoveData loveData = new LoveData();
			loveData.setAuthor(user);
			loveData.setContent(commitContent);
			loveData.setPicurl(figureFile);
			loveData.setLove(0);
			loveData.setComment(0);
			loveData.setPass(true);
			Toast("发表last");
			loveData.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub

					ActivityUtils.Toast(Activity_EditContent.this, "发表成功");

					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtils.Toast(Activity_EditContent.this, "发表失败~"
							+ arg1);
				}
			});
			break;
		case 2:
			StudyData studyData = new StudyData();
			studyData.setAuthor(user);
			studyData.setContent(commitContent);
			studyData.setPicurl(figureFile);
			studyData.setLove(0);
			studyData.setComment(0);
			studyData.setPass(true);
			Toast("发表last");
			studyData.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub

					ActivityUtils.Toast(Activity_EditContent.this, "发表成功");
					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtils.Toast(Activity_EditContent.this, "发表失败~"
							+ arg1);
				}
			});
			break;
		case 3:
			TravelData travelData = new TravelData();
			travelData.setAuthor(user);
			travelData.setContent(commitContent);
			travelData.setPicurl(figureFile);
			travelData.setLove(0);
			travelData.setComment(0);
			travelData.setPass(true);
			Toast("发表last");
			travelData.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub

					ActivityUtils.Toast(Activity_EditContent.this, "发表成功");
					sp.edit().putInt("update", 1).commit();
					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtils.Toast(Activity_EditContent.this, "发表失败~"
							+ arg1);
				}
			});
			break;

		}

		dialog.cancel();
	}

	String targeturl = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTOZOOM:
			Log.i("开始处理图片", "dddddddddddd");

			Toast("开始处理");
			String fileName = null;
			if (data != null) {
				Uri originalUri = data.getData();
				ContentResolver cr = getContentResolver();
				Cursor cursor = cr.query(originalUri, null, null, null, null);
				if (cursor.moveToFirst()) {
					do {
						fileName = cursor.getString(cursor
								.getColumnIndex("_data"));

					} while (cursor.moveToNext());
				}
				Bitmap bitmap = compressImageFromFile(fileName);
				if (bitmap != null) {
					targeturl = saveToSdCard(bitmap);
					iv_open_pic.setImageBitmap(bitmap);
					take_pic.setVisibility(View.GONE);
				}

			}
			break;

		case REQUEST_CODE_CAMERA:
			if (data == null) {
				Toast("开始处理");

				String files = ActivityUtils.getCacheDirectory(2) + "Original/"
						+ currenttime + ".jpg";
				Bitmap bitmap = compressImageFromFile(files);
				targeturl = saveToSdCard(bitmap);
				iv_take_pic.setImageBitmap(bitmap);
				open_pic.setVisibility(View.GONE);
			}

			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 400f;//
		float ww = 400f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		Toast("图片处理成功");
		return bitmap;
	}

	public String saveToSdCard(Bitmap bitmap) {
		String files = ActivityUtils.getCacheDirectory(2) + "send_pic.jpg";
		File file = new File(files);

		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 35, out)) {
				out.flush();
				out.close();
				Toast("保存SD卡成功");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast("保存SD卡失败");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Toast("保存SD卡失败");
		}

		return file.getAbsolutePath();
	}

	private void Toast(String toast) {
		Toast.makeText(this, toast, 3000).show();

	}
}
