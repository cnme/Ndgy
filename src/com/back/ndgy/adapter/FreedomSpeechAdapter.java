package com.back.ndgy.adapter;

import java.util.ArrayList;
import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.data.User;
import com.back.ndgy.ui.Activity_Comment;
import com.back.ndgy.ui.RefreshListView;
import com.back.ndgy.ui.Activity_img;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FreedomSpeechAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<FreedomSpeechDate> DataList;
	private Context context;
	private String picurl, content, avatarUrl = null;;
	private ViewHolder holder = null;
	private FreedomSpeechDate mData;
	private User user;
	private AsyncBitmapLoader asyncBitmapLoader;
	private boolean isScrolling = true;
	private RefreshListView listview;
	private SharedPreferences sp;
	private int width;

	public FreedomSpeechAdapter(Context context,
			ArrayList<FreedomSpeechDate> arg0, int width) {
		this.DataList = arg0;
		inflater = LayoutInflater.from(context);
		this.context = context;
		Log.i("适配器传入成功", ".............");
		asyncBitmapLoader = new AsyncBitmapLoader();
		listview = new RefreshListView(context);
		this.width = width;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		/* 重复利用converView */
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_item, null);
			holder = new ViewHolder();
			holder.tv_username = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.avater = (ImageView) convertView
					.findViewById(R.id.user_icon_default_main);
			holder.tv_signature = (TextView) convertView
					.findViewById(R.id.tv_signature);
			holder.tv_comment = (TextView) convertView
					.findViewById(R.id.tv_comment);
			holder.tv_love = (TextView) convertView.findViewById(R.id.tv_love);
			holder.img = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.iv_love = (ImageView) convertView.findViewById(R.id.iv_love);
			holder.rl_iv = (RelativeLayout) convertView
					.findViewById(R.id.rl_iv);
			holder.lin_commect = (LinearLayout) convertView
					.findViewById(R.id.lin_detail);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		mData = (FreedomSpeechDate) getItem(position);
		user = mData.getAuthor();
		holder.tv_username.setText(user.getUsername());
		holder.tv_signature.setText(user.getSignature());
		holder.tv_content.setText(mData.getContent());
		holder.tv_love.setText(mData.getLove() + "");

		if (user.getAvatar() != null) {
			avatarUrl = user.getAvatar().getFileUrl(context);
			if (avatarUrl != null) {
				holder.avater.setTag(avatarUrl);
				Bitmap bitmapavater = asyncBitmapLoader.loadBitmap(
						holder.avater, avatarUrl, listview.isScrolling,
						new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								imageView.setImageBitmap(bitmap);
							}
						});
				if (bitmapavater == null) {
					holder.avater
							.setImageResource(R.drawable.user_icon_default_main);
				} else if (holder.avater.getTag().equals(avatarUrl)) {

					holder.avater.setImageBitmap(bitmapavater);

				}
			}

		}

		if (mData.getPicurl() == null) {
			holder.img.setVisibility(View.GONE);

		} else {
			picurl = mData.getPicurl().getFileUrl(context);
			Log.i("test", picurl);
			holder.img.setTag(picurl);
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(holder.img, picurl,
					listview.isScrolling, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				holder.img.setImageResource(R.drawable.bg_pic_loading);
			} else if (holder.img.getTag().equals(picurl)) {
				LayoutParams imgParams = holder.img.getLayoutParams();
				Log.i(holder.rl_iv.getWidth() + "", imgParams.width + "");
				if (bitmap.getWidth() >= width - 60
						&& bitmap.getHeight() >= width - 60) {
					imgParams.width = width - 60;
					imgParams.height = width - 60;
				} else if (bitmap.getHeight() < width - 60
						&& bitmap.getWidth() > width - 60) {
					imgParams.width = width - 60;
					imgParams.height = bitmap.getHeight();
				} else if (bitmap.getWidth() < width - 60
						&& bitmap.getHeight() > width - 60) {
					imgParams.height = width - 60;
					imgParams.width = bitmap.getWidth();
				} else if (bitmap.getWidth() < width - 60
						&& bitmap.getHeight() < width - 60) {
					imgParams.height = bitmap.getHeight();
					imgParams.width = bitmap.getWidth();
				}

				holder.img.setLayoutParams(imgParams);
				holder.img.setImageBitmap(bitmap);
				holder.img.setVisibility(View.VISIBLE);
			}

		}

		holder.lin_commect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mData = (FreedomSpeechDate) getItem(position);
				Intent intent = new Intent();
				intent.putExtra("data", mData);
				intent.putExtra("avater", mData.getAuthor().getAvatar()
						.getFileUrl(context));
				if (mData.getPicurl() != null) {
					intent.putExtra("picurl",
							mData.getPicurl().getFileUrl(context));
				}
				intent.setClass(context, Activity_Comment.class);
				context.startActivity(intent);

			}
		});

		if (mData.isMyLove()) {
			holder.iv_love
					.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
			holder.tv_love.setTextColor(Color.parseColor("#3399FF"));
		} else {

			holder.iv_love
					.setBackgroundResource(R.drawable.ndgy_icon_love_default);
			holder.tv_love.setTextColor(Color.parseColor("#666666"));
		}

		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mData = (FreedomSpeechDate) getItem(position);
				Intent intent = new Intent();
				intent.putExtra("picurl", mData.getPicurl().getFileUrl(context));
				intent.setClass(context, Activity_img.class);
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	/* 内部类 ，缓存item项视图控件 */
	class ViewHolder {
		ImageView img;
		TextView tv_content, tv_love, tv_username, tv_comment, tv_signature;
		ImageView iv_love, avater;
		LinearLayout lin_commect;
		RelativeLayout rl_iv;
	}

	public void refreshData(ArrayList<FreedomSpeechDate> arg0) {
		this.DataList = arg0;
		notifyDataSetChanged();//刷新视图

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DataList.size();
	}

	@Override
	public Object getItem(int position) {
		Log.i("位置", DataList.get(position) + "");
		return DataList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

}
