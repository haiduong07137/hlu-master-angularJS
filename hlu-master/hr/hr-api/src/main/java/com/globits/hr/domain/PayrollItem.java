package com.globits.hr.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_payroll_period")
@Entity
public class PayrollItem extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7732796437679271913L;
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
