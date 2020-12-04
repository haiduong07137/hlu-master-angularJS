package com.globits.taskman.domain;

import java.util.HashSet;
import java.util.Set;

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
@Entity
@Table(name = "tbl_task_comment")
@XmlRootElement
public class TaskComment extends BaseObject {
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="participate_id")
	private Participate participate;
	@Column(name="user_name")
	private String userName;//Người thực hiện viết comment (vì 1 phòng ban có thể có nhiều người tham gia, nên cần xác định đúng ai là người viết comment)
	@Column(name="comment")
	private String comment;
	@Column(name="file_path")
	private String filePath;
	
	@OneToMany(mappedBy = "taskComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<CommentFileAttachment> attachments = new HashSet<CommentFileAttachment>();

	
	public Participate getParticipate() {
		return participate;
	}
	public void setParticipate(Participate participate) {
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Set<CommentFileAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<CommentFileAttachment> attachments) {
		this.attachments = attachments;
	}
	
}
