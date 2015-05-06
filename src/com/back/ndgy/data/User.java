package com.back.ndgy.data;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser {

	private String signature;
	private BmobFile avatar;
	private BmobRelation favorite;
	private String sex;
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile response) {
		this.avatar = response;
	}
	public BmobRelation getFavorite() {
		return favorite;
	}
	public void setFavorite(BmobRelation favorite) {
		this.favorite = favorite;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
