package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.hr.dto.SalaryItemDto;

/**
 * 
 * @author dunghq
 * Bảng thuộc tính lương của Staff
 *
 */
@XmlRootElement
@Table(name = "tbl_staff_salary_property")
@Entity
public class StaffSalaryProperty  extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2169442983827528358L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "staff_id")
	private Staff staff;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "salary_item_id")
	private SalaryItem salaryItem;
	@Column(name="value")
	private Double value;//Giá trị của thuộc tính lương tương ứng với salaryItem (ví dụ :SalaryItem là HSL thì giá trị có thể là 3.0)
	@Column(name="code")
	private String code;
	@Column(name="name")
	private String name;
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public SalaryItem getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
			
	}
	
}
