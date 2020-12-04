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
import com.globits.core.domain.Person;
@Entity
@Table(name = "tbl_task_owner")
@XmlRootElement
public class TaskOwner extends BaseObject{

	private static final long serialVersionUID = 1L;
	@Column(name="display_name")
	private String displayName;
	@Column(name="owner_type")
	private Integer ownerType;//0= phòng ban, 1= cá nhân, other type = 2 - dùng TaskOwnerTypeEnum để lấy giá trị
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, optional=true)
	@JoinColumn(name="department_id")
	private Department department;//Trong trường hợp là phòng ban tham gia xử ly
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, optional=true)
	@JoinColumn(name="person_id")
	private Person person;//Trong trường hợp là người cụ thể tham gia
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, optional=true)
	@JoinColumn(name="parent_id")
	private TaskOwner parent;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<TaskOwner> subTaskOwner;
	
	@OneToMany(mappedBy="taskOwner", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<UserTaskOwner> userTaskOwners;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}
	
	
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public TaskOwner getParent() {
		return parent;
	}
	public void setParent(TaskOwner parent) {
		this.parent = parent;
	}
	public Set<UserTaskOwner> getUserTaskOwners() {
		return userTaskOwners;
	}
	public void setUserTaskOwners(Set<UserTaskOwner> userTaskOwners) {
		this.userTaskOwners = userTaskOwners;
	}
	public Set<TaskOwner> getSubTaskOwner() {
		return subTaskOwner;
	}
	public void setSubTaskOwner(Set<TaskOwner> subTaskOwner) {
		this.subTaskOwner = subTaskOwner;
	}
	
	
}
