package com.back.ndgy.ui;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.back.ndgy.R;
import com.back.ndgy.data.GoodsData;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @Back
 * @失物招领
 * @2015/5
 */
public class Activity_goods extends Activity {

	public static int size;
	public static String[] imageUrls = new String[17];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		querydata();

	}

	/**
	 * 获取数据
	 */
	public final void querydata() {

		BmobQuery<GoodsData> gQuery = new BmobQuery<GoodsData>();
		gQuery.order("-updatedAt");
		gQuery.setLimit(1000);
		gQuery.addQueryKeys("pic");
		gQuery.findObjects(Activity_goods.this, new FindListener<GoodsData>() {

			@Override
			public void onSuccess(List<GoodsData> arg0) {
				size = arg0.size();
				for (int i = 0; i < arg0.size(); i++) {
					imageUrls[i] = arg0.get(i).getPic()
							.getFileUrl(Activity_goods.this);
				}
				setContentView(R.layout.activity_goods);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

}
