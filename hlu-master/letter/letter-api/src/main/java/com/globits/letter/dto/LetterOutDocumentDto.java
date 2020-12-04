package com.globits.letter.dto;

import java.util.Date;
import java.util.List;

import com.globits.letter.domain.LetterOutDocument;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskOwnerDto;

public class LetterOutDocumentDto extends LetterDocumentDto{
private static final long serialVersionUID = 1L;
	
	private Date deliveredDate;//Ngày chuyển đến
	
	private Date issuedDate;//Ngày ban hành
	
	private Date officialDate;//Ngày hiệu lực
	
	private Date expiredDate;//Ngày hết hạn

	private Boolean isOtherIssueOrgan; // Nếu là cơ quan ban hành khác (nhập tay)
	
	private String otherIssueOrgan; // cơ quan ban hành khác (nhập tay)
	
	private String otherIssuePerson;
	
	private TaskOwnerDto editorUnit; // đơn vị soạn thảo

	private String sendEmail;//gửi thông báo email

	private String sendSMS;//gửi thông báo SMS
	
	private LetterDocBookGroupDto letterDocBookGroup;	//Nhóm sổ văn bản
	
	private LetterDocBookDto letterDocBook;	//Sổ văn bản
	
	private LetterDocumentTypeDto letterDocumentType;//Loại văn bản
	
	private LetterDocFieldDto letterDocField;//Lĩnh vực văn bản
	
	private LetterDocPriorityDto letterDocPriority;//Độ khẩn văn bản
	
	private LetterDocSecurityLevelDto letterDocSecurityLevel;//Độ mật văn bản

	private TaskOwnerDto chiefOfStaff;//Chánh văn phòng
	
	private TaskOwnerDto signer;//người ký
	/* Đường công văn
	 * 1: Bưu điện
	 * 2: Chuyển trực tiếp
	 * 3: Fax
	 * 4: Email
	 */
	private Integer letterLine;//Đường công văn
	
	/* Loại lưu văn bản
	 * 1: Bản gốc
	 * 2: Bản sao chép
	 * 3: Bản mềm
	 */
	private Integer saveLetterType;//Đường công văn
	
	private List<ParticipateDto> participateFowards;

	private Date create;//ngày đăng
	private Date modify;//Ngày chỉnh sửa
	private Boolean promulgate;//Trạng thái văn bản ban hành: 1: đã ban hành, 0: chưa ban hành
	
