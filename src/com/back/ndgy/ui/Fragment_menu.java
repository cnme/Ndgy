package com.back.ndgy.ui;

import cn.bmob.v3.BmobUser;

import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_menu extends Fragment implements OnClickListener {

	private View rootView;
	private ImageView user_icon;
	private TextView tv_freendom, tv_settings, tv_love, tv_study, tv_travel;
	private Bitmap bmpimage;
	private SharedPreferences sp;
	private String path = ActivityUtils.getCacheDirectory(1) + "usericon.png";
	private AsyncBitmapLoader asyncBitmapLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_menu, container, false);
		tv_settings = (TextView) rootView.findViewById(R.id.tv_navi_settings);
		tv_freendom = (TextView) rootView.findViewById(R.id.tv_navi_freedom);
		tv_love = (TextView) rootView.findViewById(R.id.tv_navi_love);
		tv_study = (TextView) rootView.findViewById(R.id.tv_navi_study);
		tv_travel = (TextView) rootView.findViewById(R.id.tv_navi_travl);
		user_icon = (ImageView) rootView.findViewById(R.id.iv_user_icon);
		tv_freendom.setOnClickListener(this);
		tv_settings.setOnClickListener(this);
		tv_love.setOnClickListener(this);
		tv_study.setOnClickListener(this);
		tv_travel.setOnClickListener(this);
		user_icon.setOnClickListener(this);
		download_user_icon();
		return rootView;

	}

	private void download_user_icon() {
		asyncBitmapLoader = new AsyncBitmapLoader();
		User user = BmobUser.getCurrentUser(getActivity(), User.class);
		String avaterurl = user.getAvatar().getFileUrl(getActivity());
		if (avaterurl != null) {
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(user_icon, avaterurl,
					0, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				user_icon.setImageResource(R.drawable.user_icon_default_main);
			} else {
				user_icon.setImageBitmap(bitmap);
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_navi_freedom:
			switchContent(0);
			closemenu();
			break;
		case R.id.tv_navi_love:
			switchContent(1);
			closemenu();
			break;

		case R.id.tv_navi_study:
			switchContent(2);
			closemenu();
			break;
		case R.id.tv_navi_travl:
			switchContent(3);
			closemenu();
			break;
		case R.id.iv_user_icon:
			Intent intent = new Intent(getActivity(), Activity_person.class);
			startActivity(intent);
			closemenu();
			break;
		case R.id.tv_navi_settings:
			Intent intent2 = new Intent(getActivity(), Activity_Setting.class);
			startActivity(intent2);
			closemenu();
			break;

		}

	}

	private void switchContent(int index) {
		Activity_Main activity_Main = (Activity_Main) getActivity();
		activity_Main.switchContent(index);
	}

	private void closemenu() {
		// TODO Auto-generated method stub
		Activity_Main activity_Main = (Activity_Main) getActivity();
		activity_Main.openmenu(false);
	}

}
