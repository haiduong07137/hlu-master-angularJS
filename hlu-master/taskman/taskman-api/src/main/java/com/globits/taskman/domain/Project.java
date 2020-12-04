package com.globits.taskman.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name = "tbl_task_project")
@XmlRootElement
public class Project extends BaseObject{
	private String name;
	private String code;
	@Column(name="description")
	private String description;
	
	@Column(name="project_size")
	private Double projectSize;//Kích thước dự án - tính theo số lượng man/month thực hiện
	
	@Column(name="project_value")
	private Double projectValue;//Giá trị dự án - Tổng tiền dự án
	
	@OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true)	
	private Set<ProjectTaskOwner> members;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ProjectFileAttachment> attachments = new HashSet<ProjectFileAttachment>();
	
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
	public Set<ProjectTaskOwner> getMembers() {
		return members;
	}
	public void setMembers(Set<ProjectTaskOwner> members) {
		this.members = members;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Set<ProjectFileAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<ProjectFileAttachment> attachments) {
		this.attachments = attachments;
	}
	
	
}
