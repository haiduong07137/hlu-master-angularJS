package com.globits.letter.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Organization;
@Entity
@Table(name = "tbl_org_letter_document")
public class OrgLetterDocument extends BaseObject{
	@ManyToOne
	@JoinColumn(name="org_id")
	private Organization org;
	@ManyToOne
	@JoinColumn(name="document_id")	
	private LetterDocument document;
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	public LetterDocument getDocument() {
		return document;
	}
	public void setDocument(LetterDocument document) {
		this.document = document;
	}
	
	
}
