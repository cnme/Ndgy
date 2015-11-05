package com.back.ndgy.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class GoodsData extends BmobObject {
	BmobFile pic;
	String detail;

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}
	
	public void setPic(BmobFile pic) {
		this.pic = pic;
	}

	public BmobFile getPic() {
		return pic;
	}
}
