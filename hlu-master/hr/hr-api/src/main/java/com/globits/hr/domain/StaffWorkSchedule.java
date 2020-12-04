package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.globits.core.domain.BaseObject;
/*
 *Bảng phân công công việc, nhân viên nào sẽ làm việc vào giờ nào 
 */
@XmlRootElement
@Table(name = "tbl_staff_work_schedule")
@Entity
public class StaffWorkSchedule  extends BaseObject {
	private static final long serialVersionUID = 572369945947940265L;
	@ManyToOne
	@JoinColumn(name="shif_work_id")
	private ShiftWork shiftWork;
	
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
	@Column(name = "working_date", nullable = true)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime workingDate;

	public ShiftWork getShiftWork() {
		return shiftWork;
	}

	public void setShiftWork(ShiftWork shiftWork) {
		this.shiftWork = shiftWork;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public LocalDateTime getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(LocalDateTime workingDate) {
		this.workingDate = workingDate;
	}

	
}
