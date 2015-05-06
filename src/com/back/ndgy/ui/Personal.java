package com.back.ndgy.ui;

import java.io.ByteArrayOutputStream;

import com.back.ndgy.R;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
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
