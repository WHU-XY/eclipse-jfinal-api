package com.whu.jFinal.model;


public class TestModel {
	private String userId;
	private String loginName;
	private String nickName;
	private String password;
	private int sex;
	private String email;
	public TestModel() {
		
	}
	public String getuserId() {
		return userId;
	}
	public void setuserId(String userId) {
		this.userId=userId;
	}
	public String getloginName() {
		return loginName;
	}
	public void setloginName(String loginName) {
		this.loginName=loginName;
	}
	public String getnickName() {
		return nickName;
	}
	public void setnickName(String nickName) {
		this.nickName=nickName;
	}
	public String getpassword() {
		return password;
	}
	public void setpassword(String password) {
		this.password=password;
	}
	public int getsex() {
		return sex;
	}
	public void setsex(int sex) {
		this.sex=sex;
	}
	public String getemail() {
		return email;
	}
	public void setemail(String email) {
		this.email=email;
	}
}
