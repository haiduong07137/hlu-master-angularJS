package com.globits.hr.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;
@Entity
@Table(name = "tbl_staff_labour_agreement_attachment")
public class StaffLabourAgreementAttachment extends BaseObject{
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	@ManyToOne
	@JoinColumn(name="agreement_id")
	private StaffLabourAgreement staffLabourAgreement;
	public FileDescription getFile() {
		return file;
	}
	public void setFile(FileDescription file) {
		this.file = file;
	}
	public StaffLabourAgreement getStaffLabourAgreement() {
		return staffLabourAgreement;
	}
	public void setStaffLabourAgreement(StaffLabourAgreement staffLabourAgreement) {
		this.staffLabourAgreement = staffLabourAgreement;
	}
}
