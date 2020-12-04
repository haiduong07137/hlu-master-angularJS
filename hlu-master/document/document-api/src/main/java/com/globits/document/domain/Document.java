package com.globits.document.domain;

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

import org.hibernate.annotations.ManyToAny;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name="tbl_document")
public class Document extends BaseObject {
	private static final long serialVersionUID = -634205721156061338L;
	@Column(name="title")
	private String title;
	@Column(name="description",length = 1000)
	private String description;
	@Column(name="doc_code")
	private String docCode;//Số vào sổ
	@Column(name="summary")
	private String summary;
	@Column(name="is_limited_read")
	private Boolean isLimitedRead;
	@ManyToOne
	@JoinColumn(name="category_id")
	private DocumentCategory category;
	@OneToMany(mappedBy = "document", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DocumentAttachment> attachments = new HashSet<DocumentAttachment>();
	@OneToMany(mappedBy = "document", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DocumentUser> userPermission;
	
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public DocumentCategory getCategory() {
		return category;
	}

	public void setCategory(DocumentCategory category) {
		this.category = category;
	}

	public Set<DocumentAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<DocumentAttachment> attachments) {
		this.attachments = attachments;
	}

	public Set<DocumentUser> getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(Set<DocumentUser> userPermission) {
		this.userPermission = userPermission;
	}

	public Boolean getIsLimitedRead() {
		return isLimitedRead;
	}

	public void setIsLimitedRead(Boolean isLimitedRead) {
		this.isLimitedRead = isLimitedRead;
	}
	
}
