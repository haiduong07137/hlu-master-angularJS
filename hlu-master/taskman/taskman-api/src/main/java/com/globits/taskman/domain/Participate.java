package com.globits.taskman.domain;

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

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
import com.globits.hr.domain.Staff;
@Entity
@Table(name = "tbl_task_participate")
@XmlRootElement
public class Participate extends BaseObject {
	private static final long serialVersionUID = 1L;
	@Column(name="display_name")
	private String displayName;
	@ManyToOne
	@JoinColumn(name="department_id")
	private Department department;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="employee_id")
	private Staff employee;
	

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="task_owner_id")
	private TaskOwner taskOwner;
	@Column(name="participate_type")
	private int participateType;//type=0 => phòng ban, type = 1 => cá nhân,   type =2 => khác, type=3=> cá nhân- xử lý chính
	
	@Column(name="current_state")
	private Integer currentState;//Trạng thái hiện thời : 0 = chưa bắt đầu, 1 = đang xử lý, 2= đã hoàn thành.
	
	@ManyToOne
	@JoinColumn(name="task_id")
	private Task task;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private TaskRole role;//Vai trò của người này hay đơn vị này trong công việc
	
	@Column(name="number_hours")
	private Double numberHours;//Tổng thời gian tham gia cho việc này của TaskOwner này
//, orphanRemoval = true
	@OneToMany(mappedBy = "participate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<TaskComment> comments;
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Staff getEmployee() {
		return employee;
	}
	public void setEmployee(Staff employee) {
		this.employee = employee;
	}
	public int getParticipateType() {
		return participateType;
	}
	public void setParticipateType(int participateType) {
		this.participateType = participateType;
	}
	
	
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public TaskRole getRole() {
		return role;
	}
	public void setRole(TaskRole role) {
		this.role = role;
	}
	public Set<TaskComment> getComments() {
		return comments;
	}
	public void setComments(Set<TaskComment> comments) {
		this.comments = comments;
	}
	public Integer getCurrentState() {
		return currentState;
	}
	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}
	public TaskOwner getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(TaskOwner taskOwner) {
		this.taskOwner = taskOwner;
	}
	public Double getNumberHours() {
		return numberHours;
	}
	public void setNumberHours(Double numberHours) {
		this.numberHours = numberHours;
	}
	
}
