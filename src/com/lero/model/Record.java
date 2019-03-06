package com.lero.model;


public class Record {
	
	private int recordId;
	private String studentNumber;
	private String studentName;
	private String date;
	private String detail;
	private int dormBuildId;
	private String dormBuildName;
	private String dormName;
	private String startDate;
	private String endDate;
	
	public Record() {
	}
	
	public Record(String studentNumber, String date, String detail) {
		this.studentNumber = studentNumber;
		this.date = date;
		this.detail = detail;
	}
	
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getDormBuildName() {
		return dormBuildName;
	}
	public void setDormBuildName(String dormBuildName) {
		this.dormBuildName = dormBuildName;
	}
	public String getDormName() {
		return dormName;
	}
	public void setDormName(String dormName) {
		this.dormName = dormName;
	}

	public int getDormBuildId() {
		return dormBuildId;
	}

	public void setDormBuildId(int dormBuildId) {
		this.dormBuildId = dormBuildId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	
}
