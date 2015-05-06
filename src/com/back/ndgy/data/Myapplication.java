package com.back.ndgy.data;
import java.util.LinkedList;
import java.util.List;
import cn.bmob.v3.BmobUser;
import android.app.Activity;
import android.app.Application;

public class Myapplication extends Application {

	private static Myapplication instance;

	  private List<Activity> activityList = new LinkedList<Activity>();

	  public Myapplication() {
	  }

	  // 单例模式获取唯一的MyApplication实例
	  public static Myapplication getInstance() {
	    if (null == instance) {
	      instance = new Myapplication();
	    }
	    return instance;
	  }

	  // 添加Activity到容器中
	  public void addActivity(Activity activity) {
	    activityList.add(activity);
	  }

	  // 遍历所有Activity并finish
	  public void exit() {
	    for (Activity activity : activityList) {
	      activity.finish();
	    }
	    System.exit(0);
	  }
}

// public User getCurrentUser() {
// User user = BmobUser.getCurrentUser(myApplication, User.class);
// if (user != null) {
// return user;
// }
// return null;
// }
//
// @Override
// public void onCreate() {
// // TODO Auto-generated method stub
// super.onCreate();
// TAG = this.getClass().getSimpleName();
// // 由于Application类本身已经单例，所以直接按以下处理即可。
// myApplication = this;
//
// }
//
// public FreedomSpeechDate getCurrentData() {
// return currentData;
// }
//
// public void setCurrentData(FreedomSpeechDate currentData) {
// this.currentData = currentData;
// }

