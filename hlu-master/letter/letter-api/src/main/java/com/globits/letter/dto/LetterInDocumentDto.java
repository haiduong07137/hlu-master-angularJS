package com.globits.letter.dto;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.utils.LetterUtils;
import com.globits.taskman.dto.TaskOwnerDto;


public class LetterInDocumentDto extends LetterDocumentDto{
	private static final long serialVersionUID = 1L;
	
	private Date deliveredDate;//Ngày chuyển đến
	
	private Date issuedDate;//Ngày ban hành
	
	private Date officialDate;//Ngày hiệu lực
	
	private Date expiredDate;//Ngày hết hạn

	private Boolean expiredDateStatus;//Trạng thái văn bản đã xong hay chưa
	
	private Long dayLeft;//đếm ngày hết hạn

	private Boolean isOtherIssueOrgan; // Nếu là cơ quan ban hành khác (nhập tay)
	
	private String otherIssueOrgan; // cơ quan ban hành khác (nhập tay)

	private String sendEmail;//gửi thông báo email

	private String sendSMS;//gửi thông báo SMS
	
	private LetterDocBookGroupDto letterDocBookGroup;	//Nhóm sổ văn bản
	
	private LetterDocBookDto letterDocBook;	//Sổ văn bản
	
	private LetterDocumentTypeDto letterDocumentType;//Loại văn bản
	
	private LetterDocFieldDto letterDocField;//Lĩnh vực văn bản
	
	private LetterDocPriorityDto letterDocPriority;//Độ khẩn văn bản
	
	private LetterDocSecurityLevelDto letterDocSecurityLevel;//Độ mật văn bản
	
	private Boolean statusLetter;

	private TaskOwnerDto chiefOfStaff;//Chánh văn phòng
	
	public TaskOwnerDto getChiefOfStaff() {
		return chiefOfStaff;
	}

	public void setChiefOfStaff(TaskOwnerDto chiefOfStaff) {
		this.chiefOfStaff = chiefOfStaff;
	}

	public Boolean getStatusLetter() {
		return statusLetter;
	}

	public void setStatusLetter(Boolean statusLetter) {
		this.statusLetter = statusLetter;
	}

	public Boolean getExpiredDateStatus() {
		return expiredDateStatus;
	}

	public Boolean isExpiredDateStatus() {
		return expiredDateStatus;
	}

	public void setExpiredDateStatus(Boolean expiredDateStatus) {
		this.expiredDateStatus = expiredDateStatus;
	}

	public Long getDayLeft() {
		return dayLeft;
	}

	public void setDayLeft(Long dayLeft) {
		this.dayLeft = dayLeft;
	}

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

	private Date create;//ngày đăng
	private Date modify;//Ngày chỉnh sửa
	
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
	
	public LetterDocBookDto getLetterDocBook() {
		return letterDocBook;
	}

	public void setLetterDocBook(LetterDocBookDto letterDocBook) {
		this.letterDocBook = letterDocBook;
	}

	public LetterDocBookGroupDto getLetterDocBookGroup() {
		return letterDocBookGroup;
	}

	public void setLetterDocBookGroup(LetterDocBookGroupDto letterDocBookGroup) {
		this.letterDocBookGroup = letterDocBookGroup;
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

	public Date getModify() {
		return modify;
	}

	public void setModify(Date modify) {
		this.modify = modify;
	}

	public LetterInDocumentDto() {
		
	}
	
	public LetterInDocumentDto(LetterInDocument entity) {
		super(entity);
		this.setDeliveredDate(entity.getDeliveredDate());
		this.setExpiredDate(entity.getExpiredDate());
		this.setIssuedDate(entity.getIssuedDate());
		this.setOfficialDate(entity.getOfficialDate());
		this.setLetterLine(entity.getLetterLine());
		this.setSaveLetterType(entity.getSaveLetterType());
		this.isOtherIssueOrgan = entity.getIsOtherIssueOrgan();
		this.otherIssueOrgan = entity.getOtherIssueOrgan();
		this.sendEmail = entity.getSendEmail();
		this.sendSMS = entity.getSendSMS();
		this.createdBy=entity.getCreatedBy();
		this.modifiedBy=entity.getModifiedBy();
		this.create=entity.getCreateDate().toDate();
		this.modify=entity.getModifyDate().toDate();
		if(entity.getExpiredDate()!=null && entity.getTask().getCurrentStep().getId() != LetterConstant.LetterInStep5) {
			Date date = LetterUtils.getStartOfDay(new Date());
			if(date.getTime() < entity.getExpiredDate().getTime()) {
				this.expiredDateStatus = true;//Chưa quá hạn xử lý
				this.dayLeft=TimeUnit.MILLISECONDS.toDays(entity.getExpiredDate().getTime() - date.getTime());
			}
			if(date.getTime() == entity.getExpiredDate().getTime()) {
				this.expiredDateStatus = null;//Đến hạn xử lý
			}
			if(date.getTime() > entity.getExpiredDate().getTime()) {
				this.expiredDateStatus = false;//Quá hạn xử lý
				this.dayLeft=TimeUnit.MILLISECONDS.toDays(date.getTime() - entity.getExpiredDate().getTime());
			}
		}else if(entity != null && entity.getTask()!= null && entity.getTask().getCurrentStep() !=null && entity.getTask().getCurrentStep().getId() != null && entity.getTask().getCurrentStep().getId() == LetterConstant.LetterInStep5) {
			this.statusLetter = true;
		}
		if(entity.getChiefOfStaff() != null) {
			this.chiefOfStaff = new TaskOwnerDto(entity.getChiefOfStaff());
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
