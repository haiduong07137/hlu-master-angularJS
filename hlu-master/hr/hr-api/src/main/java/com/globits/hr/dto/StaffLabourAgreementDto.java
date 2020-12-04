package com.globits.hr.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.domain.StaffLabourAgreementAttachment;

public class StaffLabourAgreementDto {
	private Long id;
	private StaffDto staff;
	private Date startDate;
	private Date endDate;
	private Date signedDate;
	private Integer agreementStatus;
	private LabourAgreementTypeDto labourAgreementType; 
	private List<StaffLabourAgreementAttachmentDto> attachments = new ArrayList<StaffLabourAgreementAttachmentDto>();
	

	public StaffDto getStaff() {
		return staff;
	}

	public void setStaff(StaffDto staff) {
		this.staff = staff;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LabourAgreementTypeDto getLabourAgreementType() {
		return labourAgreementType;
	}

	public void setLabourAgreementType(LabourAgreementTypeDto labourAgreementType) {
		this.labourAgreementType = labourAgreementType;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<StaffLabourAgreementAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<StaffLabourAgreementAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public StaffLabourAgreementDto() {

	}

	public StaffLabourAgreementDto(StaffLabourAgreement agreement) {
		
		if (agreement == null) {
			return;
		}
		
		this.id = agreement.getId();
		this.startDate = agreement.getStartDate();
		this.endDate = agreement.getEndDate();
		this.signedDate = agreement.getSignedDate();
		this.agreementStatus = agreement.getAgreementStatus();
		
		if(agreement.getStaff() != null) {
			this.staff = new StaffDto();
			this.staff.setId(agreement.getStaff().getId());
			this.staff.setStaffCode(agreement.getStaff().getStaffCode());
		}
		
		if(agreement.getLabourAgreementType() != null)
		{
			this.labourAgreementType = new LabourAgreementTypeDto();
			this.labourAgreementType.setId(agreement.getLabourAgreementType().getId());
			this.labourAgreementType.setName(agreement.getLabourAgreementType().getName());
			//labourAgreementType = new LabourAgreementTypeDto(agreement.getLabourAgreementType());			
		}
		
	}

}
