package com.back.ndgy.ui;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import com.back.ndgy.R;
import com.back.ndgy.utils.ActivityUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 
 * @Back
 * @µÇÂ¼
 * @2015/5
 */
public class Activity_Login extends Activity implements OnClickListener {
	private EditText user_name_input, user_password_input;
	private Button btn;
	private String email, name, password;
	private ImageView rigster;
	private FragmentManager fmm;
	private FragmentTransaction ftt;
	private Fragment_rigster f_rigster;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Bmob.initialize(this, "d25ecd4fc957b6e70b6a713987e2214e");
		user_name_input = (EditText) findViewById(R.id.user_name_input);
		user_password_input = (EditText) findViewById(R.id.user_password_input);
		rigster = (ImageView) findViewById(R.id.iv_rigster);
		btn = (Button) findViewById(R.id.btn_login);
		rigster.setOnClickListener(this);
		btn.setOnClickListener(this);
		sp = getSharedPreferences("infor", 0);
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_login:
			name = user_name_input.getText().toString().trim();
			password = user_password_input.getText().toString().trim();
			if (TextUtils.isEmpty(name)) {
				ActivityUtils.Toast(this, "ÓÃ»§ÃûÎª¿Õ");
			} else if (TextUtils.isEmpty(password)) {
				ActivityUtils.Toast(this, "ÃÜÂëÎª¿Õ");
			} else {
				sp.edit().putString("name", name).commit();
				sp.edit().putString("password", password).commit();
				login();
			}
			break;

		case R.id.iv_rigster:
			rigster(true);
			break;
		}

	}

	/**
	 *»ñÈ¡»º´æÓÃ»§£¬×Ô¶¯µÇÂ¼/×¢²á
	 *//*
	private void autologin() {
		if (sp.getString("name", "back000").equals("back000")) {

		} else {

			user_name_input.setText(sp.getString("name", "back000"));
			user_password_input.setText(sp.getString("password", "back000"));
			View view = View.inflate(this, R.layout.dialog_loading, null);
			final Dialog dialog = new Dialog(this, R.style.dialog);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			BmobUser login = new BmobUser();
			login.setUsername(sp.getString("name", "back000"));
			login.setPassword(sp.getString("password", "back000"));
			login.login(this, new SaveListener() {
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					ActivityUtils.Toast(Activity_UseRigster.this, "µÇ Â½ ³É ¹¦");
					Intent intent = new Intent(Activity_UseRigster.this,
							Activity_Main.class);
					startActivity(intent);
					dialog.cancel();
					finish();
				}

				@Override
				public void onFailure(int code, String msg) {
					// TODO Auto-generated method stub
					dialog.cancel();
					ActivityUtils.Toast(Activity_UseRigster.this, "µÇÂ½Ê§°Ü" + msg);

				}

			});
		}

	}*/

	/**
	 * µÇÂ¼
	 */
	private void login() {
		View view1 = View.inflate(this, R.layout.dialog_loading, null);
		final Dialog dialog1 = new Dialog(this, R.style.dialog);
		dialog1.setContentView(view1);
		dialog1.setCanceledOnTouchOutside(false);
		dialog1.show();
		BmobUser login = new BmobUser();
		login.setUsername(name);
		login.setPassword(password);
		login.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ActivityUtils.Toast(Activity_Login.this, "µÇ Â½ ³É ¹¦");
				Intent intent = new Intent(Activity_Login.this,
						Activity_Main.class);
				startActivity(intent);
				dialog1.cancel();
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				dialog1.cancel();
				ActivityUtils.Toast(Activity_Login.this, "µÇÂ½Ê§°Ü" + msg);

			}

		});
	}

	

	/**
	 * ´ò¿ª×¢²áÒ³
	 * @param on
	 */
	public void rigster(boolean on) {

		f_rigster = new Fragment_rigster();
		if (on) {

			fmm = getFragmentManager();
			ftt = fmm.beginTransaction();
			ftt.setCustomAnimations(R.anim.push_bottom_top_in,
					R.anim.push_top_bottom_in);
			ftt.replace(R.id.rl_rigster, f_rigster);
			ftt.addToBackStack(null);
			ftt.commit();
		} else {
			fmm = getFragmentManager();
			ftt = fmm.beginTransaction();
			ftt.setCustomAnimations(R.anim.push_bottom_top_in,
					R.anim.push_top_bottom_in);
			ftt.remove(f_rigster);
			ftt.addToBackStack(null);
			ftt.commit();
		}

	}

}
