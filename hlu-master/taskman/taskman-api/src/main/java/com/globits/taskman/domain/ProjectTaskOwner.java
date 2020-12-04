package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name = "tbl_task_project_owner")
@XmlRootElement
public class ProjectTaskOwner extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;
	@ManyToOne
	@JoinColumn(name="task_owner_id")
	private TaskOwner taskOwner;
	@ManyToOne
	@JoinColumn(name="main_role_id")
	private TaskRole mainRole;
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public TaskOwner getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(TaskOwner taskOwner) {
		this.taskOwner = taskOwner;
	}
	public TaskRole getMainRole() {
		return mainRole;
	}
	public void setMainRole(TaskRole mainRole) {
		this.mainRole = mainRole;
	}

}
