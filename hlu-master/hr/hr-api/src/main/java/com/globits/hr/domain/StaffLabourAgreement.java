package com.globits.hr.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import com.globits.core.domain.BaseObject;
/*
 * Hợp đồng lao động với nhân viên
 */
@XmlRootElement
@Table(name = "tbl_staff_labour_agreement")
@Entity
public class StaffLabourAgreement extends BaseObject {
	private static final long serialVersionUID = 1L;
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="staff_id")
	private Staff staff;
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="labour_agreement_type_id")
	private LabourAgreementType labourAgreementType;

	@OneToMany(mappedBy = "staffLabourAgreement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<StaffLabourAgreementAttachment> attachments = new HashSet<StaffLabourAgreementAttachment>();
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="signed_date")
	private Date signedDate;//Ngày ký
	
	@Column(name="agreement_status")
	private Integer agreementStatus;//Trạng thái - sẽ cho vào 1 Enum (1= hiện thời, -1 đã kết thúc, -2 đã chấm dứt hợp đồng, ...)

	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public LabourAgreementType getLabourAgreementType() {
		return labourAgreementType;
	}
	public void setLabourAgreementType(LabourAgreementType labourAgreementType) {
		this.labourAgreementType = labourAgreementType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}
	public Integer getAgreementStatus() {
		return agreementStatus;
	}
	public void setAgreementStatus(Integer agreementStatus) {
		this.agreementStatus = agreementStatus;
	}
	public Set<StaffLabourAgreementAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<StaffLabourAgreementAttachment> attachments) {
		this.attachments = attachments;
	}
	public StaffLabourAgreement() {
		this.setUuidKey(UUID.randomUUID());
	}	
}
