package com.globits.letter.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;
@Entity
@Table(name = "tbl_letter_document_attachment")
public class LetterDocumentAttachment extends BaseObject{
	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	@ManyToOne
	@JoinColumn(name="document_id")
	private LetterDocument document;
	public FileDescription getFile() {
		return file;
	}
	public void setFile(FileDescription file) {
		this.file = file;
	}
	public LetterDocument getDocument() {
		return document;
	}
	public void setDocument(LetterDocument document) {
		this.document = document;
	}
	
	
}
