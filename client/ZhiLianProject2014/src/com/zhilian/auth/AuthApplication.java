package com.zhilian.auth;

import android.app.Application;

public class AuthApplication extends Application {

	private String userphone = null;
	private String userpassword = null;
	private String username = "";
	private String userdemand_field = "";
	private String userdemand_job = "";
	private String userdemand_salary = "0";
	private String userdemand_experience = "0";
	private String resume_path = null;
	private String resume_name = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}
	
	public String getUserdemand_field() {
		return userdemand_field;
	}

	public void setUserdemand_field(String userdemand_field) {
		this.userdemand_field = userdemand_field;
	}

	public String getUserdemand_job() {
		return userdemand_job;
	}

	public void setUserdemand_job(String userdemand_job) {
		this.userdemand_job = userdemand_job;
	}

	public String getUserdemand_salary() {
		return userdemand_salary;
	}

	public void setUserdemand_salary(String userdemand_salary) {
		this.userdemand_salary = userdemand_salary;
	}

	public String getUserdemand_experience() {
		return userdemand_experience;
	}

	public void setUserdemand_experience(String userdemand_experience) {
		this.userdemand_experience = userdemand_experience;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getResume_path() {
		return resume_path;
	}

	public void setResume_path(String resume_path) {
		this.resume_path = resume_path;
	}

	public String getResume_name() {
		return resume_name;
	}

	public void setResume_name(String resume_name) {
		this.resume_name = resume_name;
	}
}
