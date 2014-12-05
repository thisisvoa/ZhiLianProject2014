package com.recruiter.bean;

public class LbsDataAdd {
	private String _name;
	private String _location;
	private String _address;
	private String industry = "IT";
	private String field;
	private String job;
	private String jobname;
	private String salary_min;
	private String salary_max;
	private String experience;
	
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public String get_location() {
		return _location;
	}
	public void set_location(String _location) {
		this._location = _location;
	}
	public String get_address() {
		return _address;
	}
	public void set_address(String _address) {
		this._address = _address;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getSalary_min() {
		return salary_min;
	}
	public void setSalary_min(String salary_min) {
		this.salary_min = salary_min;
	}
	public String getSalary_max() {
		return salary_max;
	}
	public void setSalary_max(String salary_max) {
		this.salary_max = salary_max;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
}
