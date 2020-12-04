package com.globits.hr.timesheet.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

@XmlRootElement
@Table(name = "tbl_timesheet_detail")
@Entity
public class TimeSheetDetail extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	@Column(name="working_item_title")
	private String workingItemTitle;
	
	@ManyToOne
	@JoinColumn(name="timesheet_id")
	private TimeSheet timeSheet;
	@Column(name ="start_time")
	private Date startTime;
	@Column(name ="end_time")
	private Date endTime;
	@Column(name="hours")
	private double duration;
	
	public String getWorkingItemTitle() {
		return workingItemTitle;
	}
	public void setWorkingItemTitle(String workingItemTitle) {
		this.workingItemTitle = workingItemTitle;
	}
	public TimeSheet getTimeSheet() {
		return timeSheet;
	}
	public void setTimeSheet(TimeSheet timeSheet) {
		this.timeSheet = timeSheet;
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
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
}
