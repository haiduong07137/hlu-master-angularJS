package com.globits.letter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.ManyToAny;

import com.globits.core.domain.BaseObject;
import com.globits.security.domain.User;

@Entity
@Table(name = "tbl_letter_document_user")
@XmlRootElement
public class LetterDocumentUser extends BaseObject {
	private static final long serialVersionUID = 1L;//Bảng phân quyền đọc cho User
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne(fetch = FetchType.EAGER)
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
