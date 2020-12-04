package com.globits.hr.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
@XmlRootElement
@Table(name = "tbl_salary_config")
@Entity
public class SalaryConfig extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7438693553216933636L;
	private String name;
	private String code;
	
	@ManyToOne(fetch = FetchType.EAGER, optional=true)
	@JoinColumn(name = "department_id")
	private Department department;
	
	@OneToMany(mappedBy = "salaryConfig", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SalaryConfigItem> salaryConfigItems = new HashSet<SalaryConfigItem>();

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
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Set<SalaryConfigItem> getSalaryConfigItems() {
		return salaryConfigItems;
	}
	public void setSalaryConfigItems(Set<SalaryConfigItem> salaryConfigItems) {
		this.salaryConfigItems = salaryConfigItems;
	}
	
}
