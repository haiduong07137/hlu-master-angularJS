package com.globits.letter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name = "tbl_letter_doc_book_group")
@XmlRootElement
public class LetterDocBookGroup extends BaseObject{	//Nhóm sổ văn bản
	@Column(name="name")
	private String name;
	@Column(name="code")
	private String code;
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
}
