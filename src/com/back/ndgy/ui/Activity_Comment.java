package com.back.ndgy.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.adapter.CommentAdapter;
import com.back.ndgy.data.LoveData;
import com.back.ndgy.data.Comment;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.StudyData;
import com.back.ndgy.data.TravelData;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;
import com.back.ndgy.utils.Utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @Back
 * @评论管理
 * @2015/5
 */
public class Activity_Comment extends Activity implements OnClickListener {
	private ImageView iv_pic, avater, iv_love;
	private TextView tv_content, tv_username, tv_love, tv_signature;
	private LoveData mData1;
	private StudyData mData2;
	private TravelData mData3;
	private FreedomSpeechDate mData;
	private AsyncBitmapLoader asyncBitmapLoader;
	private String commtentcontent, picurl;
	private ListView listview;
	private EditText comment_content;
	private Button comment_commit;
	private String avaterurl, reply_name, username, signature;
	private CommentAdapter adapter;
	private User byuser;
	private List<Comment> mComments = new ArrayList<Comment>();
	private SharedPreferences sp;
	private User author;// 作者
	private String content;// 文字
	private Integer love;// 点赞
	private boolean myLove;// 赞

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Myapplication.getInstance().addActivity(this);
		InitView();
		InitData();

	}

	/**
	 * 初始化视图控件
	 */
	private void InitView() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		tv_love = (TextView) findViewById(R.id.tv_love);
		iv_love = (ImageView) findViewById(R.id.iv_love);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_username = (TextView) findViewById(R.id.tv_name);
		tv_signature = (TextView) findViewById(R.id.tv_signature);
		listview = (ListView) findViewById(R.id.comment_list);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		avater = (ImageView) findViewById(R.id.user_icon_default_main);
		comment_content = (EditText) findViewById(R.id.comment_content);
		comment_commit = (Button) findViewById(R.id.comment_commit);
		comment_commit.setOnClickListener(this);
		iv_love.setOnClickListener(this);
		asyncBitmapLoader = new AsyncBitmapLoader();
		adapter = new CommentAdapter(Activity_Comment.this, mComments);
	}

	/**
	 * 初始化数据，接收来自Listview item的数据
	 */
	private void InitData() {

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		switch (sp.getInt("index", 0)) {
		case 0:
			mData = (FreedomSpeechDate) getIntent()
					.getSerializableExtra("data");
			username = mData.getAuthor().getUsername();
			content = mData.getContent();
			love = mData.getLove();
			myLove = mData.isMyLove();
			author = mData.getAuthor();
			signature = mData.getAuthor().getSignature();
			break;

		case 1:
			mData1 = (LoveData) getIntent().getSerializableExtra("data");
			username = mData1.getAuthor().getUsername();
			content = mData1.getContent();
			love = mData1.getLove();
			myLove = mData1.isMyLove();
			author = mData1.getAuthor();

			signature = mData1.getAuthor().getSignature();
			break;
		case 2:
			mData2 = (StudyData) getIntent().getSerializableExtra("data");
			username = mData2.getAuthor().getUsername();
			content = mData2.getContent();
			love = mData2.getLove();
			myLove = mData2.isMyLove();
			author = mData2.getAuthor();
			signature = mData2.getAuthor().getSignature();
			break;
		case 3:
			mData3 = (TravelData) getIntent().getSerializableExtra("data");
			username = mData3.getAuthor().getUsername();
			content = mData3.getContent();
			love = mData3.getLove();
			myLove = mData3.isMyLove();
			author = mData3.getAuthor();
			signature = mData3.getAuthor().getSignature();

			break;
		}

		tv_username.setText(username);
		tv_content.setText(content);
		tv_signature.setText(signature);
		tv_love.setText(love + "");
		picurl = getIntent().getStringExtra("picurl");
		avaterurl = getIntent().getStringExtra("avater");

		if (avaterurl != null) {

			/*
			 * 异步下载头用户像
			 */
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(avater, avaterurl, 0,
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				avater.setImageResource(R.drawable.user_icon_default_main);
			} else {
				avater.setImageBitmap(bitmap);
			}
		}

		if (picurl != null) {
			Bitmap bitmap = asyncBitmapLoader.loadBitmap(iv_pic, picurl, 0,
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							// TODO Auto-generated method stub
							imageView.setImageBitmap(bitmap);
						}
					});
			if (bitmap == null) {
				iv_pic.setImageResource(R.drawable.bg_pic_loading);
			} else {
				iv_pic.setImageBitmap(bitmap);
			}
		} else {
			iv_pic.setVisibility(View.GONE);
		}

		if (myLove) {
			iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
			tv_love.setTextColor(Color.parseColor("#3399FF"));
		} else {

			iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_default);
			tv_love.setTextColor(Color.parseColor("#666666"));

		}
		listview.setFocusable(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("pinglun", position + "");
				Comment td = (Comment) getItem(position);
				reply_name = td.getUser().getUsername();
				comment_content.setHint("回复  " + reply_name);
				byuser = td.getUser();
			}
		});

		fetchComment();
	}

	protected Comment getItem(int position) {
		return adapter.mComments.get(position);
	}

	/**
	 * 获取item的评论
	 */
	private void fetchComment() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		switch (sp.getInt("index", 0)) {
		case 0:
			query.addWhereRelatedTo("relation", new BmobPointer(mData));
			break;

		case 1:
			query.addWhereRelatedTo("relation", new BmobPointer(mData1));
			break;
		case 2:
			query.addWhereRelatedTo("relation", new BmobPointer(mData2));
			break;
		case 3:
			query.addWhereRelatedTo("relation", new BmobPointer(mData3));
			break;
		}

		query.include("user");
		query.order("createdAt");
		query.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				if (data != null) {
					mComments.clear();
					for (Comment td : data) {
						mComments.add(td);
					}

					listview.setAdapter(adapter);

					// 获取所有评论，给listview设定高度，使外层ScrollView能获取焦点
					Utility.setListViewHeightBasedOnChildren(listview);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_commit:
			onClickCommit();

			Log.i("点击", "开始评论");
			break;

		case R.id.iv_love:
			love();
			break;
		}
	}

	private void love() {
		if (myLove) {

		} else {
			switch (sp.getInt("index", 0)) {
			case 0:
				mData.setLove(mData.getLove() + 1);
				tv_love.setTextColor(Color.parseColor("#3399ff"));
				tv_love.setText(mData.getLove() + "");
				iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
				mData.setMyLove(true);
				mData.increment("love", 1);
				mData.update(this, new UpdateListener() {

					@Override
					public void onSuccess() {
						mData.setMyLove(true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {

						mData.setMyLove(true);
					}
				});
				break;

			case 1:

				mData1.setLove(mData1.getLove() + 1);
				tv_love.setTextColor(Color.parseColor("#3399ff"));
				tv_love.setText(mData1.getLove() + "");
				iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
				mData1.setMyLove(true);
				mData1.increment("love", 1);
				mData1.update(this, new UpdateListener() {

					@Override
					public void onSuccess() {
						mData1.setMyLove(true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {

						mData1.setMyLove(true);
					}
				});
			case 2:

				mData2.setLove(mData2.getLove() + 1);
				tv_love.setTextColor(Color.parseColor("#3399ff"));
				tv_love.setText(mData2.getLove() + "");
				iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
				mData2.setMyLove(true);
				mData2.increment("love", 1);
				mData2.update(this, new UpdateListener() {

					@Override
					public void onSuccess() {
						mData2.setMyLove(true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {

						mData2.setMyLove(true);
					}
				});
			case 3:

				mData3.setLove(mData3.getLove() + 1);
				tv_love.setTextColor(Color.parseColor("#3399ff"));
				tv_love.setText(mData3.getLove() + "");
				iv_love.setBackgroundResource(R.drawable.ndgy_icon_love_pressed);
				mData3.setMyLove(true);
				mData3.increment("love", 1);
				mData3.update(this, new UpdateListener() {

					@Override
					public void onSuccess() {
						mData3.setMyLove(true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {

						mData3.setMyLove(true);
					}
				});

				break;
			}

		}
	}

	private void onClickCommit() {

		commtentcontent = comment_content.getText().toString().trim();
		if (TextUtils.isEmpty(commtentcontent)) {
			ActivityUtils.Toast(this, "内容为空");
		} else {
			User currentUser = BmobUser.getCurrentUser(this, User.class);
			publishComment(currentUser, commtentcontent);
			Log.i("评论", "开始评论");
		}
	}

	/**
	 * 发布评论
	 * 
	 */
	private void publishComment(final User user, String content) {

		View view = View.inflate(this, R.layout.dialog_loading_data, null);
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		final Comment comment = new Comment();
		comment.setUser(user);
		if (reply_name != null) {
			comment.setByuser(byuser);
			comment.setNew_message(false);
			comment.setCommentContent(user.getUsername() + " 回复 " + reply_name
					+ " : " + content);
		} else {

			comment.setCommentContent(content);
		}
		switch (sp.getInt("index", 0)) {
		case 0:
			comment.setFreedom(mData);
			break;
		case 1:
			comment.setLove(mData1);
			break;
		case 2:
			comment.setStudy(mData2);
			break;
		case 3:
			comment.setTravel(mData3);
			break;
		}
		comment.setDatauser(author);
		comment.save(Activity_Comment.this, new SaveListener() {

			@Override
			public void onSuccess() {
				comment_content.setText("");
				hideSoftInput();

				// 将该评论与数据绑定到一起
				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				switch (sp.getInt("index", 0)) {
				case 0:
					mData.setRelation(relation);
					mData.update(Activity_Comment.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							fetchComment();
							ActivityUtils.Toast(Activity_Comment.this, "评论成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							ActivityUtils.Toast(Activity_Comment.this, "评论失败"
									+ arg1);
						}
					});

					Log.i("name", reply_name + "dd");
					break;

				case 1:
					mData1.setRelation(relation);
					mData1.update(Activity_Comment.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							fetchComment();
							ActivityUtils.Toast(Activity_Comment.this, "评论成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							ActivityUtils.Toast(Activity_Comment.this, "评论失败"
									+ arg1);
						}
					});

					Log.i("name", reply_name + "dd");

					break;
				case 2:
					mData2.setRelation(relation);
					mData2.update(Activity_Comment.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							fetchComment();
							ActivityUtils.Toast(Activity_Comment.this, "评论成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							ActivityUtils.Toast(Activity_Comment.this, "评论失败"
									+ arg1);
						}
					});

					Log.i("name", reply_name + "dd");

					break;
				case 3:
					mData3.setRelation(relation);
					mData3.update(Activity_Comment.this, new UpdateListener() {

						@Override
						public void onSuccess() {
							fetchComment();
							ActivityUtils.Toast(Activity_Comment.this, "评论成功");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							ActivityUtils.Toast(Activity_Comment.this, "评论失败"
									+ arg1);
						}
					});

					Log.i("name", reply_name + "dd");

					break;
				}

				dialog.cancel();

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ActivityUtils.Toast(Activity_Comment.this, "评论失败" + arg1);
			}
		});
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(comment_content.getWindowToken(), 0);
	}

}
