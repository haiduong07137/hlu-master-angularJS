package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/*
 * Độ ưu tiên của công việc
 */
@Entity
@Table(name = "tbl_task_priority")
@XmlRootElement
public class TaskPriority extends BaseObject{
	private static final long serialVersionUID = -6696184729457289821L;
	private String name;
	private String code;
	private int priorityOrder;
	
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
	public int getPriorityOrder() {
		return priorityOrder;
	}
	public void setPriorityOrder(int priorityOrder) {
		this.priorityOrder = priorityOrder;
	}
}
