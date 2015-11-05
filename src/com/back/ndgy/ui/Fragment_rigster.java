package com.back.ndgy.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.back.ndgy.R;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 
 * @Back
 * @注册
 * @2015/5
 */
public class Fragment_rigster extends Fragment implements OnClickListener {
	private View rootview;
	private ImageView iv_avater;
	private EditText et_name_rigster, et_password_rigster, et_email_rigster;
	private Button btn_rigster;
	private Bitmap photo;
	private String iconUrl, name, password, email;
	private File file;
	public static final int PHOTOZOOM = 1;
	public static final int PHOTORESOULT = 2;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater
				.inflate(R.layout.fragment_rigster, container, false);
		iv_avater = (ImageView) rootview.findViewById(R.id.user_avater);
		et_name_rigster = (EditText) rootview
				.findViewById(R.id.user_name_rigster);
		et_password_rigster = (EditText) rootview
				.findViewById(R.id.user_password_rigster);
		et_email_rigster = (EditText) rootview
				.findViewById(R.id.user_email_rigster);
		btn_rigster = (Button) rootview.findViewById(R.id.btn_register);
		btn_rigster.setOnClickListener(this);
		iv_avater.setOnClickListener(this);
		sp = getActivity().getSharedPreferences("infor", 0);
		return rootview;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:

			name = et_name_rigster.getText().toString().trim();
			password = et_password_rigster.getText().toString().trim();
			email = et_email_rigster.getText().toString().trim();
			if (TextUtils.isEmpty(name)) {
				ActivityUtils.Toast(getActivity(), "用户名为空");
			} else if (TextUtils.isEmpty(password)) {
				ActivityUtils.Toast(getActivity(), "密码为空");
			} else if (TextUtils.isEmpty(email)) {
				ActivityUtils.Toast(getActivity(), "激活需要使用邮箱");
			} else if (TextUtils.isEmpty(iconUrl)) {
				ActivityUtils.Toast(getActivity(), "请设置头像");
			} else {
				sp.edit().putString("name", name).commit();
				sp.edit().putString("password", password).commit();
				rigster();
			}
			break;

		case R.id.user_avater:
			avatet();
			break;
		}

	}

	/**
	 * 打开图库
	 */
	private void avatet() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTOZOOM);

	}

	/**
	 * 注册
	 */
	private void rigster() {
		View view = View.inflate(getActivity(), R.layout.dialog_loading, null);
		final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		BmobUser newuser = new BmobUser();
		newuser.setUsername(name);
		newuser.setPassword(password);
		newuser.setEmail(email);
		newuser.signUp(getActivity(), new SaveListener() {

			@Override
			public void onSuccess() {

				Log.i("注册成功", "dddddd");
				updateIcon(iconUrl);

			}

			@Override
			public void onFailure(int arg0, String arg1) {

				Log.i("注册是吧", arg1);

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (data == null)
			return;

		// 读取相册图片
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

	/**
	 * 裁切图片
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	public String saveToSdCard(Bitmap bitmap) {
		file = new File(ActivityUtils.getCacheDirectory(1) + "usericon.png");

		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
				iv_avater.setImageBitmap(bitmap);

			}
		} catch (FileNotFoundException e) {

			System.out.print(e);
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return file.getAbsolutePath();
	}

	private void updateIcon(String avataPath) {
		if (avataPath != null) {
			final BmobFile file = new BmobFile(new File(avataPath));

			Log.i("上传图片", "dddddd");
			file.upload(getActivity(), new UploadFileListener() {

				@Override
				public void onSuccess() {

					User currentUser = BmobUser.getCurrentUser(getActivity(),
							User.class);
					currentUser.setAvatar(file);
					currentUser.setSignature("这个家伙很懒，什么都没有留下~");
					currentUser.setSex("0");
					currentUser.update(getActivity(), new UpdateListener() {

						@Override
						public void onSuccess() {
							ActivityUtils.Toast(getActivity(), "注册成功");
							Activity_Login activity_Login = (Activity_Login) getActivity();
							activity_Login.rigster(false);
							Intent intent = new Intent();
							intent.setClass(getActivity(), Activity_Main.class);
							startActivity(intent);
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub

							ActivityUtils.Toast(getActivity(), "头像设置失败。请检查网络~");
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

					ActivityUtils.Toast(getActivity(), "上传头像失败。请检查网络~");

				}
			});
		}
	}

}
