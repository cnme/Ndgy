package com.back.ndgy.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.back.ndgy.R;
import com.back.ndgy.adapter.NoticeAdapter;
import com.back.ndgy.data.Comment;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.data.LoveData;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.StudyData;
import com.back.ndgy.data.TravelData;
import com.back.ndgy.data.User;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Activity_Notice extends Activity {

	private ListView lv_notice;
	private NoticeAdapter noticeadapter;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		Myapplication.getInstance().addActivity(this);

		lv_notice = (ListView) findViewById(R.id.listView_notice);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		sp.edit().putInt("backindex", sp.getInt("index", 0)).commit();
		sp.edit().putInt("message", 0).commit();
		InitData();
		OnClick();
	}

	private void InitData() {
		Log.i("notice", "查询数据");
		Log.i("notice", "开始notice");
		User user = BmobUser.getCurrentUser(this, User.class);
		Comment comment = new Comment();

		BmobQuery<Comment> eq2 = new BmobQuery<Comment>();
		eq2.addWhereEqualTo("datauser", user);
		BmobQuery<Comment> eq3 = new BmobQuery<Comment>();
		eq3.addWhereEqualTo("byuser", user);
		BmobQuery<Comment> eq4 = new BmobQuery<Comment>();
		eq4.include("freedom");
		BmobQuery<Comment> eq5 = new BmobQuery<Comment>();
		eq5.include("love");
		BmobQuery<Comment> eq6 = new BmobQuery<Comment>();
		eq6.include("study");
		BmobQuery<Comment> eq7 = new BmobQuery<Comment>();
		eq7.include("travel");
		BmobQuery<Comment> eq8 = new BmobQuery<Comment>();
		eq8.include("datauser");
		List<BmobQuery<Comment>> orQuerys = new ArrayList<BmobQuery<Comment>>();
		orQuerys.add(eq2);
		orQuerys.add(eq3);
		orQuerys.add(eq4);
		orQuerys.add(eq5);
		orQuerys.add(eq6);
		orQuerys.add(eq7);
		orQuerys.add(eq8);
		BmobQuery<Comment> mainQuery = new BmobQuery<Comment>();
		BmobQuery<Comment> or = mainQuery.or(orQuerys);

		List<BmobQuery<Comment>> andQuerys = new ArrayList<BmobQuery<Comment>>();
		// andQuerys.add(eq1);
		andQuerys.add(or);
		BmobQuery<Comment> noticeQuery = new BmobQuery<Comment>();
		noticeQuery.and(andQuerys);
		noticeQuery.include("user");
		noticeQuery.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				noticeadapter = new NoticeAdapter(Activity_Notice.this, arg0);
				lv_notice.setAdapter(noticeadapter);

			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void OnClick() {
		lv_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Comment td = (Comment) getItem(position);
				new Thread() {
					public void run() {

						if (td.getFreedom() == null) {
							Log.i("ID为空f", "kongongkongg");
						} else {

							Log.i("ID为空ff", "kongongkongg");
							BmobQuery<FreedomSpeechDate> query = new BmobQuery<FreedomSpeechDate>();
							query.include("author");
							query.getObject(Activity_Notice.this, td
									.getFreedom().getObjectId(),
									new GetListener<FreedomSpeechDate>() {
										@Override
										public void onSuccess(
												FreedomSpeechDate arg0) {
											sp.edit().putInt("index", 0)
													.commit();
											Intent intent = new Intent();
											intent.putExtra("data", arg0);
											Log.i("intent", "kongongkongg");
											intent.putExtra(
													"avater",
													arg0.getAuthor()
															.getAvatar()
															.getFileUrl(
																	Activity_Notice.this));
											intent.putExtra(
													"picurl",
													arg0.getPicurl()
															.getFileUrl(
																	Activity_Notice.this));
											intent.setClass(
													Activity_Notice.this,
													Activity_Comment.class);
											Activity_Notice.this
													.startActivity(intent);
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {
											// TODO Auto-generated method stub

										}

									});

							if (td.getLove() == null) {
								Log.i("ID为空l", "kongongkongg");
							} else {
								Log.i("ID为空ll", "kongongkongg");
								BmobQuery<LoveData> querylove = new BmobQuery<LoveData>();
								querylove.include("author");
								querylove.getObject(Activity_Notice.this, td
										.getLove().getObjectId(),
										new GetListener<LoveData>() {
											@Override
											public void onSuccess(LoveData arg0) {
												sp.edit().putInt("index", 1)
														.commit();
												Intent intent = new Intent();
												intent.putExtra("data", arg0);
												intent.putExtra(
														"avater",
														arg0.getAuthor()
																.getAvatar()
																.getFileUrl(
																		Activity_Notice.this));
												intent.putExtra(
														"picurl",
														arg0.getPicurl()
																.getFileUrl(
																		Activity_Notice.this));
												intent.setClass(
														Activity_Notice.this,
														Activity_Comment.class);
												Activity_Notice.this
														.startActivity(intent);

											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												// TODO Auto-generated method
												// stub

											}

										});

								if (td.getStudy() == null) {
									Log.i("ID为空s", "kongongkongg");
								} else {
									Log.i("ID为空ss", "kongongkongg");
									BmobQuery<StudyData> querystudy = new BmobQuery<StudyData>();
									querystudy.include("author");
									querystudy.getObject(Activity_Notice.this,
											td.getStudy().getObjectId(),
											new GetListener<StudyData>() {
												@Override
												public void onSuccess(
														StudyData arg0) {
													sp.edit()
															.putInt("index", 2);
													// .commit();
													Intent intent = new Intent();
													intent.putExtra("data",
															arg0);
													intent.putExtra(
															"avater",
															arg0.getAuthor()
																	.getAvatar()
																	.getFileUrl(
																			Activity_Notice.this));
													intent.putExtra(
															"picurl",
															arg0.getPicurl()
																	.getFileUrl(
																			Activity_Notice.this));
													intent.setClass(
															Activity_Notice.this,
															Activity_Comment.class);
													Activity_Notice.this
															.startActivity(intent);

												}

												@Override
												public void onFailure(int arg0,
														String arg1) {
													// TODO Auto-generated
													// method stub

												}

											});

									if (td.getTravel() == null) {
										Log.i("ID为空t", "kongongkongg");
									} else {
										Log.i("ID为空tt", "kongongkongg");
										BmobQuery<TravelData> querytravel = new BmobQuery<TravelData>();
										querytravel.include("author");
										querytravel.getObject(
												Activity_Notice.this, td
														.getTravel()
														.getObjectId(),
												new GetListener<TravelData>() {
													@Override
													public void onSuccess(
															TravelData arg0) {
														sp.edit()
																.putInt("index",
																		3)
																.commit();
														Intent intent = new Intent();
														intent.putExtra("data",
																arg0);
														intent.putExtra(
																"avater",
																arg0.getAuthor()
																		.getAvatar()
																		.getFileUrl(
																				Activity_Notice.this));
														intent.putExtra(
																"picurl",
																arg0.getPicurl()
																		.getFileUrl(
																				Activity_Notice.this));
														intent.setClass(
																Activity_Notice.this,
																Activity_Comment.class);
														Activity_Notice.this
																.startActivity(intent);

													}

													@Override
													public void onFailure(
															int arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub

													}

												});

									}

								}

							}
						}
					};
				}.start();
			}
		});

	};

	protected Comment getItem(int position) {
		// TODO Auto-generated method stub
		return noticeadapter.mComments.get(position);
	}

	@Override
	protected void onDestroy() {
		sp.edit().putInt("index", sp.getInt("backindex", 0)).commit();
		super.onDestroy();
	}

}
