package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_salary_config_item")
@Entity
public class SalaryConfigItem extends BaseObject {

	private static final long serialVersionUID = 5044965188766036345L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "salary_config_id")
	private SalaryConfig salaryConfig;
	
	@Column(name="item_order")
	private int itemOrder;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "salary_item_id")
	private SalaryItem salaryItem;
	
	@Column(name = "formula")
	private String formula;
	
	public SalaryConfig getSalaryConfig() {
		return salaryConfig;
	}
	public void setSalaryConfig(SalaryConfig salaryConfig) {
		this.salaryConfig = salaryConfig;
	}
	public SalaryItem getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}
	public int getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	
}
