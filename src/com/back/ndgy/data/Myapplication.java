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

	  // ����ģʽ��ȡΨһ��MyApplicationʵ��
	  public static Myapplication getInstance() {
	    if (null == instance) {
	      instance = new Myapplication();
	    }
	    return instance;
	  }

	  // ���Activity��������
	  public void addActivity(Activity activity) {
	    activityList.add(activity);
	  }

	  // ��������Activity��finish
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
// // ����Application�౾���Ѿ�����������ֱ�Ӱ����´����ɡ�
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

