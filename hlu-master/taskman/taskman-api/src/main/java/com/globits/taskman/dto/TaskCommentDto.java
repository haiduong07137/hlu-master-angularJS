package com.globits.taskman.dto;

import java.util.HashSet;
import java.util.Set;

import com.globits.core.domain.Department;
import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.TaskComment;

public class TaskCommentDto extends BaseObjectDto{

	private static final long serialVersionUID = -2084470670652775397L;
	private ParticipateDto participate;
	private String comment;
	private String filePath;
	private Set<CommentFileAttachmentDto> attachments = new HashSet<CommentFileAttachmentDto>();
	private String userName;//Người thực hiện viết comment (vì 1 phòng ban có thể có nhiều người tham gia, nên cần xác định đúng ai là người viết comment)
	private Boolean hasEditCommentPermission = false;//Dùng để xác định xem người dùng hiện tại có thể sửa comment này hay k
	public ParticipateDto getParticipate() {
		return participate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setParticipate(ParticipateDto participate) {
		this.participate = participate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Set<CommentFileAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<CommentFileAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public Boolean getHasEditCommentPermission() {
		return hasEditCommentPermission;
	}

	public void setHasEditCommentPermission(Boolean hasEditCommentPermission) {
		this.hasEditCommentPermission = hasEditCommentPermission;
	}

	public TaskCommentDto() {
		
	}
	
	public TaskCommentDto(TaskComment domain) {
		if(domain.getParticipate()!=null) {
			this.participate = new ParticipateDto();
			this.participate.setId(domain.getParticipate().getId());
			if(domain.getParticipate().getDepartment()!=null) {
				this.participate.setDepartment(new DepartmentDto());
				Department department = domain.getParticipate().getDepartment();
				this.participate.getDepartment().setCode(department.getCode());
				this.participate.getDepartment().setName(department.getName());
				this.participate.getDepartment().setId(department.getId());
			}
			this.participate.setParticipateType(domain.getParticipate().getParticipateType());
			this.participate.setDisplayName(domain.getParticipate().getDisplayName());			
		}
		if (domain.getAttachments() != null) {
			for (CommentFileAttachment att : domain.getAttachments()) {
				CommentFileAttachmentDto attDto = new CommentFileAttachmentDto(att);
				attDto.setTaskComment(new TaskCommentDto());
				attDto.getTaskComment().setComment(att.getTaskComment().getComment());
				attDto.getTaskComment().setId(att.getTaskComment().getId());

				if (att.getFile() != null) {
					FileDescriptionDto fileDescription = new FileDescriptionDto(att.getFile());
					attDto.setFile(fileDescription);
				}
				this.getAttachments().add(attDto);
			}
		}
		this.createDate = domain.getCreateDate();
		this.createdBy = domain.getCreatedBy();
		this.comment = domain.getComment();
		this.filePath = domain.getFilePath();
		this.userName = domain.getUserName();
		this.setId(domain.getId());
	}
}
