package com.globits.hr.dto;

import org.joda.time.DateTime;

import com.globits.core.domain.Department;
import com.globits.core.dto.DepartmentDto;
import com.globits.hr.domain.Position;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.domain.Staff;

public class PositionStaffDto {

	private Long id;

	private DateTime fromDate;

	private DateTime toDate;

	private boolean current;

	private StaffDto staff;

	private PositionTitleDto position;

	private DepartmentDto department;
	
	private Boolean mainPosition;

	public PositionStaffDto() {

	}

	public PositionStaffDto(PositionStaff entity) {

		if (entity == null) {
			return;
		}

		this.id = entity.getId();
		this.fromDate = entity.getFromDate();
		this.toDate = entity.getToDate();
		if(entity.getCurrent() == null) {
			this.current = false;
		}else {
			this.current = entity.getCurrent();
		}
		this.mainPosition = entity.getMainPosition();

		if (entity.getDepartment() != null) {
			department = new DepartmentDto();
			department.setId(entity.getDepartment().getId());
			department.setCode(entity.getDepartment().getCode());
			department.setName(entity.getDepartment().getName());
			department.setDepartmentType(entity.getDepartment().getDepartmentType());
		}

		if (entity.getPosition() != null) {
			position = new PositionTitleDto();
			// p.setId(ps.getPosition().getId());
			position.setDescription(entity.getPosition().getDescription());
			position.setName(entity.getPosition().getName());
			//position.setStatus(entity.getPosition());

		}

		if (entity.getStaff() != null) {
			staff = new StaffDto();
			staff.setId(entity.getStaff().getId());
			staff.setDisplayName(entity.getStaff().getDisplayName());
			staff.setBirthDate(entity.getStaff().getBirthDate());
			staff.setBirthPlace(entity.getStaff().getBirthPlace());
			staff.setEmail(entity.getStaff().getEmail());
			staff.setEndDate(entity.getStaff().getEndDate());
			staff.setFirstName(entity.getStaff().getFirstName());
			staff.setGender(entity.getStaff().getGender());
			staff.setId(entity.getStaff().getId());
			staff.setIdNumber(entity.getStaff().getIdNumber());
			staff.setIdNumberIssueBy(entity.getStaff().getIdNumberIssueBy());
			staff.setIdNumberIssueDate(entity.getStaff().getIdNumberIssueDate());
			staff.setPhoto(entity.getStaff().getPhoto());
			staff.setLastName(entity.getStaff().getLastName());
			staff.setPhoneNumber(entity.getStaff().getPhoneNumber());
		}
	}

	public PositionStaff toEntity() {
		PositionStaff entity = new PositionStaff();

		entity.setId(id);
		entity.setFromDate(fromDate);
		entity.setToDate(toDate);
		entity.setCurrent(current);

		if (department != null) {
			Department d = new Department();
			d.setId(department.getId());
			d.setCode(department.getCode());
			d.setName(department.getName());
			d.setDepartmentType(department.getDepartmentType());

			entity.setDepartment(d);
		}

		if (position != null) {
			PositionTitle p = new PositionTitle();
			// p.setId(ps.getPosition().getId());
			p.setDescription(position.getDescription());
			p.setName(position.getName());
			//p.setStatus(position.getStatus());

			entity.setPosition(p);
		}

		if (entity.getStaff() != null) {
			Staff s = new Staff();
			s.setId(staff.getId());
			s.setDisplayName(staff.getDisplayName());
			s.setBirthDate(staff.getBirthDate());
			s.setBirthPlace(staff.getBirthPlace());
			s.setEmail(staff.getEmail());
			s.setEndDate(staff.getEndDate());
			s.setFirstName(staff.getFirstName());
			s.setGender(staff.getGender());
			s.setId(staff.getId());
			s.setIdNumber(staff.getIdNumber());
			s.setIdNumberIssueBy(staff.getIdNumberIssueBy());
			s.setIdNumberIssueDate(staff.getIdNumberIssueDate());
			s.setPhoto(staff.getPhoto());
			s.setLastName(staff.getLastName());
			s.setPhoneNumber(staff.getPhoneNumber());

			entity.setStaff(s);
		}

		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public StaffDto getStaff() {
		return staff;
	}

	public void setStaff(StaffDto staff) {
		this.staff = staff;
	}

	public PositionTitleDto getPosition() {
		return position;
	}

	public void setPosition(PositionTitleDto position) {
		this.position = position;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}

	public Boolean getMainPosition() {
		return mainPosition;
	}

	public void setMainPosition(Boolean mainPosition) {
		this.mainPosition = mainPosition;
	}

}
