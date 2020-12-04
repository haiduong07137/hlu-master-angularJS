package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
/*
 * Bảng này là bảng vị trí công tác của đơn vị
 * Trong vị trí công tác sẽ có PositionTitle (chức vụ)
 * Và có hệ số phụ cấp
 * 1 nhân sự có thể đảm nhận nhiều vị trí công tác khác nhau
 */
@XmlRootElement
@Table(name = "tbl_position")
@Entity
public class Position extends BaseObject {
	private static final long serialVersionUID = 1L;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private int status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "title_id")
	private PositionTitle title;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;
	
//	@Column(name = "title_id")
//	private Long titleId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

//	public Long getTitleId() {
//		return titleId;
//	}
//
//	public void setTitleId(Long titleId) {
//		this.titleId = titleId;
//	}

	public PositionTitle getTitle() {
		return title;
	}

	public void setTitle(PositionTitle title) {
		this.title = title;
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Position() {

	}

	public Position(Position position) {
		if(position!=null) {
			this.setId(position.getId());
			this.name = position.getName();
			this.status = position.getStatus();
			this.description = position.getDescription();
			this.title = position.getTitle();
			this.department = position.getDepartment();
		}
	}
}
