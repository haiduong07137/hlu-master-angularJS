package com.globits.letter.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.dto.OrganizationDto;
import com.globits.letter.domain.LetterDocument;
import com.globits.letter.domain.LetterDocumentAttachment;
import com.globits.letter.domain.LetterDocumentUser;
import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.domain.LetterOutDocument;
import com.globits.taskman.dto.TaskDto;

public class LetterDocumentDto extends BaseObjectDto {
	private static final long serialVersionUID = 1L;
	private String title;
	private String description;
	private String docCode;// Số vào sổ
	private String signedPerson;// Người ký
	private String signedPost;// Chức vụ
	private Integer documentOrderNoByBook;// Số đi theo sổ
	private Integer numberOfPages;// Số tờ

	private String docOriginCode;// Số hiệu văn bản đến
	private String refDocOriginCode;// Phúc đáp công văn

	private Date registeredDate;//Ngày vào sổ
	private String briefNote;// Tóm tắt nội dung văn bản
	private Integer typeOfClass;
	private OrganizationDto editOrgan;// Đơn vị soạn thảo văn bản
	private OrganizationDto issueOrgan;// Đơn vị ban hành văn bản
	private TaskDto task;// Hồ sơ xử lý văn bản
	private List<LetterDocumentAttachmentDto> attachments = new ArrayList<LetterDocumentAttachmentDto>();
	private List<LetterDocumentUserDto> userPermission;
	private Boolean hasViewPermission = true;
	private Boolean hasNextPermission = false;
	private Boolean hasBackPermission = false;
	private Boolean hasEditPermission = false;
	private Boolean hasDeletePermission = false;
	private Boolean hasFinishPermission = false;
	
	private Boolean hasManagerRole = false; 	//Vai trò trưởng phòng
	private Boolean  hasClerkRole = false;		//Vai trò văn thư
	private Boolean  hasFowardRole = false;		//Vai trò phân luồng
	private Boolean  hasAssignerRole = false;	//Vai trò giao xử lý
	private Boolean  hasChairmanRole = false;	//Vai trò chủ trì
	private Boolean  hasProcessRole = false;	//Vai trò tham gia
	private Boolean  hasDraftersRole = false;	//Vai trò chuyên viên
	private Boolean  hasChiefOfStaffRole = false;	//Vai trò chuyên viên
	private Boolean isLimitedRead;
	
	public Boolean getHasChiefOfStaffRole() {
		return hasChiefOfStaffRole;
	}

	public void setHasChiefOfStaffRole(Boolean hasChiefOfStaffRole) {
		this.hasChiefOfStaffRole = hasChiefOfStaffRole;
	}

	public Boolean getHasDraftersRole() {
		return hasDraftersRole;
	}

	public void setHasDraftersRole(Boolean hasDraftersRole) {
		this.hasDraftersRole = hasDraftersRole;
	}

	public Boolean getHasManagerRole() {
		return hasManagerRole;
	}

