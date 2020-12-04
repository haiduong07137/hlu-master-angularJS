package com.globits.document.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;
@Entity
@Table(name="tbl_document_attachment")
public class DocumentAttachment extends BaseObject{
	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	@ManyToOne
	@JoinColumn(name="document_id")
	Document document;
	public FileDescription getFile() {
		return file;
	}
	public void setFile(FileDescription file) {
		this.file = file;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	
}
