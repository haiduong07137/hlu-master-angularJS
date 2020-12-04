package com.globits.hr.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
/*
 * Bảng lịch sử chức vụ nhân viên
 */
@XmlRootElement
@Table(name = "tbl_position_staff")
@Entity
public class PositionStaff extends BaseObject {
	
	private static final long serialVersionUID = 5402903435794913458L;
	
	@Column(name = "from_date")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private DateTime fromDate;
	
	@Column(name = "to_date")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private DateTime toDate;

	@Column(name = "current_position")
	private Boolean current;
	
	@Column(name = "main_position")
	private Boolean mainPosition;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "staff_id")
	private Staff staff;

	@ManyToOne(fetch = FetchType.EAGER,optional=true)
	@JoinColumn(name = "position_id")
	private PositionTitle position;
	/*
	 * Vị trí cho đơn vị cụ thể nào đó - nếu chỉ có vị trí nhưng không thuộc đơn vị nào 
	 * thì trường này bằng null
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;

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

	public Boolean isCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public PositionTitle getPosition() {
		return position;
	}

	public void setPosition(PositionTitle position) {
		this.position = position;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Boolean getMainPosition() {
		return mainPosition;
	}

	public void setMainPosition(Boolean mainPosition) {
		this.mainPosition = mainPosition;
	}

	public Boolean getCurrent() {
		return current;
	}

	public PositionStaff() {

	}

	public PositionStaff(PositionStaff ps) {
		this.setId(ps.getId());
		this.fromDate = ps.getFromDate();
		this.toDate = ps.getToDate();
		this.current = ps.getCurrent();
		this.mainPosition = ps.getMainPosition();
		this.staff = new Staff(ps.getStaff().getId(), ps.getStaff().getFirstName(), ps.getStaff().getLastName(),
				ps.getStaff().getStaffCode(), ps.getStaff().getDisplayName());
		this.position = ps.getPosition();
		if (ps.getDepartment() != null)
			this.department = new Department(ps.getDepartment().getId(), ps.getDepartment().getName(),
					ps.getDepartment().getCode(), ps.getDepartment().getDepartmentType());
	}
}
