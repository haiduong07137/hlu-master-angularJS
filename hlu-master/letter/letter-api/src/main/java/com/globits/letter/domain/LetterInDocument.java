package com.globits.letter.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.globits.core.domain.Organization;
import com.globits.letter.LetterConstant;
import com.globits.taskman.domain.TaskOwner;

@Entity
@DiscriminatorValue(value = LetterConstant.LetterInDocumentType)
public class LetterInDocument extends LetterDocument{
	private static final long serialVersionUID = 1L;
	
	@Column(name="delivered_date")
	private Date deliveredDate;//Ngày chuyển đến - Ngày đến
	
	@Column(name="issued_date")
	private Date issuedDate;//Ngày ban hành
	
	@Column(name="official_date")
	private Date officialDate;//Ngày hiệu lực
	
	@Column(name="expired_date")
	private Date expiredDate;//Ngày hết hạn
	
	@Column(name="send_email")
	private String sendEmail;//gửi thông báo email

	@Column(name="is_other_issue_organ")
	private Boolean isOtherIssueOrgan; // Nếu là cơ quan ban hành khác (nhập tay)

	@Column(name="other_issue_organ")
	private String otherIssueOrgan; // cơ quan ban hành khác (nhập tay)
	
	@Column(name="send_sms")
	private String sendSMS;//gửi thông báo SMS
	
	@ManyToOne
	@JoinColumn(name="chief_of_staff")
	private TaskOwner chiefOfStaff;//Chánh văn phòng

	@ManyToOne
	@JoinColumn(name="letter_doc_book_id")
	private LetterDocBook letterDocBook;	//Sổ văn bản

	@ManyToOne
	@JoinColumn(name="letter_doc_book_group_id")
	private LetterDocBookGroup letterDocBookGroup;	//Nhóm sổ văn bản
	
	/* Đường công văn
	 * 1: Bưu điện
	 * 2: Chuyển trực tiếp
	 * 3: Fax
	 * 4: Email
	 */
	@Column(name="letter_line")
	private Integer letterLine;//Đường công văn
	
	/* Loại lưu văn bản
	 * 1: Bản gốc
	 * 2: Bản sao chép
	 * 3: Bản mềm
	 */
	@Column(name="save_letter_type")
	private Integer saveLetterType;//Đường công văn
	
	public TaskOwner getChiefOfStaff() {
		return chiefOfStaff;
	}
	public void setChiefOfStaff(TaskOwner chiefOfStaff) {
		this.chiefOfStaff = chiefOfStaff;
	}
	public Date getDeliveredDate() {
		return deliveredDate;
	}
	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public Date getOfficialDate() {
		return officialDate;
	}
	public void setOfficialDate(Date officialDate) {
		this.officialDate = officialDate;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Integer getLetterLine() {
		return letterLine;
	}
	public void setLetterLine(Integer letterLine) {
		this.letterLine = letterLine;
	}
	public Integer getSaveLetterType() {
		return saveLetterType;
	}
	public void setSaveLetterType(Integer saveLetterType) {
		this.saveLetterType = saveLetterType;
	}
	public LetterDocBook getLetterDocBook() {
		return letterDocBook;
	}
	public void setLetterDocBook(LetterDocBook letterDocBook) {
		this.letterDocBook = letterDocBook;
	}
	public LetterDocBookGroup getLetterDocBookGroup() {
		return letterDocBookGroup;
	}
	public void setLetterDocBookGroup(LetterDocBookGroup letterDocBookGroup) {
		this.letterDocBookGroup = letterDocBookGroup;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public Boolean getIsOtherIssueOrgan() {
		return isOtherIssueOrgan;
	}
	public void setIsOtherIssueOrgan(Boolean isOtherIssueOrgan) {
		this.isOtherIssueOrgan = isOtherIssueOrgan;
	}
	public String getOtherIssueOrgan() {
		return otherIssueOrgan;
	}
	public void setOtherIssueOrgan(String otherIssueOrgan) {
		this.otherIssueOrgan = otherIssueOrgan;
	}
	public String getSendSMS() {
		return sendSMS;
	}
	public void setSendSMS(String sendSMS) {
		this.sendSMS = sendSMS;
	}
	
}
