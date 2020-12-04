package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
import com.globits.hr.domain.Staff;

@Entity
@Table(name = "tbl_staff_department_task_role")
@XmlRootElement
public class StaffDepartmentTaskRole extends BaseObject{

	private static final long serialVersionUID = 5267280269561290986L;
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;//Nhân viên nào
	@ManyToOne
	@JoinColumn(name="task_role_id")
	private TaskRole role;//Được xử lý việc gì
	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;//Cho phòng ban nào
	
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public TaskRole getRole() {
		return role;
	}
	public void setRole(TaskRole role) {
		this.role = role;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
}
