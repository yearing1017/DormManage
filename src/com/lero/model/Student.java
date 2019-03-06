package com.lero.model;

public class Student {
	private int studentId;
	private String stuNumber;
	private String userName;
	private String password;
	private int dormBuildId = 0;
	private String dormBuildName;
	private String dormName;
	private String name;
	private String sex;
	private String tel;
	
	public Student() {
	}
	
	public Student(String userName, String password) {
		this.stuNumber = userName;
		this.userName = userName;
		this.password = password;
	}
	
	
	public Student(String stuNumber, String password, int dormBuildId,
			String dormName, String name, String sex, String tel) {
		this.stuNumber = stuNumber;
		this.userName = stuNumber;
		this.password = password;
		this.dormBuildId = dormBuildId;
		this.dormName = dormName;
		this.name = name;
		this.sex = sex;
		this.tel = tel;
	}

	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		this.stuNumber = userName;
	}
	public String getStuNumber() {
		return stuNumber;
	}
	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
		this.userName = stuNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getDormBuildId() {
		return dormBuildId;
	}

	public void setDormBuildId(int dormBuildId) {
		this.dormBuildId = dormBuildId;
	}

	public String getDormBuildName() {
		return dormBuildName;
	}

	public void setDormBuildName(String dormBuildName) {
		this.dormBuildName = dormBuildName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getDormName() {
		return dormName;
	}

	public void setDormName(String dormName) {
		this.dormName = dormName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
