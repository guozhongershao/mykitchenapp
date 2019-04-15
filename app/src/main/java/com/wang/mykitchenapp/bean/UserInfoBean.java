package com.wang.mykitchenapp.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private String userName;
	private String userPassword;
	private String userAccessToken;
	private String userNickName;
	private String userGender;
	private String userEmail;
	private String userAvatar;
	private String userPhoneNumber;
	

	public String getUserPassword(){return userPassword;}

	public void setUserPassword(String password){this.userPassword = password;}
	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserAccessToken() {
		return userAccessToken;
	}



	public void setUserAccessToken(String userAccessToken) {
		this.userAccessToken = userAccessToken;
	}



	public String getUserNickName() {
		return userNickName;
	}



	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}



	public String getUserGender() {
		return userGender;
	}



	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}



	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public String getUserAvatar() {
		return userAvatar;
	}



	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}


	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}



	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
}
