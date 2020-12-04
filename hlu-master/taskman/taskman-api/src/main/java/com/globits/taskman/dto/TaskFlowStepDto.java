package com.globits.taskman.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.taskman.domain.TaskFlowStep;

public class TaskFlowStepDto extends BaseObjectDto{
	private TaskFlowDto flow;
	private TaskStepDto step;
	private Boolean isMandatory;
	private int orderIndex;

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

	public TaskFlowDto getFlow() {
		return flow;
	}

	public void setFlow(TaskFlowDto flow) {
		this.flow = flow;
	}

	public TaskStepDto getStep() {
		return step;
	}

	public void setStep(TaskStepDto step) {
		this.step = step;
	}

	public TaskFlowStepDto() {
		
	}
	
	public TaskFlowStepDto(TaskFlowStep taskFlowStep) {
		if(taskFlowStep!=null) {
			this.setId(taskFlowStep.getId());
			this.isMandatory = taskFlowStep.getIsMandatory();
			this.orderIndex = taskFlowStep.getOrderIndex();
			
			if(taskFlowStep.getStep()!=null) {
				TaskStepDto taskStepDto = new TaskStepDto();
				taskStepDto.setId(taskFlowStep.getStep().getId());
				taskStepDto.setName(taskFlowStep.getStep().getName());
				taskStepDto.setCode(taskFlowStep.getStep().getCode());
				this.step= taskStepDto;
			}
			
			if(taskFlowStep.getFlow()!=null) {
				TaskFlowDto taskFlowDto = new TaskFlowDto();
				taskFlowDto.setId(taskFlowStep.getFlow().getId());
				taskFlowDto.setName(taskFlowStep.getFlow().getName());
				taskFlowDto.setCode(taskFlowStep.getFlow().getCode());
				this.flow = taskFlowDto;
			}
		}
	}
}
