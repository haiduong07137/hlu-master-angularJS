package com.globits.hr.dto;

import java.util.Date;

import javax.persistence.Column;

import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffEducationHistory;

public class StaffEducationHistoryDto {

	private Long id;
	private Staff staff;
	private Date startDate;
	private Date endDate;
	private String schoolName;
	private String description;
	private Integer status;// Trạng thái hiện thời

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public StaffEducationHistoryDto() {
		super();
	}

	public StaffEducationHistoryDto(StaffEducationHistory educationHistory) {
		if(educationHistory != null)
		this.id = educationHistory.getId();
		this.staff = new Staff(educationHistory.getStaff().getId(),educationHistory.getStaff().getFirstName(), educationHistory.getStaff().getLastName(), educationHistory.getStaff().getStaffCode(), educationHistory.getStaff().getDisplayName());
		this.startDate = educationHistory.getStartDate();
		this.endDate = educationHistory.getEndDate();
		this.schoolName = educationHistory.getSchoolName();
		this.description = educationHistory.getDescription();
		this.status = educationHistory.getStatus();
	}

}
