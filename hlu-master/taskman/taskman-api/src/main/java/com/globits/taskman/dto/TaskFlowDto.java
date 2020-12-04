package com.globits.taskman.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.domain.TaskFlowStep;


public class TaskFlowDto extends BaseObjectDto{
	private String name;
	private String code;
	private List<TaskFlowStepDto> steps;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public List<TaskFlowStepDto> getSteps() {
		return steps;
	}
	public void setSteps(List<TaskFlowStepDto> steps) {
		this.steps = steps;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public TaskFlowDto() {
		
	}
	
	public TaskFlowDto(TaskFlow taskFlow) {
		if(taskFlow!=null) {
			this.setId(taskFlow.getId());
			if(taskFlow.getSteps()!=null) {
				this.steps = new ArrayList<TaskFlowStepDto>();
				for(TaskFlowStep taskFlowStep:taskFlow.getSteps()) {
					TaskFlowStepDto taskFlowStepDto = new TaskFlowStepDto();
					taskFlowStepDto.setId(taskFlowStep.getId());
					if(taskFlowStep.getFlow()!=null) {
						TaskFlowDto taskFlowDto = new TaskFlowDto();
						taskFlowDto.setId(taskFlowStep.getFlow().getId());
						taskFlowDto.setCode(taskFlowStep.getFlow().getCode());
						taskFlowDto.setName(taskFlowStep.getFlow().getName());
						taskFlowStepDto.setFlow(taskFlowDto);
					}
					if(taskFlowStep.getStep()!=null) {
						TaskStepDto taskStepDto = new TaskStepDto();
						taskStepDto.setId(taskFlowStep.getStep().getId());
						taskStepDto.setCode(taskFlowStep.getStep().getCode());
						taskStepDto.setName(taskFlowStep.getStep().getName());
						taskFlowStepDto.setStep(taskStepDto);
					}
					taskFlowStepDto.setOrderIndex(taskFlowStep.getOrderIndex());
					this.steps.add(taskFlowStepDto);
				}
				Collections.sort(this.steps, new Comparator<TaskFlowStepDto>() {
				    @Override
				    public int compare(TaskFlowStepDto lhs, TaskFlowStepDto rhs) {
				        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				        return lhs.getOrderIndex() < rhs.getOrderIndex() ? -1 : (lhs.getOrderIndex() > rhs.getOrderIndex()) ? 1 : 0;
				    }
				});
			}
			

			this.name = taskFlow.getName();
			this.code = taskFlow.getCode();
		}
	}
}
