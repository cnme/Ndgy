package com.back.ndgy.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.back.ndgy.R;
import com.back.ndgy.adapter.FreedomSpeechAdapter;
import com.back.ndgy.data.FreedomSpeechDate;
import com.back.ndgy.utils.ActivityUtils;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * 
 * @Back
 * @自由区
 * @2015/5
 */
public class Fragment_freedom extends Fragment implements OnClickListener,
		RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener {
	private View rootview;
	private RefreshListView freedoom_lv;
	private FreedomSpeechAdapter mcadapter;
	private SharedPreferences spPreferences;
	public static ArrayList<FreedomSpeechDate> DataList = new ArrayList<FreedomSpeechDate>();
	private static final int STATE_FRIST = 0;// 首次加载
	private static final int STATE_REFRESH = 1;// 下拉刷新
	private static final int STATE_MORE = 2;// 加载更多
	private int limit = 10; // 每页的数据是10条
	private int curPage = 0; // 当前页的编号，从0开始
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_data, container, false);
		freedoom_lv = (RefreshListView) rootview.findViewById(R.id.freedom_lv);
		queryData(curPage, STATE_FRIST);
		onload();
		return rootview;
	}

	/* 在onDestroyView方法中释放占用的UI资源 */
	@Override
	public void onDestroyView() {
		mcadapter = null;
		freedoom_lv = null;
		rootview = null;
		DataList.clear();
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		if (sp.getInt("update", 0) == 1) {
			queryData(0, STATE_REFRESH);
			sp.edit().putInt("update", 0).commit();
		}
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/* 注册listview下拉刷新，上拉加载更多 */
	protected void onload() {
		freedoom_lv.setOnRefreshListener(this);
		freedoom_lv.setOnLoadMoreListener(this);

		sp = getActivity().getSharedPreferences("setting", 0);
		mcadapter = new FreedomSpeechAdapter(getActivity(), DataList,
				sp.getInt("width", 0));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnLoadMore() {
		queryData(curPage, STATE_MORE);
	}

	@Override
	public void OnRefresh() {

		queryData(0, STATE_REFRESH);

	}

	/**
	 * 获取数据
	 * 
	 * @param page
	 * @param actionType
	 */
	public void queryData(final int page, final int actionType) {
		new Thread() {
			public void run() {
				BmobQuery<FreedomSpeechDate> query = new BmobQuery<FreedomSpeechDate>();
				query.order("-updatedAt");
				query.setLimit(limit);
				query.setSkip(page * limit);
				query.include("author");
				query.findObjects(getActivity(),
						new FindListener<FreedomSpeechDate>() {

							@Override
							public void onSuccess(List<FreedomSpeechDate> arg0) {

								if (arg0.size() > 0) {
									if (actionType == STATE_FRIST) {
										DataList.clear();
										for (FreedomSpeechDate td : arg0) {
											DataList.add(td);
										}
										freedoom_lv.setAdapter(mcadapter);
										Log.i("FRIST", "数据更新");
									} else if (actionType == STATE_REFRESH) {
										curPage = 0;
										DataList.clear();
										for (FreedomSpeechDate td : arg0) {
											DataList.add(td);
										}
										Log.i("REFRESH", "数据更新");
										mcadapter.refreshData(DataList);
										freedoom_lv.onLoadMoreComplete(false);
										freedoom_lv.onRefreshComplete(true);
									} else if (actionType == STATE_MORE) {
										for (FreedomSpeechDate td : arg0) {
											DataList.add(td);
										}
										mcadapter.refreshData(DataList);
										freedoom_lv.onLoadMoreComplete(false);
										Log.i("MORE", "数据更新");

									}
									curPage++;
								} else if (actionType == STATE_MORE) {
									freedoom_lv.onLoadMoreComplete(true);
								} else if (actionType == STATE_REFRESH) {
									Log.i("STATE_RE___数据为空", "............");
									freedoom_lv.onRefreshComplete(true);
									curPage = 0;
								}

							}

							@Override
							public void onError(int arg0, String arg1) {

								ActivityUtils.Toast(getActivity(), arg1);

							}
						});

			};

		}.start();
	}

}
