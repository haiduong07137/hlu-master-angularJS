package com.globits.letter.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.security.domain.User;

@Entity
@Table(name = "tbl_view_document_user")
@XmlRootElement
public class ViewDocumentUser extends BaseObject {
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name="document_id")
	private LetterDocument letterDocument;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LetterDocument getLetterDocument() {
		return letterDocument;
	}
	public void setLetterDocument(LetterDocument letterDocument) {
		this.letterDocument = letterDocument;
	}
}
