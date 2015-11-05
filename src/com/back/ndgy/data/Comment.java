package com.back.ndgy.data;

import cn.bmob.v3.BmobObject;

/**
 * @author Back
 * @date 2015-4-22 TODO
 */

public class Comment extends BmobObject {

	private User user;
	private String commentContent;
	private User datauser;
	private User byuser;
	private boolean new_message;
	private FreedomSpeechDate freedom;
	private LoveData love;
	private StudyData study;
	private TravelData travel;

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public User getDatauser() {
		return datauser;
	}

	public void setDatauser(User datauser) {
		this.datauser = datauser;
	}

	public User getByuser() {
		return byuser;
	}

	public void setByuser(User byuser) {
		this.byuser = byuser;
	}

	public boolean isNew_message() {
		return new_message;
	}

	public void setNew_message(boolean new_message) {
		this.new_message = new_message;
	}

	public FreedomSpeechDate getFreedom() {
		return freedom;
	}

	public void setFreedom(FreedomSpeechDate freedom) {
		this.freedom = freedom;
	}

	public LoveData getLove() {
		return love;
	}

	public void setLove(LoveData love) {
		this.love = love;
	}

	public StudyData getStudy() {
		return study;
	}

	public void setStudy(StudyData study) {
		this.study = study;
	}

	public TravelData getTravel() {
		return travel;
	}

	public void setTravel(TravelData travel) {
		this.travel = travel;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
