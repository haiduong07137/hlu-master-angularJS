package com.globits.taskman.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.security.domain.User;
@Entity
@Table(name = "tbl_user_task_owner")
@XmlRootElement
public class UserTaskOwner extends BaseObject{

	private static final long serialVersionUID = -5535628402309639020L;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="task_owner_id")
	private TaskOwner taskOwner;
	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="role_id")
	private TaskRole role;
	public TaskOwner getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(TaskOwner taskOwner) {
		this.taskOwner = taskOwner;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public TaskRole getRole() {
		return role;
	}
	public void setRole(TaskRole role) {
		this.role = role;
	}
	
	
}
