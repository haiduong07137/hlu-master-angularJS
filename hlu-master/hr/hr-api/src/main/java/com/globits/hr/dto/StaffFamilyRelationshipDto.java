package com.globits.hr.dto;

import java.util.Date;

import javax.persistence.Column;

import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffFamilyRelationship;

public class StaffFamilyRelationshipDto {

	private Long id;
	private Staff staff;
	private String fullName;
	private Date birthDate;
	private String profession;
	private String address;
	private String description;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public StaffFamilyRelationshipDto() {
		super();
	}

	public StaffFamilyRelationshipDto(StaffFamilyRelationship familyRelationship) {
		super();
		this.id = familyRelationship.getId();
		this.staff = new Staff(familyRelationship.getStaff().getId(),familyRelationship.getStaff().getFirstName(), familyRelationship.getStaff().getLastName(), familyRelationship.getStaff().getStaffCode(), familyRelationship.getStaff().getDisplayName());
		this.fullName = familyRelationship.getFullName();
		this.birthDate = familyRelationship.getBirthDate();
		this.profession = familyRelationship.getProfession();
		this.address = familyRelationship.getAddress();
		this.description = familyRelationship.getDescription();
	}

	
}
