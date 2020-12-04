package com.globits.taskman.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

/*
 * Định nghĩa quy trình xử lý văn bản đơn giản
 * Mỗi Flow gồm 1 số step
 * Mỗi task sẽ có 1 Flow nào đó
 * 
 */
@Entity
@Table(name = "tbl_task_flow")
@XmlRootElement
public class TaskFlow extends BaseObject{
	private String name;
	private String code;
	@OneToMany(mappedBy = "flow", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("orderIndex ASC")
	private Set<TaskFlowStep> steps;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Set<TaskFlowStep> getSteps() {
		return steps;
	}
	public void setSteps(Set<TaskFlowStep> steps) {
		this.steps = steps;
	}
	
	
}
