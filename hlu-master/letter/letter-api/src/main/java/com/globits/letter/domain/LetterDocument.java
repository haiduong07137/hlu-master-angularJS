package com.globits.letter.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Organization;
import com.globits.taskman.domain.Task;
@Entity
@Table(name = "tbl_letter_document")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "document_type")
public abstract class LetterDocument extends BaseObject{
	private static final long serialVersionUID = -8531248326767152584L;
	@Column(name="title")
	private String title;
	@Column(name="description",length = 1000)
	private String description;
	@Column(name="doc_code")
	private String docCode;//Số vào sổ
	@Column(name="signed_person")
	private String signedPerson;//Người ký
	@Column(name="signed_post")
	private String signedPost;//Chức vụ
	@Column(name="document_order_no_by_book")
	private Integer documentOrderNoByBook;//Số đi theo sổ
	@Column(name="number_of_pages")
	private Integer numberOfPages;//Số tờ	
	@Column(name="registered_date")
	private Date registeredDate;//Ngày vào sổ
	@Column(name="doc_origin_code")
	private String docOriginCode;//Số hiệu văn bản đến
	@Column(name="ref_doc_origin_code")
	private String refDocOriginCode;//Phúc đáp công văn
	
	@ManyToOne
	@JoinColumn(name="letter_document_type_id")
	private LetterDocumentType letterDocumentType;//Loại văn bản
	
	@ManyToOne
	@JoinColumn(name="letter_doc_field_id")
	private LetterDocField letterDocField;//Lĩnh vực văn bản
	
	@ManyToOne
	@JoinColumn(name="letter_doc_priority_id")
	private LetterDocPriority letterDocPriority;//Độ khẩn văn bản
	
	@ManyToOne
	@JoinColumn(name="letter_doc_security_level_id")
	private LetterDocSecurityLevel letterDocSecurityLevel;//Độ mật văn bản
	
	@Column(name="brief_note",length = 1000)
	private String briefNote;//Tóm tắt nội dung văn bản - trích yếu
	@Column(name="is_limited_read")
	private Boolean isLimitedRead;
	@ManyToOne
	@JoinColumn(name="edit_organ_id")
	private Organization editOrgan;//Đơn vị soạn thảo văn bản
	@ManyToOne
	@JoinColumn(name="issue_organ_id")
	private Organization issueOrgan;//Cơ quan ban hành văn bản
	@OneToMany(mappedBy = "letterDocument", cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<LetterDocumentUser> userPermission;//Nếu trường isLimitedRead = true thì kiểm tra danh sách này
	
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<LetterDocumentAttachment> attachments = new HashSet<LetterDocumentAttachment>();
	@ManyToOne(cascade=CascadeType.ALL, optional=true)
	@JoinColumn(name="task_id")
	  @ElementCollection
	  @OrderBy("currentStep.id ASC")
	private Task task;//Hồ sơ xử lý văn bản
	
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getDocOriginCode() {
		return docOriginCode;
	}
	public void setDocOriginCode(String docOriginCode) {
		this.docOriginCode = docOriginCode;
	}
	public String getBriefNote() {
		return briefNote;
	}
	public void setBriefNote(String briefNote) {
		this.briefNote = briefNote;
	}
	public Organization getEditOrgan() {
		return editOrgan;
	}
	public void setEditOrgan(Organization editOrgan) {
		this.editOrgan = editOrgan;
	}
	public Organization getIssueOrgan() {
		return issueOrgan;
	}
	public void setIssueOrgan(Organization issueOrgan) {
		this.issueOrgan = issueOrgan;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Set<LetterDocumentAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<LetterDocumentAttachment> attachments) {
		this.attachments = attachments;
	}
	public String getRefDocOriginCode() {
		return refDocOriginCode;
	}
	public void setRefDocOriginCode(String refDocOriginCode) {
		this.refDocOriginCode = refDocOriginCode;
	}
	public String getSignedPerson() {
		return signedPerson;
	}
	public void setSignedPerson(String signedPerson) {
		this.signedPerson = signedPerson;
	}
	public String getSignedPost() {
		return signedPost;
	}
	public void setSignedPost(String signedPost) {
		this.signedPost = signedPost;
	}
	public Integer getDocumentOrderNoByBook() {
		return documentOrderNoByBook;
	}
	public void setDocumentOrderNoByBook(Integer documentOrderNoByBook) {
		this.documentOrderNoByBook = documentOrderNoByBook;
	}
	public Integer getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	
	public Boolean getIsLimitedRead() {
		return isLimitedRead;
	}
	public void setIsLimitedRead(Boolean isLimitedRead) {
		this.isLimitedRead = isLimitedRead;
	}
	
	public Set<LetterDocumentUser> getUserPermission() {
		return userPermission;
	}
	public void setUserPermission(Set<LetterDocumentUser> userPermission) {
		this.userPermission = userPermission;
	}
	
	public LetterDocumentType getLetterDocumentType() {
		return letterDocumentType;
	}
	public void setLetterDocumentType(LetterDocumentType letterDocumentType) {
		this.letterDocumentType = letterDocumentType;
	}
	
	
	public LetterDocField getLetterDocField() {
		return letterDocField;
	}
	public void setLetterDocField(LetterDocField letterDocField) {
		this.letterDocField = letterDocField;
	}
	
	
	public LetterDocPriority getLetterDocPriority() {
		return letterDocPriority;
	}
	public void setLetterDocPriority(LetterDocPriority letterDocPriority) {
		this.letterDocPriority = letterDocPriority;
	}
	public LetterDocSecurityLevel getLetterDocSecurityLevel() {
		return letterDocSecurityLevel;
	}
	public void setLetterDocSecurityLevel(LetterDocSecurityLevel letterDocSecurityLevel) {
		this.letterDocSecurityLevel = letterDocSecurityLevel;
	}
	public LetterDocument() {
		
	}
}
