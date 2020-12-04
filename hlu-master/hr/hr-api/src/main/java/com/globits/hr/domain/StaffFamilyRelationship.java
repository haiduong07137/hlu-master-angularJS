package com.globits.hr.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

@XmlRootElement
@Table(name = "tbl_staff_family_relationship")
@Entity
public class StaffFamilyRelationship extends BaseObject {
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="birth_date")
	private Date birthDate;
	
	@Column(name="profession")
	private String profession;
	
	@Column(name="address")
	private String address;

	@Column(name="description")
	private String description;
	
	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
