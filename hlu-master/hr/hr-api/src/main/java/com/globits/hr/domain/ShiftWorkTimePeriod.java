package com.globits.hr.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

@XmlRootElement
@Table(name = "tbl_shift_work_time_period")
@Entity
public class ShiftWorkTimePeriod extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6766395500871760165L;

	@ManyToOne
	@JoinColumn(name = "shift_work_id", nullable = false)
	private ShiftWork shiftWork;

	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "end_time")
	private Date endTime;

	public ShiftWork getShiftWork() {
		return shiftWork;
	}

	public void setShiftWork(ShiftWork shiftWork) {
		this.shiftWork = shiftWork;
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

}
