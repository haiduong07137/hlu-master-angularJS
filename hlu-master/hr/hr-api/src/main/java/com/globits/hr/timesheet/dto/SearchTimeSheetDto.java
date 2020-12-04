package com.globits.hr.timesheet.dto;

import java.util.Date;

public class SearchTimeSheetDto {
	private Date workingDate;
	private String staffCode;
	private String displayName;
	private String codeAndName;
	
	public Date getWorkingDate() {
		return workingDate;
	}
	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}
	public String getStaffCode() {
		return staffCode;
	}
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCodeAndName() {
		return codeAndName;
	}
	public void setCodeAndName(String codeAndName) {
		this.codeAndName = codeAndName;
	}

}
