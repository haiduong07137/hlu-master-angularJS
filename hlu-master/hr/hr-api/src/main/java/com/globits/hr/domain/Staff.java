package com.globits.hr.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.Person;

@Entity
@Table(name = "tbl_staff")
@PrimaryKeyJoinColumn(name = "id", foreignKey = @ForeignKey(name = "staff_person", value = ConstraintMode.NO_CONSTRAINT))
@DiscriminatorValue("1")
@XmlRootElement
public class Staff extends Person {
	private static final long serialVersionUID = 6014783475303579207L;

	@Column(name = "staff_code", nullable = true, unique = true)
	private String staffCode;

	@Column(name = "current_working_status", nullable = true)
	private Integer currentWorkingStatus;

	@Column(name = "social_insurance_number", nullable = true)
	private String socialInsuranceNumber;//Số sổ bảo hiểm xã hội
	@OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PositionStaff> positions = new HashSet<PositionStaff>();
	
	@OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<StaffLabourAgreement> agreements;//Hợp đồng
	
	public Integer getCurrentWorkingStatus() {
		return currentWorkingStatus;
	}

	public void setCurrentWorkingStatus(Integer currentWorkingStatus) {
		this.currentWorkingStatus = currentWorkingStatus;
	}
	public String getSocialInsuranceNumber() {
		return socialInsuranceNumber;
	}

	public void setSocialInsuranceNumber(String socialInsuranceNumber) {
		this.socialInsuranceNumber = socialInsuranceNumber;
	}
	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public Set<PositionStaff> getPositions() {
		return positions;
	}

	public void setPositions(Set<PositionStaff> positions) {
		this.positions = positions;
	}



	public Set<StaffLabourAgreement> getAgreements() {
		return agreements;
	}

	public void setAgreements(Set<StaffLabourAgreement> agreements) {
		this.agreements = agreements;
	}

	public Staff() {

	}

	public Staff(Long id, String firstName, String lastName, String staffCode, String displayName) {
		this.setId(id);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setStaffCode(staffCode);
		this.setDisplayName(displayName);
	}

	public Staff(Staff staff) {
		super(staff);
		this.staffCode = staff.getStaffCode();
		// if(staff.getPositions()!=null && staff.getPositions().size()>0){
		// this.positions = staff.getPositions();
		// }
	}

	// @Column(name="last_contract_date")
	// private Date lastContractDate;
	// public Date getLastContractDate() {
	// return lastContractDate;
	// }
	// public void setLastContractDate(Date lastContractDate) {
	// this.lastContractDate = lastContractDate;
	// }

}
