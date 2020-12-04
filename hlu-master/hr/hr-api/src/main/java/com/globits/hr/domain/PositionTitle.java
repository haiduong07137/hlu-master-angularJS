package com.globits.hr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/**
 * 
 * @author dunghq
 * Ví dụ : Hiệu trưởng, hiệu phó,....
 */
@XmlRootElement
@Table(name = "tbl_position_title")
@Entity
public class PositionTitle extends BaseObject {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "position_coefficient")
	private Double positionCoefficient;//hệ số phụ cấp chức vụ
	
	
	@Column(name = "type") //1= Chính quyền; 2= Đoàn thể
	private Integer type;
	
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
	
	
	public Double getPositionCoefficient() {
		return positionCoefficient;
	}
	public void setPositionCoefficient(Double positionCoefficient) {
		this.positionCoefficient = positionCoefficient;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public PositionTitle() {
		
	}
	public PositionTitle(PositionTitle title) {
		this.code = title.getCode();
		this.name = title.getName();
		this.type = title.getType();
		this.description = title.getDescription();
	}
}
