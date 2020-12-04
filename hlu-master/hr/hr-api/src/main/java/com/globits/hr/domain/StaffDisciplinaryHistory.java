package com.globits.hr.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/**
 * 
 * @author dunghq
 * Bảng kỷ luật nhân viên
 *
 */
@XmlRootElement
@Table(name = "tbl_staff_disciplinary_history")
@Entity
public class StaffDisciplinaryHistory extends BaseObject {
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
	@Column(name="from_date")
	private Date fromDate;//Ngày bắt đầu kỷ luật
	
	@Column(name="description")
	private String description;

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
