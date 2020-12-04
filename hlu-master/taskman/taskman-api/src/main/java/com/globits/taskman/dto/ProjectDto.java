package com.globits.taskman.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.taskman.domain.Project;
import com.globits.taskman.domain.ProjectFileAttachment;
import com.globits.taskman.domain.ProjectTaskOwner;

public class ProjectDto extends BaseObjectDto{
	private String name;
	private String code;
	private List<ProjectTaskOwnerDto> members;
	private String description;
	private Double projectSize;//Kích thước dự án - tính theo số lượng man/month thực hiện
	private Double projectValue;//Giá trị dự án - Tổng tiền dự án
	private Set<ProjectFileAttachmentDto> attachments = new HashSet<ProjectFileAttachmentDto>();
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ProjectTaskOwnerDto> getMembers() {
		return members;
	}
	public void setMembers(List<ProjectTaskOwnerDto> members) {
		this.members = members;
	}
	public Double getProjectSize() {
		return projectSize;
	}
	public void setProjectSize(Double projectSize) {
		this.projectSize = projectSize;
	}
	public Double getProjectValue() {
		return projectValue;
	}
	public void setProjectValue(Double projectValue) {
		this.projectValue = projectValue;
	}
	public Set<ProjectFileAttachmentDto> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<ProjectFileAttachmentDto> attachments) {
		this.attachments = attachments;
	}
	public ProjectDto() {
		
	}
	
	public ProjectDto(Project entity) {
		this.name = entity.getName();
		this.code = entity.getCode();
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.projectSize = entity.getProjectSize();
		this.projectValue = entity.getProjectValue();
		if(entity.getMembers()!=null) {
			this.members = new ArrayList<ProjectTaskOwnerDto>();
			for(ProjectTaskOwner pto: entity.getMembers()) {
				ProjectTaskOwnerDto ptoDto = new ProjectTaskOwnerDto();
				ptoDto.setId(pto.getId());
				ptoDto.setTaskOwner(new TaskOwnerDto());
				if(pto.getMainRole()!=null) {
					ptoDto.setMainRole(new TaskRoleDto());
					ptoDto.getMainRole().setId(pto.getMainRole().getId());
					ptoDto.getMainRole().setCode(pto.getMainRole().getCode());
					ptoDto.getMainRole().setName(pto.getMainRole().getName());					
				}
				this.members.add(ptoDto);
				if(pto.getTaskOwner()!=null) {
					ptoDto.setTaskOwner(new TaskOwnerDto());
					ptoDto.getTaskOwner().setDisplayName(pto.getTaskOwner().getDisplayName());
					ptoDto.getTaskOwner().setId(pto.getTaskOwner().getId());
					ptoDto.getTaskOwner().setOwnerType(pto.getTaskOwner().getOwnerType());
				}
				
				if(pto.getMainRole()!=null) {
					ptoDto.setMainRole(new TaskRoleDto());
					ptoDto.getMainRole().setCode(pto.getMainRole().getCode());
					ptoDto.getMainRole().setName(pto.getMainRole().getName());
					ptoDto.getMainRole().setId(pto.getMainRole().getId());
				}
			}
		}
		
		if(entity.getAttachments()!=null && entity.getAttachments().size() > 0) {
			this.attachments = new HashSet<ProjectFileAttachmentDto>();
			for(ProjectFileAttachment a:entity.getAttachments()) {
				if(a!=null) {
					ProjectFileAttachmentDto aDto = new ProjectFileAttachmentDto();
					aDto.setId(a.getId());
					if(a.getFile()!=null) {
						aDto.setFile(new FileDescriptionDto());
						aDto.getFile().setContentSize(a.getFile().getContentSize());
						aDto.getFile().setContentType(a.getFile().getContentType());
						aDto.getFile().setExtension(a.getFile().getExtension());
						aDto.getFile().setFilePath(a.getFile().getFilePath());		
						aDto.getFile().setName(a.getFile().getName());
						aDto.getFile().setId(a.getFile().getId());
					}
					if (a.getProject() != null) {
						aDto.setProject(new ProjectDto());
						aDto.getProject().setId(a.getProject().getId());
						aDto.getProject().setCode(a.getProject().getCode());
						aDto.getProject().setName(a.getProject().getName());
					}
	
					this.attachments.add(aDto);
				}
			}
		}
		
	}
	
	
}
