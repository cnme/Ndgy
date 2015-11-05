package com.back.ndgy.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import com.back.ndgy.R;
import com.back.ndgy.data.Comment;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Main extends Activity implements OnClickListener {
	private FragmentManager fm;
	private FragmentTransaction ft;
	private Fragment_menu menu;
	private Fragment_freedom freedomfg;;
	private Boolean menu_isopen = true;
	private ImageView iv_menu, btn_edit;
	private TextView tv_new_message;
	private RefreshListView mc_lv;
	private SharedPreferences sp;
	private Animation mRotateAnimation;
	private Fragment from, to;
	private Fragment_love love;
	private Fragment_Study study;
	private Fragment_travel travel;
	public boolean Back = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Myapplication.getInstance().addActivity(this);
		sp = getSharedPreferences("setting", 0);
		IntView();
		WindowManager manager = this.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
		sp.edit().putInt("width", width).commit();

		querynotice();
	}

	private void IntView() {

		iv_menu = (ImageView) findViewById(R.id.btn_nav);
		iv_menu.setOnClickListener(this);
		btn_edit = (ImageView) findViewById(R.id.btn_edit);
		btn_edit.setOnClickListener(this);
		tv_new_message = (TextView) findViewById(R.id.tv_new_message);
		tv_new_message.setOnClickListener(this);
		menu = new Fragment_menu();
		love = new Fragment_love();
		study = new Fragment_Study();
		travel = new Fragment_travel();
		freedomfg = new Fragment_freedom();
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.content_center, freedomfg);
		ft.commit();
		sp.edit().putInt("index", 0).commit();

	}

	@Override
	protected void onStart() {
		if (sp.getInt("message", 0) == 0) {
			tv_new_message.setText("  0");
		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		if (sp.getInt("message", 0) == 0) {
			tv_new_message.setText("  0");
		}
		super.onResume();
	}

	public void querynotice() {
		Log.i("notice", "开始notice");
		User user = BmobUser.getCurrentUser(this, User.class);
		BmobQuery<Comment> eq1 = new BmobQuery<Comment>();
		eq1.addWhereEqualTo("new_message", false);
		BmobQuery<Comment> eq2 = new BmobQuery<Comment>();
		eq2.addWhereEqualTo("datauser", user);
		BmobQuery<Comment> eq3 = new BmobQuery<Comment>();
		eq3.addWhereEqualTo("byuser", user);

		List<BmobQuery<Comment>> orQuerys = new ArrayList<BmobQuery<Comment>>();
		orQuerys.add(eq2);
		orQuerys.add(eq3);
		BmobQuery<Comment> mainQuery = new BmobQuery<Comment>();
		BmobQuery<Comment> or = mainQuery.or(orQuerys);

		List<BmobQuery<Comment>> andQuerys = new ArrayList<BmobQuery<Comment>>();
		andQuerys.add(eq1);
		andQuerys.add(or);
		BmobQuery<Comment> noticeQuery = new BmobQuery<Comment>();
		noticeQuery.and(andQuerys);
		noticeQuery.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				tv_new_message.setText("  " + arg0.size());
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_nav:

			if (menu_isopen) {
				openmenu(true);
			} else {
				openmenu(false);
			}

			break;

		case R.id.btn_edit:
			Intent intent = new Intent(this, Activity_EditContent.class);
			startActivity(intent);
			break;
		case R.id.tv_new_message:
			Intent noticeIntent = new Intent(this, Activity_goods.class);
			startActivity(noticeIntent);
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!menu_isopen) {
				openmenu(false);
			} else {
				Clone();
			}
			break;

		case KeyEvent.KEYCODE_MENU:
			if (menu_isopen) {
				openmenu(true);
			} else {
				openmenu(false);
			}

			break;

		}

		return true;
	}

	private static long firstTime = 0;

	private void Clone() {
		
		if (firstTime + 2000 > System.currentTimeMillis()) {
			Myapplication.getInstance().exit();
			super.onBackPressed();
		} else {
			ActivityUtils.Toast(this, "再按一次退出程序");
			firstTime = System.currentTimeMillis();
		}
	}

	public void openmenu(boolean flag) {
		if (flag) {
			mRotateAnimation = new RotateAnimation(0.0f, 360.0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mRotateAnimation.setDuration(1000);
			iv_menu.startAnimation(mRotateAnimation);
			fm = getFragmentManager();
			ft = fm.beginTransaction();
			ft.setCustomAnimations(R.anim.push_right_left_in,
					R.anim.push_right_left_out);
			ft.add(R.id.content_center, menu);
			ft.addToBackStack(null);
			ft.commit();

			menu_isopen = false;
		} else {
			mRotateAnimation = new RotateAnimation(0.0f, -360.0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mRotateAnimation.setDuration(1000);
			iv_menu.startAnimation(mRotateAnimation);
			fm = getFragmentManager();
			ft = fm.beginTransaction();
			ft.setCustomAnimations(R.anim.push_right_left_in,
					R.anim.push_right_left_out);
			ft.remove(menu);
			ft.commit();
			menu_isopen = true;
		}
	}

	public void switchContent(int index) {

		switch (sp.getInt("index", 0)) {
		case 0:
			from = freedomfg;
			break;

		case 1:
			from = love;
			break;
		case 2:
			from = study;
			break;
		case 3:
			from = travel;
			break;

		}

		switch (index) {
		case 0:
			to = freedomfg;
			sp.edit().putInt("index", 0).commit();
			break;
		case 1:
			to = love;
			sp.edit().putInt("index", 1).commit();
			break;

		case 2:
			to = study;
			sp.edit().putInt("index", 2).commit();
			break;
		case 3:
			to = travel;
			sp.edit().putInt("index", 3).commit();
			break;
		}
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		if (from == null && to == from) {

		} else if (to.isAdded()) { // 先判断是否被add过
			ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
		} else {
			ft.hide(from).add(R.id.content_center, to).commit();
		}
	}
}
