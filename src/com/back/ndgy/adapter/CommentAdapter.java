package com.back.ndgy.adapter;

import java.util.List;
import com.back.ndgy.R;
import com.back.ndgy.data.Comment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	String url;
	ViewHolder holder = null;
	Comment mData, mData1;
	String content;
	public List<Comment> mComments;

	public CommentAdapter(Context context, List<Comment> arg0) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		mComments = arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comment_item, null);
			holder = new ViewHolder();
			holder.lin_comment = (LinearLayout) convertView
					.findViewById(R.id.lin_comment);
			holder.tv_user_comment = (TextView) convertView
					.findViewById(R.id.userName_comment);
			holder.tv_content_comment = (TextView) convertView
					.findViewById(R.id.content_comment);
			holder.tv_index = (TextView) convertView
					.findViewById(R.id.index_comment);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		mData = (Comment) getItem(position);
		holder.tv_index.setText((position + 1) + "¥");
		holder.tv_content_comment.setText(mData.getCommentContent());
		holder.tv_user_comment.setText(mData.getUser().getUsername());

		return convertView;

	}

	class ViewHolder {
		ImageView img, iv_ad;
		TextView tv_content_comment, tv_user_comment, tv_index, tv_love;
		ImageView iv_love;
		LinearLayout lin_comment;
	}

	public void refreshData(List<Comment> arg0) {
		this.mComments = arg0;
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {

		return mComments.size();

	}

	@Override
	public Object getItem(int position) {

		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

}
