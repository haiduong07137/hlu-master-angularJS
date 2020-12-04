package com.globits.taskman.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

/*
 * Định nghĩa một step trong 1 Flow
 * Có thể có những bước có thể bỏ qua, có những bước không thể bỏ qua
 */
@Entity
@Table(name = "tbl_task_flow_step")
@XmlRootElement
public class TaskFlowStep  extends BaseObject{
	@ManyToOne
	@JoinColumn(name="flow_id")
	private TaskFlow flow;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="step_id")
	private TaskStep step;
	
	private Boolean isMandatory;
	private int orderIndex;
	public TaskFlow getFlow() {
		return flow;
	}
	public void setFlow(TaskFlow flow) {
		this.flow = flow;
	}
	public TaskStep getStep() {
		return step;
	}
	public void setStep(TaskStep step) {
		this.step = step;
	}
	public Boolean getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public int getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	
}
