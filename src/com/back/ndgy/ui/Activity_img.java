package com.back.ndgy.ui;

import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.data.Myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Activity_img extends Activity {

private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img);
		Myapplication.getInstance().addActivity(this);
		String pic = getIntent().getStringExtra("picurl");
		ImageView img = (ImageView) findViewById(R.id.big_img);
		LinearLayout lin=(LinearLayout) findViewById(R.id.lin_img);
		lin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		if (pic != null) {
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(img, pic, 0,
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				img.setImageResource(R.drawable.bg_pic_loading);
			} else {
				img.setImageBitmap(bitmap);
			}
		}

	}
}
