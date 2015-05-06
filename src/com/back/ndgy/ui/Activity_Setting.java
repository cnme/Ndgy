package com.back.ndgy.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;
import com.back.ndgy.utils.DataCleanManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Setting extends Activity implements OnClickListener {
	private RelativeLayout usericon;
	private ImageView usericonimage, iv_settingsback;
	private Bitmap photo, bmpimage;;
	private String iconUrl, path = Environment.getExternalStorageDirectory()
			.getPath() + "/NdgyCache/", usericonname = "usericon.png", str,
			signature, sex;
	public static final int PHOTOZOOM = 1;
	public static final int PHOTORESOULT = 2;
	private AsyncBitmapLoader asyncBitmapLoader;
	private File path1;
	private User user;
	private Button sex_choice;
	private TextView tv_nickname, tv_sign_text, tv_user_logout;
	private boolean ischoice = false;
	private RelativeLayout user_sign, settings_update, settings_about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Myapplication.getInstance().addActivity(this);
		usericon = (RelativeLayout) findViewById(R.id.user_icon);
		tv_nickname = (TextView) findViewById(R.id.user_nick_text);
		tv_sign_text = (TextView) findViewById(R.id.user_sign_text);
		tv_user_logout = (TextView) findViewById(R.id.user_logout);
		usericonimage = (ImageView) findViewById(R.id.user_icon_image);
		settings_update = (RelativeLayout) findViewById(R.id.settings_update);
		settings_about = (RelativeLayout) findViewById(R.id.settings_about);
		sex_choice = (Button) findViewById(R.id.sex_choice_switch);
		user_sign = (RelativeLayout) findViewById(R.id.user_sign);
		iv_settingsback = (ImageView) findViewById(R.id.iv_settingsback);
		Init();
	}

	private void Init() {
		usericon.setOnClickListener(this);
		settings_about.setOnClickListener(this);
		settings_update.setOnClickListener(this);
		user_sign.setOnClickListener(this);
		sex_choice.setOnClickListener(this);
		iv_settingsback.setOnClickListener(this);
		tv_user_logout.setOnClickListener(this);
		asyncBitmapLoader = new AsyncBitmapLoader();
		user = BmobUser.getCurrentUser(this, User.class);

		String avaterurl = user.getAvatar().getFileUrl(this);
		if (avaterurl != null) {
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(usericonimage,
					avaterurl, 0, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				usericonimage
						.setImageResource(R.drawable.user_icon_default_main);
			} else {
				usericonimage.setImageBitmap(bitmap);
			}
		}

		BmobQuery<User> query = new BmobQuery<User>();
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query.getObject(Activity_Setting.this, user.getObjectId(),
				new GetListener<User>() {

					@Override
					public void onSuccess(User arg0) {
						signature = arg0.getSignature();
						sex = arg0.getSex();
						if (signature != null) {
							tv_sign_text.setText(signature);
						}
						String nikename = user.getUsername();
						if (nikename != null) {
							tv_nickname.setText(nikename);
						}


						if (sex.equals("0")) {
							sex_choice
									.setBackgroundResource(R.drawable.ic_sex_female);
							ischoice = false;
						} else {

							sex_choice
									.setBackgroundResource(R.drawable.ic_sex_male);
							ischoice = true;
						}

					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_icon:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTOZOOM);
			break;

		case R.id.user_sign:
			sign();
			break;
		case R.id.user_logout:
			BmobUser.logOut(this); // 清除缓存用户对象
			DataCleanManager.cleanCustomCache(path);
			DataCleanManager.cleanApplicationData(Activity_Setting.this);
			Myapplication.getInstance().exit();
			break;
		case R.id.settings_about:
			View view2 = View.inflate(this, R.layout.dialog_about, null);
			final Dialog dialog2 = new Dialog(this, R.style.dialog);
			dialog2.setContentView(view2);
			dialog2.show();

			break;
		case R.id.sex_choice_switch:
			if (ischoice) {
				sex_choice.setBackgroundResource(R.drawable.ic_sex_female);
				updateSex("0");
				ischoice = false;
				Log.i("sex", "0");
			} else {
				sex_choice.setBackgroundResource(R.drawable.ic_sex_male);
				updateSex("1");
				ischoice = true;
				Log.i("sex", "1");
			}
			break;
		case R.id.iv_settingsback:
			finish();

			break;

		}

	};

	/**
	 * 性别更新
	 * 
	 * @param i
	 */

	private void updateSex(final String i) {

		View view = View.inflate(this, R.layout.dialog_update, null);
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		user.setSex(i);
		user.update(Activity_Setting.this, new UpdateListener() {

			@Override
			public void onSuccess() {
				dialog.cancel();
				ActivityUtils.Toast(Activity_Setting.this, "信息更新成功");
			}

			@Override
			public void onFailure(int arg0, String arg1) {

				ActivityUtils.Toast(Activity_Setting.this, "信息更新失败" + arg1);
			}
		});

	}

	/**
	 * 个性签名
	 * 
	 */
	private void sign() {
		View view = View.inflate(this, R.layout.change_sign, null);
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view);
		final EditText keyTxt = (EditText) view.findViewById(R.id.sign_content);
		Button button = (Button) view.findViewById(R.id.sign_content_commit);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				str = keyTxt.getText().toString().trim();
				if (TextUtils.isEmpty(str)) {
					ActivityUtils.Toast(Activity_Setting.this, "内容为空");
				} else {
					View view1 = View.inflate(Activity_Setting.this,
							R.layout.dialog_loading_data, null);
					final Dialog dialog1 = new Dialog(Activity_Setting.this,
							R.style.dialog);
					dialog1.setCanceledOnTouchOutside(false);
					dialog1.setContentView(view1);
					dialog1.show();
					tv_sign_text.setText(str);
					user.setSignature(str);
					user.update(Activity_Setting.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							ActivityUtils
									.Toast(Activity_Setting.this, "签名更新成功");
							dialog.cancel();
							dialog1.cancel();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							Log.i("qianming", arg1);
							ActivityUtils.Toast(Activity_Setting.this, "签名更新失败"
									+ arg1);
							dialog.cancel();
							dialog1.cancel();
						}
					});

				}

			}
		});
		dialog.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());

		}

		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");
				iconUrl = saveToSdCard(photo);

			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			break;

		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	public String saveToSdCard(Bitmap bitmap) {
		File file = new File(path + usericonname);

		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)) {
				out.flush();
				out.close();
				usericonimage.setImageBitmap(bitmap);
				updateIcon(file.getAbsolutePath());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return file.getAbsolutePath();
	}

	private void updateIcon(String avataPath) {
		if (avataPath != null) {

			View view3 = View.inflate(this, R.layout.dialog_update, null);
			final Dialog dialog3 = new Dialog(this, R.style.dialog);
			dialog3.setContentView(view3);
			dialog3.setCanceledOnTouchOutside(false);
			dialog3.show();
			final BmobFile file = new BmobFile(new File(avataPath));

			file.upload(Activity_Setting.this, new UploadFileListener() {

				@Override
				public void onSuccess() {

					User currentUser = BmobUser.getCurrentUser(
							Activity_Setting.this, User.class);
					currentUser.setAvatar(file);

					currentUser.update(Activity_Setting.this,
							new UpdateListener() {

								@Override
								public void onSuccess() {
									ActivityUtils.Toast(Activity_Setting.this,
											"头像更新成功");
									dialog3.cancel();
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									ActivityUtils.Toast(Activity_Setting.this,
											"头像更新失败。请检查网络~  " + arg1);
									dialog3.cancel();
								}
							});
				}

				@Override
				public void onProgress(Integer arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub

					ActivityUtils.Toast(Activity_Setting.this,
							"头像上传失败。请检查网络~  " + arg1);

				}
			});
		}
	}

}
