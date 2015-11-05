package com.back.ndgy.adapter;

import java.util.List;
import cn.bmob.v3.listener.UpdateListener;
import com.back.ndgy.R;
import com.back.ndgy.data.Comment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	String url;
	ViewHolder holder = null;
	Comment mData;
	String content;
	public List<Comment> mComments;

	// private SharedPreferences sp;

	public NoticeAdapter(Context context, List<Comment> arg0) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		mComments = arg0;
		// sp = getSharedPreferences("setting", MODE_PRIVATE);
		Log.i("适配器com", "传入成功" + arg0.size() + "");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.notice_item, null);
			holder = new ViewHolder();
			holder.tv_notice_content = (TextView) convertView
					.findViewById(R.id.tv_notice_content);
			holder.tv_notice_user = (TextView) convertView
					.findViewById(R.id.tv_notice_name);
			holder.lin_notice = (LinearLayout) convertView
					.findViewById(R.id.lin_noitce);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			Log.i("View不为空", "ddddddd");

		}
		mData = (Comment) getItem(position);
		holder.tv_notice_user.setText(mData.getUser().getUsername());
		holder.tv_notice_content.setText(mData.getCommentContent());

	//	Log.i("user", mData.getDatauser().getUsername());
		 Comment comment = new Comment();
		 comment.setNew_message(true);
		 comment.update(context,mData.getObjectId(), new UpdateListener() {
		
		 @Override
		 public void onSuccess() {
		
		 Log.i("message", "true");
		
		 }
		
		 @Override
		 public void onFailure(int arg0, String arg1) {
		 // TODO Auto-generated method stub
		
		 }
		 });
	//	listView();
		return convertView;

	}

	
	class ViewHolder {

		TextView tv_notice_content, tv_notice_user;
		LinearLayout lin_notice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.i("getCount", "传入成功" + mComments.size() + "");
		return mComments.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
