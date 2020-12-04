package com.globits.calendar.dto;

import java.io.Serializable;

public class CalendarPermissionDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Boolean hasCalendarAdminPermission = false;
	private Boolean hasCalendarPublisherPermission = false;
	private Boolean hasCalendarApproverPermission = false;
	private Boolean hasCalendarEditorPermission = false;
	public Boolean getHasCalendarAdminPermission() {
		return hasCalendarAdminPermission;
	}
	public void setHasCalendarAdminPermission(Boolean hasCalendarAdminPermission) {
		this.hasCalendarAdminPermission = hasCalendarAdminPermission;
	}
	public Boolean getHasCalendarPublisherPermission() {
		return hasCalendarPublisherPermission;
	}
	public void setHasCalendarPublisherPermission(Boolean hasCalendarPublisherPermission) {
		this.hasCalendarPublisherPermission = hasCalendarPublisherPermission;
	}
	public Boolean getHasCalendarEditorPermission() {
		return hasCalendarEditorPermission;
	}
	public void setHasCalendarEditorPermission(Boolean hasCalendarEditorPermission) {
		this.hasCalendarEditorPermission = hasCalendarEditorPermission;
	}
	public Boolean getHasCalendarApproverPermission() {
		return hasCalendarApproverPermission;
	}
	public void setHasCalendarApproverPermission(Boolean hasCalendarApproverPermission) {
		this.hasCalendarApproverPermission = hasCalendarApproverPermission;
	}
	
	
}
