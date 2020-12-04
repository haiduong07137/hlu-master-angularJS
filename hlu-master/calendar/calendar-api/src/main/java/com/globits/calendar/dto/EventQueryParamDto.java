package com.globits.calendar.dto;

import java.util.List;

import org.joda.time.DateTime;

public class EventQueryParamDto {

	private Boolean voided;
	private Integer scope=0;

	private Integer type=0;

	private Long[] departmentIds;

	private Long[] roomIds;

	private DateTime fromDate;

	private DateTime toDate;

	private boolean includePersonalEvents;

	private Integer status;
	
	private List<Integer> listStatus;
	
	public List<Integer> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<Integer> listStatus) {
		this.listStatus = listStatus;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(Long[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	public Long[] getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(Long[] roomIds) {
		this.roomIds = roomIds;
	}

	public DateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(DateTime fromDate) {
		this.fromDate = fromDate;
	}

	public DateTime getToDate() {
		return toDate;
	}

	public void setToDate(DateTime toDate) {
		this.toDate = toDate;
	}

	public boolean isIncludePersonalEvents() {
		return includePersonalEvents;
	}

	public void setIncludePersonalEvents(boolean includePersonalEvents) {
		this.includePersonalEvents = includePersonalEvents;
	}

	public Boolean getVoided() {
		return voided;
	}

	public void setVoided(Boolean voided) {
		this.voided = voided;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
