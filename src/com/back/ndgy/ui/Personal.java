package com.back.ndgy.ui;
import com.back.ndgy.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Personal extends Activity {
	ImageView personal_icon;
	int width, hight;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		personal_icon = (ImageView) findViewById(R.id.personal_icon);
		personal_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			

			}
		});
	}

	

}