	public void setHasManagerRole(Boolean hasManagerRole) {
		this.hasManagerRole = hasManagerRole;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Boolean getHasClerkRole() {
		return hasClerkRole;
	}

	public void setHasClerkRole(Boolean hasClerkRole) {
		this.hasClerkRole = hasClerkRole;
	}

	public Boolean getHasFowardRole() {
		return hasFowardRole;
	}

	public void setHasFowardRole(Boolean hasFowardRole) {
		this.hasFowardRole = hasFowardRole;
	}

	public Boolean getHasAssignerRole() {
		return hasAssignerRole;
	}

	public void setHasAssignerRole(Boolean hasAssignerRole) {
		this.hasAssignerRole = hasAssignerRole;
	}

	public Boolean getHasChairmanRole() {
		return hasChairmanRole;
	}

	public void setHasChairmanRole(Boolean hasChairmanRole) {
		this.hasChairmanRole = hasChairmanRole;
	}

	public Boolean getHasProcessRole() {
		return hasProcessRole;
	}

	public void setHasProcessRole(Boolean hasProcessRole) {
		this.hasProcessRole = hasProcessRole;
	}

	public Boolean getHasViewPermission() {
		return hasViewPermission;
	}

	public void setHasViewPermission(Boolean hasViewPermission) {
		this.hasViewPermission = hasViewPermission;
	}

	public Boolean getHasNextPermission() {
		return hasNextPermission;
	}

	public void setHasNextPermission(Boolean hasNextPermission) {
		this.hasNextPermission = hasNextPermission;
	}

	public Boolean getHasBackPermission() {
		return hasBackPermission;
	}

	public void setHasBackPermission(Boolean hasBackPermission) {
		this.hasBackPermission = hasBackPermission;
	}

	public Boolean getHasEditPermission() {
		return hasEditPermission;
	}

	public void setHasEditPermission(Boolean hasEditPermission) {
		this.hasEditPermission = hasEditPermission;
	}

	public Boolean getHasDeletePermission() {
		return hasDeletePermission;
	}

	public void setHasDeletePermission(Boolean hasDeletePermission) {
		this.hasDeletePermission = hasDeletePermission;
	}

	public Boolean getHasFinishPermission() {
		return hasFinishPermission;
	}

	public void setHasFinishPermission(Boolean hasFinishPermission) {
		this.hasFinishPermission = hasFinishPermission;
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

	public OrganizationDto getEditOrgan() {
		return editOrgan;
	}

	public void setEditOrgan(OrganizationDto editOrgan) {
		this.editOrgan = editOrgan;
	}

	public OrganizationDto getIssueOrgan() {
		return issueOrgan;
	}

	public void setIssueOrgan(OrganizationDto issueOrgan) {
		this.issueOrgan = issueOrgan;
	}

	public TaskDto getTask() {
		return task;
	}

	public void setTask(TaskDto task) {
		this.task = task;
	}

	public List<LetterDocumentAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<LetterDocumentAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public List<LetterDocumentUserDto> getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(List<LetterDocumentUserDto> userPermission) {
		this.userPermission = userPermission;
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

	public String getRefDocOriginCode() {
		return refDocOriginCode;
	}

	public void setRefDocOriginCode(String refDocOriginCode) {
		this.refDocOriginCode = refDocOriginCode;
	}

	public Integer getTypeOfClass() {
		return typeOfClass;
	}

	public void setTypeOfClass(Integer typeOfClass) {
		this.typeOfClass = typeOfClass;
	}

	public Boolean getIsLimitedRead() {
		return isLimitedRead;
	}

	public void setIsLimitedRead(Boolean isLimitedRead) {
		this.isLimitedRead = isLimitedRead;
	}

	public LetterDocumentDto() {

	}

	public LetterDocumentDto(LetterDocument entity) {
		if (entity != null) {
			this.setId(entity.getId());
			this.title = entity.getTitle();
			this.description = entity.getDescription();
			this.docCode = entity.getDocCode();
			this.docOriginCode = entity.getDocOriginCode();
			this.refDocOriginCode = entity.getRefDocOriginCode();
			this.editOrgan = new OrganizationDto();
			this.registeredDate = entity.getRegisteredDate();
			this.briefNote = entity.getBriefNote();
			this.signedPerson = entity.getSignedPerson();
			this.signedPost = entity.getSignedPost();
			this.documentOrderNoByBook = entity.getDocumentOrderNoByBook();
			this.numberOfPages = entity.getNumberOfPages();
			this.isLimitedRead = entity.getIsLimitedRead();
			if (entity instanceof LetterInDocument) {
				this.setTypeOfClass(0);
			}else if (entity instanceof LetterOutDocument) {
				this.setTypeOfClass(1);
			}
			
			if (entity.getEditOrgan() != null) {
				this.editOrgan = new OrganizationDto();
				this.editOrgan.setId(entity.getEditOrgan().getId());
				this.editOrgan.setName(entity.getEditOrgan().getName());
				this.editOrgan.setCode(entity.getEditOrgan().getCode());
			}
			if (entity.getIssueOrgan() != null) {
				this.issueOrgan = new OrganizationDto();
				this.issueOrgan.setId(entity.getIssueOrgan().getId());
				this.issueOrgan.setName(entity.getIssueOrgan().getName());
				this.issueOrgan.setCode(entity.getIssueOrgan().getCode());
			}
			if (entity.getTask() != null) {
				this.task = new TaskDto(entity.getTask());
			}
			if (entity.getAttachments() != null) {
				for (LetterDocumentAttachment att : entity.getAttachments()) {
					LetterDocumentAttachmentDto attDto = new LetterDocumentAttachmentDto();
					attDto.setDocument(new LetterDocumentDto());
					attDto.getDocument().setDocCode(att.getDocument().getDocCode());
					attDto.getDocument().setId(att.getDocument().getId());

					if (att.getFile() != null) {
						FileDescriptionDto fileDescription = new FileDescriptionDto(att.getFile());
						attDto.setFile(fileDescription);
					}
					this.getAttachments().add(attDto);
				}
			}
			if(entity.getUserPermission()!=null) {
				this.userPermission = new ArrayList<LetterDocumentUserDto>();
				for(LetterDocumentUser u : entity.getUserPermission()) {
					LetterDocumentUserDto uDto = new LetterDocumentUserDto(u);
					this.userPermission.add(uDto);
				}
			}
		}
	}
}
