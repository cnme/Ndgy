package com.back.ndgy.data;

import android.R.integer;

import com.bmob.BmobProFile;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class FreedomSpeechDate extends BmobObject {
	/**
	 * ÿ��ListView item���� 2015/4/14 19:44
	 */

	private User author;// ����
	private String content;// ����
	private BmobFile picurl;// ͼƬ����
	private Integer love;// ����
	private Integer comment;// ����
	private boolean isPass;
	private boolean myLove;// ��
	private BmobRelation relation;// ����

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}

	public BmobRelation getRelation() {
		return relation;
	}

	public void setRelation(BmobRelation relation) {
		this.relation = relation;
	}

	public BmobFile getPicurl() {
		return picurl;
	}

	public void setPicurl(BmobFile picurl) {
		this.picurl = picurl;
	}

	public boolean isMyLove() {
		return myLove;
	}

	public void setMyLove(boolean myLove) {
		this.myLove = myLove;
	}

	
}
