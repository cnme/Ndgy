package com.back.ndgy.ui;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.back.ndgy.R;
import com.back.ndgy.data.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * @Back
 * @闪屏页
 * @2015/5
 */
public class Activity_Splash extends Activity {

	private final int DISPLAY_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Bmob.initialize(this, "d25ecd4fc957b6e70b6a713987e2214e");
		redirectByTime();
	}

	/**
	 * 根据时间进行页面跳转
	 */
	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				User user = BmobUser.getCurrentUser(Activity_Splash.this,
						User.class);
				if (user != null) {
					Intent mainIntent = new Intent(Activity_Splash.this,
							Activity_Main.class);
					Activity_Splash.this.startActivity(mainIntent);
					Activity_Splash.this.finish();
				} else {
					Intent intent = new Intent(Activity_Splash.this,
							Activity_Login.class);
					Activity_Splash.this.startActivity(intent);
					Activity_Splash.this.finish();
				}

			}

		}, DISPLAY_TIME);
	}
}