	public TaskOwnerDto getSigner() {
		return signer;
	}
	public void setSigner(TaskOwnerDto signer) {
		this.signer = signer;
	}
	public TaskOwnerDto getChiefOfStaff() {
		return chiefOfStaff;
	}
	public void setChiefOfStaff(TaskOwnerDto chiefOfStaff) {
		this.chiefOfStaff = chiefOfStaff;
	}
	public TaskOwnerDto getEditorUnit() {
		return editorUnit;
	}
	public void setEditorUnit(TaskOwnerDto editorUnit) {
		this.editorUnit = editorUnit;
	}
	public Date getCreate() {
		return create;
	}
	public void setCreate(Date create) {
		this.create = create;
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
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getSendSMS() {
		return sendSMS;
	}
	public void setSendSMS(String sendSMS) {
		this.sendSMS = sendSMS;
	}
	public LetterDocBookGroupDto getLetterDocBookGroup() {
		return letterDocBookGroup;
	}
	public void setLetterDocBookGroup(LetterDocBookGroupDto letterDocBookGroup) {
		this.letterDocBookGroup = letterDocBookGroup;
	}
	public LetterDocBookDto getLetterDocBook() {
		return letterDocBook;
	}
	public void setLetterDocBook(LetterDocBookDto letterDocBook) {
		this.letterDocBook = letterDocBook;
	}
	public LetterDocumentTypeDto getLetterDocumentType() {
		return letterDocumentType;
	}
	public void setLetterDocumentType(LetterDocumentTypeDto letterDocumentType) {
		this.letterDocumentType = letterDocumentType;
	}
	public LetterDocFieldDto getLetterDocField() {
		return letterDocField;
	}
	public void setLetterDocField(LetterDocFieldDto letterDocField) {
		this.letterDocField = letterDocField;
	}
	public LetterDocPriorityDto getLetterDocPriority() {
		return letterDocPriority;
	}
	public void setLetterDocPriority(LetterDocPriorityDto letterDocPriority) {
		this.letterDocPriority = letterDocPriority;
	}
	public LetterDocSecurityLevelDto getLetterDocSecurityLevel() {
		return letterDocSecurityLevel;
	}
	public void setLetterDocSecurityLevel(LetterDocSecurityLevelDto letterDocSecurityLevel) {
		this.letterDocSecurityLevel = letterDocSecurityLevel;
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
	public Date getModify() {
		return modify;
	}
	public void setModify(Date modify) {
		this.modify = modify;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<ParticipateDto> getParticipateFowards() {
		return participateFowards;
	}
	public void setParticipateFowards(List<ParticipateDto> participateFowards) {
		this.participateFowards = participateFowards;
	}
	public String getOtherIssuePerson() {
		return otherIssuePerson;
	}
	public void setOtherIssuePerson(String otherIssuePerson) {
		this.otherIssuePerson = otherIssuePerson;
	}
	
	public Boolean getPromulgate() {
		return promulgate;
	}
	public void setPromulgate(Boolean promulgate) {
		this.promulgate = promulgate;
	}
	public LetterOutDocumentDto() {
		super();
	}
	public LetterOutDocumentDto(LetterOutDocument entity) {
		super(entity);
		this.setDeliveredDate(entity.getDeliveredDate());
		this.setExpiredDate(entity.getExpiredDate());
		this.setIssuedDate(entity.getIssuedDate());
		this.setOfficialDate(entity.getOfficialDate());
		this.setLetterLine(entity.getLetterLine());
		this.setSaveLetterType(entity.getSaveLetterType());
		this.isOtherIssueOrgan = entity.getIsOtherIssueOrgan();
		this.otherIssueOrgan = entity.getOtherIssueOrgan();
		this.otherIssuePerson = entity.getOtherIssuePerson();
		this.sendEmail = entity.getSendEmail();
		this.sendSMS = entity.getSendSMS();
		this.createdBy = entity.getCreatedBy();
		this.modifiedBy = entity.getModifiedBy();
		this.create = entity.getCreateDate().toDate();
		this.modify = entity.getModifyDate().toDate();
		this.promulgate = entity.getPromulgate();
		
		if(entity.getSigner() != null) {
			this.signer = new TaskOwnerDto(entity.getSigner());
		}
		if(entity.getChiefOfStaff() != null) {
			this.chiefOfStaff = new TaskOwnerDto(entity.getChiefOfStaff());
		}
		if(entity.getEditorUnit() != null) {
			this.editorUnit = new TaskOwnerDto(entity.getEditorUnit());
		}
		if(entity.getLetterDocField() != null) {
			this.letterDocField = new LetterDocFieldDto();
			this.letterDocField.setId(entity.getLetterDocField().getId());
			this.letterDocField.setName(entity.getLetterDocField().getName());
			this.letterDocField.setCode(entity.getLetterDocField().getCode());
		}
		if(entity.getLetterDocPriority() != null) {
			this.letterDocPriority = new LetterDocPriorityDto();
			this.letterDocPriority.setId(entity.getLetterDocPriority().getId());
			this.letterDocPriority.setName(entity.getLetterDocPriority().getName());
			this.letterDocPriority.setCode(entity.getLetterDocPriority().getCode());
		}
		if(entity.getLetterDocSecurityLevel() != null) {
			this.letterDocSecurityLevel = new LetterDocSecurityLevelDto();
			this.letterDocSecurityLevel.setId(entity.getLetterDocSecurityLevel().getId());
			this.letterDocSecurityLevel.setName(entity.getLetterDocSecurityLevel().getName());
			this.letterDocSecurityLevel.setCode(entity.getLetterDocSecurityLevel().getCode());
		}
		if(entity.getLetterDocumentType() != null) {
			this.letterDocumentType = new LetterDocumentTypeDto();
			this.letterDocumentType.setId(entity.getLetterDocumentType().getId());
			this.letterDocumentType.setName(entity.getLetterDocumentType().getName());
			this.letterDocumentType.setCode(entity.getLetterDocumentType().getCode());
		}
		if (entity.getLetterDocBook() != null) {
			this.letterDocBook = new LetterDocBookDto();
			this.letterDocBook.setId(entity.getLetterDocBook().getId());
			this.letterDocBook.setCode(entity.getLetterDocBook().getCode());
			this.letterDocBook.setName(entity.getLetterDocBook().getName());
			this.letterDocBook.setDocAppType(entity.getLetterDocBook().getDocAppType());
		}
		if (entity.getLetterDocBookGroup() != null) {
			this.letterDocBookGroup = new LetterDocBookGroupDto();
			this.letterDocBookGroup.setId(entity.getLetterDocBookGroup().getId());
			this.letterDocBookGroup.setCode(entity.getLetterDocBookGroup().getCode());
			this.letterDocBookGroup.setName(entity.getLetterDocBookGroup().getName());
		}
		
	}
}
