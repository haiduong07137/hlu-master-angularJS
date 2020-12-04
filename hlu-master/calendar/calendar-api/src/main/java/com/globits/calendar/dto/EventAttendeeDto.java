package com.globits.calendar.dto;

import com.globits.calendar.domain.EventAttendee;
import com.globits.core.dto.AuditableEntityDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.hr.dto.StaffDto;

public class EventAttendeeDto extends AuditableEntityDto {

	private Long id;

	private String displayName;

	private Boolean isChairPerson;

	private Boolean isOptional;

	private String email;

	private Integer attendeeType;

	private Integer visibility;

	private Long eventId;

	private StaffDto staff;

	private DepartmentDto department;

	public EventAttendeeDto() {

	}

	public EventAttendeeDto(EventAttendee entity) {

		if (entity == null) {
			return;
		}

		this.id = entity.getId();
		this.displayName = entity.getDisplayName();
		this.isChairPerson = entity.getIsChairPerson();
		this.isOptional = entity.getIsOptional();
		this.email = entity.getEmail();
		this.attendeeType = entity.getAttendeeType();
		this.visibility = entity.getVisibility();

		if (entity.getEvent() != null) {
			this.eventId = entity.getEvent().getId();
		}

		if (entity.getStaff() != null) {
			this.staff = new StaffDto(entity.getStaff());
		}

		if (entity.getDepartment() != null) {
			this.department = new DepartmentDto(entity.getDepartment());
		}

		setCreateDate(entity.getCreateDate());
		setCreatedBy(entity.getCreatedBy());
		setModifyDate(entity.getModifyDate());
		setModifiedBy(entity.getModifiedBy());
	}

	public EventAttendee toEntity() {
		EventAttendee entity = new EventAttendee();

		entity.setId(id);
		entity.setDisplayName(displayName);
		entity.setIsChairPerson(isChairPerson);
		entity.setIsOptional(isOptional);
		entity.setEmail(email);
		entity.setAttendeeType(attendeeType);
		entity.setVisibility(visibility);

		if (staff != null) {
			entity.setStaff(staff.toEntity());
		}

		if (department != null) {
			entity.setDepartment(department.toEntity());
		}

		entity.setCreateDate(getCreateDate());
		entity.setCreatedBy(getCreatedBy());

		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsChairPerson() {
		return isChairPerson;
	}

	public void setIsChairPerson(Boolean isChairPerson) {
		this.isChairPerson = isChairPerson;
	}

	public Boolean getIsOptional() {
		return isOptional;
	}

	public void setIsOptional(Boolean isOptional) {
		this.isOptional = isOptional;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAttendeeType() {
		return attendeeType;
	}

	public void setAttendeeType(Integer attendeeType) {
		this.attendeeType = attendeeType;
	}

	public Integer getVisibility() {
		return visibility;
	}

	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public StaffDto getStaff() {
		return staff;
	}

	public void setStaff(StaffDto staff) {
		this.staff = staff;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}

}
