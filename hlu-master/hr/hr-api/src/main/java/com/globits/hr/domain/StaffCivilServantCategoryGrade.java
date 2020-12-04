package com.globits.hr.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/**
 * 
 * @author dunghq
 * Bảng nối mã ngạch công chức với Staff nào đó
 *
 */
@XmlRootElement
@Table(name = "tbl_staff_civil_servant_grade")
@Entity
public class StaffCivilServantCategoryGrade extends BaseObject {
	private static final long serialVersionUID = 6014783475303579207L;
	
	@Column(name = "is_current")
	private Boolean isCurrent;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "civil_servant_category_grade_id")
	private CivilServantCategoryGrade civilServantCategoryGrade;

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public CivilServantCategoryGrade getCivilServantCategoryGrade() {
		return civilServantCategoryGrade;
	}

	public void setCivilServantCategoryGrade(CivilServantCategoryGrade civilServantCategoryGrade) {
		this.civilServantCategoryGrade = civilServantCategoryGrade;
	}
}
