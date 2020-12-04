package com.globits.letter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

@Entity
@Table(name = "tbl_letter_doc_book")
@XmlRootElement
public class LetterDocBook extends BaseObject{//Sổ văn bản
	@Column(name="name")
	private String name;
	@Column(name="code")
	private String code;	
	@Column(name="doc_app_type")
	private String docAppType;//Là ứng dụng nào - InDocument, OutDocument, ...
	
	@Column(name="current_number")
	private Integer currentNumber =0;
	
	@ManyToOne
	@JoinColumn(name="doc_book_group_id")
	private LetterDocBookGroup docBookGroup;	//nhóm vào sổ văn bản
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDocAppType() {
		return docAppType;
	}
	public void setDocAppType(String docAppType) {
		this.docAppType = docAppType;
	}
	public LetterDocBookGroup getDocBookGroup() {
		return docBookGroup;
	}
	public void setDocBookGroup(LetterDocBookGroup docBookGroup) {
		this.docBookGroup = docBookGroup;
	}
	public Integer getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}	
	
}
