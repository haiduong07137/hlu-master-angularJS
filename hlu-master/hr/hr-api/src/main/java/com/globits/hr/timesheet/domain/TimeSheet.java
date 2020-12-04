package com.globits.hr.timesheet.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.globits.core.domain.BaseObject;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.WorkingStatus;
@XmlRootElement
@Table(name = "tbl_timesheet")
@Entity
public class TimeSheet extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	@Column(name="working_date")
	private Date workingDate;
	@Column(name ="total_hours")
	private double 	totalHours;//Tổng thời gian làm việc (không nhất thiết phải là giờ kết thúc - giờ bắt đầu vì còn vấn đề nghỉ giữa giờ, ra ngoài, ...)
	
	@Column(name="start_time")
	private Date startTime;//Thời điểm bắt đầu làm việc
	@Column(name="end_time")
	private Date endTime;//Thời điểm kết thúc công việc
	
	@ManyToOne (cascade= CascadeType.PERSIST)
	@JoinColumn(name="employee_id")
	private Staff employee;

	@ManyToOne (cascade= CascadeType.PERSIST)
	@JoinColumn(name="shift_work_id")
	private ShiftWork shiftWork;
	
	@OneToMany(mappedBy="timeSheet", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<TimeSheetDetail> details;
	
	@ManyToOne
	@JoinColumn(name="working_status_id")
	private WorkingStatus workingStatus;//Trạng thái thực hiện.
	
	@Column(name ="approve_status")
	private Integer approveStatus;//Trạng thái phê duyệt - 0 = chưa phê duyệt, 1 đã phê duyệt
	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public ShiftWork getShiftWork() {
		return shiftWork;
	}

	public void setShiftWork(ShiftWork shiftWork) {
		this.shiftWork = shiftWork;
	}

	public Staff getEmployee() {
		return employee;
	}

	public void setEmployee(Staff employee) {
		this.employee = employee;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public WorkingStatus getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(WorkingStatus workingStatus) {
		this.workingStatus = workingStatus;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Set<TimeSheetDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<TimeSheetDetail> details) {
		this.details = details;
	}

}
