package com.back.ndgy.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.back.ndgy.R;
import com.back.ndgy.adapter.AsyncBitmapLoader;
import com.back.ndgy.adapter.FreedomSpeechAdapter;
import com.back.ndgy.adapter.LoveAdapter;
import com.back.ndgy.adapter.AsyncBitmapLoader.ImageCallBack;
import com.back.ndgy.adapter.StudyAdapter;
import com.back.ndgy.adapter.TravelAdapter;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.data.LoveData;
import com.back.ndgy.data.Myapplication;
import com.back.ndgy.data.StudyData;
import com.back.ndgy.data.TravelData;
import com.back.ndgy.data.User;
import com.back.ndgy.utils.ActivityUtils;

import B.in;
import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Activity_person extends Activity implements
		RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener {
	private ImageView personal_icon;
	private TextView personl_name, personl_signature, text, text1, text2,
			text3;
	private RefreshListView listView, listView1, listView2, listView3;
	private User user;
	public static ArrayList<FreedomSpeechDate> DataList = new ArrayList<FreedomSpeechDate>();
	public static ArrayList<LoveData> DataList1 = new ArrayList<LoveData>();
	public static ArrayList<StudyData> DataList2 = new ArrayList<StudyData>();
	public static ArrayList<TravelData> DataList3 = new ArrayList<TravelData>();
	private AsyncBitmapLoader asyncBitmapLoader;
	private SharedPreferences sp;
	private LoveAdapter adapter1;
	private FreedomSpeechAdapter adapter;
	private LinearLayout linearLayout1;
	private ViewPager viewPager;
	private int currIndex = 0;// 当前页卡编号
	private ImageView imageView;// 页卡标题动画图片
	private int textViewW = 0, index;// 页卡标题的宽
	private List<View> listViews;
	private View view, view1, view2, view3;
	private static final int STATE_FRIST = 0;// 首次加载
	private static final int STATE_REFRESH = 1;// 下拉刷新
	private static final int STATE_MORE = 2;// 加载更多
	private int limit = 10; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始
	private FreedomSpeechAdapter madapter;
	private LoveAdapter madapter1;
	private StudyAdapter madapter2;
	private TravelAdapter madapter3;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		Myapplication.getInstance().addActivity(this);
		sp = getSharedPreferences("setting", 0);
		sp.edit().putInt("backindex", sp.getInt("index", 0)).commit();
		personal_icon = (ImageView) findViewById(R.id.personal_icon);
		personl_name = (TextView) findViewById(R.id.personl_name);
		personl_signature = (TextView) findViewById(R.id.personl_signature);
		imageView = (ImageView) findViewById(R.id.cursor);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		viewPager = (ViewPager) findViewById(R.id.vp);
		Init();
		initViewPager();
		listView = (RefreshListView) view.findViewById(R.id.freedom_lv);
		listView1 = (RefreshListView) view1.findViewById(R.id.freedom_lv);
		listView2 = (RefreshListView) view2.findViewById(R.id.freedom_lv);
		listView3 = (RefreshListView) view3.findViewById(R.id.freedom_lv);
		listView.setOnRefreshListener(Activity_person.this);
		listView.setOnLoadMoreListener(Activity_person.this);
		listView1.setOnRefreshListener(Activity_person.this);
		listView1.setOnLoadMoreListener(Activity_person.this);
		listView2.setOnRefreshListener(Activity_person.this);
		listView2.setOnLoadMoreListener(Activity_person.this);
		listView3.setOnRefreshListener(Activity_person.this);
		listView3.setOnLoadMoreListener(Activity_person.this);
		madapter = new FreedomSpeechAdapter(this, DataList, sp.getInt("width",
				0));
		madapter1 = new LoveAdapter(this, DataList1, sp.getInt("width", 0));
		madapter2 = new StudyAdapter(this, DataList2, sp.getInt("width", 0));
		madapter3 = new TravelAdapter(this, DataList3, sp.getInt("width", 0));
	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		mInflater = getLayoutInflater();
		view = mInflater.inflate(R.layout.fragment_data, null);
		view1 = mInflater.inflate(R.layout.fragment_data, null);
		view2 = mInflater.inflate(R.layout.fragment_data, null);
		view3 = mInflater.inflate(R.layout.fragment_data, null);
		listViews.add(view);
		listViews.add(view1);
		listViews.add(view2);
		listViews.add(view3);
		viewPager.setAdapter(new MyPagerAdapter(listViews));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		queryData(curPage, STATE_FRIST, 0);
	}

	private void Init() {
		user = BmobUser.getCurrentUser(this, User.class);
		asyncBitmapLoader = new AsyncBitmapLoader();
		BmobQuery<User> query = new BmobQuery<User>();
		query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
		query.getObject(this, user.getObjectId(), new GetListener<User>() {

			@Override
			public void onSuccess(User arg0) {
				String avaterurl = arg0.getAvatar().getFileUrl(
						Activity_person.this);
				if (avaterurl != null) {
					Bitmap bitmap = asyncBitmapLoader.loadBitmap(personal_icon,
							avaterurl, 0, new ImageCallBack() {

								@Override
								public void imageLoad(ImageView imageView,
										Bitmap bitmap) {
									// TODO Auto-generated method stub
									imageView.setImageBitmap(bitmap);
								}
							});
					if (bitmap == null) {
						personal_icon
								.setImageResource(R.drawable.user_icon_default_main);
					} else {
						personal_icon.setImageBitmap(bitmap);
					}
				}

				personl_name.setText(arg0.getUsername());
				personl_signature.setText(arg0.getSignature());
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {

			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/* 页卡切换监听 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {

			Log.i("change", arg0 + "");
			if (textViewW == 0) {
				textViewW = text1.getWidth();
			}
			Animation animation = new TranslateAnimation(textViewW * currIndex,
					textViewW * arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);/* True:图片停在动画结束位置 */
			animation.setDuration(400);
			imageView.startAnimation(animation);
			setTextTitleSelectedColor(arg0);
			setImageViewWidth(textViewW);
			index = arg0;
			queryData(0, STATE_FRIST, arg0);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			Log.i("StateChanged", arg0 + "");
		}

	}

	/* 设置标题文本的颜色 */
	private void setTextTitleSelectedColor(int arg0) {
		int count = viewPager.getChildCount();
		for (int i = 0; i < count; i++) {
			TextView mTextView = (TextView) linearLayout1.getChildAt(i);
			if (arg0 == i) {
				mTextView.setTextColor(0xff3399ff);
			} else {
				mTextView.setTextColor(0xff666666);
			}
		}
	}

	/* 设置图片宽度 */
	private void setImageViewWidth(int width) {
		if (width != imageView.getWidth()) {
			LayoutParams laParams = (LayoutParams) imageView.getLayoutParams();
			laParams.width = width;
			imageView.setLayoutParams(laParams);
		}
	}

	/* 标题点击监听 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	/* 初始化动画 */
	private void InitImageView() {

		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		imageView.setImageMatrix(matrix);//

	}

	private void queryData(final int page, final int actionType, final int index) {

		User user = BmobUser.getCurrentUser(this, User.class);

		switch (index) {
		case 0:
			sp.edit().putInt("index", 0).commit();
			BmobQuery<FreedomSpeechDate> query = new BmobQuery<FreedomSpeechDate>() {
			};
			query.order("-updateAt");
			query.setLimit(limit);
			query.setSkip(page * limit);
			query.include("author");
			query.addWhereEqualTo("author", user);
			query.findObjects(this, new FindListener<FreedomSpeechDate>() {

				@Override
				public void onSuccess(List<FreedomSpeechDate> arg0) {
					if (arg0.size() > 0) {
						if (actionType == STATE_FRIST) {
							DataList.clear();
							for (FreedomSpeechDate td : arg0) {
								DataList.add(td);
							}

							madapter = new FreedomSpeechAdapter(
									Activity_person.this, DataList, sp.getInt(
											"width", 0));
							listView.setAdapter(madapter);
							Log.i("FRIST", "数据更新");
						} else if (actionType == STATE_REFRESH) {
							curPage = 0;
							DataList.clear();
							for (FreedomSpeechDate td : arg0) {
								DataList.add(td);
							}
							Log.i("REFRESH", "数据更新");
							madapter.refreshData(DataList);
							listView.onLoadMoreComplete(false);
							listView.onRefreshComplete(true);
						} else if (actionType == STATE_MORE) {
							for (FreedomSpeechDate td : arg0) {
								DataList.add(td);
							}
							madapter.refreshData(DataList);
							listView.onLoadMoreComplete(false);
							Log.i("MORE", "数据更新");

						}
						curPage++;
					} else if (actionType == STATE_MORE) {
						listView.onLoadMoreComplete(true);
					} else if (actionType == STATE_REFRESH) {
						Log.i("STATE_RE___数据为空", "............");
						listView.onRefreshComplete(true);
						curPage = 0;
					}

				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});
			break;

		case 1:

			sp.edit().putInt("index", 1).commit();
			BmobQuery<LoveData> query1 = new BmobQuery<LoveData>() {
			};
			query1.order("-updateAt");
			query1.setLimit(limit);
			query1.setSkip(page * limit);
			query1.include("author");
			query1.addWhereEqualTo("author", user);
			query1.findObjects(this, new FindListener<LoveData>() {

				@Override
				public void onSuccess(List<LoveData> arg0) {
					if (arg0.size() > 0) {
						if (actionType == STATE_FRIST) {
							DataList1.clear();
							for (LoveData td : arg0) {
								DataList1.add(td);
							}
							listView1.setAdapter(madapter1);
							Log.i("FRIST", "数据更新");
						} else if (actionType == STATE_REFRESH) {
							curPage = 0;
							DataList1.clear();
							for (LoveData td : arg0) {
								DataList1.add(td);
							}
							Log.i("REFRESH", "数据更新");
							madapter1.refreshData(DataList1);
							listView1.onLoadMoreComplete(false);
							listView1.onRefreshComplete(true);
						} else if (actionType == STATE_MORE) {
							for (LoveData td : arg0) {
								DataList1.add(td);
							}
							madapter1.refreshData(DataList1);
							listView1.onLoadMoreComplete(false);
							Log.i("MORE", "数据更新");

						}
						curPage++;
					} else if (actionType == STATE_MORE) {
						listView1.onLoadMoreComplete(true);
					} else if (actionType == STATE_REFRESH) {
						Log.i("STATE_RE___数据为空", "............");
						listView1.onRefreshComplete(true);
						curPage = 0;
					}

				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case 2:

			sp.edit().putInt("index", 2).commit();
			BmobQuery<StudyData> query2 = new BmobQuery<StudyData>() {
			};
			query2.order("-updateAt");
			query2.setLimit(limit);
			query2.setSkip(page * limit);
			query2.include("author");
			query2.addWhereEqualTo("author", user);
			query2.findObjects(this, new FindListener<StudyData>() {

				@Override
				public void onSuccess(List<StudyData> arg0) {
					if (arg0.size() > 0) {
						if (actionType == STATE_FRIST) {
							DataList2.clear();
							for (StudyData td : arg0) {
								DataList2.add(td);
							}
							listView2.setAdapter(madapter2);
							Log.i("FRIST", "数据更新");
						} else if (actionType == STATE_REFRESH) {
							curPage = 0;
							DataList2.clear();
							for (StudyData td : arg0) {
								DataList2.add(td);
							}
							Log.i("REFRESH", "数据更新");
							madapter2.refreshData(DataList2);
							listView2.onLoadMoreComplete(false);
							listView2.onRefreshComplete(true);
						} else if (actionType == STATE_MORE) {
							for (StudyData td : arg0) {
								DataList2.add(td);
							}
							madapter2.refreshData(DataList2);
							listView2.onLoadMoreComplete(false);
							Log.i("MORE", "数据更新");

						}
						curPage++;
					} else if (actionType == STATE_MORE) {
						listView2.onLoadMoreComplete(true);
					} else if (actionType == STATE_REFRESH) {
						Log.i("STATE_RE___数据为空", "............");
						listView2.onRefreshComplete(true);
						curPage = 0;
					}

				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case 3:

			sp.edit().putInt("index", 3).commit();
			BmobQuery<TravelData> query3 = new BmobQuery<TravelData>() {
			};
			query3.order("-updateAt");
			query3.setLimit(limit);
			query3.setSkip(page * limit);
			query3.include("author");
			query3.addWhereEqualTo("author", user);
			query3.findObjects(this, new FindListener<TravelData>() {

				@Override
				public void onSuccess(List<TravelData> arg0) {
					if (arg0.size() > 0) {
						if (actionType == STATE_FRIST) {
							DataList3.clear();
							for (TravelData td : arg0) {
								DataList3.add(td);
							}
							listView3.setAdapter(madapter3);
							Log.i("FRIST", "数据更新");
						} else if (actionType == STATE_REFRESH) {
							curPage = 0;
							DataList3.clear();
							for (TravelData td : arg0) {
								DataList3.add(td);
							}
							Log.i("REFRESH", "数据更新");
							madapter3.refreshData(DataList3);
							listView3.onLoadMoreComplete(false);
							listView3.onRefreshComplete(true);
						} else if (actionType == STATE_MORE) {
							for (TravelData td : arg0) {
								DataList3.add(td);
							}
							madapter3.refreshData(DataList3);
							listView3.onLoadMoreComplete(false);
							Log.i("MORE", "数据更新");

						}
						curPage++;
					} else if (actionType == STATE_MORE) {
						listView3.onLoadMoreComplete(true);
					} else if (actionType == STATE_REFRESH) {
						Log.i("STATE_RE___数据为空", "............");
						listView3.onRefreshComplete(true);
						curPage = 0;
					}

				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}
			});
			break;
		}

	}

	@Override
	public void OnLoadMore() {
		queryData(curPage, STATE_MORE, index);

	}

	@Override
	public void OnRefresh() {
		queryData(0, STATE_REFRESH, index);

	}

	@Override
	protected void onDestroy() {
		Log.i("index", sp.getInt("index", 0) + "");
		Log.i("index", sp.getInt("backindex", 0) + "");
		sp.edit().putInt("index", sp.getInt("backindex", 0)).commit();
		Log.i("index", sp.getInt("index", 0) + "");
		super.onDestroy();
	}
}
