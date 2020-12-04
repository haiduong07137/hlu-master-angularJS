package com.globits.hr.dto;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.hr.domain.StaffLabourAgreement;

public class StaffLabourAgreementAttachmentDto extends BaseObjectDto implements Serializable {
	private static final long serialVersionUID = 1L;	
	private FileDescriptionDto file;	
	private StaffLabourAgreementDto staffLabourAgreement;
	public FileDescriptionDto getFile() {
		return file;
	}
	public void setFile(FileDescriptionDto file) {
		this.file = file;
	}
	public StaffLabourAgreementDto getStaffLabourAgreement() {
		return staffLabourAgreement;
	}
	public void setStaffLabourAgreement(StaffLabourAgreementDto staffLabourAgreement) {
		this.staffLabourAgreement = staffLabourAgreement;
	}
	public StaffLabourAgreementAttachmentDto() {
		
	}
}
