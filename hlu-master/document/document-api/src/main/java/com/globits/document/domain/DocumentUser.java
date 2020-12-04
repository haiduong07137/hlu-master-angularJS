package com.globits.document.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.security.domain.User;
@Entity
@Table(name="tbl_document_user")
public class DocumentUser extends BaseObject{
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="document_id")
	private Document document;
	public User getUser() {
		return user;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public void setUser(User user) {
		this.user = user;
	}


	
	
}
